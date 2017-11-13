package gui;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Window extends Stage{
	private Group root;
	private Scene scene;
	
	private BorderPane borderPane; //Pour le centrage de la mise en page
	
	private TabPane tabPane;
	private ArrayList<MyTab> tabs;

	
	public Window(){
		tabs=new ArrayList<MyTab>();
	}
	
	public Window drawWindow(){
		this.setTitle("BloomberGate");
		root = new Group();
		scene = new Scene(root, 500, 500, Color.WHITE);
		
		
		borderPane = new BorderPane();
		
		borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
       
        tabPane= new TabPane();
        
        MyTab tabCurrent = new MyTabCurrent();

        tabCurrent.drawTab();
        tabs.add(tabCurrent);
        tabPane.getTabs().add(tabCurrent.getTab());
        
        MyTab tabHistorical = new MyTabHistorical();
        tabHistorical.drawTab();
        tabs.add(tabHistorical);
        tabPane.getTabs().add(tabHistorical.getTab());
        
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
        
		this.setScene(scene);
		this.show();
		
		return this;
	}

	public ArrayList<MyTab> getTabs() {
		return tabs;
	}
}
