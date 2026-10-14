package alabaster.hearthandharvest.common.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record SeedPouchContents(Item seedType, int count, int plantRadius) {
    public static final int MAX_COUNT = 512;
    public static final int[] RADII = {0, 1, 2, 3, 4};
    public static final String[] AREA_NAMES = {"1×1", "3×3", "5×5", "7×7", "9×9"};

    public SeedPouchContents(Item seedType, int count) {
        this(seedType, count, 0);
    }

    public SeedPouchContents withCount(int newCount) {
        return new SeedPouchContents(seedType, newCount, plantRadius);
    }

    public SeedPouchContents withRadius(int newRadius) {
        return new SeedPouchContents(seedType, count, newRadius);
    }

    public static final Codec<SeedPouchContents> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("seed_type").forGetter(SeedPouchContents::seedType),
                    Codec.INT.fieldOf("count").forGetter(SeedPouchContents::count),
                    Codec.INT.optionalFieldOf("plant_radius", 0).forGetter(SeedPouchContents::plantRadius)
            ).apply(instance, SeedPouchContents::new)
    );
}
