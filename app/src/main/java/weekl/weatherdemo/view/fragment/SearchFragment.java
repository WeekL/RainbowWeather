package weekl.weatherdemo.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import weekl.weatherdemo.R;
import weekl.weatherdemo.presenter.SearchPresenter;
import weekl.weatherdemo.view.SearchActivity;

import static android.support.constraint.Constraints.TAG;

/**
 * 搜索城市页面
 */
public class SearchFragment extends Fragment {
    private Context mContext;
    private SearchPresenter mPresenter;
    private View mView;
    private GridView gridView;
    private ListView listView;

    private List<String> hots = new ArrayList<>();
    private List<String> histories = new ArrayList<>();

    private ArrayAdapter<String> gridAdapter;
    private ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mPresenter = ((SearchActivity)getActivity()).getSearchPresenter();
        mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_search, container, false);
        init();
        return mView;
    }

    private void init() {
        gridView = mView.findViewById(R.id.hot);
        listView = mView.findViewById(R.id.history);
        gridAdapter = new ArrayAdapter<>(mContext, R.layout.item_grid_view, R.id.text, hots);
        listAdapter = new ListAdapter();
        gridView.setAdapter(gridAdapter);
        listView.setAdapter(listAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SearchActivity) getActivity()).search(hots.get(position));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SearchActivity) getActivity()).search(histories.get(position));
            }
        });
        mPresenter.loadHistory();
    }

    public void setHots(List<String> cities) {
        hots.clear();
        if (cities == null){
            return;
        }
        hots.addAll(cities);
        if (gridView != null && gridView.isShown()) {
            gridAdapter.notifyDataSetChanged();
        }
    }

    public void setHistories(List<String> histories) {
        this.histories.clear();
        this.histories.addAll(histories);
        if (listView != null && listView.isShown()) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends ArrayAdapter<String> {

        public ListAdapter() {
            super(mContext, R.layout.item_search_list, histories);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String name = getItem(position);
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_search_list, parent, false);
            } else {
                view = convertView;
            }
            TextView textView = view.findViewById(R.id.text);
            ImageView imageView = view.findViewById(R.id.btn);
            textView.setText(name);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = histories.get(position);
                    mPresenter.deleteHistory(name);
                    histories.remove(position);
                    listAdapter.notifyDataSetChanged();
                    listView.invalidate();
                    Log.i(TAG, "删除历史记录："+histories.size()+","+listAdapter.getCount());
                }
            });
            return view;
        }
    }
}
