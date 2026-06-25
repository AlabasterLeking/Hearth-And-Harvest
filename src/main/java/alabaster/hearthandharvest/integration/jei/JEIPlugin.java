package alabaster.hearthandharvest.integration.jei;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.client.gui.CaskGUI;
import alabaster.hearthandharvest.common.block.entity.container.CaskMenu;
import alabaster.hearthandharvest.common.crafting.BottleCrateRecipe;
import alabaster.hearthandharvest.common.registry.HHModItems;
import alabaster.hearthandharvest.common.registry.HHModMenuTypes;
import alabaster.hearthandharvest.common.registry.HHModRecipeTypes;
import alabaster.hearthandharvest.common.utilities.HHTextUtils;
import alabaster.hearthandharvest.integration.jei.category.AgingRecipeCategory;
import alabaster.hearthandharvest.integration.jei.category.StompingRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new AgingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new StompingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        HHRecipes modRecipes = new HHRecipes();
        registration.addRecipes(HHRecipeTypes.AGING, modRecipes.getCaskRecipes());
        registration.addRecipes(HHRecipeTypes.STOMPING, modRecipes.getStompingRecipes());

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        RecipeManager recipeManager = level.getRecipeManager();
        List<RecipeHolder<BottleCrateRecipe>> bottleCrateHolders = recipeManager.getAllRecipesFor(HHModRecipeTypes.BOTTLE_CRATE.get());
        List<RecipeHolder<CraftingRecipe>> craftingRecipeHolders = bottleCrateHolders.stream()
                .map(holder -> new RecipeHolder<CraftingRecipe>(holder.id(), holder.value()))
                .toList();
        registration.addRecipes(RecipeTypes.CRAFTING, craftingRecipeHolders);

        registration.addIngredientInfo(new ItemStack(HHModItems.SALT.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.salt"));
        registration.addIngredientInfo(new ItemStack(HHModItems.WATERING_CAN.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.watering_can"));
        registration.addIngredientInfo(new ItemStack(HHModItems.TREE_TAPPER.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.tree_tapper"));
        registration.addIngredientInfo(new ItemStack(HHModItems.SAP_BUCKET.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.sap_bucket"));
        registration.addIngredientInfo(new ItemStack(HHModItems.NEST.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.nest"));
        registration.addIngredientInfo(new ItemStack(HHModItems.SCARECROW.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.scarecrow"));
        registration.addIngredientInfo(new ItemStack(HHModItems.TROUGH.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.trough"));
        registration.addIngredientInfo(new ItemStack(HHModItems.SPRINKLER.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.sprinkler"));
        registration.addIngredientInfo(new ItemStack(HHModItems.SALT_BLOCK.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.salt_block"));
        registration.addIngredientInfo(new ItemStack(HHModItems.JUG.get()), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.jug"));
        registration.addIngredientInfo(new ItemStack(Items.FEATHER), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.pluck_chickens"));
        registration.addIngredientInfo(List.of(new ItemStack(HHModItems.WILD_RED_GRAPES.get()), new ItemStack(HHModItems.RED_GRAPES.get()), new ItemStack(HHModItems.WILD_GREEN_GRAPES.get()), new ItemStack(HHModItems.GREEN_GRAPES.get())), VanillaTypes.ITEM_STACK, HHTextUtils.getTranslation("jei.info.wild_grapes"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(HHModItems.CASK.get()), HHRecipeTypes.AGING);
        registration.addRecipeCatalyst(new ItemStack(HHModItems.STOMPING_BASIN.get()), HHRecipeTypes.STOMPING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CaskGUI.class, 89, 25, 24, 17, HHRecipeTypes.AGING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CaskMenu.class, HHModMenuTypes.CASK_MENU.get(), HHRecipeTypes.AGING, 0, 6, 9, 36);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
