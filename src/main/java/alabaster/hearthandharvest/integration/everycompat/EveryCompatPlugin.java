package alabaster.hearthandharvest.integration.everycompat;

import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;

public class EveryCompatPlugin {
    public static void register(String modId) {
        EveryCompatAPI.registerModule(new HHEveryCompatModule(modId));
    }
}
