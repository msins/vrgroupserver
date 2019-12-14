package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.communication.PushMode;
import edu.vrgroup.GameChangeListener;
import edu.vrgroup.GameChangeNotifier;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.ui.providers.GameDataProvider;
import edu.vrgroup.util.SecurityUtils;

@Route("")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@Push(PushMode.AUTOMATIC)
@PreserveOnRefresh
@PWA(name = "Administrator dashboard", shortName = "Dashboard")
public class MainAppUi extends AppLayout implements GameChangeListener {

  Select<Game> gameSelect = createGamesList();

  public MainAppUi() {
    gameSelect.addValueChangeListener(e -> {
      Game game = e.getValue();
      if (game != null) {
        GameChangeNotifier notifier = ((GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier"));
        notifier.setGame(game);
      }
    });
    addToNavbar(true, createNewGameButton(gameSelect));
    addToNavbar(true, gameSelect);
    addToNavbar(true, createRemoveGameButton(gameSelect));
    addToNavbar(true, createMenuLayout());
    addToNavbar(true, createLogOutButton());
  }

  private static FlexLayout createMenuLayout() {
    FlexLayout menuLayout = new FlexLayout() {{
      setSizeFull();
      setJustifyContentMode(JustifyContentMode.CENTER);
    }};

    Tabs menu = createMenuTabs();
    menuLayout.add(menu);
    return menuLayout;
  }

  private static Tabs createMenuTabs() {
    final Tabs tabs = new Tabs();
    tabs.getStyle().set("margin", "0 auto");
    tabs.setOrientation(Orientation.HORIZONTAL);
    tabs.add(createTab(VaadinIcon.DASHBOARD, "Dashboard", DashboardUi.class));
    tabs.add(createTab(VaadinIcon.QUESTION_CIRCLE, "Questions", QuestionsUi.class));
    return tabs;
  }

  private static Button createNewGameButton(Select<Game> select) {
    Button newGame = new Button(VaadinIcon.PLUS.create(), e -> new NewGameForm(select).open());
    newGame.getStyle().set("border-radius", "50%");
    return newGame;
  }

  private static Button createRemoveGameButton(Select<Game> select) {
    Button removeGame = new Button(VaadinIcon.MINUS.create(), e -> {

      if (select.getValue() != null) {
        DaoProvider.getDao().removeGame(select.getValue());
        select.getDataProvider().refreshAll();
      }
    });
    removeGame.getStyle().set("border-radius", "50%");
    removeGame.getStyle().set("color", "red");
    return removeGame;
  }

  private static Button createLogOutButton() {
    return new Button("Log out", e -> {
      SecurityUtils.setUserIsLoggedIn(false);
      UI.getCurrent().navigate("login");
    }) {{
      setThemeName("primary");
      getElement().getStyle().set("margin", "auto");
      getElement().getStyle().set("margin-right", "10px");
    }};
  }

  private static Select<Game> createGamesList() {
    Select<Game> games = new Select<>();
    games.setPlaceholder("Select a game");
    games.setDataProvider(new GameDataProvider());
    return games;
  }

  private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> view) {
    return createTab(populateLink(new RouterLink(null, view), icon, title));
  }

  private static Tab createTab(Component content) {
    Tab tab = new Tab();
    tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    tab.add(content);
    return tab;
  }

  private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
    a.add(icon.create());
    a.add(title);
    return a;
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    if (!SecurityUtils.isUserLoggedIn()) {
      getUI().ifPresent(ui -> ui.navigate("login"));
    }
    registerToGameNotifier();
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
    unregisterFromGameNotifier();
  }

  @Override
  public void gameChanged(Game game) {
    gameSelect.setValue(game);
  }
}
