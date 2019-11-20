package Util;

public enum Branch {
    VAN1("Yaletown", "Vancouver"),
    VAN2("Granville", "Vancouver"),
    TOR1("Younge St.", "Toronto"),
    NYC1("Queens", "New York City");

    private String loc;
    private String city;

    Branch(String location, String city) {
        loc = location;
        this.city = city;
    }

    public String getLoc() {
        return loc;
    }

    public String getCity() {
        return city;
    }
}
