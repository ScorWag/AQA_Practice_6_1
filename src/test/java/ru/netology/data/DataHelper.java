package ru.netology.data;

import lombok.Value;
import ru.netology.page.DashboardPage;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    @Value
    public static class Cards {
        private String firstCardNumber;
        private String secondCardNumber;
        private String firstCardId;
        private String secondCardId;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static Cards getCardNumbersFor(AuthInfo info) {
        return new Cards("5559 0000 0000 0001", "5559 0000 0000 0002",
                DashboardPage.extractCardId(1), DashboardPage.extractCardId(2));
    }

    public static AuthInfo getUnAuthInfo(AuthInfo original) {
        return original;
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo info) {
        return new VerificationCode("12345");
    }
}
