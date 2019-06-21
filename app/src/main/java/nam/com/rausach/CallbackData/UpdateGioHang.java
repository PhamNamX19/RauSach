package nam.com.rausach.CallbackData;

import java.util.List;

import nam.com.rausach.models.GioHangItem;

public interface UpdateGioHang {
    void updateSanPham(List<GioHangItem> gioHangItems);

    void updateTongTien(int tongTien);
}
