package nam.com.rausach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import nam.com.rausach.R;
import nam.com.rausach.models.SanPham;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.ItemHolder> {
    Context context;
    ArrayList<SanPham> arrayListSanPhamMoi;

    public SanPhamMoiAdapter(Context context, ArrayList<SanPham> arrayListSanPhamMoi) {
        this.context = context;
        this.arrayListSanPhamMoi = arrayListSanPhamMoi;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.line_sanphammoi,null);
        ItemHolder itemHolder=new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        SanPham sanPham=arrayListSanPhamMoi.get(i);
        itemHolder.tvTenSP.setText(sanPham.getTenSP());
        DecimalFormat decimalFormat=new DecimalFormat("###,###.###");
        itemHolder.tvGiaSP.setText("Giá: "+decimalFormat.format(sanPham.getGiaSP())+"Đ");
        Picasso.with(context).load(sanPham.getImageSP())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.failimage)
                .into(itemHolder.imgSP);
    }

    @Override
    public int getItemCount() {
        return arrayListSanPhamMoi.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imgSP;
        public TextView tvTenSP,tvGiaSP;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgSP=itemView.findViewById(R.id.imgSanPham);
            tvTenSP=itemView.findViewById(R.id.tvTenSP);
            tvGiaSP=itemView.findViewById(R.id.tvGiaSP);
        }
    }
}
