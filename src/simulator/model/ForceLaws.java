package simulator.model;

import java.util.List;

public interface ForceLaws {
	default public void apply(List<Body> bs){ /*do nothing*/ }
}
