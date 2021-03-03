package simulator.model;

import simulator.misc.Vector2D;

public class MassLosingBody extends Body{

    private final double _lossFactor, _lossFreq;
    private double timeCount;

    public MassLosingBody(String str, Vector2D v, Vector2D p, double mass, double lossFactor, double lossFreq) {
        super(str, v, p, mass);
        _lossFactor = lossFactor;
        _lossFreq = lossFreq;
        timeCount = 0;
    }
	
	
	@Override
	void move(double t) {
		super.move(t);
		
		timeCount += t;
		if (timeCount >= _lossFreq) {
			m = m*(1-_lossFactor);
			timeCount = 0;
		}
	}
}


