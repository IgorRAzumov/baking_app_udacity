package udacity.com.baking_app.utils;

public final class Utils {
    private Utils() {
    }

    public static final int convertDpToPx(float dimension, float density) {
        return Math.round(dimension * density);
    }
}
