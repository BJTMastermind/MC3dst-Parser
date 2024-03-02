package me.bjtmastermind.mc3dstparser;

public enum ColorFormat {
    UNKNOWN(-1),
    ETC2_RGBA8(0),
    RGB(1),
    ABGR1555(2);

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
                return ETC2_RGBA8;
            case 1:
                return RGB;
            case 2:
                return ABGR1555;
            default:
                return UNKNOWN;
        }
    }
}
