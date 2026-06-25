package alabaster.hearthandharvest.common.entity.horseshoe;

import alabaster.hearthandharvest.common.registry.HHModEntities;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import javax.annotation.Nullable;

public class ThrownHorseshoe extends AbstractArrow {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM =
            SynchedEntityData.defineId(ThrownHorseshoe.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DATA_STUCK_ANGLE =
            SynchedEntityData.defineId(ThrownHorseshoe.class, EntityDataSerializers.FLOAT);

    public ThrownHorseshoe(EntityType<? extends ThrownHorseshoe> type, Level level) {
        super(type, level);
    }

    public ThrownHorseshoe(Level level, LivingEntity shooter, ItemStack stack) {
        super(HHModEntities.THROWN_HORSESHOE.get(), shooter, level, stack.copyWithCount(1), null);
        this.entityData.set(DATA_ITEM, stack.copyWithCount(1));
        this.setBaseDamage(3.5);
        this.pickup = Pickup.ALLOWED;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ITEM, ItemStack.EMPTY);
        builder.define(DATA_STUCK_ANGLE, -1.0f);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide && this.entityData.get(DATA_STUCK_ANGLE) < 0)
            this.entityData.set(DATA_STUCK_ANGLE, (this.tickCount * 36.0f) % 360.0f);
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        BlockPos hitPos = result.getBlockPos();
        BlockState hitState = serverLevel.getBlockState(hitPos);
        if (!hitState.is(BlockTags.FENCES)) return;
        serverLevel.updateNeighborsAt(hitPos, hitState.getBlock());
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = hitPos.relative(dir);
            BlockState neighborState = serverLevel.getBlockState(neighborPos);
            if (neighborState.is(Blocks.REDSTONE_LAMP) && !neighborState.getValue(BlockStateProperties.LIT)) {
                serverLevel.setBlock(neighborPos, neighborState.setValue(BlockStateProperties.LIT, true), 3);
                serverLevel.scheduleTick(neighborPos, neighborState.getBlock(), 20);
            }
        }
    }

    @Nullable
    @Override
    protected ProjectileDeflection hitTargetOrDeflectSelf(HitResult hitResult) {
        onHit(hitResult);
        return null;
    }

    public ItemStack getHorseshoeStack() {
        return this.entityData.get(DATA_ITEM);
    }

    public float getStuckAngle() {
        return this.entityData.get(DATA_STUCK_ANGLE);
    }

    @Override
    protected boolean tryPickup(Player player) {
        if (player.getAbilities().instabuild) return true;
        return super.tryPickup(player);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        ItemStack stack = getHorseshoeStack();
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return HHModSounds.HORSESHOE_HIT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack stack = getHorseshoeStack();
        if (!stack.isEmpty()) tag.put("HorseshoeItem", stack.save(registryAccess()));
        float stuckAngle = this.entityData.get(DATA_STUCK_ANGLE);
        if (stuckAngle >= 0) tag.putFloat("StuckAngle", stuckAngle);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("HorseshoeItem"))
            entityData.set(DATA_ITEM, ItemStack.parseOptional(registryAccess(), tag.getCompound("HorseshoeItem")));
        if (tag.contains("StuckAngle"))
            entityData.set(DATA_STUCK_ANGLE, tag.getFloat("StuckAngle"));
    }
}