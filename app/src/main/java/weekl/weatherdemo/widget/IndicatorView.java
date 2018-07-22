package weekl.weatherdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import weekl.weatherdemo.R;

public class IndicatorView extends View implements ViewPager.OnPageChangeListener {
    //指示器图标，一个图标包含两种状态（选中/未选中）
    private Drawable mIcon;

    //图标大小，从图标宽高中取大值
    private int mIconSize;

    //控件总宽度
    private int mWidth;

    //控件的真实宽度 = 内边距 + item总宽度 + item间的空隙
    private int mContextWidth;

    //指示器数量
    private int mCount;

    //指示器之间的间隔
    private int mMargin;

    //当前选中状态的指示器
    private int mCurItem;

    //滑动偏移量
    private float mOffset;

    //是否实时刷新
    private boolean mSmooth;

    //其他需要监听ViewPager滑动的控件使用的监听器
    private ViewPager.OnPageChangeListener mListener;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //通过 TypedArray 获取自定义属性
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.IndicatorView);
        //获取自定义属性的个数
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.IndicatorView_indicator_icon:
                    //通过自定义属性拿到指示器
                    mIcon = typedArray.getDrawable(attr);
                    break;
                case R.styleable.IndicatorView_indicator_interval:
                    float defaultMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            5, getResources().getDisplayMetrics());
                    mMargin = (int) typedArray.getDimension(attr, defaultMargin);
                    break;
                case R.styleable.IndicatorView_indicator_smooth:
                    mSmooth = typedArray.getBoolean(attr, false);
                    break;
            }
        }
        typedArray.recycle();
        initIndicator();
    }

    private void initIndicator() {
        //得到图片适应屏幕大小后的尺寸
        mIconSize = Math.max(mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());
        //设置item的边界（Canvas从左上角开始将item绘制在这个矩形区域）
        mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicWidth());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width;
        //期望宽度 = 内边距 + item总宽度 + item间的空隙
        int desired = getPaddingLeft() + getPaddingRight() + mIconSize * mCount + mMargin * (mCount - 1);
        mContextWidth = desired;
        if (mode == MeasureSpec.EXACTLY) {
            //指定宽度不够时，弃用指定的宽度，使用期望宽度
            width = Math.max(desired, size);
        } else if (mode == MeasureSpec.AT_MOST) {
            //受可用宽度限制
            width = Math.min(desired, size);
        } else {
            width = desired;
        }
        mWidth = width;
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (mode == MeasureSpec.EXACTLY) {
            //有指定高度直接使用指定高度，不考虑是否显示不全
            height = size;
        } else {
            //没有指定高度，期望高度 = 内边距 + item高度
            int desired = getPaddingTop() + getPaddingBottom() + mIconSize;
            if (mode == MeasureSpec.AT_MOST) {
                height = Math.min(desired, size);
            } else {
                height = desired;
            }
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //保存起点
        canvas.save();
        //计算需要绘制的位置（第一个item）
        int left = mWidth / 2 - mContextWidth / 2 + getPaddingLeft();
        canvas.translate(left, getPaddingTop());
        for (int i = 0; i < mCount; i++) {
            //传入的drawable 是一个selector ，需要设置状态来获取相应的图片
            mIcon.setState(EMPTY_STATE_SET);
            mIcon.draw(canvas);
            canvas.translate(mIconSize + mMargin, 0);
        }
        //回到起点
        canvas.restore();
        //再次计算绘制位置（当前滑动的位置）
        float leftDraw = (mIconSize + mMargin) * (mCurItem + mOffset);
        canvas.translate(left + leftDraw, getPaddingTop());
        //获取到选中状态的drawable
        mIcon.setState(SELECTED_STATE_SET);
        mIcon.draw(canvas);
    }

    /**
     * 此ViewPager 一定是先设置了Adapter，并且Adapter 需要所有数据，后续不能修改数据
     *
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null) {
            throw new RuntimeException("请看使用说明");
        }
        mCount = pagerAdapter.getCount();
        viewPager.addOnPageChangeListener(this);
        mCurItem = viewPager.getCurrentItem();
        invalidate();
    }

    /**
     * 用于其它需要监听ViewPager滑动的控件
     *
     * @param mListener
     */
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 实现和ViewPager滑动相对应的同步移动
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.v("zgy", "========" + position + ",===offset" + positionOffset);
        if (mSmooth) {
            mCurItem = position;
            mOffset = positionOffset;
            invalidate();
        }
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurItem = position;
        invalidate();
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }
}
