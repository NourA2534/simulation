import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Write a description of class ZombieAnimals here.
 *
 * @author Nour Ataya, Yinuo Xiang
 * 
 */
public class ZombieAnimals extends Zombie
{
    // The age to which a zombie animal can live.
    private static final int MAX_AGE = 5;
    // The probability that zombie animal change killed animal into zombie.
    private static final double SPREAD_PROBABILITY = 0.7;
    
    /**
     * Constructor for objects of class ZombieAnimals
     */
    public ZombieAnimals(Field field, Location location)
    {
        super(field, location);
    }

    /**
     * This is what the zombie animal act: It move in night time and is 
     * not raining. It has probability to transform a killed animal into
     * zombie animal.
     * @param newZombie A list to return newly born zombie animal.
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
     * Zombie animal givebirth.
     * @param newZombie A list to return newly born zombie animal.
     * @param location The location of the newly born zombie animal.
     */
    private void giveBirth(List<Characters> newZombie, Location location){
        if(isActive() == true) {
            Field field = getField();
            ZombieAnimals young = new ZombieAnimals(field, location);
            newZombie.add(young);
        }
    }

    /**
     * Look for animals and zombie human adjacent to the current location.
     * Only the first live animal or zombie human could be killed.
     * @param newZombie A list to return newly born zombie animal.
     * @return Where animal was found, or null if it wasn't or find a
     * zombie human.
     */
    private Location findFood(List<Characters> newZombie) 
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object characters = field.getObjectAt(where);

            if(characters instanceof Animals) {
                Animals animal = (Animals) characters;
                if(animal.isActive() == true) {
                    animal.setDead();
                    return where;
                }
            }

            if(characters instanceof ZombieHuman) {
                ZombieHuman zombieHuman = (ZombieHuman) characters;

                if(zombieHuman.isActive() == true) {
                    zombieHuman.setDead();
                }
            }

        }
        return null;
    }
}
