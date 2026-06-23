package alabaster.hearthandharvest.common.entity.pitchfork;

import alabaster.hearthandharvest.common.registry.HHModEffects;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownPitchfork extends AbstractArrow {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM =
            SynchedEntityData.defineId(ThrownPitchfork.class, EntityDataSerializers.ITEM_STACK);

    private boolean dealtDamage;

    public ThrownPitchfork(EntityType<? extends ThrownPitchfork> type, Level level) {
        super(type, level);
    }

    public ThrownPitchfork(Level level, LivingEntity shooter, ItemStack stack) {
        super(HHModEntities.THROWN_PITCHFORK.get(), shooter, level, stack, null);
        this.entityData.set(DATA_ITEM, stack.copyWithCount(1));
        this.setBaseDamage(meleeDamage(stack) / 2.5);
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

    @Override
    public void tick() {
        Entity owner = this.getOwner();
        int loyalty = loyaltyLevel(this.getPickupItem());
        if (loyalty > 0 && this.dealtDamage && owner instanceof Player player && player.isAlive()) {
            if (!this.level().isClientSide) {
                this.inGround = false;
                this.setNoGravity(true);
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
        super.onHitEntity(result);
        this.dealtDamage = true;
        if (result.getEntity() instanceof LivingEntity target)
            target.addEffect(new MobEffectInstance(HHModEffects.PINNED, 6000, 0));
    }

    private void returnTo(Player player) {
        ItemStack stack = getDefaultPickupItem();
        if (!stack.isEmpty() && !player.getInventory().add(stack))
            player.drop(stack, false);
        this.discard();
    }

    private int loyaltyLevel(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        return level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(Enchantments.LOYALTY)
                .map(h -> stack.getEnchantmentLevel(h))
                .orElse(0);
    }

    @Override
    public byte getPierceLevel() {
        return 2;
    }

    public ItemStack getPitchforkStack() {
        return this.entityData.get(DATA_ITEM);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        ItemStack stack = getPitchforkStack();
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1);
    }

    public boolean isStuck() {
        return inGround;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack stack = getPitchforkStack();
        if (!stack.isEmpty()) tag.put("PitchforkItem", stack.save(registryAccess()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("PitchforkItem"))
            entityData.set(DATA_ITEM, ItemStack.parseOptional(registryAccess(), tag.getCompound("PitchforkItem")));
        this.dealtDamage = tag.getBoolean("DealtDamage");
    }
}