package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.registry.HHModDataComponents;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SaltedEffectEvents {

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        var stack = event.getItem();
        if (!HHModDataComponents.SALTED.has(stack)) return;

        FoodProperties food = stack.getFoodProperties(player);
        if (food == null) return;

        int bonusHunger = Math.max(1, Math.round(food.getNutrition() * Config.SALTED_HUNGER_BONUS.get().floatValue()));
        player.getFoodData().eat(bonusHunger, 0f);

        float saturationGranted = food.getNutrition() * food.getSaturationModifier() * 2.0f;
        float newSaturation = Math.max(0f, player.getFoodData().getSaturationLevel() - saturationGranted * Config.SALTED_SATURATION_PENALTY.get().floatValue());
        player.getFoodData().setSaturation(newSaturation);

        HHSimpleTrigger.trigger(HHModTriggers.ATE_SALTED_FOOD, player);
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.level.isClientSide()) return;
        if (event.level.getGameTime() % 20 != 0) return;

        ServerLevel level = (ServerLevel) event.level;

        List<Slime> slimes = new ArrayList<>();
        level.getEntities(EntityTypeTest.forClass(Slime.class), Slime::isAlive, slimes);

        List<Slime> toHurt = new ArrayList<>();
        for (Slime slime : slimes) {
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