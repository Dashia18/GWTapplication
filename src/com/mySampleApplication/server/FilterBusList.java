package com.mySampleApplication.server;

import com.mySampleApplication.shared.Bus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Daria Serebryakova on 25.03.2017.
 */
public class FilterBusList {
    static List<Bus> getFilteredBuses(List<Bus> buses, String number, String beginStop,
                                              String endStop, String beginTime, String endTime){
        List<Bus> filteredBuses = buses;
        if (!number.equals("")){
            filteredBuses = filterByNumber(filteredBuses, number);
        }
        if (!beginStop.equals("")){
            filteredBuses = filterByBeginStop(filteredBuses, beginStop);
        }
        if (!endStop.equals("")){
            filteredBuses = filterByEndStop(filteredBuses, endStop);
        }
        if (!beginTime.equals("")){
            filteredBuses = filterByTime(filteredBuses, beginTime, "24");
        }
        if (!endTime.equals("")){
            filteredBuses = filterByTime(filteredBuses, "0", endTime);
        }

        if (filteredBuses.size()==0){
            filteredBuses = null;
        }

        return filteredBuses;
    }
    private static List<Bus> filterByNumber(List<Bus> buses, String number){
        List<Bus> filteredBuses = new ArrayList<>();
        Iterator<Bus> iter = buses.iterator();
        Bus bus;
        while(iter.hasNext()){
            bus = iter.next();
            if (bus.getNumber().equals(number)){
                filteredBuses.add(bus);
            }
        }
        return filteredBuses;
    }
    private static List<Bus> filterByBeginStop(List<Bus> buses, String beginStop){
        List<Bus> filteredBuses = new ArrayList<>();
        Iterator<Bus> iter = buses.iterator();
        Bus bus;
        while(iter.hasNext()){
            bus = iter.next();
            if (bus.getBeginstop().equals(beginStop)){
                filteredBuses.add(bus);
            }
        }
        return filteredBuses;
    }
    private static List<Bus> filterByEndStop(List<Bus> buses, String endStop){
        List<Bus> filteredBuses = new ArrayList<>();
        Iterator<Bus> iter = buses.iterator();
        Bus bus;
        while(iter.hasNext()){
            bus = iter.next();
            if (bus.getEndstop().equals(endStop)){
                filteredBuses.add(bus);
            }
        }
        return filteredBuses;
    }

    private static List<Bus> filterByTime(List<Bus> buses, String beginTime, String endTime){
        List<Bus> filteredBuses = new ArrayList<>();

        double bTime = Double.parseDouble(beginTime)*100;
        double eTime = Double.parseDouble(endTime)*100;
        Iterator<Bus> iter = buses.iterator();
        Bus bus;
        while(iter.hasNext()){
            bus = iter.next();
            double time = Double.parseDouble(bus.getTimeofsvobodasq())*100;
            if (time>=bTime && time<=eTime){
                filteredBuses.add(bus);
            }
        }
        return filteredBuses;
    }
}
