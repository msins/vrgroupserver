package edu.vrgroup.ui.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.ui.util.AbstractButtonFactory;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NewScenarioForm extends Dialog {

  public NewScenarioForm(Game game, BiConsumer<Game, Scenario> onAdd) {

    TextField scenarioField = new TextField();
    scenarioField.setPlaceholder("New Scenario...");

    Button createBtn = AbstractButtonFactory.getRectangle().createPrimaryButton("Create", e -> {
      if (scenarioField.getValue().trim().isEmpty()) {
        scenarioField.setErrorMessage("Enter scenario");
        scenarioField.setInvalid(true);
        return;
      }

      Scenario scenario = new Scenario(scenarioField.getValue());
      onAdd.accept(game, scenario);
      close();
    });

    createBtn.addClickShortcut(Key.ENTER);
    Button closeBtn = AbstractButtonFactory.getRectangle().createRedButton("Cancel", e -> close());

    add(new HorizontalLayout(scenarioField, createBtn, closeBtn));
  }
}
