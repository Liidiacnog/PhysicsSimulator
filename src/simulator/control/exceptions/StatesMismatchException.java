package simulator.control.exceptions;

import org.json.JSONObject;

/*thrown when the comparison of two states via a StateComparator is false*/

public class StatesMismatchException extends Exception { 
	
    private JSONObject _j1, _j2;
    private int _execStep;

	public StatesMismatchException(String str, JSONObject j1, JSONObject j2, int execStep) {
		super(str);
        _j1 = j1;
        _j2 = j2;
        _execStep = execStep;
	}
	
    
    public StatesMismatchException() {
		_j1 = null;
        _j2 = null;
        _execStep = -1;
	}

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


