package simulator.factories;

import org.json.JSONObject;

import simulator.model.Body;
import simulator.misc.*;


public class BasicBodyBuilder extends Builder<Body> {
    
    private static String BasicBodyBuilderType = "basic";

    private BasicBodyBuilder(){
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
        return new Body(info.getString("id"), new Vector2D(info.getJSONArray("v").getInt(0), info.getJSONArray("v").getInt(1)), 
                new Vector2D(info.getJSONArray("p").getInt(0), info.getJSONArray("p").getInt(1)), info.getInt("m"));
    }

    
}