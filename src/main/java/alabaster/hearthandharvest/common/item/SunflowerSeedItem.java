package alabaster.hearthandharvest.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class SunflowerSeedItem extends Item {

    public SunflowerSeedItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos().relative(ctx.getClickedFace());
        Player player = ctx.getPlayer();

        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!level.getBlockState(pos.below()).is(BlockTags.DIRT)) {
            return InteractionResult.FAIL;
        }

        // Space must be free for the 2-block-tall sunflower
        if (!level.getBlockState(pos).canBeReplaced() || !level.getBlockState(pos.above()).canBeReplaced()) {
            return InteractionResult.FAIL;
        }

        level.setBlock(pos, Blocks.SUNFLOWER.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 3);
        level.setBlock(pos.above(), Blocks.SUNFLOWER.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 3);

        if (player != null && !player.getAbilities().instabuild) {
            ctx.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}