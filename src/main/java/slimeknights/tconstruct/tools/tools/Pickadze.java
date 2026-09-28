package slimeknights.tconstruct.tools.tools;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.library.utils.TooltipBuilder;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Pico-Azada: pico + azada, crea caminos y ara. Porte de TC3 1.20.1 (pickadze, pathing). */
public class Pickadze extends AoeToolCore {

  public static final ImmutableSet<net.minecraft.block.material.Material> effective_materials_shovel =
      ImmutableSet.of(net.minecraft.block.material.Material.GRASS,
                      net.minecraft.block.material.Material.GROUND,
                      net.minecraft.block.material.Material.CLAY);

  public Pickadze() {
    super(PartMaterialType.handle(TinkerTools.toolRod),
          PartMaterialType.head(TinkerTools.pickHead),
          PartMaterialType.head(TinkerTools.adzeHead));

    addCategory(Category.HARVEST);
    this.setHarvestLevel("pickaxe", 0);
    this.setHarvestLevel("shovel", 0);
  }

  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
    if(StringUtils.isNullOrEmpty(toolClass)) {
      return -1;
    }
    if(toolClass.equals("pickaxe")) {
      return getPickLevel(stack);
    }
    else if(toolClass.equals("shovel")) {
      return getShovelLevel(stack);
    }
    return super.getHarvestLevel(stack, toolClass, player, blockState);
  }

  @Override
  public boolean isEffective(IBlockState state) {
    net.minecraft.block.material.Material m = state.getMaterial();
    return m == net.minecraft.block.material.Material.ROCK
        || m == net.minecraft.block.material.Material.IRON
        || effective_materials_shovel.contains(m);
  }

  @Override
  public float miningSpeedModifier() {
    return 0.75f;
  }

  @Override
  public float damagePotential() {
    return 0.5f;
  }

  @Override
  public double attackSpeed() {
    return 1.3d;
  }

  @Override
  public int[] getRepairParts() {
    return new int[]{1, 2};
  }

  @Nonnull
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    return doMakePath(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public List<String> getInformation(ItemStack stack, boolean detailed) {
    TooltipBuilder info = new TooltipBuilder(stack);
    info.addDurability(!detailed);
    String text = Util.translate("stat.mattock.axelevel.name");
    info.add(String.format("%s: %s", text, HarvestLevels.getHarvestLevelName(getPickLevel(stack))) + TextFormatting.RESET);
    text = Util.translate("stat.mattock.shovellevel.name");
    info.add(String.format("%s: %s", text, HarvestLevels.getHarvestLevelName(getShovelLevel(stack))) + TextFormatting.RESET);
    info.addMiningSpeed();
    info.addAttack();
    if(ToolHelper.getFreeModifiers(stack) > 0) {
      info.addFreeModifiers();
    }
    if(detailed) {
      info.addModifierInfo();
    }
    return info.getTooltip();
  }

  @Override
  public ToolNBT buildTagData(List<Material> materials) {
    HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
    HeadMaterialStats pick = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
    HeadMaterialStats adze = materials.get(2).getStatsOrUnknown(MaterialTypes.HEAD);

    MattockToolNBT data = new MattockToolNBT();
    data.head(pick, adze);
    data.handle(handle);
    data.axeLevel = pick.harvestLevel;
    data.shovelLevel = adze.harvestLevel;
    data.attack += 0.5f;
    data.durability *= 1.3f;
    return data;
  }

  protected int getPickLevel(ItemStack stack) {
    return new MattockToolNBT(TagUtil.getToolTag(stack)).axeLevel;
  }

  protected int getShovelLevel(ItemStack stack) {
    return new MattockToolNBT(TagUtil.getToolTag(stack)).shovelLevel;
  }

  public static class MattockToolNBT extends ToolNBT {

    private static final String TAG_AxeLevel = Tags.HARVESTLEVEL + "Axe";
    private static final String TAG_ShovelLevel = Tags.HARVESTLEVEL + "Shovel";

    public int axeLevel;
    public int shovelLevel;

    public MattockToolNBT() {
    }

    public MattockToolNBT(NBTTagCompound tag) {
      super(tag);
    }

    @Override
    public void read(NBTTagCompound tag) {
      super.read(tag);
      axeLevel = tag.getInteger(TAG_AxeLevel);
      shovelLevel = tag.getInteger(TAG_ShovelLevel);
    }

    @Override
    public void write(NBTTagCompound tag) {
      super.write(tag);
      tag.setInteger(TAG_AxeLevel, axeLevel);
      tag.setInteger(TAG_ShovelLevel, shovelLevel);
    }
  }
}