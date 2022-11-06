package com.everest.courier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Wrapper {
    public void exit(int value) {
        System.exit(value);
    }

    public void showError(String msg) {
        System.out.println(msg);
    }

    public void showError(Exception err) {
        System.out.println(err);
    }

    public void showVersion() {
        System.out.println("Version 1.0.0.");
    }

    public void showCopyRight() {
        System.out.println("(c) 2022 Everest Engineering. All rights reserved");
    }

    public void showHelp() {
        System.out.println("[Help manual here...]");
    }

    public List<String> getInputLines(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        scanner.useDelimiter(Pattern.compile("[\\n]"));
        List<String> lines = new ArrayList<>();
        while(scanner.hasNext()) {
            lines.add(scanner.next());
        }
        return lines;
    }

    public void showInputs(Context context) {
        System.out.println("\nInputs:");
        System.out.println(context.baseDeliveryCost + " " + context.noOfPackages);
        for(ShipmentItem item: context.shipmentItems) {
            String msg = item.packageId + " " + item.weight + " " + item.distance;
            if(item.offerCode != null)
                msg += " " + item.offerCode;
            System.out.println(msg);
        }
        if(context instanceof TimeContext) {
            TimeContext timeContext = (TimeContext)context;
            System.out.println(timeContext.noOfVehicle + " " + timeContext.vehicleSpeed + " " + timeContext.vehicleCapacity);
        }
    }

    public void showResults(Context context) {
        if(context.resultItems.size() > 0)
            System.out.println("\nOutputs:");
        DecimalFormat format = new DecimalFormat("0.##");
        for(ResultItem item: context.resultItems) {
            String msg = item.packageId + " " + format.format(item.discount) + " " + format.format(item.totalCost);
            if(item instanceof TimeItem) {
                TimeItem timeItem = (TimeItem)item;
                msg += " " + (timeItem.arrivalTime != null? format.format(timeItem.arrivalTime): "None");
            }
            System.out.println(msg);
        }
        if(context.resultItems.size() > 0)
            System.out.println();
    }

    public void saveOutput(String filename, Context context) throws IOException {
        FileWriter fw = new FileWriter(filename);
        DecimalFormat format = new DecimalFormat("0.##");
        for (ResultItem item :context.resultItems) {
            String msg = item.packageId + " " + format.format(item.discount) + " " + format.format(item.totalCost);
            if(item instanceof TimeItem) {
                TimeItem timeItem = (TimeItem)item;
                msg += " " + (timeItem.arrivalTime != null? format.format(timeItem.arrivalTime): "");
            }
            msg += "\n";
            fw.write(msg);
        }
        fw.close();
    }

    public String getConfiguration() throws IOException {
        return Files.readString(Paths.get("Configuration.json"));
    }

    public void log(boolean debug, String msg) {
        if(debug)
            System.out.println(msg);
    }
}
