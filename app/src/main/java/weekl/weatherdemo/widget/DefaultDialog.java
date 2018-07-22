package weekl.weatherdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import weekl.weatherdemo.R;

public class DefaultDialog extends Dialog {

    private View mView;

    private Button positiveButton;
    private Button negativeButton;
    private TextView titleTextView;
    private TextView messageTextView;

    private String positiveText = "确定";
    private String negativeText = "取消";
    private String titleText;
    private String messageText;

    private BtnOnClickListener posOnClickListener;
    private BtnOnClickListener negOnClickListener;

    public DefaultDialog(@NonNull Context context) {
        super(context, R.style.DefaultDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_default,(ViewGroup)findViewById(R.id.dialog));
        setContentView(mView);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        positiveButton = mView.findViewById(R.id.positive);
        negativeButton = mView.findViewById(R.id.negative);
        titleTextView = mView.findViewById(R.id.title);
        messageTextView = mView.findViewById(R.id.message);
    }

    private void initData() {
        if (titleText != null) {
            titleTextView.setText(titleText);
        }else {
            titleTextView.setVisibility(View.GONE);
        }
        if (messageText != null) {
            messageTextView.setText(messageText);
        }else {
            titleTextView.setVisibility(View.GONE);
        }
        if (positiveText != null) {
            positiveButton.setText(positiveText);
        }else {
            titleTextView.setVisibility(View.GONE);
        }
        if (negativeText != null) {
            negativeButton.setText(negativeText);
        }else {
            titleTextView.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posOnClickListener != null) {
                    posOnClickListener.onClick();
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negOnClickListener != null) {
                    negOnClickListener.onClick();
                }
            }
        });
    }

    public void setTitle(String title) {
        titleText = title;
    }

    public void setMessage(String message) {
        messageText = message;
    }

    public void setPositiveOnClickListener(String name, BtnOnClickListener listener) {
        positiveText = name;
        posOnClickListener = listener;
    }

    public void setNegativeOnClickListener(String name, BtnOnClickListener listener) {
        negativeText = name;
        negOnClickListener = listener;
    }

    public interface BtnOnClickListener {
        void onClick();
    }
}
