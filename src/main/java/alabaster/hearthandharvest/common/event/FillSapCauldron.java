package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.SapCauldronBlock;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class FillSapCauldron {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldItem = event.getItemStack();

        if (level.getBlockState(pos).getBlock() == Blocks.CAULDRON && heldItem.getItem() == HHModItems.SAP_BUCKET.get()) {
            if (!level.isClientSide) {
                level.setBlock(pos, HHModBlocks.SAP_CAULDRON.get().defaultBlockState().setValue(SapCauldronBlock.SAP_LEVEL, 3), 3);
                heldItem.shrink(1);
                ItemStack bucket = new ItemStack(Items.BUCKET);
                if (!player.getInventory().add(bucket)) {
                    player.drop(bucket, false);
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
