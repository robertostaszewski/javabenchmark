package main.java.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinuxHardwareDetailsManager extends HardwareDetailsManager {

    @Override
    public String getFormattedCpuDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"sh", "-c", "inxi -C -c 0"});
        List<String> output = new ArrayList<>();
        String lookingFor = "cpu:";
        commandOutputLines.forEach(line -> {
            if(line.toLowerCase().contains(lookingFor)){
                int index = line.toLowerCase().indexOf(lookingFor);
                output.add(line.substring(index + lookingFor.length()).trim());
            }
        });
        return String.join("; ", output);
    }

    @Override
    public String getFormattedGpuDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"sh", "-c", "inxi -G -c 0"});
        List<String> output = new ArrayList<>();
        String lookingFor = "card-";
        commandOutputLines.forEach(line -> {
            if(line.toLowerCase().contains(lookingFor)){
                int index = line.toLowerCase().indexOf(lookingFor);
                output.add(line.substring(index + lookingFor.length()+2).trim());
            }
        });
        return String.join("; ", output);
    }

    @Override
    public String getFormattedDiskDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"sh", "-c", "inxi -D -c 0"});
        List<String> output = new ArrayList<>();
        String lookingFor = "model:";
        commandOutputLines.forEach(line -> {
            if(line.toLowerCase().contains(lookingFor)){
                int index = line.toLowerCase().indexOf(lookingFor);
                output.add(line.substring(index + lookingFor.length()).trim());
            }
        });
        return String.join("; ", output);
    }

    @Override
    public String getFormattedRamDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"sh", "-c", "inxi -I -c 0"});
        List<String> output = new ArrayList<>();
        String lookingFor = "memory:";
        commandOutputLines.forEach(line -> {
            if(line.toLowerCase().contains(lookingFor)){
                int indexFrom = line.toLowerCase().indexOf(lookingFor);
                String tempString = line.substring(indexFrom + lookingFor.length()).trim();
                indexFrom = tempString.indexOf("/");
                int indexTo = tempString.toLowerCase().indexOf("mb");
                output.add(tempString.substring(indexFrom + "/".length(), indexTo + "mb".length()).trim());
            }
        });
        return String.join("; ", output);
    }

    @Override
    public String getFormattedName() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"sh", "-c", "inxi -M -c 0"});
        List<String> output = new ArrayList<>();
        String lookingFor = "model:";
        commandOutputLines.forEach(line -> {
            if(line.toLowerCase().contains(lookingFor)){
                int indexFrom = line.toLowerCase().indexOf(lookingFor);
                String tempString = line.substring(indexFrom + lookingFor.length()).trim();
                int indexTo = tempString.toLowerCase().indexOf(":");
                String[] name = tempString.substring(0, indexTo).split("\\s+");
                output.add(String.join(" ", Arrays.copyOf(name, name.length-1)));
            }
        });
        return String.join("; ", output);
    }

}
