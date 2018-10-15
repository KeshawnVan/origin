package star.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.bean.DomElement;
import star.bean.Key;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public final class DomUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomUtil.class);

    public static Map<Key, Object> decode(DomElement domElement, InputStream inputStream, String xmlns) {
        Map<Key, Object> result = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        Nullable.of(xmlns).ifPresent(nameSpace -> {
            Map<String, String> config = new HashMap<>(2);
            config.put("ns", nameSpace);
            saxReader.getDocumentFactory().setXPathNamespaceURIs(config);
        });
        try {
            Document document = saxReader.read(inputStream);
            buildMap(domElement, result, document, null, document);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static void buildMap(DomElement domElement, Map<Key, Object> result, Node rootNode, Boolean isCollection, Node parentNode) {
        if (CollectionUtil.isNotEmpty(domElement.getDomElements())) {
            for (DomElement element : domElement.getDomElements()) {
                result.put(new Key(element.getTable(), element.getColumn(), element.getType()), buildValue(element, rootNode, element.getXpath(), isCollection, parentNode));
            }
        }
    }

    private static Object buildValue(DomElement domElement, Node rootNode, String xpath, Boolean isCollection, Node parentNode) {
        String nsXpath = formatXpath(xpath);
        return domElement.getCollection()
                ? selectSubNodes(rootNode, nsXpath, parentNode, domElement).stream().map(subNode -> buildNodeValue(subNode, domElement, nsXpath, rootNode)).collect(Collectors.toList())
                : buildNodeValue(getSingleNode(rootNode, nsXpath, isCollection, parentNode, domElement), domElement, nsXpath, rootNode);
    }

    private static String formatXpath(String xpath) {
        String prefix = xpath.substring(0, 3);
        String formatPrefix = prefix.equals("//@") ? prefix : prefix.substring(0, 1) + "/ns:" + prefix.substring(2, 3);
        String content = xpath.substring(3, xpath.length());
        int index = content.indexOf("/@");
        String formatContent = index > 0
                ? StringUtils.replaceAll(content.substring(0, index), "/", "/ns:") + content.substring(index, content.length())
                : StringUtils.replaceAll(content, "/", "/ns:");
        return StringUtils.replaceAll(formatPrefix + formatContent, "/ns:\\.\\.", "/..");
    }


    private static List<Node> selectSubNodes(Node rootNode, String xPath, Node parentNode, DomElement domElement) {
        if (domElement.getJoin()) {
            List<Node> joinNodes = rootNode.selectNodes(xPath);
            List<Tuple<Node, Node>> sourceAndUps = joinNodes.stream().map(node -> new Tuple<>(node, node)).collect(Collectors.toList());
            return Collections.singletonList(getJoinNode(parentNode, sourceAndUps));
        } else {
            return ((List<Node>) rootNode.selectNodes(xPath)).stream().filter(it -> isSubNode(parentNode, it)).collect(Collectors.toList());
        }
    }

    private static Node getJoinNode(Node parentNode, List<Tuple<Node, Node>> sourceAndUps) {
        List<Node> joinNodes = sourceAndUps.stream().map(sourceAndUp -> sourceAndUp._2).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(joinNodes)) {
            return null;
        }
        for (Tuple<Node, Node> sourceAndUp : sourceAndUps) {
            if (isSubNode(sourceAndUp._2, parentNode)) {
                return sourceAndUp._1;
            }
        }
        List<Tuple<Node, Node>> up = sourceAndUps.stream().map(sourceAndUp -> new Tuple<>(sourceAndUp._1, (Node) sourceAndUp._2.getParent())).collect(Collectors.toList());
        return getJoinNode(parentNode, up);
    }

    private static Node getSingleNode(Node nodeElement, String xPath, Boolean isCollection, Node parentNode, DomElement domElement) {
        return isCollection != null && isCollection
                ? selectSubNodes(nodeElement, xPath, parentNode, domElement).stream().findFirst().orElse(null)
                : nodeElement.selectSingleNode(xPath);
    }

    private static Object buildNodeValue(Node node, DomElement domElement, String xPath, Node rootNode) {
        if (node == null) {
            LOGGER.warn("cannot find node by xPath : [{}]", xPath);
            return null;
        }
        if (CollectionUtil.isNotEmpty(domElement.getDomElements())) {
            Map<Key, Object> resultMap = new HashMap<>();
            buildMap(domElement, resultMap, rootNode, domElement.getCollection(), node);
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
