package weekl.weatherdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import weekl.weatherdemo.Constant;
import weekl.weatherdemo.R;
import weekl.weatherdemo.base.BaseActivity;
import weekl.weatherdemo.bean.Weather;
import weekl.weatherdemo.presenter.WeatherPresenter;
import weekl.weatherdemo.view.adapter.WeatherListAdapter;
import weekl.weatherdemo.widget.DefaultDialog;

public class MainActivity extends BaseActivity<WeatherPresenter> implements IWeatherView {
    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView mWeatherContent;
    private FloatingActionButton floatBtn;
    //RecyclerView的适配器
    private WeatherListAdapter mAdapter;

    private List<Weather> weathers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getPresenter().loadCache();//加载缓存
        getPresenter().getWeather();//请求最新天气的数据
        swipeRefresh.setRefreshing(true);
        //test();
    }

    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter();
    }

    private void test() {
        getPresenter().getWeather("北京");
        getPresenter().getWeather("杭州");
        getPresenter().getWeather("广州");
        getPresenter().getWeather("海口");
    }

    private void initView() {
        swipeRefresh =findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().getWeather();
            }
        });
        floatBtn = findViewById(R.id.add_btn);
        floatBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
        mWeatherContent = findViewById(R.id.content);
        mWeatherContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WeatherListAdapter(this, weathers);
        mAdapter.setItemColors(Constant.getColors());
        mAdapter.addLongClickListener(new WeatherListAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                removeWeather(position);
            }
        });
        mWeatherContent.setAdapter(mAdapter);
    }

    @Override
    public void addWeather(Weather weather) {
        if (weathers.size() == 0) {
            addWeather(weather,0);
        }else{
            for (int i = 0; i < weathers.size(); i++) {
                Weather w = weathers.get(i);
                if (w.basic.location.equals(weather.basic.location)){
                    //已存在，更新
                    addWeather(weather,i);
                    return;
                }
            }
            //添加到最后
            addWeather(weather, weathers.size() - 1);
        }
    }

    public void addWeather(Weather weather, int position) {
        Log.d(TAG, "addWeather:" + weather);
        boolean isChanged = true;
        if (weathers.size() == 0 || weathers.size() == position) {
            weathers.add(weather);
        }else {
            Weather w = weathers.get(position);
            if (weather.basic.location.equals(w.basic.location)) {
                if (weather.update.updateTime.equals(w.update.updateTime)){
                    isChanged = false;
                }else {
                    weathers.set(position, weather);
                }
            } else {
                weathers.add(position, weather);
            }
        }
        if (isChanged){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void removeWeather(final int position) {
        final Weather weather = weathers.get(position);
        final String location = weather.basic.location;
        final DefaultDialog dialog = new DefaultDialog(this);
        dialog.setTitle("删除城市");
        dialog.setMessage("是否移除" + location + "?");
        dialog.setPositiveOnClickListener("确定", new DefaultDialog.BtnOnClickListener() {
            @Override
            public void onClick() {
                weathers.remove(position);
                mAdapter.notifyDataSetChanged();
                getPresenter().delete(location);
                dialog.dismiss();
                Snackbar.make(mWeatherContent, location + "已删除", Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addWeather(weather,position);
                                getPresenter().cancelDelete(weather);
                            }
                        }).show();
            }
        });
        dialog.setNegativeOnClickListener("取消", new DefaultDialog.BtnOnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void stopRefresh() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
