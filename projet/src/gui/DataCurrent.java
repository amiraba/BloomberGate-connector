package gui;

import javafx.beans.property.SimpleStringProperty;

public class DataCurrent implements Data {
    private final SimpleStringProperty field;
    private final SimpleStringProperty value;
 
    public DataCurrent(String xField, String xValue) {
        this.field = new SimpleStringProperty(xField);
        this.value = new SimpleStringProperty(xValue);
    }
 
    public String getField() {
        return field.get();
    }
    public void setField(String xField) {
        field.set(xField);
    }
        
    public String getValue() {
        return value.get();
    }
    public void setValue(String xValue) {
        value.set(xValue);
    }
    
        
}