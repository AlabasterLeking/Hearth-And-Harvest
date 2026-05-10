package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

public class SapCauldronBlock extends AbstractCauldronBlock {
    public static final IntegerProperty SAP_LEVEL = IntegerProperty.create("sap_level", 0, 3);
    private static final VoxelShape INSIDE = box(2.0F, 4.0F, 2.0F, 14.0F, 16.0F, 14.0F);
    protected static final VoxelShape SHAPE;

    public SapCauldronBlock(Properties properties) {
        super(properties, CauldronInteraction.EMPTY);
        this.registerDefaultState(this.stateDefinition.any().setValue(SAP_LEVEL, 3));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return INSIDE;
    }

    @Override
    public boolean isFull(BlockState state) {
        return false;
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return 0.0F;
    }

    @Override
    protected boolean isEntityInsideContent(BlockState state, BlockPos pos, Entity entity) {
        return entity.getY() < pos.getY() + this.getContentHeight(state)
                && entity.getBoundingBox().maxY > pos.getY() + 0.25F;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SAP_LEVEL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        int currentLevel = state.getValue(SAP_LEVEL);

        if (heldItem.getItem() == Items.GLASS_BOTTLE && currentLevel == 1) {
            if (!level.isClientSide) {
                level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
                heldItem.shrink(1);
                ItemStack syrupBottle = new ItemStack(HHModItems.SYRUP_BOTTLE.get());
                if (!player.getInventory().add(syrupBottle)) {
                    player.drop(syrupBottle, false);
                }
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 480);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int currentLevel = state.getValue(SAP_LEVEL);
        boolean isLit = level.getBlockState(pos.below()).is(ModTags.Blocks.HEAT_SOURCES);

        if (currentLevel > 1 && isLit) {
            int newLevel = currentLevel - 1;
            BlockState newState = state.setValue(SAP_LEVEL, newLevel);
            level.setBlock(pos, newState, 3);
            level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,
                    0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);

            if (newLevel > 1) {
                level.scheduleTick(pos, this, 480);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int currentLevel = state.getValue(SAP_LEVEL);
        if (currentLevel > 0) {
            boolean isLit = level.getBlockState(pos.below()).is(ModTags.HEAT_SOURCES);
            if (isLit) {
                if (random.nextFloat() < 0.2F) {
                    double x = pos.getX() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
                    double y = pos.getY() + 1.0D;
                    double z = pos.getZ() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
                    level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, 0.1D, 0.0D);
                }
                if (random.nextFloat() < 0.1F) {
                    double x = pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                    double y = pos.getY() + 0.75D;
                    double z = pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                    double motionY = random.nextBoolean() ? 0.015D : 0.005D;
                    level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
                }
            }
        }
    }

    static {
        SHAPE = Shapes.join(Shapes.block(), Shapes.or(
                box(0.0F, 0.0F, 4.0F, 16.0F, 3.0F, 12.0F),
                box(4.0F, 0.0F, 0.0F, 12.0F, 3.0F, 16.0F),
                box(2.0F, 0.0F, 2.0F, 14.0F, 3.0F, 14.0F),
                INSIDE), BooleanOp.ONLY_FIRST);
    }
}