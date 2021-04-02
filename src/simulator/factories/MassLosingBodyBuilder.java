package simulator.factories;

import simulator.model.Body;
import org.json.*;
import simulator.model.MassLosingBody;
import simulator.misc.*;

public class MassLosingBodyBuilder extends Builder<Body> {


    private static String MassLosingBodyBuilderType = "mlb";
    private static final String MassLosingBodyBuilderTypeDesc = "Body which loses mass by a certain lossFactor, every lossFrequency value of time";

    public MassLosingBodyBuilder(){
        _type = MassLosingBodyBuilderType;
        _desc = MassLosingBodyBuilderTypeDesc;
    }

    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("id", "Body id");
        data.put("v", "Velocity vector");
        data.put("p", "Position vector");
        data.put("m", "Mass of the body");
        data.put("freq", "Mass losing frequency");
        data.put("factor", "Mass losing factor");
        return data;
    }

    protected MassLosingBody createNewT(JSONObject info) {
        try {
            Vector2D p = new Vector2D(info.getJSONArray("p"));
            Vector2D v = new Vector2D(info.getJSONArray("v"));
            return new MassLosingBody(info.getString("id"), v, p, info.getDouble("m"), info.getDouble("freq"), info.getDouble("factor"));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Mass losing body could not be created with given data");
        }
    }
}