package nam.com.rausach.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nam.com.rausach.R;
import nam.com.rausach.adapter.SanPhamAdapter;
import nam.com.rausach.models.SanPham;
import nam.com.rausach.utils.Constant;
import nam.com.rausach.utils.Server;

public class RauCuFragment extends Fragment {

    Toolbar toolbarRauCu;
    ImageView imgBanner;
    ImageButton imgBack;
    TextView tvTitleSanPham;
    RecyclerView rvRauCu;
    SanPhamAdapter rauCuAdapter;
    ArrayList<SanPham> arrRauCu;
    int idSanPhamTheoID = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_loai_san_pham, container, false);
        toolbarRauCu = view.findViewById(R.id.toolbarSanPham);
        imgBanner = view.findViewById(R.id.imgBanner);
        Picasso.with(getContext())
                .load("https://cdn.giigaa.com/category/listing_banner/rau-cu.jpg-29ee1.jpg")
                .into(imgBanner);
        imgBanner.setScaleType(ImageView.ScaleType.FIT_XY);

        tvTitleSanPham = view.findViewById(R.id.tvTitleSanPham);
        tvTitleSanPham.setText("Rau củ");

        imgBack = view.findViewById(R.id.btnBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(RauCuFragment.this);
                transaction.commit();
            }
        });

        rvRauCu = view.findViewById(R.id.rvSPFragment);
        arrRauCu = new ArrayList<>();
        rauCuAdapter = new SanPhamAdapter(getContext(), arrRauCu);
        rvRauCu.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvRauCu.setLayoutManager(gridLayoutManager);

        getSanPhamTheoIDLoaiSanPham();
        getData();

        rvRauCu.setAdapter(rauCuAdapter);
        return view;
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.pathSanPhamTheoID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                int giaRauCu = 0;
                String tenRauCu = "";
                String anhRauCu = "";
                String moTa = "";
                int idTheoLoaiSanPham = 0;
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("idSanPham");
                            tenRauCu = jsonObject.getString("tensanpham");
                            giaRauCu = jsonObject.getInt("giasanpham");
                            anhRauCu = jsonObject.getString("hinhanhsanpham");
                            moTa = jsonObject.getString("motasanpham");
                            idTheoLoaiSanPham = jsonObject.getInt("idloaisanpham");
                            arrRauCu.add(new SanPham(id, tenRauCu, giaRauCu, anhRauCu, moTa, idTheoLoaiSanPham));
                            rauCuAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("idloaisanpham", String.valueOf(idSanPhamTheoID));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void getSanPhamTheoIDLoaiSanPham() {
        String myValue = this.getArguments().getString(Constant.SANPHAMTHEOID);
        Log.d("idSanPhamTheoID", myValue);
        idSanPhamTheoID = Integer.parseInt(myValue);

    }
}
