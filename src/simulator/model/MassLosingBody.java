package simulator.model;

import simulator.misc.Vector2D;

public class MassLosingBody extends Body{

    private double _lossFactor, _lossFreq;

    public MassLosingBody(String str, Vector2D v, Vector2D p, double mass, double lossFactor, double lossFreq) {
        super(str, v, p, mass);
        _lossFactor = lossFactor;
        _lossFreq = lossFreq;
    }
}
