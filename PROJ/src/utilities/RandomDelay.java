package utilities;

import java.util.Random;

public class RandomDelay {

    public int getRandomDelay(){
        Random rnd = new Random();
        return rnd.nextInt(Constants.MAX_DELAY);
    }

}
