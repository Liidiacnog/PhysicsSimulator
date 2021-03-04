package simulator.factories;

import java.util.List;
import org.json.JSONObject;

public interface Factory<T> {
	/*receives a JSON structure describing the object to be created, and returns an instance of a corresponding
	class — an instance of a sub-type of T. If it does not recognize what is described in info, it should throw 
	an IllegalArgumentException exception*/
	public T createInstance(JSONObject info);


	/*returns a list of JSON objects, which are “templates” for the valid JSON structures that can be passed to createInstance.
	 This is very useful in order to know what are the valid values for a given factory without knowing much about the factory 
	 itself. For example, we will use it when listing the possible values for force laws to the user.*/
	public List<JSONObject> getInfo();
}
