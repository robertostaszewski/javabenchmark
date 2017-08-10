package main.java.controllers;

import main.java.GUI.ConfigAndRun;
import main.java.utilities.HardwareDetailsManager;
import main.java.testingparts.CPU.*;
import main.java.testingparts.GPU.GpuTestWindow;
import main.java.testingparts.HardDrive.MeasureIOPerformance;
import main.java.testingparts.RAM.TestMemoryAccessPatterns;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

/**
 * Kontroler zachowania zakladki testingparts w stworzonej aplikacji
 */
public class TestTabController implements Initializable{

    /**
     * Obiekt reprezentujacy przycisk Start
     */
    @FXML
    private Button startBtn;

    /**
     * Obiekt reprezentujacy przycisk Cancel
     */
    @FXML
    private Button cancelButton;

    /**
     * Obiekt reprezentujacy pasek progresu. Dane pobiera z {@link TestTabController#progressPick}
     */
    @FXML
    private ProgressBar progressBar;
    
    /**
     * Obiekt reprezentujacy pole tekstowe informujace o etapie testowania
     */
    @FXML
    private Label workingOn;
    
    /**
     * Obiekt reprezentujacy znacznik umozliwiajacy przetestowanie procesora 
     */
    @FXML
    private CheckBox cpu;
    
    /**
     * Obiekt reprezentujacy znacznik umozliwiajacy przetestowanie karty graficznej
     */
    @FXML
    private CheckBox gpu;
    
    /**
     * Obiekt reprezentujacy znacznik umozliwiajacy przetestowanie dysku
     */
    @FXML
    private CheckBox disk;
    
    /**
     * Obiekt reprezentujacy znacznik umozliwiajacy przetestowanie pamieci RAM
     */
    @FXML
    private CheckBox ram;
    
    /**
     * Obiekt reprezentujacy pole tekstowe wyswietlajace uzystkany wynik
     */
    @FXML
    private TextFlow textFlow;

    /**
     * Zmienna informujaca o stanie progresu przebiegu testow
     */
    private double progressPick = 0.0;
    
    /**
     * Obiekt pozwalajacy uzykac informacje o sprzecie
     */
    private final HardwareDetailsManager hardwareDetailsManager = ConfigAndRun.getHardwareDetailsManager();

    /**
     * Funkcja testujaca. Tworzy nowy watek w ktorym kolejno uruchamiane sa zaznaczone testy.
     * Ostatnim etapem dzialania jest zapis danych do pliku score.csv.
     */
    @FXML
    public void setStartBtn() {
        Thread first = new Thread(() -> {
            GpuTestWindow cube = null;
            resetProgressBar();
            countNumOfTests();
            diableControlls();
            ResultController.reset();
            if(cpu.isSelected()){
                increaseProgressAndChangeText("CPU: Int test...");
                IntTest.measureAll();
                increaseProgressAndChangeText("CPU: Long test...");
                LongTest.measureAll();
                increaseProgressAndChangeText("CPU: Double test...");
                DoubleTest.measureAll();
                increaseProgressAndChangeText("CPU: Quicksort test...");
                Quicksort.warmAndTest();
                increaseProgressAndChangeText("CPU: Prime Number test...");
                PrimeNumberTest.warmAndTest();
                increaseProgressAndChangeText("CPU: compress test...");
                CompressTest.warmAndTest();
                increaseProgressAndChangeText("CPU: encryption test...");
                DataEncryption.warmAndTest();
            }
            if(gpu.isSelected()) {
                increaseProgressAndChangeText("GPU test...");
                cube = new GpuTestWindow();
                cube.addCube(0f, 0f, 0f);
                try {
                    cube.measure();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (cube != null){
                cube.clearCubes();
            }

            if(disk.isSelected()){
                increaseProgressAndChangeText("DISK: write, read speed test...");
                MeasureIOPerformance.test();

            }

            if(ram.isSelected()){
                increaseProgressAndChangeText("RAM test...");
                try {
                    new TestMemoryAccessPatterns().warmAndTest();
                } catch (OutOfMemoryError e){
                    setText("java.lang.OutOfMemoryError: Java heap space");
                    throw new OutOfMemoryError(e.getMessage());
                }

            }

            enableControlls();
            changeButtonsStatus();
            if(cpu.isSelected() || ram.isSelected() || gpu.isSelected() || disk.isSelected()) {
                increaseProgressAndChangeText("Done!");
                try {
                    Files.write(
                            Paths.get("./score.csv"),
                            String
                                    .join(",",
                                            System.lineSeparator() + hardwareDetailsManager.getFormattedName(),
                                            hardwareDetailsManager.getFormattedCpuDetails(),
                                            getFormatedResult(ResultController.getCpuScore()),
                                            hardwareDetailsManager.getFormattedGpuDetails(),
                                            getFormatedResult(ResultController.getGpuScore()),
                                            hardwareDetailsManager.getFormattedDiskDetails(),
                                            getFormatedResult(ResultController.getDiskScore()),
                                            hardwareDetailsManager.getFormattedRamDetails(),
                                            getFormatedResult(ResultController.getRamScore()),
                                            getFormatedResult(ResultController.getTotalScore()),
                                            hardwareDetailsManager.getJavaVersion(),
                                            hardwareDetailsManager.getVmVersion())
                                    .getBytes(),
                            StandardOpenOption.APPEND);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                displayResults();
            } else {
                setText("Aborted");
            }
        });
        first.start();
    }

    /**
     * Funkcja parsujaca zmienna typu {@link long} do typu {@link String}.
     * Jesli {@code l} wynosi {@link Long#MIN_VALUE}, to zwracany jest "-".
     *
     * @param l liczba do parsowania
     * @return liczba jako obiekt {@link String}
     */
    private String getFormatedResult(Long l){
        return (l != Long.MIN_VALUE || l == null) ? Long.toString(l) : "-";
    }

    /**
     * Blokuje mozliwosc naciskania elementow graficznego intefejsu, w czasie trwania testow.
     * wywoluje funkcje {@link TestTabController#changeDisableStatus(boolean)} z paramatrem {@code true}.
     */
    private void diableControlls(){
        changeDisableStatus(true);
    }

    /**
     * Odblokowuje mozliwosc naciskania elementow graficznego intefejsu pozakonczeniu testow.
     * wywoluje funkcje {@link TestTabController#changeDisableStatus(boolean)} z paramatrem {@code false}.
     */
    private void enableControlls(){
        changeDisableStatus(false);
    }

    /**
     * Zgodnie z parametrem {@code value} blokuje lub odblokowuje przyciski graficznego interfejsu
     *
     * @param value Okresla status elementow graficznego interfejsu.
     */
    private void changeDisableStatus(boolean value){
        cancelButton.setDisable(!value);
        startBtn.setDisable(value);
        cpu.setDisable(value);
        gpu.setDisable(value);
        ram.setDisable(value);
        disk.setDisable(value);
    }

    /**
     * Funkcja zliczajaca ilosc testow koniecznych do wykonania,
     * umozliwia to odpowiednie sterowanie paskiem progresu {@link TestTabController#progressBar}.
     */
    private void countNumOfTests(){
        int numOfTests = 0;

        if(cpu.isSelected())
            numOfTests += 7;
        if(gpu.isSelected())
            numOfTests += 1;
        if(ram.isSelected())
            numOfTests += 1;
        if(disk.isSelected())
            numOfTests += 1;

        progressPick = 1.0/((double)(numOfTests + 1));

    }

    /**
     * Resetuje postep paska progresu do stanu poczatkowego.
     * Uruchamiana w momencie ponownego testowania.
     */
    private void resetProgressBar(){
        progressBar.setProgress(0.0);
    }

    /**
     * Ziwkesza pogres na {@link TestTabController#progressBar},
     * oraz zmienia wyswietlany tekst na {@link TestTabController#workingOn}
     *
     * @param text tekst do wyswietlenia.
     */
    private void increaseProgressAndChangeText(String text){

        double progress = progressBar.getProgress() + progressPick;
        progressBar.setProgress(progress);
        setText(text);
    }

    /**
     * Wyswietla otrzymane rezultaty po zakonczonym dzialaniu w elmencie {@link TestTabController#textFlow}
     */
    private void displayResults(){
        Platform.runLater(() -> {
            Text toShow = new Text(
                    "Name: " + hardwareDetailsManager.getFormattedName() + System.lineSeparator() +
                    "Total score: " +getFormatedResult(ResultController.getTotalScore()) + System.lineSeparator() +
                    "CPU: " + getFormatedResult(ResultController.getCpuScore()) + System.lineSeparator() +
                    "GPU: " + getFormatedResult(ResultController.getGpuScore()) + System.lineSeparator() +
                    "DISK: " + getFormatedResult(ResultController.getDiskScore()) + System.lineSeparator() +
                    "RAM: " + getFormatedResult(ResultController.getRamScore()) + System.lineSeparator() +
                    "Java Version: " + hardwareDetailsManager.getJavaVersion() + System.lineSeparator() +
                    "Virtual Machine Version: " + hardwareDetailsManager.getVmVersion() + System.lineSeparator()
            );


            textFlow.getChildren().clear();
            textFlow.getChildren().add(toShow);
        });
    }

    /**
     * Funkcja zmieniajaca wyswietlana wartosc w elemencie {@link TestTabController#workingOn}
     *
     * @param text tekst do wyswietlenia.
     */
    private void setText(String text){
        Platform.runLater(() -> workingOn.setText(text));
    }

    /**
     * Funkcja anulujaca testowanie.
     */
    @FXML
    private void closeThread() {
        setText("Cancelling..");
        cancelButton.setDisable(true);
        cpu.setSelected(false);
        gpu.setSelected(false);
        ram.setSelected(false);
        disk.setSelected(false);
        resetProgressBar();
    }

    /**
     * Zmienia status przycisku {@link TestTabController#startBtn},
     * w zaleznosci od tego, czy zostal wybrany jakikolwiek z podzespolow do testowania.
     */
    @FXML
    private void changeButtonsStatus(){
        if(cpu.isSelected() || ram.isSelected() || gpu.isSelected() || disk.isSelected())
            startBtn.setDisable(false);
        else {
            startBtn.setDisable(true);
            cancelButton.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeButtonsStatus();
        setText("Choose parts to test");
    }
}
