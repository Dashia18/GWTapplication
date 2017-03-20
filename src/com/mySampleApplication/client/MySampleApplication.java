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


    private static final List<Bus> BUSES = Arrays.asList(
            new Bus("1","Minina sq","Sherbinki","12.00"),
            new Bus("3","Svoboda sq","Krasnoe Sormovo","12.05"));


    public void onModuleLoad() {
//        GetDataFromXml.getXMLdata();


        final Label label = new Label();
        final List<Bus> busList = new ArrayList<>();
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
        // Set the total row count. This isn't strictly necessary,
        // but it affects paging calculations, so its good habit to
        // keep the row count up to date.
        table.setRowCount(BUSES.size(), true);
        // Push the data into the widget.
        table.setRowData(0, BUSES);
        VerticalPanel panel = new VerticalPanel();
        panel.setBorderWidth(1);
        panel.add(table);



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
                RootPanel.get("gwtContainer2").add(panel);

                label.getElement().setInnerHTML("HOW TO TRANSPORT TIMETABLE");


            }
        });

        busesMenu.addItem("Sorting buses", new Command() {
            @Override
            public void execute() {
//                showSelectedMenuItem("Sorting buses");
                RootPanel.get("gwtContainer2").remove(panel);
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
//                showSelectedMenuItem("here will be appear a manage data");
                label.setText("");
                RootPanel.get("gwtContainer2").remove(panel);
//                label.getElement().setInnerHTML("here administrator will be manage");
                MySampleApplicationService.App.getInstance().loadData("", new DataLoadAsyncCallback(busList));



            }
        });

        menu.addItem(new MenuItem("Buses timetable", busesMenu));
        menu.addSeparator();
        menu.addItem(new MenuItem("Administration", adminMenu));
        //add the menu to the root panel
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

        public DataLoadAsyncCallback(List<Bus> busList) {
            this.busList = busList;
        }
        public void onSuccess(List<Bus> busList) {
            System.out.println("busList = " + busList);
        }

        public void onFailure(Throwable throwable) {
            System.out.println("failed");
        }

    }
    private static class MyAsyncCallback implements AsyncCallback<String> {
        private Label label;
        CellTable<Bus> table;


        public MyAsyncCallback(Label label) {
            this.label = label;
        }
        public void onSuccess(String result) {
            label.getElement().setInnerHTML(result);
        }

        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");
        }



        public MyAsyncCallback(CellTable<Bus> table) {
            this.table = table;
        }
    }
}
