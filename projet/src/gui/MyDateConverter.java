package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class MyDateConverter extends StringConverter<LocalDate>{
	
	String pattern = "dd-MM-yyyy";
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

	private static MyDateConverter instance;
	
//	public MyDateConverter(DateTimeFormatter dateFormatter){
//		this.dateFormatter= dateFormatter;
//	}

	private MyDateConverter(){}
	public static MyDateConverter getInstance(){
		if (instance==null){
			instance= new MyDateConverter();
		}
		return instance;
	}
	
	@Override
	  public String toString(LocalDate date) {
		    if (date != null) {
		    	return dateFormatter.format(date);
		    } else {
		    	return "";
		    }
	  }
	
	  @Override
	  public LocalDate fromString(String string) {
		    if (string != null && !string.isEmpty()) {
		    	return LocalDate.parse(string, dateFormatter);
		    } else {
		    	return null;
		    }
	  }
	  
	  public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public DateTimeFormatter getDateFormatter() {
			return dateFormatter;
		}

		public void setDateFormatter(DateTimeFormatter dateFormatter) {
			this.dateFormatter = dateFormatter;
		}
	
}
