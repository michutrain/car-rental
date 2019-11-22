package Util;

public enum VehicleType {
    Economy("2Door, 2WD", 190, 50, 10, 9, 4, 2, 0.4),
    Compact("2Door, 2WD", 150, 40, 8, 5, 3, 1, 0.2),
    Midsize("Heated seats, electric windows", 220, 60, 12, 10, 5, 2.5, 0.4),
    Standard("2Door, 2WD", 230, 65, 12, 6, 3, 1, 1),
    Fullsize("2Door, 2WD", 270, 75, 15, 7.5, 3, 1.2, 1.5),
    SUV("AWD, Heated seats, electric windows", 310, 80, 20, 15, 3, 1, 2),
    Truck("200 HP, 4WD", 310, 90, 22, 8, 4, 2, 2.5);

    private String features;
    private double wrate;
    private double drate;
    private double hrate;
    private double wirate;
    private double dirate;
    private double hirate;
    private double krate;

    VehicleType(String features, double wrate, double drate,
                double hrate, double wirate, double dirate, double hirate, double krate) {
        this.features = features;
        this.wrate = wrate;
        this.drate = drate;
        this.hrate = hrate;
        this.wirate = wirate;
        this.dirate = dirate;
        this.hirate = hirate;
        this.krate = krate;
    }

    public VehicleType getVehicleType(String vtname) throws IllegalArgumentException {
        return VehicleType.valueOf(vtname);
    }

    public String getFeatures() {
        return features;
    }

    public double getWrate() {
        return wrate;
    }

    public double getDrate() {
        return drate;
    }

    public double getHrate() {
        return hrate;
    }

    public double getWirate() {
        return wirate;
    }

    public double getDirate() {
        return dirate;
    }

    public double getHirate() {
        return hirate;
    }

    public double getKrate() {
        return krate;
    }
}
