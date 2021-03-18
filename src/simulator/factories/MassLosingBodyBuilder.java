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
        try {
            return new MassLosingBody(info.getString("id"), new Vector2D(info.getJSONArray("v").getInt(0), info.getJSONArray("v").getInt(1)), new Vector2D(info.getJSONArray("p").getInt(0), info.getJSONArray("p").getInt(1)), info.getInt("m"), info.getDouble("freq"), info.getDouble("factor"));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }
}