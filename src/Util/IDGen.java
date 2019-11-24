package Util;

public class IDGen {
    private static int confNum;
    private static int rid;

    static int getNextConfNum() {
        confNum++;
        return confNum;
    }

    static int getNextRID() {
        rid++;
        return rid;
    }
}
