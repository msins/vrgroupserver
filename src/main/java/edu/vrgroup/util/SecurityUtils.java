package edu.vrgroup.util;


import com.vaadin.flow.server.VaadinSession;

public class SecurityUtils {

  public static boolean isUserLoggedIn() {
    if (VaadinSession.getCurrent() != null) {
      Object isLoggedIn = VaadinSession.getCurrent().getAttribute("user.logged.in");
      return isLoggedIn != null && ((boolean) isLoggedIn);
    }
    return false;
  }

  public static void setUserIsLoggedIn(boolean loggedIn) {
    VaadinSession.getCurrent().setAttribute("user.logged.in", loggedIn);
  }

}
