package com.mySampleApplication.server;

import com.mySampleApplication.shared.Bus;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by Daria Serebryakova on 20.03.2017.
 */
public class GetDataFromXml {
    TransformerFactory tf;
    DocumentBuilderFactory dbf;

    public GetDataFromXml() {
        // Warning! For internal reasons, DocumentBuilderFactory initialises from system configuration
        // on the first call only, and in case if the configuration of the system mismatches the one
        // that was used during DocumentBuilder factory initialization on the next call,
        // the error occures. Since the Transformer factory modifies system configuration
        // on each instance request, it should be created __before__ the creation of the
        // DocumentBuilderFactory
            tf = TransformerFactory.newInstance();
            Properties systemProperties = System.getProperties();
            systemProperties.remove("javax.xml.parsers.DocumentBuilderFactory");
            System.setProperties(systemProperties);
            dbf = DocumentBuilderFactory.newInstance();
    }

    public List<Bus> getXMLdata(String msg, Bus newBus){
        InputStream stream = GetDataFromXml.class.getResourceAsStream("busTimetable.xml");

        String dataFileURL = GetDataFromXml.class.getResource("busTimetable.xml").getPath();
        System.out.println("dataFileURL = " + dataFileURL);
        System.out.println("stream = " + stream);

        List<Bus> buses;
        buses = new ArrayList<>();
        Boolean rewriteFile = false;
        System.out.println("NEW BUS number = "+newBus.getNumber());
        System.out.println("stream = " + stream);
        System.out.println("msg = " + msg);

        DocumentBuilder db  = null;
        Document doc = null;
        try {
            db  = dbf.newDocumentBuilder();
            doc = null;

            try {
                doc = db.parse(stream);
                stream.close();
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
                            String beginStop = getValue(busNode, "beginstop");
                            String endStop = getValue(busNode, "endstop");
                            String time = getValue(busNode, "timeofsvobodasq");


//                        if(!msg.isEmpty() && msg.equals(number)){
//                            System.out.println(msg);
//                            rewriteFile = true;
//                            fstElement.removeChild(busNode);
//                            System.out.println("delete " + number);
//
//                        }
//                        else {

                            bus.setNumber(number);
                            bus.setBeginstop(beginStop);
                            bus.setEndstop(endStop);
                            bus.setTimeofsvobodasq(time);
                            buses.add(bus);
//                        }

                        }
                        //bus number check??????????? ->then  up to if-else
//                        if(newBus.getNumber()!=null){
//                            System.out.println("NEW BUS is came!!");
//                            rewriteFile = true;
//                            //add new Node element to the .xml structure
//                            Element bus = doc.createElement("bus");
//
//                            Element number = doc.createElement("number");
//                            number.appendChild(doc.createTextNode(newBus.getNumber()));
//                            bus.appendChild(number);
//
//                            Element beginstop = doc.createElement("beginstop");
//                            beginstop.appendChild(doc.createTextNode(newBus.getBeginstop()));
//                            bus.appendChild(beginstop);
//
//                            Element endstop = doc.createElement("endstop");
//                            endstop.appendChild(doc.createTextNode(newBus.getEndstop()));
//                            bus.appendChild(endstop);
//
//                            Element timeofsvobodasq = doc.createElement("timeofsvobodasq");
//                            timeofsvobodasq.appendChild(doc.createTextNode(newBus.getTimeofsvobodasq()));
//                            bus.appendChild(timeofsvobodasq);
//
//                            doc.getChildNodes().item(0).appendChild(bus);
//
//                            //add to final List for user
//                            buses.add(newBus);
//
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("buses list size = " + buses.size());
        Iterator<Bus> iter = buses.iterator();
        Bus bus;
                while(iter.hasNext()){
                    bus = iter.next();
                    System.out.print(bus.getNumber() + " " +bus.getBeginstop()+
                            " "+bus.getEndstop()+" "+ bus.getTimeofsvobodasq());
                    System.out.println();
                }
        //rewrite file
//        if(rewriteFile) {
//            DOMSource source = new DOMSource(doc);
//            try {
//                Transformer t = tf.newTransformer();
//                t.transform(new DOMSource(doc), new StreamResult(System.out));
//
////                String dataFileURL = GetDataFromXml.class.getResource("busTimetable.xml").getPath();
//
////                System.out.println("dataFileURL = " + dataFileURL);
//                StreamResult result = new StreamResult(dataFileURL);
////                t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("busTimetable.xml")));
//                t.transform(source, result);
//            } catch (TransformerException e) {
//                e.printStackTrace();
//            }
//        }

        return buses;
    }

    public List<Bus> setXMLdata(){

    }
    public List<Bus> addNewBus(){

    }
    public List<Bus> deleteNewBus(){

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
