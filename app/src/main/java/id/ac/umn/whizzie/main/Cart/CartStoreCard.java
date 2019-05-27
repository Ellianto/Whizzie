package id.ac.umn.whizzie.main.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartStoreCard {
    private String storeName;
    private String storeImage;
    private String transKey;
    private String storeKey;
    private String noResi;
    private long storeTotal;
    private List<CartItemCard> cicList = new ArrayList<>();
    private boolean isSent, isReceived, genieMode;

    public CartStoreCard(){}

    public CartStoreCard(String storeName, String storeImage, String transKey, String storeKey, String noResi, long storeTotal, List<CartItemCard> newList, boolean isSent, boolean isReceived, boolean genieMode) {
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.transKey = transKey;
        this.storeKey = storeKey;
        this.noResi = noResi;
        this.storeTotal = storeTotal;
        this.isSent = isSent;
        this.isReceived = isReceived;
        this.genieMode = genieMode;

        for(int i = 0; i < newList.size(); i++)
            cicList.add(newList.get(i));
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public boolean isGenieMode() {
        return genieMode;
    }

    public void setGenieMode(boolean genieMode) {
        this.genieMode = genieMode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getTransKey() {
        return transKey;
    }

    public void setTransKey(String transKey) {
        this.transKey = transKey;
    }

    public String getStoreKey() {
        return storeKey;
    }

    public void setStoreKey(String storeKey) {
        this.storeKey = storeKey;
    }

    public String getNoResi() {
        return noResi;
    }

    public void setNoResi(String noResi) {
        this.noResi = noResi;
    }

    public long getStoreTotal() {
        return storeTotal;
    }

    public void setStoreTotal(long storeTotal) {
        this.storeTotal = storeTotal;
    }

    public List<CartItemCard> getCicList() {
        return cicList;
    }

    public void setCicList(List<CartItemCard> cicList) {
        this.cicList = cicList;
    }
}
