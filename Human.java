import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * Write a description of class Human here.
 *
 * @author Nour Ataya, Yinuo Xiang
 * @version (a version number or a date)
 */
public abstract class Human extends Alive
{
    // The age to which a human can start to breed.
    private static final int BREEDING_AGE = 0;
    // The age to which a human can live.
    private static final int MAX_AGE = 80;
    // The probability that a alive species can breed male or female.
    private static final double BREEDING_GENDER_PROBABILITY = 0.5;
    // The probability to kill zombie animals.
    private static final double KILL_ZOMBIEANIMALS_PROBABILITY = 0.2;

    // The food value of a single human. In effect, this is the
    // number of steps a human can go before he/she has to eat again.
    private static final int PLANT_FOOD_VALUE = 14;
    private static final int ANIMALS_FOOD_VALUE = 20;

    // The human's food level, which is increased by eating plants and animals.
    private int foodLevel;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Human
     */
    public Human(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables
        super(field, location);
        int age = getAge();

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = ANIMALS_FOOD_VALUE;
        }
        else{
            age = 0;
            foodLevel = ANIMALS_FOOD_VALUE;
        }
    }

    /**
     * This is what the human act: They move in day time, breeding if 
     * there is one male and one female meet. They die because of hunger 
     * or old age.
     * @param newHuman A list to return newly born human.
     * @param day The day status of one day.
     * @param isRaining The weather of the field.
     */
    protected void act(List<Characters> newHuman, boolean day, boolean isRaining)
    {

        foodLevel = incrementHunger(foodLevel);

        if(isActive() == true && day == true){
            breeding(newHuman);

            Location newLocation = findFood();
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());

            }
            else if(newLocation != null) {
                //setLocation(getField().randomAdjacentLocation(getLocation()));
                setLocation(newLocation);
            }
            else{
                setDead();
            }
        }
    }

    /**
     * Human give birth if they reach the breeding age.
     * @param newHuman A list to return newly born human.
     */
    protected void breeding(List<Characters> newHuman) {
        if(canBreed(BREEDING_AGE) == true) {
            giveBirth(newHuman);
        }
    }

    /**
     * Get the maximum age of human.
     * @return The maximum age of human.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * Human givebirth.
     * @param newHuman A list to return newly born human.
     */
    abstract void giveBirth(List<Characters> newHuman);

    /**
     * Look for plants and animals adjacent to the current location.
     * Only the first live plant or aniaml is eaten.
     * Also look for zombie animals and kiil then.
     * @return Where animal was found or kill a zombie animal, 
     * or null if it wasn't or find a plant.
     */
    private Location findFood()
    {
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
            if(character instanceof Animals) {
                Animals animals = (Animals) character;
                if(animals.isActive() == true) {
                    animals.setDead();
                    foodLevel += ANIMALS_FOOD_VALUE;
                    return where;
                }
            }
            if(character instanceof ZombieAnimals) {
                ZombieAnimals zombieAnimals = (ZombieAnimals) character;
                if(zombieAnimals.isActive() == true && rand.nextDouble() <= KILL_ZOMBIEANIMALS_PROBABILITY)
                {
                    zombieAnimals.setDead();
                    return where;
                }
            }

        }
        return null;
    }
}

