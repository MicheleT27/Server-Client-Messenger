package com.example.messengerclient;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.messengerclient.DBUtils.changeScene;

public class HelloController extends LogInController implements Initializable {

    @FXML
    private AnchorPane messenger;
    @FXML
    public Circle showProfilePic;
    @FXML
    private Button button_chats;
    @FXML
    private Button button_settings;
    @FXML
    private Button button_logout;
    @FXML
    private ImageView img_search;
    @FXML
    private TextField tf_search;
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private Label label_welcome;

    private Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            client = new Client(new Socket("localhost", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        client.receiveMessageFromServer(vbox_messages);

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String messageToSend = tf_message.getText();
                if (!messageToSend.isEmpty()) {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239, 242, 255);" +
                            "-fx-background-color: rgb(123, 210, 202);" +
                            "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);

                    client.sendMessageToServer(messageToSend);
                    tf_message.clear();
                }
            }
        });

        /*button_settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "settings.fxml", "Settings Page", null, null);
            }
        });*/

        /*button_chats.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Messenger", null, null);
            }
        });*/

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    changeScene(event, "login.fxml", "Log In!", null, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void switchToChats(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void addLabel(String msgFromServer, VBox vBox) {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233, 233, 235);" +
                " -fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);

            }
        });
    }
}