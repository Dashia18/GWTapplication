package com.mySampleApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.mySampleApplication.shared.Bus;
import com.mySampleApplication.client.MySampleApplicationService;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;


public class MySampleApplicationServiceImpl extends RemoteServiceServlet implements MySampleApplicationService {
    // Implementation of sample interface method
    @Override
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }

    @Override
    public List<Bus> loadData(String msg)
    {
        //for static method: CurrentClass.class.getResourceAsStream("busTimetable.xml")
        InputStream dataFile = getClass().getResourceAsStream("busTimetable.xml");
        System.out.println("dataFile = " + dataFile);
        //("com/mySampleApplication/server/busTimetable.xml");

        return  GetDataFromXml.getXMLdata(dataFile);
    }
}