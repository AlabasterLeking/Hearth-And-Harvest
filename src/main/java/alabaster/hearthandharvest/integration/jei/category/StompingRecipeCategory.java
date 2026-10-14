package alabaster.hearthandharvest.integration.jei.category;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.crafting.StompingBasinRecipe;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.utilities.HHTextUtils;
import alabaster.hearthandharvest.integration.jei.HHRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StompingRecipeCategory implements IRecipeCategory<StompingBasinRecipe> {

    private static final int FLUID_TANK_WIDTH = 26;
    private static final int FLUID_TANK_HEIGHT = 16;
    private static final int FLUID_TANK_CAPACITY = 1000;

    private static final int INPUT_COL_0 = 6;
    private static final int INPUT_COL_1 = 25;
    private static final int INPUT_ROW_0 = 4;
    private static final int INPUT_ROW_1 = 22;

    private static final int ARROW_X = 48;
    private static final int ARROW_Y = 20;

    private static final int FLUID_TANK_X = 78;
    private static final int FLUID_TANK_Y = 24;

    private static final int ITEM_OUTPUT_X = 83;
    private static final int ITEM_OUTPUT_Y = 2;

    protected final IDrawable arrow;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public StompingRecipeCategory(IGuiHelper helper) {
        title = HHTextUtils.getTranslation("jei.stomping");
        ResourceLocation backgroundImage = new ResourceLocation(
                HearthAndHarvest.MODID, "textures/gui/jei/jei_stomping_gui.png");

        background = helper.createDrawable(backgroundImage, 33, 16, 116, 42);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(HHModItems.STOMPING_BASIN.get()));
        arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<StompingBasinRecipe> getRecipeType() {
        return HHRecipeTypes.STOMPING;
    }

    @Override
    public Component getTitle() { return title; }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StompingBasinRecipe holder, IFocusGroup focusGroup) {
        StompingBasinRecipe recipe = holder;

        List<Map.Entry<List<ItemStack>, Integer>> grouped = groupIngredients(recipe.getIngredients());

        int[] cols = { INPUT_COL_0, INPUT_COL_1 };
        int[] rows = { INPUT_ROW_0, INPUT_ROW_1 };

        for (int i = 0; i < grouped.size() && i < 4; i++) {
            Map.Entry<List<ItemStack>, Integer> entry = grouped.get(i);
            int count = entry.getValue();
            List<ItemStack> stacks = entry.getKey().stream()
                    .map(s -> s.copyWithCount(count))
                    .collect(Collectors.toList());

            builder.addSlot(RecipeIngredientRole.INPUT, cols[i % 2], rows[i / 2])
                    .addItemStacks(stacks);
        }

        FluidStack resultFluid = recipe.getResultFluid();
        if (!resultFluid.isEmpty()) {
            IDrawable cornerOverlay = new IDrawable() {
                @Override public int getWidth()  { return FLUID_TANK_WIDTH; }
                @Override public int getHeight() { return FLUID_TANK_HEIGHT; }

                @Override
                public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY) {
                    ResourceLocation tex = new ResourceLocation(HearthAndHarvest.MODID, "textures/gui/jei/jei_stomping_gui.png");
                    int texX = 33 + FLUID_TANK_X;
                    int texY = 16 + FLUID_TANK_Y;
                    int w = FLUID_TANK_WIDTH;
                    int h = FLUID_TANK_HEIGHT;
                    guiGraphics.blit(tex, FLUID_TANK_X, FLUID_TANK_Y, texX, texY, 1, 1, 256, 256);
                    guiGraphics.blit(tex, FLUID_TANK_X + w - 1, FLUID_TANK_Y, texX + w-1, texY, 1, 1, 256, 256);
                    guiGraphics.blit(tex, FLUID_TANK_X, FLUID_TANK_Y + h - 1, texX, texY + h-1, 1, 1, 256, 256);
                    guiGraphics.blit(tex, FLUID_TANK_X + w - 1, FLUID_TANK_Y + h - 1, texX + w-1, texY + h-1, 1, 1, 256, 256);
                }
            };

            builder.addSlot(RecipeIngredientRole.OUTPUT, FLUID_TANK_X, FLUID_TANK_Y)
                    .addIngredient(ForgeTypes.FLUID_STACK, resultFluid)
                    .setFluidRenderer(FLUID_TANK_CAPACITY, false, FLUID_TANK_WIDTH, FLUID_TANK_HEIGHT)
                    .setOverlay(cornerOverlay, 0, 0);
        }

        ItemStack resultItem = recipe.getResultItem();
        if (!resultItem.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, ITEM_OUTPUT_X, ITEM_OUTPUT_Y)
                    .addItemStack(resultItem);
        }
    }

    @Override
    public void draw(StompingBasinRecipe holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, ARROW_X, ARROW_Y);
    }

    @Override
    public List<Component> getTooltipStrings(StompingBasinRecipe holder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= ARROW_X && mouseX <= ARROW_X + 24 &&
                mouseY >= 2 && mouseY <= ARROW_Y + 20) {
            return List.of(Component.translatable("hearthandharvest.jei.stomping.tooltip"));
        }
        return List.of();
    }

    private static List<Map.Entry<List<ItemStack>, Integer>> groupIngredients(NonNullList<Ingredient> ingredients) {
        List<Map.Entry<List<ItemStack>, Integer>> grouped = new ArrayList<>();

        outer:
        for (Ingredient ing : ingredients) {
            ItemStack[] incoming = ing.getItems();
            for (Map.Entry<List<ItemStack>, Integer> entry : grouped) {
                if (sameIngredient(entry.getKey(), incoming)) {
                    entry.setValue(entry.getValue() + 1);
                    continue outer;
                }
            }
            grouped.add(new AbstractMap.SimpleEntry<>(
                    new ArrayList<>(Arrays.asList(incoming)), 1));
        }

        return grouped;
    }

    private static boolean sameIngredient(List<ItemStack> existing, ItemStack[] incoming) {
        if (existing.size() != incoming.length) return false;
        for (int i = 0; i < existing.size(); i++) {
            if (!ItemStack.isSameItem(existing.get(i), incoming[i])) return false;
        }
        return true;
    }
}