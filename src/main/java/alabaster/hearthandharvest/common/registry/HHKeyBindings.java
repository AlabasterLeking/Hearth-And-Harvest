package alabaster.hearthandharvest.common.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class HHKeyBindings {

    public static final KeyMapping POOP = new KeyMapping(
            "key.hearthandharvest.poop",
            KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT,
            InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_P),
            "key.categories.hearthandharvest"
    );
}