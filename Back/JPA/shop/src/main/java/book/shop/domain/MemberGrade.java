package book.shop.domain;

public enum MemberGrade {
    GOLD(10),
    SILVER(5),
    BRONZE(0);

    private final int discountRate; // %

    MemberGrade(int discountRate) {
        this.discountRate = discountRate;
    }

    public long calculateRateByGrade(long price) {
        return price * (100 - discountRate) / 100;
    }
}
