package alabaster.hearthandharvest.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class HHModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "hearthandharvest");

    // Ticks remaining until a fed animal drops manure. 0 = no pending drop.
    public static final Supplier<AttachmentType<Integer>> MANURE_POOP_TIMER =
            ATTACHMENT_TYPES.register("manure_poop_timer", () ->
                    AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
            );

    // Game time of the player's last manual poop. -300 default = no cooldown on first use.
    public static final Supplier<AttachmentType<Long>> PLAYER_LAST_POOP_TIME =
            ATTACHMENT_TYPES.register("player_last_poop_time", () ->
                    AttachmentType.builder(() -> -300L).serialize(Codec.LONG).build()
            );

    // Ticks remaining for fly particles to follow an entity hit by a manure projectile.
    public static final Supplier<AttachmentType<Integer>> MANURE_FLY_TICKS =
            ATTACHMENT_TYPES.register("manure_fly_ticks", () ->
                    AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
            );

    // Ticks remaining before a chicken can be plucked again. 0 = ready.
    public static final Supplier<AttachmentType<Integer>> PLUCK_COOLDOWN =
            ATTACHMENT_TYPES.register("pluck_cooldown", () ->
                    AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
            );

    // Stores the actual ItemStack so enchantments are preserved on removal.
    // Empty stack = no horseshoe equipped.
    public static final Supplier<AttachmentType<ItemStack>> HORSESHOE_ITEM =
            ATTACHMENT_TYPES.register("horseshoe_item",
                    () -> AttachmentType.builder(() -> ItemStack.EMPTY)
                            .serialize(ItemStack.OPTIONAL_CODEC).build());
}