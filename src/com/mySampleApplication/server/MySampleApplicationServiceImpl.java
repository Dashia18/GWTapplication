package com.mySampleApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.mySampleApplication.shared.Bus;
import com.mySampleApplication.client.MySampleApplicationService;

import java.util.Iterator;
import java.util.List;


public class MySampleApplicationServiceImpl extends RemoteServiceServlet implements MySampleApplicationService {
    // Implementation of sample interface method
    @Override
    public String getMessage(String msg) {
        return "* " + msg + "<br>* march 2017";
    }


    @Override
    public List<Bus> getDataList(int msg, Bus newBus)
    {
        //for static method: CurrentClass.class.getResourceAsStream("busTimetable.xml")
        DataFromXml dataLoader = new DataFromXml();
        List<Bus> busses;
        busses = dataLoader.getXMLdata();

//        if(msg.equals("add")){
//            busses = dataLoader.addNewBus(newBus);
//        }
//        else if(msg.equals("delete")){
//            busses = dataLoader.deleteBus(newBus);
//        }

        return  busses;
    }

    @Override
    public List<Bus> addDataToList(Bus newBus) {
        DataFromXml dataLoader = new DataFromXml();
        List<Bus> busses;
        dataLoader.getXMLdata();
        busses = dataLoader.addNewBus(newBus);
        return busses;
    }

    @Override
    public List<Bus> deleteDataFromList(Bus newBus) {

        DataFromXml dataLoader = new DataFromXml();
        List<Bus> busses;
        dataLoader.getXMLdata();
        busses = dataLoader.deleteBus(newBus);

        return busses;
    }

    @Override
    public List<Bus> getSortedDataList(String msg) {
        DataFromXml dataLoader = new DataFromXml();
        List<Bus> busses;
        busses = dataLoader.getXMLdata();
        busses = SortBusList.getSortedBuses(msg, busses);


        return busses;
    }
}