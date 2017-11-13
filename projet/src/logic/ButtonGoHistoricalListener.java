package logic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import backEnd.DataRequestStrategyHistorical;
import backEnd.EntryHistoric;
import exceptions.ConnectionException;
import exceptions.DataNotEnteredException;
import exceptions.InvalidDatesException;
import exceptions.InvalidEquityException;
import gui.MyDateConverter;
import gui.MyTabHistorical;
import gui.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public class ButtonGoHistoricalListener extends ButtonGoListener{
	
	public ButtonGoHistoricalListener(Window w){
		super(w);
	}
	
	 public void handle(ActionEvent event)
	 {

			DatePicker datePickerStart =((MyTabHistorical)w.getTabs().get(1)).getDatePickerStart();
			DatePicker datePickerEnd= ((MyTabHistorical)w.getTabs().get(1)).getDatePickerEnd();
			
			String dateStringStart= "",  dateStringEnd="";
			
		    StringConverter<LocalDate> converter = MyDateConverter.getInstance();
		    DateTimeFormatter dateFormatter = ((MyDateConverter) converter).getDateFormatter();
		    
			LocalDate date = datePickerStart.getValue();
			if (date != null){
				dateStringStart = dateFormatter.format(date);
			}
	
			date = datePickerEnd.getValue();
			if (date != null){
				dateStringEnd = dateFormatter.format(date);
			}
			
			
			String equityEntered= w.getTabs().get(1).getEquityTextField().getText();
	
	
	    	ArrayList<String> fieldsEntered= new ArrayList<String>();
	    	ArrayList<CheckBox> fieldsBoxes = w.getTabs().get(1).getFieldsBoxes();
	    	ArrayList<String> fieldFilters= w.getTabs().get(1).getFieldsNames();
	    	
	    	CheckBox checkBox;
	    	for (int i=0; i<fieldsBoxes.size(); i++)
	    	{
	    		checkBox=fieldsBoxes.get(i);
	    		if (checkBox.isSelected())
	    		{
	    			fieldsEntered.add(fieldFilters.get(i));
	    		}
	    	}
	        
	    	dataRequestStrategy= new DataRequestStrategyHistorical(equityEntered, fieldsEntered, dateStringStart, dateStringEnd );
			MyTabHistorical myTabHistorical=(MyTabHistorical)w.getTabs().get(1);
		try {
    		dataRequestStrategy.run();
			ArrayList<EntryHistoric> historicEntries= ((DataRequestStrategyHistorical)dataRequestStrategy).getHistoricEntries();
			myTabHistorical.setHistoricEntries(historicEntries);
			myTabHistorical.setFieldsEntered(fieldsEntered);
			myTabHistorical.displayTableContent();
			myTabHistorical.drawChart();
			myTabHistorical.getErrorText().setText("");
			myTabHistorical.getFieldsEntered().clear();;
			
		} catch (ConnectionException connectionException) {
			w.getTabs().get(1).getErrorText().setText(connectionException.getErrorMessage());
			((MyTabHistorical)w.getTabs().get(1)).getTableView().getColumns().clear();
			((MyTabHistorical)w.getTabs().get(1)).eraseChart();
			
		} catch (DataNotEnteredException dataNotEnteredException){
			w.getTabs().get(1).getErrorText().setText(dataNotEnteredException.getErrorMessage());
			((MyTabHistorical)w.getTabs().get(1)).getTableView().getColumns().clear();
			((MyTabHistorical)w.getTabs().get(1)).eraseChart();
			
		}catch (InvalidEquityException invalidEquityException){
			w.getTabs().get(1).getErrorText().setText(invalidEquityException.getErrorMessage());
			((MyTabHistorical)w.getTabs().get(1)).getTableView().getColumns().clear();
			((MyTabHistorical)w.getTabs().get(1)).eraseChart();
			
		}catch (InvalidDatesException invalidDatesException){
			w.getTabs().get(1).getErrorText().setText(invalidDatesException.getErrorMessage());
			((MyTabHistorical)w.getTabs().get(1)).getTableView().getColumns().clear();
			((MyTabHistorical)w.getTabs().get(1)).eraseChart();
		}catch (Exception exception){
			w.getTabs().get(1).getErrorText().setText("A problem has occured. Please retry later...");
			((MyTabHistorical)w.getTabs().get(1)).getTableView().getColumns().clear();
			((MyTabHistorical)w.getTabs().get(1)).eraseChart();
		}
    	
	 }
}
