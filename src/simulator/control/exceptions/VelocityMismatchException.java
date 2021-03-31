package simulator.control.exceptions;

public class VelocityMismatchException extends StatesMismatchException {
	
	public VelocityMismatchException(String str) {
		super(str);
	}
	
	public VelocityMismatchException(String str, Throwable cause) {
		super(str, cause);
	}
	
	public VelocityMismatchException(Throwable cause) {
		super(cause);
	}
	
	public VelocityMismatchException(String str, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(str, cause, enableSuppression, writableStackTrace);
	}
	
	
}
