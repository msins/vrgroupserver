package edu.vrgroup.ui.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import edu.vrgroup.model.Game;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.ui.util.AbstractButtonFactory;
import java.util.function.Consumer;

public class NewGameForm extends Dialog {

  public NewGameForm(Consumer<Game> onAdd) {
    TextField gameField = new TextField();
    gameField.setPlaceholder("New game...");

    Button createBtn = AbstractButtonFactory.getRectangle().createPrimaryButton("Create", e -> {
      if (gameField.getValue().trim().isEmpty()) {
        gameField.setErrorMessage("Enter game");
        gameField.setInvalid(true);
        return;
      }

      Game game = new Game(gameField.getValue());
      onAdd.accept(game);
      close();
    });

    createBtn.addClickShortcut(Key.ENTER);
    Button closeBtn = AbstractButtonFactory.getRectangle().createRedButton("Cancel", e -> close());

    add(new HorizontalLayout(gameField, createBtn, closeBtn));
  }
}
