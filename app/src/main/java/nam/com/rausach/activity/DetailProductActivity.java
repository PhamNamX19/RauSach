package nam.com.rausach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nam.com.rausach.R;
import nam.com.rausach.adapter.ViewPagerAdapter;
import nam.com.rausach.controller.GetSanPhamGioHangApi;
import nam.com.rausach.controller.InsertSPGioHang;
import nam.com.rausach.controller.UpdateSPGioHang;
import nam.com.rausach.models.GioHangItem;
import nam.com.rausach.models.SanPham;
import nam.com.rausach.utils.MySharedPreferences;
import nam.com.rausach.utils.Server;

public class DetailProductActivity extends AppCompatActivity {

    private static final String TAG = "ChiTietSanPhamActivity";
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabs;
    private ImageView imageViewBack;
    private Button btnThemVaoGioHang;
    private ImageView imageViewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        viewPager = findViewById(R.id.viewPager);
        tabs = findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("Tổng quan");
        titles.add("Chi tiết");
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(viewPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        imageViewBack = findViewById(R.id.imageViewBack);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);
        imageViewCart = findViewById(R.id.imageViewCart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSanPham();
        actionBack();
        actionCart();
    }

    private void actionCart() {
        imageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idUser = MySharedPreferences.getSharedPreferences(DetailProductActivity.this);
                if (idUser != null) {
                    Intent intent = new Intent(DetailProductActivity.this, GioHangActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DetailProductActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getSanPham() {
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                SanPham sanPham = (SanPham) intent.getSerializableExtra("sanpham");
                GioHangItem gioHangItem = new GioHangItem();
                gioHangItem.setTenSanPham(sanPham.getTenSP());
                gioHangItem.setGiaSanPham(String.valueOf(sanPham.getGiaSP()));
                gioHangItem.setAnhSanPham(sanPham.getImageSP());
                gioHangItem.setMoTaSanPham(sanPham.getMoTaSP());
                gioHangItem.setLoaiSanPham(String.valueOf(sanPham.getIdTheoSanPham()));
                gioHangItem.setSoLuong(1);
                int idUser = Integer.parseInt(MySharedPreferences
                        .getSharedPreferences(DetailProductActivity.this));
                gioHangItem.setIdCustomer(idUser);
                gioHangItem.setIdSanPham(sanPham.getIdTheoSanPham());

                // get so luong product from cart
                List<GioHangItem> gioHangItems = getSanPhamGioHang(String.valueOf(idUser));
                int sizeGioHang = gioHangItems.size();

                boolean update = false;
                String respond;

                for (int i = 0; i < sizeGioHang; i++) {
                    if (gioHangItem.getIdSanPham() == gioHangItems.get(i).getIdSanPham()) {

                        // lay ve id cua sp de update.
                        int idUpdate = gioHangItems.get(i).getId();
                        update = true;
                        int soLuongTang = gioHangItems.get(i).getSoLuong() + 1;
                        UpdateSPGioHang updateSPGioHang = new UpdateSPGioHang();
                        try {
                            respond = updateSPGioHang.execute(Server.pathUpdateSPGioHang,
                                    String.valueOf(soLuongTang), String.valueOf(idUpdate)).get();
                            Toast.makeText(DetailProductActivity.this, "Sản phẩm đã được thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

                if (update == false) {
                    Log.d(TAG, "Insert: ");
                    InsertSPGioHang insertSPGioHang = new InsertSPGioHang();
                    Gson gson = new Gson();
                    String sanPhamGioHangJson = gson.toJson(gioHangItem);
                    Log.d(TAG, "onClick: " + sanPhamGioHangJson);
                    try {
                        respond = insertSPGioHang.execute(Server.pathInsertSPGioHang, sanPhamGioHangJson).get();
                        Log.d(TAG, "respond: " + respond);
                        Toast.makeText(DetailProductActivity.this, "Sản phẩm đã được thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private List<GioHangItem> getSanPhamGioHang(String idUser) {
        GetSanPhamGioHangApi getSanPhamGioHangApi = new GetSanPhamGioHangApi();
        List<GioHangItem> gioHangItems = new ArrayList<>();
        try {
            String apiGioHang = getSanPhamGioHangApi.execute(Server.pathGioHang, idUser).get();
            JSONArray jsonArray = new JSONArray(apiGioHang);
            for (int i = 0; i < jsonArray.length(); i++) {
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
                Log.d(TAG, "getGioHangItems: " + gioHangItem.toString());
                gioHangItems.add(gioHangItem);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gioHangItems;
    }

    private void actionBack() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
