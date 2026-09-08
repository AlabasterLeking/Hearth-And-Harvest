package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID)
public class RandomArmorStands {

    public static final String RANDOM_ARMOR_TAG = "hearthandharvest:random_armor";

    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD
    };

    private static final Item[] LEATHER = {
            Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET
    };
    private static final Item[] CHAINMAIL = {
            Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET
    };
    private static final Item[] IRON = {
            Items.IRON_BOOTS, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE, Items.IRON_HELMET
    };
    private static final Item[][] SETS = { LEATHER, CHAINMAIL, IRON };

    private static final int[] LEATHER_DYES = {
            0x8C6239, 0x6B4423, 0xA0522D, 0x556B2F, 0x8B7355, 0x704214, 0x9C6B3C, 0x5C4033
    };

    private static final float EMPTY_SLOT_CHANCE = 0.25F;
    private static final float DYE_CHANCE = 0.6F;
    private static final float WEAR_CHANCE = 0.7F;

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof ArmorStand stand)) return;
        if (!stand.getTags().contains(RANDOM_ARMOR_TAG)) return;

        stand.removeTag(RANDOM_ARMOR_TAG);

        RandomSource random = stand.getRandom();
        for (int i = 0; i < SLOTS.length; i++) {
            if (random.nextFloat() < EMPTY_SLOT_CHANCE) {
                stand.setItemSlot(SLOTS[i], ItemStack.EMPTY);
                continue;
            }
            stand.setItemSlot(SLOTS[i], makePiece(SETS[random.nextInt(SETS.length)], i, random));
        }
    }

    private static ItemStack makePiece(Item[] set, int slotIndex, RandomSource random) {
        ItemStack stack = new ItemStack(set[slotIndex]);

        if (set == LEATHER && random.nextFloat() < DYE_CHANCE) {
            int colour = LEATHER_DYES[random.nextInt(LEATHER_DYES.length)];
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(colour, true));
        }

        int max = stack.getMaxDamage();
        if (max > 0 && random.nextFloat() < WEAR_CHANCE) {
            stack.setDamageValue(random.nextInt(Math.max(1, (max * 3) / 4)));
        }

        return stack;
    }
}