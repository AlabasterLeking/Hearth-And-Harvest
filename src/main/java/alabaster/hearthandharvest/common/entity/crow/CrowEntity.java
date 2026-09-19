package alabaster.hearthandharvest.common.entity.crow;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.advancement.HHSimpleTrigger;
import alabaster.hearthandharvest.common.registry.HHModTriggers;

import javax.annotation.Nullable;

import alabaster.hearthandharvest.common.entity.crow.goals.*;
import alabaster.hearthandharvest.common.registry.HHModEntities;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import alabaster.hearthandharvest.common.tag.HHModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class CrowEntity extends ShoulderRidingEntity implements FlyingAnimal {
    public static final double ALARM_RANGE = 16.0D;
    private static final EntityDataAccessor<Boolean> DATA_GLIDING = SynchedEntityData.defineId(CrowEntity.class, EntityDataSerializers.BOOLEAN);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;
    private boolean snatching;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    public final AnimationState glidingAnimationState = new AnimationState();
    private final CrowFlightPose flightPose = new CrowFlightPose();
    private final CrowWariness wariness = new CrowWariness();

    public CrowEntity(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new CrowMoveControl(this);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public boolean isBaby() {
        return false;
    }

    private void setupAnimationStates() {
        if (!this.level().isClientSide()) return;

        if (isInSittingPose()) {
            startAnimation(sittingAnimationState);
        } else if (isVisuallyFlying()) {
            startAnimation(isGliding() ? glidingAnimationState : flyingAnimationState);
        } else {
            startAnimation(idleAnimationState);
        }
    }

    private void startAnimation(AnimationState active) {
        if (active.isStarted()) return;
        for (AnimationState state : new AnimationState[]{idleAnimationState, flyingAnimationState, sittingAnimationState, glidingAnimationState}) {
            if (state != active) state.stop();
        }
        active.start(this.tickCount);
    }

    public CrowFlightPose getFlightPose() {
        return flightPose;
    }

    public boolean isVisuallyFlying() {
        return flightPose.isVisuallyFlying(this);
    }

    public boolean isBusy() {
        return isOrderedToSit() || isPassenger();
    }

    public float getFlapAnimationSpeed() {
        return flightPose.flapSpeed();
    }

    public boolean isGliding() {
        return this.entityData.get(DATA_GLIDING);
    }

    public void setGliding(boolean gliding) {
        this.entityData.set(DATA_GLIDING, gliding);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TamableAnimalPanicGoal(1.25F));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CrowFleeEntityGoal(this, 1.8F));
        this.goalSelector.addGoal(1, new CrowAvoidRepellingBlocksGoal(this, 1.6F));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new CrowSeekShinyItemGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new CrowRetrieveItemsGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new CrowEatDroppedFoodGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new CrowWaryTemptGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new CrowEatCropsGoal(this, 1.2F));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0F, 5.0F, 1.0F));
        this.goalSelector.addGoal(5, new LandOnOwnersShoulderGoal(this));
        this.goalSelector.addGoal(5, new CrowWanderGoal(this, 1.0F));
        this.goalSelector.addGoal(6, new FollowMobGoal(this, 1.0F, 3.0F, 7.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0F)
                .add(Attributes.FLYING_SPEED, 0.8F)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.FOLLOW_RANGE, 32.0F)
                .add(Attributes.ATTACK_DAMAGE, 3.0F);
    }

    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            this.flightPose.tick(this);
        } else {
            this.wariness.tick();
            this.syncSittingPose();
        }
        this.calculateFlapping();
    }

    private void syncSittingPose() {
        if (this.isTame() && this.isOrderedToSit() && this.onGround() && !this.isInSittingPose()) {
            this.setInSittingPose(true);
        }
    }

    public void alarm(LivingEntity source) {
        if (this.isTame()) return;
        this.wariness.alarm(source);
    }

    public boolean isAlarmed() {
        return wariness.isAlarmed();
    }

    @Nullable
    public LivingEntity getAlarmSource() {
        return wariness.getAlarmSource();
    }

    public boolean consumeFreshAlarm() {
        return wariness.consumeFreshAlarm();
    }

    private void raiseAlarm(LivingEntity attacker) {
        this.alarm(attacker);
        if (!this.isSilent()) {
            this.playSound(HHModSounds.CROW_SQUAWK.get(), 2.0F, 1.25F);
        }
        int alarmed = 1;
        for (CrowEntity other : this.level().getEntitiesOfClass(CrowEntity.class, this.getBoundingBox().inflate(ALARM_RANGE), crow -> crow != this && !crow.isTame())) {
            other.alarm(attacker);
            alarmed++;
            if (!other.isSilent() && other.getRandom().nextInt(3) == 0) {
                other.playSound(HHModSounds.CROW_SQUAWK.get(), 1.5F, getPitch(other.getRandom()) + 0.15F);
            }
        }
        if (alarmed >= 5) HHSimpleTrigger.trigger(HHModTriggers.CROW_FLOCK_ALARM.get(), attacker);
    }

    public void setBeingTempted(boolean beingTempted) {
        wariness.setBeingTempted(beingTempted);
    }

    public void addTemptTrust(int amount) {
        wariness.addTrust(amount);
    }

    public boolean isFullyTrusting() {
        return wariness.isFullyTrusting();
    }

    public double getComfortDistance() {
        return wariness.comfortDistance();
    }

    public double getWaryDistance() {
        return wariness.waryDistance();
    }

    public static boolean isHoldingTemptItem(LivingEntity entity) {
        return entity.getMainHandItem().is(HHModTags.CROW_TEMPT_ITEMS) || entity.getOffhandItem().is(HHModTags.CROW_TEMPT_ITEMS);
    }

    public double getThreatRadius(LivingEntity entity, double baseRadius) {
        if (entity == this || !entity.isAlive()) return -1.0D;
        if (entity == getAlarmSource()) return ALARM_RANGE;
        if (!entity.getType().is(HHModTags.SCARY_FOR_CROW)) return -1.0D;
        if (entity instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) return -1.0D;
            if (Config.CROW_TEMPTING.get() && isHoldingTemptItem(player)) return Math.min(baseRadius, getWaryDistance());
        }
        return baseRadius;
    }

    public boolean isSpotThreatened(Vec3 pos, double baseRadius) {
        double scan = isAlarmed() ? Math.max(baseRadius, ALARM_RANGE) : baseRadius;
        return !this.level().getEntitiesOfClass(LivingEntity.class, AABB.ofSize(pos, scan * 2.0D, scan * 2.0D, scan * 2.0D), entity -> {
            double radius = getThreatRadius(entity, baseRadius);
            return radius > 0.0D && entity.distanceToSqr(pos) <= radius * radius;
        }).isEmpty();
    }

    private void calculateFlapping() {
        setupAnimationStates();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;

        this.flap += this.flapping * 2.0F;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!this.isTame() && itemstack.is(HHModTags.CROW_FOOD)) {
            itemstack.consume(1, player);
            if (!this.isSilent()) {
                this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), HHModSounds.CROW_EAT.get(), this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.level().isClientSide) {
                if (this.random.nextInt(5) == 0 && !EventHooks.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (!itemstack.is(ItemTags.PARROT_POISONOUS_FOOD)) {
            if (this.isTame() && this.isOwnedBy(player)) {
                if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
                if (!this.level().isClientSide) {
                    boolean sit = !this.isOrderedToSit();
                    this.setOrderedToSit(sit);
                    this.setInSittingPose(sit && this.onGround());
                    this.getNavigation().stop();
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else {
                return super.mobInteract(player, hand);
            }
        } else {
            itemstack.consume(1, player);
            this.addEffect(new MobEffectInstance(MobEffects.POISON, 900));
            if (player.isCreative() || !this.isInvulnerable()) {
                this.hurt(this.damageSources().playerAttack(player), Float.MAX_VALUE);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
    }

    public void tryTameFromFood(@Nullable Player player) {
        if (player == null || this.isTame() || this.level().isClientSide) return;
        if (this.random.nextInt(8) == 0 && !EventHooks.onAnimalTame(this, player)) {
            this.tame(player);
            this.level().broadcastEntityEvent(this, (byte) 7);
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6);
        }
    }

    public void tryTameFromPickup(@Nullable Player player) {
        if (player == null || this.isTame())
            return;

        if (!this.level().isClientSide) {
            if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {

                ItemStack held = this.getMainHandItem();

                this.tame(player);
                this.level().broadcastEntityEvent(this, (byte)7);

                if (!held.isEmpty()) {
                    this.holdItem(ItemStack.EMPTY);
                    ItemEntity dropped = this.spawnAtLocation(held, 0.3F);
                    if (dropped != null) dropped.setThrower(player);
                }

            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
        }
    }

    public boolean isSnatching() {
        return snatching;
    }

    public void setSnatching(boolean snatching) {
        this.snatching = snatching;
    }

    @Override
    public boolean canSitOnShoulder() {
        return super.canSitOnShoulder() && this.getMainHandItem().isEmpty();
    }

    public void holdItem(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        if (!stack.isEmpty()) this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(HHModTags.CROW_FOOD);
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HHModEntities.CROW.get().create(level);
    }

    protected SoundEvent getAmbientSound() {
        return HHModSounds.CROW_SQUAWK.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return HHModSounds.CROW_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return HHModSounds.CROW_HURT.get();
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(HHModSounds.CROW_STEP.get(), 0.15F, 1.0F);
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    public float getVoicePitch() {
        return getPitch(this.random);
    }

    public static float getPitch(RandomSource random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    public boolean isPushable() {
        return true;
    }

    protected void doPush(Entity entity) {
        if (!(entity instanceof Player)) {
            super.doPush(entity);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            return false;
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.level().isClientSide) {
                this.setOrderedToSit(false);
            }

            boolean hurt = super.hurt(source, amount);
            if (hurt && !this.level().isClientSide && !this.isTame() && Config.CROW_FLOCK_ALARM.get() && source.getEntity() instanceof LivingEntity attacker && attacker != this) {
                this.raiseAlarm(attacker);
            }
            return hurt;
        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_GLIDING, false);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    public boolean isFlying() {
        return !this.onGround() && !this.isInSittingPose();
    }

    protected boolean canFlyToOwner() {
        return true;
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0F, (0.5F * this.getEyeHeight()), (this.getBbWidth() * 0.4F));
    }

}