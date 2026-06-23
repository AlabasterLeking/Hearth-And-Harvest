package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.IHarvestable;
import alabaster.hearthandharvest.common.registry.HHModEnchantments;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.GAME)
public class FarmersHatEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!player.getItemBySlot(EquipmentSlot.HEAD).is(HHModItems.FARMERS_HAT.get())) return;

        BlockState state = event.getState();
        if ((state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state))
                || (state.getBlock() instanceof IHarvestable h && h.isHarvestReady(state))) {
            dropXp(level, event.getPos());
            damageHat(player);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        Player player = event.getEntity();
        if (!player.getItemBySlot(EquipmentSlot.HEAD).is(HHModItems.FARMERS_HAT.get())) return;

        BlockState state = level.getBlockState(event.getPos());
        boolean harvestable = (state.getBlock() instanceof IHarvestable h && h.isHarvestReady(state))
                || (state.is(HHModTags.RIGHT_CLICK_HARVESTABLE)
                && state.getBlock() instanceof CropBlock crop
                && crop.isMaxAge(state));
        if (!harvestable) return;

        var tool = player.getItemInHand(event.getHand());
        if (tool.getItem() instanceof HoeItem) {
            int lvl = level.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolder(HHModEnchantments.HARVESTING)
                    .map(holder -> tool.getEnchantmentLevel(holder))
                    .orElse(0);
            if (lvl > 0) return;
        }

        dropXp((ServerLevel) level, event.getPos());
        damageHat(player);
    }

    public static void dropXp(ServerLevel level, BlockPos pos) {
        ExperienceOrb.award(level, Vec3.atCenterOf(pos), 1 + level.random.nextInt(2));
    }

    public static void damageHat(Player player) {
        ItemStack hat = player.getItemBySlot(EquipmentSlot.HEAD);
        if (hat.is(HHModItems.FARMERS_HAT.get()))
            hat.hurtAndBreak(1, player, EquipmentSlot.HEAD);
    }
}