package controller.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import model.Address;

public class AddressDeserializer implements ObjectDeserializer {
    public AddressDeserializer() {}

    @Override
    public Address objectDeserialize(JsonElement json) {
        Address address = new Gson().fromJson(json, Address.class);
        return address;
    }
}
