package simulator.model;
import simulator.misc.Vector2D;

public class Body {
	protected String id;
	protected Vector2D v, f, p;
	protected double m;
	
	public static void main(String[] args) {
		test1();
	}
	

    public static void test1(){ //TODO remove
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
		return null;
	}
}

