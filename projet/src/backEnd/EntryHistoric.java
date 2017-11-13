package backEnd;

import com.bloomberglp.blpapi.Datetime;

public class EntryHistoric {
	private String date;
	private String ask;
	private String bid;

	public EntryHistoric (String date, String ask, String bid){
		this.date=date;
		this.ask=ask;
		this.bid=bid;
	}

	/*public EntryHistoric(Datetime date2, double ask2, double bid2) {
		this.date=date;
		this.ask=ask;
		this.bid=bid;
	}*/
	
	public String toString(){
		return date+"- BID: "+bid+" - ASK: "+ask;
		
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}
}
