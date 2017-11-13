package exceptions;

public abstract class CustomException extends Exception{

	protected static final long serialVersionUID = 1L;
	protected String errorMessage;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
