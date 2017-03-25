package com.mySampleApplication.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
    private final static VerticalPanel tablePanel = new VerticalPanel();
    private static String mode = "";


    private final List<Bus> busList = new ArrayList<>();
    private ListBox listBoxSorting = createListBox();
    private HorizontalPanel panelPaging = createPanelPaging();
    private DecoratorPanel addNewRowPanel = getAddNewRowPanel();
    private DecoratorPanel filteringDataPanel = createFilteringDataPanel();

    private ListBox createListBox(){
        ListBox listBox = new ListBox();
        listBox.addItem("(none)");
        listBox.addItem("Sort by number");
        listBox.addItem("Sort by begin stop");
        listBox.addItem("Sort by end stop");
        listBox.addItem("Sort by time on the stop");
        listBox.setVisibleItemCount(1);
        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                tablePanel.clear();
                String sortingType = listBox.getSelectedValue();
                pagingBegin=0;
                MySampleApplicationService.App.getInstance().getSortedDataList(pagingBegin,sortingType, new DataLoadAsyncCallback(busList));
                listBox.setItemSelected(0,true);
            }
        });
        return listBox;
    }
    private static Integer pagingBegin;
    private static Integer listSize;

    private void changeNextPagingBegin() {
        if(pagingBegin + 10< (listSize-10)){
             pagingBegin  = pagingBegin + 10;
        }
        else
        {
            if(listSize<10){
                pagingBegin = 0;
            }
            else {
                pagingBegin = listSize - 10;
            }
        }
    }
    private void changePrevPagingBegin() {
        if (pagingBegin>10) {
            pagingBegin = pagingBegin - 10;
        }
        else {
            pagingBegin = 0;
        }
    }

    private HorizontalPanel createPanelPaging(){
        HorizontalPanel panelPaging = new HorizontalPanel();

        Button buttonPagePrev = new Button("Previous page");
        buttonPagePrev.setStyleName("my-button paging-button");
        buttonPagePrev.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                tablePanel.clear();
                MySampleApplicationService.App.getInstance().getListSize(new GetListSizeAsyncCallback(listSize));
                changePrevPagingBegin();
                MySampleApplicationService.App.getInstance().getCurrentList(pagingBegin, new DataLoadAsyncCallback(busList));

            }
        });
        Button buttonPageNext = new Button("Next page");
        buttonPageNext.setStyleName("my-button paging-button");
        buttonPageNext.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                tablePanel.clear();
                MySampleApplicationService.App.getInstance().getListSize(new GetListSizeAsyncCallback(listSize));
                changeNextPagingBegin();
                MySampleApplicationService.App.getInstance().getCurrentList(pagingBegin, new DataLoadAsyncCallback(busList));


            }
        });
        panelPaging.add(buttonPagePrev);
        panelPaging.add(buttonPageNext);
        return panelPaging;
    }

    private DecoratorPanel getAddNewRowPanel(){
        final FormPanel form = new FormPanel();
        DecoratorPanel addNewRowPanel = new DecoratorPanel();
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
                tablePanel.clear();
                listSize++;
                pagingBegin++;
                MySampleApplicationService.App.getInstance().addDataToList(pagingBegin,
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
        addNewRowPanel.add(form);

        return addNewRowPanel;

    }

    private DecoratorPanel createFilteringDataPanel(){

        DecoratorPanel filteringDataPanel = new DecoratorPanel();

        final VerticalPanel panelFilter = new VerticalPanel();

        final FormPanel form = new FormPanel();
        form.setWidget(panelFilter);
        // Create a TextBox, giving it a name so that it will be submitted.
        final Label lb0 = new Label();
        lb0.setText("Choose parameters for filtering");
        lb0.setStyleName("small-grey-text");
        panelFilter.add(lb0);

        final HorizontalPanel filter1 = new HorizontalPanel();

        final Label lb1 = new Label();
        lb1.setText("Number");
        filter1.add(lb1);
        final TextBox tb1 = new TextBox();
        tb1.setStyleName("text-box-filter");
        filter1.add(tb1);
        panelFilter.add(filter1);

        final HorizontalPanel filter2 = new HorizontalPanel();
        final Label lb2 = new Label();
        lb2.setText("Begin bus stop");
        filter2.add(lb2);
        final TextBox tb2 = new TextBox();
        tb2.setStyleName("text-box-filter");
        filter2.add(tb2);
        panelFilter.add(filter2);

        final HorizontalPanel filter3 = new HorizontalPanel();
        final Label lb3 = new Label();
        lb3.setText("End bus stop");
        filter3.add(lb3);
        final TextBox tb3 = new TextBox();
        tb3.setStyleName("text-box-filter");
        filter3.add(tb3);
        panelFilter.add(filter3);

        final HorizontalPanel filter4 = new HorizontalPanel();
        final Label lb4 = new Label();
        lb4.setText("Time period from");
        filter4.add(lb4);
        final TextBox tb4 = new TextBox();
        tb4.setStyleName("text-box-filter text-time");
        filter4.add(tb4);
        final Label lb5 = new Label();
        lb5.setText("to");
        filter4.add(lb5);
        final TextBox tb5 = new TextBox();
        tb5.setStyleName("text-box-filter text-time");
        filter4.add(tb5);

        panelFilter.add(filter4);
        panelFilter.setStyleName("filter-style");

        // Add a 'submit' button.
        Button buttonForm = new Button("Make filtering");
        buttonForm.setStyleName("my-button filter-button");
        buttonForm.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                label.setText("");
                pagingBegin = 0;
                MySampleApplicationService.App.getInstance().getFilteringDataList(pagingBegin,
                        tb1.getText(),tb2.getText(), tb3.getText(),tb4.getText(),
                        tb5.getText(),
                        new DataLoadAsyncCallback(busList));


                form.submit();
                tb1.setText("");
                tb2.setText("");
                tb3.setText("");
                tb4.setText("");
                tb5.setText("");



            }
        });
        panelFilter.add(buttonForm);
        filteringDataPanel.add(form);
        filteringDataPanel.setStyleName("filter-main-box");

        return filteringDataPanel;

    }


    private MenuBar createMenuBar(){
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
                tablePanel.clear();
                addNewRowPanel.setVisible(false);
                listBoxSorting.setVisible(true);
                filteringDataPanel.setVisible(true);
                panelPaging.setVisible(true);
                mode = "seeTimetable";
                pagingBegin = 0;
                MySampleApplicationService.App.getInstance().getListSize(new GetListSizeAsyncCallback(listSize));
                MySampleApplicationService.App.getInstance().getDataList(pagingBegin, new Bus(),new DataLoadAsyncCallback(busList));

            }
        });

        busesMenu.addItem("Hello page", new Command() {
            @Override
            public void execute() {
                label.setText("");
                tablePanel.clear();
                addNewRowPanel.setVisible(false);
                listBoxSorting.setVisible(false);
                filteringDataPanel.setVisible(false);
                panelPaging.setVisible(false);

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
                tablePanel.clear();
                listBoxSorting.setVisible(false);
                filteringDataPanel.setVisible(false);
                panelPaging.setVisible(true);
                mode = "manageTimetable";
                pagingBegin = 0;
                label.setText("If you want to delete the row click on it, if you want to add the row - find the button bellow");
                label.setStyleName("small-grey-text");

                MySampleApplicationService.App.getInstance().getListSize(new GetListSizeAsyncCallback(listSize));
                MySampleApplicationService.App.getInstance().getDataList(pagingBegin, new Bus(),new DataLoadAsyncCallback(busList));
                addNewRowPanel.setVisible(true);


            }
        });
        menu.addItem(new MenuItem("Buses timetable", busesMenu));
        menu.addSeparator();
        menu.addItem(new MenuItem("Administration", adminMenu));

        return menu;
    }
    public void onModuleLoad() {

        HorizontalPanel mailPanel = new HorizontalPanel();

        VerticalPanel contentPanel = new VerticalPanel();

        MenuBar menu = createMenuBar();


        RootPanel.get("gwtContainer1").add(menu);



        contentPanel.add(label);
        listBoxSorting.setVisible(false);
        contentPanel.add(listBoxSorting);



        mailPanel.add(tablePanel);
        filteringDataPanel.setVisible(false);
        mailPanel.add(filteringDataPanel);
        contentPanel.add(mailPanel);
        panelPaging.setVisible(false);
        contentPanel.add(panelPaging);

        addNewRowPanel.setVisible(false);
        // Add the widgets to the root tablePanel.
        contentPanel.add(addNewRowPanel);
//        mailPanel.add(contentPanel);


        RootPanel.get("gwtContainer2").add(contentPanel);

    }



    private static void createTable(List<Bus> busListResult){
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
        tablePanel.setStyleName("time-table");

        tablePanel.setBorderWidth(1);
        tablePanel.add(table);
    }
    private static void actionWithRowSelected(Bus selected, List<Bus> busListResult){
        if(mode.equals("seeTimetable")) {
            Window.alert("You selected bus №: " + selected.getNumber() + " from " +
                    selected.getBeginstop() + " to " + selected.getEndstop() +
                    " at time: " + selected.getTimeofsvobodasq());
        }
        else{
            tablePanel.clear();
            listSize--;
            pagingBegin--;
            MySampleApplicationService.App.getInstance().deleteDataFromList(pagingBegin,
                    new Bus(selected.getNumber(),selected.getBeginstop(),
                            selected.getEndstop(), selected.getTimeofsvobodasq()), new DataLoadAsyncCallback(busListResult));

        }

    }
    private static class DataLoadAsyncCallback implements AsyncCallback<List<Bus>> {
        List<Bus> busList;

        DataLoadAsyncCallback(List<Bus> busList) {
            this.busList = busList;
        }

        @Override
        public void onSuccess(List<Bus> busListResult) {


            if(busListResult!=null){
                tablePanel.clear();

                createTable(busListResult);
            }
            else
            {
                label.setText("No buses with this parameters, try again!");
            }
            MySampleApplicationService.App.getInstance().getListSize(new GetListSizeAsyncCallback(listSize));

        }

        @Override
        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");

        }
    }
    private static class MyAsyncCallback implements AsyncCallback<String> {
        private Label label;

        MyAsyncCallback(Label label) {
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
    private static class GetListSizeAsyncCallback implements AsyncCallback<Integer> {
        final Label label = new Label();
//        Integer listSize;
        GetListSizeAsyncCallback(Integer listSize) {
//            this.listSize = listSize;
        }
        @Override
        public void onSuccess(Integer result) {
//            label.getElement().setInnerHTML("result is"+result);
            listSize = result;

        }
        @Override
        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");
        }
    }
}
