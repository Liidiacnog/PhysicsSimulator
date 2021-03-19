package simulator.factories;

import org.json.JSONObject;

import simulator.model.Body;
import simulator.misc.*;


public class BasicBodyBuilder extends Builder<Body> {
    
    private static String BasicBodyBuilderType = "basic";

    public BasicBodyBuilder(){
        _type = BasicBodyBuilderType;
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
        String id = info.getString("id");
        Vector2D v = (Vector2D) info.get("v");
        Vector2D p = (Vector2D) info.get("p");
        double m = info.getDouble("m");
        try {
            return new Body(id, v, p, m);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }

    
}