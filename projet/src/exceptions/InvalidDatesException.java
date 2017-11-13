package exceptions;

public class InvalidDatesException extends CustomException {
	
	public InvalidDatesException(){
		this.errorMessage="Please select valid dates.";
	}
}
