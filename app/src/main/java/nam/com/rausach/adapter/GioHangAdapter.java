package nam.com.rausach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nam.com.rausach.callback.UpdateGioHang;
import nam.com.rausach.R;
import nam.com.rausach.controller.GetSanPhamGioHangApi;
import nam.com.rausach.controller.UpdateSPGioHang;
import nam.com.rausach.models.GioHangItem;
import nam.com.rausach.utils.MySharedPreferences;
import nam.com.rausach.utils.Server;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    private static final String TAG = "GioHangAdapter";
    private Context mContext;
    private List<GioHangItem> mSanPhams;

    private int tongTien = 0;

    private UpdateGioHang iUpdateGioHang;

    public void setiUpdateGioHang(UpdateGioHang iUpdateGioHang) {
        this.iUpdateGioHang = iUpdateGioHang;
    }

    public GioHangAdapter(Context context, List<GioHangItem> sanPhams) {
        this.mContext = context;
        this.mSanPhams = sanPhams;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gio_hang, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        GioHangItem sanPham = mSanPhams.get(i);
        Picasso.with(mContext).load(sanPham.getAnhSanPham()).into(myViewHolder.imageViewAnhSP);
        myViewHolder.textViewTenSanPham.setText(sanPham.getTenSanPham());
        myViewHolder.textViewGiaSanPham.setText(sanPham.getGiaSanPham());
        myViewHolder.textViewSoLuong.setText(String.valueOf(sanPham.getSoLuong()));
    }

    @Override
    public int getItemCount() {
        return mSanPhams.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageViewAnhSP;
        private TextView textViewTenSanPham;
        private TextView textViewGiam;
        private TextView textViewSoLuong;
        private TextView textViewTang;
        private TextView textViewGiaSanPham;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewAnhSP = itemView.findViewById(R.id.imageViewAnhSP);
            textViewTenSanPham = itemView.findViewById(R.id.textViewTenSanPham);
            textViewGiam = itemView.findViewById(R.id.textViewGiam);
            textViewSoLuong = itemView.findViewById(R.id.textViewSoLuong);
            textViewTang = itemView.findViewById(R.id.textViewTang);
            textViewGiaSanPham = itemView.findViewById(R.id.textViewGiaSanPham);

            textViewTang.setOnClickListener(this);
            textViewGiam.setOnClickListener(this);
        }

        private List<GioHangItem> getGioHangItems(){
            List<GioHangItem> gioHangItems = null;
            GetSanPhamGioHangApi getSanPhamGioHangApi = new GetSanPhamGioHangApi();
            String idUser = MySharedPreferences.getSharedPreferences(mContext);

            String apiGioHang = null;
            try {
                apiGioHang = getSanPhamGioHangApi.execute(Server.pathGioHang, idUser).get();
                JSONArray jsonArray = new JSONArray(apiGioHang);
                gioHangItems = new ArrayList<>();

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

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gioHangItems;
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            int id;
            UpdateSPGioHang updateGioHang;
            String respon;

            switch (v.getId()) {
                case R.id.textViewTang:
                    int soLuongTang = mSanPhams.get(position).getSoLuong() + 1;
                    Log.d(TAG, "onClick: "+ position);
                    id = mSanPhams.get(position).getId();
                    updateGioHang = new UpdateSPGioHang();
                    try {
                        respon = updateGioHang.execute(Server.pathUpdateSPGioHang,
                                String.valueOf(soLuongTang), String.valueOf(id)).get();
                        Log.d(TAG, "onClick: "+respon);
                        iUpdateGioHang.updateSanPham(getGioHangItems());
                        iUpdateGioHang.updateTongTien(tongTien);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.textViewGiam:
                    int soLuongGiam = mSanPhams.get(position).getSoLuong() - 1;
                    id = mSanPhams.get(position).getId();
                    updateGioHang = new UpdateSPGioHang();
                    try {
                        respon = updateGioHang.execute(Server.pathUpdateSPGioHang,
                                String.valueOf(soLuongGiam), String.valueOf(id)).get();
                        Log.d(TAG, "onClick: "+respon);
                        iUpdateGioHang.updateSanPham(getGioHangItems());
                        iUpdateGioHang.updateTongTien(tongTien);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
