package config;

import logWriter.LogWriter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration
{
    private static final String SEED_KEY = "Seed";
    private static final String ANIMATE_KEY = "Animate";
    private static final String PLAYER0_KEY = "Player0";
    private static final String PLAYER1_KEY = "Player1";

    private static Configuration configuration = null;

    private int seed;
    private boolean animate;
    private String player0class;
    private String player1class;

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
        // System.out.println("#Seed: " + seed);
        logWriter.writeConfig(SEED_KEY, seed);

        /**
         * write into file instead print out in the console
         */
        // Animate
        animate = Boolean.parseBoolean(properties.getProperty(ANIMATE_KEY));
        //System.out.println("#Animate: " + animate);
        logWriter.writeConfig(ANIMATE_KEY, animate);

        /**
         * write into file instead print out in the console
         */
        // Player0
        player0class = properties.getProperty(PLAYER0_KEY);
        //System.out.println("#Player0: " + player0class);
        logWriter.writeConfig(PLAYER0_KEY, player0class);

        /**
         * write into file instead print out in the console
         */
        // Player1
        player1class = properties.getProperty(PLAYER1_KEY);
        //System.out.println("#Player1: " + player1class);
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

    public String getPlayer0class()
    {
        return player0class;
    }

    public String getPlayer1class()
    {
        return player1class;
    }
}
