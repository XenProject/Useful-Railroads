package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;

public class TeleportRailFuelRecipe extends FuelRecipe {
	
	public TeleportRailFuelRecipe(ResourceLocation id, String group, Ingredient ingredient, int fuel) {
		super(id, group, ingredient, fuel);
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return UsefulRailroadsRecipeSerializers.TELEPORT_FUEL;
	}
	
	@Override
	public IRecipeType<?> getType() {
		return UsefulRailroadsRecipeTypes.TELEPORT_FUEL;
	}
	
}
