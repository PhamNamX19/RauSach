package nam.com.rausach.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nam.com.rausach.R;
import nam.com.rausach.adapter.DanhMucAdapter;
import nam.com.rausach.adapter.LoaiSPAdapter;
import nam.com.rausach.adapter.SanPhamAdapter;
import nam.com.rausach.adapter.SanPhamMoiAdapter;
import nam.com.rausach.fragment.FragmentSearch;
import nam.com.rausach.fragment.GiaViFragment;
import nam.com.rausach.fragment.RauCuFragment;
import nam.com.rausach.fragment.ThitSachFragment;
import nam.com.rausach.fragment.ThuyHaiSanFragment;
import nam.com.rausach.fragment.TraiCayFragment;
import nam.com.rausach.models.LoaiSanPham;
import nam.com.rausach.models.SanPham;
import nam.com.rausach.utils.CheckConection;
import nam.com.rausach.utils.Constant;
import nam.com.rausach.utils.Server;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    LinearLayout toolbar;
    ImageView imgBack, imgMenuActionbar, imgShopping, imgLogout;
    ViewFlipper viewFlipper;
    RecyclerView rvSanPhamMoi, rvSanPham, rvDanhMuc;
    NavigationView navigationView;
    ListView lvMenuActionBar;
    DrawerLayout drawerLayout;
    CircleImageView imgProflieAvatar;
    TextView tvProfileName, tvLogin, tvRegister;
    EditText edSearch;
    FragmentTransaction transaction;
    LinearLayout checkLogin, checkOut, checkSearch;

    LoaiSPAdapter loaiSPAdapter;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    SanPhamAdapter sanPhamAdapter;
    DanhMucAdapter danhMucAdapter;

    ArrayList<LoaiSanPham> arrLoaiSanPham;
    ArrayList<SanPham> arrSanPhamMoi;
    ArrayList<SanPham> arrSanPham;
    ArrayList<LoaiSanPham> arrDanhMuc;

    TraiCayFragment traiCayFragment;
    RauCuFragment rauCuFragment;
    ThitSachFragment thitSachFragment;
    GiaViFragment giaViFragment;
    ThuyHaiSanFragment thuyHaiSanFragment;
    //FragmentSearch fragmentSearch;

    String name, email, birthday, id, loginFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        khoiTao();
        //kiểm tra có mạng thì mới cho load dữ liệu
        if (CheckConection.haveNetworkConnection(getApplicationContext())) {
            ActionViewFlipper();
            GetDataLoaiSP();
            GetDataSPMoi();
            GetDataSanPham();
            getDanhMuc();
            onClickListView();
        } else {
            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView();
        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String isFacebook = sharedpreferences.getString(Constant.LOGINFACEBOOK,null);
        if (isFacebook != null) {
            if (isFacebook.equals("false")) {
                getInfoLocal();
            } else {
                getInfoFacebook();
            }
        }
    }

    //tìm kiếm sản phẩm
    private void searchView() {
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "onEditorAction: " + edSearch.getText().toString());
                    imgBack.setVisibility(View.VISIBLE);
                    transaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_SEARCH, edSearch.getText().toString());
                    FragmentSearch fragmentSearch=new FragmentSearch();
                    fragmentSearch.setArguments(bundle);
                    transaction.replace(R.id.frSearch, fragmentSearch, "fragS");
                    transaction.addToBackStack(null);
                    if (checkSearch != null) {
                        checkSearch.setVisibility(View.GONE);
                    }
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
    }

    //lấy thông tin người dùng từ đăng nhập từ local
    private void getInfoLocal() {
        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Constant.SESSION_STATUS, false);
        String idLocal = sharedpreferences.getString(Constant.ID_LOCAL, null);
        name = sharedpreferences.getString(Constant.USERNAME_LOCAL, null);
        tvProfileName.setText(name);
        if (session) {
            checkLogin.setVisibility(View.VISIBLE);
            checkOut.setVisibility(View.GONE);
        }
    }

    //Lấy thông tin người dùng từ facebook
    private void getInfoFacebook() {
        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id = sharedpreferences.getString((Constant.ID_USER), null);
        name = sharedpreferences.getString(Constant.NAME, null);
        email = sharedpreferences.getString(Constant.EMAIL, null);
        birthday = sharedpreferences.getString(Constant.BIRTHDAY, null);
        loginFacebook = String.valueOf(sharedpreferences.getBoolean(Constant.BIRTHDAY, true));

//        name = getIntent().getStringExtra(Constant.NAME);
//        email = getIntent().getStringExtra(Constant.EMAIL);
//        birthday = getIntent().getStringExtra(Constant.BIRTHDAY);
//        id = getIntent().getStringExtra(Constant.ID_USER);
//        loginFacebook = getIntent().getStringExtra(Constant.LOGINFACEBOOK);
        tvProfileName.setText(name);
        Picasso.with(MainActivity.this)
                .load("https://graph.facebook.com/" + id + "/picture?type=large")
                .into(imgProflieAvatar);
        if (loginFacebook != null) {
            if (loginFacebook.equals("true")) {
                checkLogin.setVisibility(View.VISIBLE);
                checkOut.setVisibility(View.GONE);
            }
        }
    }

    //onClick item listview recyclervew
    private void onClickListView() {
        lvMenuActionBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {
//                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                            startActivity(intent);
                            if (getActionBar() != null) {
                                getActionBar().hide();
                            }
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {
                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(i).getId()));
                            traiCayFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, traiCayFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {

                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(i).getId()));
                            rauCuFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, rauCuFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {

                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(i).getId()));
                            thitSachFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, thitSachFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {

                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(i).getId()));
                            giaViFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, giaViFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {

                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(i).getId()));
                            thuyHaiSanFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, thuyHaiSanFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });

        danhMucAdapter.setOnItemClickListener(new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                switch (position) {
                    case 0:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {
                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(position + 1).getId()));
                            traiCayFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, traiCayFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {
                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(position + 1).getId()));
                            rauCuFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, rauCuFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {

                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(position + 1).getId()));
                            thitSachFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, thitSachFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {

                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(position + 1).getId()));
                            giaViFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, giaViFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if (CheckConection.haveNetworkConnection(getApplicationContext())) {

                            transaction = getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.SANPHAMTHEOID, String.valueOf(arrLoaiSanPham.get(position + 1).getId()));
                            thuyHaiSanFragment.setArguments(bundle);
                            transaction.replace(R.id.frameLayout, thuyHaiSanFragment);
                            transaction.commit();
                        } else {
                            CheckConection.showNotifyConection(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }

    //Lấy ra sản phẩm mới nhất
    private void GetDataSPMoi() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.pathSanPhamMoi, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int idSanPham = 0;
                    String tenSanPham = "";
                    Integer giaSanPham = 0;
                    String imgSanPham = "";
                    String moTaSanPham = "";
                    int idLoaiSP = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            idSanPham = jsonObject.getInt("idSanPham");
                            tenSanPham = jsonObject.getString("tensanpham");
                            giaSanPham = jsonObject.getInt("giasanpham");
                            imgSanPham = jsonObject.getString("hinhanhsanpham");
                            moTaSanPham = jsonObject.getString("motasanpham");
                            idLoaiSP = jsonObject.getInt("idloaisanpham");
                            arrSanPhamMoi.add(new SanPham(idSanPham, tenSanPham, giaSanPham, imgSanPham, moTaSanPham, idLoaiSP));
                            sanPhamMoiAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConection.showNotifyConection(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    //Lấy ra sản phẩm
    private void GetDataSanPham() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.pathSanPham, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int idSanPham = 0;
                    String tenSanPham = "";
                    Integer giaSanPham = 0;
                    String imgSanPham = "";
                    String moTaSanPham = "";
                    int idLoaiSP = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            idSanPham = jsonObject.getInt("idSanPham");
                            tenSanPham = jsonObject.getString("tensanpham");
                            giaSanPham = jsonObject.getInt("giasanpham");
                            imgSanPham = jsonObject.getString("hinhanhsanpham");
                            moTaSanPham = jsonObject.getString("motasanpham");
                            idLoaiSP = jsonObject.getInt("idloaisanpham");
                            arrSanPham.add(new SanPham(idSanPham, tenSanPham, giaSanPham, imgSanPham, moTaSanPham, idLoaiSP));
                            sanPhamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConection.showNotifyConection(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    //lấy ra loại sản phẩm
    private void GetDataLoaiSP() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.pathLoaiSP, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int id = 0;
                    String loaiSP = "";
                    String imageLoaiSP = "";
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            loaiSP = jsonObject.getString("tenLoaiSP");
                            imageLoaiSP = jsonObject.getString("hinhAnhLoaiSP");
                            arrLoaiSanPham.add(new LoaiSanPham(id, loaiSP, imageLoaiSP));
                            loaiSPAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    arrLoaiSanPham.add(6, new LoaiSanPham(0, "Liên hệ", "http://chittagongit.com//images/contact-png-icon/contact-png-icon-18.jpg"));
                    arrLoaiSanPham.add(7, new LoaiSanPham(0, "Thông tin", "https://img.icons8.com/ios/50/000000/info.png"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConection.showNotifyConection(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    //lấy ra các danh mục
    private void getDanhMuc() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.pathLoaiSP, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int idDanhMuc = 0;
                    String tenDanhMuc = "";
                    String imgDanhMuc = "";
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            idDanhMuc = jsonObject.getInt("id");
                            tenDanhMuc = jsonObject.getString("tenLoaiSP");
                            imgDanhMuc = jsonObject.getString("hinhAnhLoaiSP");
                            arrDanhMuc.add(new LoaiSanPham(idDanhMuc, tenDanhMuc, imgDanhMuc));
                            danhMucAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConection.showNotifyConection(getApplicationContext(), error.toString());
            }
        });
        Log.d(TAG, "getDanhMuc: " + jsonArrayRequest.toString());
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://cdn.giigaa.com/banner/dk.jpg-2d64d.jpg");
        mangQuangCao.add("https://media-ak.static-adayroi.com/0_0/80/images/h33/hb1/17291084922910.jpg");
        mangQuangCao.add("https://cdn.giigaa.com/banner/dk.jpg-3b30f.jpg");
        mangQuangCao.add("https://cdn.giigaa.com/banner/dk.jpg-de806.jpg");
        mangQuangCao.add("https://cdn.giigaa.com/banner/dk.jpg-61fe6.jpg");

        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3500);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void anhXa() {
        toolbar = findViewById(R.id.toolbarTrangChu);
        viewFlipper = findViewById(R.id.viewFlipper);
        rvSanPhamMoi = findViewById(R.id.rvSanPhamMoi);
        rvSanPham = findViewById(R.id.rvSanPham);
        navigationView = findViewById(R.id.navigation_view);
        lvMenuActionBar = findViewById(R.id.lvMenuActionBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        edSearch = findViewById(R.id.edSearch);
        tvLogin = findViewById(R.id.tvLogin);
        tvRegister = findViewById(R.id.tvRegister);
        rvDanhMuc = findViewById(R.id.rvDanhMuc);
        tvProfileName = findViewById(R.id.tvProfileName);
        imgProflieAvatar = findViewById(R.id.imgProfile_image);
        checkOut = findViewById(R.id.checkOut);
        checkOut.setVisibility(View.VISIBLE);
        checkLogin = findViewById(R.id.checkLogin);
        checkLogin.setVisibility(View.GONE);
        checkSearch = findViewById(R.id.checkSearch);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setVisibility(View.GONE);
        imgMenuActionbar = findViewById(R.id.imgMenuActionbar);
        imgShopping = findViewById(R.id.imgShopping);
        imgLogout = findViewById(R.id.imgLogout);

        imgBack.setOnClickListener(this);
        imgMenuActionbar.setOnClickListener(this);
        imgShopping.setOnClickListener(this);
        imgProflieAvatar.setOnClickListener(this);
        tvProfileName.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
    }

    public void khoiTao() {
        arrLoaiSanPham = new ArrayList<>();//
        arrLoaiSanPham.add(0, new LoaiSanPham(0, "Trang chủ",
                "https://img.icons8.com/ios/50/000000/home-page.png"));
        loaiSPAdapter = new LoaiSPAdapter(arrLoaiSanPham, getApplicationContext());
        lvMenuActionBar.setAdapter(loaiSPAdapter);

        arrSanPhamMoi = new ArrayList<>();
        sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(), arrSanPhamMoi);
        rvSanPhamMoi.setHasFixedSize(true);
        //rvSanPhamMoi.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rvSanPhamMoi.setLayoutManager(gridLayoutManager);
        rvSanPhamMoi.setAdapter(sanPhamMoiAdapter);

        arrDanhMuc = new ArrayList<>();
        danhMucAdapter = new DanhMucAdapter(getApplicationContext(), arrDanhMuc);
        rvDanhMuc.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManagerDanhMuc =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rvDanhMuc.setLayoutManager(gridLayoutManagerDanhMuc);
        rvDanhMuc.setAdapter(danhMucAdapter);

        arrSanPham = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), arrSanPham);
        rvSanPham.setHasFixedSize(true);
        rvSanPham.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rvSanPham.setAdapter(sanPhamAdapter);

        traiCayFragment = new TraiCayFragment();
        rauCuFragment = new RauCuFragment();
        thitSachFragment = new ThitSachFragment();
        giaViFragment = new GiaViFragment();
        thuyHaiSanFragment = new ThuyHaiSanFragment();
        //fragmentSearch = new FragmentSearch();
    }

    public void sendData() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra(Constant.NAME, name);
        intent.putExtra(Constant.EMAIL, email);
        intent.putExtra(Constant.BIRTHDAY, birthday);
        intent.putExtra(Constant.ID_USER, id);
        intent.putExtra(Constant.LOGINFACEBOOK, loginFacebook);
        startActivity(intent);
    }

    private void intentLogin() {
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgProfile_image:
                sendData();
                break;
            case R.id.tvProfileName:
                sendData();
                break;
            case R.id.tvLogin:
                intentLogin();
                break;
            case R.id.tvRegister:
                Intent register = new Intent(MainActivity.this, SingUpActivity.class);
                startActivity(register);
                break;
            case R.id.imgBack:
                transaction = getSupportFragmentManager().beginTransaction();
                FragmentSearch fragmentSearch = (FragmentSearch) getSupportFragmentManager().findFragmentByTag("fragS");
                transaction.remove(fragmentSearch);
                transaction.commit();
                edSearch.setText("");
                checkSearch.setVisibility(View.VISIBLE);
                imgBack.setVisibility(View.GONE);
                break;
            case R.id.imgMenuActionbar:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.imgShopping:

                break;
            case R.id.imgLogout:
                SharedPreferences preferences = getSharedPreferences(Constant.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                checkLogin.setVisibility(View.GONE);
                checkOut.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }
}