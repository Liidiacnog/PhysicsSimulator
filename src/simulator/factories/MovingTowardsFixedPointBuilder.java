package simulator.factories;

import simulator.model.MovingTowardsFixedPoint;
import simulator.model.ForceLaws;
import org.json.JSONObject;
import simulator.misc.*;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {
    
    private static String MovingTowardsFixedPointBuilderType = "mtcp";
    protected String _desc = "Applying a force in the direction of a fixed point";

    public MovingTowardsFixedPointBuilder(){
        _type = MovingTowardsFixedPointBuilderType;
    }
    
    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("c", "Fixed point coords");
        data.put("g", "Aceleration vector");

        return data;
    }

    protected ForceLaws createNewT(JSONObject info) {
        try {
            Vector2D c = new Vector2D(info.getJSONArray("c"));
            return new MovingTowardsFixedPoint(c, info.getDouble("g"));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }
}
