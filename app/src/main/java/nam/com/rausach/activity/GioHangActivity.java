package nam.com.rausach.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nam.com.rausach.callback.UpdateGioHang;
import nam.com.rausach.R;
import nam.com.rausach.adapter.GioHangAdapter;
import nam.com.rausach.controller.GetSanPhamGioHangApi;
import nam.com.rausach.models.GioHangItem;
import nam.com.rausach.utils.MySharedPreferences;
import nam.com.rausach.utils.Server;

public class GioHangActivity extends AppCompatActivity implements UpdateGioHang {

    private static final String TAG = "GioHangActivity";
    private RecyclerView mRecyclerView;
    private GioHangAdapter mGioHangAdapter;
    private TextView textViewTongTien;
    private StringBuilder thanhTien;
    private ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        mRecyclerView = findViewById(R.id.recyclerView);
        textViewTongTien = findViewById(R.id.textViewTongTien);
        imageViewBack = findViewById(R.id.imageViewBack);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setGioHang();
        setImageBack();
    }

    private void setImageBack() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setGioHang(){
        GetSanPhamGioHangApi getSanPhamGioHangApi = new GetSanPhamGioHangApi();
        String idUser = MySharedPreferences.getSharedPreferences(GioHangActivity.this);
        int tongTien = 0;

        try {
            String apiGioHang = getSanPhamGioHangApi.execute(Server.pathGioHang, idUser).get();
            Log.d(TAG, "setGioHang: "+ apiGioHang);
            List<GioHangItem> gioHangItems = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(apiGioHang);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                GioHangItem gioHangItem = new GioHangItem();
                gioHangItem.setId(jsonObject.getInt("idGioHang"));
                gioHangItem.setTenSanPham(jsonObject.getString("tenSanPham"));
                gioHangItem.setGiaSanPham(jsonObject.getString("giaSanPham"));
                gioHangItem.setAnhSanPham(jsonObject.getString("anhSanPham"));
                gioHangItem.setMoTaSanPham(jsonObject.getString("moTaSanPham"));
                gioHangItem.setLoaiSanPham(jsonObject.getString("loaiSanPham"));
                gioHangItem.setSoLuong(jsonObject.getInt("soLuong"));
                gioHangItem.setIdCustomer(jsonObject.getInt("idCustomer"));
                gioHangItem.setIdSanPham(jsonObject.getInt("idSanPham"));
                Log.d(TAG, "getGioHangItems: "+ gioHangItem.toString());
                gioHangItems.add(gioHangItem);
                // get tong tien
                tongTien += Integer.parseInt(gioHangItem.getGiaSanPham())* gioHangItem.getSoLuong();
            }
            mGioHangAdapter = new GioHangAdapter(this, gioHangItems);
            mGioHangAdapter.setiUpdateGioHang(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(GioHangActivity.this));
            mRecyclerView.setAdapter(mGioHangAdapter);
            mGioHangAdapter.notifyDataSetChanged();

            // set tong tien
            setTongTien(tongTien);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTongTien(int tongTien) {
        thanhTien = new StringBuilder();
        thanhTien.append(tongTien);
        thanhTien.append(getResources().getString(R.string.dongia));
        textViewTongTien.setText(thanhTien);
    }

    @Override
    public void updateSanPham(List<GioHangItem> gioHangItems) {
        mGioHangAdapter = new GioHangAdapter(this, gioHangItems);
        mGioHangAdapter.setiUpdateGioHang(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GioHangActivity.this));
        mRecyclerView.setAdapter(mGioHangAdapter);
        mGioHangAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateTongTien(int tongTien) {
        setTongTien(tongTien);
    }
}
