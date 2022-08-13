package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private static ElementsCollection cards = $$(".list div[data-test-id").filter(visible);
    private final static String balanceStart = "баланс: ";
    private final static String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public static String extractCardId(int number) {
        String id = cards.get(number - 1).attr("data-test-id");
        return id;
    }

    public static int getCardBalance(String id) {
        String text = cards.findBy(attribute("data-test-id", id)).text();
        return extractBalance(text);
    }

    private static int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public static MoneyTransferPage transfer(String id) {
        SelenideElement card = cards.findBy(attribute("data-test-id", id));
        card.$("button[data-test-id=action-deposit]").click();

        return new MoneyTransferPage();
    }

    public static SelenideElement cardBalance(String id) {
        SelenideElement element = cards.findBy(attribute("data-test-id", id));
        return element;
    }

    public static void equalizeBalance(DataHelper.AuthInfo info) {
        String firstCardId = extractCardId(1);
        String secondCardId = extractCardId(2);
        String firstCardNumber = DataHelper.getCardNumbersFor(info).getFirstCardNumber();
        String secondCardNumber = DataHelper.getCardNumbersFor(info).getSecondCardNumber();
        int firstCardBalance = getCardBalance(firstCardId);
        int secondCardBalance = getCardBalance(secondCardId);
        int equalizeAmount = (firstCardBalance + secondCardBalance) / 2;

        if (firstCardBalance > equalizeAmount) {
            transfer(secondCardId);
            MoneyTransferPage.moneyTransfer(firstCardBalance - equalizeAmount,
                    firstCardNumber);
        }
        if (secondCardBalance > equalizeAmount) {
            transfer(firstCardId);
            MoneyTransferPage.moneyTransfer(secondCardBalance - equalizeAmount,
                    secondCardNumber);
        }
        return;
    }
}
