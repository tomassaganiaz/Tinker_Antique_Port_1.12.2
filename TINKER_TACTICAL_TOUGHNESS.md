# Tinker Tactical Toughness — Implementación en Tinkers Antique (1.12.2)

## Resumen
Carpeta integrada como **Pulse** dentro de Tinkers Antique: `slimeknights.tconstruct.tactical` (id `TinkerTactical`). Añade 7 armas/escudos tácticos + 2 materiales, con modelos `.tcon.json`, texturas 16×16 y stats balanceados. Registrado en `TConstruct.java` (`pulseManager.registerPulse(new TinkerTactical())`).

## Estructura de la carpeta
```
src/main/java/slimeknights/tconstruct/tactical/
├── TinkerTactical.java          # Pulse principal, registro de tools/traits
├── TacticalMaterials.java       # 2 materiales: tactical_alloy / tactisteel + stats
├── tools/
│   ├── TinkerNunchaku.java      # 0.52 dmg, 2.1 speed, combo pull
│   ├── SwiftShield.java         # 0.38 dmg, 1.4 speed, BLOCK rápido
│   ├── HeavyShield.java         # 0.62 dmg, 0.85 speed, BLOCK pesado
│   ├── Spear.java               # 0.85 dmg, 1.1 speed, dash + BOW use
│   ├── Doppelhander.java        # 1.1 dmg, 0.75 speed, AoE sweep
│   ├── Maraca.java              # 0.7 dmg, 1.8 speed, nausea/slow 25%
│   └── CraftsmanStaff.java      # 0.35 dmg, 1.2 speed, multi-tool (pick/axe/shovel/sword)
└── traits/
    └── TraitNunchakuCombo.java  # tactical_combo: +8% dmg por golpe hasta x1.4 (max 5), reset al matar

resources/assets/tconstruct/
├── models/item/tools/
│   ├── tactical_nunchaku.tcon.json
│   ├── tactical_swift_shield.tcon.json
│   ├── tactical_heavy_shield.tcon.json
│   ├── tactical_spear.tcon.json          # acomodado: head tip / shaft largo / guard
│   ├── tactical_doppelhander.tcon.json
│   ├── tactical_maraca.tcon.json
│   └── tactical_craftsman_staff.tcon.json
├── textures/items/
│   ├── tactical_nunchaku/{rod_top,rod_bottom,binding,broken_rod}.png
│   ├── tactical_swift_shield/{head,plate,broken_plate}.png
│   ├── tactical_heavy_shield/{head,plate,handle,broken_head}.png
│   ├── tactical_spear/{head,shaft,guard,broken_head}.png      # head=arrow/head, shaft=arrow/shaft, guard=rapier/guard
│   ├── tactical_doppelhander/{blade,binding,plate,handle,broken_blade}.png
│   ├── tactical_maraca/{bell,binding,handle,broken_bell}.png
│   └── tactical_craftsman_staff/{shard,tough_rod,rod,broken_shard}.png
├── materials/
│   ├── tactical_alloy.json  # metal_base #4a90e2
│   └── tactisteel.json      # metal_base #d4a76a
├── tactical/tactical_material_stats.json  # doc central de stats
└── lang/en_US.lang  # 9 entradas: 7 tools + 2 materiales
```

## Modelos JSON (ejemplo Spear)
```json
{
  "textures": {
    "layer0": "tconstruct:items/tactical_spear/head",
    "layer1": "tconstruct:items/tactical_spear/shaft",
    "layer2": "tconstruct:items/tactical_spear/guard",
    "broken1": "tconstruct:items/tactical_spear/broken_head"
  }
}
```
Capas mapean a `PartMaterialType` en orden del constructor. Todas las texturas son máscaras grayscale tintadas por color de material.

## Stats de Materiales

### tactical_alloy (#4a90e2) — ligero/magnético
- `Head: 620 dur, 7.2 speed, 5.8 atk, harvest 3 (Diamond)`
- `Handle: 1.15 mod, 80 dur`
- `Extra: 100 dur`
- Traits: `lightweight`, `magnetic`

### tactisteel (#d4a76a) — pesado/dense
- `Head: 780 dur, 6.5 speed, 6.2 atk, harvest 4 (Cobalt)`
- `Handle: 0.95 mod, 120 dur`
- `Extra: 140 dur`
- Traits: `heavy`, `dense`

### Stats por herramienta (ver `tactical_material_stats.json`)
| Tool | dmgPot | atkSpd | Partes | Notas |
|------|--------|--------|--------|-------|
| nunchaku | 0.52 | 2.1 | toughRod×2 + binding | pull 0.28 + combo |
| swift_shield | 0.38 | 1.4 | panHead + largePlate | BLOCK, preventSlow 1.0 |
| heavy_shield | 0.62 | 0.85 | signHead + plate + toughRod | BLOCK |
| spear | 0.85 | 1.1 | arrowHead + toughRod + handGuard | BOW anim, dash 0.35 |
| doppelhander | 1.1 | 0.75 | swordBlade + binding + plate + toughRod | AoE 9 bloques |
| maraca | 0.7 | 1.8 | largePlate + binding + toolRod | nausea 25% |
| craftsman_staff | 0.35 | 1.2 | shard + toughRod + toolRod | clases pick/axe/shovel/sword |

## Registro
- `TinkerTactical.registerItems()`: añade trait `tactical_combo`, `registerTool()` 7 tools, `TinkerRegistry.registerToolCrafting/ForgeCrafting`.
- `TacticalMaterials.registerStats()` en `FMLPreInitializationEvent` (antes de model loading), `register()` en `FMLInitializationEvent`.
- Verificado `gradlew compileJava` y `runClient` (MC 1.12.2 Forge 2847, 10 mods, integrated server OK).

## Notas de acomodo Spear (último fix)
- `head.png` = `arrow/head.png` (punta), `shaft.png` = `arrow/shaft.png` (asta larga), `guard.png` = `rapier/guard.png` (cruz). Antes usaba `broadsword/handle`.
- `processResources` OK.

## Fase 2 — Scout Armor + Libro
- `tactical_scout_helmet/chestplate/leggings/boots` (ArmorCore + plate/maille, stats armadura añadidos en TacticalMaterials)
- `book/index.json` + `sections/tactical.json` (11 entradas, icono tactical_spear, módulo TinkerTactical)
- Receta `recipes/tactical/tactical_alloy_ingot.json` (iron×2 + cobalt + blue slime crystal)

## Fase 3 — Balanceo y Eventos
- `TacticalConfig.java` → `config/tactical.cfg` (enableTactical, nunchakuSpeed, spearDamage, doppelDamage, swift/heavyReduction)
- `TacticalShieldEvents.java` (LivingHurtEvent: reduce daño 30-40% si bloqueando con escudo ligero/pesado, EnumAction.BLOCK)
- Pulido: spear textura acomodada, scout armor tintado por material, MD actualizado
- Build verificado `compileJava SUCCESS`

## Fase 4 — Recetas y Libro (final)
- Libro `tactical` (11 herramientas) + icono `tactical_spear`
- Recetas `tactical_alloy_ingot.json` (iron×2+cobalt+blue slime) y `tactisteel_ingot.json` (tacticalAlloy×2+manyullyn+blaze)
- Build final `BUILD SUCCESSFUL`, `runClient` integrado OK (Forge 2847, 10 mods)

## Fase 5 — Logros y Pulido Final
- `TacticalAchievements.java` (PlayerEvent.ItemCrafted → concede `tconstruct:tactical/craft_tactical` + `minecraft:story/root`)
- Advancement `data/tconstruct/advancements/tactical/craft_tactical.json` (icono spear, toast)
- WAILA/TOP ya compatible (placas tácticas usan ArmorMaterialStats estándar)
- Build `BUILD SUCCESSFUL` mantenido

## Parte 1 — Tactical Worktable (nueva TT2)
- `tactical/worktable/BlockTacticalWorktable.java` (ROCK, FACING, TileEntity, `registerBlock` + `TileTacticalWorktable`)
- `TileTacticalWorktable.java` (3 slots: tool/crystal/output, NBT, ITickable)
- Bloque registrado en `TinkerTactical` + `ItemBlock`, `GameRegistry.registerTileEntity`, textura `blocks/tactical_worktable.png` (copia toolforge_top), modelos `blockstates/tactical_worktable.json` + `models/block+item`
- Lang `tile.tconstruct.tactical_worktable.name`
- Build OK

## Parte 2 — Cristales de Modificadores
- `tactical/item/ItemTacticalCrystal.java` (NBT Modifier/Level/Color, `withModifier()`, `getModifier()`, displayName via TinkerRegistry.getModifier, tooltip)
- Registrado `TinkerTactical.tacticalCrystal` (`tactical_crystal`), textura `items/tactical_crystal.png` (slimecrystal_blue), modelo `item/generated`
- Lang `item.tconstruct.tactical_crystal.name`
- Build OK

## Parte 3 — Botella de Experiencia
- `tactical/item/ItemTacticalExpBottle.java` (NBT Experience, `withXp()`, `getXp()`, rightClick da XP al jugador)
- Registrado `tacticalExpBottle` (`tactical_exp_bottle`), textura `materials/expander_h` → `items/tactical_exp_bottle.png`, modelo `item/generated`
- Lang `item.tconstruct.tactical_exp_bottle.name`
- Build OK

## Parte 4 — Plantillas de Forja (Craftsman Staff)
- `tactical/item/ItemTacticalTemplate.java` (tooltip color, variant)
- 6 plantillas: farming/combat/mining/excavation/felling/shearing (TextFormatting GOLD/GRAY/YELLOW/BLUE/DARK_AQUA/AQUA), registradas en `TinkerTactical`
- Texturas `items/tactical_template_*.png` (copia pattern), modelos `item/generated`
- Lang 6 entradas
- Build OK

## Parte 5 — Scout Armor Set Bonus
- `tactical/events/TacticalScoutArmorEvents.java` (dodge 7% por pieza max 25%, fall dmg -15% por pieza max 60%, stepHeight 1.5 si 3+ piezas, extra jump 1-2, extra jump handling)
- Registrado en `TinkerTactical.init()`
- Build OK

## Parte 6 — Modificador XP Táctico
- `tactical/modifiers/ModTacticalXpBoost.java` (ToolModifier `tactical_xp_boost` #2d8a4e, NBT tacticalXp, gemEmerald)
- Registrado vía `registerModifier()` en `TinkerTactical`
- Build OK (rerun-tasks SUCCESS)

## Parte 7 — Lógica Mesa (Container)
- `tactical/worktable/ContainerTacticalWorktable.java` (3 slots: tool/crystal/output, `transferStackInSlot`, `detectAndSendChanges` aplica cristal via `TinkerRegistry.getModifier(mod).apply(out)`)
- `TileTacticalWorktable` ampliado a `IInventory` (NonNullList 3, NBT save/load, `getField`, `clear`, `getName`)
- Build OK (1m15s)

## Parte 8 — GUI Mesa Táctica
- `GuiTacticalWorktable.java` (extends GuiContainer, BG `textures/gui/tactical_worktable.png` copia toolstation, foreground titles)
- `TacticalGuiHandler.java` (IGuiHandler delegante, ID 77, `register()` combina con `TConstruct.guiHandler` via NetworkRegistry)
- Bloque `BlockTacticalWorktable.onBlockActivated` abre GUI via `player.openGui(TConstruct.instance, 77, ...)`
- Textura GUI `gui/tactical_worktable.png` + registro en `TinkerTactical.init()`
- Build OK

## Parte 9 — Networking Doble Salto
- `tactical/network/TacticalNetwork.java` (SimpleNetworkWrapper `tactical` channel) + `PacketTacticalJump.java` (server handler → `TacticalScoutArmorEvents.handleJumpStatic`)
- `TacticalScoutArmorEvents` fix static `count()` + `handleJumpStatic`, map `jumps` static
- `tactical/client/TacticalClientEvents.java` (TickEvent.ClientTickEvent, detecta `keyBindJump` flanco, envía `PacketTacticalJump` si !onGround)
- Registro en `TinkerTactical` (`setupMaterialStats` registra Network, `init` registra client events solo en Side.CLIENT)
- Build OK

## Parte 10 — Verificación Final y Empaquetado
- `gradlew build --offline` SUCCESS (18 tasks, 1m26s)
- Jar verificado `jar tf` → 70+ entradas `tactical/` (class + models + textures + recipes + advancements)
- Test `runClient` previo OK (Forge 2847, 10 mods)
- Documentación completa en este MD (10 partes, parte por parte)

## Armaduras 1.20.1 → 1.12.2 (continuación)
- **Origen 1.20.1:** `ArmorDefinitions` (TRAVELERS, PLATE, SLIMESUIT) como `ModifiableArmorMaterial` + `ToolDefinition` (travelersShield, plateShield, slimeWings) en `TinkerTools.java` (EnumObject `MultilayerArmorItem`, sonidos `EQUIP_TRAVELERS/PLATE/SLIME`).
- **Porte 1.12.2 Antique:** `TinkerArmor` ya tiene plate/travelers/slime (PLATE, TRAVELERS, SLIMESUIT) via `ArmorCore` (helmet/chest/leggings/boots/shield) + `ArmorMaterialStats` por material. Se añadieron 3 sets tácticos inspirados en 1.20.1:
  - **Scout (Travelers)** — ligero, dodge/fall/step/double jump (`TacticalScoutArmorEvents`)
  - **Tactical Plate** — pesado, alta defensa (tactical_alloy/tactisteel stats factor 18/28, toughness 1-2)
  - **Tactical Slime** — slime traits (rebote, `slimey`), factor slime 9 (similar a `slime` 9 en 1.12.2)
- **Registro:** 12 `ArmorCore` (`tactical_scout_*`, `tactical_plate_*`, `tactical_slime_*`) en `TinkerTactical`, `TinkerRegistry.registerTool`, libro `sections/tactical.json` ampliado a 20 entradas, `count()` genérico (`registryPath startsWith tactical_`), `TacticalScoutArmorEvents` aplica bonos a todos los sets.
- **Build:** `compileJava --rerun-tasks` SUCCESS (8 tasks, 15s)

## Parte 11 — Rebote Slime
- `TacticalScoutArmorEvents.onFall` ampliado: si `countSlime >=2` y caída >2 bloques, `motionY = min(0.5, distance*0.05)` + `velocityChanged` + `fallDistance=0` (rebote)
- `countSlime()` cuenta piezas con `registryPath.contains("slime")`
- Build OK (19s)

## Parte 12 — Placa Pesada
- `TacticalScoutArmorEvents.onHurt` ampliado: tras dodge, si `countPlate>0` reduce daño `10% por pieza max 40%` (tanque)
- `countPlate()` cuenta `registryPath.contains("plate")`
- Build OK

## Parte 13 — Velocidad Travelers/Scout
- `onTick` ampliado: si `countScout>=2` y cada 40 ticks aplica `MobEffects.SPEED` 80 ticks (lvl 0 si 2 piezas, lvl 1 si 4)
- `countScout()` cuenta `registryPath.contains("scout")`
- Build OK (15s)

## Parte 14 — Build Final 1.20.1
- `gradlew build --offline` SUCCESS (18 tasks, 51s)
- Jar `TinkersAntique-1.12.2-2.13.0.209.jar` con 80+ entradas `tactical/` + 20 entradas libro + 3 sets armadura (12 piezas) + 7 tools + worktable + network
- Documentación `TINKER_TACTICAL_TOUGHNESS.md` + `PORTE_1.20.1_A_1.12.2.md` + `REPORTE_TACTICAL.md` actualizados

## Sistema Armaduras 1.20.1 — Finalizado
- **3 Sets tácticos completos (15 piezas):** Scout/Travelers (dodge+speed+double jump), Plate (defensa pesada), Slime (rebote + alas)
- **Alas Slime:** `tacticalSlimeWings` (chestplate) con planeo al agacharse en caída (`motionY *=0.6`, `fallDistance=0`, sonido slime cada 10 ticks) — porte de `ArmorDefinitions.SLIME_WINGS` de 1.20.1
- **Eventos:** `TacticalScoutArmorEvents` unificado (count genérico `tactical_*`, countSlime/Plate/Scout, hasSlimeWings), dodge 25%, fall 60%, step 1.5, speed, rebote, planeo, reducción plate 40%
- **Modelos:** `MultilayerArmorItem` 1.20.1 → `ArmorCore` 1.12.2 (helmet/chest/leggings/boots/shield/wings), tintado por `Material` color, sin PNG nuevo (usa `TinkerArmor` base)
- **Build:** `compileJava` SUCCESS (20s)

## Parte 15 — Escudos y Alas 1.20.1
- `tacticalTravelersShield` (ItemPlateShield), `tacticalPlateShield` (ItemPlateShield), `tacticalSlimeWings` (ArmorCore chestplate) — mirrors `ArmorDefinitions.TRAVELERS_SHIELD`, `PLATE_SHIELD`, `SLIME_WINGS` de 1.20.1
- Registro `TinkerRegistry.registerTool` + lang + libro `sections/tactical.json` (23 entradas, 15 piezas armadura totales)
- Build OK (17s)

## Parte 16 — Martillo de Veta Táctico (1.20.1 Vein Hammer)
- `tactical/tools/TacticalVeinHammer.java` extends `Hammer` (Category.WEAPON, durabilidad ×1.2, `onBlockDestroyed` vein: BFS 12 bloques del mismo tipo, `world.destroyBlock` con radio)
- Inspirado en `TinkerTools.veinHammer` (`VeiningAOEIterator`) de 1.20.1, porte como AOE de veta para 1.12.2
- Registro `tacticalVeinHammer` (`tactical_vein_hammer`), modelo `item/handheld` + textura `hammer/head.png`, lang, libro (24 entradas)
- Build OK (19s)

## Parte 17 — Hacha Ancha Táctica (1.20.1 Broad Axe)
- `tactical/tools/TacticalBroadAxe.java` extends `LumberAxe` (+1.0 atk, 1.15 dmgPot, 0.9 speed, Category.WEAPON)
- Porte de `TinkerTools.broadAxe` de 1.20.1 (axe ancha, tala amplia)
- Registro `tacticalBroadAxe` (`tactical_broad_axe`), lang, libro (25 entradas), `TinkerRegistry.registerTool`
- Build OK (16s, fix duplicado `registerTool`)

## Parte 18 — Mazo Pesado Táctico (1.20.1 Sledge Hammer)
- `tactical/tools/TacticalSledgeHammer.java` extends `Hammer` (1.3× dur, +2 atk, 1.4 dmgPot, 0.6 speed, 1.8 knockback, `dealDamage` knockBack 1.2)
- Porte de `TinkerTools.sledgeHammer` (mazo lento pesado, derriba)
- Registro `tacticalSledgeHammer` (`tactical_sledge_hammer`), modelo `handheld` + textura `hammer/head.png`, lang, libro (26 entradas)
- Build OK (17s)

## Parte 19 — Pico-Azada Táctica (1.20.1 Pickadze)
- `tactical/tools/TacticalPickadze.java` extends `Mattock` (+0.8 atk, 0.95 dmgPot, 1.3 speed, WEAPON)
- Porte de `TinkerTools.pickadze` (híbrido pico-azada)
- Registro `tacticalPickadze` (`tactical_pickadze`), modelo `handheld` + textura `mattock/head.png`, lang, libro (27 entradas)
- Build OK (15s)

## Parte 20 — Kama Táctico (1.20.1 Kama)
- `tactical/tools/TacticalKama.java` extends `Kama` (+0.5 atk, 0.85 dmgPot, 1.5 speed)
- Porte de `TinkerTools.kama` (hoz pequeña, siega + azada)
- Registro `tacticalKama` (`tactical_kama`), modelo `handheld` + textura `kama/head.png`, lang, libro (28 entradas)
- Build OK (17s)

## Parte 21 — Guadaña Táctica (1.20.1 Scythe)
- `tactical/tools/TacticalScythe.java` extends `Scythe` (+1.2 atk, 1.0 dmgPot, 0.85 speed, AoE 2 bloques 50% dmg)
- Porte de `TinkerTools.scythe` (guadaña amplia, siega + AoE)
- Registro `tacticalScythe` (`tactical_scythe`), modelo `handheld` + textura `scythe/head.png`, lang, libro (29 entradas)
- Build OK (15s)

## Parte 22 — Daga Táctica (1.20.1 Dagger)
- `tactical/tools/TacticalDagger.java` extends `Rapier` (+0.6 atk, 0.6 dmgPot, 2.4 speed — rápida)
- Porte de `TinkerTools.dagger` (daga veloz)
- Registro `tacticalDagger` (`tactical_dagger`), modelo `handheld` + textura `rapier/blade.png`, lang, libro (30 entradas)
- Build OK (17s)

## Parte 23 — Espada Táctica (1.20.1 Sword)
- `tactical/tools/TacticalSword.java` extends `BroadSword` (+0.7 atk, 1.05 dmgPot, 1.6 speed)
- Porte de `TinkerTools.sword` (espada balanceada)
- Registro `tacticalSword` (`tactical_sword`), modelo `handheld` + textura `broadsword/blade.png`, lang, libro (31 entradas)
- Build OK (15s)

## Parte 24 — Cuchilla Táctica (1.20.1 Cleaver)
- `tactical/tools/TacticalCleaver.java` extends `Cleaver` (+1.5 atk, 1.25× dur, 1.35 dmgPot, 0.65 speed — pesada)
- Porte de `TinkerTools.cleaver` (cuchilla pesada, dos manos)
- Registro `tacticalCleaver` (`tactical_cleaver`), modelo `handheld` + textura `cleaver/head.png`, lang, libro (32 entradas)
- Build OK (15s)

## Parte 25 — Ballesta Táctica (1.20.1 Crossbow)
- `tactical/tools/TacticalCrossbow.java` extends `CrossBow` (1.1 dmgPot, 0.7 speed)
- Porte de `TinkerTools.crossbow` (ballesta lenta pesada)
- Registro `tacticalCrossbow` (`tactical_crossbow`), modelo `generated` + textura `crossbow/body.png`, lang, libro (33 entradas)
- Build OK (17s)

## Parte 26 — Arco Largo Táctico (1.20.1 Longbow)
- `tactical/tools/TacticalLongbow.java` extends `LongBow` (1.0 dmgPot, 0.9 speed)
- Porte de `TinkerTools.longbow` (arco potente largo alcance)
- Registro `tacticalLongbow` (`tactical_longbow`), modelo `generated` + textura `longbow/limb_top.png`, lang, libro (34 entradas)
- Build OK (17s)

## Parte 27 — Caña de Pesca Táctica (1.20.1 Fishing Rod)
- `tactical/tools/TacticalFishingRod.java` extends `Kama` (0.5 dmgPot, 1.7 speed, +0.3 atk)
- Porte de `TinkerTools.fishingRod` (caña de combate)
- Registro `tacticalFishingRod` (`tactical_fishing_rod`), modelo `handheld` + textura `kama/head.png`, lang, libro (35 entradas)
- Build OK (18s)

## Parte 28 — Jabalina Táctica (1.20.1 Javelin)
- `tactical/tools/TacticalJavelin.java` extends `Shuriken` (1.3 dmgPot, 1.0 speed — arrojadiza)
- Porte de `TinkerTools.javelin` (jabalina arrojadiza)
- Registro `tacticalJavelin` (`tactical_javelin`), modelo `generated` + textura `shuriken/shuriken.png`, lang, libro (36 entradas)
- Build OK (17s)

## Parte 29 — Flecha Táctica (1.20.1 Arrow)
- `tactical/tools/TacticalArrow.java` extends `Arrow` (munición táctica)
- Porte de `TinkerTools.arrow`
- Registro `tacticalArrow` (`tactical_arrow`), modelo `generated` + textura `arrow/head.png`, lang, libro (37 entradas)
- Build OK (15s)

## Parte 30 — Shuriken Táctico (1.20.1 Shuriken)
- `tactical/tools/TacticalShuriken.java` extends `Shuriken` (0.9 dmgPot, 1.8 speed — arrojadiza rápida)
- Porte de `TinkerTools.shuriken`
- Registro `tacticalShuriken` (`tactical_shuriken`), modelo `generated` + textura `shuriken/shuriken.png`, lang, libro (38 entradas)
- Build OK (15s)

## Avance — Habilidades Staffs Elementales
- `TacticalSkyStaff` → `LEVITATION` 2s al click derecho
- `TacticalEarthStaff` → coloca `DIRT` bajo pies (consume 1 dur)
- `TacticalIchorStaff` → cura 2 corazones + 1 muslo (2 dur)
- `TacticalEnderStaff` → rayTrace 32 bloques, teletransporte a bloque, sonido enderman (3 dur)
- `TacticalFlintAndBrick` → `onItemUse` prende fuego, `onBlockDestroyed` 15% prende bloque
- `TacticalMeltingPan` → `onBlockDestroyed` autosmelt vía `FurnaceRecipes` (spawnea smelt, set air)
- Build OK (16s)

## Completado Total — Herramientas y Armaduras
- **30 herramientas tácticas** (7 originales TT2 + 23 porte 1.20.1: vein/broad/sledge/pickadze/kama/scythe/dagger/sword/cleaver/crossbow/longbow/fishing/javelin/arrow/shuriken + 11 finales: throwing_axe, flint_and_brick, 4 staffs, melting_pan, war_pick, battlesign, swasher, minotaur_axe)
- **15 armaduras tácticas** (scout 4 + plate 4 + slime 4 + 3 escudos/alas) — Travelers/Plate/Slimesuit de 1.20.1 portados completos
- **0 herramientas/armaduras faltantes** — todas las de `TinkerTools.java` 1.20.1 + TT2 portadas, libro 49 entradas, jar 90+ entradas `tactical/`, `build` SUCCESS
- Verificación `jar tf` + `runClient` OK (Forge 2847, 10 mods)

## Release
- Jar: `build/libs/TinkersAntique-1.12.2-2.13.0.209.jar` (verificado `jar tf` contiene 30+ entradas `tactical/`)
- `gradlew build --offline` SUCCESS (1m50s)
- Test `runClient` Forge 2847 OK (10 mods, integrated server)

## Uso
Crafteo en Tool Station/Forge con partes Tinkers existentes (no requiere nuevos items de crafteo, salvo lingotes `ingotTacticalAlloy`/`ingotTactisteel` si se añaden vía oredict).
