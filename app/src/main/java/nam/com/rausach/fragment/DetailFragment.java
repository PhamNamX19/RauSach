package nam.com.rausach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import nam.com.rausach.R;
import nam.com.rausach.models.SanPham;

public class DetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail, container, false);

        WebView wvChiTiet = v.findViewById(R.id.wvChiTiet);

        Intent intent = getActivity().getIntent();
        SanPham sanPham = (SanPham) intent.getSerializableExtra("sanpham");

        wvChiTiet.loadData(sanPham.getMoTaSP(), "text/html", null);

        return v;
    }
}