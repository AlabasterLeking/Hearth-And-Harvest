package alabaster.hearthandharvest.integration.jei.category;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
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
import mezz.jei.api.neoforge.NeoForgeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KegRecipeCategory implements IRecipeCategory<RecipeHolder<KegRecipe>>
{
    private static final int INPUT_TANK_X = 4;
    private static final int OUTPUT_TANK_X = 128;
    private static final int TANK_Y = 4;
    private static final int TANK_WIDTH = 16;
    private static final int TANK_HEIGHT = 40;

    private static final int INPUT_SLOT_X = 26;
    private static final int OUTPUT_SLOT_X = 102;
    private static final int SLOT_TOP_Y = 4;
    private static final int SLOT_BOTTOM_Y = 24;

    private static final int ARROW_X = 60;
    private static final int ARROW_Y = 16;

    protected final IDrawable timeIcon;
    protected final IDrawable expIcon;
    protected final IDrawableAnimated arrow;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public KegRecipeCategory(IGuiHelper helper) {
        title = HHTextUtils.getTranslation("jei.fermenting");
        ResourceLocation backgroundImage = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "textures/gui/jei/jei_keg_gui.png");
        background = helper.createDrawable(backgroundImage, 0, 0, 148, 48);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(HHModItems.KEG.get()));
        timeIcon = helper.createDrawable(backgroundImage, 176, 32, 8, 11);
        expIcon = helper.createDrawable(backgroundImage, 176, 43, 9, 9);
        arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<RecipeHolder<KegRecipe>> getRecipeType() {
        return HHRecipeTypes.FERMENTING;
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

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<KegRecipe> holder, IFocusGroup focusGroup) {
        KegRecipe recipe = holder.value();

        FluidStack inputFluid = recipe.getInputFluid();
        if (!inputFluid.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_TANK_X, TANK_Y)
                    .setFluidRenderer(KegBlockEntity.TANK_CAPACITY, false, TANK_WIDTH, TANK_HEIGHT)
                    .addIngredient(NeoForgeTypes.FLUID_STACK, inputFluid);
        }

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int index = 0; index < ingredients.size() && index < 2; ++index) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_SLOT_X, index == 0 ? SLOT_TOP_Y : SLOT_BOTTOM_Y)
                    .addItemStacks(Arrays.asList(ingredients.get(index).getItems()));
        }

        ItemStack resultItem = recipe.getResultItem();
        if (!resultItem.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_SLOT_X, SLOT_TOP_Y).addItemStack(resultItem);
        }

        FluidStack resultFluid = recipe.getResultFluid();
        if (!resultFluid.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_TANK_X, TANK_Y)
                    .setFluidRenderer(KegBlockEntity.TANK_CAPACITY, false, TANK_WIDTH, TANK_HEIGHT)
                    .addIngredient(NeoForgeTypes.FLUID_STACK, resultFluid);
        }
    }

    @Override
    public void draw(RecipeHolder<KegRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, ARROW_X, ARROW_Y);
        timeIcon.draw(guiGraphics, ARROW_X + 3, ARROW_Y - 11);
        if (holder.value().getExperience() > 0) {
            expIcon.draw(guiGraphics, ARROW_X + 3, ARROW_Y + 19);
        }
    }

    @Override
    public List<Component> getTooltipStrings(RecipeHolder<KegRecipe> holder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (!ClientRenderUtils.isCursorInsideBounds(ARROW_X - 4, ARROW_Y - 12, 30, 40, mouseX, mouseY)) {
            return Collections.emptyList();
        }

        KegRecipe recipe = holder.value();
        List<Component> tooltip = new ArrayList<>();

        int fermentTime = recipe.getFermentTime();
        if (fermentTime > 0) {
            tooltip.add(Component.translatable("gui.jei.category.smelting.time.seconds", fermentTime / 20));
        }
        float experience = recipe.getExperience();
        if (experience > 0) {
            tooltip.add(Component.translatable("gui.jei.category.smelting.experience", experience));
        }
        return tooltip;
    }
}