package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.MoneyTransferPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class MoneyTransferTest {

    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    DataHelper.FirstCardInfo firstCardInfo = DataHelper.getFirstCardInfoFor(authInfo);
    DataHelper.SecondCardInfo secondCardInfo = DataHelper.getSecondCardInfoFor(authInfo);

    public void equalizeBalance() {

        DashboardPage dashboardPage = new DashboardPage();
        int firstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getFirstCardId());
        int secondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getSecondCardId());
        int equalizeAmount = (firstCardBalance + secondCardBalance) / 2;


        if (firstCardBalance > equalizeAmount) {
            dashboardPage.transfer(secondCardInfo.getSecondCardId())
                    .moneyTransfer(firstCardBalance - equalizeAmount, firstCardInfo.getFirstCardNumber());
        }
        if (secondCardBalance > equalizeAmount) {
            dashboardPage.transfer(firstCardInfo.getFirstCardId())
                    .moneyTransfer(secondCardBalance - equalizeAmount, secondCardInfo.getSecondCardNumber());
        } return;
    }

    @BeforeEach
    void setupTest() {
        open("http://localhost:9999");
        new LoginPage()
                .validLogin(authInfo)
                .validVerify(verificationCode);
        equalizeBalance();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsSecondToFirstCard() {
        int amount = 200;
        DashboardPage dashboardPage = new DashboardPage();
        var balanceBeforeFirstCard = dashboardPage.getCardBalance(firstCardInfo.getFirstCardId());
        var balanceBeforeSecondCard = dashboardPage.getCardBalance(secondCardInfo.getSecondCardId());

        dashboardPage.transfer(firstCardInfo.getFirstCardId())
                .moneyTransfer(amount, secondCardInfo.getSecondCardNumber());

        int expectedBalanceFirstCard = balanceBeforeFirstCard + amount;
        int expectedBalanceSecondCard = balanceBeforeSecondCard - amount;
        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo.getFirstCardId());
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo.getSecondCardId());

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsFirstToSecondCard() {
        int amount = 200;
        DashboardPage dashboardPage = new DashboardPage();
        var balanceBeforeFirstCard = dashboardPage.getCardBalance(firstCardInfo.getFirstCardId());
        var balanceBeforeSecondCard = dashboardPage.getCardBalance(secondCardInfo.getSecondCardId());

        dashboardPage.transfer(secondCardInfo.getSecondCardId())
                .moneyTransfer(amount, firstCardInfo.getFirstCardNumber());

        int expectedBalanceFirstCard = balanceBeforeFirstCard - amount;
        int expectedBalanceSecondCard = balanceBeforeSecondCard + amount;
        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo.getFirstCardId());
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo.getSecondCardId());

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }
    @Test
    void shouldErrorNotEnoughMoneyFirstToSecondCard() {
        int amount = 10200;
        DashboardPage dashboardPage = new DashboardPage();

        dashboardPage.transfer(firstCardInfo.getFirstCardId())
                .moneyTransferError(amount, secondCardInfo.getSecondCardNumber());
    }

    @Test
    void shouldErrorNotEnoughMoneySecondToFirstCard() {
        int amount = 10200;
        DashboardPage dashboardPage = new DashboardPage();

        dashboardPage.transfer(secondCardInfo.getSecondCardId())
                .moneyTransferError(amount, firstCardInfo.getFirstCardNumber());
    }
}
