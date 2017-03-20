package com.mySampleApplication.server;

import com.mySampleApplication.shared.Bus;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daria Serebryakova on 20.03.2017.
 */
public class GetDataFromXml {

    public static List<Bus> getXMLdata(InputStream stream){
        List<Bus> buses;
//        String filePath = "server/busTimeTable.xml";
        buses = new ArrayList<>();


        DocumentBuilderFactory dbf = null;
        DocumentBuilder db  = null;
        Document doc = null;
        try {

            dbf = DocumentBuilderFactory.newInstance();
            db  = dbf.newDocumentBuilder();
            doc = null;

            FileInputStream fis = null;


            try {
//                fis = new FileInputStream(path);
                doc = db.parse(stream);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

            if (doc != null) {
                doc.getDocumentElement().normalize();
            }

            NodeList nodeList = null;
            if (doc != null) {
                nodeList = doc.getElementsByTagName("buses");
            }
            NodeList fields   = null;
            if (nodeList != null) {
                for (int s = 0; s < nodeList.getLength(); s++) {
                    Node node = nodeList.item(s);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element fstElement = (Element) node;
                        fields = fstElement.getElementsByTagName("bus");
                        for(int busIdx = 0; busIdx < fields.getLength(); busIdx++) {
                            Bus bus = new Bus();
                            Node busNode = fields.item(busIdx);
                            String number = getValue(busNode, "number");
                            System.out.println("number = " + number);
                            String beginStop = getValue(busNode, "beginstop");
                            System.out.println("beginStop = " + beginStop);
                            String endStop = getValue(busNode, "endstop");
                            System.out.println("endStop = " + endStop);
                            String time = getValue(busNode, "timeofsvobodasq");
                            System.out.println("time = " + time);
                            bus.setNumber(number);
                            bus.setBeginstop(beginStop);
                            bus.setEndstop(endStop);
                            bus.setTimeofsvobodasq(time);
                            buses.add(bus);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(buses.size());

        return buses;
    }

    private static String getValue(Node field, String fieldName)
    {
        NodeList list = field.getChildNodes();
        if (list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); ++i) {
                Node value = list.item(i);
                if(value.getNodeName().equals(fieldName))
                {
                    return value.getChildNodes().item(0).getNodeValue();
                }
            }
            return "";
        } else {
            return "";
        }
    }



}
