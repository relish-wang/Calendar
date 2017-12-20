package wang.relish.datepicker.sample;

import java.util.Random;

/**
 * @author Relish Wang
 * @since 2017/12/20
 */
class RandomUtils {

    public static float nextFloat(float start, float end) {
        Random random = new Random();
        return random.nextFloat() * (end - start) + start;
    }
}
