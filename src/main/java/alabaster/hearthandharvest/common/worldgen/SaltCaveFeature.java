package alabaster.hearthandharvest.common.worldgen;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.block.SaltBlock;
import alabaster.hearthandharvest.common.block.SaltDripBlock;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaltCaveFeature extends Feature<NoneFeatureConfiguration> {
    private static final int RADIUS_H = 16;
    private static final int RADIUS_V = 16;
    private static final int SIZE_H = RADIUS_H * 2 + 1;
    private static final int SIZE_V = RADIUS_V * 2 + 1;

    private static final byte OUTSIDE = 0;
    private static final byte SHELL = 1;
    private static final byte INSIDE = 2;

    private static final int BEDROCK_MARGIN = 6;
    private static final double SHELL_THICKNESS = 1.5D;
    private static final double FLOOR_FLATTEN = 0.55D;
    private static final double WALL_WOBBLE = 0.14D;
    private static final double MIN_SOLID_SHELL = 0.7D;

    private static final float POOL_CHANCE = 0.012F;
    private static final float CEILING_DRIP_CHANCE = 0.06F;
    private static final float FLOOR_DRIP_CHANCE = 0.05F;

    private static final Direction[] HORIZONTAL = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

    public SaltCaveFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (!Config.SALT_CAVES_ENABLED.get()) return false;

        RandomSource random = ctx.random();
        if (random.nextInt(Config.SALT_CAVE_RARITY.get()) != 0) return false;

        WorldGenLevel level = ctx.level();
        BlockPos origin = pickOrigin(level, ctx.origin(), random);
        if (origin == null) return false;

        byte[] grid = buildGrid(Layout.create(random));
        if (!canPlace(level, origin, grid)) return false;

        carve(level, origin, grid, random);
        placePools(level, origin, grid, random);
        decorate(level, origin, grid, random);
        return true;
    }

    @Nullable
    private static BlockPos pickOrigin(WorldGenLevel level, BlockPos origin, RandomSource random) {
        int minY = Math.max(Config.SALT_CAVE_MIN_Y.get(), level.getMinBuildHeight() + BEDROCK_MARGIN + RADIUS_V);
        int maxY = Math.min(Config.SALT_CAVE_MAX_Y.get(), level.getMaxBuildHeight() - RADIUS_V - 1);
        if (minY > maxY) return null;
        if (origin.getY() >= minY && origin.getY() <= maxY) return origin;
        return new BlockPos(origin.getX(), minY + random.nextInt(maxY - minY + 1), origin.getZ());
    }

    private static byte[] buildGrid(Layout layout) {
        byte[] grid = new byte[SIZE_H * SIZE_V * SIZE_H];
        for (int dx = -RADIUS_H; dx <= RADIUS_H; dx++) {
            for (int dy = -RADIUS_V; dy <= RADIUS_V; dy++) {
                for (int dz = -RADIUS_H; dz <= RADIUS_H; dz++) {
                    boolean edge = Math.abs(dx) == RADIUS_H || Math.abs(dy) == RADIUS_V || Math.abs(dz) == RADIUS_H;
                    grid[index(dx, dy, dz)] = edge ? OUTSIDE : layout.classify(dx, dy, dz);
                }
            }
        }
        return grid;
    }

    private static boolean canPlace(WorldGenLevel level, BlockPos origin, byte[] grid) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos neighbor = new BlockPos.MutableBlockPos();
        int shellCells = 0;
        int solidShellCells = 0;

        for (int dx = -RADIUS_H; dx <= RADIUS_H; dx++) {
            for (int dy = -RADIUS_V; dy <= RADIUS_V; dy++) {
                for (int dz = -RADIUS_H; dz <= RADIUS_H; dz++) {
                    byte cell = grid[index(dx, dy, dz)];
                    if (cell == OUTSIDE) continue;

                    pos.setWithOffset(origin, dx, dy, dz);
                    BlockState state = level.getBlockState(pos);
                    if (!state.getFluidState().isEmpty()) return false;
                    if (state.is(BlockTags.FEATURES_CANNOT_REPLACE) || state.getDestroySpeed(level, pos) < 0) return false;

                    if (cell != SHELL) continue;
                    shellCells++;
                    if (isHostRock(state) || state.is(Tags.Blocks.ORES)) {
                        solidShellCells++;
                    } else if (state.isAir()) {
                        for (Direction direction : Direction.values()) {
                            neighbor.setWithOffset(pos, direction);
                            if (!level.getBlockState(neighbor).getFluidState().isEmpty()) return false;
                        }
                    }
                }
            }
        }
        return shellCells > 0 && solidShellCells >= shellCells * MIN_SOLID_SHELL;
    }

    private static void carve(WorldGenLevel level, BlockPos origin, byte[] grid, RandomSource random) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean deep = origin.getY() < 0;

        for (int dx = -RADIUS_H; dx <= RADIUS_H; dx++) {
            for (int dy = -RADIUS_V; dy <= RADIUS_V; dy++) {
                for (int dz = -RADIUS_H; dz <= RADIUS_H; dz++) {
                    byte cell = grid[index(dx, dy, dz)];
                    if (cell == OUTSIDE) continue;

                    pos.setWithOffset(origin, dx, dy, dz);
                    BlockState state = level.getBlockState(pos);

                    if (cell == INSIDE) {
                        if (!state.isAir()) level.setBlock(pos, Blocks.CAVE_AIR.defaultBlockState(), 2);
                    } else if (isHostRock(state)) {
                        level.setBlock(pos, pickWallBlock(random, deep), 2);
                    }
                }
            }
        }
    }

    private static void placePools(WorldGenLevel level, BlockPos origin, byte[] grid, RandomSource random) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int limit = RADIUS_H - 4;

        for (int dx = -limit; dx <= limit; dx++) {
            for (int dy = -RADIUS_V + 1; dy <= RADIUS_V; dy++) {
                for (int dz = -limit; dz <= limit; dz++) {
                    if (grid[index(dx, dy, dz)] != INSIDE || grid[index(dx, dy - 1, dz)] == INSIDE) continue;
                    if (random.nextFloat() >= POOL_CHANCE) continue;

                    pos.setWithOffset(origin, dx, dy - 1, dz);
                    if (isSolid(level, pos)) tryPool(level, pos.immutable(), 1 + random.nextInt(2));
                }
            }
        }
    }

    private static void tryPool(WorldGenLevel level, BlockPos center, int radius) {
        Set<BlockPos> water = new HashSet<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius + 1) continue;
                BlockPos pos = center.offset(dx, 0, dz);
                if (isSolid(level, pos) && level.getBlockState(pos.above()).isAir()) water.add(pos);
            }
        }

        boolean changed = true;
        while (changed && !water.isEmpty()) {
            changed = water.removeIf(pos -> !isContained(level, pos, water));
        }
        if (water.size() < 2) return;

        for (BlockPos pos : water) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
        }
        for (BlockPos pos : water) {
            for (Direction direction : HORIZONTAL) {
                BlockPos rim = pos.relative(direction);
                if (water.contains(rim)) continue;
                BlockState state = level.getBlockState(rim);
                if (isHostRock(state) || isCaveMineral(state)) {
                    level.setBlock(rim, HHModBlocks.SALT_BLOCK.get().defaultBlockState(), 2);
                }
            }
        }
    }

    private static boolean isContained(WorldGenLevel level, BlockPos pos, Set<BlockPos> water) {
        if (!isSolid(level, pos.below())) return false;
        for (Direction direction : HORIZONTAL) {
            BlockPos side = pos.relative(direction);
            if (!water.contains(side) && !isSolid(level, side)) return false;
        }
        return true;
    }

    private static void decorate(WorldGenLevel level, BlockPos origin, byte[] grid, RandomSource random) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int dx = -RADIUS_H; dx <= RADIUS_H; dx++) {
            for (int dy = RADIUS_V; dy >= -RADIUS_V; dy--) {
                for (int dz = -RADIUS_H; dz <= RADIUS_H; dz++) {
                    if (grid[index(dx, dy, dz)] != INSIDE) continue;

                    pos.setWithOffset(origin, dx, dy, dz);
                    if (!level.getBlockState(pos).isAir()) continue;

                    BlockPos above = pos.above();
                    BlockPos below = pos.below();
                    if (level.getBlockState(above).isFaceSturdy(level, above, Direction.DOWN) && random.nextFloat() < CEILING_DRIP_CHANCE) {
                        placeFormation(level, pos.immutable(), Direction.DOWN, random);
                    } else if (level.getBlockState(below).isFaceSturdy(level, below, Direction.UP) && random.nextFloat() < FLOOR_DRIP_CHANCE) {
                        placeFormation(level, pos.immutable(), Direction.UP, random);
                    }
                }
            }
        }
    }

    private static void placeFormation(WorldGenLevel level, BlockPos pos, Direction tip, RandomSource random) {
        int gap = airRun(level, pos, tip, 4);

        if (tip == Direction.DOWN && gap == 3 && isSturdy(level, pos.below(3), Direction.UP) && random.nextFloat() < 0.35F) {
            level.setBlock(pos, drip(Direction.DOWN, SaltDripBlock.SaltDripThickness.LARGE), 2);
            level.setBlock(pos.below(), drip(Direction.DOWN, SaltDripBlock.SaltDripThickness.POINT_MERGE), 2);
            level.setBlock(pos.below(2), drip(Direction.UP, SaltDripBlock.SaltDripThickness.LARGE), 2);
            return;
        }

        if (gap >= 2 && random.nextFloat() < 0.4F) {
            level.setBlock(pos, drip(tip, SaltDripBlock.SaltDripThickness.LARGE), 2);
            level.setBlock(pos.relative(tip), drip(tip, SaltDripBlock.SaltDripThickness.POINT), 2);
            return;
        }

        SaltDripBlock.SaltDripThickness thickness = random.nextBoolean()
                ? SaltDripBlock.SaltDripThickness.MEDIUM
                : SaltDripBlock.SaltDripThickness.SMALL;
        level.setBlock(pos, drip(tip, thickness), 2);
    }

    private static int airRun(WorldGenLevel level, BlockPos start, Direction direction, int max) {
        int run = 0;
        BlockPos.MutableBlockPos pos = start.mutable();
        while (run < max && level.getBlockState(pos).isAir()) {
            run++;
            pos.move(direction);
        }
        return run;
    }

    private static BlockState drip(Direction tip, SaltDripBlock.SaltDripThickness thickness) {
        return HHModBlocks.SALT_DRIP.get().defaultBlockState()
                .setValue(SaltDripBlock.TIP_DIRECTION, tip)
                .setValue(SaltDripBlock.THICKNESS, thickness);
    }

    private static BlockState pickWallBlock(RandomSource random, boolean deep) {
        int roll = random.nextInt(100);
        if (roll < 58) return HHModBlocks.SALT_BLOCK.get().defaultBlockState();
        if (roll < 66) return HHModBlocks.LIGHTLY_LICKED_SALT_BLOCK.get().defaultBlockState();
        if (roll < 70) return HHModBlocks.WELL_LICKED_SALT_BLOCK.get().defaultBlockState();
        if (roll < 88) return deep ? Blocks.TUFF.defaultBlockState() : Blocks.CALCITE.defaultBlockState();
        return deep ? Blocks.CALCITE.defaultBlockState() : Blocks.TUFF.defaultBlockState();
    }

    private static boolean isHostRock(BlockState state) {
        return state.is(BlockTags.STONE_ORE_REPLACEABLES) || state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    }

    private static boolean isCaveMineral(BlockState state) {
        return state.getBlock() instanceof SaltBlock
                || state.is(Blocks.CALCITE)
                || state.is(Blocks.TUFF);
    }

    private static boolean isSolid(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isAir() && state.getFluidState().isEmpty() && state.isCollisionShapeFullBlock(level, pos);
    }

    private static boolean isSturdy(WorldGenLevel level, BlockPos pos, Direction face) {
        return level.getBlockState(pos).isFaceSturdy(level, pos, face);
    }

    private static boolean inBounds(int dx, int dy, int dz) {
        return Math.abs(dx) <= RADIUS_H && Math.abs(dy) <= RADIUS_V && Math.abs(dz) <= RADIUS_H;
    }

    private static int index(int dx, int dy, int dz) {
        return ((dx + RADIUS_H) * SIZE_V + (dy + RADIUS_V)) * SIZE_H + (dz + RADIUS_H);
    }

    private record Chamber(double x, double y, double z, double radiusH, double radiusV) {
        double distance(double px, double py, double pz, double grow) {
            double h = radiusH + grow;
            double v = (py < y ? radiusV * FLOOR_FLATTEN : radiusV) + grow;
            double ox = px - x;
            double oy = py - y;
            double oz = pz - z;
            return Math.sqrt((ox * ox + oz * oz) / (h * h) + (oy * oy) / (v * v));
        }
    }

    private record Tunnel(double ax, double ay, double az, double bx, double by, double bz, double radius) {
        double distance(double px, double py, double pz) {
            double sx = bx - ax;
            double sy = by - ay;
            double sz = bz - az;
            double lengthSqr = sx * sx + sy * sy + sz * sz;
            double t = lengthSqr == 0 ? 0 : ((px - ax) * sx + (py - ay) * sy + (pz - az) * sz) / lengthSqr;
            t = Math.max(0, Math.min(1, t));
            double cx = px - (ax + sx * t);
            double cy = py - (ay + sy * t);
            double cz = pz - (az + sz * t);
            return Math.sqrt(cx * cx + cy * cy + cz * cz);
        }
    }

    private record Layout(List<Chamber> chambers, List<Tunnel> tunnels, SimplexNoise noise) {
        static Layout create(RandomSource random) {
            List<Chamber> chambers = new ArrayList<>();
            List<Tunnel> tunnels = new ArrayList<>();

            Chamber main = new Chamber(0, 0, 0, 8 + random.nextInt(3), 6 + random.nextInt(2));
            chambers.add(main);

            int lobes = 2 + random.nextInt(3);
            double startAngle = random.nextDouble() * Math.PI * 2;
            for (int i = 0; i < lobes; i++) {
                double radiusH = 4 + random.nextInt(2);
                double angle = startAngle + (Math.PI * 2 / lobes) * i + (random.nextDouble() - 0.5) * 0.8;
                double distance = Math.min(maxDistance(radiusH), main.radiusH() - 1 + random.nextInt(3));
                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;
                chambers.add(new Chamber(x, random.nextInt(3) - 1, z, radiusH, 4));
            }

            if (random.nextBoolean()) {
                boolean upper = random.nextBoolean();
                double radiusH = 5 + random.nextInt(2);
                double radiusV = 3 + random.nextInt(2);
                double x = random.nextInt(9) - 4;
                double z = random.nextInt(9) - 4;
                double y = upper ? main.radiusV() + 1 : -(main.radiusV() * FLOOR_FLATTEN + radiusV + 1);
                chambers.add(new Chamber(x, y, z, radiusH, radiusV));
                tunnels.add(new Tunnel(0, upper ? 1 : -1, 0, x, y, z, 2.0 + random.nextDouble() * 0.5));
            }

            return new Layout(chambers, tunnels, new SimplexNoise(random));
        }

        private static double maxDistance(double radiusH) {
            return RADIUS_H - 1 - (radiusH + SHELL_THICKNESS) * (1 + WALL_WOBBLE);
        }

        byte classify(int dx, int dy, int dz) {
            double wobble = 1 + noise.getValue(dx * 0.18, dy * 0.25, dz * 0.18) * WALL_WOBBLE;
            boolean shell = false;

            for (Chamber chamber : chambers) {
                if (chamber.distance(dx, dy, dz, 0) <= wobble) return INSIDE;
                if (chamber.distance(dx, dy, dz, SHELL_THICKNESS) <= wobble) shell = true;
            }
            for (Tunnel tunnel : tunnels) {
                double distance = tunnel.distance(dx, dy, dz);
                if (distance <= tunnel.radius() * wobble) return INSIDE;
                if (distance <= (tunnel.radius() + SHELL_THICKNESS) * wobble) shell = true;
            }
            return shell ? SHELL : OUTSIDE;
        }
    }
}