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

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ItemHolder> {
    Context context;
    ArrayList<SanPham> arrayListSanPham;

    public SanPhamAdapter(Context context, ArrayList<SanPham> arrayListSanPham) {
        this.context = context;
        this.arrayListSanPham = arrayListSanPham;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.line,null);
        ItemHolder itemHolder=new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        SanPham sanPham=arrayListSanPham.get(i);
        itemHolder.tvTenSP.setText(sanPham.getTenSP());
        DecimalFormat decimalFormat=new DecimalFormat("###,###.###");
        itemHolder.tvGiaSP.setText("Giá: "+decimalFormat.format(sanPham.getGiaSP())+"Đ");
        //itemHolder.tvGiaSP.setText("Giá: "+sanPham.getGiaSP()+"Đ");
        Picasso.with(context).load(sanPham.getImageSP())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.failimage)
                .into(itemHolder.imgSP);
    }

    @Override
    public int getItemCount() {
        return arrayListSanPham.size();
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
