package com.ejada.meetingroomreservation.auth.config;


import com.ejada.meetingroomreservation.auth.model.LoggedInUser;

public class ThreadLocalContext {
    private static ThreadLocal<LoggedInUser> threadLocal = new ThreadLocal<LoggedInUser>();
    public static void setUserInfo(LoggedInUser loggedInUser) {
        clearUserInfo();
        threadLocal.set(loggedInUser);
    }

    public static LoggedInUser getUserInfo() {
        return threadLocal.get();
    }

    public static void clearUserInfo() {
        threadLocal.remove();
    }
}
