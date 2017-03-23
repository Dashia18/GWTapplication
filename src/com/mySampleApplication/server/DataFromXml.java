package com.mySampleApplication.server;

import com.mySampleApplication.shared.Bus;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
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
public class DataFromXml {
    TransformerFactory tf;
    DocumentBuilderFactory dbf;

    String dataFileURL;
    Document doc;
    List<Bus> buses;

    public DataFromXml() {
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

            dataFileURL = DataFromXml.class.getResource("busTimetable.xml").getPath();
    }

    public List<Bus> getXMLdata(String msg, Bus comeBus){
        InputStream stream = DataFromXml.class.getResourceAsStream("busTimetable.xml");

        System.out.println("dataFileURL = " + dataFileURL);
        System.out.println("stream = " + stream);

        buses = new ArrayList<>();
        System.out.println("NEW BUS number = "+comeBus.getNumber());
        System.out.println("stream = " + stream);
        System.out.println("msg = " + msg);

        DocumentBuilder db  = null;
        doc = null;
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

        System.out.println("buses list size = " + buses.size());
        Iterator<Bus> iter = buses.iterator();
        Bus bus;
            while(iter.hasNext()){
                bus = iter.next();
                System.out.print(bus.getNumber() + " " +bus.getBeginstop()+
                        " "+bus.getEndstop()+" "+ bus.getTimeofsvobodasq());
                System.out.println();
            }
        return buses;
    }

    public void setXMLdata(){
            DOMSource source = new DOMSource(doc);
            try {
                Transformer t = tf.newTransformer();
                t.transform(new DOMSource(doc), new StreamResult(System.out));
                StreamResult result = new StreamResult(dataFileURL);
                t.transform(source, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
    }
    public List<Bus> addNewBus(Bus newBus){

        buses.add(newBus);

        System.out.println("NEW BUS is came!!");
        Element bus = doc.createElement("bus");

        Element number = doc.createElement("number");
        number.appendChild(doc.createTextNode(newBus.getNumber()));
        bus.appendChild(number);

        Element beginstop = doc.createElement("beginstop");
        beginstop.appendChild(doc.createTextNode(newBus.getBeginstop()));
        bus.appendChild(beginstop);

        Element endstop = doc.createElement("endstop");
        endstop.appendChild(doc.createTextNode(newBus.getEndstop()));
        bus.appendChild(endstop);

        Element timeofsvobodasq = doc.createElement("timeofsvobodasq");
        timeofsvobodasq.appendChild(doc.createTextNode(newBus.getTimeofsvobodasq()));
        bus.appendChild(timeofsvobodasq);

        doc.getChildNodes().item(0).appendChild(bus);
        setXMLdata();

        return buses;
    }

    public List<Bus> deleteBus(Bus comeBus){
        Iterator<Bus> iter = buses.iterator();
        Bus bus;
        int ind = 0;
        while(iter.hasNext()){
            bus = iter.next();
            System.out.println("ind = " + ind);

            if(comeBus.getNumber().equals(bus.getNumber()) &&
                comeBus.getBeginstop().equals(bus.getBeginstop()) &&
                comeBus.getEndstop().equals(bus.getEndstop()) &&
                comeBus.getTimeofsvobodasq().equals(bus.getTimeofsvobodasq()))
            {
                System.out.print(bus.getNumber() + " " +bus.getBeginstop()+
                        " "+bus.getEndstop()+" "+ bus.getTimeofsvobodasq());
                System.out.println();
                System.out.println("delete");
                break;
            }
            ind++;
        }
        buses.remove(ind);
        deleteBusFromXml(comeBus);

        return buses;
    }

    private void deleteBusFromXml(Bus comeBus){
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
                    for (int busIdx = 0; busIdx < fields.getLength(); busIdx++) {
                        Bus bus = new Bus();
                        Node busNode = fields.item(busIdx);

                        String number = getValue(busNode, "number");
                        String beginStop = getValue(busNode, "beginstop");
                        String endStop = getValue(busNode, "endstop");
                        String time = getValue(busNode, "timeofsvobodasq");

                        if(comeBus.getNumber().equals(number) &&
                                comeBus.getBeginstop().equals(beginStop) &&
                                comeBus.getEndstop().equals(endStop) &&
                                comeBus.getTimeofsvobodasq().equals(time)){
                            fstElement.removeChild(busNode);
                            System.out.println("delete " + number);
                        }
                    }
                }
            }
        }
        setXMLdata();
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
