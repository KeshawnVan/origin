package star.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.bean.DomElement;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class DomUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomUtil.class);

    public static Map<String, Object> decode(DomElement domElement, InputStream inputStream) {
        Map<String, Object> result = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);
            buildMap(domElement, result, document, null);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static void buildMap(DomElement domElement, Map<String, Object> result, Node node, Boolean isCollection) {
        if (CollectionUtil.isNotEmpty(domElement.getDomElements())) {
            for (DomElement element : domElement.getDomElements()) {
                result.put(element.getName(), buildValue(element, node, element.getXpath(), isCollection));
            }
        }
    }

    private static Object buildValue(DomElement domElement, Node node, String xpath, Boolean isCollection) {
        return domElement.getCollection()
                ? selectSubNodes(node, xpath).stream().map(subNode -> buildNodeValue(subNode, domElement, xpath)).collect(Collectors.toList())
                : buildNodeValue(getSingleNode(node, xpath, isCollection), domElement, xpath);
    }


    private static List<Node> selectSubNodes(Node parentNode, String xPath) {
        return ((List<Node>) parentNode.selectNodes(xPath)).stream().filter(it -> isSubNode(parentNode, it)).collect(Collectors.toList());
    }

    private static Node getSingleNode(Node nodeElement, String xPath, Boolean isCollection) {
        return isCollection != null && isCollection
                ? selectSubNodes(nodeElement, xPath).stream().findFirst().orElse(null)
                : nodeElement.selectSingleNode(xPath);
    }

    private static Object buildNodeValue(Node node, DomElement domElement, String xPath) {
        if (node == null) {
            LOGGER.warn("cannot find node by xPath : [{}]", xPath);
            return null;
        }
        if (CollectionUtil.isNotEmpty(domElement.getDomElements())) {
            Map<String, Object> resultMap = new HashMap<>();
            buildMap(domElement, resultMap, node, domElement.getCollection());
            return resultMap;
        } else {
            return node.getText();
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
