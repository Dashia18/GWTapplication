package com.mySampleApplication.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTable;

import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mySampleApplication.shared.Bus;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class MySampleApplication implements EntryPoint {

    /**
     * This is the entry point method.
     */

    private void showSelectedMenuItem(String menuItemName){
        Window.alert("Menu item: "+menuItemName+" selected");
    }


//    private static final List<Bus> BUSES = Arrays.asList(
//            new Bus("1","Minina sq","Sherbinki","12.00"),
//            new Bus("3","Svoboda sq","Krasnoe Sormovo","12.05"));

    static VerticalPanel panel = new VerticalPanel();
    public void onModuleLoad() {

        final Label label = new Label();
        final List<Bus> busList = new ArrayList<>();
        RootPanel.get("gwtContainer2").add(panel);


        // Create a menu bar
        MenuBar menu = new MenuBar();
        menu.setAutoOpen(true);
        menu.addStyleName("main-menu-style");
        menu.setAnimationEnabled(true);

        // Create the buses menu
        MenuBar busesMenu = new MenuBar(true);

        busesMenu.setAnimationEnabled(true);
        busesMenu.addItem("See buses timetable", new Command() {
            @Override
            public void execute() {
                label.setText("");
                panel.clear();
                label.getElement().setInnerHTML("HOW TO TRANSPORT TIMETABLE");
            }
        });

        busesMenu.addItem("Sorting buses", new Command() {
            @Override
            public void execute() {
                label.setText("");
                panel.clear();
                MySampleApplicationService.App.getInstance().getMessage("Hello, World!", new MyAsyncCallback(label));
            }
        });
        busesMenu.addSeparator();

        // Create the edit menu
        MenuBar adminMenu = new MenuBar(true);
        adminMenu.setAnimationEnabled(true);
        adminMenu.addItem("Open Administration page", new Command() {
            @Override
            public void execute() {
                label.setText("");
                panel.clear();
                MySampleApplicationService.App.getInstance().loadData("", new DataLoadAsyncCallback(busList));

            }
        });

        menu.addItem(new MenuItem("Buses timetable", busesMenu));
        menu.addSeparator();
        menu.addItem(new MenuItem("Administration", adminMenu));
        RootPanel.get("gwtContainer1").add(menu);
        RootPanel.get("gwtContainer2").add(label);


//        final Button button = new Button("Click me");
//        button.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                if (label.getText().equals("")) {
//                    MySampleApplicationService.App.getInstance().getMessage("Hello, World!", new MyAsyncCallback(label));
//                } else {
//                    label.setText("");
//                }
//            }
//        });

        // Assume that the host HTML has elements defined whose
        // IDs are "slot1", "slot2".  In a real app, you probably would not want
        // to hard-code IDs.  Instead, you could, for example, search for all
        // elements with a particular CSS class and replace them with widgets.
        //
//        RootPanel.get("slot1").add(button);

    }


    private static class DataLoadAsyncCallback implements AsyncCallback<List<Bus>> {
        List<Bus> busList;
        final Label label = new Label();


        public DataLoadAsyncCallback(List<Bus> busList) {
            this.busList = busList;
        }


        public void createTable(List<Bus> busListResult){
//            Window.alert("busListResult onSuccess" + "DataLoadAsyncCallback: busListResult size = " + busListResult.get(0).getBeginstop());


            // Create a CellTable.
            CellTable<Bus> table = new CellTable<Bus>();
            table.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
            // Add a text column to show the name.

            TextColumn<Bus> nameColumn =
                    new TextColumn<Bus>() {
                        @Override
                        public String getValue(Bus object) {
                            return object.getNumber();
                        }
                    };
            table.addColumn(nameColumn, "Bus number");
            TextColumn<Bus> beginStopColumn
                    = new TextColumn<Bus>() {
                @Override
                public String getValue(Bus object) {
                    return object.getBeginstop();
                }
            };
            table.addColumn(beginStopColumn, "Begin stop");

            // Add a text column to show the address.
            TextColumn<Bus> endStopColumn
                    = new TextColumn<Bus>() {
                @Override
                public String getValue(Bus object) {
                    return object.getEndstop();
                }
            };
            table.addColumn(endStopColumn, "End stop");

            TextColumn<Bus> timeColumn
                    = new TextColumn<Bus>() {
                @Override
                public String getValue(Bus object) {
                    return object.getTimeofsvobodasq();
                }
            };
            table.addColumn(timeColumn, "Time of the bus on Svoboda sq.");

            // Add a selection model to handle user selection.
            final SingleSelectionModel<Bus> selectionModel
                    = new SingleSelectionModel<Bus>();
            table.setSelectionModel(selectionModel);
            selectionModel.addSelectionChangeHandler(
                    new SelectionChangeEvent.Handler() {
                        public void onSelectionChange(SelectionChangeEvent event) {
                            Bus selected = selectionModel.getSelectedObject();
                            if (selected != null) {
                                Window.alert("You selected: " + selected.getNumber());
                            }
                        }
                    });

            table.setRowCount( busListResult.size(), true);
            table.setVisibleRange(0,busListResult.size());
            table.setRowData(0, busListResult);

            panel.setBorderWidth(1);
            panel.add(table);
        }
        @Override
        public void onSuccess(List<Bus> busListResult) {

            createTable(busListResult);

        }
        @Override
        public void onFailure(Throwable throwable) {
            System.out.println("DataLoadAsyncCallback: failed");
            label.setText("Failed to receive answer from server!");
        }

    }
    private static class MyAsyncCallback implements AsyncCallback<String> {
        private Label label;

        public MyAsyncCallback(Label label) {
            this.label = label;
        }
        @Override
        public void onSuccess(String result) {
            label.getElement().setInnerHTML(result);
        }
        @Override
        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");
        }
    }
}
