package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    private static SelenideElement amountField = $("[data-test-id=amount] input");
    private static SelenideElement fromCardField = $("[data-test-id=from] input");

    public MoneyTransferPage() {
        amountField.shouldBe(visible);
        fromCardField.shouldBe(visible);
    }

    public static DashboardPage moneyTransfer(int amount, String from) {
        amountField.setValue(Integer.toString(amount));
        fromCardField.setValue(from);
        $("[data-test-id=action-transfer]").click();

        return new DashboardPage();
    }

//    public DashboardPage moneyTransferError(int amount, String from) {
//        amountField.setValue(Integer.toString(amount));
//        fromCardField.setValue(from);
//        $("[data-test-id=action-transfer]").click();
//        $()
//
//        return new DashboardPage();
//    }
}
