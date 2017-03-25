package com.mySampleApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.mySampleApplication.shared.Bus;
import com.mySampleApplication.client.MySampleApplicationService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MySampleApplicationServiceImpl extends RemoteServiceServlet implements MySampleApplicationService {

    private List<Bus> currentList;

    @Override
    public String getMessage(String msg) {
        return "* " + msg + "<br>* march 2017";
    }


    @Override
    public List<Bus> getDataList(int fromInd, Bus newBus)
    {

        //for static method: CurrentClass.class.getResourceAsStream("busTimetable.xml")
        DataFromXml dataLoader = new DataFromXml();
        List<Bus> buses = dataLoader.getXMLdata();
        currentList = buses;

        List<Bus> subList = new ArrayList<>(buses.subList(fromInd, fromInd + 10));
        return  subList;
    }

    @Override
    public List<Bus> getCurrentList(int fromInd) {
        return getSubList( currentList, fromInd);
    }

    @Override
    public Integer getListSize() {

        List<Bus> buses;
        if(currentList==null){
            DataFromXml dataLoader = new DataFromXml();
            buses = dataLoader.getXMLdata();
        }
        else {
            buses = currentList;
        }

        return buses.size();
    }

    private List<Bus> getSubList(List<Bus> buses, int fromInd){
        int step = 10;

        List<Bus> subList;
        if(buses==null ){
            subList = null;
        }
        else {

            if (buses.size()<10){
                step=buses.size();
            }
            subList = new ArrayList<>(buses.subList(fromInd, fromInd + step));
        }
        return subList;
    }

    @Override
    public List<Bus> addDataToList(int fromInd, Bus newBus) {
        DataFromXml dataLoader = new DataFromXml();
        List<Bus> buses;
        dataLoader.getXMLdata();
        buses = dataLoader.addNewBus(newBus);
        currentList = buses;
        List<Bus> subList = new ArrayList<>(buses.subList(fromInd, fromInd + 10));
        return subList;
    }

    @Override
    public List<Bus> deleteDataFromList(int fromInd,Bus newBus) {

        DataFromXml dataLoader = new DataFromXml();
        List<Bus> buses;
        dataLoader.getXMLdata();
        buses = dataLoader.deleteBus(newBus);
        currentList = buses;
        List<Bus> subList = new ArrayList<>(buses.subList(fromInd, fromInd + 10));

        return subList;
    }

    @Override
    public List<Bus> getSortedDataList(int fromInd, String msg) {

//            DataFromXml dataLoader = new DataFromXml();
            List<Bus> buses;
//            buses = dataLoader.getXMLdata();
            buses = SortBusList.getSortedBuses(msg, currentList);
            currentList = buses;
            List<Bus> subList = getSubList(buses, fromInd);



        return subList;
    }

    @Override
    public List<Bus> getFilteringDataList(int fromInd, String number, String beginStop,
                                          String endStop, String beginTime, String endTime) {

        DataFromXml dataLoader = new DataFromXml();
        List<Bus> buses;
        buses = dataLoader.getXMLdata();
        buses = FilterBusList.getFilteredBuses(buses,number,beginStop,endStop,beginTime,endTime);

        List<Bus> subList = getSubList( buses, fromInd);
        currentList = buses;

        return subList;
    }



}