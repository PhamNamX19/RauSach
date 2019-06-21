package nam.com.rausach.models;

public class GioHangItem {

    private int id;
    private String tenSanPham;
    private String giaSanPham;
    private String anhSanPham;
    private String moTaSanPham;
    private String loaiSanPham;
    private int soLuong;
    private int idCustomer;
    private int idSanPham;

    public GioHangItem() {
    }

    public GioHangItem(int id, String tenSanPham, String giaSanPham, String anhSanPham,
                       String moTaSanPham, String loaiSanPham, int soLuong, int idCustomer) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.anhSanPham = anhSanPham;
        this.moTaSanPham = moTaSanPham;
        this.loaiSanPham = loaiSanPham;
        this.soLuong = soLuong;
        this.idCustomer = idCustomer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(String giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getAnhSanPham() {
        return anhSanPham;
    }

    public void setAnhSanPham(String anhSanPham) {
        this.anhSanPham = anhSanPham;
    }

    public String getMoTaSanPham() {
        return moTaSanPham;
    }

    public void setMoTaSanPham(String moTaSanPham) {
        this.moTaSanPham = moTaSanPham;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(int idSanPham) {
        this.idSanPham = idSanPham;
    }

    @Override
    public String toString() {
        return "GioHangItem{" +
                "id=" + id +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", giaSanPham='" + giaSanPham + '\'' +
                ", anhSanPham='" + anhSanPham + '\'' +
                ", moTaSanPham='" + moTaSanPham + '\'' +
                ", loaiSanPham='" + loaiSanPham + '\'' +
                ", soLuong=" + soLuong +
                ", idCustomer=" + idCustomer +
                ", idSanPham=" + idSanPham +
                '}';
    }
}
