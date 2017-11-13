package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import backEnd.EntryHistoric;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class MyTabHistorical extends MyTab {
	private DatePicker datePickerStart;
	private DatePicker datePickerEnd;
	private ArrayList<EntryHistoric> historicEntries;
	
	protected TableColumn<DataHistorical,String> colDate ;
	protected TableColumn<DataHistorical,String> colAsk;
	protected TableColumn<DataHistorical,String> colBid;
	
	protected ArrayList<String> fieldsEntered;
	
	protected LineChart lineChart;
	


	public MyTabHistorical(){
		super();
		tabTitle="Historical";
		
		datePickerStart = new DatePicker();
		setDatePickerEnd(new DatePicker());
		
        fieldsNames.add("ASK");
        fieldsNames.add("BID");
        
        tableView = new TableView<DataHistorical>();  
	}
	
	public void drawTab(){
		super.drawTab();
		
		pane2Grid.add(getDatePickerStart(),0,1);
		pane2Grid.add(getDatePickerEnd(),0,2);

	    
		StringConverter<LocalDate> converter = MyDateConverter.getInstance();
	    
	    datePickerStart.setPromptText(((MyDateConverter) converter).getPattern().toLowerCase());
	    datePickerEnd.setPromptText(((MyDateConverter) converter).getPattern().toLowerCase());
	    
	    datePickerStart.setConverter(converter);
	    datePickerEnd.setConverter(converter);

	    
	    pane1HBox.getChildren().add(tableView);
	    
		pane1HBox.setPrefHeight(pane1HBox.getPrefHeight()/2);
	    
	}
	
	public void drawChart(){
		
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

		if (lineChart != null){	//if it's not the first request
			eraseChart();
    		
		}
        
        //creating the chart
        lineChart =  new LineChart(xAxis,yAxis);
        
        xAxis.setLabel("Date");
        
        lineChart.setPrefWidth(pane4VBox.getPrefWidth());
        lineChart.setPrefHeight(pane4VBox.getPrefHeight()/2);
        
        lineChart.setTitle("Historical graph");
       
        lineChart.setData(getChartData() );
        
        lineChart.setPrefSize(pane4VBox.getPrefHeight()/2, pane4VBox.getPrefHeight());
	      
        pane4VBox.getChildren().add(lineChart);
        

	}
	
	public void eraseChart(){

        pane4VBox.getChildren().remove(lineChart);
	}
	
	private ObservableList<XYChart.Series<String, Double>> getChartData() {

	      ObservableList<XYChart.Series<String, Double>> answer = FXCollections.observableArrayList();
	      
	      Series<String, Double> askSeries = new Series<String, Double>();
	      askSeries.setName("ASK");
	      
	      Series<String, Double> bidSeries = new Series<String, Double>();
	      bidSeries.setName("BID");
	      

	      for (EntryHistoric eh: historicEntries){
	  		if(fieldsEntered.contains("ASK")){
		    	  askSeries.getData().add(new XYChart.Data(eh.getDate(), convertBloombergDataToDouble(eh.getAsk()) ) );
	    	  }
	    	 
	  		if(fieldsEntered.contains("BID")){
		    	  bidSeries.getData().add(new XYChart.Data(eh.getDate(), convertBloombergDataToDouble(eh.getBid()) ) );
	    	  }
	      }
	      answer.addAll(askSeries,bidSeries);
	      
	      return answer;
	    }
	private double convertBloombergDataToDouble(String s){
		s= s.replaceAll(",", ".").replaceAll("€", "").replaceAll(" ", "");
  	  	Double d=Double.parseDouble(s);
  	  	return d;
	}
	


		
	@Override
	public void displayTableContent() {
		
		List<DataHistorical> list= new ArrayList<DataHistorical>();
		
		for (EntryHistoric  entry : historicEntries )
		{
	        list.add(new DataHistorical(entry.getDate(), entry.getAsk(), entry.getBid() ));
	        
	        System.out.println("inDisplayTableContent \t"+entry.getDate() + " = " +  entry.getAsk()+ " = " +  entry.getBid());
	    }
		
		final ObservableList<DataHistorical> data =
		        FXCollections.observableArrayList(list);
		
		if (colDate ==null){ //first request
			colDate = new TableColumn("Date");
			colDate.setCellValueFactory(
		        	    new PropertyValueFactory<DataHistorical,String>("date")
		        	);
		}
		if (colAsk ==null){
			colAsk = new TableColumn("ASK");
			colAsk.setCellValueFactory(
	        	    new PropertyValueFactory<DataHistorical,String>("ask")
	        	);
		}
		if (colBid ==null){
			colBid = new TableColumn("BID");
			colBid.setCellValueFactory(
	        	    new PropertyValueFactory<DataHistorical,String>("bid")
	        	);
		}
		
		if(!fieldsEntered.contains("ASK")){
			colAsk.setVisible(false);
		}else{
			colAsk.setVisible(true);
		}
		
		if(!fieldsEntered.contains("BID")){
			colBid.setVisible(false);
		}else{
			colBid.setVisible(true);
		}
        

        tableView.setItems(data);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(colDate, colAsk, colBid);
	}

	public TableView<DataHistorical> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<DataHistorical> tableView) {
		this.tableView = tableView;
	}

	public ArrayList<EntryHistoric> getHistoricEntries() {
		return historicEntries;
	}

	public void setHistoricEntries(ArrayList<EntryHistoric> historicEntries) {
		this.historicEntries = historicEntries;
	}
	public ArrayList<String> getFieldsEntered() {
		return fieldsEntered;
	}

	public void setFieldsEntered(ArrayList<String> fieldsEntered) {
		this.fieldsEntered = fieldsEntered;
	}
	public DatePicker getDatePickerStart() {
		return datePickerStart;
	}

	public void setDatePickerStart(DatePicker datePickerStart) {
		this.datePickerStart = datePickerStart;
	}

	public DatePicker getDatePickerEnd() {
		return datePickerEnd;
	}

	public void setDatePickerEnd(DatePicker datePickerEnd) {
		this.datePickerEnd = datePickerEnd;
	}
}
	
