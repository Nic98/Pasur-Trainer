package playerFactory;

import pasur.Player;

import java.lang.reflect.InvocationTargetException;

public class PlayerFactory {

    private static PlayerFactory playerFactory = null;

    private PlayerFactory() {};

    public static PlayerFactory getPlayerFactoryInstance() {
        if (playerFactory == null) {
            playerFactory = new PlayerFactory();
        }
        return playerFactory;
    }

    public Player createNewPlayer(Class clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (Player) clazz.getConstructor(int.class).newInstance(0);
    }
}
