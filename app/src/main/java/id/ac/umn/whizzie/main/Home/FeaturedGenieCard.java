package id.ac.umn.whizzie.main.Home;

public class FeaturedGenieCard {
    private String genie_uid;
    private String genie_name;
    private String image_url;

    public FeaturedGenieCard() {
    }

    public FeaturedGenieCard(String genie_uid, String genie_name, String image_url) {
        this.genie_uid = genie_uid;
        this.genie_name = genie_name;
        this.image_url = image_url;
    }

    public String getGenie_uid() {
        return genie_uid;
    }

    public void setGenie_uid(String genie_uid) {
        this.genie_uid = genie_uid;
    }

    public String getGenie_name() {
        return genie_name;
    }

    public void setGenie_name(String genie_name) {
        this.genie_name = genie_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
