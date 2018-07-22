package weekl.weatherdemo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import weekl.weatherdemo.bean.weather.Basic;
import weekl.weatherdemo.bean.weather.Forecast;
import weekl.weatherdemo.bean.weather.Hourly;
import weekl.weatherdemo.bean.weather.LifeStyle;
import weekl.weatherdemo.bean.weather.Now;

public class Weather {

    private String responseText;

    public String status;

    public Basic basic;

    public Update update;

    public Now now;

    public List<Hourly> hourly;

    @SerializedName("daily_forecast")
    public List<Forecast> forecasts;

    @SerializedName("lifestyle")
    public List<LifeStyle> lifeStyles;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

    public void setResponseText(String responseText){
        this.responseText = responseText;
    }

    public String getResponseText(){
        return responseText;
    }
}
