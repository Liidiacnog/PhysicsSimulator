package simulator.control.exceptions;


/*thrown when the comparison of two states via a StateComparator is false*/
public class StatesMismatchException extends Exception {

	public StatesMismatchException() {}

   	public StatesMismatchException(String str) {
		super(str);
	}
	
	public StatesMismatchException(String str, Throwable cause) {
		super(str, cause);
	}
	
	public StatesMismatchException(Throwable cause) {
		super(cause);
	}
	
	public StatesMismatchException(String str, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(str, cause, enableSuppression, writableStackTrace);
	}
	
	
}


