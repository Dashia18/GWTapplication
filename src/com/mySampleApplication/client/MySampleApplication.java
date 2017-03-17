package com.mySampleApplication.client;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.DOM;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.Arrays;
import java.util.Date;
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
    private static class Contact {
        private final String address;
        private final Date birthday;
        private final String name;
        public Contact(String name, Date birthday, String address) {
            this.name = name;
            this.birthday = birthday;
            this.address = address;
        }
    }

    private static final List<Contact> CONTACTS = Arrays.asList(
            new Contact("John", new Date(80, 4, 12), "123 Fourth Avenue"),
            new Contact("Joe", new Date(85, 2, 22), "22 Lance Ln"),
            new Contact("George",new Date(46, 6, 6),"1600 Pennsylvania Avenue"));

    public void onModuleLoad() {

        final Label label = new Label();

        // Create a CellTable.
        CellTable<Contact> table = new CellTable<Contact>();
        table.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        // Add a text column to show the name.
        TextColumn<Contact> nameColumn =
                new TextColumn<Contact>() {
                    @Override
                    public String getValue(Contact object) {
                        return object.name;
                    }
                };
        table.addColumn(nameColumn, "Name");
        // Add a date column to show the birthday.
        DateCell dateCell = new DateCell();
        Column<Contact, Date> dateColumn
                = new Column<Contact, Date>(dateCell) {
            @Override
            public Date getValue(Contact object) {
                return object.birthday;
            }
        };
        table.addColumn(dateColumn, "Birthday");
        // Add a text column to show the address.
        TextColumn<Contact> addressColumn
                = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact object) {
                return object.address;
            }
        };
        table.addColumn(addressColumn, "Address");
        // Add a selection model to handle user selection.
        final SingleSelectionModel<Contact> selectionModel
                = new SingleSelectionModel<Contact>();
        table.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(
                new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {
                        Contact selected = selectionModel.getSelectedObject();
                        if (selected != null) {
                            Window.alert("You selected: " + selected.name);
                        }
                    }
                });
        // Set the total row count. This isn't strictly necessary,
        // but it affects paging calculations, so its good habit to
        // keep the row count up to date.
        table.setRowCount(CONTACTS.size(), true);
        // Push the data into the widget.
        table.setRowData(0, CONTACTS);
        VerticalPanel panel = new VerticalPanel();
        panel.setBorderWidth(1);
        panel.setWidth("400");
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
                RootPanel.get("gwtContainer2").remove(panel);
                label.getElement().setInnerHTML("here administrator will be manage");
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

    private static class MyAsyncCallback implements AsyncCallback<String> {
        private Label label;
        CellTable<Contact> table;

        public MyAsyncCallback(Label label) {
            this.label = label;
        }
        public void onSuccess(String result) {
            label.getElement().setInnerHTML(result);
        }

        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");
        }



        public MyAsyncCallback(CellTable<Contact> table) {
            this.table = table;
        }
    }
}
