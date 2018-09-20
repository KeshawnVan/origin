package star.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.parser.ModelElement;
import star.annotation.parser.XPath;
import star.bean.ClassInfo;
import star.bean.TypeWrapper;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class XmlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

    public static <T> T decode(InputStream inputStream, Class<T> type) {
        T instance = ReflectionUtil.newInstance(type);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);
            ClassInfo classInfo = ClassUtil.getClassInfo(type);
            for (Field field : classInfo.getFields()) {
                setFieldValue(instance, document, field, null);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    private static <T> void setFieldValue(T instance, Node node, Field field, TypeWrapper parentType) {
        String xPath = Optional.ofNullable(field.getAnnotation(XPath.class)).map(XPath::value).orElseGet(() -> instance.getClass().getName() + "." + field.getName());
        ReflectionUtil.setField(instance, field, getValue(node, field, xPath, parentType));
    }

    private static Object getValue(Node nodeElement, Field field, String xPath, TypeWrapper parentType) {
        TypeWrapper typeWrapper = ReflectionUtil.typeParse(field.getGenericType());
        if (typeWrapper.isCollection()) {
            return selectNodes(nodeElement, xPath).stream().map(node -> buildFieldValue(node, typeWrapper, xPath)).collect(Collectors.toList());
        } else {
            Node node = getSingleNode(nodeElement, xPath, parentType);
            return buildFieldValue(node, typeWrapper, xPath);
        }
    }

    private static List<Node> selectNodes(Node parentNode, String xPath) {
        return ((List<Node>) parentNode.selectNodes(xPath)).stream().filter(it -> isTrace(parentNode, it)).collect(Collectors.toList());
    }

    private static Node getSingleNode(Node nodeElement, String xPath, TypeWrapper parentType) {
        return parentType != null && parentType.isCollection()
                ? selectNodes(nodeElement, xPath).stream().findFirst().orElse(null)
                : nodeElement.selectSingleNode(xPath);
    }

    private static Object buildFieldValue(Node node, TypeWrapper typeWrapper, String xPath) {
        if (node == null) {
            LOGGER.warn("cannot find node by xPath : [{}]", xPath);
            return null;
        }
        Class<?> classType = typeWrapper.isCollection() ? (Class<?>) typeWrapper.getGenericType()[0] : typeWrapper.getCls();
        if (classType.isAnnotationPresent(ModelElement.class)) {
            Object instance = ReflectionUtil.newInstance(classType);
            ClassInfo classInfo = ClassUtil.getClassInfo(classType);
            for (Field field : classInfo.getFields()) {
                setFieldValue(instance, node, field, typeWrapper);
            }
            return instance;
        } else {
            return JsonUtil.decodeJson(StringUtil.castJsonString(node.getText()), classType);
        }
    }

    private static boolean isTrace(Node parent, Node it) {
        if (parent instanceof Document) {
            return true;
        }
        Node up = it.getParent();
        while (up != null) {
            if (up.equals(parent)) {
                return true;
            } else {
                up = up.getParent();
            }
        }
        return false;
    }
}
