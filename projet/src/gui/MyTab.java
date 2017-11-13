package gui;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public abstract class MyTab {
	
	protected String tabTitle;

	protected Tab tab;
	protected HBox pane1HBox; //Main pane
	protected VBox pane4VBox; //for the chart so it's the main of the main
	protected GridPane pane2Grid;	//Left side pane
	protected HBox pane3HBox; //for label/textField
	
	protected Label equityLabel;
	protected TextField equityTextField;

	protected ArrayList<String> fieldsNames;
	protected ArrayList<CheckBox> fieldsBoxes;
	
	protected Button buttonGo ;

	protected Text errorText;
	
	protected TableView tableView ;
	
	public abstract void displayTableContent();
	

	
	public MyTab(){

		fieldsNames= new ArrayList<String>();
		
		tab= new Tab();
		
		pane1HBox= new HBox();
		pane2Grid= new GridPane();
		pane3HBox= new HBox();
		pane4VBox= new VBox();
		
		equityLabel= new Label("Equity");
		equityTextField= new TextField();
		fieldsBoxes = new ArrayList<CheckBox>();
		
		setButtonGo(new Button("GO"));
		

		errorText = new Text();
	}
	

	public void drawTab(){

    	CheckBox temp;
		int i=0;
		tab.setText(tabTitle);
		
		pane3HBox.getChildren().add(equityLabel);
		pane3HBox.getChildren().add(equityTextField);
		equityLabel.setPrefWidth(200);
		equityTextField.setPrefWidth(300);
		equityTextField.setPromptText("Enter Equity here...");
		pane3HBox.setPadding(new Insets(20,20,20,20));
		pane3HBox.setSpacing(10);
		
		pane2Grid.add(pane3HBox,0,i);
		
		for (i=0; i<fieldsNames.size(); i++){
			temp=new CheckBox(fieldsNames.get(i));
			fieldsBoxes.add(temp);
			pane2Grid.add(fieldsBoxes.get(i),0,i+3);
			pane2Grid.setHalignment(temp,HPos.CENTER);
			pane2Grid.setAlignment(Pos.CENTER);
			fieldsBoxes.get(i).setSelected(true);
		}
		
		pane2Grid.add(getButtonGo(),0,i+4);
		
		pane2Grid.add(errorText, 0, i+7);
		errorText.setFill(Color.FIREBRICK);
		
		pane2Grid.setHalignment(getButtonGo(),HPos.RIGHT);
		
		pane2Grid.setAlignment(Pos.CENTER);
		pane2Grid.setPrefWidth(300);
		pane2Grid.setPrefHeight(pane1HBox.getPrefHeight());
		pane2Grid.setPadding(new Insets(20,20,20,20));
		pane2Grid.setVgap(5);
		pane2Grid.setHgap(10);

		

		pane1HBox.getChildren().add(pane2Grid);
		pane4VBox.getChildren().add(pane1HBox);

		tab.setContent(pane4VBox);
	}
	
	

	public ArrayList<CheckBox> getFieldsBoxes() {
		return fieldsBoxes;
	}

	public void setFieldsBoxes(ArrayList<CheckBox> fieldsBoxes) {
		this.fieldsBoxes = fieldsBoxes;
	}
	
	public Text getErrorText() {
		return errorText;
	}

	public void setErrorText(Text errorText) {
		this.errorText = errorText;
	}

	
	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public TextField getEquityTextField() {
		return equityTextField;
	}

	public void setEquityTextField(TextField equityTextField) {
		this.equityTextField = equityTextField;
	}

	public ArrayList<String> getFieldsNames() {
		return fieldsNames;
	}

	public void setFieldsNames(ArrayList<String> fieldsNames) {
		this.fieldsNames = fieldsNames;
	}

	public Button getButtonGo() {
		return buttonGo;
	}

	public void setButtonGo(Button buttonGo) {
		this.buttonGo = buttonGo;
	}


}
