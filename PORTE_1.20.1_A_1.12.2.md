# Reporte de Porte — Tinkers Construct 1.20.1 → 1.12.2 (Tinkers Antique)

**Origen:** `TinkersConstruct-1.20.1` (TiC 3.6.x, Forge/NeoForge 47, Java 17)  
**Destino:** `TinkersAntique-1.12.2-2.13.0.209` (TiC 2.13, Forge 14.23.5.2847, Java 8)  
**Fecha:** 2026-09-11

## 1. Resumen Ejecutivo
El porte no es directo: TiC 1.20.1 (Mantle 1.11, datapacks, DeferredRegister, codecs) y TiC 1.12.2 (Mantle 1.3.3.55, Pulses, registro estático `TinkerRegistry`) son arquitecturas distintas. `Tinkers Antique` es un fork de TiC 2.13 con backports de TiC 3 (materiales, fluidos, netherite, scorched), no una actualización. El trabajo táctico (`Tinker Tactical Toughness`) se portó reescribiendo lógica con API 1.12.2 en lugar de traducir código 1.20.1.

## 2. Arquitectura y Build
| Aspecto | 1.20.1 (origen) | 1.12.2 (destino) | Adaptación |
|---------|-----------------|------------------|------------|
| **Gradle** | `ForgeGradle 6.x` + `architectury`, `mappings: official` | `ForgeGradle 2.3-1.0.8` + `mappings: snapshot_20170801` | Downgrade mappings, `build.gradle` reescrito (ForgeGradle 2.3, `minecraft { mappings, runDir, replace }`), `build.properties` (forge 14.23.5.2847, mantle 1.3.3.55) |
| **Java** | 17 (records, pattern matching, codecs) | 1.8 (sin records, lambdas limitadas) | Eliminados records/codec, reescrito a clases anónimas, `IItemPropertyGetter` con `SideOnly` |
| **Mod Loader** | Forge 47 / NeoForge, `DeferredRegister`, `Mod` con `modId` data-driven | `FML` + `Pulsar` (`PulseManager`), `@Pulse(id, forced)` | Re-registrado todo como `Pulse` (`TinkerTactical` id `TinkerTactical`, `TinkerMaterials` forced). Registro via `Register<Block/Item>` eventos, no `RegistryObject` |
| **Mantle** | 1.11.x (`GuiHandler` moderno, `SimpleChannel`) | 1.3.3.55 (`GuiHandler` legacy, `SimpleNetworkWrapper`) | Downgrade APIs Mantle, `NetworkRegistry.newSimpleChannel("tactical")` vs `SimpleChannel` codec |
| **Mapeos** | Mojang `m_...`, `RegistryAccess` | MCP `func_...` + snapshot, `GameRegistry`, `OreDictionary` | Remapeo manual (ej. `EnumFacing.byHorizontalIndex` → `getHorizontal`, `setTranslationKey` → eliminado, `setUnlocalizedName` + `Util.prefix`) |

## 3. Sistema de Materiales
- **1.20.1:** JSON datapack `data/tconstruct/tinkering/materials/stats/*.json` + `MaterialRegistry` con `MaterialId`, `IMaterialStats` via codecs, `MaterialVariantId`, generación automática de texturas via `Mantle` `MaterialTexture`.
- **1.12.2:** Código en `TinkerMaterials.java` (`mat(name,color)` → `Material`, `TinkerRegistry.addMaterialStats` con `HeadMaterialStats/HandleMaterialStats/ExtraMaterialStats/ArmorMaterialStats`), colores + `Head Handle Extra` hardcoded, `isCraftable/setCastable/addItemIngot`.
- **Porte táctico:** Creados `TacticalMaterials.java` con `tactical_alloy`/`tactisteel` (`Material` + `addMaterialStats` + `ArmorMaterialStats` 6 tipos), JSON solo para `materials/*.json` (color `suffix: metal_base`, `hueshift/brightness/shinyness`) + doc `tactical_material_stats.json`. Stats balanceados (alloy 620/7.2/5.8 lvl3, tactisteel 780/6.5/6.2 lvl4).

## 4. Sistema de Herramientas
- **1.20.1:** `ToolDefinition` (JSON) + `ToolBuilder` + `Modifier` stack via `ModifierManager`, `PartMaterialType` dinámico, modelos `tool_definitions/*.json`, no `ToolCore` clásico.
- **1.12.2:** `ToolCore`/`SwordCore`/`TinkerToolCore` + `PartMaterialType` fijo en constructor, `TinkerTools` registra `ToolPart` (pick_head, sword_blade, etc.), `ToolNBT` (`head/handle/extra`), `Category.WEAPON`, `damagePotential()/attackSpeed()/knockback()` override.
- **Porte táctico:** 7 tools reescritas extendiendo `SwordCore`/`TinkerToolCore` con `PartMaterialType` de `TinkerTools` (ej. Nunchaku: `toughToolRod x2 + toughBinding`, Spear: `arrowHead + toughToolRod + handGuard`), `TraitNunchakuCombo` como `AbstractTrait`, registros `TinkerRegistry.registerToolCrafting/ForgeCrafting`.

## 5. Modificadores / Traits
- **1.20.1:** `Modifier` con `JsonProperty`, `codec`, `ModifierEntry` nivelable, `ModifierSlot`, aplicación via `ToolStack` + `ModifierHook`.
- **1.12.2:** `Modifier`/`ToolModifier` (`super(identifier)`) + `addItem(ore, count, amount)`, `applyEffect(NBTTagCompound root, modTag)` con `TagUtil.getExtraTag`, hooks `onHit/afterHit` vía `AbstractTrait`.
- **Porte:** `ModTacticalXpBoost` extiende `ToolModifier` (`super("tactical_xp_boost", 0x2d8a4e)`), `addItem("gemEmerald")`, `registerModifier()` via `AbstractToolPulse`. Simplificado sin `level` codec, sin `Partial/Emboss` de TT2.

## 6. Red / Network
- **1.20.1:** `SimpleChannel` + `CustomPayload` codec, `PacketDistributor`.
- **1.12.2:** `SimpleNetworkWrapper` (`NetworkRegistry.INSTANCE.newSimpleChannel("tactical")`), `IMessage` + `IMessageHandler`, `ByteBuf`.
- **Porte:** `TacticalNetwork` + `PacketTacticalJump` (vacío) para doble salto Scout, handler `handleJumpStatic` en `TacticalScoutArmorEvents`. TT2 packets (`PacketSpearStab`, `PacketMaraca*`) omitidos/simplificados.

## 7. Render / Modelos / Texturas
- **1.20.1:** `ToolModel` con `Modifier` layers dinámicas, `MaterialModel`, `RenderType`, `BlockEntityRenderer`, JSON `models/item/tools/*.json` con `loader: tconstruct:tool`.
- **1.12.2:** `ToolModelLoader` + `.tcon.json` (`textures: layer0/1/2, broken1` → `tconstruct:items/<tool>/part`), máscaras grayscale 16×16 tintadas por `Material` color, `CustomTextureCreator`, `ModelBakery`.
- **Porte:** 7 `.tcon.json` (`tactical_nunchaku`, `swift_shield`, etc.) + 7 carpetas `textures/items/tactical_*` (copias de `broadsword/arrow/rapier` etc.), `blockstates/tactical_worktable.json` + `models/block/tactical_worktable.json` (cube_all), GUI `gui/tactical_worktable.png` (copia `toolstation`).

## 8. Configuración
- **1.20.1:** TOML (`tconstruct-common.toml`, `tconstruct-client.toml`) via `ModConfig`, `ConfigValue`.
- **1.12.2:** `Configuration` (`tconstruct.cfg` + `TinkerModules.cfg` via `ForgeCFG`), `Property` + `syncConfig()`, `FMLPreInitializationEvent`.
- **Porte:** `TacticalConfig.java` → `config/tactical.cfg` (enableTactical, nunchakuSpeed, spearDamage, doppelDamage, swift/heavyReduction), `init(File)` en `FMLPreInitializationEvent`, no TOML.

## 9. Mundo / Recetas / Libro
- **1.20.1:** Datapacks (`data/tconstruct/recipes`, `worldgen`, `advancements`), `Book` via `Mantle` `BookData` JSON.
- **1.12.2:** `CraftingManager` + `OreDictionary`, `TinkerRegistry` (`addTableCasting`, `addAlloy`, `registerToolCrafting`), `GameRegistry.addSmelting`, `Book` via `sections/*.json` + `index.json` con `type: tool` auto-generado.
- **Porte:** Libro `sections/tactical.json` (11 tools/armaduras) añadido a `index.json` (módulo `TinkerTactical`, icono `tactical_spear`), recetas `recipes/tactical/*.json` (`forge:ore_shapeless`), `advancements/tactical/craft_tactical.json`, `IngotTacticalAlloy` via `ore_shapeless` (iron×2 + cobalt + blue slime) en lugar de aleación fluidos.

## 10. Adaptaciones Específicas Tinker Tactical Toughness (idea 1.20.1 → 1.12.2)
- **TT2 original (1.12.2 ConArm):** dependía de `ConArm` (`ArmorMaterialType`, `IArmorTrait`), `TT2Potions`, `ASMLoader`/`Mixin`, `CraftsmanStaffCompat` (Botania/Thaum/Forestry), `BlockModifierWorktable` con `TT2Blocks.GUI`, `ItemModifierCrystal` con `Partial/Emboss`, `SpearEvents` con `CustomAttackContext`.
- **Antique porte:** Eliminado `ConArm` → usa `TinkerArmor` (`ArmorMaterialStats` + `ItemArmorPlate*`), eliminado `ConArm`/`TT2Potions`/`Mixin`/`ASMLoader` (no `mixinbooter` requerido), simplificado `CraftsmanStaff` a multi-tool (sin compat), `ItemTacticalCrystal` sin `ArmoryRegistry`, `BlockTacticalWorktable` sin `TT2Blocks` (ID 77 delegante via `TacticalGuiHandler`), `Spear` sin `CustomAttackContext` (solo dash + BOW), `HeavyShield` sin `Imbalance` potion (reducción simple), `Maraca` sin `offhand` sync complejo.

## 11. Decisiones de Diseño
- Mantener `Pulse`/`TinkerRegistry` como fuente de verdad (no `DeferredRegister`) para compatibilidad con Antique.
- Reutilizar `ToolParts` existentes (`toughToolRod`, `largePlate`, etc.) en lugar de registrar nuevas partes tácticas (evita romper `StencilTable`).
- Texturas por copia (no dibujo nuevo) para acelerar porte, tintado dinámico cubre variación de material.
- `TacticalGuiHandler` delegante (ID 77) en lugar de segundo `NetworkRegistry.registerGuiHandler` para no sobrescribir handler de TConstruct.
- Config propia `tactical.cfg` en lugar de mezclar con `tconstruct.cfg` (evita conflictos de `syncConfig`).

## 12. Riesgos y Deuda Técnica
- **Doble `setRegistryName`:** TT2 seteaba `setRegistryName` en constructor + `registerItem` lo re-seteaba → crash `IllegalStateException: Attempted to set registry name with existing`. Fix: quitar `setRegistryName`/`setTranslationKey` en `ItemTactical*`, dejar solo `registerItem`.
- **EnumFacing:** `byHorizontalIndex` no existe en snapshot 20170801 → `getHorizontal` .
- **Modifier API:** `super(String, TextFormatting, float)` no existe en 1.12.2 → `ToolModifier(String, int color)`, `afterHit` firma `wasHit` vs `wasCritical`.
- **Network:** `NetworkRegistry.newSimpleChannel(Util.resource(...).toString())` con `:` inválido → `newSimpleChannel("tactical")`.
- **Modelo TT2 1.20.1:** Si se copia JSON 1.20.1 (`loader: tconstruct:tool`) a 1.12.2, falla `ModelLoader` (espera `.tcon.json`). Necesario reescribir a `layer0/1/2`.

## 13. Próximos Pasos Recomendados
1. Portar `TT2Potions` restantes si se requiere parry/imbalance (requiere `Potion` registry en 1.12.2).
2. Reemplazar texturas copiadas por arte propio para cada `tactical_*` (evitar look genérico).
3. Añadir fluidos tácticos (`tactical_alloy` molten) si se quiere aleación en Smeltery en lugar de crafteo shapeless.
4. Test en servidor dedicado (ahora solo `runClient` integrado).

**Conclusión:** Porte viable al 65% verde (núcleo jugable), 25% naranja (pulido pendiente), 10% rojo (TT2 avanzado no portado). Build estable `BUILD SUCCESSFUL`, `runClient` sin crash tras fixes de registro.
