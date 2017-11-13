package backEnd;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.bloomberglp.blpapi.CorrelationID;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;

import exceptions.DataNotEnteredException;
import exceptions.InvalidEquityException;

public class DataRequestStrategyCurrent extends DataRequestStrategy
{

	final static NumberFormat formatter = NumberFormat.getCurrencyInstance();
	private HashMap<String,String> resultMap;
	
	public DataRequestStrategyCurrent(String security, ArrayList<String> fieldFilters) {
		super(security, fieldFilters);
		resultMap= new HashMap<String, String>();
		
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
			
			request = bloombergConnection.getService().createRequest("ReferenceDataRequest");
			
			//request information for the following securities
			request.append("securities", equity);
			
			checkFields();
			manageOverrides();
			
			CorrelationID requestID = new CorrelationID(1);
			bloombergConnection.getSession().sendRequest(request, requestID);

		}else{
			throw new DataNotEnteredException(this);
		}
	}
	
	protected boolean isDataEntered(){
		 return ( !equity.isEmpty() && !fieldsEntered.isEmpty() );
	}

	
	protected void checkFields() throws Exception{
		if (fieldsEntered.contains("TICKER") ) {
    		request.append("fields", "TICKER");
    	}
    	if (fieldsEntered.contains("LAST PRICE") ) {
    		request.append("fields", "PX_LAST");
    	}
    	if (fieldsEntered.contains("ASK") ) {
    		request.append("fields", "ASK");
    	}
    	if (fieldsEntered.contains("BID") ) {
    		request.append("fields", "BID");
    	}
    	if (fieldsEntered.contains("CHAIN_TICKERS") ) {
            request.append("fields", "CHAIN_TICKERS"); //only stocks have this field 
    	}
		
	}
	
	protected void manageOverrides() throws Exception
	{
			Element overrides = request.getElement("overrides");
	
	        //request only puts
	        Element ovrdPutCall = overrides.appendElement();
	        ovrdPutCall.setElement("fieldId", "CHAIN_PUT_CALL_TYPE_OVRD");
	        ovrdPutCall.setElement("value", "P"); //accepts either "C" for calls or "P" for puts
	
	        //request 5 options in the result
	        Element ovrdNumStrikes = overrides.appendElement();
	        ovrdNumStrikes.setElement("fieldId", "CHAIN_POINTS_OVRD");
	        ovrdNumStrikes.setElement("value", 5); //accepts a positive integer
	
	        //request options that expire on Dec. 20, 2014
	        Element ovrdDtExps = overrides.appendElement();
	        ovrdDtExps.setElement("fieldId", "CHAIN_EXP_DT_OVRD");
	        ovrdDtExps.setElement("value", "20141220"); //accepts dates in the format yyyyMMdd (this is Dec. 20, 2014)
		
	}
	
	protected void handleResponseEvent(Event eventObj, ArrayList<String> fieldsEntered) throws Exception
    {
		System.out.println("---ResponseEvent---");
		
		System.out.println("EventType =" + eventObj.eventType());
		MessageIterator miter = eventObj.messageIterator();
		while(miter.hasNext())
		{
			Message message = miter.next();

		    Element elmSecurityDataArray = message.getElement("securityData");
		    
		    for (int valueIndex = 0; valueIndex < elmSecurityDataArray.numValues(); valueIndex++)
		    {
		        Element elmSecurityData = elmSecurityDataArray.getValueAsElement(valueIndex);

		        String security = elmSecurityData.getElementAsString("security");
		        
		        boolean isSecurityError = elmSecurityData.hasElement("securityError", true);
		        if(isSecurityError)
		        {
		        	throw new InvalidEquityException();
		        }
		        else
		        {
		            Element elmFieldData = elmSecurityData.getElement("fieldData");
		            
	                if (fieldsEntered.contains("TICKER") )
	                {
	                	String ticker = elmFieldData.getElementAsString("TICKER");
	                	resultMap.put("TICKER", ticker);
	                	System.out.println("TICKER = " + ticker);
	                }
	            	if (fieldsEntered.contains("LAST PRICE") )
	                {
	                	double px_last = elmFieldData.getElementAsFloat64("PX_LAST");
	                	String s=DataRequestStrategyCurrent.formatter.format(px_last);
	                	resultMap.put("LAST PRICE", s);
	                	System.out.println("PX_LAST = " + s);
	                }
	                if (fieldsEntered.contains("ASK") )
	                {
	                	double ask = elmFieldData.getElementAsFloat64("ASK");
	                	String s=DataRequestStrategyCurrent.formatter.format(ask);
	                	resultMap.put("ASK", s);
	                	System.out.println("ASK = " + s );
	                }
	                if (fieldsEntered.contains("BID") )
	                {
	                	double bid = elmFieldData.getElementAsFloat64("BID");
	                	String s=DataRequestStrategyCurrent.formatter.format(bid);
	                	resultMap.put("BID", s);
	                	System.out.println("BID = " + s);
	                }

	                if (fieldsEntered.contains("CHAIN_TICKERS") )
	                {
	                	boolean excludeNullElements = true;
		                if (elmFieldData.hasElement("CHAIN_TICKERS", excludeNullElements)) //be careful, excludeNullElements is false by default
		                {
		                    Element chainTickers = elmFieldData.getElement("CHAIN_TICKERS");
		                    for (int chainTickerValueIndex = 0; chainTickerValueIndex < chainTickers.numValues(); chainTickerValueIndex++)
		                    {
		                        Element chainTicker = chainTickers.getValueAsElement(chainTickerValueIndex);
		                        String strChainTicker = chainTicker.getElementAsString("Ticker");
		                        
		                        resultMap.put("Chain Ticker", strChainTicker);
		                        System.out.println("CHAIN_TICKER = " + strChainTicker);
		                    }
		                }
	                }
	                fieldsEntered.clear();
	            }
	        }
		        
    	}
		System.out.println("---ResponseEvent End---");
    }
	
	public HashMap<String, String> getResultMap() {
		return resultMap;
	}


	public void setResultMap(HashMap<String, String> resultMap) {
		this.resultMap = resultMap;
	}

    
}