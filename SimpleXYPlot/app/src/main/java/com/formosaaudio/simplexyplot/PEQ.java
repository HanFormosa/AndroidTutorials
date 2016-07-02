package com.formosaaudio.simplexyplot;

/**
 * Created by tzehan on 02/07/16.
 */
public class PEQ {

    public static final int kLPF = 0;
    public static final double kSAMPLESIZE = 48000;
    private double freq;
    private double Q;

    private double[] b;
    private double[] a;

    private double w0;
    private double alpha;

    private int mode;

    public PEQ(double __freq, double __Q) {
        this.freq  = __freq;
        this.Q = __Q;

        b = new double[3];
        a = new double[3];
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public double getQ() {
        return Q;
    }

    public void setQ(double q) {
        Q = q;
    }

    public void calcCoeffs(int mode){
        switch(mode){
            case kLPF:
                //a0 : etc.
                alpha = Math.sin(getW0())/(2*Q);
                b[0] = (1 - Math.cos(getW0()))/2;
                b[1] = 1 - Math.cos(getW0());
                b[2] = (1 - Math.cos(getW0()))/2;
                a[0] = 1 + alpha;
                a[1] = -2 * Math.cos(getW0());
                a[2] = 1 - alpha;
                break;
            default:
                break;
        }
    }

    public double[] getCoefsA() {
        return a;
    }

    public double[] getCoefsB() {
        return b;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getW0() {
        w0 = (2*Math.PI*getFreq())/kSAMPLESIZE;
        return w0;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
