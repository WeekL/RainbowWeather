package weekl.weatherdemo.view.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import weekl.weatherdemo.R;
import weekl.weatherdemo.base.adapter.ItemPagerAdapter;
import weekl.weatherdemo.bean.Weather;
import weekl.weatherdemo.util.ResourceUtil;
import weekl.weatherdemo.view.MainActivity;
import weekl.weatherdemo.view.viewholder.DailyView;
import weekl.weatherdemo.view.viewholder.HourlyView;
import weekl.weatherdemo.view.viewholder.Overview;
import weekl.weatherdemo.view.viewholder.WeatherItemView;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherItemView> {
    private static final String TAG = "WeatherListAdapter";
    private MainActivity mContext;

    private List<Weather> mDatas;
    private List<int[]> mColors;
    private int colorIndex = 0;

    private OnLongClickListener clickListener;

    public WeatherListAdapter(MainActivity context, List<Weather> data) {
        mContext = context;
        mDatas = data;
    }

    @NonNull
    @Override
    public WeatherItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_weather, parent, false);
        return new WeatherItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WeatherItemView holder, final int position) {
        if (null != clickListener) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.onLongClick(position);
                    Log.d("Test long click", "onLongClick: " + position);
                    return true;
                }
            });
        }
        //设置item背景色
        holder.setBackground(mColors.get(colorIndex++ % mColors.size()));

        Weather weather = mDatas.get(position);
        holder.weatherIcon.setImageResource(ResourceUtil.getResIdFromString(mContext, "code" + weather.now.code));

        //设置ViewPager适配器
        holder.viewPager.setAdapter(new ItemPagerAdapter(initPagers(weather)));
        holder.indicator.setViewPager(holder.viewPager);
    }

    /**
     * 初始化每个item中ViewPager的三个页面
     *
     * @param weather
     * @return
     */
    private List<View> initPagers(Weather weather) {
        List<View> views = new ArrayList<>(3);
        views.add(new Overview(mContext, weather));
        views.add(new HourlyView(mContext,weather));
        views.add(new DailyView(mContext, weather));
        return views;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 设置item可用颜色
     *
     * @param list
     */
    public void setItemColors(List<int[]> list) {
        mColors = list;
    }

    public void addLongClickListener(OnLongClickListener listener) {
        clickListener = listener;
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }
}
