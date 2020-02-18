package ua.org.algoritm.terminal.ConnectTo1c;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.OrderOutfit;

public class JsonParser {

    public static ArrayList<OrderOutfit> getOrderOutfit(String response) throws JSONException {
        ArrayList<OrderOutfit> mOrderOutfits = new ArrayList<>();
        JSONArray orders = new JSONArray(response);

        for (int i = 0; i < orders.length(); i++) {
            OrderOutfit orderOutfit = new OrderOutfit();
            JSONObject order = new JSONObject(orders.get(i).toString());

            orderOutfit.setID(order.getString("ID"));
            orderOutfit.setDescription(order.getString("Description"));
            orderOutfit.setResponsibleID(order.getString("ResponsibleID"));
            orderOutfit.setResponsible(order.getString("Responsible"));
            orderOutfit.setStateID(order.getString("StateID"));
            orderOutfit.setState(order.getString("State"));

            JSONArray CarDataOutfits = new JSONArray(order.getString("CarDataOutfit"));
            ArrayList<CarDataOutfit> mCarDataOutfits = new ArrayList<>();
            for (int j = 0; j < CarDataOutfits.length(); j++) {
                JSONObject car = new JSONObject(CarDataOutfits.get(j).toString());

                CarDataOutfit mCar = new CarDataOutfit();
                mCar.setCarID(car.getString("CarID"));
                mCar.setCar(car.getString("Car"));
                mCar.setSectorID(car.getString("SectorID"));
                mCar.setSector(car.getString("Sector"));
                mCar.setRow(car.getString("Row"));
               // mCar.setBarCode(car.getString("BarCode"));

                mCarDataOutfits.add(mCar);
            }
            orderOutfit.setCarDataOutfit(mCarDataOutfits);
        }
        return mOrderOutfits;
    }

}
