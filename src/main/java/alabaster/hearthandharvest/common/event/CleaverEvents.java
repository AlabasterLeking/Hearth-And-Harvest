package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.common.tag.HHVanillaTags;
import java.util.UUID;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.item.CleaverItem;
import alabaster.hearthandharvest.common.tag.HHModTags;
import alabaster.hearthandharvest.common.entity.cleaver.ThrownCleaver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class CleaverEvents {

    private static final java.util.WeakHashMap<LivingEntity, Boolean> CLEAVER_KILL = new java.util.WeakHashMap<>();

    private static final UUID CLEAVER_SPEED_ID = UUID.fromString("5b0f8e43-6a2d-4f61-9a3e-2c7a1d0e8b51");

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        if (!(event.getSource().getEntity() instanceof Player player))
            return;

        Entity direct = event.getSource().getDirectEntity();
        boolean cleaverKill = direct instanceof ThrownCleaver
                || (direct == player && player.getMainHandItem().getItem() instanceof CleaverItem);

        if (cleaverKill) {
            CLEAVER_KILL.put(target, true);
        }
    }

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        LivingEntity target = event.getEntity();

        if (CLEAVER_KILL.remove(target) == null)
            return;

        // Only affect entities in the tag
        if (!target.getType().is(HHModTags.CAN_BE_BUTCHERED))
            return;

        // Remove non-meat drops
        event.getDrops().removeIf(drop -> !drop.getItem().is(HHVanillaTags.MEAT));

        // If there are no meat drops left, nothing more to do
        if (event.getDrops().isEmpty())
            return;

        // Add bonus meat
        int meatCount = event.getDrops().stream()
                .mapToInt(e -> e.getItem().getCount())
                .sum();

        int bonus = Math.max(0, (int)Math.ceil(meatCount * 0.5));

        ItemStack firstMeat = event.getDrops().stream()
                .findFirst()
                .map(e -> e.getItem().copy())
                .orElse(ItemStack.EMPTY);

        if (!firstMeat.isEmpty()) {
            firstMeat.setCount(bonus);
            target.spawnAtLocation(firstMeat);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Player player = event.player;
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;
        boolean compensating = player.isUsingItem()
                && player.getUseItem().getItem() instanceof CleaverItem
                && !player.isPassenger()
                && !player.isInWater()
                && !player.isSwimming();

        if (compensating) {
            if (attr.getModifier(CLEAVER_SPEED_ID) == null)
                attr.addTransientModifier(new AttributeModifier(CLEAVER_SPEED_ID, "Cleaver charge speed", 4.0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else {
            attr.removeModifier(CLEAVER_SPEED_ID);
        }
    }
}
