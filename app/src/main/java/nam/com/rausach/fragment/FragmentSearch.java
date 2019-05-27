package nam.com.rausach.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import nam.com.rausach.R;
import nam.com.rausach.adapter.SanPhamAdapter;
import nam.com.rausach.models.PostSearch;
import nam.com.rausach.models.SanPham;
import nam.com.rausach.utils.Constant;
import nam.com.rausach.utils.Server;

public class FragmentSearch extends Fragment {
    private RecyclerView rvSearch;
    private ArrayList<SanPham> arrSearch;
    private SanPhamAdapter sanPhamAdapter;
    private SanPham mSanPham;
    String keySearch="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search, container, false);
        rvSearch = view.findViewById(R.id.rvSearch);
        keySearch();
        getDataKeySearch();
        sanPhamAdapter = new SanPhamAdapter(getContext(), arrSearch);
        return view;
    }

    private void getDataKeySearch() {
        PostSearch postSearch = new PostSearch();
        try {
            String key = postSearch.execute(Server.pathSearch,keySearch).get();
            if (key != null) {
                arrSearch = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mSanPham = new SanPham(jsonObject.getInt("idSanPham"),
                            jsonObject.getString("tensanpham"),
                            jsonObject.getInt("giasanpham"),
                            jsonObject.getString("hinhanhsanpham"),
                            jsonObject.getString("motasanpham"),
                            jsonObject.getInt("idloaisanpham"));
                    arrSearch.add(mSanPham);
                }
                sanPhamAdapter = new SanPhamAdapter(getContext(), arrSearch);
                rvSearch.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rvSearch.setAdapter(sanPhamAdapter);
                sanPhamAdapter.notifyDataSetChanged();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void keySearch() {
        keySearch = this.getArguments().getString(Constant.KEY_SEARCH);
        Log.d("FragmentSearch", "keySearch: " + keySearch);
    }
}
