package me.bjtmastermind.mc3dstparser;

public enum ColorFormat {
    UNKNOWN(-1),
    ABGR(0),
    BGR(1),
    RGBA5551(2);

    private int value;

    private ColorFormat(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ColorFormat formatFromId(int id) {
        switch (id) {
            case 0:
                return ABGR;
            case 1:
                return BGR;
            case 2:
                return RGBA5551;
            default:
                return UNKNOWN;
        }
    }
}
