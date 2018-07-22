package weekl.weatherdemo.presenter;

import android.location.Location;
import android.util.Log;

import weekl.weatherdemo.base.Presenter;
import weekl.weatherdemo.bean.Weather;
import weekl.weatherdemo.model.WeatherModel;
import weekl.weatherdemo.util.LocationUtil;
import weekl.weatherdemo.view.IWeatherView;

import static weekl.weatherdemo.base.BaseApplication.getContext;

/**
 * 天气模块Presenter类
 */
public class WeatherPresenter extends Presenter<IWeatherView> {
    private static final String TAG = "WeatherPresenter";

    //数据请求监听器
    private WeatherModel.RequestCallback requestCallback = new WeatherModel.RequestCallback() {
        @Override
        public void onSuccess(Weather weather) {
            getView().addWeather(weather);
            getView().stopRefresh();
        }

        @Override
        public void onFailed(String reason) {
            getView().stopRefresh();
            getView().showToast(reason);
        }
    };

    /**
     * 如果用户已经添加了城市，则更新城市天气，否则就获取当前定位地点的天气
     */
    public void getWeather() {
        if (WeatherModel.getLocationSet().size() > 0) {
            update();
            return;
        }
        LocationUtil.getLocation(getContext(), new LocationUtil.LocationCallback() {
            @Override
            public void onSuccess(Location location) {
                getWeather(location.getLongitude(), location.getLatitude());
            }

            @Override
            public void onFailed(String msg) {
                getView().showToast("定位失败！请手动选择城市");
                Log.e(TAG, "getWeather: 定位失败");
            }
        });
    }

    /**
     * 更新当前添加城市的信息
     */
    public void update() {
        WeatherModel.getWeather(requestCallback);
    }

    /**
     * 获取指定地点的天气
     *
     * @param location weatherId、地点名或拼音
     */
    public void getWeather(String location) {
        if (location == null) {
            getWeather();
        }
        WeatherModel.getWeather(location, requestCallback);
    }

    /**
     * 获取指定地点的天气（精确）
     *
     * @param city       指定地点的城市名或拼音
     * @param parentCity 地点所属城市
     */
    public void getWeather(String city, String parentCity) {
        if (city == null) {
            getWeather();
        } else if (parentCity == null) {
            getWeather(city);
        } else {
            WeatherModel.getWeather(city, parentCity, requestCallback);
        }
    }

    /**
     * 获取指定经纬度的天气
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    public void getWeather(double longitude, double latitude) {
        WeatherModel.getWeather(longitude, latitude, requestCallback);
    }

    /**
     * 删除指定城市的天气数据
     *
     * @param location
     */
    public void delete(String location) {
        WeatherModel.delete(location);
    }

    /**
     * 取消删除
     * @param weather
     */
    public void cancelDelete(Weather weather){
        WeatherModel.save(weather);
    }

    /**
     * 加载缓存
     */
    public void loadCache() {
        WeatherModel.getCache(new WeatherModel.LoadCacheCallback() {
            @Override
            public void onSuccess(Weather... weathers) {
                for (Weather weather : weathers) {
                    getView().addWeather(weather);
                }
            }
        });
    }
}
