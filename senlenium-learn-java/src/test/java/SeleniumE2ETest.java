

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.seleniumlearn.SeleniumLearnApplication;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(classes = SeleniumLearnApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumE2ETest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    void beforeAll() {
        // Ensure driver binaries are available before tests start
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        String headless = System.getenv().getOrDefault("HEADLESS", "0");
        if (!"0".equals(headless)) {
            // newer headless flag recommended; fallback will work on older versions
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
        }
        options.addArguments("--window-size=1200,800");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/";
    }

    @Test
    void testHomePageTitle() {
        driver.get(baseUrl());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // wait for the title and main input to be present
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.titleContains("Selenium Learning App"));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("name")));
        Assertions.assertTrue(driver.getTitle().contains("Selenium Learning App"));
    }

    @Test
    void testGreetingShowsName() {
        driver.get(baseUrl());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement nameInput = driver.findElement(By.id("name"));
        WebElement button = driver.findElement(By.id("greet"));

        nameInput.clear();
        nameInput.sendKeys("Alice");
        button.click();

        WebElement greeting = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("greeting")));
        Assertions.assertEquals("Hello, Alice!", greeting.getText());
    }

    @Test
    void testGreetingDefaultForEmptyName() {
        driver.get(baseUrl());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement nameInput = driver.findElement(By.id("name"));
        WebElement button = driver.findElement(By.id("greet"));

        nameInput.clear();
        button.click();

        WebElement greeting = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("greeting")));
        Assertions.assertEquals("Hello, stranger!", greeting.getText());
    }
}