package logic;

import java.util.ArrayList;
import java.util.HashMap;

import backEnd.DataRequestStrategyCurrent;
import exceptions.ConnectionException;
import exceptions.DataNotEnteredException;
import exceptions.InvalidEquityException;
import gui.MyTabCurrent;
import gui.Window;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;

public class ButtonGoCurrentListener extends ButtonGoListener{
	
	public ButtonGoCurrentListener(Window w){
		super(w);
	}

	public void handle(ActionEvent e) {

    	String equityEntered= w.getTabs().get(0).getEquityTextField().getText();

    	ArrayList fieldsEntered= new ArrayList();
    	
    	ArrayList<CheckBox> fieldsBoxes = w.getTabs().get(0).getFieldsBoxes();
    	ArrayList<String> fieldFilters= w.getTabs().get(0).getFieldsNames();
    	
    	CheckBox checkBox;
    	for (int i=0; i<fieldsBoxes.size(); i++)
    	{
    		checkBox=fieldsBoxes.get(i);
    		if (checkBox.isSelected())
    		{
    			fieldsEntered.add(fieldFilters.get(i));
    		}
    	}
        
    	dataRequestStrategy= new DataRequestStrategyCurrent(equityEntered, fieldsEntered );
        MyTabCurrent myTabCurrent=(MyTabCurrent)w.getTabs().get(0);
        try {
        	dataRequestStrategy.run();
			HashMap<String, String> resultMap= ((DataRequestStrategyCurrent)dataRequestStrategy).getResultMap();
			myTabCurrent.setResultMap(resultMap);
			myTabCurrent.displayTableContent();
			myTabCurrent.getErrorText().setText("");
			
		} catch (ConnectionException connectionException) {
			w.getTabs().get(0).getErrorText().setText(connectionException.getErrorMessage());
			myTabCurrent.getTableView().getColumns().clear();
			
		} catch (DataNotEnteredException dataNotEnteredException){
			w.getTabs().get(0).getErrorText().setText(dataNotEnteredException.getErrorMessage());
			myTabCurrent.getTableView().getColumns().clear();
			
		}catch (InvalidEquityException invalidEquityException ){
			w.getTabs().get(0).getErrorText().setText(invalidEquityException.getErrorMessage());
			myTabCurrent.getTableView().getColumns().clear();
		
		}catch (Exception exception){
			w.getTabs().get(0).getErrorText().setText("A problem has occured. Please retry later...");
			myTabCurrent.getTableView().getColumns().clear();
		}
		

    }

}
