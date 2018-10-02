import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a aliveSpecies.
 * aliveSpecieses age, move, eat rabbits, and die.
 * 
 * @author Nour Ataya, Yinuo Xiang
 * @version 2016.02.29 (2)
 */
public abstract class Zombie extends Characters
{
    // charactersistics shared by all aliveSpecieses (class variables).

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    

    // Whether the character is alive or not.

    /**
     * Create a aliveSpecies. A aliveSpecies can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the aliveSpecies will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Zombie(Field field, Location location)
    {
        super(field, location);
    }

    /**
     * Generate a random double value to decide if can spread disease.
     */
    protected double spreadDisease()
    {
        return rand.nextDouble();
    }
}
