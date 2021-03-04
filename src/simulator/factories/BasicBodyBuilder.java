package simulator.factories;

import netscape.javascript.JSObject;

public class BasicBodyBuilder extends Builder<Body> {
    public JSONObject getBuilderInfo() {
        JSONObject o = super.getBuilderInfo();
        JSONObject data = new JSObject();
        data.put("id", null);
    }
}