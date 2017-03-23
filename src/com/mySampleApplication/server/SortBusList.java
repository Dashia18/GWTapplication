package com.mySampleApplication.server;

import com.mySampleApplication.shared.Bus;

import java.util.*;

/**
 * Created by Daria Serebryakova on 23.03.2017.
 */
public class SortBusList {

    static List<Bus> getSortedBuses(String msg, List<Bus> sortedBuses) {


        switch (msg) {
            case "Sort by number": {
                Comparator<Bus> Comparator = new Comparator<Bus>() {
                    @Override
                    public int compare(Bus b1, Bus b2) {
                        int b1int = Integer.parseInt(b1.getNumber());
                        int b2int = Integer.parseInt(b2.getNumber());
                        return (b1int - b2int);
                    }
                };
                Collections.sort(sortedBuses, Comparator);
                break;
            }
            case "Sort by begin stop": {
                Comparator<Bus> Comparator = new Comparator<Bus>() {
                    @Override
                    public int compare(Bus b1, Bus b2) {
                        return b1.getBeginstop().compareTo(b2.getBeginstop());
                    }
                };
                Collections.sort(sortedBuses, Comparator);
                break;
            }
            case "Sort by end stop": {
                Comparator<Bus> Comparator = new Comparator<Bus>() {
                    @Override
                    public int compare(Bus b1, Bus b2) {
                        return b1.getEndstop().compareTo(b2.getEndstop());
                    }
                };
                Collections.sort(sortedBuses, Comparator);
                break;
            }
            case "Sort by time on the stop": {
                Comparator<Bus> Comparator = new Comparator<Bus>() {
                    @Override
                    public int compare(Bus b1, Bus b2) {
                        double b1double = Double.parseDouble(b1.getTimeofsvobodasq()) * 100;
                        double b12double = Double.parseDouble(b2.getTimeofsvobodasq()) * 100;
                        return (int) (b1double - b12double);
                    }
                };
                Collections.sort(sortedBuses, Comparator);
                break;
            }
        }

//        Iterator<Bus> iter = sortedBuses.iterator();
//        Bus bus;
//        while(iter.hasNext()){
//            bus = iter.next();
//            System.out.print(bus.getNumber() + " " +bus.getBeginstop()+
//                    " "+bus.getEndstop()+" "+ bus.getTimeofsvobodasq());
//            System.out.println();
//        }

        return sortedBuses;
    }
}
