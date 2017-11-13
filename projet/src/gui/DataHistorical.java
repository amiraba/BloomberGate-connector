package gui;

import javafx.beans.property.SimpleStringProperty;

public class DataHistorical implements Data{
    private final SimpleStringProperty date;
    private final SimpleStringProperty ask;
    private final SimpleStringProperty bid;
 
    public DataHistorical(String xDate, String xAsk, String xBid) {
        this.date = new SimpleStringProperty(xDate);
        this.ask = new SimpleStringProperty(xAsk);
        this.bid = new SimpleStringProperty(xBid);
    }
 
    public String getDate() {
        return date.get();
    }
    public void setDate(String xDate) {
        date.set(xDate);
    }
        
    public String getAsk() {
        return ask.get();
    }
    public void setAsk(String xAsk) {
    	ask.set(xAsk);
    }
    
    public String getBid() {
        return bid.get();
    }
    public void setBid(String xBid) {
    	bid.set(xBid);
    }
            
}