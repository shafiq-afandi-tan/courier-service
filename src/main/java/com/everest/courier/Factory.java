package com.everest.courier;

import com.everest.courier.Exceptions.MyException;

import java.util.List;

public class Factory {
    public Process getProcess(ServiceType type) {
        if(type == ServiceType.COST)
            return new CostProcess();
        else if(type == ServiceType.TIME)
            return new TimeProcess();
        return null;
    }

    public Wrapper getWrapper() {
        return new Wrapper();
    }

    public Parser getParser() {
        return new Parser();
    }

    public Context getContext(ServiceType type, List<String> lines) throws MyException {
        Parser parser = new Parser();
        if(type == ServiceType.COST)
            return parser.parseCostContext(lines);
        else if(type == ServiceType.TIME)
            return parser.parseTimeContext(lines);
        return null;
    }
}
