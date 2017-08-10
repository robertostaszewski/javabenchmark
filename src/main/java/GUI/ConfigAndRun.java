package main.java.GUI;

import main.java.utilities.HardwareDetailsManager;
import main.java.utilities.LinuxHardwareDetailsManager;
import main.java.utilities.WindowsHardwareDetailsManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Tworzy okno programu na podstawie plikow fxml.
 */
public class ConfigAndRun extends Application {

    /**
     * Glowna scena do ktorej dodawane sa obiekty GUI
     */
    private Stage primaryStage;

    /**
     * Obiekt w ktorym znajduja sie karty GUI
     */
    private TabPane rootLayout;

    /**
     * Obiekt odczytujacy informacje o komputerze
     */
    private static HardwareDetailsManager hardwareDetailsManager;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Java Benchmark");

        this.primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        initRootLayout();

        showTestTab();
        showRankingTab();
        showInfoTab();
    }

    /**
     * Inicjalizuje {@link ConfigAndRun#rootLayout} na podstawie pliku fxml
     */
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ConfigAndRun.class.getResource("/main/java/GUI/TabRoot.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Inicjalizuje karty {@link ConfigAndRun#rootLayout} na podstawie pliku fxml
     */
    private void showTestTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ConfigAndRun.class.getResource("/main/java/GUI/TestTab.fxml"));
            AnchorPane testTab = loader.load();
            rootLayout.getTabs().get(0).setContent(testTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicjalizuje karty {@link ConfigAndRun#rootLayout} na podstawie pliku fxml
     */
    private void showRankingTab(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ConfigAndRun.class.getResource("/main/java/GUI/ScoreTab.fxml"));
            AnchorPane scoreTab = loader.load();
            rootLayout.getTabs().get(1).setContent(scoreTab);
            rootLayout.getTabs().get(1).setOnSelectionChanged(event -> {
                if (rootLayout.getTabs().get(1).isSelected()) {
                    scoreTab.fireEvent(event);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicjalizuje karty {@link ConfigAndRun#rootLayout} na podstawie pliku fxml
     */
    private void showInfoTab(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ConfigAndRun.class.getResource("/main/java/GUI/InfoTab.fxml"));
            AnchorPane infoTab = loader.load();
            rootLayout.getTabs().get(2).setContent(infoTab);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Konstruktor w ktorym tworzony jest obiekt typu {@link HardwareDetailsManager}
     * odpowiedni dla systemu operacyjnego
     */
    public ConfigAndRun(){
        if(System.getProperty("os.name")
                .toLowerCase().startsWith("windows")){
            hardwareDetailsManager = new WindowsHardwareDetailsManager();
        }
        else {
            hardwareDetailsManager = new LinuxHardwareDetailsManager();
        }
    }

    /**
     * Glowna funkcja programu wywoluje metode {@link Application#launch(String...)}
     *
     * @param args parametry przekazywane do {@link Application#launch(String...)}
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Zwraca obiekt typu {@link HardwareDetailsManager}
     * odpowiedni dla systemu operacyjnego
     *
     * @return obiekt typu {@link HardwareDetailsManager}
     */
    public static HardwareDetailsManager getHardwareDetailsManager() {
        return hardwareDetailsManager;
    }
}