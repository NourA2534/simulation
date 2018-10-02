import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * Write a description of class Plants here.
 *
 * @author Nour Ataya, Yinuo Xiang
 * 
 */
public class Plants extends Characters
{

    // The age to which a plant can live.
    private static final int MAX_AGE = 5;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The plant spreading probability.
    private static final double SPREAD_PROBABILITY = 0.1;
    // The maximum of a plant can be spread.
    private static final int MAX_SPREAD_SIZE = 2;
    
    // The growing rate of a plant.
    private static final int GROWTH_INTERVAL = 20;
    // The current step of growth interval of a plant.
    private int growInterval;

    // The times a plants be eaten.
    private int eatenTimes;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Plants
     */
    public Plants(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables
        super(field, location);
        int age = getAge();
        eatenTimes = 0;
        growInterval = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    /**
     * This is what the plant does most of the time: it grows in day time 
     * and is raining. It will die if it been eaten more then 2 times 
     * or old age. It will spread anywhere once end one whole day.
     * @param newPlants A list to return newly born plants.
     * @param day The day status of one day.
     * @param isRaining The weather of the field.
     */
    protected void act(List<Characters> newPlants, boolean day, boolean isRaining)
    {
        increaseGrowInterval();

        if(eatenTimes > 2 ) {
            setDead();
        }

        if(isActive() == true && day == true && isRaining == true)
        { 
            Grow(newPlants);
           
            if(growInterval == GROWTH_INTERVAL) 
            {  

                spread(newPlants);
                resetGrowInterval();
            }

        }
    }

    /**
     * Get the max age of a plant.
     * @return The max age of plant.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * Reset the grow interval.
     */
    private void resetGrowInterval()
    {
        growInterval = 0;
    }

    /**
     * Increase the eaten time of plant.
     */
    public void increaseEatenTimes()
    {
        eatenTimes++;
    }

    /**
     * Increase the grow interval.
     */
    private void increaseGrowInterval(){
        growInterval++;
    }

    /**
     * Spread the plant randomly every growth interval, the number of birth
     * could be 0.
     * @param newPlants A list to return newly born plants.
     */
    private void spread(List<Characters> newPlants)
    {
        if(isActive() == true) {
            Field field = getField();
            int births = rand.nextInt(MAX_SPREAD_SIZE) + 1;

            for(int b = 0; b < births ; b++) {
                if(field.getRandomFreeLocation() != null) {
                    Location loc = field.getRandomFreeLocation();
                    Plants young = new Plants(false, field,loc);
                    newPlants.add(young);
                }
            }
        }
    }

    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAlives A list to return newly born Alives.
     */
    private void Grow(List<Characters> newPlants)
    {
        // New Alives are born into adjacent locations.
        // Get a list of adjacent free locations.
        if(isActive() == true) {
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = rand.nextInt(MAX_LITTER_SIZE) + 1;

            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Plants young = new Plants(false, field, loc);
                newPlants.add(young);
            }
        }
    }
}
