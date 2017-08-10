package main.java.controllers;

import main.java.GUI.ConfigAndRun;
import main.java.utilities.HardwareDetailsManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Klasa kontrolujaca zachowanie zakladki Device Info w stworzonej aplikacji
 */
public class DeviceInfoController implements Initializable{

    /**
     * Zmienna odpowiedzialna za wyswietlenie informacji na temat testowanego sprzetu.
     * Inicjalizowana na podstawie pliku fxml.
     */
    @FXML
    private TextFlow textFlow;

    /**
     * Obiekt odpowiedzialny za odpowiednie odczytanie informacji na temat sprzetu.
     */
    private final HardwareDetailsManager hardwareDetailsManager = ConfigAndRun.getHardwareDetailsManager();

    /**
     * Zwraca informacje na temat sprzetu odpowiednio sformatowane.
     *
     * @return Sformatowane informacje na temat sprzetu.
     */
    private String SystemInfo() {
        return "# CPU: " + hardwareDetailsManager.getFormattedCpuDetails() + "\n" +
                    "# GPU: " + hardwareDetailsManager.getFormattedGpuDetails() + "\n" +
                    "# Disk: " + hardwareDetailsManager.getFormattedDiskDetails() + "\n" +
                    "# Ram: " + hardwareDetailsManager.getFormattedRamDetails() + "\n";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFlow.getChildren().add(new Text(SystemInfo()));
    }
}
