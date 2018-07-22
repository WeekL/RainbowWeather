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
import weekl.weatherdemo.bean.weather.Hourly;
import weekl.weatherdemo.util.ResourceUtil;

public class HourlyView extends ListView {

    public HourlyView(Context context, Weather weather) {
        super(context);
        ListView.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);

        ArrayList<ListItem> items = new ArrayList<>(8);
        List<Hourly> hourlyList = weather.hourly;
        for (Hourly hourly : hourlyList) {
            String time = hourly.time.split(" ")[1];
            String tmp = hourly.tmp + "°";
            int code = ResourceUtil.getResIdFromString(context,"code"+hourly.code);
            items.add(new ListItem(time,code,tmp));
        }

        CommonAdapter<ListItem> adapter = new CommonAdapter<ListItem>(context,items, R.layout.item_hourly_list) {
            @Override
            public void convert(CommonViewHolder holder, ListItem bean) {
                holder.setText(R.id.time,bean.time);
                holder.setImageResource(R.id.icon,bean.icon);
                holder.setText(R.id.tmp,bean.tmp);
            }
        };

        setAdapter(adapter);
    }

    class ListItem{
        String time;
        int icon;
        String tmp;

        public ListItem(String time, int icon, String tmp) {
            this.time = time.split(" ")[0];
            this.icon = icon;
            this.tmp = tmp;
        }
    }
}
