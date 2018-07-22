package weekl.weatherdemo.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import weekl.weatherdemo.R;
import weekl.weatherdemo.base.adapter.CommonAdapter;
import weekl.weatherdemo.base.adapter.CommonViewHolder;
import weekl.weatherdemo.bean.City;
import weekl.weatherdemo.presenter.SearchPresenter;
import weekl.weatherdemo.view.SearchActivity;

public class SearchResultFragment extends Fragment {
    private static final String TAG = "SearchResultFragment";

    private Context mContext;
    private ListView listView;
    private ProgressBar progressBar;

    private List<City> cities = new ArrayList<>();
    private ListAdapter adapter;

    private SearchPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mPresenter = ((SearchActivity)getActivity()).getSearchPresenter();
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_search_result, container, false);
        listView = view.findViewById(R.id.result_list);
        adapter = new ListAdapter();
        listView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progress);
        showProgress();
        ((SearchActivity)getActivity()).addSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
            }
        });
        return view;
    }

    private void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

    }

    public void setResult(List<City> cities) {
        if (progressBar != null){
            progressBar.setVisibility(View.GONE);
        }
        if (cities == null){
            Toast.makeText(mContext, "查找失败", Toast.LENGTH_SHORT).show();
            return;
        }
        this.cities.clear();
        this.cities.addAll(cities);
        if (listView != null) {
            adapter.notifyDataSetChanged();
            listView.setVisibility(View.VISIBLE);
        }
    }

    private class ListAdapter extends CommonAdapter<City> {

        public ListAdapter() {
            super(mContext, cities, R.layout.item_search_result);
        }

        @Override
        public void convert(final CommonViewHolder holder, City bean) {
            holder.setText(R.id.location, bean.location);
            holder.setText(R.id.parent, bean.adminArea + "," + bean.parentCity);
            ImageView btnView = holder.getView(R.id.btn);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(new ProgressBar(mContext));
            final AlertDialog dialog = builder.create();
            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    mPresenter.addWeather(cities.get(holder.getPosition()).location,
                            new SearchPresenter.AddWeatherCallback() {
                        @Override
                        public void done() {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}
