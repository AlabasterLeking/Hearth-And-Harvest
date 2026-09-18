package alabaster.hearthandharvest.common.worldgen;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.block.NestBlock;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class NestFeature extends Feature<NoneFeatureConfiguration> {

    public NestFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (!Config.GENERATE_NESTS.get()) return false;

        WorldGenLevel level = ctx.level();
        BlockPos pos = ctx.origin();
        BlockState existing = level.getBlockState(pos);
        if (!existing.canBeReplaced() || !existing.getFluidState().isEmpty()) return false;

        BlockState nest = HHModBlocks.NEST.get().defaultBlockState().setValue(NestBlock.GENERATED, true);
        if (!nest.canSurvive(level, pos)) return false;

        level.setBlock(pos, nest, 2);
        return true;
    }
}