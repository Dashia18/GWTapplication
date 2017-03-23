package com.mySampleApplication.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class MySampleApplication implements EntryPoint {

    private final static Label label = new Label();
    private final static VerticalPanel panel = new VerticalPanel();
    private static String mode = "";
    private final FormPanel form = new FormPanel();
    private final static DecoratorPanel decoratorPanel = new DecoratorPanel();

    public void onModuleLoad() {


        final List<Bus> busList = new ArrayList<>();
        ListBox listBox = new ListBox();
        listBox.addItem("Sort by number");
        listBox.addItem("Sort by begin stop");
        listBox.addItem("Sort by end stop");
        listBox.addItem("Sort by time on the stop");
        listBox.setVisibleItemCount(1);

//        listBox.addChangeHandler(n)
        final HorizontalPanel panelForm = new HorizontalPanel();
        form.setWidget(panelForm);
        // Create a TextBox, giving it a name so that it will be submitted.
        final TextBox tb1 = new TextBox();
        tb1.setStyleName("text-box-form");
        panelForm.add(tb1);
        final TextBox tb2 = new TextBox();
        tb2.setStyleName("text-box-form");
        panelForm.add(tb2);
        final TextBox tb3 = new TextBox();
        tb3.setStyleName("text-box-form");
        panelForm.add(tb3);
        final TextBox tb4 = new TextBox();
        tb4.setStyleName("text-box-form");
        panelForm.add(tb4);

        // Add a 'submit' button.
        Button buttonForm = new Button("Add new row");
        buttonForm.setStyleName("my-button");
        buttonForm.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                panel.clear();
                MySampleApplicationService.App.getInstance().getDataList("add",
                        new Bus(tb1.getText(),tb2.getText(), tb3.getText(),tb4.getText()),
                        new DataLoadAsyncCallback(busList));

                form.submit();
                tb1.setText("");
                tb2.setText("");
                tb3.setText("");
                tb4.setText("");
            }
        });
        panelForm.add(buttonForm);

        // Add an event handler to the form.
        form.addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {
                // This event is fired just before the form is submitted.
                // We can take this opportunity to perform validation.
                if (tb1.getText().length() == 0 || tb2.getText().length() == 0||
                        tb3.getText().length() == 0||tb4.getText().length() == 0) {
                    Window.alert("The text box must not be empty");
                    event.cancel();
                }

            }
        });
        decoratorPanel.add(form);


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
                decoratorPanel.setVisible(false);
                listBox.setVisible(true);



                mode = "seeTimetable";
                MySampleApplicationService.App.getInstance().getDataList("", new Bus(),new DataLoadAsyncCallback(busList));
            }
        });

        busesMenu.addItem("Hello page", new Command() {
            @Override
            public void execute() {
                label.setText("");
                panel.clear();
                decoratorPanel.setVisible(false);
                listBox.setVisible(false);
                MySampleApplicationService.App.getInstance().getMessage("Hello, you can see time table here!", new MyAsyncCallback(label));
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
                listBox.setVisible(false);
                mode = "manageTimetable";

                label.setText("If you want to delete the row click on it, if you want to add the row - find the button bellow");
                label.setStyleName("small-grey-text");



                MySampleApplicationService.App.getInstance().getDataList("", new Bus(),new DataLoadAsyncCallback(busList));



            }
        });
        menu.addItem(new MenuItem("Buses timetable", busesMenu));
        menu.addSeparator();
        menu.addItem(new MenuItem("Administration", adminMenu));
        RootPanel.get("gwtContainer1").add(menu);
        RootPanel.get("gwtContainer2").add(label);
        listBox.setVisible(false);
        RootPanel.get("gwtContainer2").add(listBox);

        RootPanel.get("gwtContainer2").add(panel);

        decoratorPanel.setVisible(false);
        // Add the widgets to the root panel.
        RootPanel.get("gwtContainer2").add(decoratorPanel);

    }

    private static void createTable(List<Bus> busListResult){
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
                            actionWithRowSelected(selected, busListResult);
                        }
                    }
                });

        table.setRowCount( busListResult.size(), true);
        table.setVisibleRange(0,busListResult.size());
        table.setRowData(0, busListResult);

        panel.setBorderWidth(1);
        panel.add(table);
        if(mode.equals("manageTimetable")) {
            decoratorPanel.setVisible(true);
        }
    }

    private static void actionWithRowSelected(Bus selected, List<Bus> busListResult){
        if(mode.equals("seeTimetable")) {
            Window.alert("You selected bus №: " + selected.getNumber() + " from " +
                    selected.getBeginstop() + " to " + selected.getEndstop() +
                    " at time: " + selected.getTimeofsvobodasq());
        }
        else{
            panel.clear();
            MySampleApplicationService.App.getInstance().getDataList("delete",
                    new Bus(selected.getNumber(),selected.getBeginstop(),
                            selected.getEndstop(), selected.getTimeofsvobodasq()), new DataLoadAsyncCallback(busListResult));
        }

    }
    private static class DataLoadAsyncCallback implements AsyncCallback<List<Bus>> {
        List<Bus> busList;
        final Label label = new Label();


        public DataLoadAsyncCallback(List<Bus> busList) {
            this.busList = busList;
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
