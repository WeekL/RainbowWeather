package weekl.weatherdemo.bean.weather;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    //白天天气状况代码
    @SerializedName("cond_code_d")
    public int dayCode;

    //夜间天气状况代码
    @SerializedName("cond_code_n")
    public int nightCode;

    //白天天气状况描述
    @SerializedName("cond_txt_d")
    public String dayText;

    //夜间天气状况描述
    @SerializedName("cond_txt_n")
    public String nightText;

    //日期
    public String date;

    //最高温
    @SerializedName("tmp_max")
    public int tmpMax;

    //最低温
    @SerializedName("tmp_min")
    public int tmpMin;

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

    //降水概率
    public int pop;

    //大气压强
    public int pres;

    //紫外线线强度
    @SerializedName("uv_index")
    public int uvIndex;

    //能见度
    public int vis;

    //日出时间
    public String sr;

    //日落时间
    public String ss;

    //月升时间
    public String mr;

    //月落时间
    public String ms;
}
