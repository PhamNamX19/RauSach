package nam.com.rausach.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import nam.com.rausach.R;
import nam.com.rausach.adapter.ViewPagerAdapter;

public class DetailProductActivity extends AppCompatActivity {

    private static final String TAG = "ChiTietSanPhamActivity";
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabs;
    private ImageView imageViewBack;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        actionBack();
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
