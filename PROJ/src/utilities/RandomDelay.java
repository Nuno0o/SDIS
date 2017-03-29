package utilities;

import java.util.Random;

public class RandomDelay {

    public static int getRandomDelay(){
        Random rnd = new Random();
        return rnd.nextInt(Constants.MAX_DELAY);
    }

}
