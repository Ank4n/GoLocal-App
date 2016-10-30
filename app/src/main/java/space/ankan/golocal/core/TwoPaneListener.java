package space.ankan.golocal.core;

import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.model.kitchens.Kitchen;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public interface TwoPaneListener {

    void setupKitchenDetail(Kitchen kitchen);

    void setupChatDetail(String channelId, String channelName, String userId);

    void setupManageDishView(Dish dish);

    void setupStartKitchenTab();

    boolean isTwoPane();

}
