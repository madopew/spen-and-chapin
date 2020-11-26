package main;

public class SpenItem {

    private String spenName;
    private String spen;

    public SpenItem(String spenName, String spen) {
        this.spenName = spenName;
        this.spen = spen;
    }

    public String getSpenName() {
        return spenName;
    }

    public String getSpen() {
        return spen;
    }

    public void setSpenName(String spenName) {
        this.spenName = spenName;
    }

    public void setSpen(String spen) {
        this.spen = spen;
    }
}
