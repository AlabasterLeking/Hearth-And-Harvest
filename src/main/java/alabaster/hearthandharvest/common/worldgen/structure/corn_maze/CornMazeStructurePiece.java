package alabaster.hearthandharvest.common.worldgen.structure.corn_maze;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.CornStalkBlock;
import alabaster.hearthandharvest.common.block.ScarecrowBlock;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import alabaster.hearthandharvest.common.registry.HHModStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;

public class CornMazeStructurePiece extends StructurePiece {

    private static final int ENTRANCE_EXT = 2;
    private static final int TERRAIN_DIP = 0;
    private static final int FRINGE_EXT = 2;
    private static final Direction[] HORIZONTALS = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

    private final int width;
    private final int height;
    private final int originX;
    private final int originY;
    private final int originZ;
    private final long mazeSeed;

    public CornMazeStructurePiece(BlockPos origin, int width, int height, long mazeSeed) {
        super(HHModStructurePieces.CORN_MAZE_PIECE.get(), 0,
                BoundingBox.fromCorners(
                        origin.offset(-ENTRANCE_EXT, -TERRAIN_DIP, -FRINGE_EXT),
                        origin.offset(width + ENTRANCE_EXT, 10, height + FRINGE_EXT)));
        this.width = width;
        this.height = height;
        this.originX = origin.getX();
        this.originY = origin.getY();
        this.originZ = origin.getZ();
        this.mazeSeed = mazeSeed;
    }

    public CornMazeStructurePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super(HHModStructurePieces.CORN_MAZE_PIECE.get(), tag);
        this.width = tag.getInt("Width");
        this.height = tag.getInt("Height");
        this.originX = tag.getInt("OriginX");
        this.originY = tag.getInt("OriginY");
        this.originZ = tag.getInt("OriginZ");
        this.mazeSeed = tag.contains("MazeSeed") ? tag.getLong("MazeSeed") : BlockPos.asLong(originX, originY, originZ);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
        tag.putInt("Width", width);
        tag.putInt("Height", height);
        tag.putInt("OriginX", originX);
        tag.putInt("OriginY", originY);
        tag.putInt("OriginZ", originZ);
        tag.putLong("MazeSeed", mazeSeed);
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox box,
            ChunkPos chunkPos,
            BlockPos pivot
    ) {
        BlockPos origin = new BlockPos(originX, originY, originZ);

        boolean[][] maze = MazeGenerator.generate(width, height, RandomSource.create(mazeSeed));
        maze[1][height / 2] = false;
        maze[width - 2][height / 2] = false;

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                BlockPos pos = origin.offset(x, 0, z);

                if (maze[x][z]) {
                    boolean interior = x > 1 && z > 1 && x < width - 2 && z < height - 2;
                    if (interior) {
                        placeWallVariant(level, box, pos, random);
                    } else {
                        placeFullCornStalk(level, box, pos);
                    }
                    BlockPos wallFloor = pos.below();
                    if (box.isInside(wallFloor)) {
                        float f = random.nextFloat();
                        var floor = f < 0.45f ? Blocks.COARSE_DIRT.defaultBlockState()
                                : f < 0.70f ? Blocks.ROOTED_DIRT.defaultBlockState()
                                  : f < 0.88f ? Blocks.DIRT.defaultBlockState()
                                    : Blocks.GRASS_BLOCK.defaultBlockState();
                        level.setBlock(wallFloor, floor, 2);
                    }
                } else {
                    for (int y = 0; y < 3; y++) {
                        BlockPos airPos = pos.above(y);
                        if (box.isInside(airPos)) clearAir(level, airPos);
                    }
                    BlockPos floorPos = pos.below();
                    if (box.isInside(floorPos)) {
                        float pf = random.nextFloat();
                        var floor = pf < 0.60f ? Blocks.DIRT_PATH.defaultBlockState()
                                : pf < 0.85f ? Blocks.COARSE_DIRT.defaultBlockState()
                                  : Blocks.GRASS_BLOCK.defaultBlockState();
                        level.setBlock(floorPos, floor, 2);
                    }
                }

            }
        }

        BlockPos entrance = origin.offset(0, 0, height / 2);
        BlockPos exit = origin.offset(width - 1, 0, height / 2);

        carveOpening(level, box, entrance);
        carveOpening(level, box, exit);

        maze[0][height / 2] = false;
        maze[width - 1][height / 2] = false;

        for (int i = 1; i <= ENTRANCE_EXT; i++) {
            extendPath(level, box, entrance.relative(Direction.WEST, i));
            extendPath(level, box, exit.relative(Direction.EAST, i));
        }

        for (int x = -2; x <= width + 1; x++) {
            for (int z = -2; z <= height + 1; z++) {
                boolean outside = x < 0 || z < 0 || x >= width || z >= height;
                if (!outside) continue;

                BlockPos p = origin.offset(x, 0, z);

                boolean nearWestEntry = x < 0 && Math.abs(z - height / 2) <= 1;
                boolean nearEastEntry = x >= width && Math.abs(z - height / 2) <= 1;
                if (nearWestEntry || nearEastEntry) {
                    extendPath(level, box, p);
                    continue;
                }

                BlockPos floorPos = p.below();
                if (box.isInside(floorPos)) {
                    float f = random.nextFloat();
                    var floor = f < 0.55f ? Blocks.GRASS_BLOCK.defaultBlockState()
                            : f < 0.8f ? Blocks.COARSE_DIRT.defaultBlockState()
                              : Blocks.DIRT.defaultBlockState();
                    level.setBlock(floorPos, floor, 2);
                }

                if (box.isInside(p)) {
                    float roll = random.nextFloat();
                    if (roll < 0.3f) {
                        placeFullCornStalk(level, box, p);
                    } else if (roll < 0.5f) {
                        level.setBlock(p, Blocks.SHORT_GRASS.defaultBlockState(), 2);
                    } else if (roll < 0.6f) {
                        level.setBlock(p, Blocks.TALL_GRASS.defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER), 2);
                        BlockPos upper = p.above();
                        if (box.isInside(upper)) {
                            level.setBlock(upper, Blocks.TALL_GRASS.defaultBlockState()
                                    .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 2);
                        }
                    } else if (roll < 0.65f) {
                        level.setBlock(p, Blocks.PUMPKIN.defaultBlockState(), 2);
                    } else {
                        clearAir(level, p);
                    }
                }

            }
        }

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                if (!maze[x][z]) {
                    if ((x == 0 && z == height / 2) || (x == width - 1 && z == height / 2)) continue;

                    int open = 0;
                    Direction face = null;

                    if (x > 0 && !maze[x - 1][z]) { open++; face = Direction.WEST; }
                    if (x < width - 1 && !maze[x + 1][z]) { open++; face = Direction.EAST; }
                    if (z > 0 && !maze[x][z - 1]) { open++; face = Direction.NORTH; }
                    if (z < height - 1 && !maze[x][z + 1]) { open++; face = Direction.SOUTH; }

                    if (open == 1) {
                        BlockPos placePos = origin.offset(x, 0, z);
                        if (!box.isInside(placePos)) continue;

                        float roll = random.nextFloat();
                        if (roll < 0.3f) {
                            level.setBlock(placePos,
                                    Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, face), 2);
                            if (level.getBlockEntity(placePos) instanceof ChestBlockEntity chest) {
                                chest.setLootTable(mazeLootKey(), random.nextLong());
                            }
                        } else if (roll < 0.6f) {
                            level.setBlock(placePos,
                                    HHModBlocks.SCARECROW.get().defaultBlockState()
                                            .setValue(ScarecrowBlock.FACING, face), 2);
                        } else if (roll < 0.9f) {
                            level.setBlock(placePos, Blocks.HAY_BLOCK.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }

        BlockPos chamberCenter = origin.offset(width / 2, 0, height / 2);
        if (box.isInside(chamberCenter)) {
            level.setBlock(chamberCenter,
                    Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH), 2);
            if (level.getBlockEntity(chamberCenter) instanceof ChestBlockEntity chest) {
                chest.setLootTable(mazeCenterLootKey(), random.nextLong());
            }
        }

        buildEntryStructure(level, box, entrance);
        buildEntryStructure(level, box, exit);
    }

    private void extendPath(WorldGenLevel level, BoundingBox box, BlockPos pos) {
        for (int y = 0; y < 3; y++) {
            BlockPos p = pos.above(y);
            if (box.isInside(p)) clearAir(level, p);
        }
        BlockPos floorPos = pos.below();
        if (box.isInside(floorPos)) level.setBlock(floorPos, Blocks.DIRT_PATH.defaultBlockState(), 2);
    }

    private ResourceKey<LootTable> mazeLootKey() {
        return ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "chests/corn_maze"));
    }

    private ResourceKey<LootTable> mazeCenterLootKey() {
        return ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "chests/corn_maze_center"));
    }

    private void placeWallVariant(WorldGenLevel level, BoundingBox box, BlockPos pos, RandomSource random) {
        float roll = random.nextFloat();
        if (roll < 0.08f) {
            if (box.isInside(pos)) level.setBlock(pos, Blocks.HAY_BLOCK.defaultBlockState(), 2);
            BlockPos mid = pos.above();
            if (box.isInside(mid)) level.setBlock(mid, Blocks.HAY_BLOCK.defaultBlockState(), 2);
            BlockPos top = pos.above(2);
            if (box.isInside(top)) clearAir(level, top);
        } else if (roll < 0.12f) {
            Direction facing = HORIZONTALS[random.nextInt(4)];
            if (box.isInside(pos)) {
                level.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, facing), 2);
            }
            for (int y = 1; y < 3; y++) {
                BlockPos p = pos.above(y);
                if (box.isInside(p)) clearAir(level, p);
            }
        } else if (roll < 0.15f) {
            Direction facing = HORIZONTALS[random.nextInt(4)];
            if (box.isInside(pos)) {
                level.setBlock(pos, Blocks.JACK_O_LANTERN.defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, facing), 2);
            }
            for (int y = 1; y < 3; y++) {
                BlockPos p = pos.above(y);
                if (box.isInside(p)) clearAir(level, p);
            }
        } else {
            placeFullCornStalk(level, box, pos);
        }
    }

    private static void clearAir(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)) return;
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
    }

    private void placeFullCornStalk(WorldGenLevel level, BoundingBox box, BlockPos pos) {
        if (box.isInside(pos)) {
            level.setBlock(pos, HHModBlocks.CORN_STALK.get().defaultBlockState()
                    .setValue(CornStalkBlock.SECTION, CornStalkBlock.CornSection.BOTTOM)
                    .setValue(CornStalkBlock.CROW_PROOF, true)
                    .setValue(CornStalkBlock.AGE, 5), 2);
        }
        BlockPos mid = pos.above();
        if (box.isInside(mid)) {
            level.setBlock(mid, HHModBlocks.CORN_STALK.get().defaultBlockState()
                    .setValue(CornStalkBlock.SECTION, CornStalkBlock.CornSection.MIDDLE)
                    .setValue(CornStalkBlock.CROW_PROOF, true)
                    .setValue(CornStalkBlock.AGE, 5), 2);
        }
        BlockPos top = pos.above(2);
        if (box.isInside(top)) {
            level.setBlock(top, HHModBlocks.CORN_STALK.get().defaultBlockState()
                    .setValue(CornStalkBlock.SECTION, CornStalkBlock.CornSection.TOP)
                    .setValue(CornStalkBlock.CROW_PROOF, true)
                    .setValue(CornStalkBlock.AGE, 5), 2);
        }
    }

    private void carveOpening(WorldGenLevel level, BoundingBox box, BlockPos pos) {
        for (int y = 0; y < 3; y++) {
            BlockPos p = pos.above(y);
            if (box.isInside(p)) clearAir(level, p);
        }
        BlockPos floorPos = pos.below();
        if (box.isInside(floorPos)) level.setBlock(floorPos, Blocks.DIRT_PATH.defaultBlockState(), 2);
    }

    private void buildEntryStructure(WorldGenLevel level, BoundingBox box, BlockPos center) {
        BlockPos left = center.relative(Direction.NORTH);
        BlockPos right = center.relative(Direction.SOUTH);

        for (int y = 0; y <= 4; y++) {
            BlockPos lp = left.above(y);
            BlockPos rp = right.above(y);
            if (box.isInside(lp)) level.setBlock(lp, Blocks.OAK_FENCE.defaultBlockState(), 2);
            if (box.isInside(rp)) level.setBlock(rp, Blocks.OAK_FENCE.defaultBlockState(), 2);
        }

        for (BlockPos lp : new BlockPos[]{left.above(4), center.above(4), right.above(4)}) {
            if (box.isInside(lp)) {
                level.setBlock(lp, Blocks.OAK_LOG.defaultBlockState()
                        .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z), 2);
            }
        }

        BlockPos lanternPos = center.above(3);
        if (box.isInside(lanternPos)) {
            level.setBlock(lanternPos, Blocks.LANTERN.defaultBlockState()
                    .setValue(LanternBlock.HANGING, true), 2);
        }
    }
}