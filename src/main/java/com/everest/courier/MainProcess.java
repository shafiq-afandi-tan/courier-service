package com.everest.courier;

import com.everest.courier.Exceptions.*;

import java.util.List;

public class MainProcess implements Process {
    private Factory factory;
    private Wrapper wrapper;
    private Parser parser;
    private String[] args;

    
    public MainProcess(Factory factory, String[] args) {
        this.factory = factory;
        this.wrapper = factory.getWrapper();
        this.parser = factory.getParser();
        this.args = args;
    }

    @Override
    public void run(Context context) {
        try {
            if(args == null)
                throw new CmdLineArgument("Null Argument");
            else if(args.length == 0)
                throw new CmdLineArgument("Empty Argument");

            ServiceType serviceType;

            try {
                serviceType = ServiceType.valueOf(args[0].toUpperCase());
            }
            catch (Exception e) {
                throw new CmdLineArgument("Invalid Argument");
            }

            int end = args.length;
            String iFileName = null, oFileName = null, lastArg = null;
            boolean debug = false;

            for(int i = 1; i < end; i++) {
                String arg = args[i];
                if(arg.charAt(0) == '-') {
                    switch(arg) {
                        case "-i":
                            iFileName = "";
                            break;
                        case "-o":
                            oFileName = "";
                            break;
                        case "-d":
                            debug = true;
                            break;
                        default:
                            throw new CmdLineArgument("Invalid Argument");
                    }
                }
                else if(lastArg != null){
                    switch (lastArg) {
                        case "-i":
                            iFileName = arg;
                            break;
                        case "-o":
                            oFileName = arg;
                            break;
                    }
                }
                lastArg = arg;
            }

            if(iFileName == null || oFileName == null)
                throw new CmdLineArgument("Missing Argument");
            else if(iFileName.length() == 0 || oFileName.length() == 0)
                throw new CmdLineArgument("Missing Argument Value");

            wrapper.log(debug, "Get input lines");
            List<String> lines = wrapper.getInputLines(iFileName);
            wrapper.log(debug, "Get context");
            Process process = factory.getProcess(serviceType);
            context = factory.getContext(serviceType, lines);
            wrapper.log(debug, "Get configuration string");
            String strConfig = wrapper.getConfiguration();
            wrapper.log(debug, "Build configuration");
            parser.buildConfiguration(context, strConfig);
            wrapper.log(debug, "Show inputs");
            wrapper.showInputs(context);
            wrapper.log(debug, "Run process");
            process.run(context);
            wrapper.log(debug, "Show results");
            wrapper.showResults(context);
            wrapper.log(debug, "Save output");
            wrapper.saveOutput(oFileName, context);
            wrapper.exit(0);
        }
        catch (Exception e) {
            String msg = e.getMessage();
            if(msg != null && (msg.equals("Empty Argument") || msg.equals("Null Argument"))) {
                wrapper.showVersion();
                wrapper.showCopyRight();
                wrapper.showHelp();
            }
            else if(e instanceof GeneralException) {
                wrapper.showError(e.getMessage());
                wrapper.showHelp();
            }
            else
                wrapper.showError(e);
            wrapper.exit(-1);
        }
    }
}
