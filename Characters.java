import java.util.List;

/**
 * A class representing shared characteristics of characters.
 * 
 * @author Nour Ataya, Yinuo Xiang
 * @version 2016.02.29 (2)
 */
abstract public class Characters
{

    // The character's field.
    private Field field;
    // The character's position in the field.
    private Location location;
    // The character's living status.
    private boolean active;
    // The character's age.
    private int age;

    
    /**
     * Create a new character at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Characters(Field field, Location location)
    {
        this.field = field;
        setLocation(location);
        active = true;

    }

    /**
     * Make this character act - that is: make it do
     * whatever it wants/needs to do.
     * @param newcharacters A list to receive newly born characters.
     */
    abstract void act(List<Characters> characters, boolean day, boolean isRaining);

    /**
     * Check whether the character is alive or not.
     * @return true if the character is still alive.
     */
    protected boolean isActive()
    {
        return active;
    }
    
    /**
     * Get the age of character.
     * @return The age of character.
     */
    protected int getAge()
    {
        return age;
    }
    
    /**
     * Get the max age of character.
     * @return The max age of character.
     */
    abstract int getMaxAge();

    /**
     * Return the character's location.
     * @return The character's location.
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * Indicate that the character is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        active = false;

        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Increase the age.
     * This could result in the character's death.
     */
    protected void incrementAge(int maxAge) 

    {
        age++;
        if(age > maxAge) {
            setDead();

        }
    }
    

    /**
     * Place the character at the new location in the given field.
     * @param newLocation The character's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the character's field.
     * @return The character's field.
     */
    protected Field getField()
    {
        return field;
    }
}
