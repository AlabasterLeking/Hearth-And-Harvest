package alabaster.hearthandharvest.common.registry;

import alabaster.hearthandharvest.common.item.component.SeedPouchContents;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HHModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "hearthandharvest");

    public static final Supplier<DataComponentType<Integer>> COOK_TIME =
            DATA_COMPONENTS.registerComponentType("cook_time", builder ->
                    builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final Supplier<DataComponentType<Integer>> WATER_LEVEL =
            DATA_COMPONENTS.registerComponentType("water_level", builder ->
                    builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final Supplier<DataComponentType<Integer>> BONEMEAL_LEVEL =
            DATA_COMPONENTS.registerComponentType("bonemeal_level", builder ->
                    builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final Supplier<DataComponentType<Boolean>> SALTED =
            DATA_COMPONENTS.registerComponentType("salted", builder ->
                    builder.persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
            );

    public static final Supplier<DataComponentType<Item>> FERTILIZER_ITEM =
            DATA_COMPONENTS.registerComponentType("fertilizer_item", builder ->
                    builder.persistent(BuiltInRegistries.ITEM.byNameCodec())
                            .networkSynchronized(ByteBufCodecs.registry(Registries.ITEM))
            );

    public static final Supplier<DataComponentType<SeedPouchContents>> SEED_POUCH_CONTENTS =
            DATA_COMPONENTS.registerComponentType("seed_pouch_contents", builder ->
                    builder.persistent(SeedPouchContents.CODEC)
                            .networkSynchronized(SeedPouchContents.STREAM_CODEC)
            );
}