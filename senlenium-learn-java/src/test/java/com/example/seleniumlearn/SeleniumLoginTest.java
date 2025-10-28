package com.example.seleniumlearn;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(classes = SeleniumLearnApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumLoginTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        String headless = System.getenv().getOrDefault("HEADLESS", "1");
        if (!"0".equals(headless)) {
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
    void testLoginFormElementsPresent() {
        driver.get(baseUrl() + "login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("username")));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("password")));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("login")));

        WebElement u = driver.findElement(By.id("username"));
        WebElement p = driver.findElement(By.id("password"));
        WebElement b = driver.findElement(By.id("login"));

        Assertions.assertNotNull(u);
        Assertions.assertNotNull(p);
        Assertions.assertNotNull(b);
    }

    @Test
    void testSuccessfulLoginShowsWelcome() {
        driver.get(baseUrl() + "login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login"));

        username.clear();
        password.clear();
        username.sendKeys("testuser");
        password.sendKeys("password123");
        loginBtn.click();

        // login.html shows the message after a short timeout; wait for it
        WebElement msg = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.id("login-message")));
        Assertions.assertTrue(msg.getText().contains("Welcome, testuser"));
    }

    @Test
    void testFailedLoginShowsError() {
        driver.get(baseUrl() + "login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login"));

        username.clear();
        password.clear();
        username.sendKeys("wronguser");
        password.sendKeys("wrongpass");
        loginBtn.click();

        // login.html shows the message after a short timeout; wait for it
        WebElement msg = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.id("login-message")));
        Assertions.assertTrue(msg.getText().contains("Invalid credentials"));

}
}