package com.everest.courier;


public class Application {

    public static void main(String[] args) {
        if(args == null) {
            Utilities.showError("Null Argument");
            Utilities.exit(-1);
            return;
        }
        else if(args.length == 0) {
            Utilities.showError("Empty Argument");
            Utilities.showHelp();
            Utilities.exit(-1);
            return;
        }

        int end = args.length;
        String iFileName = null, oFileName = null;

        for(int i = 0; i < end; i++) {
            String arg = args[i];
            if(arg.charAt(0) == '-') {
                switch(arg) {
                    case "-i":
                        iFileName = "";
                        break;
                    case "-o":
                        oFileName = "";
                        break;
                    default:
                        Utilities.showError("Invalid Argument");
                        Utilities.showHelp();
                        Utilities.exit(-1);
                        return;
                }

            }
        }

        if(iFileName == null || oFileName == null) {
            Utilities.showError("Missing Argument");
            Utilities.showHelp();
            Utilities.exit(-1);
            return;
        }

        Utilities.exit(0);
    }
}
