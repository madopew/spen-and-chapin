package main;

public class Item {
    private String p;
    private String m;
    private String c;
    private String t;
    private String IOp;
    private String IOm;
    private String IOc;
    private String IOt;

    public Item(String p, String m, String c, String t, String IOp, String IOm, String IOc, String IOt) {
        this.p = p;
        this.m = m;
        this.c = c;
        this.t = t;
        this.IOp = IOp;
        this.IOm = IOm;
        this.IOc = IOc;
        this.IOt = IOt;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getIOp() {
        return IOp;
    }

    public void setIOp(String IOp) {
        this.IOp = IOp;
    }

    public String getIOm() {
        return IOm;
    }

    public void setIOm(String IOm) {
        this.IOm = IOm;
    }

    public String getIOc() {
        return IOc;
    }

    public void setIOc(String IOc) {
        this.IOc = IOc;
    }

    public String getIOt() {
        return IOt;
    }

    public void setIOt(String IOt) {
        this.IOt = IOt;
    }
}