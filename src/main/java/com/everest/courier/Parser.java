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
    public static CostContext parseCostContext(List<String> lines) throws MyException {
        CostContext context = new CostContext();
        String msg = "";
        try {
            if(lines.size() == 0)
                throw new UserInputEmpty();
            String line = lines.get(0).trim();
            if (line.length() == 0)
                throw new UserInputFieldMissingPackageId();
            String[] parts = line.split(" ");
            msg = "Invalid field - base_delivery_cost";
            context.baseDeliveryCost = new BigDecimal(parts[0]);
            if(parts.length == 1)
                throw new UserInputFieldMissingNoOfPackages();
            msg = "Invalid field - no_of_packages";
            context.noOfPackages = Integer.parseInt(parts[1]);
            if(lines.size() == 1)
                throw new UserInputFieldMissingPackageInfo();
            for(int i = 1; i < lines.size(); i++) {
                line = lines.get(i).trim();
                if(line.length() == 0)
                    throw new UserInputFieldMissingPackageInfo();
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
        } catch (Throwable e) {
            if(e instanceof MyException)
                throw e;
            else
                throw new UnknownException(msg, e);
        }
        return context;
    }

    public static void buildConfiguration(CostContext context, String value) throws ParseException {
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
