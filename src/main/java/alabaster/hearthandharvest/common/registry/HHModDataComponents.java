package alabaster.hearthandharvest.common.registry;


import alabaster.hearthandharvest.common.item.component.ItemComponent;
import alabaster.hearthandharvest.common.item.component.SeedPouchContents;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

public class HHModDataComponents {
    public static final ItemComponent<Integer> COOK_TIME = new ItemComponent<>("hearthandharvest:cook_time", Codec.INT);
    public static final ItemComponent<Integer> WATER_LEVEL = new ItemComponent<>("hearthandharvest:water_level", Codec.INT);
    public static final ItemComponent<Integer> BONEMEAL_LEVEL = new ItemComponent<>("hearthandharvest:bonemeal_level", Codec.INT);
    public static final ItemComponent<Integer> SERVINGS = new ItemComponent<>("hearthandharvest:servings", Codec.INT);
    public static final ItemComponent<Integer> VINTAGE = new ItemComponent<>("hearthandharvest:vintage", Codec.INT);
    public static final ItemComponent<Boolean> SALTED = new ItemComponent<>("hearthandharvest:salted", Codec.BOOL);
    public static final ItemComponent<Item> FERTILIZER_ITEM = new ItemComponent<>("hearthandharvest:fertilizer_item", BuiltInRegistries.ITEM.byNameCodec());
    public static final ItemComponent<FluidStack> JUG_FLUID = new ItemComponent<>("hearthandharvest:jug_fluid", FluidStack.CODEC);
    public static final ItemComponent<SeedPouchContents> SEED_POUCH_CONTENTS = new ItemComponent<>("hearthandharvest:seed_pouch_contents", SeedPouchContents.CODEC);
}
