package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuiteProfileEditing {
    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeClass
    public static void setUpClass()
    {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.navigate().to("http://31.40.251.201/login");
        wait = new WebDriverWait(driver, 5);
    }

    @AfterClass
    public static void tearDownClass()
    {
        driver.findElement(By.xpath("(//*[@class='main-layout__link'])[4]"));
        driver.quit();
    }

    @After
    public void tearDown()
    {
        driver.navigate().to("http://31.40.251.201/login");
    }

    private By emailField = By.id("login-email");
    private By passwordField = By.id("login-password");
    private By loginButton = By.cssSelector(".btn--white");
    private By logo = By.cssSelector(".main-layout__logo");
    private By userName = By.cssSelector(".main-layout__user-name");
    private By profileName = By.cssSelector(".profile-info__name");
    private By editProfile = By.cssSelector(".edit");
    private By titleMain = By.xpath("(//*[contains(@class, 'aside-filter__item')])[1]");
    private By fieldName = By.xpath("(//*[@class='user-info-form__input'])[1]");
    private By fieldSurname = By.xpath("(//*[@class='user-info-form__input'])[2]");
    private By fieldPhoneNumber = By.xpath("(//*[@class='user-info-form__input'])[3]");
    private By fieldCountry = By.xpath("(//*[contains(@class, 'user-info-form__input')])[4]");
    private By fieldCity = By.xpath("(//*[contains(@class, 'user-info-form__input')])[5]");
    private By fieldDay = By.cssSelector(".day");
    private By fieldMonth = By.cssSelector(".month");
    private By fieldYear = By.cssSelector(".year");
    private By fieldText = By.cssSelector(".user-info-form__input--textarea");
    private By buttonSave = By.xpath("(//*[@class='btn'])");
    private By titleDateOfBirth = By.xpath("(//*[@class='profile-info__val'])[1]");
    private By titlePhone = By.xpath("(//*[@class='profile-info__val'])[2]");
    private By titleCountyAndCity = By.xpath("(//*[@class='profile-info__val'])[3]");
    private By titleAboutMe = By.xpath("(//*[@class='profile-info__val'])[4]");

    @Test
    public void fillingAProfile()
    {
        //arrange
        var email = "zerone115@mail.ru";
        var password = "Zerone115";
        var phoneNumber = "9991234567";
        var text = "?????????? ?????? ???????????????????? ???????? ?? ????????";
        var expectedResultDateOfBirth = "25 ?????? 1975 (46 ??????)";
        var expectedResultPhoneNumber = "+7 (999) 123-45-67";
        var expectedResultCountryAndCity = "????????????, ??????????????????";
        var expectedResultText = "?????????? ?????? ???????????????????? ???????? ?? ????????";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
        driver.findElement(userName).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));
        driver.findElement(editProfile).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(titleMain));
        driver.findElement(fieldPhoneNumber).sendKeys(phoneNumber);
        Select country = new Select(driver.findElement(fieldCountry));
        country.selectByIndex(0); //index 0 = ????????????, index 1 = USA
        Select city = new Select(driver.findElement(fieldCity));
        city.selectByIndex(14);
        /* ???????????? ????????????
        index 0 = ????????????, , index 1 = ??????????-??????????????, index 2 = ??????????????, index 3 = ??????????, index 4 = ????????????????,
        index 5 = ??????????????????????, index 6 = ??????????????, index 7 = ???????????????????????? (???????????????? ??????????????),
        index 8 = ?????????????????? (???????????????? ??????????????), index 9 = ??????????????????, index 10 = ??????????????????????, index 11 = ????????????????????????,
        index 12 = ????????????, index 13 = ??????????????????, index 14 = ??????????????????, index 15 = ????????????????, index 16 = ??????,
        index 17 = ??????????????????????, index 18 = ??????????????, index 19 = ????????????????????, index 20 = ,
        index 21 = , index 22 = , index 23 = , index 24 = ,
        index 25 = , index 26 = , index 27 = , index 28 = ,
        index 29= , index 30 = , index 31 = , index 32= ,

        ???????????? ??????
        to do
        */
        Select day = new Select(driver.findElement(fieldDay));
        day.selectByIndex(24); //index 0 = 1, index 1 = 2 ... index 30 = 31
        Select month = new Select(driver.findElement(fieldMonth));
        month.selectByIndex(4); //index 0 = ????????????, index 1 = ?????????????? ... index 11 = ??????????????,
        Select year = new Select(driver.findElement(fieldYear));
        year.selectByIndex(5); //index 0 = 1970, index 1 = 1971 ... index 51 = 2021,
        driver.findElement(fieldText).sendKeys(Keys.CONTROL + "a");
        driver.findElement(fieldText).sendKeys(Keys.DELETE);
        driver.findElement(fieldText).sendKeys(text);
        driver.findElement(buttonSave).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));

        //assert
        var actualResultDateOfBirth = driver.findElement(titleDateOfBirth).getText();
        Assert.assertEquals("???????????????? ???????? ????????????????", expectedResultDateOfBirth, actualResultDateOfBirth);
        var actualResultPhoneNumber = driver.findElement(titlePhone).getText();
        Assert.assertEquals("???????????????? ?????????? ????????????????", expectedResultPhoneNumber, actualResultPhoneNumber);
        var actualResultCountryAndCity = driver.findElement(titleCountyAndCity).getText();
        Assert.assertEquals("???????????????? ???????????? ?? ??????????", expectedResultCountryAndCity, actualResultCountryAndCity);
        var actualResultText = driver.findElement(titleAboutMe).getText();
        Assert.assertEquals("???????????????? ???????????????? ?? ????????", expectedResultText, actualResultText);
    }

    @Test
    public void name??hange()
    {
        //arrange
        var email = "zerone115@mail.ru";
        var password = "Zerone115";
        var name = "????????????";
        var expectedResult = "???????????? ??????????????????????";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
        driver.findElement(userName).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));
        driver.findElement(editProfile).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(titleMain));
        driver.findElement(fieldName).sendKeys(Keys.CONTROL + "a");
        driver.findElement(fieldName).sendKeys(Keys.DELETE);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(buttonSave).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));

        //assert
        var actualResult = driver.findElement(profileName).getText();
        Assert.assertEquals("?????? ???? ????????????????????", expectedResult, actualResult);
    }

    @Test
    public void surname??hange()
    {
        //arrange
        var email = "zerone115@mail.ru";
        var password = "Zerone115";
        var surname = "??????????";
        var expectedResult = "???????????? ??????????";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
        driver.findElement(userName).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));
        driver.findElement(editProfile).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(titleMain));
        driver.findElement(fieldSurname).sendKeys(Keys.CONTROL + "a");
        driver.findElement(fieldSurname).sendKeys(Keys.DELETE);
        driver.findElement(fieldSurname).sendKeys(surname);
        driver.findElement(buttonSave).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));

        //assert
        var actualResult = driver.findElement(profileName).getText();
        Assert.assertEquals("?????????????? ???? ????????????????????", expectedResult, actualResult);
    }

    @Test
    public void phoneNumberChange()
    {
        //arrange
        var email = "zerone115@mail.ru";
        var password = "Zerone115";
        var phoneNumber = "1234567890";
        var expectedResult = "+7 (123) 456-78-90";

        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
        driver.findElement(userName).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));
        driver.findElement(editProfile).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(titleMain));
        driver.findElement(fieldPhoneNumber).sendKeys(Keys.CONTROL + "a");
        driver.findElement(fieldPhoneNumber).sendKeys(Keys.DELETE);
        driver.findElement(fieldPhoneNumber).sendKeys(phoneNumber);
        driver.findElement(buttonSave).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));

        //assert
        var actualResult = driver.findElement(titlePhone).getText();
        Assert.assertEquals("?????????? ???????????????? ???? ??????????????????", expectedResult, actualResult);
    }

    @Test
    public void cityChange()
    {
        //arrange
        var email = "zerone118@mail.ru";
        var password = "Zerone118";
        var expectedResult = "????????????, ??????????";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
        driver.findElement(userName).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));
        driver.findElement(editProfile).click();
        Select city = new Select(driver.findElement(fieldCity));
        city.selectByIndex(0);
        /* ???????????? ????????????
        index 0 = ??????????, , index 1 = ????????????, index 2 = ????????????????, index 3 = ????????????, index 4 = ??????????????,
        index 5 = ??????????, index 6 = ????????????????, index 7 = ??????????????????,
        index 8 = ????????, index 9 = ????-??????????????, index 10 = ??????????, index 11 = ????????????,
        index 12 = ??????????????????, index 13 = ??????????????, index 14 = ??????????, index 15 = ????????????, index 16 = ??????????????????????,
        index 17 = ??????????????????????????, index 18 = ??????????????????????????-??????????????????????, index 19 = ????????????????????, index 20 = ,
        index 21 = , index 22 = , index 23 = , index 24 = ,
        index 25 = , index 26 = , index 27 = , index 28 = ,
        index 29= , index 30 = , index 31 = , index 32= ,

        */
        driver.findElement(buttonSave).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));

        //assert
        var actualResult = driver.findElement(titleCountyAndCity).getText();
        Assert.assertEquals("?????????? ???? ??????????????????", expectedResult, actualResult);
    }

    @Test
    public void CountryAndCityChange()
    {
        //arrange
        var email = "zerone118@mail.ru";
        var password = "Zerone118";
        var expectedResult = "USA, Chicago";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
        driver.findElement(userName).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));
        driver.findElement(editProfile).click();
        Select country = new Select(driver.findElement(fieldCountry));
        country.selectByIndex(1); //index 0 = ????????????, index 1 = USA
        Select city = new Select(driver.findElement(fieldCity));
        city.selectByIndex(1);
        /* ???????????? ????????????
        index 0 = ??????????, , index 1 = ????????????, index 2 = ????????????????, index 3 = ????????????, index 4 = ??????????????,
        index 5 = ??????????, index 6 = ????????????????, index 7 = ??????????????????,
        index 8 = ????????, index 9 = ????-??????????????, index 10 = ??????????, index 11 = ????????????,
        index 12 = ??????????????????, index 13 = ??????????????, index 14 = ??????????, index 15 = ????????????, index 16 = ??????????????????????,
        index 17 = ??????????????????????????, index 18 = ??????????????????????????-??????????????????????, index 19 = ????????????????????, index 20 = ,
        index 21 = , index 22 = , index 23 = , index 24 = ,
        index 25 = , index 26 = , index 27 = , index 28 = ,
        index 29= , index 30 = , index 31 = , index 32= ,

        ???????????? ??????
        index 0 = Boston, , index 1 = Chicago, index 2 = Minnesota, index 3 = New-York, index 4 = Utah,
        index 5 = Washington
        */
        driver.findElement(buttonSave).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));

        //assert
        var actualResult = driver.findElement(titleCountyAndCity).getText();
        Assert.assertEquals("?????????? ???? ??????????????????", expectedResult, actualResult);
    }

    @Test
    public void aboutMeChange()
    {
        //arrange
        var email = "zerone118@mail.ru";
        var password = "Zerone118";
        var text = "?????????? ?????????? ?????? ???????????????????? ???????? ?? ????????";
        var expectedResult = "?????????? ?????????? ?????? ???????????????????? ???????? ?? ????????";

        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
        driver.findElement(userName).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));
        driver.findElement(editProfile).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(titleMain));
        driver.findElement(fieldText).sendKeys(Keys.CONTROL + "a");
        driver.findElement(fieldText).sendKeys(Keys.DELETE);
        driver.findElement(fieldText).sendKeys(text);
        driver.findElement(buttonSave).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileName));

        //assert
        var actualResult = driver.findElement(titleAboutMe).getText();
        Assert.assertEquals("???????????????? ???????????????? ?? ????????", expectedResult, actualResult);
    }
}