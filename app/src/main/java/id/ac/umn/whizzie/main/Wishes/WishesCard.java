package id.ac.umn.whizzie.main.Wishes;

public class WishesCard {
    private String profileName;
    private String userKey;
    private String wishesDesc;
    private String wishesName;
    private String wishPic;
    private String wishKey;
    private long wishesCount;

    public WishesCard() {}

    public WishesCard(String profileName, String userKey, String wishesDesc, String wishesName, String wishPic, String wishKey, long wishesCount) {
        this.profileName = profileName;
        this.userKey = userKey;
        this.wishesDesc = wishesDesc;
        this.wishesName = wishesName;
        this.wishPic = wishPic;
        this.wishKey = wishKey;
        this.wishesCount = wishesCount;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getWishKey() {
        return wishKey;
    }

    public void setWishKey(String wishKey) {
        this.wishKey = wishKey;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getWishesDesc() {
        return wishesDesc;
    }

    public void setWishesDesc(String wishesDesc) {
        this.wishesDesc = wishesDesc;
    }

    public String getWishesName() {
        return wishesName;
    }

    public void setWishesName(String wishesName) {
        this.wishesName = wishesName;
    }

    public String getWishPic() {
        return wishPic;
    }

    public void setWishPic(String wishPic) {
        this.wishPic = wishPic;
    }

    public long getWishesCount() {
        return wishesCount;
    }

    public void setWishesCount(long wishesCount) {
        this.wishesCount = wishesCount;
    }
}
