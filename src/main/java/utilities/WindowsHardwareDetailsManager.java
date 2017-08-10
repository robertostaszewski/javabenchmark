package main.java.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WindowsHardwareDetailsManager extends HardwareDetailsManager {

    public WindowsHardwareDetailsManager(){
        super();
    }

    @Override
    public String getFormattedCpuDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"cmd.exe", "/c", "wmic cpu get name"});
        List<String> foundedCPUs = new ArrayList<>();
        for(String line : commandOutputLines){
            if(!line.contains("Name") && !line.isEmpty()){
                foundedCPUs.add(line.trim());
            }
        }
        return String.join("; ", foundedCPUs).replaceAll(",", ";");
    }

    @Override
    public String getFormattedGpuDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"cmd.exe", "/c", "wmic path win32_VideoController get name"});
        List<String> foundedGPUs = new ArrayList<>();
        for(String line : commandOutputLines){
            if(!line.contains("Name") && !line.isEmpty()){
                foundedGPUs.add(line.trim());
            }
        }
        return String.join("; ", foundedGPUs);
    }

    @Override
    public String getFormattedDiskDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"cmd.exe", "/c", "wmic diskdrive get model, size"});
        List<String> foundedDisks = new ArrayList<>();
        for(String line : commandOutputLines){
            if(!line.contains("Model") && !line.isEmpty()){
                foundedDisks.add(line.trim());
            }
        }
        List<String> formattedLines = new ArrayList();
        for (String diskDetails : foundedDisks){
            List<String> details = Arrays.asList(diskDetails.split("\\s+"));
            if(details.size() >=2) {
                long size;
                try {
                    size = Long.parseLong(details.get(details.size()-1));
                } catch (NumberFormatException e){
                    size = 0;
                }
                String manufacturer = String.join(" ", details.subList(0, details.size()-1));
                formattedLines.add(manufacturer + " " + size/(1024*1024*1024)+ "GB");
            }
        }
        return String.join("; ", formattedLines);
    }

    @Override
    public String getFormattedRamDetails() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"cmd.exe", "/c", "wmic memorychip get manufacturer, capacity"});
        List<String> foundedRam = new ArrayList<>();
        List<String> formatedLines = new ArrayList();
        for(String line : commandOutputLines){
            if(!line.contains("Manufacturer") && !line.isEmpty()){
                foundedRam.add(line.trim());
                List<String> details = Arrays.asList(line.trim().split("\\s+"));
                if(details.size() >=2) {
                    long size;
                    try {
                        size = Long.parseLong(details.get(0));
                    } catch (NumberFormatException e){
                        size = 0;
                    }
                    String manufacturer = String.join(" ", details.subList(1, details.size()));
                    formatedLines.add(manufacturer + " " + size/(1024*1024*1024) + "GB");
                }
            }
        }
//        List<String> formatedLines = new ArrayList();
//        for (String ramDetails : foundedRam){
//            List<String> details = Arrays.asList(ramDetails.split("\\s+"));
//            if(details.size() >=2) {
//                long size;
//                try {
//                    size = Long.parseLong(details.get(0));
//                } catch (NumberFormatException e){
//                    size = 0;
//                }
//                String manufacturer = String.join(" ", details.subList(1, details.size()));
//                formatedLines.add(manufacturer + " " + size/(1024*1024*1024) + "GB");
//            }
//        }
        return String.join("; ", formatedLines);
    }

    @Override
    public String getFormattedName() {
        List<String> commandOutputLines = getCommandOutput(new String[]{"cmd.exe", "/c", "wmic computersystem get name"});
        List<String> foundedName = new ArrayList<>();
        for(String line : commandOutputLines){
            if(!line.contains("Name") && !line.isEmpty()){
                foundedName.add(line.trim());
            }
        }
        return String.join("; ", foundedName);
    }
}
