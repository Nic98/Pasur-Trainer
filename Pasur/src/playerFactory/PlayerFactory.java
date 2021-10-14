package playerFactory;

import pasur.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Factory&Singleton pattern used.
 * PlayerFactory for creating different class of players,
 * not only the RandomPlayer class (SmartPlayer, LazyPlayer, humanPlayer etc.)
 *
 * Also, the PlayerFactory exist only by itself and is unmodifiable,
 * therefore, we make it as a Singleton.
 */
public class PlayerFactory {

    /**
     * Make a Singleton playerFactory
     */
    private static PlayerFactory playerFactory = null;

    private PlayerFactory() {};

    public static PlayerFactory getPlayerFactoryInstance() {
        if (playerFactory == null) {
            playerFactory = new PlayerFactory();
        }
        return playerFactory;
    }

    /**
     * Method for creating different class of Players
     * @param clazz class name of the new player
     * @return the class Player Object
     */
    public Player createNewPlayer(Class clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (Player) clazz.getConstructor(int.class).newInstance(0);
    }
}
