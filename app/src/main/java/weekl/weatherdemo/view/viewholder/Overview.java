package weekl.weatherdemo.view.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import weekl.weatherdemo.R;
import weekl.weatherdemo.base.adapter.CommonAdapter;
import weekl.weatherdemo.base.adapter.CommonViewHolder;
import weekl.weatherdemo.bean.Weather;
import weekl.weatherdemo.bean.weather.Forecast;
import weekl.weatherdemo.bean.weather.LifeStyle;
import weekl.weatherdemo.util.ResourceUtil;

/**
 * 主界面RecyclerView中每个item的ViewPager的第一页
 */
public class Overview extends LinearLayout {
    private Context mContext;
    private Weather mWeather;

    private TextView tmp;
    private TextView time;
    private TextView city;
    private TextView description;
    private TextView tmpRange;
    private ListView listView;

    private ArrayList<ListItem> items = new ArrayList<>();

    public Overview(Context context) {
        this(context, null);
    }

    public Overview(Context context, Weather weather) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_overview, this);

        tmp = findViewById(R.id.tmp);
        time = findViewById(R.id.time);
        city = findViewById(R.id.location);
        description = findViewById(R.id.description);
        tmpRange = findViewById(R.id.tmp_range);
        listView = findViewById(R.id.list);
        if (weather != null) {
            setData(weather);
        }
    }

    public void setData(Weather weather) {
        mWeather = weather;
        //获取紫外线数据
        String uv = "unknow";
        for (int i = 0; i < mWeather.lifeStyles.size(); i++) {
            LifeStyle lifeStyle = mWeather.lifeStyles.get(i);
            if (lifeStyle.type.equals("uv")) {
                uv = lifeStyle.brf;
                break;
            }
        }
        items = new ArrayList<>(3);
        //体感温度 + 紫外线
        items.add(new ListItem(getResId("feel"), String.valueOf(mWeather.now.feel),
                getResId("umbrella"), uv));
        //降水量 + 相对湿度
        items.add(new ListItem(getResId("rain"), String.valueOf(mWeather.now.pcpn),
                getResId("water"), mWeather.now.hum + "%"));
        //风向 + 风速
        items.add(new ListItem(getResId("wind"), String.valueOf(mWeather.now.windDir),
                getResId("wind_speed"), String.valueOf(mWeather.now.windSpd)));

        String temperature = mWeather.now.tmp + "°";
        tmp.setText(temperature);
        String updateTime = mWeather.update.updateTime.split(" ")[1];
        time.setText(updateTime);
        city.setText(mWeather.basic.location);
        description.setText(mWeather.now.text);

        Forecast today = mWeather.forecasts.get(0);
        String tmpR = today.tmpMin + "°/" + today.tmpMax + "°";
        tmpRange.setText(tmpR);

        listView.setAdapter(new CommonAdapter<ListItem>(this.getContext(), items, R.layout.item_overview_list) {
            @Override
            public void convert(CommonViewHolder holder, ListItem bean) {
                holder.setImageResource(R.id.left_icon, bean.leftIcon);
                holder.setImageResource(R.id.right_icon, bean.rightIcon);
                holder.setText(R.id.left_text, bean.leftTxt);
                holder.setText(R.id.right_text, bean.rightTxt);
            }
        });
        invalidate();
    }

    private int getResId(String name) {
        return ResourceUtil.getResIdFromString(mContext, name);
    }

    class ListItem {
        int leftIcon, rightIcon;
        String leftTxt, rightTxt;

        public ListItem(int leftIcon, String leftTxt, int rightIcon, String rightTxt) {
            this.leftIcon = leftIcon;
            this.leftTxt = leftTxt;
            this.rightIcon = rightIcon;
            this.rightTxt = rightTxt;
        }
    }
}
