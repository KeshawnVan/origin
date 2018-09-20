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
import java.util.Map;
import java.util.stream.Collectors;

public final class XmlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

    public static <T> T decode(InputStream inputStream, Class<T> type) {
        return decode(inputStream, type, null);
    }

    public static <T> T decode(InputStream inputStream, Class<T> type, Map<String, String> xPathMap) {
        T instance = ReflectionUtil.newInstance(type);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);
            ClassInfo classInfo = ClassUtil.getClassInfo(type);
            for (Field field : classInfo.getFields()) {
                setFieldValue(instance, document, field, null, xPathMap);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    private static <T> void setFieldValue(T instance, Node node, Field field, TypeWrapper parentType, Map<String, String> xPathMap) {
        String xPath = CollectionUtil.isEmpty(xPathMap)
                ? Nullable.of(field.getAnnotation(XPath.class)).map(XPath::value).orElse(null)
                : xPathMap.get(instance.getClass().getName() + "." + field.getName());
        Nullable.of(xPath).ifPresent(path -> ReflectionUtil.setField(instance, field, getFieldValue(node, field, path, parentType, xPathMap)));
    }

    private static Object getFieldValue(Node nodeElement, Field field, String xPath, TypeWrapper parentType, Map<String, String> xPathMap) {
        TypeWrapper typeWrapper = ReflectionUtil.typeParse(field.getGenericType());
        if (typeWrapper.isCollection()) {
            return selectSubNodes(nodeElement, xPath).stream().map(node -> buildNodeValue(node, typeWrapper, xPath, xPathMap)).collect(Collectors.toList());
        } else {
            Node node = getSingleNode(nodeElement, xPath, parentType);
            return buildNodeValue(node, typeWrapper, xPath, xPathMap);
        }
    }

    private static List<Node> selectSubNodes(Node parentNode, String xPath) {
        return ((List<Node>) parentNode.selectNodes(xPath)).stream().filter(it -> isSubNode(parentNode, it)).collect(Collectors.toList());
    }

    private static Node getSingleNode(Node nodeElement, String xPath, TypeWrapper parentType) {
        return parentType != null && parentType.isCollection()
                ? selectSubNodes(nodeElement, xPath).stream().findFirst().orElse(null)
                : nodeElement.selectSingleNode(xPath);
    }

    private static Object buildNodeValue(Node node, TypeWrapper typeWrapper, String xPath, Map<String, String> xPathMap) {
        if (node == null) {
            LOGGER.warn("cannot find node by xPath : [{}]", xPath);
            return null;
        }
        Class<?> classType = typeWrapper.isCollection() ? (Class<?>) typeWrapper.getGenericType()[0] : typeWrapper.getCls();
        if (classType.isAnnotationPresent(ModelElement.class)) {
            Object instance = ReflectionUtil.newInstance(classType);
            ClassInfo classInfo = ClassUtil.getClassInfo(classType);
            for (Field field : classInfo.getFields()) {
                setFieldValue(instance, node, field, typeWrapper, xPathMap);
            }
            return instance;
        } else {
            return JsonUtil.decodeJson(StringUtil.castJsonString(node.getText()), classType);
        }
    }

    private static boolean isSubNode(Node parentNode, Node node) {
        if (parentNode instanceof Document) {
            return true;
        }
        Node up = node.getParent();
        while (up != null) {
            if (up.equals(parentNode)) {
                return true;
            } else {
                up = up.getParent();
            }
        }
        return false;
    }
}
