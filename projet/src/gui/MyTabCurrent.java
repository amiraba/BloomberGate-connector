package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyTabCurrent extends MyTab{
	private HashMap<String, String> resultMap;
	

	protected TableColumn<DataCurrent,String> colField ;
	protected TableColumn<DataCurrent,String> colValue;

	public MyTabCurrent(){
		super();
		tabTitle="Current";
        fieldsNames.add("TICKER");
        fieldsNames.add("LAST PRICE");
        fieldsNames.add("ASK");
        fieldsNames.add("BID");
        fieldsNames.add("CHAIN_TICKER");
        
		tableView = new TableView<DataCurrent>();        
	}
	
	public void drawTab(){
		super.drawTab();
		pane1HBox.getChildren().add(tableView);
	}

	@Override
	public void displayTableContent(){
		List<DataCurrent> list= new ArrayList<DataCurrent>();
		
		for (HashMap.Entry<String, String> entry : resultMap.entrySet())
		{
	        list.add(new DataCurrent(entry.getKey().toString(), entry.getValue().toString()));
	        
	        //System.out.println("in tableView:\t "+entry.getKey() + " = " + entry.getValue());
	    }
		
		final ObservableList<DataCurrent> data =
		        FXCollections.observableArrayList(list);
		if (colField ==null){ //first request
			 colField = new TableColumn("Field");
			 colField.setCellValueFactory(
		        	    new PropertyValueFactory<DataCurrent,String>("field")
		        	);
		}
		if (colValue ==null){
			colValue = new TableColumn("Value");
			colValue.setCellValueFactory(
	        	    new PropertyValueFactory<DataCurrent,String>("value")
	        	);
		}
        

        tableView.setItems(data);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(colField, colValue);
	}
	
	public HashMap<String, String> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, String> resultMap) {
		this.resultMap = resultMap;
	}
	
	public TableView<DataCurrent> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<DataCurrent> tableView) {
		this.tableView = tableView;
	}
}
