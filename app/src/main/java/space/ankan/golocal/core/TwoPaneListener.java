package space.ankan.golocal.core;

import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.model.kitchens.Kitchen;

/**
 * Created by ankan.
 * Methods an activity or fragment needs to support to enable two panes on tablet devices
 */

public interface TwoPaneListener {

    void setupKitchenDetail(Kitchen kitchen);

    void setupChatDetail(String channelId, String channelName, String userId);

    void setupManageDishView(Dish dish);

    @SuppressWarnings("unused")
    void setupStartKitchenTab();

    boolean isTwoPane();

}
