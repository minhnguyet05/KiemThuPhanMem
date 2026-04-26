import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    // URL Đăng nhập chính xác
    protected final String LOGIN_URL = "http://hauiproj.somee.com/Dangnhap.aspx";

    @BeforeMethod
    public void setup() {
        // Tự động tải driver Chrome
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Cấu hình thời gian chờ ngầm định và phóng to cửa sổ
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}