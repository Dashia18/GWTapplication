package com.mySampleApplication.shared;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Daria Serebryakova on 20.03.2017.
 */
public class Bus implements Serializable{
    //private + set/get
    private  String number;
    private  String beginstop;
    private  String endstop;
    private  String timeofsvobodasq;

    public Bus() {
    }

    public Bus(String number, String beginstop, String endstop, String timeofsvobodasq) {
        this.number = number;
        this.beginstop = beginstop;
        this.endstop = endstop;
        this.timeofsvobodasq = timeofsvobodasq;
    }

    public String getNumber() {
        return number;
    }

    public String getBeginstop() {
        return beginstop;
    }

    public String getEndstop() {
        return endstop;
    }

    public String getTimeofsvobodasq() {
        return timeofsvobodasq;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setBeginstop(String beginstop) {
        this.beginstop = beginstop;
    }

    public void setEndstop(String endstop) {
        this.endstop = endstop;
    }

    public void setTimeofsvobodasq(String timeofsvobodasq) {
        this.timeofsvobodasq = timeofsvobodasq;
    }




}
