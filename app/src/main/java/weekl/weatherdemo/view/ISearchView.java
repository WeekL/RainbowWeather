package weekl.weatherdemo.view;

import java.util.List;

import weekl.weatherdemo.base.IView;
import weekl.weatherdemo.bean.City;

public interface ISearchView extends IView {
    void showResult(List<City> cities);

    void showHotCities(List<String> cities);

    void showHistory(List<String> history);
}
