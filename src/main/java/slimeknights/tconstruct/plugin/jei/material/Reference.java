package slimeknights.tconstruct.plugin.jei.material;

import slimeknights.tconstruct.library.materials.MaterialTypes;

import java.util.ArrayList;
import java.util.List;

public final class Reference {
    public static final List<String> HARVEST_TYPES = new ArrayList<>();
    public static final List<String> RANGED_TYPES = new ArrayList<>();
    public static final List<String> PROJECTILE_TYPES = new ArrayList<>();
    public static final List<String> ARMOR_TYPES = new ArrayList<>();

    static {
        HARVEST_TYPES.add(MaterialTypes.HEAD);
        HARVEST_TYPES.add(MaterialTypes.EXTRA);
        HARVEST_TYPES.add(MaterialTypes.HANDLE);

        RANGED_TYPES.add(MaterialTypes.BOW);
        RANGED_TYPES.add(MaterialTypes.BOWSTRING);

        PROJECTILE_TYPES.add(MaterialTypes.PROJECTILE);
        PROJECTILE_TYPES.add(MaterialTypes.SHAFT);
        PROJECTILE_TYPES.add(MaterialTypes.FLETCHING);

        ARMOR_TYPES.add(slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_HELMET);
        ARMOR_TYPES.add(slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_CHESTPLATE);
        ARMOR_TYPES.add(slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_LEGGINGS);
        ARMOR_TYPES.add(slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_BOOTS);
        ARMOR_TYPES.add(slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_MAILLE);
    }
}
