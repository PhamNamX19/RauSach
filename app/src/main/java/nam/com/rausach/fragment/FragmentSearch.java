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
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TAG = "FragmentSearch";
    private RecyclerView rvSearch;
    private TextView tvnotFound;
    private ArrayList<SanPham> arrSearch;
    private SanPhamAdapter sanPhamAdapter;
    private SanPham mSanPham;
    String keySearch = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search, container, false);
        rvSearch = view.findViewById(R.id.rvSearch);
        tvnotFound = view.findViewById(R.id.tvNotFound);
        keySearch();

        getDataKeySearch();
        sanPhamAdapter = new SanPhamAdapter(getContext(), arrSearch);
        return view;
    }

    private void getDataKeySearch() {
        PostSearch postSearch = new PostSearch();
        try {
            String key = postSearch.execute(Server.pathSearch, keySearch).get();
            Log.d(TAG, "getDataKeySearch: " + key);
            if (key != null) {
                arrSearch = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(key);
                Log.d(TAG, "getDataKeySearch: " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d(TAG, "getDataKeySearch: data " + jsonObject);
                    if (jsonObject != null) {
                        mSanPham = new SanPham(jsonObject.getInt("idSanPham"),
                                jsonObject.getString("tensanpham"),
                                jsonObject.getInt("giasanpham"),
                                jsonObject.getString("hinhanhsanpham"),
                                jsonObject.getString("motasanpham"),
                                jsonObject.getInt("idloaisanpham"));
                        arrSearch.add(mSanPham);
                    }
                    if (jsonArray.length() == 0) {
                        Toast.makeText(getActivity(), "Not found", Toast.LENGTH_SHORT).show();
                        rvSearch.setVisibility(View.GONE);
                        tvnotFound.setVisibility(View.VISIBLE);
                    }
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
            Toast.makeText(getActivity(), "Not found", Toast.LENGTH_SHORT).show();
            rvSearch.setVisibility(View.GONE);
            tvnotFound.setVisibility(View.VISIBLE);
        }
    }

    private void keySearch() {
        keySearch = this.getArguments().getString(Constant.KEY_SEARCH);
        Log.d("FragmentSearch", "keySearch: " + keySearch);
    }
}
