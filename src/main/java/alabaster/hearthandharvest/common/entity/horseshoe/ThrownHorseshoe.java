package alabaster.hearthandharvest.common.entity.horseshoe;

import alabaster.hearthandharvest.common.registry.HHModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ThrownHorseshoe extends AbstractArrow {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM =
            SynchedEntityData.defineId(ThrownHorseshoe.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DATA_STUCK_ANGLE =
            SynchedEntityData.defineId(ThrownHorseshoe.class, EntityDataSerializers.FLOAT);

    public ThrownHorseshoe(EntityType<? extends ThrownHorseshoe> type, Level level) {
        super(type, level);
    }

    public ThrownHorseshoe(Level level, LivingEntity shooter, ItemStack stack) {
        super(HHModEntities.THROWN_HORSESHOE.get(), shooter, level, stack, null);
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
    }

    public ItemStack getHorseshoeStack() {
        return this.entityData.get(DATA_ITEM);
    }

    // Returns the frozen spin angle once embedded in a block, or -1 if still in flight.
    public float getStuckAngle() {
        return this.entityData.get(DATA_STUCK_ANGLE);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        ItemStack stack = getHorseshoeStack();
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ANVIL_LAND;
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