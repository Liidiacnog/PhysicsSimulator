package simulator.model;

import org.json.JSONObject;
import simulator.misc.Vector2D;

public class Body {
	private String id;
	private Vector2D v, f, p;
	protected double m;
	
	public Body(String str, Vector2D v, Vector2D p, double mass) {
		id = str;
		this.v = v;
		this.p = p;
		m = mass;
		resetForce();
	}
	
	public String getId() {
		return id;
	}
	
	public Vector2D getVelocity() {
		return v;
	}
	
	public Vector2D getPosition() {
		return p;
	}

	public void setPosition(double x, double y) {
		p = new Vector2D(x, y);
	}
	
	public Vector2D getForce() {
		return f;
	}
	
	public double getMass() {
		return m;
	}
	
	void addForce(Vector2D f) {
		this.f = this.f.plus(f);
	}
	
	void resetForce() {
		f = new Vector2D();
	}
	
	void move(double t) {
		Vector2D a;
		if (m != 0)
			a = f.scale(1/m);
		else 
			a = new Vector2D();
		p = p.plus(v.scale(t)).plus(a.scale(0.5*t*t));
		v = v.plus(a.scale(t));
	}
	
	public String toString() {
		return getState().toString();
	}

	//equal bodies if they have the same identifier
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(o.getClass() != getClass())
			return false;

		Body b = (Body) o;
		if(b.getId() == null)
			if(id == null)
				return true;
		else if(id.equals(b.getId()))
			return true;
		
		return false;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;	
	}


/*returns the JSON structure that includes the body’s information:
{ "id": id, "m": m, "p": p, "v": v, "f": f } */
	public JSONObject getState() {
		JSONObject jo = new JSONObject();
    	jo.put("id", id);
		jo.put("m", m);
		jo.put("p", p.asJSONArray());
		jo.put("v", v.asJSONArray());
		jo.put("f", f.asJSONArray());

		return jo;
	}
}
