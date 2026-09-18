package alabaster.hearthandharvest.common.block.entity;

import alabaster.hearthandharvest.common.block.JarBlock;
import alabaster.hearthandharvest.common.registry.HHModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Arrays;

public class JarBlockEntity extends BlockEntity {

    private static final int[][] ROTATION_PERMUTATIONS = {
            {0, 1, 2, 3},
            {1, 3, 0, 2},
            {3, 2, 1, 0},
            {2, 0, 3, 1}
    };
    private static final int[][] MIRROR_PERMUTATIONS = {
            {0, 1, 2, 3},
            {2, 3, 0, 1},
            {1, 0, 3, 2}
    };
    private static final Mirror[] RECONCILE_MIRRORS = {Mirror.NONE, Mirror.LEFT_RIGHT};

    private final Item[] slots = new Item[4];

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(HHModBlockEntities.JAR.get(), pos, state);
    }

    public void setSlot(int index, @Nullable Item item) {
        if (index < 0 || index >= 4) return;
        slots[index] = (item == Items.AIR) ? null : item;
        setChanged();
    }

    @Nullable
    public Item getSlot(int index) {
        if (index < 0 || index >= 4) return null;
        return slots[index];
    }

    public int getCount() {
        int count = 0;
        for (Item slot : slots) if (slot != null) count++;
        return count;
    }

    public void dropAllJars(Level level, BlockPos pos) {
        for (Item item : slots) {
            if (item != null && item != Items.AIR) {
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(item));
            }
        }
    }

    public static int[] slotPermutation(Mirror mirror, Rotation rotation) {
        int[] mirrored = MIRROR_PERMUTATIONS[mirror.ordinal()];
        int[] rotated = ROTATION_PERMUTATIONS[rotation.ordinal()];
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) result[i] = rotated[mirrored[i]];
        return result;
    }

    public static CompoundTag transformTag(CompoundTag tag, Mirror mirror, Rotation rotation) {
        CompoundTag result = tag.copy();
        if (!tag.contains("slots")) return result;
        CompoundTag source = tag.getCompound("slots");
        CompoundTag moved = new CompoundTag();
        int[] permutation = slotPermutation(mirror, rotation);
        for (int i = 0; i < 4; i++) {
            String key = "slot_" + i;
            if (source.contains(key)) moved.putString("slot_" + permutation[i], source.getString(key));
        }
        result.put("slots", moved);
        return result;
    }

    private int storedMask() {
        int mask = 0;
        for (int i = 0; i < 4; i++) if (slots[i] != null) mask |= 1 << i;
        return mask;
    }

    private static int permuteMask(int mask, int[] permutation) {
        int result = 0;
        for (int i = 0; i < 4; i++) if ((mask & (1 << i)) != 0) result |= 1 << permutation[i];
        return result;
    }

    private void alignSlotsToState() {
        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof JarBlock)) return;
        int stateMask = JarBlock.slotMask(state);
        int stored = storedMask();
        if (stateMask == stored) return;

        for (Mirror mirror : RECONCILE_MIRRORS) {
            for (Rotation rotation : Rotation.values()) {
                int[] permutation = slotPermutation(mirror, rotation);
                if (permuteMask(stored, permutation) != stateMask) continue;
                Item[] previous = slots.clone();
                Arrays.fill(slots, null);
                for (int i = 0; i < 4; i++) {
                    if (previous[i] != null) slots[permutation[i]] = previous[i];
                }
                return;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag slotsTag = new CompoundTag();
        for (int i = 0; i < 4; i++) {
            if (slots[i] != null) {
                slotsTag.putString("slot_" + i, BuiltInRegistries.ITEM.getKey(slots[i]).toString());
            }
        }
        tag.put("slots", slotsTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        readSlots(tag);
        alignSlotsToState();
    }

    private void readSlots(CompoundTag tag) {
        Arrays.fill(slots, null);
        CompoundTag slotsTag = tag.getCompound("slots");
        for (int i = 0; i < 4; i++) {
            String key = "slot_" + i;
            if (slotsTag.contains(key)) {
                ResourceLocation rl = ResourceLocation.tryParse(slotsTag.getString(key));
                if (rl == null) continue;
                Item item = BuiltInRegistries.ITEM.get(rl);
                if (item != Items.AIR) slots[i] = item;
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        readSlots(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) readSlots(tag);
    }
}