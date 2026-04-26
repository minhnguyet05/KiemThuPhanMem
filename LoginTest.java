import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

// Đảm bảo class này kế thừa BaseTest (hoặc class chứa setup driver)
public class LoginTest extends BaseTest {

    // --- DỮ LIỆU CẤU HÌNH ---
    private final String VALID_USER = "duyetht";
    private final String VALID_PASS = "123456";
    private final String INVALID_PASS = "sai_matkhau";
    private final String LOGIN_CUSTOMER_URL = "http://hauiproj.somee.com/Dangnhap.aspx";

    // --- SELECTOR CHỨC NĂNG ĐĂNG NHẬP ---
    private final By CUS_USERNAME_FIELD = By.id("ContentPlaceHolder1_txtTaikhoan");
    private final By CUS_PASSWORD_FIELD = By.id("ContentPlaceHolder1_txtMatkhau");
    private final By CUS_LOGIN_BUTTON = By.id("ContentPlaceHolder1_btDangnhap");

    // Selector Xác minh thành công
    private final By WELCOME_USER_LABEL = By.id("lblHTTK");

    // Selector Xác minh lỗi Đăng nhập thất bại (Dựa trên hình ảnh bạn cung cấp)
    private final By ERROR_MESSAGE_LABEL_FAIL = By.xpath("//*[contains(text(), 'Sai tên tài khoản hoặc mật khẩu!')]");

    // Selector Xác minh lỗi Để trống trường (Sử dụng Validator của ASP.NET)
    // Giả định: Validator cho tài khoản có ID là ContentPlaceHolder1_RequiredFieldValidator1
    private final By ERROR_MESSAGE_LABEL_REQUIRED_USER = By.id("ContentPlaceHolder1_RequiredFieldValidator1");

    // Giả định: Validator cho mật khẩu có ID là ContentPlaceHolder1_RequiredFieldValidator2
    private final By ERROR_MESSAGE_LABEL_REQUIRED_PASS = By.id("ContentPlaceHolder1_RequiredFieldValidator2");


    // =========================================================================
    // TC 1: ĐĂNG NHẬP THÀNH CÔNG (POSITIVE)
    // =========================================================================

    @Test(description = "1. Kiểm tra Đăng nhập thành công với tài khoản hợp lệ")
    public void testSuccessfulLogin() {

        System.out.println("--- TC 1: Bắt đầu Đăng nhập thành công ---");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(LOGIN_CUSTOMER_URL);

        // Nhập thông tin hợp lệ
        driver.findElement(CUS_USERNAME_FIELD).sendKeys(VALID_USER);
        driver.findElement(CUS_PASSWORD_FIELD).sendKeys(VALID_PASS);
        driver.findElement(CUS_LOGIN_BUTTON).click();

        // Xác minh kết quả
        wait.until(ExpectedConditions.textToBePresentInElementLocated(WELCOME_USER_LABEL, "Xin chào: " + VALID_USER));

        WebElement welcomeLabel = driver.findElement(WELCOME_USER_LABEL);
        Assert.assertTrue(welcomeLabel.getText().contains(VALID_USER),
                "Assertion FAIL: Đăng nhập thành công nhưng tên người dùng không hiển thị.");

        System.out.println("-> KẾT QUẢ TC 1: PASS. Đăng nhập thành công.");
    }

    // =========================================================================
    // TC 2: ĐĂNG NHẬP THẤT BẠI (NEGATIVE - Sai mật khẩu)
    // =========================================================================

    @Test(description = "2. Kiểm tra Đăng nhập thất bại khi nhập sai mật khẩu")
    public void testFailedLogin_InvalidPassword() {

        System.out.println("\n--- TC 2: Bắt đầu Đăng nhập thất bại (Sai mật khẩu) ---");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(LOGIN_CUSTOMER_URL);

        // Nhập Tài khoản hợp lệ và Mật khẩu SAI
        driver.findElement(CUS_USERNAME_FIELD).sendKeys(VALID_USER);
        driver.findElement(CUS_PASSWORD_FIELD).sendKeys(INVALID_PASS);
        driver.findElement(CUS_LOGIN_BUTTON).click();

        // Xác minh kết quả (Thông báo lỗi hiển thị)
        wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE_LABEL_FAIL));

        WebElement errorLabel = driver.findElement(ERROR_MESSAGE_LABEL_FAIL);
        String expectedError = "Sai tên tài khoản hoặc mật khẩu!";
        String actualError = errorLabel.getText();

        Assert.assertTrue(actualError.contains(expectedError),
                "Assertion FAIL: Thông báo lỗi không chính xác. Mong đợi: '" + expectedError + "'. Thực tế: '" + actualError + "'");

        System.out.println("-> KẾT QUẢ TC 2: PASS. Hiển thị thông báo lỗi: " + actualError);
    }
}