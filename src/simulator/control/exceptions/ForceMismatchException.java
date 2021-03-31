package simulator.control.exceptions;

public class ForceMismatchException extends StatesMismatchException {
	
	public ForceMismatchException(String str) {
		super(str);
	}
	
	public ForceMismatchException(String str, Throwable cause) {
		super(str, cause);
	}
	
	public ForceMismatchException(Throwable cause) {
		super(cause);
	}
	
	public ForceMismatchException(String str, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(str, cause, enableSuppression, writableStackTrace);
	}
	
	
}

