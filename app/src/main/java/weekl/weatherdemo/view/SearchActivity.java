package weekl.weatherdemo.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import weekl.weatherdemo.R;
import weekl.weatherdemo.base.BaseActivity;
import weekl.weatherdemo.bean.City;
import weekl.weatherdemo.presenter.SearchPresenter;
import weekl.weatherdemo.view.fragment.SearchFragment;
import weekl.weatherdemo.view.fragment.SearchResultFragment;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView, View.OnClickListener {

    private FragmentManager fragmentManager;

    private EditText inputText;
    private TextView searchView;
    private SearchFragment searchFragment;
    private SearchResultFragment resultFragment;

    private View.OnClickListener searchClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        getPresenter().loadHistory();
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        inputText = findViewById(R.id.input);
        searchView = findViewById(R.id.search);
        searchView.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.contains("\r") || str.contains("\n")) {
                    String newStr = str.replace("/r", "").replace("\n", "");
                    inputText.setText(newStr);
                    search(newStr);
                }
            }
        });

        searchFragment = new SearchFragment();
        resultFragment = new SearchResultFragment();

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, searchFragment);
        transaction.commit();

        getPresenter().getHotCities();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search:
                search(inputText.getText().toString());
                break;
        }
    }

    @Override
    public void showResult(final List<City> cities) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultFragment.setResult(cities);
            }
        });
    }

    @Override
    public void showHotCities(final List<String> cities) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchFragment.setHots(cities);
            }
        });
    }

    @Override
    public void showHistory(final List<String> history) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchFragment.setHistories(history);
            }
        });
    }

    public void search(final String name) {
        getPresenter().search(name);
        if (searchClickListener != null){
            searchClickListener.onClick(searchView);
        }
                Fragment fragment = fragmentManager.findFragmentById(R.id.content);
                if (fragment instanceof SearchFragment) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, resultFragment);
                    transaction.commit();
                }
                if (!inputText.getText().toString().equals(name)) {
                    inputText.setText(name);
                    inputText.setSelection(name.length());
                }
    }

    public SearchPresenter getSearchPresenter(){
        return getPresenter();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.content);
        if (fragment instanceof SearchFragment) {
            finish();
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, searchFragment);
            transaction.commit();
        }
    }

    public void addSearchClickListener(View.OnClickListener listener){
        searchClickListener = listener;
    }
}
