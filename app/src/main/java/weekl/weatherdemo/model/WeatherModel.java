package weekl.weatherdemo.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import weekl.weatherdemo.R;
import weekl.weatherdemo.base.BaseApplication;
import weekl.weatherdemo.bean.City;
import weekl.weatherdemo.bean.Weather;
import weekl.weatherdemo.util.HttpUtil;

/**
 * Weather数据源
 */
public class WeatherModel {
    private static final String TAG = "WeatherModel";

    //请求链接
    private static final String BASE_URL = "https://free-api.heweather.com/s6/weather?";
    //和风天气认证key
    private static final String KEY = BaseApplication.getContext().getResources().getString(R.string.weather_key);

    //用于保存天气数据的SharedPreferences
    private static SharedPreferences weatherPref = BaseApplication.getContext()
            .getSharedPreferences("weather_data", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor weatherEditor = weatherPref.edit();

    //用于保存搜索历史的SharedPreferences
    private static final String CITY_HISTORY = "city_history";
    private static final String HOT_CITY = "hot_city";
    private static SharedPreferences cityPref = BaseApplication.getContext()
            .getSharedPreferences("city_data", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor cityEditor = cityPref.edit();

    /**
     * 更新当前显示地点列表的天气
     */
    public static void getWeather(RequestCallback callback) {
        for (String location : getLocationSet()) {
            getWeather(location, callback);
        }
    }

    /**
     * 使用地点、天气id或地点拼音发送请求
     *
     * @param location
     * @param requestCallback
     */
    public static void getWeather(String location, RequestCallback requestCallback) {
        String weatherUrl = BASE_URL + "location=" + location + "&key=" + KEY;
        Log.d(TAG, "getWeather: " + weatherUrl);
        sendRequest(weatherUrl, requestCallback);
    }

    /**
     * 使用地点+所属城市发送请求
     *
     * @param city            地点
     * @param parentCity      地点所属城市
     * @param requestCallback
     */
    public static void getWeather(String city, String parentCity, RequestCallback requestCallback) {
        String weatherUrl = BASE_URL + "location=" + city + "," + parentCity + "&key=" + KEY;
        Log.d(TAG, "getWeather: " + weatherUrl);
        sendRequest(weatherUrl, requestCallback);
    }

    /**
     * 使用经纬度发送请求
     *
     * @param longitude
     * @param latitude
     * @param requestCallback
     */
    public static void getWeather(double longitude, double latitude, RequestCallback requestCallback) {
        String weatherUrl = BASE_URL + "location=" + longitude + "," + latitude + "&key=" + KEY;
        Log.d(TAG, "getWeather: " + weatherUrl);
        sendRequest(weatherUrl, requestCallback);
    }

    /**
     * 发送网络请求
     *
     * @param url
     * @param requestCallback
     */
    private static void sendRequest(String url, final RequestCallback requestCallback) {
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestCallback.onFailed("天气数据请求失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (null == responseText) {
                    requestCallback.onFailed("请求错误");
                }
                Weather weather = jsonToWeather(responseText);
                Log.d(TAG, "onResponse: " + responseText);
                if (weather == null) {
                    requestCallback.onFailed("解析异常");
                } else if (!weather.status.equals("ok")) {
                    requestCallback.onFailed("请求错误，status：" + weather.status);
                } else {
                    requestCallback.onSuccess(weather);
                    //解析成功后直接保存本地
                    save(weather);
                }
            }
        });
    }

    /**
     * 保存一条天气数据
     *
     * @param weather 保存的天气数据
     */
    public static void save(Weather weather) {
        String location = weather.basic.location;
        String updateTime = weather.update.updateTime;
        String responseText = weather.getResponseText();
        String cache = weatherPref.getString(location, null);
        if (null != cache) {
            Weather w = jsonToWeather(cache);
            if (null != w) {
                String time = w.update.updateTime;
                if (updateTime.equals(time)) {
                    return;
                }
            }
        }
        weatherEditor.putString(location, responseText).apply();
    }

    /**
     * 删除一条数据
     */
    public static void delete(String location) {
        weatherEditor.remove(location);
        weatherEditor.apply();
    }

    /**
     * 获取保存的城市
     */
    public static Set<String> getLocationSet() {
        Map<String, ?> weathers = weatherPref.getAll();
        return weathers.keySet();
    }

    /**
     * 获取本地存储的天气数据
     *
     * @return
     */
    public static void getCache(LoadCacheCallback callback) {
        Map<String, ?> weathers = weatherPref.getAll();
        if (weathers.isEmpty()) {
            Log.e(TAG, "getCache: caches is empty.");
            return;
        }
        for (Map.Entry<String, ?> entry : weathers.entrySet()) {
            String location = entry.getKey();
            String weatherText = entry.getValue().toString();
            Weather weather = jsonToWeather(weatherText);
            if (weather != null) {
                callback.onSuccess(weather);
            } else {
                Log.e(TAG, "loadCache: get '" + location + "' data error!");
            }
        }
    }

    /**
     * 处理响应数据
     *
     * @param response
     * @return
     */
    private static Weather jsonToWeather(String response) {
        if (TextUtils.isEmpty(response)) {
            Log.e(TAG, "jsonToWeather: response id empty :" + response);
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            JSONObject object = jsonArray.getJSONObject(0);
            String weatherContent = object.toString();
            Weather weather = new Gson().fromJson(weatherContent, Weather.class);
            weather.setResponseText(response);
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找城市
     *
     * @param name
     * @param callback
     */
    public static void searchCity(String name, final RequestCityCallback callback) {
        String url = "https://search.heweather.com/find?group=cn&key=" + KEY + "&location=" + name;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                List<City> cities = handleCityResponse(responseText);
                if (cities == null) {
                    callback.onFailed("解析失败");
                } else if (cities.size() == 0) {
                    callback.onFailed("查询结果为空");
                } else {
                    callback.onSuccess(cities);
                }
            }
        });
        saveSearchHistory(name);
    }

    /**
     * 保存搜索记录
     *
     * @param name
     */
    private static void saveSearchHistory(String name) {
        Set<String> cache = cityPref.getStringSet(CITY_HISTORY, null);
        if (cache == null) {
            cache = new HashSet<>();
        }
        cache.add(name);
        cityEditor.putStringSet(CITY_HISTORY, cache).apply();
    }

    /**
     * 获取搜索历史
     *
     */
    public static List<String> getSearchHistory() {
        Set<String> cache = cityPref.getStringSet(CITY_HISTORY, null);
        if (cache == null){
            return null;
        }
        return new ArrayList<>(cache);
    }

    public static List<String> loadHotCity() {
        Set<String> cache = cityPref.getStringSet(HOT_CITY, null);
        if(cache == null){
            return null;
        }
        return new ArrayList<>(cache);
    }

    public static void deleteHistory(String name) {
        Set<String> cities = cityPref.getStringSet(CITY_HISTORY, null);
        if (cities != null) {
            cities.remove(name);
        }
        cityEditor.putStringSet(CITY_HISTORY, cities).apply();
    }

    public static void getHotCity(int count, final RequestCityNameCallback callback) {
        String url = "https://search.heweather.com/top?group=cn&key=" + KEY + "&number=" + count;
        Log.d(TAG, "getHotCity: " + url);
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                List<City> cities = handleCityResponse(responseText);
                if (cities != null && cities.size() > 0) {
                    List<String> data = new ArrayList<>();
                    for (City city : cities) {
                        data.add(city.location);
                    }
                    callback.onSuccess(data);
                    cityEditor.putStringSet(HOT_CITY, new HashSet<>(data)).apply();
                }
            }
        });
    }

    private static List<City> handleCityResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            Log.e(TAG, "handleCityResponse: response id empty :" + response);
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            JSONObject object = jsonArray.getJSONObject(0);
            String status = object.getString("status");
            if (status.equals("ok")) {
                JSONArray cities = object.getJSONArray("basic");
                List<City> data = new ArrayList<>();
                for (int i = 0; i < cities.length(); i++) {
                    JSONObject city = cities.getJSONObject(i);
                    String location = city.getString("location");
                    String parentCity = city.getString("parent_city");
                    String adminArea = city.getString("admin_area");
                    data.add(new City(location, parentCity, adminArea));
                }
                return data;
            } else {
                Log.e(TAG, "handleCityResponse: status = " + status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求网络数据的回调接口
     */
    public interface RequestCallback {
        void onSuccess(Weather weather);

        void onFailed(String reason);
    }

    /**
     * 加载缓存数据的回调接口
     */
    public interface LoadCacheCallback {
        void onSuccess(Weather... weathers);
    }

    /**
     * 请求热门城市
     */
    public interface RequestCityCallback {
        void onSuccess(List<City> cities);

        void onFailed(String error);
    }

    public interface RequestCityNameCallback {
        void onSuccess(List<String> cities);

        void onFailed(String error);
    }
}
