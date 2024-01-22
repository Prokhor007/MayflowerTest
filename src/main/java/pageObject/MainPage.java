package PageObjects;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage {

    /** Кнопка принятия куки **/
    private final SelenideElement cockieButton = $(By.id("accept-choices"));

    /** Заголовок поля ввода 'SQL Statement' **/
    private final SelenideElement sqlStatementText = $(By.xpath(".//h3[contains(text(),'SQL Statement')]"));

    /** Кнопка запуска скрипта **/
    private final SelenideElement runSqlButton = $(By.xpath(".//button[@class='ws-btn' and contains(text(),'Run SQL')]"));

    /** Таблица результатов **/
    private final SelenideElement resultTable = $(By.xpath(".//table[contains(@class,'ws-table-all')]"));

    /** Количество результатов **/
    private final SelenideElement nubmerOfRecordsText = $(By.xpath(".//div[contains(text(), 'Number of Records')]"));

    /** Таблица результатов **/
    private final SelenideElement resultAfterDelete = $(By.id("divResultSQL"));


    @Step("Нажимаем кнопку принятия куки")
    public MainPage clickCookieButton() {
        cockieButton.click();
        return this;
    }

    @Step("Проверка наличия заголовка 'SQL Statement'")
    public MainPage getSqlStatementText() {
        sqlStatementText.shouldBe(visible);
        String text = sqlStatementText.getText();
        assertTrue(text.contains("SQL Statement"), "Текст не содержит 'SQL Statement:'");
        return this;
    }

    @Step("Нажимаем кнопку 'Run SQL'")
    public MainPage clickRunSqlButton() {
        runSqlButton.click();
        return this;
    }

    @Step("Получение имени и адреса из таблицы результатов")
    public MainPage getInfoFromResultTable(String contactName, String address) {
        // switchTo().frame($("#iframeResultSQL"));
        String tableText = resultTable.getText();
        assertTrue(tableText.contains(contactName), "Имя не найдено в таблице");
        assertTrue(tableText.contains(address), "Адрес не найден в таблице");
        // switchTo().defaultContent();
        return this;
    }

    @Step("Заполнить поле ввода скрипта")
    public MainPage fillInputFieldWithJS(String text) {
        String script = "window.editor.getDoc().setValue(arguments[0]);";
        Selenide.executeJavaScript(script, text);
        return this;
    }

    // Далее в тестах использовалась конструкция для работы с элементами в iframe.
    // Элементы внутри iframe находятся только в последних версиях Google Chrome.
    // Последние версии Хрома не поддерживают WebSQL из-за чего часть тестов невозможно было пройти.
    // Пришлось откатиться на старую версию 116 для прохождения тестов.
    // В старых версиях элементы уже находятся вне iframe.

    @Step("Получение количества записей таблицы")
    public MainPage checkCountOfTable(int expectedCount, String nubmerOfRecords) {
        // switchTo().frame($("#iframeResultSQL"));
        resultTable.should(exist.because("Таблица не найдена"));
        int actualCount = resultTable.$$("tr").size();
        assertEquals(expectedCount + 1, actualCount, "Количество записей в таблице не совпадает");
        assertTrue(nubmerOfRecordsText.getText().contains(nubmerOfRecords), "Количество записей в поле 'Number of Records' не соответсвует заданному значению");
        // switchTo().defaultContent();
        return this;
    }

    @Step("Получение всех полей из таблицы результатов")
    public MainPage getFullInfoFromResultTable(String customerName, String contactName, String address,
                                               String city, String postalCode, String country) {
        // switchTo().frame($("#iframeResultSQL"));
        String tableText = resultTable.getText();
        assertTrue(tableText.contains(customerName), "Клиент не найден в таблице");
        assertTrue(tableText.contains(contactName), "Имя не найдено в таблице");
        assertTrue(tableText.contains(address), "Адрес не найден в таблице");
        assertTrue(tableText.contains(city), "Город не найден в таблице");
        assertTrue(tableText.contains(postalCode), "Индекс не найден в таблице");
        assertTrue(tableText.contains(country), "Страна не найдена в таблице");
        // switchTo().defaultContent();
        return this;
    }

    @Step("Проверка отсутствия записей со значение city = 'London' в таблице результатов")
    public MainPage getInfoFromResultTableAfterDelete() {
        // //switchTo().frame($("#iframeResultSQL"));
        String tableText = resultAfterDelete.getText();
        assertTrue(tableText.contains("No result"), "В таблице есть результаты с city = 'London'");
        //switchTo().defaultContent();
        return this;
    }
}
