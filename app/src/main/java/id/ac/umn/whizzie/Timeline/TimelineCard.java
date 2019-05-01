package id.ac.umn.whizzie.Timeline;

public class TimelineCard {
    private String profileName;
    private String productDesc;
    private int productPrice;

    public TimelineCard(String profileName, String productDesc, int productPrice) {
        this.profileName = profileName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
