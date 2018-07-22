package weekl.weatherdemo.view.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import weekl.weatherdemo.R;
import weekl.weatherdemo.base.adapter.CommonAdapter;
import weekl.weatherdemo.base.adapter.CommonViewHolder;
import weekl.weatherdemo.bean.Weather;
import weekl.weatherdemo.bean.weather.Forecast;
import weekl.weatherdemo.util.ResourceUtil;

public class DailyView extends ListView {

    public DailyView(Context context, Weather weather) {
        super(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);

        ArrayList<ListItem> items = new ArrayList<>(7);
        List<Forecast> forecasts = weather.forecasts;
        for (Forecast forecast : forecasts) {
            String date = forecast.date.substring(5,10);
            int icon = ResourceUtil.getResIdFromString(context,"code"+forecast.dayCode);
            String tmpRange = forecast.tmpMin + "°/" + forecast.tmpMax + "°";
            items.add(new ListItem(date,icon,tmpRange));
        }
        CommonAdapter<ListItem> adapter = new CommonAdapter<ListItem>(context, items, R.layout.item_daily_list) {
            @Override
            public void convert(CommonViewHolder holder, ListItem bean) {
                holder.setText(R.id.date, bean.weekTime);
                holder.setImageResource(R.id.icon, bean.icon);
                holder.setText(R.id.tmp_range, bean.tmpRange);
            }
        };
        setAdapter(adapter);
    }

    class ListItem {
        String weekTime;
        int icon;
        String tmpRange;

        public ListItem(String weekTime, int icon, String tmpRange) {
            this.weekTime = weekTime;
            this.icon = icon;
            this.tmpRange = tmpRange;
        }
    }
}
