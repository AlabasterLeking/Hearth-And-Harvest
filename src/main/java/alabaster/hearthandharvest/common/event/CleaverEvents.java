package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.item.CleaverItem;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class CleaverEvents {

    private static final java.util.WeakHashMap<LivingEntity, Boolean> CLEAVER_KILL = new java.util.WeakHashMap<>();

    private static final ResourceLocation CLEAVER_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "cleaver_charge_speed");

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        if (!(event.getSource().getEntity() instanceof Player player))
            return;

        ItemStack weapon = player.getMainHandItem();
        if (weapon.getItem() instanceof CleaverItem) {
            CLEAVER_KILL.put(target, true);
        }
    }

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        LivingEntity target = event.getEntity();

        // Only affect entities in the tag
        if (!target.getType().is(HHModTags.CAN_BE_BUTCHERED))
            return;

        if (!CLEAVER_KILL.containsKey(target))
            return;

        // Clean up marker
        CLEAVER_KILL.remove(target);

        // Remove non-meat drops
        event.getDrops().removeIf(drop -> !drop.getItem().is(ItemTags.MEAT));

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
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof CleaverItem) {
            if (!attr.hasModifier(CLEAVER_SPEED_ID))
                attr.addTransientModifier(new AttributeModifier(CLEAVER_SPEED_ID, 4.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            attr.removeModifier(CLEAVER_SPEED_ID);
        }
    }
}
