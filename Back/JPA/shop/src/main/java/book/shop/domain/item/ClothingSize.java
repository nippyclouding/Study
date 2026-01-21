package book.shop.domain.item;

public enum ClothingSize {

    // 상의 (TOP)
    TOP_80("TOP", 80),
    TOP_85("TOP", 85),
    TOP_90("TOP", 90),
    TOP_95("TOP", 95),
    TOP_100("TOP", 100),
    TOP_105("TOP", 105),
    TOP_110("TOP", 110),
    TOP_115("TOP", 115),

    // 하의 (BOTTOM)
    BOTTOM_22("BOTTOM", 22),
    BOTTOM_24("BOTTOM", 24),
    BOTTOM_26("BOTTOM", 26),
    BOTTOM_28("BOTTOM", 28),
    BOTTOM_30("BOTTOM", 30),
    BOTTOM_32("BOTTOM", 32),
    BOTTOM_34("BOTTOM", 34),
    BOTTOM_36("BOTTOM", 36),
    BOTTOM_38("BOTTOM", 38),
    BOTTOM_40("BOTTOM", 40);

    private final String type; // TOP or BOTTOM
    private final int value;

    ClothingSize(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    // 팩토리 메서드: 상의만 가져오기
    public static ClothingSize[] top() {
        return new ClothingSize[] {
                TOP_80, TOP_85, TOP_90, TOP_95, TOP_100, TOP_105, TOP_110, TOP_115
        };
    }

    // 팩토리 메서드: 하의만 가져오기
    public static ClothingSize[] bottom() {
        return new ClothingSize[] {
                BOTTOM_22, BOTTOM_24, BOTTOM_26, BOTTOM_28, BOTTOM_30, BOTTOM_32, BOTTOM_34, BOTTOM_36, BOTTOM_38, BOTTOM_40
        };
    }
}
