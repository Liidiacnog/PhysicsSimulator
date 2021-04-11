package simulator.factories;

import simulator.model.MovingTowardsFixedPoint;
import simulator.model.ForceLaws;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.*;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {
    
    private static String MovingTowardsFixedPointBuilderType = "mtfp";
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

    protected ForceLaws createNewT(JSONObject info) { //TODO correct:
        /*the way you treat the default values is a bit complicated because you have to make and 'if' for all combinations 
        (exponential). Better to choose the default values in the builder and have only one constructor for MovingTowardsFixedPoint.
         This way you will only have one 'if' for each option.*/
        try {
            if (!info.isNull("c") || !info.isNull("g")) {
                if (info.isNull("c")) {
                    return new MovingTowardsFixedPoint(info.getDouble("g"));
                } else if (info.isNull("g")) {
                    Vector2D c = new Vector2D(info.getJSONArray("c"));
                    return new MovingTowardsFixedPoint(c);
                } else {
                    Vector2D c = new Vector2D(info.getJSONArray("c"));
                    return new MovingTowardsFixedPoint(c, info.getDouble("g"));
                }
            } else
                return new MovingTowardsFixedPoint();
            
        } catch (JSONException e) { //No params provided, so default values are used
            throw new IllegalArgumentException("Invalid values for moving towrds fixed point");
        }
    }
}
