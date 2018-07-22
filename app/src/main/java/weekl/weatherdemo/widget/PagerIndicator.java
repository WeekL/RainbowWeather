package weekl.weatherdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import weekl.weatherdemo.R;

public class PagerIndicator extends LinearLayout {

    private Context mContext;
    private int mCount;
    private int mPad = 30;
    private int mSeq = 0;
    private float mRatio = 0.0f;
    private Paint mPaint;
    private Bitmap mBackIcon;
    private Bitmap mForeIcon;

    public PagerIndicator(Context context) {
        this(context,null);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBackIcon = BitmapFactory.decodeResource(getResources(), R.drawable.item_indicator);
        mForeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.item_indicator_selected);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int left = (getMeasuredWidth() - mCount * mPad) / 2;
        for (int i = 0; i < mCount; i++) {
            canvas.drawBitmap(mBackIcon, left + i * mPad, 0, mPaint);
        }
        canvas.drawBitmap(mForeIcon, left + (mSeq + mRatio) * mPad, 0, mPaint);
    }

    private void setCount(int count, int pad){
        mCount = count;
        mPad = pad;
        invalidate();
    }

    private void setCurrent(int seq, float ratio){
        mSeq = seq;
        mRatio = ratio;
        invalidate();
    }
}
