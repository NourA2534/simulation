import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Write a description of class ZombieHuman here.
 *
return null;
}
 * @author Nour Ataya, Yinuo Xiang
 * 
 */
public class ZombieHuman extends Zombie
{
    // The age to which a zombie human can live.
    private static final int MAX_AGE = 7;
    // The probability that zombie human change killed human into zombie.
    private static final double SPREAD_PROBABILITY = 0.25;
    // The probabality that zombie human kill a human.
    private static final double KILL_PROBABILITY = 0.6;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Constructor for objects of class ZombieHuman
     */
    public ZombieHuman(Field field, Location location)
    {
        super(field, location);
    }

    /**
     * This is what the zombie human act: It move in night time and is 
     * not raining. It has probability to transform a killed human into
     * zombie human.
     * @param newZombie A list to return newly born zombie human.
     * @param day The day status of one day.
     * @param isRaining The weather of the field.
     */
    protected void act(List<Characters> newZombie, boolean day, boolean isRaining)
    {
        if(isActive() == true && day == false && isRaining == false){
            Location newLocation = findFood(newZombie);
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            else if(newLocation != null) {
                if(spreadDisease() <= SPREAD_PROBABILITY) {
                    giveBirth(newZombie, newLocation);
                    //setLocation(getField().randomAdjacentLocation(getLocation()));
                }else{
                    setLocation(newLocation);
                }
            }
        }
    }

    /**
     * Get the maximum age of zombie human.
     * @return The maximum age of zombie human.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * Zombie human givebirth.
     * @param newZombie A list to return newly born zombie human.
     * @param location The location of the newly born zombie human.
     */
    public void giveBirth(List<Characters> newZombie, Location location){
        Field field = getField();
        ZombieHuman young = new ZombieHuman(field, location);
        newZombie.add(young);
    }

    /**
     * Look for human adjacent to the current location.
     * Only the first live human could be killed.
     * @param newZombie A list to return newly born zombie human.
     * @return Where human was found, or null if it wasn't.
     */
    private Location findFood(List<Characters> newZombie) 
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object characters = field.getObjectAt(where);
            if(characters instanceof HumanMale) {
                HumanMale malehuman = (HumanMale) characters;
                if(malehuman.isActive() == true && rand.nextDouble() <= KILL_PROBABILITY) {
                    malehuman.setDead();

                    return where;
                }
            }
            if(characters instanceof HumanFemale) {
                HumanFemale femalehuman = (HumanFemale) characters;
                if(femalehuman.isActive() == true  && rand.nextDouble() <= KILL_PROBABILITY) {
                    femalehuman.setDead();

                    return where;
                }
            }
        }
        return null;
    }
}
