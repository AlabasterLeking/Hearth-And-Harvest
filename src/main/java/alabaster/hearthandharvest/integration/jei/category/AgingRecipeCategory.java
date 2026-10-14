package alabaster.hearthandharvest.integration.jei.category;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.crafting.CaskRecipe;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.utilities.HHTextUtils;
import alabaster.hearthandharvest.integration.jei.HHRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
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
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AgingRecipeCategory implements IRecipeCategory<CaskRecipe>
{
    protected final IDrawable timeIcon;
    protected final IDrawable expIcon;
    protected final IDrawableAnimated arrow;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public AgingRecipeCategory(IGuiHelper helper) {
        title = HHTextUtils.getTranslation("jei.aging");
        ResourceLocation backgroundImage = new ResourceLocation(HearthAndHarvest.MODID, "textures/gui/jei/jei_cask_gui.png");
        background = helper.createDrawable(backgroundImage, 33, 16, 116, 42);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(HHModItems.CASK.get()));
        timeIcon = helper.createDrawable(backgroundImage, 176, 32, 8, 11);
        expIcon = helper.createDrawable(backgroundImage, 176, 43, 9, 9);
        arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<CaskRecipe> getRecipeType() {
        return HHRecipeTypes.AGING;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, CaskRecipe holder, IFocusGroup focusGroup) {
        CaskRecipe recipe = holder;
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
        ItemStack resultStack = RecipeUtils.getResultItem(recipe);

        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                int inputIndex = row * 2 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, (column * borderSlotSize) + 6, (row * borderSlotSize) + 4)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 91, 12).addItemStack(resultStack);
    }

    public void draw(CaskRecipe holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 51, 12);
        timeIcon.draw(guiGraphics, 54, 5);
        if (holder.getExperience() > 0) {
            expIcon.draw(guiGraphics, 53, 24);
        }
    }

    public List<Component> getTooltipStrings(CaskRecipe holder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        CaskRecipe recipe = holder;
        if (ClientRenderUtils.isCursorInsideBounds(47, 4, 22, 28, mouseX, mouseY)) {
            List<Component> tooltipStrings = new ArrayList<>();

            int cookTime = recipe.getCookTime();
            if (cookTime > 0) {
                int cookTimeSeconds = cookTime / 20;
                tooltipStrings.add(Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds));
            }
            float experience = recipe.getExperience();
            if (experience > 0) {
                tooltipStrings.add(Component.translatable("gui.jei.category.smelting.experience", experience));
            }

            return tooltipStrings;
        }
        return Collections.emptyList();
    }
}
