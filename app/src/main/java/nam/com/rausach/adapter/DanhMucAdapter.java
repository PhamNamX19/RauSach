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

import java.util.ArrayList;

import nam.com.rausach.R;
import nam.com.rausach.models.LoaiSanPham;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ViewHolder> {

    Context context;
    ArrayList<LoaiSanPham> arrayListDanhMuc;

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public DanhMucAdapter(Context context, ArrayList<LoaiSanPham> arrayListDanhMuc) {
        this.context = context;
        this.arrayListDanhMuc = arrayListDanhMuc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_danh_muc, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LoaiSanPham loaiSanPham = arrayListDanhMuc.get(i);
        viewHolder.tvDanhMuc.setText(loaiSanPham.getTenLoaiSP());
        Picasso.with(context).load(loaiSanPham.getHinhAnhLoaiSP())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.failimage)
                .into(viewHolder.imgDanhMuc);

    }

    @Override
    public int getItemCount() {
        return arrayListDanhMuc.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDanhMuc;
        TextView tvDanhMuc;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgDanhMuc = itemView.findViewById(R.id.imgDanhMuc);
            tvDanhMuc = itemView.findViewById(R.id.tvDanhMuc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });

        }
    }
}
