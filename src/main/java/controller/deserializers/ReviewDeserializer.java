package controller.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import model.Review;
import model.ShortReview;

import java.util.ArrayList;
import java.util.List;

public class ReviewDeserializer {
    public ReviewDeserializer() {}

    public List<ShortReview> reviewJsonDeserializer(JsonElement json) {
        List<ShortReview> reviews = new ArrayList<>();
        for (JsonElement review : json.getAsJsonArray()) {
            reviews.add(objectDeserialize(review));
        }

        return reviews;
    }

    public ShortReview objectDeserialize(JsonElement review) {
        return new Gson().fromJson(review, ShortReview.class);
    }
}
