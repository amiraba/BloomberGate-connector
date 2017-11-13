package backEnd;

import java.util.ArrayList;
import java.util.HashMap;

import com.bloomberglp.blpapi.Datetime;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;
import com.bloomberglp.blpapi.Request;

public abstract class DataRequestStrategy {
	
	protected BloombergConnection bloombergConnection;
	
	String equity;
	ArrayList<String> fieldsEntered;
	Request request =null;


	public DataRequestStrategy(String equity, ArrayList<String> fieldsEntered ){
		this.equity= equity;
		this.fieldsEntered= fieldsEntered;
		this.bloombergConnection=  BloombergConnection.getInstance();
	}
	

	protected void welcomeEvents() throws Exception{
		boolean continueToLoop = true;

        while (continueToLoop)
        {
            Event eventObj = null;
				eventObj = bloombergConnection.getSession().nextEvent();
				
				switch (eventObj.eventType().intValue())
	            {
	                case Event.EventType.Constants.RESPONSE: // final event
	                    continueToLoop = false;
	                    handleResponseEvent(eventObj, fieldsEntered);
	                    break;
	                case Event.EventType.Constants.PARTIAL_RESPONSE:
	                	handleResponseEvent(eventObj, fieldsEntered);
	                    break;
					default:
						handleOtherEvent(eventObj);
						break;
	            }
        }
	}
	
	private void handleOtherEvent(Event evt)
    {
    	MessageIterator miter = evt.messageIterator();
		System.out.println("---OtherEvent---");
		while(miter.hasNext())
		{
			Message message = miter.next();

        	System.out.println("correlationID=" + message.correlationID());
        	System.out.println("messageType=" + message.messageType());
        	System.out.println(message.toString());
        	
            if (Event.EventType.Constants.SESSION_STATUS == evt.eventType().intValue() && message.messageType().equals("SessionTerminated"))
            {
            	System.out.println("Terminating: " + message.messageType());
            }
		}
		System.out.println("---OtherEvent End---");
    }
	
	public abstract void run() throws Exception;
	protected abstract void checkFields() throws Exception;
	protected abstract void manageOverrides() throws Exception;
	protected abstract void handleResponseEvent(Event eventObj, ArrayList<String> fieldFilters) throws Exception;
	protected abstract void request() throws Exception;
	protected abstract boolean isDataEntered();

	
	public BloombergConnection getBloombergConnection() {
		return bloombergConnection;
	}


	public void setBloombergConnection(BloombergConnection bloombergConnection) {
		this.bloombergConnection = bloombergConnection;
	}


	public String getEquity() {
		return equity;
	}


	public void setEquity(String security) {
		this.equity = security;
	}


	public ArrayList getFieldsEntered() {
		return fieldsEntered;
	}


	public void setFieldsEntered(ArrayList fieldsEntered) {
		this.fieldsEntered = fieldsEntered;
	}


	public Request getRequest() {
		return request;
	}


	public void setRequest(Request request) {
		this.request = request;
	}





	
}
