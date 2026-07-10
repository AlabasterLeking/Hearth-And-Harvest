package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SaltedEffectEvents {

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        var stack = event.getItem();
        if (!stack.has(HHModDataComponents.SALTED.get())) return;

        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food == null) return;

        int bonusHunger = Math.max(1, Math.round(food.nutrition() * Config.SALTED_HUNGER_BONUS.get().floatValue()));
        player.getFoodData().eat(bonusHunger, 0f);

        float saturationGranted = food.nutrition() * food.saturation() * 2.0f;
        float newSaturation = Math.max(0f, player.getFoodData().getSaturationLevel() - saturationGranted * Config.SALTED_SATURATION_PENALTY.get().floatValue());
        player.getFoodData().setSaturation(newSaturation);
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getLevel().getGameTime() % 20 != 0) return;

        ServerLevel level = (ServerLevel) event.getLevel();

        List<Entity> snapshot = new ArrayList<>();
        level.getAllEntities().forEach(snapshot::add);

        List<Slime> toHurt = new ArrayList<>();
        for (Entity entity : snapshot) {
            if (!(entity instanceof Slime slime)) continue;
            AABB check = slime.getBoundingBox().inflate(0.001);
            boolean touchingSalt = BlockPos.betweenClosedStream(
                    BlockPos.containing(check.minX, check.minY, check.minZ),
                    BlockPos.containing(check.maxX, check.maxY, check.maxZ)
            ).anyMatch(pos -> level.getBlockState(pos).is(HHModTags.SALT_BLOCKS));
            if (touchingSalt) toHurt.add(slime);
        }
        for (Slime slime : toHurt) {
            slime.hurt(level.damageSources().magic(), 2.0f);
        }
    }
}