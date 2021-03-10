package simulator.factories;

import org.json.JSONObject;

import netscape.javascript.JSObject;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {
    public JSONObject getBuilderInfo() {
        JSONObject o = super.getBuilderInfo();
        JSONObject data = new JSONObject();
        data.put("id", null);
    }
}