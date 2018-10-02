import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * Write a description of class Female here.
 *
 * @author Nour Ataya, Yinuo Xiang
 *
 */
public class HumanFemale extends Human
{
    // The likelihood of a human breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    /**
     * Constructor for objects of class Female
     */
    public HumanFemale(boolean randomAge, Field field, Location location)
    {
       super(randomAge, field, location); 
    }
    
    /**
     * Check if a female meet a male.
     * @return True if female meet a male, false if not.
     */
    protected boolean meet()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object character = field.getObjectAt(where);
            if(character instanceof HumanMale) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Female give birth if she meet a male, the number of birth could be 0.
     * @param newHuman A list to return newly born human.
     */
     protected void giveBirth(List<Characters> newHuman){
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(BREEDING_PROBABILITY, MAX_LITTER_SIZE);

        if (meet() == true){
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                if(generateGender().equals("male")) {
                    HumanMale young = new HumanMale(false, field, loc);
                    newHuman.add(young);
                }else{ 
                    HumanFemale young = new HumanFemale(false, field, loc);
                    newHuman.add(young);
                }
            }
        }
    }
}
