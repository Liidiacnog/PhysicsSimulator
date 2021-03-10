package simulator.factories;

import org.json.JSONObject;

import netscape.javascript.JSObject;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {
    
    public T createInstance(JSONObject info) throws IllegalArgumentException {
        T inst = null;
        if(info.get("type") == _type)
            T = new T(); //TODO ?, also: to be overwitten by those subclasses that do require the data section data to be built?, is tehre any class that doesn't require it?
        return inst;
    }


    /*returns a JSON serving as a template for the corresponding builder, i.e., a valid value for the parameter of 
    createInstance (see getInfo() of Factory<T> as well).*/
    public JSONObject getBuilderInfo(){ 
        JSONObject o = super.getBuilderInfo();
        JSONObject data = new JSONObject();
        data.put("id", null);
    }
    
    
}