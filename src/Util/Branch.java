package Util;

public enum Branch {
    VAN1("Robson St. - Vancouver"),
    VAN2("YVR - Vancouver"),
    VAN3("Granville St. - Vancouver"),
    TOR1("Yonge St. - Toronto");

    private String loc;

    Branch(String location_city) {
        loc = location_city;
    }

    public String getLoc() {
        return loc;
    }

    public Branch getDefault() {
        return VAN1;
    }
}
