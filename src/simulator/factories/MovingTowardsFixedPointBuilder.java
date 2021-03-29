package simulator.factories;

import simulator.model.MovingTowardsFixedPoint;
import simulator.model.ForceLaws;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.*;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {
    
    private static String MovingTowardsFixedPointBuilderType = "mtcp";
    private static final String MovingTowardsFixedPointBuilderDesc = "Applying a force in the direction of a fixed point";

    public MovingTowardsFixedPointBuilder(){
        _type = MovingTowardsFixedPointBuilderType;
        _desc = MovingTowardsFixedPointBuilderDesc; 
    }
    
    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("c", "Fixed point coordinates");
        data.put("g", "Acceleration vector");
        return data;
    }

    protected ForceLaws createNewT(JSONObject info) {
        try {
            Vector2D c = new Vector2D(info.getJSONArray("c"));
            return new MovingTowardsFixedPoint(c, info.getDouble("g"));
        } catch (JSONException e) { //no params provided, so default values are used
            return new MovingTowardsFixedPoint();
        }
    }
}
