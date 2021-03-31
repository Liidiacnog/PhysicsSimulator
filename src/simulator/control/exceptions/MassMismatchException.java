package simulator.control.exceptions;

public class MassMismatchException extends StatesMismatchException {
	
	public MassMismatchException(String str) {
		super(str);
	}
	
	public MassMismatchException(String str, Throwable cause) {
		super(str, cause);
	}
	
	public MassMismatchException(Throwable cause) {
		super(cause);
	}
	
	public MassMismatchException(String str, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(str, cause, enableSuppression, writableStackTrace);
	}
	
}
