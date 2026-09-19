package alabaster.hearthandharvest.common.entity.crow;

import alabaster.hearthandharvest.Config;
import alabaster.hearthandharvest.common.block.NestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class CrowSpawnRules {

    public static boolean canSpawnCrow(EntityType<? extends PathfinderMob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {

        if (MobSpawnType.isSpawner(spawnType)) return true;

        if (!PathfinderMob.checkMobSpawnRules(type, level, spawnType, pos, random))
            return false;

        if (!level.getBlockState(pos.below()).isSolid())
            return false;

        int radius = Config.CROW_SPAWN_RADIUS.get();
        int cropRequirement = Config.CROW_SPAWN_NUMBER_OF_CROPS.get();

        if (Config.CROW_SPAWN_NEAR_NESTS.get() && hasNearbyGeneratedNest(level, pos, radius)) return true;
        if (cropRequirement == 0) return false;

        return countNearbyCrops(level, pos, radius) >= cropRequirement;
    }

    private static boolean hasNearbyGeneratedNest(ServerLevelAccessor level, BlockPos pos, int radius) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -4; dy <= 8; dy++) {
                    cursor.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    BlockState state = level.getBlockState(cursor);

                    if (state.getBlock() instanceof NestBlock &&
                            state.getValue(NestBlock.GENERATED)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int countNearbyCrops(ServerLevelAccessor level, BlockPos pos, int radius) {
        int count = 0;
        int needed = Config.CROW_SPAWN_NUMBER_OF_CROPS.get();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -2; dy <= 2; dy++) {
                    cursor.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);

                    if (level.getBlockState(cursor).is(BlockTags.CROPS)) {
                        count++;
                        if (count >= needed) return count;
                    }
                }
            }
        }
        return count;
    }
}