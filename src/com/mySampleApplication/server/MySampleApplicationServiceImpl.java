package com.mySampleApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.mySampleApplication.shared.Bus;
import com.mySampleApplication.client.MySampleApplicationService;

import java.util.List;


public class MySampleApplicationServiceImpl extends RemoteServiceServlet implements MySampleApplicationService {
    // Implementation of sample interface method
    @Override
    public String getMessage(String msg) {
        return "* " + msg + "<br>* march 2017";
    }


    @Override
    public List<Bus> getDataList(String msg, Bus newBus)
    {

        //for static method: CurrentClass.class.getResourceAsStream("busTimetable.xml")
        DataFromXml dataLoader = new DataFromXml();
        List<Bus> busses;
        busses = dataLoader.getXMLdata(msg, newBus);
        if(msg.equals("add")){
            busses = dataLoader.addNewBus(newBus);
        }
        else if(msg.equals("delete")){
            busses = dataLoader.deleteBus(newBus);
        }

        return  busses;
    }
}