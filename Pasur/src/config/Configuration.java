package config;

import logWriter.LogWriter;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Configuration
{
    private static final String SEED_KEY = "Seed";
    private static final String ANIMATE_KEY = "Animate";
    private static final String PLAYER0_KEY = "Player0";
    private static final String PLAYER1_KEY = "Player1";
    /**
     * WAITING FOR MORE PLAYERS:
     * E.g.
     *
     * private static final String PLAYER1_KEY = "Player2";
     * private String player2class;
     *
     * private static final String PLAYER1_KEY = "Player3";
     * private String player3class;
     * ... ...
     */

    private static Configuration configuration = null;

    private int seed;
    private boolean animate;
    private String player0class;
    private String player1class;

    /**
     * NEW ADDED ATTRIBUTE:
     * Keep all the names of player class in an array list
     * and create them all at once by the playerFactory
     */
    private ArrayList<String> playerClasses = new ArrayList<String>();

    /**
     * NEW ADDED ATTRIBUTES
     * logWriter: write logs in the "pasur.log" file
     */
    private LogWriter logWriter = LogWriter.getLogWriterInstance();

    public static Configuration getInstance()
    {
        if(configuration == null)
        {
            configuration = new Configuration();

            try {
                configuration.setUp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return configuration;
    }

    private void setUp() throws IOException 
    {
        // Default properties

        // Read properties
        Properties properties = new Properties();
        FileReader inStream = null;
        try {
            inStream = new FileReader("pasur.properties");
            properties.load(inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }

        /**
         * write into file instead print out in the console
         */
        // Seed
        seed = Integer.parseInt(properties.getProperty(SEED_KEY));
        logWriter.writeConfig(SEED_KEY, seed);

        /**
         * write into file instead print out in the console
         */
        // Animate
        animate = Boolean.parseBoolean(properties.getProperty(ANIMATE_KEY));
        logWriter.writeConfig(ANIMATE_KEY, animate);

        /**
         * write into file instead print out in the console
         */
        // Player0
        player0class = getPlayerClass(properties, PLAYER0_KEY);
        playerClasses.add(player0class);
        logWriter.writeConfig(PLAYER0_KEY, player0class);

        /**
         * write into file instead print out in the console
         */
        // Player1
        player1class = getPlayerClass(properties, PLAYER1_KEY);
        playerClasses.add(player1class);
        logWriter.writeConfig(PLAYER1_KEY, player1class);
    }

    public int getSeed()
    {
        return seed;
    }

    public boolean isAnimate()
    {
        return animate;
    }

    /**
     * UPDATED PART:
     * getPlayer0class
     * getPlayer1class
     * Two methods are now being combined to one single method
     * and adapt the situation that more players are joining the game
     *
     * //    public String getPlayer0class()
     * //    {
     * //        return player0class;
     * //    }
     * //
     * //    public String getPlayer1class()
     * //    {
     * //        return player1class;
     * //    }
     */


    /**
     * NEW ADDED METHOD:
     * @param properties the properties file which contains the information of the type of player
     * @param PlayerKey String of key name of the player
     * @return return a string of the class name of the player
     */
    public String getPlayerClass(Properties properties, String PlayerKey) {
        return properties.getProperty(PlayerKey);
    }

    /**
     * NEW ADDED METHOD:
     * @return A list of class name of the players
     */
    public ArrayList<String> getAllPlayerClasses() {
        return this.playerClasses;
    }
}
