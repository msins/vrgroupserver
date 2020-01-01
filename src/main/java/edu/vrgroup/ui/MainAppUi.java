package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
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
import edu.vrgroup.ScenarioChangeListener;
import edu.vrgroup.ScenarioChangeNotifier;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.ui.forms.NewScenarioForm;
import edu.vrgroup.ui.util.DynamicSelect;
import edu.vrgroup.ui.util.DynamicSelect.DynamicSelectListener;
import edu.vrgroup.ui.forms.NewGameForm;
import edu.vrgroup.ui.providers.GameDataProvider;
import edu.vrgroup.ui.providers.ScenarioDataProvider;
import edu.vrgroup.ui.util.AbstractButtonFactory;
import edu.vrgroup.util.SecurityUtils;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Route("")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@Push(PushMode.AUTOMATIC)
@PreserveOnRefresh
@PWA(name = "Administrator dashboard", shortName = "Dashboard")
public class MainAppUi extends AppLayout implements GameChangeListener, ScenarioChangeListener {

  DynamicSelect<Game> gameSelect;
  DynamicSelect<Scenario> scenarioSelect;

  public MainAppUi() {
    scenarioSelect = new DynamicSelect<>(new ScenarioDynamicSelectListener());
    scenarioSelect.setPlaceholder("Select scenario");

    gameSelect = new DynamicSelect<>(new GameSelectListener());
    gameSelect.setPlaceholder("Select game");
    gameSelect.getSelect().setDataProvider(new GameDataProvider());

    addToNavbar(new DrawerToggle());
    addToDrawer(gameSelect);
    addToDrawer(scenarioSelect);
    addToNavbar(false, createMenuLayout());
    addToNavbar(false, createLogOutButton());
  }

  private FlexLayout createMenuLayout() {
    FlexLayout menuLayout = new FlexLayout();
    menuLayout.setWidthFull();
    menuLayout.setJustifyContentMode(JustifyContentMode.CENTER);

    Tabs menu = createMenuTabs();
    menuLayout.add(menu);
    return menuLayout;
  }

  private static Tabs createMenuTabs() {
    final Tabs tabs = new Tabs();
    tabs.setOrientation(Orientation.HORIZONTAL);
    tabs.add(createTab(VaadinIcon.DASHBOARD, "Dashboard", DashboardUi.class));
    tabs.add(createTab(VaadinIcon.QUESTION_CIRCLE, "Questions", QuestionsUi.class));
    tabs.add();
    return tabs;
  }

  private static Button createLogOutButton() {
    Button logOut = AbstractButtonFactory.getRectangle().createPrimaryButton("Log out", e -> {
      SecurityUtils.setUserIsLoggedIn(false);
      UI.getCurrent().navigate("login");
    });
    logOut.getElement().getStyle().set("margin-right", "10px");
    return logOut;
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
    unregisterFromGameNotifier();
    unregisterFromScenarioNotifier(gameSelect.getValue());
  }

  @Override
  public void gameChanged(Game game) {
    if (game == null) {
      return;
    }
    scenarioSelect.getSelect().setDataProvider(new ScenarioDataProvider(game));
    System.out.println("[Main] New game:" + game);
    registerToScenarioNotifier(game);
  }

  @Override
  public void scenarioChanged(Scenario scenario) {
    System.out.println("[Main] New scenario:" + scenario);
  }

  /**
   * Lister for changes in scenario select ui element.
   */
  private class ScenarioDynamicSelectListener implements DynamicSelectListener<Scenario> {

    @Override
    public void onRemoveClicked(Scenario scenario) {
      if (scenarioSelect.getSelect().getValue() == null) {
        new Notification("No scenario selected", 3000).open();
        return;
      }

      if (scenario.getName().equalsIgnoreCase("default")) {
        new Notification("Can't remove default scenario", 3000).open();
        return;
      }

      DaoProvider.getDao().removeScenario(scenario);
      scenarioSelect.refreshAll();
      getCurrentNotifier(gameSelect.getValue()).setScenario(null);
    }

    @Override
    public void onSelectionChange(Scenario scenario) {
      if (scenario != null) {
        getCurrentNotifier(getCurrentGame()).setScenario(scenario);
      }
    }

    @Override
    public void onAddClicked(Scenario oldScenario) {
      if (gameSelect.getValue() == null) {
        new Notification("No game selected", 3000).open();
        return;
      }

      BiConsumer<Game, Scenario> onNewScenario = (newGame, newScenario) -> {
        if (newScenario.getName().equalsIgnoreCase("Default")) {
          new Notification("Can't create default", 3000).open();
          return;
        }
        DaoProvider.getDao().addScenario(newGame, newScenario);
        scenarioSelect.refreshAll();
        scenarioSelect.setValue(newScenario);
      };
      new NewScenarioForm(gameSelect.getValue(), onNewScenario).open();
    }

    private Game getCurrentGame() {
      var gameNotifier = ((GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier"));
      return gameNotifier.getGame();
    }

    private ScenarioChangeNotifier getCurrentNotifier(Game game) {
      return ((ScenarioChangeNotifier) VaadinSession.getCurrent().getAttribute(game.getName() + ".scenario.notifier"));
    }
  }

  /**
   * Lister for changes in game select ui element.
   */
  private class GameSelectListener implements DynamicSelectListener<Game> {

    @Override
    public void onRemoveClicked(Game oldGame) {
      if (gameSelect.getValue() == null) {
        new Notification("No game selected", 3000).open();
        return;
      }

      unregisterFromScenarioNotifier(oldGame);
      DaoProvider.getDao().removeGame(gameSelect.getValue());
      gameSelect.refreshAll();
      scenarioSelect.refreshAll();
      getCurrentNotifier().setGame(null);

    }

    @Override
    public void onSelectionChange(Game game) {
      if (game != null) {
        GameChangeNotifier notifier = ((GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier"));
        notifier.setGame(game);
      }
    }

    @Override
    public void onAddClicked(Game oldGame) {

      Consumer<Game> newGameCreated = newGame -> {
        DaoProvider.getDao().addGame(newGame);
        gameSelect.refreshAll();
        gameSelect.setValue(newGame);

        scenarioSelect.getSelect().setDataProvider(new ScenarioDataProvider(newGame));
        scenarioSelect.refreshAll();

        //todo set for default scenario
      };
      new NewGameForm(newGameCreated).open();
    }

    private GameChangeNotifier getCurrentNotifier() {
      return ((GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier"));
    }

  }
}
