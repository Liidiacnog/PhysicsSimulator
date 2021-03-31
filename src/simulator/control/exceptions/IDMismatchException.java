package simulator.control.exceptions;

import org.json.JSONObject;


/*thrown when the comparison of the ID's of 2 bodies via a StateComparator is false*/
public class IDMismatchException extends StatesMismatchException {
	
    private String _id1, _id2;
    
	public IDMismatchException(String str, String id1, String id2) {
		super(str);
        _id1 = id1;
        _id2 = id2;
	}//TODO used?
	
    
    public IDMismatchException() {
		_id1 = null;
        _id2 = null;
	}//TODO used?


	public IDMismatchException(String str) {
		super(str);
	}
	
	public IDMismatchException(String str, Throwable cause) {
		super(str, cause);
	}
	
	public IDMismatchException(Throwable cause) {
		super(cause);
	}
	
	public IDMismatchException(String str, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(str, cause, enableSuppression, writableStackTrace);
	}
	
	
}

