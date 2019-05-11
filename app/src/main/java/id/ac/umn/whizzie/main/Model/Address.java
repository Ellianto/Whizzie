package id.ac.umn.whizzie.main.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Address {
    public String cityName;
    public String detailAddress;
    public String phoneNum;
    public int postalCode;
    public String provinceName;
    public String receiverName;


    public Address() {
    }

    public Address(String cityName, String detailAddress, String phoneNum, int postalCode, String provinceName, String receiverName) {
        this.cityName = cityName;
        this.detailAddress = detailAddress;
        this.phoneNum = phoneNum;
        this.postalCode = postalCode;
        this.provinceName = provinceName;
        this.receiverName = receiverName;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("cityName", cityName);
        result.put("detailAddress", detailAddress);
        result.put("phoneNum", phoneNum);
        result.put("postalCode", postalCode);
        result.put("provinceName", provinceName);
        result.put("receiverName", receiverName);

        return result;
    }
}
