package udacity.com.baking_app.utils;

public class Utils {

    public static int convertDpToPx(float dimension, float density) {
        return Math.round(dimension * density);
    }
}
