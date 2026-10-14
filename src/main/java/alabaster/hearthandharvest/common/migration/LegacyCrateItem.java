package alabaster.hearthandharvest.common.migration;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class LegacyCrateItem extends Item {
    private static final int CRATE_STACK_SIZE = 16;

    private final Supplier<ItemStack> content;

    public LegacyCrateItem(Supplier<ItemStack> content) {
        super(new Item.Properties());
        this.content = content;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !(entity instanceof Player player)) return;
        int count = stack.getCount();
        int kept = Math.min(count, CRATE_STACK_SIZE);
        player.getInventory().setItem(slot, LegacyCrates.filledCrateItem(content, kept));
        int remaining = count - kept;
        while (remaining > 0) {
            int batch = Math.min(remaining, CRATE_STACK_SIZE);
            ItemStack extra = LegacyCrates.filledCrateItem(content, batch);
            if (!player.getInventory().add(extra)) player.drop(extra, false);
            remaining -= batch;
        }
    }
}
