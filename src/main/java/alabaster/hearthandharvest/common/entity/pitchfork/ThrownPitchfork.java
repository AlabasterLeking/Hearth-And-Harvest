package alabaster.hearthandharvest.common.entity.pitchfork;

import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;
import alabaster.hearthandharvest.common.registry.HHModEffects;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;

public class ThrownPitchfork extends AbstractArrow {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM =
            SynchedEntityData.defineId(ThrownPitchfork.class, EntityDataSerializers.ITEM_STACK);

    private boolean dealtDamage;
    private boolean creativeThrown;
    private IntOpenHashSet piercedEntityIds;

    public ThrownPitchfork(EntityType<? extends ThrownPitchfork> type, Level level) {
        super(type, level);
    }

    public ThrownPitchfork(Level level, LivingEntity shooter, ItemStack stack) {
        super(HHModEntities.THROWN_PITCHFORK.get(), shooter, level);
        this.entityData.set(DATA_ITEM, stack.copyWithCount(1));
        this.setBaseDamage(meleeDamage(stack) / 2.5);
        this.pickup = Pickup.ALLOWED;
        this.creativeThrown = shooter instanceof Player player && player.getAbilities().instabuild;
    }

    private static double meleeDamage(ItemStack stack) {
        double damage = 2.0;
        for (AttributeModifier modifier : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
            if (modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                damage += modifier.getAmount();
        }
        return damage;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();
        int loyalty = loyaltyLevel(this.getPickupItem());
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
    public void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.dealtDamage = true;
        this.piercedEntityIds = null;
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && (piercedEntityIds == null || !piercedEntityIds.contains(target.getId()));
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        if (this.isNoPhysics()) {
            if (result.getEntity().equals(this.getOwner()) && this.getOwner() instanceof Player player)
                returnTo(player);
            return;
        }
        Entity entity = result.getEntity();
        if (piercedEntityIds == null) piercedEntityIds = new IntOpenHashSet(5);
        piercedEntityIds.add(entity.getId());
        Entity owner = this.getOwner();
        DamageSource source;
        if (owner instanceof Player player) source = this.damageSources().playerAttack(player);
        else if (owner instanceof LivingEntity living) source = this.damageSources().mobAttack(living);
        else source = this.damageSources().thrown(this, owner != null ? owner : this);
        float damage = (float)(this.getDeltaMovement().length() * this.getBaseDamage());
        if (entity instanceof LivingEntity livingTarget)
            damage += EnchantmentHelper.getDamageBonus(this.getWeaponItem(), livingTarget.getMobType());
        this.dealtDamage = true;
        if (entity.hurt(source, damage)) {
            if (entity instanceof LivingEntity livingEntity) {
                this.doPostHurtEffects(livingEntity);
                if (livingEntity.isDeadOrDying()) HHSimpleTrigger.trigger(HHModTriggers.PITCHFORK_KILL, owner);
            }
            if (entity instanceof LivingEntity livingTarget && owner instanceof LivingEntity livingOwner) {
                EnchantmentHelper.doPostHurtEffects(livingTarget, livingOwner);
                EnchantmentHelper.doPostDamageEffects(livingOwner, livingTarget);
            }
            if (entity instanceof LivingEntity target)
                target.addEffect(new MobEffectInstance(HHModEffects.PINNED.get(), 200, 0));
        }
    }

    private void returnTo(Player player) {
        if (!player.getAbilities().instabuild) {
            ItemStack stack = getPickupItem();
            if (!stack.isEmpty() && !player.getInventory().add(stack))
                player.drop(stack, false);
        }
        this.discard();
    }

    public ItemStack getWeaponItem() {
        return getPitchforkStack();
    }

    private int loyaltyLevel(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        return EnchantmentHelper.getLoyalty(stack);
    }

    @Nullable
    @Override
    public EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.isNoPhysics() ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED || loyaltyLevel(getPickupItem()) <= 0)
            super.tickDespawn();
    }

    @Override
    public byte getPierceLevel() {
        return 2;
    }

    public ItemStack getPitchforkStack() {
        return this.entityData.get(DATA_ITEM);
    }

    @Override
    protected ItemStack getPickupItem() {
        ItemStack stack = getPitchforkStack();
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1);
    }

    public boolean isStuck() {
        return inGround;
    }

    @Override
    public boolean tryPickup(Player player) {
        if (player.getAbilities().instabuild) return true;
        if (this.creativeThrown) return false;
        return super.tryPickup(player);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return HHModSounds.PITCHFORK_HIT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack stack = getPitchforkStack();
        if (!stack.isEmpty()) tag.put("PitchforkItem", stack.save(new CompoundTag()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
        tag.putBoolean("CreativeThrown", this.creativeThrown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("PitchforkItem"))
            entityData.set(DATA_ITEM, ItemStack.of(tag.getCompound("PitchforkItem")));
        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.creativeThrown = tag.getBoolean("CreativeThrown");
    }
}