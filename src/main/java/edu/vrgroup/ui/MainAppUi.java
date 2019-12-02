package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.communication.PushMode;
import edu.vrgroup.GameChangeNotifier;
import edu.vrgroup.model.Game;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.util.SecurityUtils;
import java.util.stream.Stream;

@Route("")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@Push(PushMode.AUTOMATIC)
@PWA(name = "Administrator dashboard", shortName = "Dashboard")
public class MainAppUi extends AppLayout {

  Select<Game> gameSelect = createGamesList();

  public MainAppUi() {
    gameSelect.addValueChangeListener(e -> {
      System.out.println("Value change" + e.getValue());
      Game game = e.getValue();
      if (game != null) {
        ((GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier")).setGame(game);
      }
    });
    addToNavbar(true, createNewGameButton(gameSelect));
    addToNavbar(true, gameSelect);
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

  private static Button createLogOutButton() {
    return new Button("Log out", e -> {
      SecurityUtils.setIsUserLoggedIn(false);
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
    games.setDataProvider(new AbstractBackEndDataProvider<>() {
      @Override
      protected Stream<Game> fetchFromBackEnd(Query<Game, Object> query) {
        return DaoProvider.getDao().getGames().stream();
      }

      @Override
      protected int sizeInBackEnd(Query<Game, Object> query) {
        return DaoProvider.getDao().getGamesCount();
      }
    });
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

    //return current game on refresh
    Game game = ((GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier")).getGame();
    if (game != null) {
      gameSelect.setValue(game);
    }
    System.out.println("attach to main " + attachEvent.getSession());
  }
}
