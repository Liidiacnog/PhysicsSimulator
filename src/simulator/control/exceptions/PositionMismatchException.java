package simulator.control.exceptions;

public class PositionMismatchException extends StatesMismatchException {
	
	public PositionMismatchException(String str) {
		super(str);
	}
	
	public PositionMismatchException(String str, Throwable cause) {
		super(str, cause);
	}
	
	public PositionMismatchException(Throwable cause) {
		super(cause);
	}
	
	public PositionMismatchException(String str, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(str, cause, enableSuppression, writableStackTrace);
	}
	
	
}

