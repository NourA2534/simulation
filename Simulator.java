import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing zombies and alive speices
 * 
 * @author Nour Ataya, Yinuo Xiang
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a zombie human will be created in any given grid position.
    private static final double ZOMBIEHUMAN_CREATION_PROBABILITY = 0.01;
    // The probability that a zombie animals will be created in any given grid position.
    private static final double ZOMBIEANIMALS_CREATION_PROBABILITY = 0.02;
    // The probability that a human will be created in any given grid position.
    private static final double HUMAN_CREATION_PROBABILITY = 0.09;
    // The probability that a animals will be created in any given grid position.
    private static final double ANIMALS_CREATION_PROBABILITY = 0.07;
    // The probability that a plant will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.03;

    // List of characters in the field.
    private List<Characters> characters;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // The current weather of the simulation.
    private boolean isRaining;

    // The current step of timeflow of a day period.
    private int timeFlow;
    // The length of a day period.
    private static final int lengthOfDay = 10;
    // The crrent step of a whole day period.
    private int interval;
    // The length of a whole day period.
    private static final int timeInterval = 20;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        characters = new ArrayList<>();
        field = new Field(depth, width);

        // State time-flow as 0.
        timeFlow = 0;
        // State interval as 0.
        interval = 0;
        // Set the weather as no raining.
        isRaining = false;

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(ZombieAnimals.class, Color.RED);
        view.setColor(ZombieHuman.class, Color.BLUE);
        view.setColor(Animals.class, Color.YELLOW);
        view.setColor(Plants.class, Color.GREEN);
        view.setColor(HumanMale.class, Color.CYAN);
        view.setColor(HumanFemale.class, Color.PINK);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(60);   // run more slowly
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * character.
     */
    public void simulateOneStep()
    {
        //Increase the time step fields.
        step++;
        timeFlow++;
        interval++;
        // Check if it is the end of one whole day.
        boolean isDayEnd = oneDayEnd();
        // Check if need to change the day status.
        changeBetweenNightAndDay();

        Random rand = Randomizer.getRandom();
        // Generate a random boolean to set the weather.
        isRaining = rand.nextBoolean();

        // Provide space for newborn animals.
        List<Characters> newCharacters = new ArrayList<>();
        // Let all characters act.
        for(Iterator<Characters> it = characters.iterator(); it.hasNext(); ) {
            Characters character = it.next();

            character.act(newCharacters, view.isDayTime(), isRaining);
            // If one whole day end, characters increase age.
            if(isDayEnd == true) {
                character.incrementAge(character.getMaxAge());
            }

            if(! character.isActive()) {
                it.remove();
            }
        }

        // Add the newly born foxes and rabbits to the main lists.
        characters.addAll(newCharacters);

        view.showStatus(step, field, isRaining);
    }

    /**
     * Return the weather status, return true if is raining.
     * @return The weather is raining, false if not.
     */
    private boolean getWeather(){
        return isRaining;
    }

    /**
     * Reset the time flow of a day period.
     */
    private void resetTimeFlow(){
        timeFlow = 0;
    }

    /**
     * Change the day status between day and night.
     */
    private void changeBetweenNightAndDay(){
        if (timeFlow == lengthOfDay){
            view.changeEmptyColor();
            resetTimeFlow();
        }
    }

    /**
     * Return the boolean value if it is the end of whole day.
     * @return The true value if it is whole day end, false if not.
     */
    private boolean oneDayEnd()
    {
        interval ++;
        if(interval == timeInterval) {

            resetInterval();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Reset the interval of a whole day period.
     */
    private void resetInterval()
    {
        interval = 0;
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        resetInterval();
        resetTimeFlow();
        view.resetEmptyColor();

        isRaining = false;

        characters.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field, isRaining);
    }

    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= ZOMBIEHUMAN_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    ZombieHuman zombieHuman = new ZombieHuman(field, location);
                    characters.add(zombieHuman);
                }
                else if(rand.nextDouble() <= ZOMBIEANIMALS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    ZombieAnimals zombieAnimals = new ZombieAnimals(field, location);
                    characters.add(zombieAnimals);
                }
                else if(rand.nextDouble() <= ANIMALS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Animals animals = new Animals(true, field, location);
                    characters.add(animals);
                }
                else if(rand.nextDouble() <= HUMAN_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    HumanMale human = new HumanMale(true, field, location);

                    characters.add(human);
                }
                else if(rand.nextDouble() <= HUMAN_CREATION_PROBABILITY)
                {
                    Location location = new Location(row,col);
                    HumanFemale human = new HumanFemale(true, field, location);
                    characters.add(human);
                }
                else if(rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plants plants = new Plants(true, field, location);
                    characters.add(plants);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int milliesec)
    {
        try {
            Thread.sleep(milliesec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
