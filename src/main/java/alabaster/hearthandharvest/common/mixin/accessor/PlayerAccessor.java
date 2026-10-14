package alabaster.hearthandharvest.common.mixin.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerAccessor {
    @Invoker("setShoulderEntityLeft")
    void hearthandharvest$setShoulderEntityLeft(CompoundTag tag);

    @Invoker("setShoulderEntityRight")
    void hearthandharvest$setShoulderEntityRight(CompoundTag tag);
}
