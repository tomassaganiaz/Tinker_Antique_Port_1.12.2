# Plan de Optimización y Refactorización — TinkersAntique 1.12.2

> **Principio**: No borrar contenido implementado (armor 100%, foundry 100%, metales 100%, etc.), solo **modificar/optimizar** para mejorar rendimiento, mantenibilidad y parity TC3.

**Fecha**: 2026-09-03 · **Build**: SUCCESS 18 tasks · **Regla**: `importante1.20.1` read-only

---

## 0. Objetivos

| Objetivo | Métrica | Estado actual | Meta |
|----------|---------|---------------|------|
| **Rendimiento** (tick, memoria, GC) | `TileSmeltery`/`TileMelter` 20 ticks/s + `LayerTinkerArmor` per-frame | 3-5ms/tick, 4GB | <2ms/tick, <3GB |
| **Mantenibilidad** (duplicados, LOC) | `TinkerArmor` 328 líneas, 50× `addMatStats` duplicado | 15% duplicado | <5% duplicado |
| **Paridad visual** (sangrado PNG, modelos) | 18 PNGs 16x16 borde 1px, `tmat/tcon` 5+13 | 95% | 100% sin sangrado |
| **Estabilidad** (crash `slime_boots`, `scorched_channel`) | `slime_boots` duplicado, `scorched_channel` `center_lever` | Fix `slime_armor_boots` + `center_lever` | 0 crash |

---

## 1. Fase 1 — Núcleo y NBT (1 semana)

### 1.1 Unificar `ToolNBT`/`ArmorNBT` + `Tags`
- **Problema**: `ToolNBT` (163 líneas) y `ArmorNBT` (60 líneas) duplican `durability`, `modifiers`, `write/read`, `FREE_UPGRADES/ABILITIES`.
- **Refactor**: Crear `AbstractNBT` con `durability`, `modifiers`, `upgrades`, `abilities`, `write/read` común; `ToolNBT` y `ArmorNBT` extienden y añaden `head/handle/extra` vs `defense/toughness/knockback`.
- **Beneficio**: -80 líneas, -15% duplicado, single source `Tags.FREE_*`.
- **Riesgo**: Bajo (solo NBT, no gameplay).

### 1.2 Cache `Material` y `Fluid`
- **Problema**: `TinkerUtil.getMaterialFromStack` y `TinkerRegistry.getMaterial` hacen `HashMap` lookup por cada `Slot.isItemValid` (20 ticks/s * 6 slots = 120 lookups/s).
- **Optimización**: Cache `ThreadLocal<Map<ItemStack, Material>>` en `ContainerNewToolStation` + `ArmorCore.isItemValid` con `WeakHashMap`.
- **Beneficio**: -1ms/tick en `TileNewToolStation` 4 slots directos.

### 1.3 `FoundryTierHelper` estático
- **Problema**: `FoundryTierHelper.isTier4Fluid` hace 7 `==` por cada `alloyAlloys` (10 ticks/s * 19 alloys = 190 checks/s).
- **Optimización**: `EnumSet<Fluid>` cache al `preInit`, `isTier4Fluid` → `tier4Fluids.contains(fluid)` O(1) `HashSet`.
- **Beneficio**: Micro, pero patrón para futuros tiers.

---

## 2. Fase 2 — Modelos y Texturas (1 semana)

### 2.1 `LayerTinkerArmor` → `ArmorModelDispatcher` (parity TC3)
- **Problema**: `LayerTinkerArmor` actual hace `ResourceManager.getResource` por cada `slot` por cada `frame` (4 slots * 60fps = 240 I/O/frame), y `GlStateManager.color` sin `pushMatrix`.
- **Refactor**: Cache `Map<String, ResourceLocation>` per-material en `ArmorModelDispatcher` (como `1.20.1/library/client/armor/ArmorModelManager.java`), `preInit` precarga `tinker_armor/plate/*` 381 variantes.
- **Optimización**: `getArmorTextureForSet` con `cache.computeIfAbsent`, `GlStateManager.pushMatrix/popMatrix`.
- **Beneficio**: -2ms/frame, sin I/O per-frame.

### 2.2 `ArmorClientProxy` + `TinkerArmor` registro duplicado
- **Problema**: `TinkerArmor` 13 `registerTool` + 5 `registerPartModel` + 13 `registerToolModel` duplicados (26 líneas).
- **Refactor**: Ya parcialmente hecho (`registerArmorMaterialStats` loop `Object[][] data`), extender a `registerTools` loop `Object[][] tools` (ya hecho para `addMatStats`, falta para `registerTools` 13 → ya refactorizado a loop, pero `ArmorClientProxy` aún 5+13 `if` separados).
- **Refactor**: `ArmorClientProxy.registerModels()` → `Stream.of(plateHelmet, ...).forEach(ModelRegisterUtil::registerPartModel)` y `Stream.of(helmet, ...).forEach(ModelRegisterUtil::registerToolModel)`.
- **Beneficio**: -20 líneas, DRY.

### 2.3 PNG re-muestreo
- **Problema**: 13 PNGs 16x16 con borde 1px interno 14x14 (re-save `System.Drawing` sin `Graphics.CompositingQuality.HighQuality`), `scorched` recolor `media/σ` 80% aproximado.
- **Optimización**: Re-generar `scorched_*` 1:1 `importante/foundry/scorched` con `ColorTransfer` `HighQualityBicubic` + `CompositingQuality.HighQuality`, y `stone_ladder`/`efln` etc. con `SmoothingMode.AntiAlias`.
- **Beneficio**: Sin sangrado `mipmap` + `anisotropic`, parity 1.20.1.

---

## 3. Fase 3 — Modificadores y Traits (1 semana)

### 3.1 Unificar `ModProtection` familia (6×)
- **Problema**: `ModProtection`, `ModProjectileProtection`, `ModBlastProtection`, `ModFireProtection`, `ModMagicProtection`, `ModFeatherFalling` duplican `applyEffect` (6× 10 líneas).
- **Refactor**: Ya hecho `ModArmorProtectionBase` (`tag`+`amount`+`FreeUpgrade/AbilityAspect`) — 6 → 1 base + 6 one-liners.
- **Next**: Aplicar mismo patrón a `ModTwin`/`ModSpilling`/etc. (11 mods) → `ModArmorAbilityBase` con `LevelAspect` + `FreeAbilityAspect`.

### 3.2 `ModHaste` armor `MOVEMENT_SPEED` acoplado
- **Problema**: `ModHaste` modificado para `ArmorCore` con `if(!harvest && !weapon && !launcher && TOOL)` → `MOVEMENT_SPEED` 1.5%/redstone, pero `ToolNBT` y `ArmorNBT` duplican `MOVEMENT_SPEED` handling.
- **Refactor**: Extraer `ArmorHasteHandler` con `getMovementSpeedBonus(level)` y `ArmorCore.getAttributeModifiers` usa `handler.getBonus(tag)`.
- **Beneficio**: Single source `0.015f` constante.

### 3.3 `DoubleJump` event duplicado
- **Problema**: `ModDoubleJump` registra `MinecraftForge.EVENT_BUS` en constructor (si `registered` static), pero `TinkerArmor` también registra `onKnockback`/`onHurt` en `registerEventHandlers` (2 handlers).
- **Optimización**: Consolidar todos los armor events (`onKnockback`, `onHurt`, `onJump`, `onFall`) en `ArmorEventHandler` singleton, `TinkerArmor` solo registra ese.

---

## 4. Fase 4 — Foundry y Smeltery (3 días)

### 4.1 `alloyAlloys` duplicado
- **Problema**: `TileSmeltery.alloyAlloys` y `TileScorchedFoundry.alloyAlloys` duplican `ALLOYING_PER_TICK` loop (10) y `FoundryTierHelper` check.
- **Refactor**: Ya hecho `FoundryTierHelper.alloyAlloysForTier(liquids, isFoundry)` único — 20 líneas → 1 helper.
- **Next**: Extraer `heatItems` duplicado? `TileMelter` y `TileSmeltery` ambos tienen `heatItems` con `temperature` check, pero `TileMelter` usa `MAX_TEMPERATURE 550` y `TileSmeltery` usa `fuelQuality`.

### 4.2 `BlockScorched` 8 tipos + `BlockScorchedChannel` 6 modelos `scorched_channel` duplicados
- **Problema**: `BlockScorchedChannel` copia 6 JSON de `channel` con `seared_stone` → `scorched_stone` replace via `Copy-Item` + `-replace`.
- **Optimización**: Hacer `BlockScorchedChannel` que extienda `BlockChannel` y override `getTextureName` para usar `scorched_stone` sin copiar 6 JSON, o hacer `ModelLoader` con `scorched` variant.

---

## 5. Fase 5 — Estructural (NewToolStation) (1 semana)

### 5.1 `ContainerNewToolStation` 4 slots directos
- **Problema**: `ContainerNewToolStation` reemplaza 4 slots `inventorySlots.set(i, new Slot(...))` con `slotNumber = i` manual, y `buildToolDirect` duplica `TinkerRegistry.getToolStationCrafting` loop.
- **Refactor**: Hacer `TileNewToolStation` con `getSizeInventory() = 6` (4 materiales + 2 modifiers) y `ContainerNewToolStation` con `SlotNewToolStationMaterial` (extends `Slot` con `isItemValid` `TinkerUtil.getMaterialFromStack` cache) + `SlotNewToolStationUpgrade`/`Ability` con `FreeUpgrade/AbilityAspect` check.
- **Optimización**: `buildToolDirect` con `Optional<Material>` y `ToolBuilder.tryBuildTool` sin `try/catch`.

### 5.2 `PartBuilder` oculto
- **Problema**: `JEIPlugin` blacklist `ToolPart` + `Pattern` + `PartBuilder` cuando `NewToolStation` presente, pero `TinkerTools` aún registra `Pattern` y `PartBuilder` como `Item` y `Block`, y `TinkerRegistry.getToolParts()` aún contiene 30+ parts.
- **Refactor**: Añadir `Config.hideOldToolParts` (default `false` para compat, `true` para parity) que en `TinkerTools.registerToolParts` skip `registerToolPart` si `hideOld` true, y en `TinkerTools.registerBlocks` skip `TilePartBuilder` TE.

---

## 6. Fase 6 — LANG y Libro (2 días)

### 6.1 LANG duplicado `tcontruct` typo + `slime_skin` `_`
- **Problema**: `en_us.lang` tiene `item.tconstruct.materials.slime_skin.name` + alias `slimeskin` + `tcontruct` typo (3 líneas * 8 materiales = 24 líneas duplicadas).
- **Refactor**: Mantener alias para compat (ya hecho), pero generar `en_us.lang` via `LangGenerator` (como `MaterialModelLoader` genera `tmat`), no manual `Add-Content`.

### 6.2 Libro `Materials and You`
- **Problema**: `book/en_us/tools/plate_helmet.json` 5 + `seared_devices/foundry.json` 2 + `sections/tools.json` 4 + `sections/seared_devices.json` 2 = 13 archivos JSON con `text` duplicado `Requires a Tool Forge` etc.
- **Refactor**: Crear `BookGenerator` que genere `book/en_us/tools/plate_*.json` desde `TinkerArmor` `getLocalizedDesc` + `ArmorMaterialStats`, similar a `MaterialModelLoader`.

---

## 7. Fase 7 — Build y CI (2 días)

### 7.1 `gradle.properties` + `build.gradle` RAM
- **Problema**: `org.gradle.jvmargs=-Xmx4G` y `tasks.withType(JavaExec) jvmArgs ["-Xmx4G"]` duplican `-Xmx4G` para `runClient`/`runServer` (gradle daemon + game).
- **Optimización**: Centralizar en `gradle.properties` `org.gradle.jvmargs=-Xmx4G` y `build.gradle` `minecraft { jvmArgs = ["-Xmx4G"] }` + `tasks.withType(JavaExec).configureEach { jvmArgs "-Xmx4G" }` (ya hecho, pero duplicado).
- **Refactor**: Extraer a `gradle/runArgs.gradle` con `ext.runJvmArgs = ["-Xmx4G", "-Xms1G", "-XX:+UseG1GC"]` y aplicar a `runClient`/`runServer`.

### 7.2 `JsonMaterialLoader` N/A → Listo
- **Problema**: `JsonMaterialLoader` carga `assets/materials/*.json` + `data/.../stats/*.json` con `Gson` pero no cachea, y `TinkerMaterials.setupMaterialStats` llama `load()` sin `try` específico.
- **Optimización**: Cache `Map<String, JsonObject>` en `JsonMaterialLoader` y `load()` con `try-with-resources` + `log.debug` para cada `material` no encontrado.

---

## 8. Cronograma

| Fase | Días | % duplicado eliminado | Riesgo |
|------|------|-----------------------|--------|
| 1 NBT/Cache | 5 | 15% → 5% | Bajo |
| 2 Modelos | 5 | 25% → 10% | Bajo |
| 3 Modifiers | 5 | 30% → 10% | Medio |
| 4 Foundry | 3 | 20% → 5% | Bajo |
| 5 NewToolStation | 5 | 40% → 15% | Medio (breaking si se oculta `PartBuilder`) |
| 6 LANG/Libro | 2 | 15% → 5% | Bajo |
| 7 Build/CI | 2 | - | Bajo |
| **Total** | **27 días** | **30% → <5%** | **Build SUCCESS cada fase** |

> **Regla**: Cada fase `gradlew build` SUCCESS antes de siguiente, `importante1.20.1` read-only, `run/mods` 6 jars para test 4GB.

---

## 9. Verificación por fase

```powershell
gradlew build # SUCCESS 18 tasks cada fase
gradlew runClient # FML 10 mods + 5 (FutureMC etc.) + `NewToolStation` 4GB, `LayerTinkerArmor` 60fps, `TileSmeltery` <2ms/tick
```

- `TinkerArmor` 13 piezas `ARMOR 13` + `maille` + `LayerTinkerArmor` per-material 381 sin sangrado (borde 1px)
- `Foundry` `manyullyn` Tier4 solo `blazingBlood`, `scorched_channel` 6 modelos + `scorched_glass` 3 tipos
- `NewToolStation` 3 `ingotIron` → `pickaxe` directo, `FREE_UPGRADES/ABILITIES` separados, `PartBuilder` oculto JEI

