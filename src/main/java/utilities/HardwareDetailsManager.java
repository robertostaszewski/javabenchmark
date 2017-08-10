package main.java.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa abstrakcyjna zawierajaca metody niezbede do zaimplementowania
 * przez klasy dziedziczace, pozwalajaca na odczytanie informacji o sprzecie.
 * Klasa dziedziczaca implementuje metody dla konkretnego systemu.
 */
public abstract class HardwareDetailsManager {

    /**
     * Wykonuje okreslona komende (podana jako parametr) w konsoli lub terminualu.
     * Metoda wspolna dla wszytskich klas dziedziczacych.
     *
     * @param command komenda do wykonania.
     * @return wynik komendy (jeden element listy, to jedna linia).
     */
    List<String> getCommandOutput(String[] command){
        List<String> commandOutput = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder(
                command);
        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) { break; }
            commandOutput.add(line);
        }
        return commandOutput;
    }

    /**
     * Odczytuje informacje na temat nazwy maszyny wirtualnej.
     *
     * @return Informacje w formacie odpowiednim do wyswietlenia.
     */
    public String getVmVersion(){
        return System.getProperty("java.vm.version");
    }

    /**
     * Odczytuje informacje na temat wersji Javy.
     *
     * @return Informacje w formacie odpowiednim do wyswietlenia.
     */
    public String getJavaVersion(){
        return System.getProperty("java.version");
    }

    /**
     * Odczytuje informacje na temat procesora.
     *
     * @return Informacje w formacie odpowiednim do wyswietlenia.
     */
    public abstract String getFormattedCpuDetails();

    /**
     * Odczytuje informacje na temat karty graficznej.
     *
     * @return Informacje w formacie odpowiednim do wyswietlenia.
     */
    public abstract String getFormattedGpuDetails();

    /**
     * Odczytuje informacje na temat dysku.
     *
     * @return Informacje w formacie odpowiednim do wyswietlenia.
     */
    public abstract String getFormattedDiskDetails();

    /**
     * Odczytuje informacje na temat pamieci RAM.
     *
     * @return Informacje w formacie odpowiednim do wyswietlenia.
     */
    public abstract String getFormattedRamDetails();

    /**
     * Odczytuje informacje na temat nazwy modelu komputera.
     *
     * @return Informacje w formacie odpowiednim do wyswietlenia.
     */
    public abstract String getFormattedName();

}
