package alabaster.hearthandharvest.common.registry;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.WeakHashMap;

public class HHModAttachments {
    private static final String ROOT = "hearthandharvest";

    // Ticks remaining until a fed animal drops manure. 0 = no pending drop.
    public static final IntData MANURE_POOP_TIMER = new IntData("manure_poop_timer", 0);

    // Game time of the player's last manual poop. -300 default = no cooldown on first use.
    public static final LongData PLAYER_LAST_POOP_TIME = new LongData("player_last_poop_time", -300L);

    // Ticks remaining for fly particles to follow an entity hit by a manure projectile.
    public static final IntData MANURE_FLY_TICKS = new IntData("manure_fly_ticks", 0);

    // Ticks remaining before a chicken can be plucked again. 0 = ready.
    public static final IntData PLUCK_COOLDOWN = new IntData("pluck_cooldown", 0);

    // Packed position of the nest a chicken is walking to. 0 = no claim. Not saved.
    public static final TransientLongData CLAIMED_NEST = new TransientLongData(0L);

    // Stores the actual ItemStack so enchantments are preserved on removal.
    // Empty stack = no horseshoe equipped.
    public static final ItemStackData HORSESHOE_ITEM = new ItemStackData("horseshoe_item");

    @Nullable
    private static CompoundTag peek(Entity entity) {
        CompoundTag persistent = entity.getPersistentData();
        return persistent.contains(ROOT, CompoundTag.TAG_COMPOUND) ? persistent.getCompound(ROOT) : null;
    }

    private static void clear(Entity entity, String key) {
        CompoundTag tag = peek(entity);
        if (tag == null) return;
        tag.remove(key);
        if (tag.isEmpty()) entity.getPersistentData().remove(ROOT);
    }

    private static CompoundTag data(Entity entity) {
        CompoundTag persistent = entity.getPersistentData();
        if (!persistent.contains(ROOT, CompoundTag.TAG_COMPOUND)) persistent.put(ROOT, new CompoundTag());
        return persistent.getCompound(ROOT);
    }

    public record IntData(String key, int fallback) {
        public int get(Entity entity) {
            CompoundTag tag = peek(entity);
            return tag != null && tag.contains(key) ? tag.getInt(key) : fallback;
        }

        public void set(Entity entity, int value) {
            if (value == fallback) clear(entity, key);
            else data(entity).putInt(key, value);
        }
    }

    public record LongData(String key, long fallback) {
        public long get(Entity entity) {
            CompoundTag tag = peek(entity);
            return tag != null && tag.contains(key) ? tag.getLong(key) : fallback;
        }

        public void set(Entity entity, long value) {
            data(entity).putLong(key, value);
        }
    }

    public static final class TransientLongData {
        private final long fallback;
        private final Map<Entity, Long> values = new WeakHashMap<>();

        TransientLongData(long fallback) {
            this.fallback = fallback;
        }

        public long get(Entity entity) {
            return values.getOrDefault(entity, fallback);
        }

        public void set(Entity entity, long value) {
            values.put(entity, value);
        }
    }

    public record ItemStackData(String key) {
        public ItemStack get(Entity entity) {
            CompoundTag tag = peek(entity);
            return tag != null && tag.contains(key, CompoundTag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound(key)) : ItemStack.EMPTY;
        }

        public void set(Entity entity, ItemStack stack) {
            if (stack.isEmpty()) clear(entity, key);
            else data(entity).put(key, stack.save(new CompoundTag()));
        }
    }
}
