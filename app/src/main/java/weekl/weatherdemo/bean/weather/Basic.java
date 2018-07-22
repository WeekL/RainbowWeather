package weekl.weatherdemo.bean.weather;

import com.google.gson.annotations.SerializedName;

public class Basic {

    //天气id
    @SerializedName("cid")
    public String weatherId;

    //城市名
    public String location;

    //所属城市
    @SerializedName("parent_city")
    public String parentCity;

    //行政区域（省份）
    @SerializedName("admin_area")
    public String adminArea;

    //维度
    public double lat;

    //经度
    public double lon;
}
