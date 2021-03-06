package code.view.panes;

import code.entities.Styles;
import code.entities.Views;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * MenuBar.java
 * The main menu bar used for switching between panes.
 * @author Lucas Eliasson, Tor Stenfeldt, Georg Grankvist
 * @version 4.0
 */
public class MenuPane extends StackPane {
    private Label title;
    private MainPane mainPane;
    private Button[] buttons;
    private Button selectedButton;
    private Button toggleButton;

    private ImageView toggleImage;
    private ImageView viewLogo;
    private Image logoText;
    private Image logo;
    private Image activeExpand;
    private Image activeMinimize;
    private Image minimizeImage;
    private Image expandImage;

    private HBox toggleContainer;
    private boolean expanded;

    public MenuPane(MainPane mainPane) {
        this.mainPane = mainPane;
        buttons = new Button[Views.values().length];
        this.expanded = true;

        for (int i=0; i<buttons.length; i++) {
            buttons[i] = initButton(Views.values()[i]);
        }

        selectedButton = buttons[0];
        selectedButton.setStyle(Styles.getMenuButtonHighlighted());

        VBox mainContainer = new VBox();

        title = new Label();
        title.getStylesheets().add("styles.css");
        title.getStyleClass().add("title");

        HBox titleContainer = new HBox();
        titleContainer.setPadding(new Insets(0, 0, 0, 28));
        titleContainer.getChildren().add(title);
        titleContainer.setAlignment(Pos.CENTER);

        mainContainer.getChildren().add(titleContainer);

        for (Button b: buttons) {
            mainContainer.getChildren().add(b);
        }

        try {
            logoText = new Image(new FileInputStream("src/resources/logo/logoText.png"));
            logo = new Image(new FileInputStream("src/resources/logo/logo.png"));
            viewLogo = new ImageView(logoText);

            minimizeImage = new Image(new FileInputStream("src/resources/toggleButton/crop.png"));
            expandImage = new Image(new FileInputStream("src/resources/toggleButton/expand.png"));
            activeExpand = new Image(new FileInputStream("src/resources/toggleButton/expand-active.png"));
            activeMinimize = new Image(new FileInputStream("src/resources/toggleButton/crop-active.png"));
            toggleImage = new ImageView(minimizeImage);

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }

        title.setGraphic(viewLogo);
        toggleButton = new Button("");
        toggleButton.setPrefSize(40, 40);
        toggleButton.setStyle("-fx-background-color: #21252B;" + "-fx-cursor: hand;");
        toggleButton.setOnAction(e -> toggle(toggleButton));
        toggleButton.setGraphic(toggleImage);

        toggleButton.setOnMouseEntered(e-> {
            toggleImage = new ImageView(activeMinimize);
            toggleButton.setGraphic(toggleImage);
        });

        toggleButton.setOnMouseExited(a-> {
            toggleImage = new ImageView(minimizeImage);
            toggleButton.setGraphic(toggleImage);
        });

        toggleContainer = new HBox();
        toggleContainer.setPadding(new Insets(20, 0, 20, 105));
        toggleContainer.getChildren().add(toggleButton);
        toggleContainer.setAlignment(Pos.BOTTOM_LEFT);

        mainContainer.getChildren().add(toggleContainer);
        setStyle("-fx-background-color: #21252B;");
        getChildren().add(mainContainer);
        setPrefSize(280, 736);
    }

    /**
     * Activated ToggleButton
     * @param button Button to be scaled
     */
    private void toggle(Button button) {
        if (this.expanded) {
            contract(button);
        } else {
            expand(button);
        }

        this.expanded = !this.expanded;
    }

    /**
     * Contracts the menu
     */
    private void contract(Button button) {
        for (EnhancedPane p: this.mainPane.getViews()) {
            p.expand();
        }

        setPrefSize(134,736);

        for (int i=0; i<Views.values().length; i++) {
            this.buttons[i].setText("");
        }

        toggleImage = new ImageView(expandImage);
        toggleButton.setGraphic(toggleImage);

        toggleButton.setOnMouseEntered(e-> {
            toggleImage = new ImageView(activeExpand);
            toggleButton.setGraphic(toggleImage);
        });

        toggleButton.setOnMouseExited(a->{
            toggleImage = new ImageView(expandImage);
            toggleButton.setGraphic(toggleImage);
        } );

        button.setPrefSize(40,40);
        toggleContainer.setPadding(new Insets(20, 0, 20, 35));

        title.setText("C");
        title.setGraphic(null);
    }

    /**
     * Expands the menu
     */
    private void expand(Button button) {
        for (EnhancedPane p: this.mainPane.getViews()) {
            p.contract();
        }

        setPrefSize(280, 736);

        for (int i=0; i<Views.values().length; i++) {
            this.buttons[i].setText(Views.values()[i].name());
        }

        toggleButton.setOnMouseEntered(e-> {
            toggleImage = new ImageView(activeMinimize);
            toggleButton.setGraphic(toggleImage);

        });

        toggleButton.setOnMouseExited(a->{
            toggleImage = new ImageView(minimizeImage);
            toggleButton.setGraphic(toggleImage);
        } );

        toggleImage = new ImageView(minimizeImage);
        toggleButton.setGraphic(toggleImage);

        button.setPrefSize(40,40);
        toggleContainer.setPadding(new Insets(20, 0, 20, 105));

        title.setText("");
        title.setGraphic(viewLogo);
    }

    /**
     * Initialize the buttons
     * @param view Active code.view
     * @return new styled button
     */
    private Button initButton(Views view) {
        Button newButton = new Button(view.name());
        newButton.setPrefSize(280, 100);
        newButton.setStyle(Styles.getMenuButtonStandard());

        try {
            Image image = new Image(new FileInputStream("src/resources/deSelectedImages/" + view.name() + ".png"));
            ImageView imageView = new ImageView(image);

            Image selectedImage = new Image(new FileInputStream("src/resources/activeImages/" + view.name() + ".png"));
            ImageView selectedView = new ImageView(selectedImage);
            newButton.setGraphic(imageView);

            newButton.setOnMouseClicked((handler) -> {
                for (Button b : buttons) {
                    b.setStyle(Styles.getMenuButtonStandard());
                }
                newButton.setGraphic(selectedView);
                mainPane.setView(view);
                newButton.setStyle(Styles.getMenuButtonHighlighted());
                selectedButton = newButton;
            });

            newButton.setOnMouseEntered((handler) -> {
                newButton.setStyle(Styles.getMenuButtonHighlighted());
                newButton.setGraphic(selectedView);
            });

            newButton.setOnMouseExited((handler) -> {
                newButton.setStyle(Styles.getMenuButtonStandard());
                newButton.setGraphic(imageView);
                selectedButton.setStyle(Styles.getMenuButtonHighlighted());
            });

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }

        return newButton;
    }

    public boolean getExpanded() {
        return this.expanded;
    }
}
