import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * Write a description of class Animals here.
 *
 * @author Nour Ataya, Yinuo Xiang
 *
 */
public class Animals extends Alive
{
    // The age at which an animal can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which an animal can live.
    private static final int MAX_AGE = 5;
    // The likelihood of an animal breeding.
    private static final double BREEDING_PROBABILITY = 0.8;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single animal. In effect, this is the
    // number of steps an animal can go before it has to eat again.
    private static final int PLANT_FOOD_VALUE = 12;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The animal's food level, which is increased by eating plants.
    private int foodLevel;

    /**
     * Constructor for objects of class Animals
     */
    public Animals(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables
        super(field, location);
        int age = getAge();
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = PLANT_FOOD_VALUE;
        }
        else{
            age = 0;
            foodLevel = PLANT_FOOD_VALUE;
        }
    }

    /**
     * This is what the animal act: It move and breedin day time. 
     * They die because of hunger or old age.
     * @param newAnimals A list to return newly born animal.
     * @param day The day status of one day.
     * @param isRaining The weather of the field.
     */
    protected void act(List<Characters> newAnimals, boolean day,boolean isRaining){
        foodLevel = incrementHunger(foodLevel);
        if(isActive() == true && day == true){
            giveBirth(newAnimals);
            Location newLocation = findFood();
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());

            }
            else if(newLocation != null) {
                //setLocation(getField().freeAdjacentLocation(getLocation()));
                setLocation(newLocation);
            }
            else{
                setDead();
            }
        }
    }

    /**
     * Get the maximum age of human.
     * @return The maximum age of animal.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAnimals A list to return newly born animals.
     */
    private void giveBirth(List<Characters> newAnimals){
        if(isActive() == true) {
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed(BREEDING_PROBABILITY, MAX_LITTER_SIZE);

            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Animals young = new Animals(false, field, loc);
                newAnimals.add(young);
            }
        }
    }
    
    /**
     * Look for plants adjacent to the current location.
     * Only the first live plant is eaten.
     * @return null when plants was foundor if it wasn't.
     */
    private Location findFood()
    {
        if(isActive() == true) {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object character = field.getObjectAt(where);
            if(character instanceof Plants) {
                Plants plant = (Plants) character;
                if(plant.isActive() == true) {
                    //plant.setDead();
                    plant.increaseEatenTimes();
                    foodLevel += PLANT_FOOD_VALUE;
                    return null;
                }
            }
        }
    }
        return null;
    }
}

