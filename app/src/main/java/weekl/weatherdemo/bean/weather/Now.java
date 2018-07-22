package weekl.weatherdemo.bean.weather;

import com.google.gson.annotations.SerializedName;

public class Now {

    //体感温度
    @SerializedName("fl")
    public int feel;

    //温度
    public int tmp;

    //实况天气代码
    @SerializedName("cond_code")
    public int code;

    //实况天气状况描述
    @SerializedName("cond_txt")
    public String text;

    //风向360角度
    @SerializedName("wind_deg")
    public int windDeg;

    //风向
    @SerializedName("wind_dir")
    public String windDir;

    //风力
    @SerializedName("wind_sc")
    public String windSc;

    //风速
    @SerializedName("wind_spd")
    public int windSpd;

    //相对湿度
    public int hum;

    //降水量
    public float pcpn;

    //大气压强
    public int pres;

    //能见度
    public int vis;

    //云量
    public int cloud;
}
