package com.everest.courier;

import com.everest.courier.Exceptions.MyException;

import java.math.BigDecimal;
import java.util.List;

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

    public Context getContext(ServiceType type, List<String> lines) throws MyException {
        Parser parser = new Parser();
        if(type == ServiceType.COST)
            return parser.parseCostContext(lines);
        else if(type == ServiceType.TIME)
            return parser.parseTimeContext(lines);
        return null;
    }

    public ResultItem getResultItem(ServiceType type, String packageId, BigDecimal discount, BigDecimal totalCost) {
        if(type == ServiceType.COST)
            return new CostItem(packageId, discount, totalCost);
        else if(type == ServiceType.TIME)
            return new TimeItem(packageId, discount, totalCost, null);
        return null;
    }

    public Utilities getUtilities() {
        return new Utilities();
    }

    public TimeEstimator getTimeEstimator(int vehicleSpeed) {
        return new TimeEstimator(vehicleSpeed);
    }
}
