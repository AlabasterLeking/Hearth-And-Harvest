package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.tag.HHVanillaTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class AnimalFoodEvents {

    @Nullable
    public static TagKey<Item> foodTag(Animal animal) {
        if (animal instanceof Cow) return HHVanillaTags.COW_FOOD;
        if (animal instanceof Sheep) return HHVanillaTags.SHEEP_FOOD;
        if (animal instanceof Pig) return HHVanillaTags.PIG_FOOD;
        if (animal instanceof Chicken) return HHVanillaTags.CHICKEN_FOOD;
        if (animal instanceof Goat) return HHVanillaTags.GOAT_FOOD;
        if (animal instanceof Llama) return HHVanillaTags.LLAMA_FOOD;
        if (animal instanceof Camel) return null;
        if (animal instanceof AbstractHorse) return HHVanillaTags.HORSE_FOOD;
        return null;
    }

    @Nullable
    private static TagKey<Item> temptTag(Animal animal) {
        if (animal instanceof Goat || animal instanceof Camel) return null;
        if (animal instanceof Llama) return HHVanillaTags.LLAMA_TEMPT_ITEMS;
        if (animal instanceof AbstractHorse) return HHVanillaTags.HORSE_TEMPT_ITEMS;
        return foodTag(animal);
    }

    public static boolean isFood(Animal animal, ItemStack stack) {
        if (animal.isFood(stack)) return true;
        TagKey<Item> tag = foodTag(animal);
        return tag != null && stack.is(tag);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onFeed(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Animal animal) || animal instanceof AbstractHorse) return;
        ItemStack stack = event.getItemStack();
        if (animal.isFood(stack) || !isFood(animal, stack)) return;

        Player player = event.getEntity();
        Level level = event.getLevel();
        int age = animal.getAge();
        InteractionResult result;
        if (!level.isClientSide && age == 0 && animal.canFallInLove()) {
            consume(player, stack);
            animal.setInLove(player);
            result = InteractionResult.SUCCESS;
        } else if (animal.isBaby()) {
            consume(player, stack);
            animal.ageUp(Animal.getSpeedUpSecondsWhenFeeding(-age), true);
            result = InteractionResult.sidedSuccess(level.isClientSide);
        } else if (level.isClientSide) {
            result = InteractionResult.CONSUME;
        } else {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(result);
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Animal animal)) return;
        TagKey<Item> tag = temptTag(animal);
        if (tag == null) return;
        double speed = animal instanceof Chicken ? 1.0D : 1.2D;
        animal.goalSelector.addGoal(3, new TemptGoal(animal, speed, Ingredient.of(tag), false));
    }

    private static void consume(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) stack.shrink(1);
    }
}