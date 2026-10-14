package alabaster.hearthandharvest.common.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.UnaryOperator;

public final class ItemComponent<T> {
    private final String key;
    private final Codec<T> codec;

    public ItemComponent(String key, Codec<T> codec) {
        this.key = key;
        this.codec = codec;
    }

    public String key() {
        return key;
    }

    public boolean has(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(key);
    }

    @Nullable
    public T get(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(key)) return null;
        return read(tag.get(key));
    }

    public T getOrDefault(ItemStack stack, T fallback) {
        T value = get(stack);
        return value == null ? fallback : value;
    }

    public void set(ItemStack stack, @Nullable T value) {
        if (value == null) {
            remove(stack);
            return;
        }
        codec.encodeStart(NbtOps.INSTANCE, value).result().ifPresent(encoded -> stack.getOrCreateTag().put(key, encoded));
    }

    public void update(ItemStack stack, T fallback, UnaryOperator<T> updater) {
        set(stack, updater.apply(getOrDefault(stack, fallback)));
    }

    public void remove(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        tag.remove(key);
        if (tag.isEmpty()) stack.setTag(null);
    }

    @Nullable
    public T read(@Nullable Tag tag) {
        if (tag == null) return null;
        return codec.parse(NbtOps.INSTANCE, tag).result().orElse(null);
    }
}
