package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.entity.ManureProjectile;
import alabaster.hearthandharvest.common.registry.HHModParticleTypes;
import alabaster.hearthandharvest.common.registry.HHModSounds;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

public class ManureItem extends Item {
    public ManureItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return 200;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (entity.level().isClientSide) {
            RandomSource random = entity.getRandom();
            if (random.nextInt(20) == 0) {
                entity.level().addParticle(HHModParticleTypes.FLIES.get(),
                        entity.getX() + (random.nextDouble() - 0.5) * 0.3,
                        entity.getY() + 0.1,
                        entity.getZ() + (random.nextDouble() - 0.5) * 0.3,
                        0, 0, 0);
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            ManureProjectile projectile = new ManureProjectile(level, player);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
            level.addFreshEntity(projectile);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.EGG_THROW, SoundSource.PLAYERS,
                    0.5f, 1.0f + level.random.nextFloat() * 0.4f);
        }
        if (!player.getAbilities().instabuild) stack.shrink(1);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}