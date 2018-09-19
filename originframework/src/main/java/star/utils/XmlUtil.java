package star.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import star.annotation.parser.ModelElement;
import star.annotation.parser.XPath;
import star.bean.ClassInfo;
import star.bean.TypeWrapper;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class XmlUtil {
    public static <T> T decode(InputStream inputStream, Class<T> type) {
        T instance = ReflectionUtil.newInstance(type);
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(inputStream);
            ClassInfo classInfo = ClassUtil.getClassInfo(type);
            for (Field field : classInfo.getFields()) {
                setFieldValue(instance, document, field);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    private static <T> void setFieldValue(T instance, Node node, Field field) {
        Optional.ofNullable(field.getAnnotation(XPath.class)).map(XPath::value)
                .ifPresent(xPath -> ReflectionUtil.setField(instance, field, getValue(node, field, xPath))
        );
    }

    private static Object getValue(Node nodeElement, Field field, String xPath) {
        TypeWrapper typeWrapper = ReflectionUtil.typeParse(field.getGenericType());
        if (typeWrapper.isCollection()) {
            List<Node> selectNodes = nodeElement.selectNodes(xPath);
            return selectNodes.stream().map(node -> buildFieldValue(node, typeWrapper.getGenericType()[0])).collect(Collectors.toList());
        } else {
            Node node = nodeElement.selectSingleNode(xPath);
            return buildFieldValue(node, typeWrapper.getCls());
        }
    }

    private static Object buildFieldValue(Node node, Type type) {
        Class<?> classType = (Class) type;
        if (classType.isAnnotationPresent(ModelElement.class)) {
            Object instance = ReflectionUtil.newInstance(classType);
            ClassInfo classInfo = ClassUtil.getClassInfo(classType);
            for (Field field : classInfo.getFields()) {
                setFieldValue(instance, node, field);
            }
            return instance;
        } else {
            return JsonUtil.decodeJson(StringUtil.castJsonString(node.getText()), classType);
        }
    }
}
