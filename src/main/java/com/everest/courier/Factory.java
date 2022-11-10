package com.everest.courier;

import com.everest.courier.Exceptions.EverestException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Factory {
    public Process getProcess(ServiceType type) {
        if(type == ServiceType.COST)
            return new CostProcess(new Factory());
        else if(type == ServiceType.TIME)
            return new TimeProcess(new Factory());
        return null;
    }

    public Wrapper getWrapper() {
        return new Wrapper();
    }

    public Parser getParser() {
        return new Parser();
    }

    public Context getContext(ServiceType type, List<String> lines) throws EverestException {
        Parser parser = new Parser();
        if(type == ServiceType.COST)
            return parser.parseCostContext(lines);
        else if(type == ServiceType.TIME)
            return parser.parseTimeContext(lines);
        return null;
    }

    public ResultItem getResultItem(String packageId, BigDecimal discount, BigDecimal totalCost) {
        return new ResultItem(packageId, discount, totalCost);
    }

    public Utilities getUtilities() {
        return new Utilities();
    }
}
