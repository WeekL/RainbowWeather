package weekl.weatherdemo.bean.weather;

import com.google.gson.annotations.SerializedName;

public class Hourly {
    public String time;

    public String tmp;

    //天气状况代码
    @SerializedName("cond_code")
    public int code;

    //天气状况描述
    @SerializedName("cond_txt")
    public String txt;

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

    //大气压强
    public int pres;

    //降水概率
    public int pop;

    //云量
    public int cloud;
}
