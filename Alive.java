import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Zombies age, move, breed, and die.
 * 
 * @author Nour Ataya, Yinuo Xiang
 * @version 2016.02.29 (2)
 */
public abstract class Alive extends Characters
{
    // Characteristics shared by all Zombies (class variables).

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The gender of alive speices.
    private String Gender;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Alive(Field field, Location location)
    {
        super(field, location);
    }
    
    /**
     * Make this alive species more hungry. This could result in the species's death.
     * @param foodLevel The food level of alive species.
     */
    protected int incrementHunger(int foodLevel)
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
        return foodLevel;
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed(double breedingProbability, int maxLitterSize)
    {
        int births = 0;
        if(rand.nextDouble() <= breedingProbability) {
            births = rand.nextInt(maxLitterSize) + 1;
        }
        return births;
    }

    /**
     * Generate the gender of birth.
     * @return The gender of new born.
     */
    protected  String generateGender()
    {

        if(rand.nextBoolean() == true) {
            Gender = "male";
        }else{
            Gender = "female";
        }
        return Gender;
    }

    /**
     * A alive species can breed if it has reached the breeding age.
     * @return true if the alive species can breed, false otherwise.
     */
    protected boolean canBreed(int breedingAge)
    {
        return getAge() >= breedingAge;
    }
}
