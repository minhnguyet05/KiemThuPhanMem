import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class CartManagementTest extends BaseTest {

    // --- DỮ LIỆU CẤU HÌNH VÀ SELECTOR ---
    private final String CUSTOMER_USER = "duyetht";
    private final String CUSTOMER_PASS = "123456";
    private final String LOGIN_CUSTOMER_URL = "http://hauiproj.somee.com/Dangnhap.aspx";
    private final String PRODUCT_LIST_URL = "http://hauiproj.somee.com/Kinhmat.aspx";
    private final String CART_URL = "http://hauiproj.somee.com/Giohang.aspx";
    private final String PRODUCT_NAME = "Kính Thời Trang 9";

    // Các Selector giữ nguyên
    private final By CUS_USERNAME_FIELD = By.id("ContentPlaceHolder1_txtTaikhoan");
    private final By CUS_PASSWORD_FIELD = By.id("ContentPlaceHolder1_txtMatkhau");
    private final By CUS_LOGIN_BUTTON = By.id("ContentPlaceHolder1_btDangnhap");
    private final By WELCOME_USER_LABEL = By.id("lblHTTK");
    private final By DELETE_ALL_CART_BUTTON = By.id("ContentPlaceHolder1_btnXoaGioHang");
    private final By PRODUCT_DETAIL_LINK = By.id("ContentPlaceHolder1_DataList1_HyperLink1_7");
    private final By QUANTITY_INPUT = By.id("ContentPlaceHolder1_Datalist1_txtSoLuong_0");
    private final By ADD_TO_CART_BUTTON = By.id("ContentPlaceHolder1_Datalist1_btnThemVaoGio_0");
    private final By TOTAL_PRICE_LABEL = By.id("ContentPlaceHolder1_lblTongTien");
    private final By ITEM_QUANTITY_INPUT = By.id("ContentPlaceHolder1_gvGioHang_txtSoLuong_0");
    private final By ITEM_UPDATE_BUTTON = By.xpath("//input[@value='Cập nhật']");
    private final By ITEM_DELETE_BUTTON = By.xpath("//input[@value='Xóa']");

    // *** KHẮC PHỤC LỖI TẠI ĐÂY ***: Selector linh hoạt hơn
    private final By ITEM_IN_CART_XPATH = By.xpath("//*[contains(text(), '" + PRODUCT_NAME + "')]");


    @Test(description = "Kiểm tra toàn bộ luồng Quản lý Giỏ hàng của Khách hàng đã đăng nhập")
    public void testFullCartManagementFlow() throws InterruptedException {

        System.out.println("--- Bắt đầu Test: Quản lý Giỏ hàng (Khách hàng) ---");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // --- BƯỚC 1: ĐĂNG NHẬP KHÁCH HÀNG ---
        System.out.println("\n--- BƯỚC 1: ĐĂNG NHẬP (Tài khoản: " + CUSTOMER_USER + ") ---");
        driver.get(LOGIN_CUSTOMER_URL);

        driver.findElement(CUS_USERNAME_FIELD).sendKeys(CUSTOMER_USER);
        driver.findElement(CUS_PASSWORD_FIELD).sendKeys(CUSTOMER_PASS);
        driver.findElement(CUS_LOGIN_BUTTON).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(WELCOME_USER_LABEL, "Xin chào: " + CUSTOMER_USER));
        System.out.println("-> Đăng nhập thành công với tài khoản: " + CUSTOMER_USER);

        // --- BƯỚC 2: CHUẨN BỊ (Xóa giỏ hàng cũ nếu có) ---
        System.out.println("\n--- BƯỚC 2: Chuẩn bị: Xóa giỏ hàng cũ ---");
        driver.get(CART_URL);

        if (isElementPresent(TOTAL_PRICE_LABEL)) {
            driver.findElement(DELETE_ALL_CART_BUTTON).click();
            System.out.println("-> Đã thực hiện thao tác xóa toàn bộ giỏ hàng cũ.");
            Thread.sleep(2000);
        } else {
            System.out.println("-> Giỏ hàng đã rỗng. Tiếp tục Test.");
        }

        // --- BƯỚC 3: ĐIỀU HƯỚNG VÀ THÊM SẢN PHẨM VÀO GIỎ ---
        System.out.println("\n--- BƯỚC 3: Điều hướng và Thêm sản phẩm (Số lượng 2) ---");

        driver.get(PRODUCT_LIST_URL);
        wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_DETAIL_LINK)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(QUANTITY_INPUT));
        driver.findElement(QUANTITY_INPUT).clear();
        driver.findElement(QUANTITY_INPUT).sendKeys("2");
        driver.findElement(ADD_TO_CART_BUTTON).click();

        // XÁC MINH 3.1: Chuyển về trang Giỏ hàng và Sản phẩm xuất hiện
        System.out.println("-> Đang chờ trang Giỏ hàng tải lại và sản phẩm xuất hiện...");
        wait.until(ExpectedConditions.urlToBe(CART_URL));

        // Thêm một đoạn Sleep ngắn ĐẶC BIỆT sau khi trang Giỏ hàng tải lại,
        // để chắc chắn dữ liệu giỏ hàng được render xong.
        Thread.sleep(1500);

        // CHỜ SẢN PHẨM XUẤT HIỆN (Dùng selector mới)
        wait.until(ExpectedConditions.visibilityOfElementLocated(ITEM_IN_CART_XPATH));
        System.out.println("-> Sản phẩm '" + PRODUCT_NAME + "' đã xuất hiện trong giỏ hàng.");

        WebElement totalLabel = driver.findElement(TOTAL_PRICE_LABEL);
        Assert.assertTrue(totalLabel.getText().contains("800.000 VNĐ"),
                "Assertion 3.1 FAIL: Tổng tiền không đúng (Phải là 800.000 VNĐ). Thực tế: " + totalLabel.getText());
        System.out.println("-> Xác minh tổng tiền thành công: " + totalLabel.getText());


        // --- BƯỚC 4: SỬA SỐ LƯỢNG (2 -> 5) ---
        System.out.println("\n--- BƯỚC 4: SỬA SỐ LƯỢNG (2 -> 5) ---");

        // Kiểm tra xem ô nhập số lượng có tồn tại không trước khi thao tác
        wait.until(ExpectedConditions.visibilityOfElementLocated(ITEM_QUANTITY_INPUT));

        driver.findElement(ITEM_QUANTITY_INPUT).clear();
        driver.findElement(ITEM_QUANTITY_INPUT).sendKeys("5");
        driver.findElement(ITEM_UPDATE_BUTTON).click();

        // XÁC MINH 4.1: Tổng tiền mới
        wait.until(ExpectedConditions.textToBePresentInElementLocated(TOTAL_PRICE_LABEL, "2.000.000 VNĐ"));
        totalLabel = driver.findElement(TOTAL_PRICE_LABEL);
        Assert.assertTrue(totalLabel.getText().contains("2.000.000 VNĐ"),
                "Assertion 4.1 FAIL: Tổng tiền không đúng sau khi sửa số lượng (Phải là 2.000.000 VNĐ). Thực tế: " + totalLabel.getText());
        System.out.println("-> Sửa số lượng thành công. Tổng tiền mới: " + totalLabel.getText());


        // --- BƯỚC 5: XÓA MỘT MẶT HÀNG ---
        System.out.println("\n--- BƯỚC 5: XÓA MỘT MẶT HÀNG ---");
        driver.findElement(ITEM_DELETE_BUTTON).click();

        // XÁC MINH 5.1: Sản phẩm đã bị xóa (Giỏ hàng trống)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(ITEM_IN_CART_XPATH));
        Assert.assertFalse(isElementPresent(ITEM_IN_CART_XPATH),
                "Assertion 5.1 FAIL: Giỏ hàng vẫn có sản phẩm sau khi xóa.");
        System.out.println("-> Xóa mặt hàng thành công. Giỏ hàng đã trống. TEST HOÀN TẤT VÀ PASS!");
    }

    private boolean isElementPresent(By by) {
        try {
            // Sử dụng findElements và kiểm tra kích thước list để tránh Exception
            return driver.findElements(by).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}