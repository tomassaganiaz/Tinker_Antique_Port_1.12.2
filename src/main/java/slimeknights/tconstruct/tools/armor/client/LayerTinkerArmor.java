package slimeknights.tconstruct.tools.armor.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.armor.item.ArmorCore;

public class LayerTinkerArmor implements LayerRenderer<EntityLivingBase> {

  private final RenderLivingBase<?> renderer;
  private final ModelBiped modelLeggings;
  private final ModelBiped modelArmor;

  private static final ResourceLocation LAYER_1 = Util.getResource("textures/models/armor/plate_layer_1.png");
  private static final ResourceLocation LAYER_2 = Util.getResource("textures/models/armor/plate_layer_2.png");
  private static final java.util.Map<String, ResourceLocation> TEX_CACHE = new java.util.HashMap<>();
  private static final java.util.Map<String, ResourceLocation> MAILLE_CACHE = new java.util.HashMap<>();
  private static final java.util.Map<String, ResourceLocation> WINGS_CACHE = new java.util.HashMap<>();

  public LayerTinkerArmor(RenderLivingBase<?> renderer) {
    this.renderer = renderer;
    this.modelLeggings = new ModelBiped(0.5F);
    this.modelArmor = new ModelBiped(1.0F);
  }

  @Override
  public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
      if(slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) continue;
      ItemStack stack = entity.getItemStackFromSlot(slot);
      if(stack.isEmpty() || !(stack.getItem() instanceof ArmorCore)) continue;
      java.util.List<Material> mats = null;
      try { mats = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack)); } catch(Exception e) {}
      if(mats == null || mats.isEmpty()) continue;
      Material matPlating = mats.get(0);
      if(matPlating == null || matPlating == Material.UNKNOWN) continue;
      Material matMaille = mats.size() > 1 ? mats.get(1) : null;
      ResourceLocation texPlating = getArmorTextureForSet(slot, matPlating, stack);
      
      ModelBiped model = slot == EntityEquipmentSlot.LEGS ? modelLeggings : modelArmor;
      model.setModelAttributes(renderer.getMainModel());
      model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
      setModelVisible(model, slot);
      renderPlatingLayer(model, texPlating, matPlating, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      if(matMaille != null) renderMailleLayer(model, slot, matMaille, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      // alas (maille_wings) en el peto: solo si tiene la habilidad "wings" (ELYTRA), texturizadas por la maille
      if(slot == EntityEquipmentSlot.CHEST && matMaille != null && hasWings(stack)) renderWingsLayer(model, matMaille, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      
    }
  }

  private static void setModelVisible(ModelBiped model, EntityEquipmentSlot slot) {
    model.setVisible(false);
    switch(slot) {
      case HEAD:
        model.bipedHead.showModel = true;
        model.bipedHeadwear.showModel = true;
        break;
      case CHEST:
        model.bipedBody.showModel = true;
        model.bipedRightArm.showModel = true;
        model.bipedLeftArm.showModel = true;
        break;
      case LEGS:
        model.bipedBody.showModel = true;
        model.bipedRightLeg.showModel = true;
        model.bipedLeftLeg.showModel = true;
        break;
      case FEET:
        model.bipedRightLeg.showModel = true;
        model.bipedLeftLeg.showModel = true;
        break;
      default:
        break;
    }
  }

  private static ResourceLocation tryResource(ResourceLocation loc) {
    try {
      if(Minecraft.getMinecraft().getResourceManager().getResource(loc) != null) return loc;
    } catch(Exception e) {}
    return null;
  }

  private static final java.util.Map<String, String> TEXTURE_SLUGS = new java.util.HashMap<>();
  static {
    TEXTURE_SLUGS.put("pigiron", "pig_iron");
    TEXTURE_SLUGS.put("queensslime", "queens_slime");
    TEXTURE_SLUGS.put("rosegold", "rose_gold");
    TEXTURE_SLUGS.put("amethystbronze", "amethyst_bronze");
    TEXTURE_SLUGS.put("dragonscale", "dragon_scale");
    TEXTURE_SLUGS.put("jeweledhide", "jeweled_hide");
    TEXTURE_SLUGS.put("necroticbone", "necrotic_bone");
    TEXTURE_SLUGS.put("magmaslime", "magma");
  }

  private static String textureSlug(Material mat) {
    String id = mat.identifier.toLowerCase(java.util.Locale.US);
    String alias = TEXTURE_SLUGS.get(id);
    return alias != null ? alias : id;
  }

  private static java.util.List<String> platingSubs(String set, EntityEquipmentSlot slot) {
    boolean legs = slot == EntityEquipmentSlot.LEGS;
    java.util.List<String> subs = new java.util.ArrayList<>(1);
    subs.add("plating_" + (legs ? "leggings" : "armor"));
    return subs;
  }

  public static ResourceLocation getArmorTextureForSet(EntityEquipmentSlot slot, Material mat, ItemStack stack) {
    String set = "plate";
    ResourceLocation fallback = slot == EntityEquipmentSlot.LEGS ? LAYER_2 : LAYER_1;
    String m = textureSlug(mat);
    for(String sub : platingSubs(set, slot)) {
      String key = set + ":" + sub + ":" + mat.identifier;
      if(TEX_CACHE.containsKey(key)) {
        ResourceLocation cached = TEX_CACHE.get(key);
        if(cached != null) return cached;
        continue;
      }
      ResourceLocation found = null;
      try {
        found = tryResource(new ResourceLocation(Util.MODID, "textures/tinker_armor/" + set + "/" + sub + "_tconstruct_" + m + ".png"));
        if(found == null) found = tryResource(new ResourceLocation(Util.MODID, "textures/tinker_armor/" + set + "/" + sub + ".png"));
      } catch(Exception e) {}
      TEX_CACHE.put(key, found);
      if(found != null) return found;
    }
    return fallback;
  }

  private void renderPlatingLayer(ModelBiped model, ResourceLocation tex, Material mat, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    GlStateManager.pushMatrix();
    try {
      Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
      if(tex == LAYER_1 || tex == LAYER_2) {
        int color = mat.materialTextColor;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        GlStateManager.color(r, g, b, 1f);
      } else {
        GlStateManager.color(1f, 1f, 1f, 1f);
      }
      model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    } finally {
      GlStateManager.color(1f, 1f, 1f, 1f);
      GlStateManager.popMatrix();
    }
  }

  private void renderMailleLayer(ModelBiped model, EntityEquipmentSlot slot, Material mat, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    ResourceLocation texMaille = getMailleTexture(slot, mat);
    if(texMaille == null) return;
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    try {
      Minecraft.getMinecraft().getTextureManager().bindTexture(texMaille);
      GlStateManager.color(1f, 1f, 1f, 0.85f);
      model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    } catch(Exception e) {
    } finally {
      GlStateManager.color(1f, 1f, 1f, 1f);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
    }
  }

  private ResourceLocation getMailleTexture(EntityEquipmentSlot slot, Material mat) {
    String m2 = textureSlug(mat);
    String mailleSub = "maille_" + (slot == EntityEquipmentSlot.LEGS ? "leggings" : "armor");
    String key = mailleSub + ":" + m2;
    if(MAILLE_CACHE.containsKey(key)) return MAILLE_CACHE.get(key);
    ResourceLocation result = null;
    try {
      result = tryResource(new ResourceLocation(Util.MODID, "textures/tinker_armor/plate/" + mailleSub + "_tconstruct_" + m2 + ".png"));
      if(result == null) result = tryResource(new ResourceLocation(Util.MODID, "textures/tinker_armor/plate/" + mailleSub + ".png"));
    } catch(Exception e) {}
    MAILLE_CACHE.put(key, result);
    return result;
  }

  /** True si el stack (peto) tiene la habilidad "wings" (ELYTRA). */
  private static boolean hasWings(ItemStack stack) {
    try {
      return stack.hasTagCompound() && TagUtil.getToolTag(stack).getFloat(slimeknights.tconstruct.library.utils.Tags.WINGS) > 0f;
    } catch(Exception e) {
      return false;
    }
  }

  private void renderWingsLayer(ModelBiped model, Material mat, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    ResourceLocation texWings = getWingsTexture(mat);
    if(texWings == null) return;
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    try {
      Minecraft.getMinecraft().getTextureManager().bindTexture(texWings);
      GlStateManager.color(1f, 1f, 1f, 1f);
      model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    } catch(Exception e) {
    } finally {
      GlStateManager.color(1f, 1f, 1f, 1f);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
    }
  }

  private ResourceLocation getWingsTexture(Material mat) {
    String m2 = textureSlug(mat);
    String key = "maille_wings:" + m2;
    if(WINGS_CACHE.containsKey(key)) return WINGS_CACHE.get(key);
    ResourceLocation result = null;
    try {
      result = tryResource(new ResourceLocation(Util.MODID, "textures/tinker_armor/plate/maille_wings_tconstruct_" + m2 + ".png"));
      if(result == null) result = tryResource(new ResourceLocation(Util.MODID, "textures/tinker_armor/plate/maille_wings.png"));
    } catch(Exception e) {}
    WINGS_CACHE.put(key, result);
    return result;
  }

  @Override
  public boolean shouldCombineTextures() {
    return false;
  }
}
