package com.softserve.academy;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

class GreenCityNotValidRegistrationTest {

    private static WebDriver driver;

    @BeforeAll
    static void setUp() {
        driver = WebDriverManager.chromedriver().create();
        driver.manage().window().maximize();
    }

    @BeforeEach
    void openUrl() {
        driver.navigate().to("https://www.greencity.cx.ua/#/greenCity");
        sleep(1000);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void registration(String email, String userName, String password) {
        driver.findElement(By.cssSelector(".header_sign-up-btn > span")).click();
        sleep(500);

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.clear();
        emailInput.sendKeys(email);

        WebElement userNameInput = driver.findElement(By.id("firstName"));
        userNameInput.clear();
        userNameInput.sendKeys(userName);

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        WebElement confirmPasswordInput = driver.findElement(By.id("repeatPassword"));
        confirmPasswordInput.clear();
        confirmPasswordInput.sendKeys(password);

        sleep(500);
        WebElement btnSubmit = driver.findElement(By.cssSelector(".greenStyle"));
        assertFalse(btnSubmit.isEnabled());


    }


    static Stream<Arguments> invalidEmails() {
        return Stream.of(
                Arguments.of(" ", "Roman Ivanov", "Qwerty123!"),
                Arguments.of("   ", "Roman Ivanov", "Qwerty123!"),
                Arguments.of("", "Roman Ivanov", "Qwerty123!"),
                Arguments.of("@gmail.com", "Ivan Brown", "Qwerty123!"),
                Arguments.of("brown@", "Ivan Brown", "Qwerty123!"),
                Arguments.of("ivanov.gmail.com", "Roman Grey", "Qwerty123!"),
                Arguments.of("ivanov @gmail.com", "Roman Grey", "Qwerty123!"),
                Arguments.of("ivanov gmail.com", "Roman Grey", "Qwerty123!"),
                Arguments.of("ivanov@@gmail.com", "Roman Grey", "Qwerty123!"),
                Arguments.of("roman@grey@gmail.com", "Roman Grey", "Qwerty123!"),
                Arguments.of("123", "Roman Grey", "Qwerty123!"),
                Arguments.of("ivanov@gmail", "Sergiy Kriz", "Qwerty123!"),
                Arguments.of("ivanov@.com", "Sergiy Kriz", "Qwerty123!"),
                Arguments.of("ivanov#@gmail.com", "Sergiy Kriz", "Qwerty123!"),
                Arguments.of("ivanov.@gmail", "Sergiy Kriz", "Qwerty123!"),
                Arguments.of("іванов@gmail.com", "Sergiy Kriz", "Qwerty123!"),
                Arguments.of("ivanov!@gmail.com", "Sergiy Kriz", "Qwerty123!")
        );


    }

    @ParameterizedTest
    @DisplayName("Check registration not valid email")
    @MethodSource("invalidEmails")
    void testWithMethodSource(String email, String userName, String password) {
        registration(email, userName, password);
    }


    @ParameterizedTest(name = "Data from file- Email:{0}")
    @DisplayName("Check registration with not valed UserName field")
    @CsvFileSource(resources = "/notvalidregistration_data.csv", numLinesToSkip = 1)
    void testNotValidWithCsvFileSourceNotValidUserName(String email, String userName, String password) {
        String uniqueEmail = "from_file" + System.currentTimeMillis() + email;
        registration(uniqueEmail, userName, password);
    }


    @ParameterizedTest
    @DisplayName("Check registration with not valid password")
    @CsvSource({
            "file_csv_1@gmail.com,Olga,Qwerty123",
            "file_csv_2@gmail.com,Olga,Qwe123!",
            "file_csv_3@gmail.com,Olga,Qwerty",
            "file_csv_4@gmail.com,Olga,Qwertyuio!",
            "file_csv_5@gmail.com,Olga,12345",
            "file_csv_6@gmail.com,Olga,12345678",
            "file_csv_7@gmail.com,Olga,12345678!",
            "file_csv_7@gmail.com,Olga,!!!!!!!!",
            "file_csv_8@gmail.com,Olga,qwerty123!",
            "file_csv_9@gmail.com,Olga,qwertyuio",
            "file_csv_10@gmail.com,Olga,Qwerty 123!",
            "file_csv_11@gmail.com,Olga,QWERTY123!",
            "file_csv_12@gmail.com,Olga,Qwerty!!!",
            "file_csv_13@gmail.com,Olga,Qwerty123",
            "file_csv_14@gmail.com,Olga,' Qwerty123!'",
            "file_csv_15@gmail.com,Olga,' Qwerty123! '",
            "file_csv_16@gmail.com,Olga,'Qwerty123! '",
            "file_csv_17@gmail.com,Olga,Qwerty123!Qwerty123!Qwerty123!"
    })
    void testWithCsvSourceNotValidPassword(String email, String userName, String password) {
        registration(email, userName, password);
    }


    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}