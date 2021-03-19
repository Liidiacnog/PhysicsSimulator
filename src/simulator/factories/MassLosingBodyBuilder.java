package simulator.factories;

import simulator.model.Body;
import org.json.JSONObject;
import simulator.model.MassLosingBody;
import simulator.misc.*;

public class MassLosingBodyBuilder extends Builder<Body> {


    private static String MassLosingBodyBuilderType = "mlb";

    public MassLosingBodyBuilder(){
        _type = MassLosingBodyBuilderType;
    }

    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("id", "Body id");
        data.put("v", "Velocity vector");
        data.put("p", "Position vector");
        data.put("m", "Mass of the body");
        data.put("freq", "Losing mass frequency");
        data.put("factor", "Losing mass factor");

        return data;
    }

    protected MassLosingBody createNewT(JSONObject info) {
        Vector2D v = (Vector2D) info.get("v");
        Vector2D p = (Vector2D) info.get("p");
        try {
            return new MassLosingBody(info.getString("id"), v, p, info.getDouble("m"), info.getDouble("freq"), info.getDouble("factor"));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }
}