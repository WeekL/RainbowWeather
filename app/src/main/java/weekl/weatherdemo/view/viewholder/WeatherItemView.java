package weekl.weatherdemo.view.viewholder;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import weekl.weatherdemo.R;
import weekl.weatherdemo.widget.IndicatorView;

/**
 * 主界面RecyclerView的ViewHolder类
 */
public class WeatherItemView extends RecyclerView.ViewHolder {
    private static final String TAG = "WeatherItemView";
    private RecyclerView.LayoutParams params;
    private RelativeLayout left;
    private View heightView;
    public ImageView weatherIcon;
    public ViewPager viewPager;
    public IndicatorView indicator;

    private int expandHeight;
    private int shrinkHeight;
    private int itemHeight;

    public WeatherItemView(final View itemView) {
        super(itemView);
        left = itemView.findViewById(R.id.left);
        weatherIcon = itemView.findViewById(R.id.weather_icon);
        viewPager = itemView.findViewById(R.id.view_pager);
        indicator = itemView.findViewById(R.id.indicator);
        heightView = itemView.findViewById(R.id.height_view);

        params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        calculateHeight();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemHeight == shrinkHeight) {
                    params.height = expandHeight;
                }else {
                    params.height = shrinkHeight;
                    viewPager.setCurrentItem(0);
                }
                itemView.setLayoutParams(params);
            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return itemHeight == shrinkHeight;
            }
        });
    }

    //设置item背景色
    public void setBackground(int[] color) {
        left.setBackgroundColor(itemView.getResources().getColor(color[1]));
        viewPager.setBackgroundColor(itemView.getResources().getColor(color[0]));
    }

    //获取子View真实高度
    private void calculateHeight() {
        heightView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    heightView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                shrinkHeight = heightView.getHeight();
                Log.d(TAG, "onGlobalLayout: shrinkHeight = " + shrinkHeight);
            }
        });
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                expandHeight = viewPager.getHeight();
                Log.d(TAG, "onGlobalLayout: expandHeight = " + expandHeight);
            }
        });
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }*/
                itemHeight = itemView.getHeight();
                Log.d(TAG, "onGlobalLayout: itemHeight = " + itemHeight);
            }
        });
    }


}
