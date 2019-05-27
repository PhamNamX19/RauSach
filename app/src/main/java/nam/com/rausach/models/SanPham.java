package nam.com.rausach.models;

public class SanPham {
    private int id;
    private String tenSP;
    private Integer giaSP;
    private String imageSP;
    private String moTaSP;
    private int idTheoSanPham;

    public SanPham(int id, String tenSP, Integer giaSP, String imageSP, String moTaSP, int idTheoSanPham) {
        this.id = id;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.imageSP = imageSP;
        this.moTaSP = moTaSP;
        this.idTheoSanPham = idTheoSanPham;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public Integer getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(Integer giaSP) {
        this.giaSP = giaSP;
    }

    public String getImageSP() {
        return imageSP;
    }

    public void setImageSP(String imageSP) {
        this.imageSP = imageSP;
    }

    public String getMoTaSP() {
        return moTaSP;
    }

    public void setMoTaSP(String moTaSP) {
        this.moTaSP = moTaSP;
    }

    public int getIdTheoSanPham() {
        return idTheoSanPham;
    }

    public void setIdTheoSanPham(int idTheoSanPham) {
        this.idTheoSanPham = idTheoSanPham;
    }
}
