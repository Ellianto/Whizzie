package id.ac.umn.whizzie.main.Transaction;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.main.Cart.CartStoreCard;

public class TransactionCard {
    private String transactionID;
    private String address;
    private String timestamp;
    private long totalHarga;
    private List<CartStoreCard> scList;

    public TransactionCard() {

    }

    public TransactionCard(String transactionID, String address, String timestamp, long totalHarga, List<CartStoreCard> newList) {
        this.transactionID = transactionID;
        this.address = address;
        this.timestamp = timestamp;
        this.totalHarga = totalHarga;

        this.scList = new ArrayList<>();

        for(int i = 0; i < newList.size(); i++)
            this.scList.add(newList.get(i));
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(long totalHarga) {
        this.totalHarga = totalHarga;
    }

    public List<CartStoreCard> getScList() {
        return scList;
    }

    public void setScList(List<CartStoreCard> scList) {
        this.scList = scList;
    }
}
