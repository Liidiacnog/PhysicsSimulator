package simulator.factories;

import simulator.model.MovingTowardsFixedPoint;
import simulator.model.ForceLaw;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.*;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaw> {

    private static String MovingTowardsFixedPointBuilderType = "mtfp";
    private static final String MovingTowardsFixedPointBuilderDesc = "Moving towards a fixed point";
    private final static Vector2D Default_c = new Vector2D();
    private static final double Default_g = 9.81;

    public MovingTowardsFixedPointBuilder() {
        _type = MovingTowardsFixedPointBuilderType;
        _desc = MovingTowardsFixedPointBuilderDesc;
    }

    protected JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("c", "the point towards which bodies move (a json list of 2 numbers, e.g., [100.0,50.0])");
        data.put("g", "the length of the acceleration vector (a number)");
        return data;
    }

    protected ForceLaw createNewT(JSONObject info) { 
        try {
            if (!info.has("c") && !info.has("g")) // No params provided, so default values are used
                return new MovingTowardsFixedPoint(Default_c, Default_g);
            else if (!info.has("c") && info.has("g"))
                return new MovingTowardsFixedPoint(Default_c, info.getDouble("g"));
            else if (info.has("c") && !info.has("g"))
                return new MovingTowardsFixedPoint(new Vector2D(info.getJSONArray("c")), Default_g);
            else
                return new MovingTowardsFixedPoint(new Vector2D(info.getJSONArray("c")), info.getDouble("g"));

        } catch (JSONException e) { 
            throw new IllegalArgumentException("Invalid values for Moving towards fixed point");
        }
    }
}
