package simulator.factories;

import org.json.JSONObject;

/*A builder is an object that is able to create an instance of a specific type, i.e., it can handle a
JSON structure with a very specific value for key type. */
public abstract class Builder<T> {

    protected String _type;
    protected String _desc;

    /*creates an object of type T (i.e., an instance of a sub-class of T) if it recognizes the information in info, 
    otherwise it returns null to indicate that this builder cannot handle the request. In the case that it recognizes 
    the type tag but there is an error in the values provided in the data section, it throws IllegalArgumentException*/
    public T createInstance(JSONObject info) throws IllegalArgumentException { 
        T inst = null;
        if(_type.equals(info.getString("type")))
            inst = createNewT(info.getJSONObject("data"));//throws IllegalArgumentException if data is incorrect
        return inst;
    }


    /*returns a JSON serving as a template for the corresponding builder, i.e., a valid value for the parameter of 
    createInstance (see getInfo() of Factory<T> as well).*/
    public JSONObject getBuilderInfo(){ 
        JSONObject o = new JSONObject();
        o.put("type", _type);
        o.put("data", getData());
        o.put("desc", _desc);
        return o;
    }

    //default behaviour,  to be overwritten by some subclasses
    protected JSONObject getData(){
        JSONObject o = new JSONObject();
        o.put("data", "No data required");
        return o;
    }

    protected abstract T createNewT(JSONObject info) throws IllegalArgumentException;

}
