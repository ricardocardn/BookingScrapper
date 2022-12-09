package controller.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import model.Address;
import model.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelDeserializer implements ObjectDeserializer{
    private static int id = 280;

    public HotelDeserializer() {}

    public List<Hotel> hotelsJsonDeserializer(String json) {
        List<Hotel> hotels = new ArrayList<>();
        JsonArray items = new Gson().fromJson(json, JsonArray.class);

        for (JsonElement item : items) {
            Hotel hotel = objectDeserialize(item);
            hotels.add(hotel);
        }
        return hotels;
    }

    public Hotel hotelJsonDeserializer(String json, boolean review) {
        Hotel hotel = new Hotel();
        JsonElement item = new Gson().fromJson(json, JsonElement.class);

        hotel.setId(id++);
        hotel.setName(item.getAsJsonObject().get("name").getAsString());
        hotel.setType(item.getAsJsonObject().get("type").getAsString());
        hotel.setTotalReviews(item.getAsJsonObject().get("totalReviews").getAsInt());
        hotel.setStars(item.getAsJsonObject().get("stars").getAsInt());
        hotel.setRating(item.getAsJsonObject().get("rating").getAsFloat());
        hotel.setAddress(new Address());

        if (review) hotel.setReviews(new ReviewDeserializer().reviewJsonDeserializer(item.getAsJsonObject().get("userReviews")));

        return hotel;
    }

    @Override
    public Hotel objectDeserialize(JsonElement item) {
        Hotel hotel = new Hotel();
        hotel.setId(id++);
        hotel.setName(item.getAsJsonObject().get("name").getAsString());
        Object address = new AddressDeserializer().objectDeserialize(item.getAsJsonObject().get("address"));
        hotel.setAddress((Address) address);
        hotel.setType(item.getAsJsonObject().get("type").getAsString());
        try {
            hotel.setStars(item.getAsJsonObject().get("stars").getAsInt());
            hotel.setRating(item.getAsJsonObject().get("rating").getAsFloat());
        } catch (Exception e) {}
        hotel.setTotalReviews(item.getAsJsonObject().get("reviews").getAsInt());
        hotel.setReviews(new ReviewDeserializer().reviewJsonDeserializer(item.getAsJsonObject().get("userReviews")));
        return hotel;
    }
}
