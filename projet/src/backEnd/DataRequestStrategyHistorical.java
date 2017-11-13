package backEnd;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bloomberglp.blpapi.Datetime;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.CorrelationID;
import com.bloomberglp.blpapi.Request;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.SessionOptions;

import exceptions.DataNotEnteredException;
import exceptions.InvalidDatesException;
import exceptions.InvalidEquityException;

public class DataRequestStrategyHistorical extends DataRequestStrategy
{
	private static DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
	
	private String dateStringStart;
	private String dateStringEnd;
	
	private ArrayList<EntryHistoric> historicEntries;
	
	public DataRequestStrategyHistorical(String security, ArrayList fieldFilters,String dateStringStart, String dateStringEnd ){
		super(security, fieldFilters);
		this.dateStringStart=dateStringStart;
		this.dateStringEnd=dateStringEnd;
		historicEntries= new ArrayList<EntryHistoric>();
	}
	
	public void run() throws Exception
	{
		bloombergConnection.connect();
		
		if(bloombergConnection.isEstablished()== true){
			request();
			welcomeEvents();
		}
		
	}
	
	protected void request() throws Exception{
		
		if ( isDataEntered() ){
			
			if(AreDatesLegimate()){
				request = bloombergConnection.getService().createRequest("HistoricalDataRequest");

				request.append("securities", equity );//SPY US EQUITY

				checkFields();
				manageDates();
				adjustDateParameters();
				manageOverrides();
				
				CorrelationID requestID = new CorrelationID(1);
				bloombergConnection.getSession().sendRequest(request, requestID);
			}else{
				throw new InvalidDatesException();
			}
			
			

		}else{
			throw new DataNotEnteredException(this);
		}
	}
	
	protected boolean isDataEntered(){
		 return ( !equity.isEmpty() && !fieldsEntered.isEmpty() && !dateStringStart.isEmpty() && !dateStringEnd.isEmpty());
	}
	
	protected boolean AreDatesLegimate(){
		//check if start<end
		int i1 = dateStringStart.compareTo(dateStringEnd);
		boolean b1= i1<0;
		
		//check if end<=today
//		String pattern = "dd-MM-yyyy";
//		String dateInString =new SimpleDateFormat(pattern).format(new Date());
		
		String dateInString = formatter.format(new Date());
		int i2= dateStringEnd.compareTo(dateInString);
		boolean b2= i2<=0;
		
		//check if 1900<start when no computers existed XD
		int i3= "01-01-1900".compareTo(dateStringStart);
		boolean b3= i3<0;
		return b1&&b2&&b3;
	}
	
	

	protected void checkFields() throws Exception{

    	if (fieldsEntered.isEmpty()) 
        {
			request.append("fields", "BID");
			
			request.append("fields", "ASK");

        }else{
        	if (fieldsEntered.contains("BID") ) {
        		request.append("fields", "BID");
        	}
        	if (fieldsEntered.contains("ASK") ) {
        		request.append("fields", "ASK");
        	}
        }
	}
	
	
	
	protected void manageOverrides(){

	    try {
			request.set("overrideOption", "OVERRIDE_OPTION_CLOSE");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	

	
	protected void manageDates() throws Exception{
		
		Date dateStart = formatter.parse(dateStringStart);
		String strStart = dateFmt.format(dateStart);
	
		
		Date dateEnd = formatter.parse(dateStringEnd);
		String strEnd = dateFmt.format(dateEnd);
		
		request.set("startDate", strStart);
		request.set("endDate", strEnd); //Request that the information end three days before today.  This is an optional override.  The default is today.
	}
	protected void adjustDateParameters() throws Exception{
		request.set("periodicityAdjustment", "CALENDAR");
		
		//Determine the frequency of the output. To be used in conjunction with Period Adjustment.
	    request.set("periodicitySelection", "DAILY"); //Optional string.  Valid values are DAILY (default), WEEKLY, MONTHLY, QUARTERLY, SEMI_ANNUALLY, and YEARLY
	
	    //Sets quote to Price or Yield for a debt instrument whose default value is quoted in yield (depending on pricing source).
	    request.set("pricingOption", "PRICING_OPTION_PRICE"); //Optional string.  Valid values are PRICING_OPTION_PRICE (default) and PRICING_OPTION_YIELD
	
	    //Adjust for "change on day"
	    request.set("adjustmentNormal", true); //Optional bool. Valid values are true and false (default = false)
	
	    //Adjusts for Anormal Cash Dividends
	    request.set("adjustmentAbnormal", false); //Optional bool. Valid values are true and false (default = false)
	
	    //Capital Changes Defaults
	    request.set("adjustmentSplit", true); //Optional bool. Valid values are true and false (default = false)
	
	    //The maximum number of data points to return, starting from the startDate
	    //request.set("maxDataPoints", 5); //Optional integer.  Valid values are positive integers.  The default is unspecified in which case the response will have all data points between startDate and endDate
	}
	
	protected void handleResponseEvent(Event evt, ArrayList fieldFilters) throws Exception
	{
		
		System.out.println("---ResponseEvent---");
		
		
		//handle errors like in Current
		System.out.println("EventType = " + evt.eventType());
		
		MessageIterator miter = evt.messageIterator();
		
		EntryHistoric historicEntry;
		
		while(miter.hasNext())
		{
			Message msg = miter.next();
			
			System.out.println("correlationID= " + msg.correlationID());
			System.out.println("messageType= " + msg.messageType());
			
			Element elmSecurityData = msg.getElement("securityData");
			
			Element elmSecurity = elmSecurityData.getElement("security");
			String security = elmSecurity.getValueAsString();
			System.out.println(security);
			
			if(elmSecurityData.hasElement("securityError", true))
			{
				
				throw new InvalidEquityException();
			}
			
			Element elmFieldData = elmSecurityData.getElement("fieldData");
			for (int valueIndex = 0; valueIndex < elmFieldData.numValues(); valueIndex++)
			{
	            Element elmValues = elmFieldData.getValueAsElement(valueIndex);                
				Datetime date = elmValues.getElementAsDate("date");
				double bid=0, ask=0;
				String dateString;
				
				 String askString="0", bidString="0";
				 
				 dateString=(DataRequestStrategyHistorical.formatter.format(date.calendar().getTime()));
				
				 if (fieldFilters.contains("ASK") )
	                {
	                	ask = elmValues.getElementAsFloat64("ASK");
	                	askString= DataRequestStrategyCurrent.formatter.format(ask);
	                }
				 if (fieldFilters.contains("BID") )
				 {
				 	bid = elmValues.getElementAsFloat64("BID");
				 	bidString= DataRequestStrategyCurrent.formatter.format(bid);
				 }
                
                
                 historicEntry= new EntryHistoric(dateString, askString, bidString);
                 historicEntries.add(historicEntry);
                 historicEntry=null;
			}
		}
		
		System.out.println("---ResponseEvent End---");
	}
	
	public ArrayList<EntryHistoric> getHistoricEntries() {
		return historicEntries;
	}

	public void setHistoricEntries(ArrayList<EntryHistoric> historicEntries) {
		this.historicEntries = historicEntries;
	}
}
