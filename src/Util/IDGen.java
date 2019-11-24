package Util;

public class IDGen {
    private static int confNum;
    private static int rid;

    public static int getNextConfNum() {
        confNum++;
        return confNum;
    }

    public static int getNextRID() {
        rid++;
        return rid;
    }
}
