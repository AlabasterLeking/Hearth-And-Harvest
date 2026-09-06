package alabaster.hearthandharvest.common.item;

import alabaster.hearthandharvest.common.block.trellis.TrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TrellisBlockItem extends BlockItem {

    private final TrellisMaterial material;

    public TrellisBlockItem(Block block, TrellisMaterial material, Properties props) {
        super(block, props);
        this.material = material;
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return 300 ;
    }

    public TrellisMaterial getMaterial() {
        return material;
    }

    public record PlacementResult(BlockPos pos, BlockState state) {}

    public Optional<PlacementResult> simulatePlacement(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Direction face = ctx.getClickedFace();
        Direction playerFacing = ctx.getHorizontalDirection();
        boolean sneaking = ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown();
        Vec3 hit = ctx.getClickLocation();
        double lx = hit.x - pos.getX();
        double ly = hit.y - pos.getY();
        double lz = hit.z - pos.getZ();

        if (state.getBlock() instanceof TrellisBlock) {
            return simulateTrellisClick(state, pos, level, face, sneaking, lx, ly, lz, playerFacing);
        }
        return simulateFreshPlacement(level, pos, face, sneaking, lx, lz, playerFacing);
    }

    private Optional<PlacementResult> simulateTrellisClick(BlockState state, BlockPos pos, Level level, Direction face, boolean sneaking, double lx, double ly, double lz, Direction playerFacing) {
        return switch (face) {
            case UP    -> simulateTrellisTopFace(state, pos, level, sneaking, lx, lz, playerFacing);
            case DOWN  -> simulateTrellisBottomFace(state, pos, level, sneaking, lx, lz);
            case NORTH -> simulateTrellisSideFace(state, pos, level, Direction.NORTH,
                    TrellisBlock.SIDE_SOUTH, TrellisBlock.SIDE_NORTH, TrellisBlock.MIDDLE_EW, sneaking, ly, lx, lz);
            case SOUTH -> simulateTrellisSideFace(state, pos, level, Direction.SOUTH,
                    TrellisBlock.SIDE_NORTH, TrellisBlock.SIDE_SOUTH, TrellisBlock.MIDDLE_EW, sneaking, ly, lx, lz);
            case EAST  -> simulateTrellisSideFace(state, pos, level, Direction.EAST,
                    TrellisBlock.SIDE_WEST, TrellisBlock.SIDE_EAST, TrellisBlock.MIDDLE_NS, sneaking, ly, lx, lz);
            case WEST  -> simulateTrellisSideFace(state, pos, level, Direction.WEST,
                    TrellisBlock.SIDE_EAST, TrellisBlock.SIDE_WEST, TrellisBlock.MIDDLE_NS, sneaking, ly, lx, lz);
        };
    }

    private Optional<PlacementResult> simulateTrellisTopFace(BlockState state, BlockPos pos, Level level, boolean sneaking, double lx, double lz, Direction playerFacing) {
        if (sneaking) return simulatePlaceOrMerge(level, pos.above(), TrellisBlock.HAS_FLAT);
        if (hasSide(state) && !state.getValue(TrellisBlock.HAS_TOP)) return Optional.of(new PlacementResult(pos, state.setValue(TrellisBlock.HAS_TOP, true)));
        if (state.getValue(TrellisBlock.HAS_FLAT) || state.getValue(TrellisBlock.HAS_TOP)) {
            BooleanProperty toExtend = state.getValue(TrellisBlock.HAS_FLAT) ? TrellisBlock.HAS_FLAT : TrellisBlock.HAS_TOP;
            Direction extDir = horizontalDirectionFromHit(lx, lz);
            if (extDir != null) return simulatePlaceOrMerge(level, pos.relative(extDir), toExtend);
        }
        BooleanProperty component = componentFromTopHit(lx, lz, playerFacing);
        if (component == null) return Optional.empty();
        if (isMiddle(component) && hasSide(state)) return Optional.empty();
        if (isSide(component) && hasMiddle(state)) return Optional.empty();
        return Optional.of(new PlacementResult(pos, state.setValue(component, true)));
    }

    private Optional<PlacementResult> simulateTrellisBottomFace(BlockState state, BlockPos pos, Level level, boolean sneaking, double lx, double lz) {
        if (sneaking) return simulatePlaceOrMerge(level, pos.below(), TrellisBlock.HAS_TOP);
        if (state.getValue(TrellisBlock.HAS_TOP) || state.getValue(TrellisBlock.HAS_FLAT)) {
            BooleanProperty toExtend = state.getValue(TrellisBlock.HAS_TOP) ? TrellisBlock.HAS_TOP : TrellisBlock.HAS_FLAT;
            Direction extDir = horizontalDirectionFromHit(lx, lz);
            if (extDir != null) return simulatePlaceOrMerge(level, pos.relative(extDir), toExtend);
        }
        return Optional.empty();
    }

    private Optional<PlacementResult> simulateTrellisSideFace(BlockState state, BlockPos pos, Level level, Direction dir, BooleanProperty sideProp, BooleanProperty oppSideProp, BooleanProperty middleProp, boolean sneaking, double vert, double lx, double lz) {
        boolean hasSideProp = state.getValue(sideProp);
        boolean hasOppSide = state.getValue(oppSideProp);
        boolean hasMiddleProp = state.getValue(middleProp);

        if (sneaking && hasSideProp) {
            if (hasOppSide) return Optional.of(new PlacementResult(pos, state.setValue(vert > 0.5 ? TrellisBlock.HAS_TOP : TrellisBlock.HAS_FLAT, true)));
            return simulatePlaceOrMerge(level, pos.relative(dir), oppSideProp);
        }
        if (sneaking && hasOppSide) return Optional.of(new PlacementResult(pos, state.setValue(vert > 0.5 ? TrellisBlock.HAS_TOP : TrellisBlock.HAS_FLAT, true)));
        if (!sneaking && hasSideProp) return simulatePlaceOrMerge(level, pos.relative(extensionDirection(dir, vert, lx, lz)), sideProp);
        if (!sneaking && hasOppSide) return simulatePlaceOrMerge(level, pos.relative(extensionDirection(dir, vert, lx, lz)), oppSideProp);
        if (!sneaking && hasMiddleProp) return simulatePlaceOrMerge(level, pos.relative(extensionDirection(dir, vert, lx, lz)), middleProp);
        if (!sneaking) {
            if (hasMiddle(state)) return Optional.empty();
            if (state.getValue(sideProp)) return Optional.empty();
            if (state.getValue(TrellisBlock.MATERIAL) != material) return Optional.empty();
            return Optional.of(new PlacementResult(pos, state.setValue(sideProp, true)));
        }
        return Optional.empty();
    }

    private Optional<PlacementResult> simulateFreshPlacement(Level level, BlockPos clickedPos, Direction face, boolean sneaking, double lx, double lz, Direction playerFacing) {
        BlockPos targetPos = clickedPos.relative(face);
        BooleanProperty component = switch (face) {
            case UP -> sneaking ? TrellisBlock.HAS_FLAT : componentFromTopHit(lx, lz, playerFacing);
            case DOWN -> TrellisBlock.HAS_TOP;
            case NORTH -> TrellisBlock.SIDE_NORTH;
            case SOUTH -> TrellisBlock.SIDE_SOUTH;
            case EAST -> TrellisBlock.SIDE_EAST;
            case WEST -> TrellisBlock.SIDE_WEST;
        };
        if (component == null) return Optional.empty();
        return simulatePlaceOrMerge(level, targetPos, component);
    }

    private Optional<PlacementResult> simulatePlaceOrMerge(Level level, BlockPos targetPos, BooleanProperty component) {
        BlockState targetState = level.getBlockState(targetPos);
        if (targetState.getBlock() instanceof TrellisBlock) {
            if (targetState.getValue(TrellisBlock.MATERIAL) != material) return Optional.empty();
            if (targetState.getValue(component)) return Optional.empty();
            if (isSide(component) && hasMiddle(targetState)) return Optional.empty();
            if (isMiddle(component) && hasSide(targetState)) return Optional.empty();
            return Optional.of(new PlacementResult(targetPos, targetState.setValue(component, true)));
        }
        if (!targetState.isAir() && !targetState.canBeReplaced()) return Optional.empty();
        BlockState newState = getBlock().defaultBlockState()
                .setValue(TrellisBlock.MATERIAL, material)
                .setValue(component, true);
        return Optional.of(new PlacementResult(targetPos, newState));
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Direction face = ctx.getClickedFace();
        boolean sneaking = ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown();
        Vec3 hit = ctx.getClickLocation();
        double lx = hit.x - pos.getX();
        double ly = hit.y - pos.getY();
        double lz = hit.z - pos.getZ();

        if (state.getBlock() instanceof TrellisBlock) {
            return handleTrellisClick(ctx, state, pos, level, face, sneaking, lx, ly, lz);
        }
        return handleFreshPlacement(ctx, level, pos, face, sneaking, lx, lz);
    }

    private InteractionResult handleTrellisClick(UseOnContext ctx, BlockState state, BlockPos pos, Level level, Direction face, boolean sneaking, double lx, double ly, double lz) {
        return switch (face) {
            case UP    -> handleTrellisTopFace(ctx, state, pos, level, sneaking, lx, lz);
            case DOWN  -> handleTrellisBottomFace(ctx, state, pos, level, sneaking, lx, lz);
            case NORTH -> handleTrellisSideFace(ctx, state, pos, level, Direction.NORTH,
                    TrellisBlock.SIDE_SOUTH, TrellisBlock.SIDE_NORTH, TrellisBlock.MIDDLE_EW, sneaking, ly, lx, lz);
            case SOUTH -> handleTrellisSideFace(ctx, state, pos, level, Direction.SOUTH,
                    TrellisBlock.SIDE_NORTH, TrellisBlock.SIDE_SOUTH, TrellisBlock.MIDDLE_EW, sneaking, ly, lx, lz);
            case EAST  -> handleTrellisSideFace(ctx, state, pos, level, Direction.EAST,
                    TrellisBlock.SIDE_WEST, TrellisBlock.SIDE_EAST, TrellisBlock.MIDDLE_NS, sneaking, ly, lx, lz);
            case WEST  -> handleTrellisSideFace(ctx, state, pos, level, Direction.WEST,
                    TrellisBlock.SIDE_EAST, TrellisBlock.SIDE_WEST, TrellisBlock.MIDDLE_NS, sneaking, ly, lx, lz);
        };
    }

    private InteractionResult handleTrellisTopFace(UseOnContext ctx, BlockState state, BlockPos pos, Level level, boolean sneaking, double lx, double lz) {
        if (sneaking) {
            return placeOrMerge(ctx, level, pos.above(), material, TrellisBlock.HAS_FLAT);
        }

        if (hasSide(state) && !state.getValue(TrellisBlock.HAS_TOP)) {
            return mergeIntoBlock(ctx, state, pos, level, TrellisBlock.HAS_TOP);
        }

        if (state.getValue(TrellisBlock.HAS_FLAT) || state.getValue(TrellisBlock.HAS_TOP)) {
            BooleanProperty toExtend = state.getValue(TrellisBlock.HAS_FLAT) ? TrellisBlock.HAS_FLAT : TrellisBlock.HAS_TOP;
            Direction extDir = horizontalDirectionFromHit(lx, lz);
            if (extDir != null) {
                return placeOrMerge(ctx, level, pos.relative(extDir), material, toExtend);
            }
        }

        BooleanProperty component = componentFromTopHit(lx, lz, ctx.getHorizontalDirection());
        if (component == null) return InteractionResult.FAIL;
        if (isMiddle(component) && hasSide(state)) return InteractionResult.FAIL;
        if (isSide(component) && hasMiddle(state)) return InteractionResult.FAIL;
        return mergeIntoBlock(ctx, state, pos, level, component);
    }

    private InteractionResult handleTrellisBottomFace(UseOnContext ctx, BlockState state, BlockPos pos, Level level, boolean sneaking, double lx, double lz) {
        if (sneaking) {
            return placeOrMerge(ctx, level, pos.below(), material, TrellisBlock.HAS_TOP);
        }

        if (state.getValue(TrellisBlock.HAS_TOP) || state.getValue(TrellisBlock.HAS_FLAT)) {
            BooleanProperty toExtend = state.getValue(TrellisBlock.HAS_TOP) ? TrellisBlock.HAS_TOP : TrellisBlock.HAS_FLAT;
            Direction extDir = horizontalDirectionFromHit(lx, lz);
            if (extDir != null) {
                return placeOrMerge(ctx, level, pos.relative(extDir), material, toExtend);
            }
        }

        return InteractionResult.PASS;
    }

    private InteractionResult handleTrellisSideFace(UseOnContext ctx, BlockState state, BlockPos pos, Level level, Direction dir, BooleanProperty sideProp, BooleanProperty oppSideProp, BooleanProperty middleProp, boolean sneaking, double vert, double lx, double lz) {

        boolean hasSideProp = state.getValue(sideProp);
        boolean hasOppSide = state.getValue(oppSideProp);
        boolean hasMiddleProp = state.getValue(middleProp);

        if (sneaking && hasSideProp) {
            if (hasOppSide) {
                return mergeIntoBlock(ctx, state, pos, level, vert > 0.5 ? TrellisBlock.HAS_TOP : TrellisBlock.HAS_FLAT);
            }
            return placeOrMerge(ctx, level, pos.relative(dir), material, oppSideProp);
        }

        if (sneaking && hasOppSide) {
            BooleanProperty toPlace = vert > 0.5 ? TrellisBlock.HAS_TOP : TrellisBlock.HAS_FLAT;
            return mergeIntoBlock(ctx, state, pos, level, toPlace);
        }

        if (!sneaking && hasSideProp) {
            Direction extDir = extensionDirection(dir, vert, lx, lz);
            return placeOrMerge(ctx, level, pos.relative(extDir), material, sideProp);
        }

        if (!sneaking && hasOppSide) {
            Direction extDir = extensionDirection(dir, vert, lx, lz);
            return placeOrMerge(ctx, level, pos.relative(extDir), material, oppSideProp);
        }

        if (!sneaking && hasMiddleProp) {
            Direction extDir = extensionDirection(dir, vert, lx, lz);
            return placeOrMerge(ctx, level, pos.relative(extDir), material, middleProp);
        }

        if (!sneaking) {
            if (hasMiddle(state)) return InteractionResult.FAIL;
            return mergeIntoBlock(ctx, state, pos, level, sideProp);
        }

        return InteractionResult.PASS;
    }

    private InteractionResult handleFreshPlacement(UseOnContext ctx, Level level, BlockPos clickedPos, Direction face, boolean sneaking, double lx, double lz) {
        BlockPos targetPos = clickedPos.relative(face);

        BooleanProperty component = switch (face) {
            case UP -> sneaking ? TrellisBlock.HAS_FLAT : componentFromTopHit(lx, lz, ctx.getHorizontalDirection());
            case DOWN -> TrellisBlock.HAS_TOP;
            case NORTH -> TrellisBlock.SIDE_NORTH;
            case SOUTH -> TrellisBlock.SIDE_SOUTH;
            case EAST -> TrellisBlock.SIDE_EAST;
            case WEST -> TrellisBlock.SIDE_WEST;
        };

        if (component == null) return InteractionResult.FAIL;
        return placeOrMerge(ctx, level, targetPos, material, component);
    }

    private static boolean mayModify(UseOnContext ctx, BlockPos targetPos) {
        Player player = ctx.getPlayer();
        if (player == null) return true;
        return player.mayUseItemAt(targetPos, ctx.getClickedFace(), ctx.getItemInHand());
    }

    private InteractionResult placeOrMerge(UseOnContext ctx, Level level, BlockPos targetPos, TrellisMaterial mat, BooleanProperty component) {
        if (!mayModify(ctx, targetPos)) return InteractionResult.FAIL;
        BlockState targetState = level.getBlockState(targetPos);

        if (targetState.getBlock() instanceof TrellisBlock) {
            if (targetState.getValue(TrellisBlock.MATERIAL) != material) return InteractionResult.FAIL;
            if (targetState.getValue(component)) return InteractionResult.FAIL;
            if (isSide(component) && hasMiddle(targetState)) return InteractionResult.FAIL;
            if (isMiddle(component) && hasSide(targetState)) return InteractionResult.FAIL;
            if (!level.isClientSide()) {
                level.setBlock(targetPos, targetState.setValue(component, true), Block.UPDATE_ALL);
                playSoundAt(level, targetPos);
                consumeItem(ctx);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (!targetState.isAir() && !targetState.canBeReplaced()) return InteractionResult.FAIL;

        BlockState newState = getBlock().defaultBlockState()
                .setValue(TrellisBlock.MATERIAL, mat)
                .setValue(component, true);
        if (!level.isClientSide()) {
            level.setBlock(targetPos, newState, Block.UPDATE_ALL);
            playSoundAt(level, targetPos);
            consumeItem(ctx);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private InteractionResult mergeIntoBlock(UseOnContext ctx, BlockState state, BlockPos pos, Level level, BooleanProperty component) {
        if (!mayModify(ctx, pos)) return InteractionResult.FAIL;
        if (state.getValue(TrellisBlock.MATERIAL) != material) return InteractionResult.FAIL;
        if (state.getValue(component)) return InteractionResult.FAIL;
        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(component, true), Block.UPDATE_ALL);
            playSoundAt(level, pos);
            consumeItem(ctx);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    private BooleanProperty componentFromTopHit(double lx, double lz, @Nullable Direction playerFacing) {
        if (lz < 0.25) return TrellisBlock.SIDE_SOUTH;
        if (lz > 0.75) return TrellisBlock.SIDE_NORTH;
        if (lx < 0.25) return TrellisBlock.SIDE_EAST;
        if (lx > 0.75) return TrellisBlock.SIDE_WEST;
        if (playerFacing == null) return TrellisBlock.MIDDLE_EW;
        return (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH)
                ? TrellisBlock.MIDDLE_EW : TrellisBlock.MIDDLE_NS;
    }

    @Nullable
    private static Direction horizontalDirectionFromHit(double lx, double lz) {
        if (lz < 0.25) return Direction.NORTH;
        if (lz > 0.75) return Direction.SOUTH;
        if (lx < 0.25) return Direction.WEST;
        if (lx > 0.75) return Direction.EAST;
        return null;
    }

    private static Direction extensionDirection(Direction face, double vert, double lx, double lz) {
        if (vert > 0.75) return Direction.UP;
        if (vert < 0.25) return Direction.DOWN;
        return switch (face) {
            case NORTH, SOUTH -> lx < 0.5 ? Direction.WEST : Direction.EAST;
            case EAST, WEST   -> lz < 0.5 ? Direction.NORTH : Direction.SOUTH;
            default -> Direction.NORTH;
        };
    }

    private static boolean hasMiddle(BlockState state) {
        return state.getValue(TrellisBlock.MIDDLE_EW) || state.getValue(TrellisBlock.MIDDLE_NS);
    }

    private static boolean hasSide(BlockState state) {
        return state.getValue(TrellisBlock.SIDE_NORTH) || state.getValue(TrellisBlock.SIDE_SOUTH)
                || state.getValue(TrellisBlock.SIDE_EAST) || state.getValue(TrellisBlock.SIDE_WEST);
    }

    private static boolean isMiddle(BooleanProperty p) {
        return p == TrellisBlock.MIDDLE_EW || p == TrellisBlock.MIDDLE_NS;
    }

    private static boolean isSide(BooleanProperty p) {
        return p == TrellisBlock.SIDE_NORTH || p == TrellisBlock.SIDE_SOUTH
                || p == TrellisBlock.SIDE_EAST || p == TrellisBlock.SIDE_WEST;
    }

    private void playSoundAt(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1f, 1f);
    }

    private void consumeItem(UseOnContext ctx) {
        if (ctx.getPlayer() != null && !ctx.getPlayer().isCreative()) {
            ctx.getItemInHand().shrink(1);
        }
    }

    @Override
    public String getDescriptionId() {
        return "item." + BuiltInRegistries.ITEM.getKey(this).getNamespace()
                + "." + BuiltInRegistries.ITEM.getKey(this).getPath();
    }
}