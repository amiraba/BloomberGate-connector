package logic;

import gui.Window;
import javafx.scene.control.Button;


public class Logic {
	
	public static void runLogic(Window w){
		
		 Button buttonGo = w.getTabs().get(0).getButtonGo();
		 buttonGo.setOnAction(new ButtonGoCurrentListener(w));
		 
		 Button buttonGoHistorical = w.getTabs().get(1).getButtonGo();
		 buttonGoHistorical.setOnAction( new ButtonGoHistoricalListener(w));
	}
	
	
}
