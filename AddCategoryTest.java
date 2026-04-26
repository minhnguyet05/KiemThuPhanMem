import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddCategoryTest extends BaseTest {

    // --- DỮ LIỆU KIỂM THỬ ---
    private final String CATEGORY_ID = String.valueOf(System.currentTimeMillis() % 10000);
    private final String CATEGORY_NAME = "Danh Muc Auto Test " + CATEGORY_ID;

    // --- SELECTOR ĐĂNG NHẬP (ĐÃ XÁC ĐỊNH CHÍNH XÁC) ---
    private final By USERNAME_FIELD = By.id("ContentPlaceHolder1_txtTaikhoan");
    private final By PASSWORD_FIELD = By.id("ContentPlaceHolder1_txtMatkhau");
    private final By LOGIN_BUTTON = By.id("ContentPlaceHolder1_btDangnhap");

    // --- SELECTOR THÊM DANH MỤC (CHÍNH XÁC) ---
    private final By ID_FIELD = By.id("ContentPlaceHolder1_txtID");
    private final By NAME_FIELD = By.id("ContentPlaceHolder1_txtTenDM");
    private final By ADD_NEW_BUTTON = By.id("ContentPlaceHolder1_LinkButton1");
    private final By SUCCESS_MESSAGE_ELEMENT = By.id("ContentPlaceHolder1_lblThongBao");

    // Dữ liệu Đăng nhập ĐÃ CẬP NHẬT CHÍNH XÁC
    private final String ADMIN_USER = "Admin";
    private final String ADMIN_PASS = "1234";


    @Test(description = "Kiểm tra chức năng thêm danh mục mới thành công")
    public void testAddCategorySuccess() throws InterruptedException {

        System.out.println("--- Bắt đầu Test: Thêm Danh mục Thành công ---");

        // --- BƯỚC 1: ĐĂNG NHẬP ---
        driver.get(LOGIN_URL); // Truy cập Dangnhap.aspx

        System.out.println("1. Đang nhập với TK: " + ADMIN_USER + "/ MK: " + ADMIN_PASS);
        driver.findElement(USERNAME_FIELD).sendKeys(ADMIN_USER);
        driver.findElement(PASSWORD_FIELD).sendKeys(ADMIN_PASS);
        driver.findElement(LOGIN_BUTTON).click();

        // --- BƯỚC 2: CHUYỂN ĐẾN TRANG QUẢN LÝ DANH MỤC ---
        String categoryAdminUrl = "http://hauiproj.somee.com/Admin/Quanlydanhmuc.aspx";
        System.out.println("2. Chuyển đến trang quản lý: " + categoryAdminUrl);
        driver.get(categoryAdminUrl);

        // --- BƯỚC 3: NHẬP THÔNG TIN DANH MỤC MỚI ---
        System.out.println("3. Nhập Mã: " + CATEGORY_ID + ", Tên: " + CATEGORY_NAME);
        driver.findElement(ID_FIELD).sendKeys(CATEGORY_ID);
        driver.findElement(NAME_FIELD).sendKeys(CATEGORY_NAME);

        // --- BƯỚC 4: CLICK NÚT THÊM MỚI ---
        System.out.println("4. Nhấn nút Thêm Mới...");
        driver.findElement(ADD_NEW_BUTTON).click();

        // Chờ để hệ thống xử lý
        Thread.sleep(2000);

        // --- BƯỚC 5: XÁC MINH (ASSERTION) ---

        // 5.1. Kiểm tra thông báo thành công
        WebElement successMessage = driver.findElement(SUCCESS_MESSAGE_ELEMENT);
        String actualMessage = successMessage.getText().trim();

        Assert.assertTrue(actualMessage.toLowerCase().contains("thành công"),
                "Lỗi Thông báo: Thông báo không chứa 'thành công'. Message: " + actualMessage);

        System.out.println("-> Thông báo hệ thống: " + actualMessage);

        // 5.2. Kiểm tra danh mục mới có trong bảng không
        By categoryInTable = By.xpath("//td[contains(text(), '" + CATEGORY_NAME + "')]");

        try {
            driver.findElement(categoryInTable);
            System.out.println("-> Danh mục '" + CATEGORY_NAME + "' đã được tìm thấy trong danh sách.");
        } catch (Exception e) {
            Assert.fail("Lỗi Danh sách: Không tìm thấy danh mục mới trong bảng sau khi thêm.");
        }

        System.out.println("Kiểm thử Thêm Danh mục **HOÀN TẤT VÀ THÀNH CÔNG**!");
    }
}