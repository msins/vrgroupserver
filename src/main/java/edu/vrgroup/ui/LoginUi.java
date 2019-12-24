package edu.vrgroup.ui;

import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.vrgroup.DatabaseUtil;
import edu.vrgroup.util.SecurityUtils;

@Route("login")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@PageTitle("Login")
public class LoginUi extends HorizontalLayout implements BeforeEnterObserver {

  private LoginForm form = new LoginForm();

  public LoginUi() {
    form.setForgotPasswordButtonVisible(false);
    form.addLoginListener(e -> {
      if (authenticate(e)) {
        SecurityUtils.setUserIsLoggedIn(true);
        this.getUI().ifPresent(ui -> ui.navigate(DashboardUi.class));
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
    try {
      DatabaseUtil.init(e.getUsername(), e.getPassword());
    } catch (Exception wrongPassword) {
      return false;
    }
    return true;
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.forwardTo(DashboardUi.class);
    if (SecurityUtils.isUserLoggedIn()) {
      event.forwardTo(DashboardUi.class);
    }
  }
}
