package backEnd;

import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.SessionOptions;

import exceptions.ConnectionException;

public class BloombergConnection {
	
	private static BloombergConnection instance;
	
	private  Session session;
	private Service service = null;
	
	private final static String serverHost="127.0.0.1";
	private final static int port=8194;
	
	private static boolean sessionStatus= false; //true if OK
	private static boolean serviceStatus= false; //true if OK
	
	private BloombergConnection(){};
	
	public static BloombergConnection getInstance(){
		if (instance==null){
			instance= new BloombergConnection();
		}
		return instance;
	}
	
	public void connect() throws ConnectionException{
		try{
			startSession();
			startService();
		}catch(Exception e){
			throw new ConnectionException(); //throw it when getService("//blp/refdata") doesn't work 
		}
		if (!isEstablished()){
			throw new ConnectionException();// throw it for other reasons
		}

	}
	
	private  Session startSession() throws Exception{ //new & start
		
		SessionOptions soptions = new SessionOptions();
		soptions.setServerHost(serverHost);
		soptions.setServerPort(port);
		
		setSession(new Session(soptions));
		sessionStatus=getSession().start();

		return getSession();
	}
	
	private  Service startService() throws Exception { //open & get
		serviceStatus= getSession().openService("//blp/refdata");
		if(sessionStatus && serviceStatus)
		{
			setService(getSession().getService("//blp/refdata"));
		}
		return getService();
	}
	
	public boolean isEstablished(){
		return (sessionStatus && serviceStatus);
	}
	

	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	
}
