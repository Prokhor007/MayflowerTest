import PageObjects.MainPage;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;


public class MayflowerTest {

    @BeforeAll
    public static void setUp() {
        Configuration.pageLoadStrategy = "eager";
        Configuration.remote = "http://127.0.0.1:4444/wd/hub";
        Configuration.browser = "chrome";
    }

    @BeforeEach
    public void openSite()
    {
        // При открытии страница в вечном лоадере, из-за этого не стартовали проверки теста.
        // Пришлось добавить данную настройку для старта как только загрузиться ДОМ дерево
        // Configuration.pageLoadStrategy = "eager";
        open("https://www.w3schools.com/sql/trysql.asp?filename=trysql_select_all");
    }

    // Во всех тестах закомменчен метод "clickCookieButton()"
    // Окно принятия кук отсутствует в зависимости от версии браузера.
    // Пришлось закомментить для запуска через селениум хаб.
    @Test
    @DisplayName("Вывод всех строк таблицы и проверка наличия заданного имени и адреса")
    public void checkNameAnaAddressInTable() {
        MainPage mainPage = new MainPage()
        //      .clickCookieButton()
                .getSqlStatementText()
                .clickRunSqlButton()
                .getInfoFromResultTable("Giovanni Rovelli","Via Ludovico il Moro 22");
    }

    @Test
    @DisplayName("Вывод строк таблицы где город Лондон. Проверка что таких записей 6")
    public void requestDataWithLondon() {
        MainPage mainPage = new MainPage()
        //      .clickCookieButton()
                .getSqlStatementText()
                .fillInputFieldWithJS("SELECT * FROM Customers WHERE city = 'London';")
                .clickRunSqlButton()
                .checkCountOfTable(6, "6");
    }

    @Test
    @DisplayName("Добавление новой записи в таблицу. Проверка что запись добавлена")
    public void addNewEntryInTable() {
        MainPage mainPage = new MainPage()
        //      .clickCookieButton()
                .getSqlStatementText()
                .fillInputFieldWithJS("INSERT INTO Customers (CustomerID,CustomerName,ContactName,Address,City,PostalCode,Country) " +
                        "VALUES ('123','TestCustomerName','TestContactName','TestAddress','TestCity','TestPostalCode','TestCountry')")
                .clickRunSqlButton();

        Selenide.sleep(1000); // Ожидание пока обновятся данные в БД

        mainPage.fillInputFieldWithJS("SELECT * FROM Customers WHERE CustomerID = 123;")
                .clickRunSqlButton()
                .getFullInfoFromResultTable("TestCustomerName","TestContactName","TestAddress",
                        "TestCity","TestPostalCode","TestCountry");
    }

    @Test
    @DisplayName("Изменение записи таблицы. Проверка что данные успешно обновлены")
    public void updateEntryInTable() {
        MainPage mainPage = new MainPage()
        //      .clickCookieButton()
                .getSqlStatementText()
                .fillInputFieldWithJS("UPDATE Customers SET " +
                        "CustomerName = 'NewCustomerName', " +
                        "ContactName = 'NewContactName', " +
                        "Address = 'NewAddress', " +
                        "City = 'NewCity', " +
                        "PostalCode = 'NewPostalCode', " +
                        "Country = 'NewCountry' " +
                        "WHERE CustomerID = 1;")
                .clickRunSqlButton();

        Selenide.sleep(1000); // Ожидание пока обновятся данные в БД

        mainPage.fillInputFieldWithJS("SELECT * FROM Customers WHERE CustomerID = 1")
                .clickRunSqlButton()
                .getFullInfoFromResultTable("NewCustomerName","NewContactName","NewAddress",
                        "NewCity","NewPostalCode","NewCountry");
    }

    @Test
    @DisplayName("Удаление записей где City = 'London'. Проверка что записи успешно удалены")
    public void deleteDataWithLondon() {
        MainPage mainPage = new MainPage()
        //      .clickCookieButton()
                .getSqlStatementText()
                .fillInputFieldWithJS("DELETE FROM Customers WHERE City = 'London';")
                .clickRunSqlButton();

        Selenide.sleep(1000); // Ожидание пока обновятся данные в БД

        mainPage.fillInputFieldWithJS("SELECT * FROM Customers WHERE city = 'London';")
                .clickRunSqlButton()
                .getInfoFromResultTableAfterDelete();
    }

    @AfterEach
    public void tearDown() {
        closeWindow();
    }
}
