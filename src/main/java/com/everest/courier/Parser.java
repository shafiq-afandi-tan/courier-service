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
    private void buildContext(Context context, List<String> lines) throws UserInputException {
        if(lines.size() == 0)
            throw new UserInputException("User Input Empty");

        String line = lines.get(0).trim();

        if (line.length() == 0)
            throw new UserInputException("Missing field - base_delivery_cost");

        String[] parts = line.split(" ");

        try {
            context.baseDeliveryCost = new BigDecimal(parts[0]);
        }
        catch(NumberFormatException e) {
            throw new UserInputException("Invalid field - base_delivery_cost", e);
        }

        if(parts.length == 1)
            throw new UserInputException("Missing field - no_of_packages");

        try {
            context.noOfPackages = Integer.parseInt(parts[1]);
        }
        catch(NumberFormatException e) {
            throw new UserInputException("Invalid field - no_of_packages", e);
        }

        if(lines.size() == 1)
            throw new UserInputException("Missing field - package_id");

        for(int i = 1; i <= context.noOfPackages; i++) {
            line = lines.get(i).trim();

            if(line.length() == 0)
                throw new UserInputException("Missing field - package_id");

            parts = line.split(" ");
            ShipmentItem item = new ShipmentItem();
            item.packageId = parts[0];

            if(parts.length == 1)
                throw new UserInputException("Missing field - package_weight");

            try {
                item.weight = Integer.parseInt(parts[1]);
            }
            catch(NumberFormatException e) {
                throw new UserInputException("Invalid field - package_weight");
            }

            if(parts.length == 2)
                throw new UserInputException("Missing field - distance");

            try {
                item.distance = Integer.parseInt(parts[2]);
            }
            catch(NumberFormatException e) {
                throw new UserInputException("Invalid field - distance");
            }

            if(parts.length >= 4)
                item.offerCode = parts[3];

            context.shipmentItems.add(item);
        }
        if(context.noOfPackages < (lines.size() - 1)) {
            line = lines.get(context.noOfPackages);
            parts = line.split(" ");
        }
    }

    public Context parseCostContext(List<String> lines) throws UserInputException {
        Context context = new Context();
        context.type = ServiceType.COST;
        buildContext(context, lines);
        return context;
    }

    public Context parseTimeContext(List<String> lines) throws UserInputException {
        Context context = new Context();
        context.type = ServiceType.TIME;
        buildContext(context, lines);
        if(context.noOfPackages == (lines.size() - 1))
            throw new UserInputException("Missing field - no_of_vehicle");
        String line = lines.get(context.noOfPackages + 1).trim();
        if(line.length() == 0)
            throw new UserInputException("Missing field - no_of_vehicle");
        String[] parts = line.split(" ");
        try {
            context.noOfVehicle = Integer.parseInt(parts[0]);
        }
        catch (NumberFormatException e) {
            throw new UserInputException("Invalid field - no_of_vehicle", e);
        }
        if(parts.length == 1)
            throw new UserInputException("Missing field - vehicle_speed");
        try {
            context.vehicleSpeed = Integer.parseInt(parts[1]);
        }
        catch(NumberFormatException e) {
            throw new UserInputException("Invalid field - vehicle_speed", e);
        }
        if(parts.length == 2)
            throw new UserInputException("Missing field - vehicle_capacity");
        try {
            context.vehicleCapacity = Integer.parseInt(parts[2]);
        }
        catch(NumberFormatException e) {
            throw new UserInputException("Invalid field - vehicle_capacity", e);
        }
        for(int i = 0; i < context.noOfVehicle; i++) {
            context.vehicles.add(new ShippingVehicle(String.format("%02d", i + 1), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
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
