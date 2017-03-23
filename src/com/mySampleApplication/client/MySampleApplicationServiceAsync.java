package com.mySampleApplication.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.shared.Bus;

import java.util.List;

public interface MySampleApplicationServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
    void getDataList(String msg, Bus newBus, AsyncCallback<List<Bus>> async);
}
