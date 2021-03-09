package simulator.factories;

public class Builder<T> {
/*A builder is an object that is able to create an instance of a specific type, i.e., it can handle a
JSON structure with a very specific value for key type. */

    private static String _type;

    /*creates an object of type T (i.e., an instance of a sub-class of T) if it recognizes the information in info, 
otherwise it returns null to indicate that this builder cannot handle the request. In the case that
it recognizes the type tag but there is an error in the values provided in the data
section, it should throw an IllegalArgumentException exception.*/
    public T createInstance(JSONObject info) throws IllegalArgumentException {
        T inst = null;
        if(info.type == _type)
            T = new Builder();
        return inst;
    }


    /*returns a JSON serving as a template for the corresponding builder, i.e., a valid value for the parameter of 
    createInstance (see getInfo() of Factory<T> as well).*/
    public JSONObject getBuilderInfo(){
        JSONObject o = new JSONObject();
        o.put("type", _type);
        return o;
    }


}