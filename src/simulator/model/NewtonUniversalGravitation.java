package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {
	
	private Double _G;
	
	public NewtonUniversalGravitation(double G){
		_G = G;
	}
	
	public void apply(List<Body> bs){
		for(int i = 0; i < bs.size(); ++i)
			for(int j = i + 1; j < bs.size(); ++j){
				 computeForce(bs.get(i), bs.get(j));
			}
	}

	//computes the corresponding Newton's Universal Gravitation force, and adds it to b1 and b2's forces
	private void computeForce(Body b1, Body b2) {
		Vector2D dirB1ToB2 =  b2.getPosition().minus(b1.getPosition());
		double distance = dirB1ToB2.magnitude();
		if(distance > 0 && b1.getMass() > 0 && b2.getMass() > 0){
			Vector2D dirUnityV = dirB1ToB2.direction();
			double factor = _G*b1.getMass()*b2.getMass() / (distance*distance);
			
			Vector2D result = dirUnityV.scale(factor);

			b1.addForce(b1.getForce().plus(result));
			b2.addForce(b2.getForce().plus(result.scale(-1))); //opposite directions, same magnitude
		}
	}

	public String toString(){
		return "Newton’s Universal Gravitation with G =" + _G;
	}

}
