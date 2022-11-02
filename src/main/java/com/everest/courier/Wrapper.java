package com.everest.courier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Wrapper {
    public static void exit(int value) {
        System.exit(value);
    }

    public static void showError(String msg) {
        System.out.println(msg);
    }

    public static void showError(Exception err) {
        System.out.println(err);
    }

    public static void showVersion() {
        System.out.println("Version 1.0.0.");
    }

    public static void showCopyRight() {
        System.out.println("(c) 2022 Everest Engineering. All rights reserved");
    }

    public static void showHelp() {
        System.out.println("[Help manual here...]");
    }

    public static List<String> getInputLines(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        scanner.useDelimiter(Pattern.compile("[\\n]"));
        List<String> lines = new ArrayList<>();
        while(scanner.hasNext()) {
            lines.add(scanner.next());
        }
        return lines;
    }

    public static void showInputs(CostContext context) {
        System.out.println("\nInputs:");
        System.out.println(context.baseDeliveryCost + " " + context.noOfPackages);
        for(ShipmentItem item: context.shipmentItems) {
            System.out.println(item.packageId + " " + item.weight + " " + item.distance + " " + item.offerCode);
        }
    }

    public static void showResults(CostContext context) {
        if(context.costItems.size() > 0)
            System.out.println("\nOutputs:");
        for(CostItem item: context.costItems) {
            System.out.println(item.packageId + " " + item.discount + " " + item.totalCost);
        }
        if(context.costItems.size() > 0)
            System.out.println();
    }

    public static void saveOutput(String filename, CostContext context) throws IOException {
        FileWriter fw = new FileWriter(filename);
        List<CostItem> costItems = context.costItems;
        for (CostItem item :costItems) {
            fw.write(item.packageId + " " + item.discount + " " + item.totalCost + "\n");
        }
        fw.close();
    }

    public static String getConfiguration() throws IOException, URISyntaxException {
        return Files.readString(Paths.get("Configuration.json"));
    }

    public static void log(boolean debug, String msg) {
        if(debug)
            System.out.println(msg);
    }
}
