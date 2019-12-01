package edu.vrgroup.ui;

import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.vrgroup.util.SecurityUtils;

@Route("login")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@PageTitle("dashboard-view")
public class LoginUi extends HorizontalLayout implements AfterNavigationObserver,
    BeforeEnterObserver {

  private LoginForm form = new LoginForm();

  public LoginUi() {
    SecurityUtils.setIsUserLoggedIn(true);
    this.getUI().ifPresent(ui -> ui.navigate(DashboardUi.class));

    form.setForgotPasswordButtonVisible(false);
    form.addLoginListener(e -> {
      boolean isAuthenticated = authenticate(e);
      if (isAuthenticated) {
        SecurityUtils.setIsUserLoggedIn(true);
        this.getUI().ifPresent(ui -> ui.navigate(MainAppUi.class));
      } else {
        form.setError(true);
      }
    });
    form.setVisible(true);
    add(form);
    form.getElement().getStyle().set("margin", "auto");
    setHeightFull();
  }

  private boolean authenticate(LoginEvent e) {
    return e.getPassword().equals("root") && e.getUsername().equals("root");
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    form.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (SecurityUtils.isUserLoggedIn()) {
      event.forwardTo(DashboardUi.class);
    }
  }
}
