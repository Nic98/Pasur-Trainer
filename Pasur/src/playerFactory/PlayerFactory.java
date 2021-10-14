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
     * @param index index of the player in the game e.g. 1st 2nd 3rd 4th ...
     * @param clazz class name of the new player
     * @return the class Player Object

     */
    public Player createNewPlayer(int index, Class clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (Player) clazz.getConstructor(int.class).newInstance(index);
    }
}
