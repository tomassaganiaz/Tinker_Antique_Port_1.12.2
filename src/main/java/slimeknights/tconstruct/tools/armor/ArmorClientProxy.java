package slimeknights.tconstruct.tools.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import slimeknights.tconstruct.common.CommonProxy;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.tools.armor.client.LayerTinkerArmor;

public class ArmorClientProxy extends CommonProxy {

  private final java.util.Set<net.minecraft.client.renderer.entity.RenderLivingBase<?>> layered = new java.util.HashSet<>();
  private int airJumps = 0;
  private boolean jumpWasDown = false;

  @Override
  public void registerModels() {
    registerArmorPartModels();
    registerArmorToolModels();
  }

  @Override
  public void init() {
    super.init();
    net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if(event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().world == null) return;
    for(RenderPlayer render : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
      addLayerOnce(render);
    }
    for(net.minecraft.client.renderer.entity.Render render : Minecraft.getMinecraft().getRenderManager().entityRenderMap.values()) {
      if(render instanceof net.minecraft.client.renderer.entity.RenderLivingBase) {
        addLayerOnce((net.minecraft.client.renderer.entity.RenderLivingBase<?>) render);
      }
    }
    handleDoubleJump();
  }

  // 1 extra jump per fall; client-side (SP/LAN). LivingJumpEvent only fires grounded, so it can't do mid-air jumps.
  private void handleDoubleJump() {
    EntityPlayerSP player = Minecraft.getMinecraft().player;
    if(player == null) return;
    boolean jumpDown = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
    if(player.onGround || player.isInWater() || player.isElytraFlying() || player.capabilities.isFlying) {
      airJumps = 0;
    }
    else if(jumpDown && !jumpWasDown && airJumps < 1 && ArmorEventHandler.hasDoubleJump(player)) {
      player.motionY = 0.52;
      player.fallDistance = 0;
      player.playSound(SoundEvents.ENTITY_SLIME_JUMP, 0.8f, 1.2f);
      airJumps++;
    }
    jumpWasDown = jumpDown;
  }

  private void addLayerOnce(net.minecraft.client.renderer.entity.RenderLivingBase<?> render) {
    if(layered.add(render)) render.addLayer(new LayerTinkerArmor(render));
  }

  private void registerArmorPartModels() {
    for(slimeknights.tconstruct.library.tools.IToolPart part : new slimeknights.tconstruct.library.tools.IToolPart[]{TinkerArmor.plateHelmet, TinkerArmor.plateChestplate, TinkerArmor.plateLeggings, TinkerArmor.plateBoots, TinkerArmor.maille}) {
      if(part != null) ModelRegisterUtil.registerPartModel((slimeknights.tconstruct.library.tools.ToolPart)part);
    }
  }

  private void registerArmorToolModels() {
    for(slimeknights.tconstruct.library.tools.ToolCore tool : new slimeknights.tconstruct.library.tools.ToolCore[]{TinkerArmor.helmet, TinkerArmor.chestplate, TinkerArmor.leggings, TinkerArmor.boots}) {
      if(tool != null) ModelRegisterUtil.registerToolModel(tool);
    }
  }
}
