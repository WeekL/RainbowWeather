package weekl.weatherdemo.presenter;

import android.util.Log;

import java.util.List;

import weekl.weatherdemo.base.Presenter;
import weekl.weatherdemo.bean.City;
import weekl.weatherdemo.bean.Weather;
import weekl.weatherdemo.model.WeatherModel;
import weekl.weatherdemo.view.ISearchView;

public class SearchPresenter extends Presenter<ISearchView> {
    private static final String TAG = "SearchPresenter";

    public void search(String inputText) {
        WeatherModel.searchCity(inputText, new WeatherModel.RequestCityCallback() {
            @Override
            public void onSuccess(List<City> cities) {
                if (isViewAttached()){
                    getView().showResult(cities);
                }
            }

            @Override
            public void onFailed(String error) {
                Log.e(TAG, "onFailed: " + error);
                if (isViewAttached()){
                    getView().showResult(null);
                }
            }
        });
    }

    public void getHotCities() {
        getHotCities(20);
    }

    public void getHotCities(int count) {
        List<String> hots = WeatherModel.loadHotCity();
        if (isViewAttached() && hots != null){
            getView().showHotCities(hots);
        }
        WeatherModel.getHotCity(count, new WeatherModel.RequestCityNameCallback() {
            @Override
            public void onSuccess(List<String> cities) {
                if (isViewAttached()){
                    getView().showHotCities(cities);
                }
            }

            @Override
            public void onFailed(String error) {
                Log.e(TAG, "onFailed: " + error);
            }
        });
    }

    public void loadHistory() {
        List<String> history = WeatherModel.getSearchHistory();
        if (history == null){
            Log.e(TAG, "loadHistory: 没有历史记录");
        }else {
            if (isViewAttached()){
                getView().showHistory(history);
            }
        }
    }

    public void deleteHistory(String history) {
        WeatherModel.deleteHistory(history);
    }

    public void addWeather(final String name, final AddWeatherCallback callback) {
        WeatherModel.getWeather(name, new WeatherModel.RequestCallback() {
            @Override
            public void onSuccess(Weather weather) {
                Log.d(TAG, "onSuccess: 添加城市成功:" + name);
                callback.done();
                if (isViewAttached()){
                    getView().showToast("添加成功");
                }
            }

            @Override
            public void onFailed(String reason) {
                Log.e(TAG, "onFailed: 添加城市失败：" + name + ",reson:" + reason);
                callback.done();
                if (isViewAttached()){
                    getView().showToast("添加失败");
                }
            }
        });
    }

    public interface AddWeatherCallback{
        void done();
    }
}
