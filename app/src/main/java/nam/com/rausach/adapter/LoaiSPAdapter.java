package nam.com.rausach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nam.com.rausach.R;
import nam.com.rausach.models.LoaiSanPham;

public class LoaiSPAdapter extends BaseAdapter {
    ArrayList<LoaiSanPham> arrayListLoaiSP;
    Context context;

    public LoaiSPAdapter(ArrayList<LoaiSanPham> arrayListLoaiSP, Context context) {
        this.arrayListLoaiSP = arrayListLoaiSP;
        this.context = context;
    }

    @Override
    public int getCount() {
        //đổ hết dữ liệu đã đưa vào mảng
        return arrayListLoaiSP.size();
    }

    @Override
    public Object getItem(int i) {
        //lấy ra từng thuộc tính ở trong mảng
        return arrayListLoaiSP.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        TextView tvLoaiSP;
        ImageView imgLoaiSP;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.listview_loaisp,null);
            viewHolder.tvLoaiSP=(TextView) view.findViewById(R.id.tvLoaiSP);
            viewHolder.imgLoaiSP=(ImageView) view.findViewById(R.id.imgLoaiSP);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) view.getTag();//khi run lần 2 sẽ có view của gtri và gắn lại
        }
        LoaiSanPham loaiSanPham= (LoaiSanPham) getItem(i);
        viewHolder.tvLoaiSP.setText(loaiSanPham.getTenLoaiSP());
        Picasso.with(context).load(loaiSanPham.getHinhAnhLoaiSP())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.failimage)
                .into(viewHolder.imgLoaiSP);
        return view;
    }
}
