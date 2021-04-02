package simulator.factories;

import org.json.*;

import simulator.model.Body;
import simulator.misc.*;


public class BasicBodyBuilder extends Builder<Body> {
    
    private static String BasicBodyBuilderType = "basic";
    private static final String BasicBodyBuilderDesc = "Body with no special behaviour";

    public BasicBodyBuilder(){
        _type = BasicBodyBuilderType;
        _desc = BasicBodyBuilderDesc;
    }

    /*returns a JSON serving as a template for the corresponding builder, i.e., a valid value for the parameter of 
    createInstance (see getInfo() of Factory<T> as well).*/
    protected JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("id", "Body id");
        data.put("v", "Velocity vector");
        data.put("p", "Position vector");
        data.put("m", "Mass of the body");
        return data;
    }
    
    protected Body createNewT(JSONObject info) {
        try {
            String id = info.getString("id");
            JSONArray a = info.getJSONArray("p");
            Vector2D p = new Vector2D(a);
            a = info.getJSONArray("v");
            Vector2D v = new Vector2D(a);
            double m = info.getDouble("m");
            
            return new Body(id, v, p, m);
        } catch (RuntimeException e) { // TODO change for JSONException?
            throw new IllegalArgumentException("Basic body could not be created with given data"); //TODO add message
        }
    }

    
}