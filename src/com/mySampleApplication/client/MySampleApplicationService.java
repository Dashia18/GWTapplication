package com.mySampleApplication.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mySampleApplication.shared.Bus;

import java.util.List;

@RemoteServiceRelativePath("MySampleApplicationService")
public interface MySampleApplicationService extends RemoteService {
    // Sample interface method of remote interface
    String getMessage(String msg);
    List<Bus> getDataList(int msg, Bus newBus);
    List<Bus> getCurrentList(int msg);

    List<Bus> addDataToList(int fromInd, Bus newBus);
    List<Bus> deleteDataFromList(int fromInd, Bus newBus);

    List<Bus> getSortedDataList(int fromInd, String msg);
    Integer getListSize();

    /**
     * Utility/Convenience class.
     * Use MySampleApplicationService.App.getInstance() to access static instance of MySampleApplicationServiceAsync
     */
    public static class App {
        private static MySampleApplicationServiceAsync ourInstance = GWT.create(MySampleApplicationService.class);

        public static synchronized MySampleApplicationServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
