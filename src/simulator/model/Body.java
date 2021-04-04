package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class Body {
	protected String id;
	protected Vector2D v, f, p;
	protected double m;
	
	public static void main(String[] args) {
		test1();
	}
	

    public static void test1(){ //TODO remove
        Body b1 = new MassLosingBody("1", new Vector2D(), new Vector2D(), 10, 0.1, 3);
		Body b2 = new MassLosingBody("2", new Vector2D(), new Vector2D(1, 1), 10, 0.1, 3);
		double t = 0.5;
		List<Body> bs = new ArrayList<>();
		bs.add(b1);
		bs.add(b2);
		double G = 6.67E-11; 
		NewtonUniversalGravitation f = new NewtonUniversalGravitation(G);
		f.apply(bs);
		for (int i = 0; i < 300; i++) {
			System.out.println("f: " + b1.getForce() + ", p: " + b1.getPosition());
			b1.move(t);
			System.out.println("f: " + b2.getForce() + ", p: " + b2.getPosition());
			b2.move(t);
		}
    }
	
	public static void test2() { // TODO remove
		Body b1 = new MassLosingBody("1", new Vector2D(), new Vector2D(), 100, 0.1, 3);
		double t = 0.5;
		b1.addForce(new Vector2D(100, 50));
		for (int i = 0; i < 300; i++) {
			System.out.println("v: " + b1.getVelocity() + ", p: " + b1.getPosition());
			b1.move(t);
		}
	}
	
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
	
	public void move(double t) {
		Vector2D a;
		if (m != 0)
			a = f.scale(1/m);
		else 
			a = new Vector2D();
		p = p.plus(v.scale(t)).plus(a.scale(0.5*t*t));
		v = v.plus(a.scale(t));
	}
	
	public String toString() {
		return null;
	}

	//equal bodies if they have the same identifier
	public boolean equals(Body b){
		return id.equals(b.getId());
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
