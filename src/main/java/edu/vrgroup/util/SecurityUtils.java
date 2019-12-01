package edu.vrgroup.util;

public class SecurityUtils {

  private static String username;
  private static String password;
  private static boolean isUserLoggedIn;

  public static boolean isUserLoggedIn() {
    return isUserLoggedIn;
  }

  public static void setIsUserLoggedIn(boolean isUserLoggedIn) {
    SecurityUtils.isUserLoggedIn = isUserLoggedIn;
  }

  public static String getUsername() {
    return username;
  }

  public static void setUsername(String username) {
    SecurityUtils.username = username;
  }

  public static String getPassword() {
    return password;
  }

  public static void setPassword(String password) {
    SecurityUtils.password = password;
  }
}
