package logic;

import backEnd.DataRequestStrategy;
import gui.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class ButtonGoListener implements EventHandler<ActionEvent>{
	
	protected Window w;
	protected DataRequestStrategy dataRequestStrategy;
	
	public ButtonGoListener(Window w){
		this.w=w;
	}
}
