package slimeknights.tconstruct.foundry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.foundry.tileentity.TileScorchedFoundry;
import slimeknights.tconstruct.smeltery.block.BlockTank;
import slimeknights.tconstruct.smeltery.client.SmelteryRenderer;

import static slimeknights.tconstruct.common.ModelRegisterUtil.registerItemBlockMeta;
import static slimeknights.tconstruct.common.ModelRegisterUtil.registerItemModel;

public class FoundryClientProxy extends ClientProxy {

  @Override
  public void preInit() {
    super.preInit();

    MinecraftForge.EVENT_BUS.register(new FoundryClientEvents());
  }

  @Override
  public void registerModels() {
    // Blocks
    registerItemBlockMeta(TinkerScorched.blockScorched);
    registerItemModel(TinkerScorched.foundryController);
    registerItemBlockMeta(TinkerScorched.scorchedIO);
    registerItemModel(TinkerScorched.melter);
    registerItemModel(TinkerScorched.scorchedChannel);

    registerItemModel(TinkerScorched.scorchedForge);
    registerItemBlockMeta(TinkerScorched.foundryCasting);
    registerItemModel(TinkerScorched.heater);
    registerItemModel(TinkerScorched.alloyer);
    registerItemModel(TinkerScorched.scorchedFaucet);
    registerItemModel(TinkerScorched.scorchedLantern);
    registerItemModel(TinkerScorched.scorchedBrick);

    // scorched tank items (same types as the seared tank)
    Item tank = Item.getItemFromBlock(TinkerScorched.scorchedTank);
    for(BlockTank.TankType type : BlockTank.TankType.values()) {
      ModelLoader.setCustomModelResourceLocation(tank, type.meta, new ModelResourceLocation(tank.getRegistryName(), type.getName()));
    }

    // render the molten metals inside the foundry like the smeltery
    ClientRegistry.bindTileEntitySpecialRenderer(TileScorchedFoundry.class, new SmelteryRenderer());
  }
}