package nam.com.rausach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import nam.com.rausach.R;
import nam.com.rausach.models.SanPham;

public class OverviewFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FragmentTongQuan";
    RecyclerView recyclerViewSanPhamLienQuan;
    View v;
    ImageView imgAnhChiTiet;
    TextView tvTenSanPham;
    TextView tvGiaSanPham;
    TextView tvChiTietSanPham;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_overview, container, false);
        imgAnhChiTiet = v.findViewById(R.id.imgAnhChiTiet);
        tvTenSanPham = v.findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = v.findViewById(R.id.tvGiaSanPham);
        tvChiTietSanPham = v.findViewById(R.id.tvChiTietSanPham);
        TextView tvXemThem = v.findViewById(R.id.tvXemThem);
        tvXemThem.setOnClickListener(this);
        recyclerViewSanPhamLienQuan = v.findViewById(R.id.recyclerViewSanPhamLienQuan);
        putDuLieuChiTiet();

        return v;
    }

    private void putDuLieuChiTiet() {
        Intent intent = getActivity().getIntent();
        SanPham sanPham = (SanPham) intent.getSerializableExtra("sanpham");
        Picasso.with(getContext()).load(sanPham.getImageSP()).into(imgAnhChiTiet);
        tvTenSanPham.setText(sanPham.getTenSP());
        StringBuilder builderGiaSanPham = new StringBuilder();
        builderGiaSanPham.append(sanPham.getGiaSP());
        builderGiaSanPham.append(" vnđ");
        tvGiaSanPham.setText(String.valueOf(builderGiaSanPham));
        tvChiTietSanPham.setText(sanPham.getMoTaSP());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvXemThem:
                ViewPager viewPager = getActivity().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(2);
                break;
        }
    }
}
