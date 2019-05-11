package id.ac.umn.whizzie.main.Wishes;

public class WishesCard {
    private String profileName;
    private String wishesDesc;
    private String wishesName;
    private String wishPic;
    private long wishesCount;

    public WishesCard() {
    }

    public WishesCard(String profileName, String wishesDesc, String wishesName, String wishPic, long wishesCount) {
        this.profileName = profileName;
        this.wishesDesc = wishesDesc;
        this.wishesName = wishesName;
        this.wishPic = wishPic;
        this.wishesCount = wishesCount;
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
