package alabaster.hearthandharvest.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class PitchforkStrawModifier extends LootModifier {
    public static final Supplier<Codec<PitchforkStrawModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, PitchforkStrawModifier::new)));

    public PitchforkStrawModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null || !isGrassOrCrop(state)) return generatedLoot;
        generatedLoot.clear();
        generatedLoot.add(new ItemStack(ModItems.STRAW.get(), 2 + context.getRandom().nextInt(3)));
        return generatedLoot;
    }

    private static boolean isGrassOrCrop(BlockState state) {
        return state.is(Blocks.GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(BlockTags.CROPS)
                || state.is(Blocks.WHEAT);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
