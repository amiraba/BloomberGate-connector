package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.Logic;

public class Launcher extends Application {
	
	public void start(Stage s) throws Exception {
		s= new Window();
		((Window)s).drawWindow();
		
		Logic.runLogic((Window)s);
    }
	
	 public static void main( String[] args ) throws Exception {
		 launch(args);
	}	
}
