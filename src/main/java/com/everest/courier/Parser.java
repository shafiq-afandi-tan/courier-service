package com.everest.courier;

import com.everest.courier.Exceptions.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private void buildContext(Context context, List<String> lines) throws MyException {
        String msg = "";
        try {
            if(lines.size() == 0)
                throw new UserInputEmpty();
            String line = lines.get(0).trim();
            if (line.length() == 0)
                throw new UserInputFieldMissingBaseDeliveryCost();
            String[] parts = line.split(" ");
            msg = "Invalid field - base_delivery_cost";
            context.baseDeliveryCost = new BigDecimal(parts[0]);
            if(parts.length == 1)
                throw new UserInputFieldMissingNoOfPackages();
            msg = "Invalid field - no_of_packages";
            context.noOfPackages = Integer.parseInt(parts[1]);
            if(lines.size() == 1)
                throw new UserInputFieldMissingPackageId();
            for(int i = 1; i <= context.noOfPackages; i++) {
                line = lines.get(i).trim();
                if(line.length() == 0)
                    throw new UserInputFieldMissingPackageId();
                parts = line.split(" ");
                ShipmentItem item = new ShipmentItem();
                item.packageId = parts[0];
                if(parts.length == 1)
                    throw new UserInputFieldMissingPackageWeight();
                msg = "Invalid field - package_weight";
                item.weight = Integer.parseInt(parts[1]);
                if(parts.length == 2)
                    throw new UserInputFieldMissingDistance();
                msg = "Invalid field - distance";
                item.distance = Integer.parseInt(parts[2]);
                if(parts.length >= 4)
                    item.offerCode = parts[3];
                context.shipmentItems.add(item);
            }
            if(context.noOfPackages < (lines.size() - 1)) {
                line = lines.get(context.noOfPackages);
                parts = line.split(" ");
            }
        } catch (Throwable e) {
            if(e instanceof MyException)
                throw e;
            else
                throw new UnknownException(msg, e);
        }
    }

    public Context parseCostContext(List<String> lines) throws MyException {
        CostContext context = new CostContext();
        buildContext(context, lines);
        return context;
    }

    public TimeContext parseTimeContext(List<String> lines) throws MyException {
        TimeContext context = new TimeContext();
        buildContext(context, lines);
        String msg = "";
        try {
            if(context.noOfPackages == (lines.size() - 1))
                throw new UserInputFieldMissingNoOfVehicle();
            String line = lines.get(context.noOfPackages + 1).trim();
            if(line.length() == 0)
                throw new UserInputFieldMissingNoOfVehicle();
            String[] parts = line.split(" ");
            msg = "Invalid field - no_of_vehicle";
            context.noOfVehicle = Integer.parseInt(parts[0]);
            if(parts.length == 1)
                throw new UserInputFieldMissingVehicleSpeed();
            msg = "Invalid field - vehicle_speed";
            context.vehicleSpeed = Integer.parseInt(parts[1]);
            if(parts.length == 2)
                throw new UserInputFieldMissingVehicleCapacity();
            msg = "Invalid field - vehicle_capacity";
            context.vehicleCapacity = Integer.parseInt(parts[2]);
            for(int i = 0; i < context.noOfVehicle; i++) {
                context.vehicles.add(new ShippingVehicle(String.format("%02d", i + 1), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
            }
        } catch (Throwable e) {
            if(e instanceof MyException)
                throw e;
            else
                throw new UnknownException(msg, e);
        }
        return context;
    }

    public void buildConfiguration(Context context, String value) throws ParseException {
        JSONObject config = (JSONObject) new JSONParser().parse(value);
        context.weightFactor = Integer.parseInt(config.get("weightFactor").toString());
        context.distanceFactor = Integer.parseInt(config.get("distanceFactor").toString());
        JSONArray arr = (JSONArray)config.get("offerCodes");
        List<OfferCode> offerCodes = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++) {
            JSONObject obj = (JSONObject)arr.get(i);
            OfferCode offerCode = new OfferCode();
            offerCode.code = (String)obj.get("code");
            Object val = obj.get("weightLowerLimit");
            if(val != null)
                offerCode.weightLowerLimit = Integer.parseInt(val.toString());
            val = obj.get("weightUpperLimit");
            if(val != null)
                offerCode.weightUpperLimit = Integer.parseInt(val.toString());
            val = obj.get("distanceLowerLimit");
            if(val != null)
                offerCode.distanceLowerLimit = Integer.parseInt(val.toString());
            val = obj.get("distanceUpperLimit");
            if(val != null)
                offerCode.distanceUpperLimit = Integer.parseInt(val.toString());
            val = obj.get("discount");
            if(val != null)
                offerCode.discountPercent = Integer.parseInt(val.toString());
            context.offerCodes.add(offerCode);
        }
    }
}
