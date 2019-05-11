package id.ac.umn.whizzie.main.Model;

public class Wishes {
    private String category;
    private String descWish;
    private String pictureWish;
    private String timeWish;
    private String titleWish;
    private String uidUpWish;
    private long offerCount;

    public Wishes() {
    }

    // Constructor tidak termasuk offerCount karena offerCount akan di set manual melalui Query terpisah
    public Wishes(String category, String descWish, String pictureWish, String timeWish, String titleWish, String uidUpWish) {
        this.category = category;
        this.descWish = descWish;
        this.pictureWish = pictureWish;
        this.timeWish = timeWish;
        this.titleWish = titleWish;
        this.uidUpWish = uidUpWish;
        this.offerCount = 0;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescWish() {
        return descWish;
    }

    public void setDescWish(String descWish) {
        this.descWish = descWish;
    }

    public String getPictureWish() {
        return pictureWish;
    }

    public void setPictureWish(String pictureWish) {
        this.pictureWish = pictureWish;
    }

    public String getTimeWish() {
        return timeWish;
    }

    public void setTimeWish(String timeWish) {
        this.timeWish = timeWish;
    }

    public String getTitleWish() {
        return titleWish;
    }

    public void setTitleWish(String titleWish) {
        this.titleWish = titleWish;
    }

    public String getUidUpWish() {
        return uidUpWish;
    }

    public void setUidUpWish(String uidUpWish) {
        this.uidUpWish = uidUpWish;
    }

    public long getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(long offerCount) {
        this.offerCount = offerCount;
    }
}
