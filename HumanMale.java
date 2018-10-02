import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * Write a description of class Male here.
 *
 * @author Nour Ataya, Yinuo Xiang
 *
 */
public class HumanMale extends Human
{
    /**
     * Constructor for objects of class Male
     */
    public HumanMale(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
    }

    /**
     * Check if a male meet a female.
     * @return True if male meet a female, false if not.
     */
    protected boolean meet()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object character = field.getObjectAt(where);
            if(character instanceof HumanFemale) {
                return true;
            }
        }
        return false;
    }
    
    protected void giveBirth(List<Characters> newHuman) {
        
    }
   
}
