package com.mySampleApplication.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.shared.Bus;

import java.util.List;

public interface MySampleApplicationServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
    void getDataList(int msg, Bus newBus, AsyncCallback<List<Bus>> async);
    void getCurrentList(int msg, AsyncCallback<List<Bus>> async);
    void addDataToList(int fromInd, Bus newBus, AsyncCallback<List<Bus>> async);
    void deleteDataFromList(int fromInd, Bus newBus, AsyncCallback<List<Bus>> async);

    void getSortedDataList(int fromInd, String msg, AsyncCallback<List<Bus>> async);

    void getListSize(AsyncCallback<Integer> async);
}
