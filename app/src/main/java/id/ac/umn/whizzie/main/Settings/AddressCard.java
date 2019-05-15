package id.ac.umn.whizzie.main.Settings;

public class AddressCard {
    private String nameAddress;
    private String cityName;
    private String detailAddress;
    private String phoneNumber;
    private String postalCode;
    private String provinceName;
    private String receiverName;
    private boolean store;

    public AddressCard(String nameAddress, String cityName, String detailAddress, String phoneNumber, String postalCode, String provinceName, String receiverName, boolean store) {
        this.nameAddress = nameAddress;
        this.cityName = cityName;
        this.detailAddress = detailAddress;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.provinceName = provinceName;
        this.receiverName = receiverName;
        this.store = store;
    }

    public String getNameAddress() {
        return nameAddress;
    }

    public void setNameAddress(String nameAddress) {
        this.nameAddress = nameAddress;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }
}
