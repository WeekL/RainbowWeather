package weekl.weatherdemo.view;

import weekl.weatherdemo.base.IView;
import weekl.weatherdemo.bean.City;
import weekl.weatherdemo.bean.Weather;

public interface IWeatherView extends IView {
    void addWeather(Weather weather);
    void stopRefresh();
}
