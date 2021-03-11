package simulator.factories;

import simulator.model.MovingTowardsFixedPoint;
import simulator.model.ForceLaws;
import org.json.JSONObject;
import simulator.misc.*;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {
    
    private static String MovingTowardsFixedPointBuilderType = "mtfp";

    private MovingTowardsFixedPointBuilder(){
        _type = MovingTowardsFixedPointBuilderType;
    }
    
    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("c", "Fixed point coords");
        data.put("g", "Aceleration vector");

        return data;
    }

    protected ForceLaws createNewT(JSONObject info) {
        return new MovingTowardsFixedPoint(new Vector2D(info.getJSONArray("c").getInt(0), info.getJSONArray("c").getInt(1)), info.getDouble("g"));
    }
}
