package alabaster.hearthandharvest.common.block;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.trellis.CropTrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisBlock;
import alabaster.hearthandharvest.common.block.trellis.TrellisPlant;
import alabaster.hearthandharvest.common.registry.HHModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

public class MigrationBlock extends Block {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

    private final TrellisPlant targetPlant;

    public MigrationBlock(TrellisPlant targetPlant) {
        super(Block.Properties.of().strength(2.0F).randomTicks().noLootTable().noOcclusion().noCollission());
        this.targetPlant = targetPlant;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) level.scheduleTick(pos, this, 1);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) level.scheduleTick(pos, this, 1);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level instanceof ServerLevel serverLevel) convert(state, serverLevel, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        convert(state, level, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        convert(state, level, pos);
    }

    private void convert(BlockState state, ServerLevel level, BlockPos pos) {
        level.setBlock(pos, HHModBlocks.CROP_TRELLIS.get().defaultBlockState()
                .setValue(TrellisBlock.MIDDLE_EW, true)
                .setValue(TrellisBlock.MIDDLE_NS, true)
                .setValue(CropTrellisBlock.PLANT, targetPlant)
                .setValue(CropTrellisBlock.AGE, state.getValue(AGE)), Block.UPDATE_ALL);
    }

    @Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID)
    public static class ChunkConversion {
        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load event) {
            if (!(event.getLevel() instanceof ServerLevel level) || !(event.getChunk() instanceof LevelChunk chunk)) return;

            List<BlockPos> stubs = new ArrayList<>();
            ChunkPos chunkPos = chunk.getPos();
            LevelChunkSection[] sections = chunk.getSections();
            for (int i = 0; i < sections.length; i++) {
                LevelChunkSection section = sections[i];
                if (section.hasOnlyAir() || !section.maybeHas(state -> state.getBlock() instanceof MigrationBlock)) continue;
                int baseY = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(i));
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            if (section.getBlockState(x, y, z).getBlock() instanceof MigrationBlock) {
                                stubs.add(new BlockPos(chunkPos.getBlockX(x), baseY + y, chunkPos.getBlockZ(z)));
                            }
                        }
                    }
                }
            }
            if (stubs.isEmpty()) return;

            MinecraftServer server = level.getServer();
            server.tell(new TickTask(server.getTickCount() + 1, () -> {
                for (BlockPos pos : stubs) {
                    if (!level.isLoaded(pos)) continue;
                    BlockState state = level.getBlockState(pos);
                    if (state.getBlock() instanceof MigrationBlock migration) migration.convert(state, level, pos);
                }
            }));
        }
    }
}