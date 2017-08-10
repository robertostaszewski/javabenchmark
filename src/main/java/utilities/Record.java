package main.java.utilities;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.csv.CSVRecord;

/**
 * Klasa symulujaca rekord w tabeli oraz pliku score.csv.
 * Niezbedna do porawnego wyswietlenia danych przez biblioteke JavaFX.
 */
public class Record {

    /**
     * Zmienna pamietajaca dane w kolumnie Name
     */
    private final SimpleStringProperty name;

    /**
     * Zmienna pamietajaca dane w kolumnie CPU score
     */
    private final SimpleStringProperty cpuName;

    /**
     * Zmienna pamietajaca dane w kolumnie CPU score
     */
    private final SimpleStringProperty cpuScore;

    /**
     * Zmienna pamietajaca dane w kolumnie GPU name
     */
    private final SimpleStringProperty gpuName;

    /**
     * Zmienna pamietajaca dane w kolumnie GPU score
     */
    private final SimpleStringProperty gpuScore;

    /**
     * Zmienna pamietajaca dane w kolumnie Disk name
     */
    private final SimpleStringProperty diskName;

    /**
     * Zmienna pamietajaca dane w kolumnie Diskscore
     */
    private final SimpleStringProperty diskScore;

    /**
     * Zmienna pamietajaca dane w kolumnie RAM name
     */
    private final SimpleStringProperty ramName;

    /**
     * Zmienna pamietajaca dane w kolumnie RAM score
     */
    private final SimpleStringProperty ramScore;

    /**
     * Zmienna pamietajaca dane w kolumnie Total score
     */
    private final SimpleStringProperty totalScore;

    /**
     * Zmienna pamietajaca dane w kolumnie Total score
     */
    private final SimpleStringProperty javaVersion;

    /**
     * Zmienna pamietajaca dane w kolumnie Total score
     */
    private final SimpleStringProperty vmVersion;

    /**
     * Konstruktor klasy tworzonej na podstawie rekordu pliku score.csv.
     *
     * @param csvRecord rekord z pliku score.csv
     */
    public Record(CSVRecord csvRecord){

        name = new SimpleStringProperty(csvRecord.get(CsvHeaders.NAME));
        cpuName = new SimpleStringProperty(csvRecord.get(CsvHeaders.CPU_NAME));
        cpuScore = new SimpleStringProperty(csvRecord.get(CsvHeaders.CPU_SCORE));
        gpuName = new SimpleStringProperty(csvRecord.get(CsvHeaders.GPU_NAME));
        gpuScore = new SimpleStringProperty(csvRecord.get(CsvHeaders.GPU_SCORE));
        diskName = new SimpleStringProperty(csvRecord.get(CsvHeaders.DISK_NAME));
        diskScore = new SimpleStringProperty(csvRecord.get(CsvHeaders.DISK_SCORE));
        ramName = new SimpleStringProperty(csvRecord.get(CsvHeaders.RAM_NAME));
        ramScore = new SimpleStringProperty(csvRecord.get(CsvHeaders.RAM_SCORE));
        totalScore = new SimpleStringProperty(csvRecord.get(CsvHeaders.TOTAL_SCORE));
        javaVersion = new SimpleStringProperty(csvRecord.get(CsvHeaders.JAVA_VERSION));
        vmVersion = new SimpleStringProperty(csvRecord.get(CsvHeaders.VM_Version));

    }

    /**
     * Zwraca wartosc pola {@link Record#name} jako {@link String}
     *
     * @return pole {@link Record#name} jako {@link String}
     */
    public String getName() {
        return name.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#cpuName} jako {@link String}
     *
     * @return pole {@link Record#cpuName} jako {@link String}
     */
    public String getCpuName() {
        return cpuName.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#cpuScore} jako {@link String}
     *
     * @return pole {@link Record#cpuScore} jako {@link String}
     */
    public String getCpuScore() {
        return cpuScore.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#gpuName} jako {@link String}
     *
     * @return pole {@link Record#gpuName} jako {@link String}
     */
    public String getGpuName() {
        return gpuName.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#gpuScore} jako {@link String}
     *
     * @return pole {@link Record#gpuScore} jako {@link String}
     */
    public String getGpuScore() {
        return gpuScore.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#diskName} jako {@link String}
     *
     * @return pole {@link Record#diskName} jako {@link String}
     */
    public String getDiskName() {
        return diskName.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#diskScore} jako {@link String}
     *
     * @return pole {@link Record#diskScore} jako {@link String}
     */
    public String getDiskScore() {
        return diskScore.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#ramScore} jako {@link String}
     *
     * @return pole {@link Record#ramScore} jako {@link String}
     */
    public String getRamScore() {
        return ramScore.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#ramName} jako {@link String}
     *
     * @return pole {@link Record#ramName} jako {@link String}
     */
    public String getRamName() {
        return ramName.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#totalScore} jako {@link String}
     *
     * @return pole {@link Record#totalScore} jako {@link String}
     */
    public String getTotalScore() {
        return totalScore.get();
    }

    /**
     * Zwraca wartosc pola {@link Record#javaVersion} jako {@link String}
     *
     * @return pole {@link Record#javaVersion} jako {@link String}
     */
    public String getJavaVersion() {
        return javaVersion.get();
    }

    /**     * Zwraca wartosc pola {@link Record#vmVersion} jako {@link String}
     *
     * @return pole {@link Record#vmVersion} jako {@link String}
     */
    public String getVmVersion() {
        return vmVersion.get();
    }
}
