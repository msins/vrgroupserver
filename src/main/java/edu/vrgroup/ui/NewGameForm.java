package edu.vrgroup.ui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import edu.vrgroup.model.Game;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.ui.util.ButtonFactory;

public class NewGameForm extends Dialog {

  public NewGameForm(Select<Game> select) {
    TextField gameField = new TextField();
    gameField.setPlaceholder("New game...");

    Button createBtn = ButtonFactory.createPrimaryButton("Create", e -> {
      if (gameField.getValue().trim().isEmpty()) {
        gameField.setErrorMessage("Enter game");
        gameField.setInvalid(true);
        return;
      }

      Game game = new Game(gameField.getValue());
      DaoProvider.getDao().addGame(game);
      select.getDataProvider().refreshAll();
      select.setValue(game);
      close();
    });
    createBtn.addClickShortcut(Key.ENTER);
    Button closeBtn = ButtonFactory.createRedButton("Cancel", e -> close());

    add(new HorizontalLayout(gameField, createBtn, closeBtn));
  }
}
