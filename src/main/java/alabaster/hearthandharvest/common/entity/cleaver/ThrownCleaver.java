package alabaster.hearthandharvest.common.entity.cleaver;

import alabaster.hearthandharvest.common.registry.HHModEntities;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;

public class ThrownCleaver extends AbstractArrow {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM =
            SynchedEntityData.defineId(ThrownCleaver.class, EntityDataSerializers.ITEM_STACK);

    private boolean dealtDamage;

    public ThrownCleaver(EntityType<? extends ThrownCleaver> type, Level level) {
        super(type, level);
    }

    public ThrownCleaver(Level level, LivingEntity shooter, ItemStack stack) {
        super(HHModEntities.THROWN_CLEAVER.get(), shooter, level, stack, null);
        this.entityData.set(DATA_ITEM, stack.copyWithCount(1));
        this.setBaseDamage(meleeDamage(stack) / 2.0);
        this.pickup = Pickup.ALLOWED;
    }

    private static double meleeDamage(ItemStack stack) {
        double damage = 2.0;
        for (ItemAttributeModifiers.Entry entry : stack.getAttributeModifiers().modifiers()) {
            if (entry.attribute().is(Attributes.ATTACK_DAMAGE)
                    && entry.slot().test(EquipmentSlot.MAINHAND)
                    && entry.modifier().operation() == AttributeModifier.Operation.ADD_VALUE)
                damage += entry.modifier().amount();
        }
        return damage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ITEM, ItemStack.EMPTY);
    }

    public ItemStack getCleaverStack() {
        return this.entityData.get(DATA_ITEM);
    }

    public boolean isStuck() {
        return inGround;
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();
        int loyalty = loyaltyLevel();
        if (loyalty > 0 && this.dealtDamage && owner instanceof Player player && player.isAlive()) {
            if (!this.level().isClientSide) {
                this.inGround = false;
                this.setNoPhysics(true);
                Vec3 toPlayer = player.getEyePosition().subtract(this.position());
                if (toPlayer.lengthSqr() < 4.0) {
                    returnTo(player);
                    return;
                }
                this.setDeltaMovement(this.getDeltaMovement()
                        .add(toPlayer.normalize().scale(0.05 * loyalty))
                        .scale(0.92));
            }
        }
        super.tick();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.dealtDamage = true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.dealtDamage) {
            if (result.getEntity().equals(this.getOwner()) && this.getOwner() instanceof Player player)
                returnTo(player);
            return;
        }
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().thrown(this, owner != null ? owner : this);
        entity.hurt(source, (float)(this.getDeltaMovement().length() * this.getBaseDamage()));
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.dealtDamage = true;
    }

    private void returnTo(Player player) {
        ItemStack stack = getDefaultPickupItem();
        if (!stack.isEmpty() && !player.getInventory().add(stack))
            player.drop(stack, false);
        this.discard();
    }

    private int loyaltyLevel() {
        ItemStack stack = getCleaverStack();
        if (stack.isEmpty()) return 0;
        return level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(Enchantments.LOYALTY)
                .map(h -> stack.getEnchantmentLevel(h))
                .orElse(0);
    }

    @Nullable
    @Override
    protected ProjectileDeflection hitTargetOrDeflectSelf(HitResult hitResult) {
        onHit(hitResult);
        return null;
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED || loyaltyLevel() <= 0)
            super.tickDespawn();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        ItemStack stack = getCleaverStack();
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return HHModSounds.CLEAVER_HIT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack stack = getCleaverStack();
        if (!stack.isEmpty()) tag.put("CleaverItem", stack.save(registryAccess()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("CleaverItem"))
            entityData.set(DATA_ITEM, ItemStack.parseOptional(registryAccess(), tag.getCompound("CleaverItem")));
        this.dealtDamage = tag.getBoolean("DealtDamage");
    }
}