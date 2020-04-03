package com.github.tuding.blindbox.infrastructure.client.payment;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {

    public static Map<String, String> xmlToMap(String xml) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        for (Element e : elementList)
            map.put(e.getName(), e.getText());
        return map;
    }
}
