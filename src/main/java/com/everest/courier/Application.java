package com.everest.courier;


import com.everest.courier.Exceptions.*;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        try {
            if(args == null)
                throw new CmdLineArgumentNull();
            else if(args.length == 0)
                throw new CmdLineArgumentEmpty();

            String contextName = args[0];

            if(!contextName.equals("cost") && !contextName.equals("time"))
                throw new CmdLineArgumentInvalid();

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
                            throw new CmdLineArgumentInvalid();
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
                throw new CmdLineArgumentMissing();
            else if(iFileName.length() == 0 || oFileName.length() == 0)
                throw new CmdLineArgumentValueMissing();

            Wrapper.log(debug, "Get input lines");
            List<String> lines = Wrapper.getInputLines(iFileName);
            Wrapper.log(debug, "Parse cost context");
            CostContext context = Parser.parseCostContext(lines);
            context.name = contextName;
            Wrapper.log(debug, "Get configuration string");
            String strConfig = Wrapper.getConfiguration();
            Wrapper.log(debug, "Build configuration");
            Parser.buildConfiguration(context, strConfig);
            Wrapper.log(debug, "Show inputs");
            Wrapper.showInputs(context);
            Wrapper.log(debug, "Run process");
            Factory.getProcess(contextName).run(context);
            Wrapper.log(debug, "Show results");
            Wrapper.showResults(context);
            Wrapper.log(debug, "Save output");
            Wrapper.saveOutput(oFileName, context);
            Wrapper.exit(0);
        }
        catch (Exception e) {
            if(e instanceof CmdLineArgumentNull || e instanceof CmdLineArgumentEmpty) {
                Wrapper.showVersion();
                Wrapper.showCopyRight();
                Wrapper.showHelp();
            }
            else if(e instanceof MyException) {
                Wrapper.showError(e.getMessage());
                Wrapper.showHelp();
            }
            else
                Wrapper.showError(e);
            Wrapper.exit(-1);
        }
    }
}
