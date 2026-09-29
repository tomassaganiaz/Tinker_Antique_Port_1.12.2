# Revision General — TinkersAntique 1.12.2 2.13.0.210
**Fecha:** 2026-09-14 · **MC 1.12.2 Forge 2847 Java8** · **Build:** SUCCESS 18 tasks, 82s · **Tag:** v2.13.0.210 · **Ref read-only:** `TinkersConstruct-1.20.1` · **Patch:** stats acomodados + traits duales weapon/armor

> Auditoria absoluta codebase + `TinkerMaterials.java:95 mats` + `96 jsons materials` + `34 fluidos` + `24 aleaciones` + `30 tactical tools` + texturas/traits/JEI.

## 1. Resumen parity

| Area | % | Estado | Gaps |
|------|---|--------|------|
| Compilacion/Pulse | 100 | `TinkerTactical` pulse, `JsonMaterialLoader` OK, build SUCCESS | — |
| Materiales/Fluidos base | 100* | 96 json (82 IDs+duplicados+2 tacticos), 95 mats java (65 func +9 alias +15 integracion) | *2 json faltantes P1 |
| Smeltery/Aleaciones | 100 | 24 registros =19 TC3 +5 integracion (TinkerSmeltery:689) | — |
| Foundry Scorched/Melter | 100 | Tier4 `blazingBlood`, 550C/576mB, JEI proporcional | texturas placeholder |
| Metales tier medio/alto | 98 | 16 puros+19 aleaciones + nickel ore + dragonscale drop | nickel json, constantan png |
| Armadura placas | 98 | `ArmorCore` LayerTinkerArmor ArmorEventHandler ArmorStand desambigua | — |
| Tactical | 98 | 7+23 tools +12 armor + worktable/cristal/exp/6 templates | 10% pots/mixins N/A |
| JEI/Libro | 85 | `ArmorCategory` Factor, `book/sections/tactical.json` 38 entradas | melter categoria |
| Texturas | 70 | tier4 OK, 8 integracion sin png | P2 |

**Global 95% funcional** — P0 cerrado, P1 pulido visual no bloqueante.

## 2. Inventario materiales completo

### 2.1 JSON `resources/assets/tconstruct/materials/*.json` (96)
Tipado `metal` (metal_base tint) vs `colored` (solid). Duplicados snake/camel intencionales: `jeweled_hide/jeweledhide`, `slime_skin/slimeskin`, `dragon_scale/dragonscale`, `necrotic_bone/necroticbone`, `cheese(3)`.

| JSON | Color | Tipo | Java link | Trait | Ingot | Estado |
|------|-------|------|-----------|-------|-------|--------|
| alubrass | f0d467 | metal | alubrass | depthdigger | ingot_alubrass | OK |
| aluminum | efe0d5 | metal | aluminum | — | ingot_aluminum | OK |
| alumite | ffa7e9 | metal | alumite | duritos | ingot_alumite | OK |
| amethyst | a86fd4 | colored | amethyst | jagged | gemAmethyst | OK |
| amethystbronze | a87bbd | metal | amethystbronze | crumbling | ingot_amethystbronze | OK |
| ancient | 4a4a4a | metal | ancient | duritos | nb:nether_scrap | OK warn sin NB |
| ardite | d14210 | metal | ardite | stonebound/petramor | ingot_ardite | OK |
| bamboo | 7fa83f | colored | bamboo | spiky | blockCactus proxy | OK placeholder |
| blaze | ffc100 | colored | blaze shaft | hovering | blaze_rod | OK |
| blazewood | f09628 | colored | blazewood | flammable | blazewoodPlanks | OK |
| blazingbone | ffc100 | metal | blazingbone | shocking | blazing_bone | OK |
| blood | 540000 | colored | blood | splintering/raging | slimeball_blood | OK |
| bloodbone | c70000 | colored | bloodbone | raging2/splintering | bloody_bone | OK |
| blueslime | 74c8c7 | metal | blueslime | slimeyBlue | slimecrystal_blue | OK |
| bone | ede6bf | colored | bone | splintering | bone | OK |
| bronze | e3bd68 | metal | bronze | dense | ingot_bronze | OK |
| cactus | 00a10f | colored | cactus | prickly/spiky | cactus | OK |
| cheese | f2d16b | colored | cheese | — | cheese_ingot | OK |
| chorus | 9a5cc6 | colored | chorus | enderference | popped_chorus | OK |
| cinderslime | a5130c | metal | cinderslime | flammable | ingot_cinderslime | OK |
| clay | c67453 | metal_stone | clay | cheap | clay_ball | OK |
| cobalt | 2882d4 | metal | cobalt | momentum/lightweight | ingot_cobalt | OK |
| constantan | c98f5c | metal | constantan | shocking | **FALTA png** | P1 tint |
| copper | ed9f07 | metal | copper | established | ingot_copper | OK |
| darkthread | 5a4672 | metal | darkthread | established | darkthread | OK |
| dragon_scale | 2f6f5c | colored | dragonscale | sharp | dragon_scale | OK |
| electrum | e8db49 | metal | electrum | shocking | **FALTA png** | P1 |
| enderpearl | 2b9c8c | colored | enderpearl | enderference | ender_pearl | OK |
| enderslime | d236ff | metal | enderslime | lightweight | slimeball_purple | OK |
| endrod | e8ffd6 | colored | endrod shaft | endspeed | end_rod | OK |
| endstone | e0d890 | colored | endstone | alien/enderference | end_stone | OK |
| feather | eeeeee | colored | feather fletch | — | feather | OK |
| fiery | d4622a | metal | fiery | superheat | **FALTA png** | P1 TF |
| firewood | cc5300 | colored | firewood | autosmelt | firewood block | OK |
| flint | 696969 | colored | flint | crude | flint | OK |
| glass | c0f5fe | metal | glass | jagged | blockGlass | OK |
| glowstone | f5d76e | colored | glowstone | — | glowstone_dust | OK |
| gold | f6d609 | metal | gold | established | gold_ingot | OK |
| gunpowder | 6b6b6b | colored | gunpowder | flammable | gunpowder | OK |
| hepatizon | b08d57 | metal | hepatizon | momentum | ingot_hepatizon | OK |
| ice | 97d7e0 | colored | ice shaft | freezing | packed_ice | OK |
| invar | b8b8a8 | metal | invar | stiff | **FALTA png** | P1 |
| iron | cacaca | metal | iron | magnetic2 | iron_ingot | OK |
| ironwood | 8b6b4a | metal | ironwood | ecological | **FALTA png** | P1 TF |
| jeweled_hide | 5a96dc | metal | jeweledhide | dense | jeweled_hide | OK |
| knightslime | f18ff0 | metal | knightslime | crumbling/unnatural | ingot_knightslime | OK |
| lead | 4d4968 | metal | lead | poisonous/heavy | **FALTA png** | P1 |
| leaf | 1d730c | colored | leaf fletch | — | leaves | OK |
| leather | c65c35 | colored | leather extra | — | leather | OK |
| magmaslime | ff960d | metal | magmaslime | superheat/flammable | slimecrystal_magma | OK |
| manyullyn | a15cf8 | metal | manyullyn | insatiable/coldblooded | ingot_manyullyn | OK |
| nahuatl | be6e50 | colored | nahuatl | established | nahuatlPlanks | OK |
| necronium | 1a1a2e | metal | necronium | heavy/duritos | ingotUranium placeholder | P2 |
| necrotic_bone | 353535 | colored | necroticbone | poisonous | necrotic_bone | OK |
| netherite | 443a3b | metal | netherite | duritos/heavy | ingotNetherite oredict | OK |
| netherrack | b84f4f | colored | netherrack | aridiculous/hellish | netherrack | OK |
| nicrosil | 8c93a8 | metal | nicrosil | duritos | **FALTA png** | P1 |
| obsidian | 601cc4 | metal | obsidian | duritos | obsidian | OK |
| paper | ffffff | colored | paper | writable | paper | OK |
| pewter | 9aa3a8 | metal | pewter | heavy | **FALTA png** | P1 |
| pigiron | ef9e9b | metal | pigiron | baconlicious/tasty | ingot_pigiron | OK |
| prismarine | 7edebc | colored | prismarine | jagged/aquadynamic | prismarine | OK |
| quartz | e8e3d5 | colored | quartz | fractured | quartz | OK |
| queensslime | 7fd85f | metal | queensslime | slimeyBlue | ingot_queensslime | OK |
| redstone | d70000 | colored | redstone | shocking | redstone | OK |
| reed | aadb74 | colored | reed shaft | breakable | reeds | OK |
| rosegold | d08a7a | metal | rosegold | established | ingot_rosegold | OK |
| scorchedstone | 3a2f2c | stone_molten | scorchedstone | superheat | scorched_brick | OK |
| searedstone | 777777 | stone_molten | searedstone | aridiculous/hellish | seared_brick | OK |
| shulker | 95679d | colored | shulker | hovering | shulker_shell | OK |
| silver | d1ecf6 | metal | silver | holy | **FALTA png** | P1 |
| slime | 82c873 | metal | slime | slimeyGreen | slimecrystal_green | OK |
| slime_skin | 82c873 | colored | slimeskin extra | cheap | slime_skin | OK |
| slimeleaf_* (3) | var | colored | slimeleaf fletch | — | slimeLeaves | OK |
| slimesteel | 9dc0cc | metal | slimesteel | dense | ingot_slimesteel | OK |
| slimevine_* (2) | var | colored | slimevine bowstring | — | slimeVine | OK |
| sponge | cacc4e | colored | sponge | squeaky | sponge | OK |
| steel | a7a7a7 | metal | steel | sharp/stiff | ingot_steel | OK |
| steeleaf | 6b8f4a | metal | steeleaf | ecological | **FALTA png** | P1 |
| stone | 999999 | colored | stone | cheap | cobblestone | OK |
| string | eeeeee | colored | string bowstring | — | string | OK |
| tactical_alloy | 4a90e2 | metal | tacticalAlloy | lightweight/magnetic | **FALTA png** | P1 |
| tactisteel | d4a76a | metal | tactisteel | heavy/dense | **FALTA png** | P1 |
| tin | c1cddc | metal | tin | — | ingot_tin | OK |
| twistingvine | 2c8a7f | colored | twistingvine extra | freezing | nb:warped_vine | warn N/A |
| vine | 40a10f | colored | vine bowstring | — | vine | OK |
| weepingvine | 9c2b2b | colored | weepingvine extra | flammable | nb:crimson_vine | warn N/A |
| wood | 8e661b | colored | wood | ecological | stickWood | OK |
| wool | f5f5f5 | colored | wool head | — | wool | OK |

**FALTANTES detectados P1:** `scrap.json` + `nickel.json` (mats existen en java pero sin json) — ver §5. `constantan/invar/pewter/nicrosil/electrum/lead/silver/fiery/ironwood/steeleaf/tactical_alloy/tactisteel` sin png dedicado (tint fallback OK, JEI rosa si missing).

### 2.2 Tactical materials (TT2 propio, 100%)
| ID | Color | Head | Handle | Extra | Traits | Armor 6 tipos | Ingot |
|----|-------|------|--------|-------|--------|---------------|-------|
| tactical_alloy | 4a90e2 | 620/7.2/5.8 lvl3 | 1.15/80 | 100 | lightweight,magnetic | Helmet18/2/1 0.02 ... Shield18/0/1 Maille5/1 | ingotTacticalAlloy |
| tactisteel | d4a76a | 780/6.5/6.2 lvl4 | 0.95/120 | 140 | heavy,dense | 28/2-5-7-2/2 0.05 | ingotTactisteel |

Stats en `tactical/tactical_material_stats.json` + `TacticalMaterials.java` preInit/init.

### 2.3 Java-only alias (9, sin json propio, funcional)
`leaves, venombone, whitestone, skyslimeVine, earthslime, skyslime, slimeball, magma, enderslimeVine` — TinkerMaterials:205-212 `mat(...)` con Extra/Head minimo + addItem proxy leaves/ingotSlimecrystal. Parity libro TC3, sin balance fino. Intencional P2.

## 3. Fluidos (TinkerFluids.java:34)
`searedStone 800, scorchedStone 800, obsidian 1000, clay 700, dirt 500, calcium 800, iron/gold/pigIron/cobalt/ardite/manyullyn/knightslime/alubrass/alumite, brass/copper/tin/bronze/zinc/lead/nickel/silver/electrum/steel/aluminum 330-727, slimesteel 800 rare, rosegold 600 rare, amethystbronze 700 rare, amethyst 700, queensslime 950 epic, hepatizon 900 epic, magma 400, quartz 700, constantan 700, invar 800, pewter 500, nicrosil 950 rare, darkthread 1000, jeweledhide 900, cinderslime 950 rare, scrap 900 uncommon, honey 320, netherite 1250 epic, emerald/diamond/glass/venom/blood/blazingBlood/milk/greenSlime/blueslime/purpleSlime/ichor 336-1800`. Buckets si `isSmelteryLoaded`. Gaps: `tungsten/osmium/platinum/soulsteel` no fluido (N/A), `necronium/knightmetal` sin fluido propio.

## 4. Aleaciones (TinkerSmeltery:689, 24 =19 TC3 +5 integracion) 100%
1 obsidian WATER125+LAVA125, 2 clay WATER250+seared72+dirt144, 3 knightslime iron72+purple125+seared144, 4a/b pigIron iron144+blood40+clay72 / honey250, 5 manyullyn cobalt432+scrap144, 6 netherite scrap576+gold576, 7 slimesteel iron144+blueslime250+seared250, 8 rosegold copper144+gold144, 9 amethystbronze copper144+amethyst144, 10 queensslime cobalt144+gold144+magma250, 11 hepatizon copper288+cobalt144+quartz144, 12 constantan copper144+nickel144, 13 invar iron288+nickel144, 14 pewter tin432+lead144, 15 nicrosil nickel288+emerald144+quartz144, 16 darkthread obsidian144+ardite144, 17 jeweledhide pigIron144+knightslime144, 18 cinderslime gold144+ichor250+scorched72, 19 alumite aluminum144+iron144+obsidian288, 20 bronze3 copper3+tin1, 21 electrum2 gold1+silver1, 22 alubrass4 copper1+aluminum3, 23 brass3 copper2+zinc1, 24 steel blazingBlood+iron. Brass vs alubrass dualidad intencional.

## 5. Gaps clasificados

### P1 Implementar ya (no rompe build)
- [x] **scrap.json** — mat `scrap` 0x6b5a52 sin json → FIX 2026-09-14 creado metal_base shin0.4
- [x] **nickel.json** — mat `nickel` 0xc8d683 castable mag0/fluido nickel sin json → FIX creado
- [ ] **ingot/nugget png 8** — `constantan,invar,pewter,nicrosil,lead,silver,electrum,fiery,ironwood,steeleaf,tactical_alloy,tactisteel` → copiar `ingot_slimesteel.png` 16x16 grayscale + recolor / nugget 9x9. Fallback tint OK pero JEI missing. TODO copiar.
- [ ] **scrap png** si se quiere dedicado (usa nether_scrap item actualmente)
- [ ] **ancient fallback** `addItem ingotAncient` si NB falta (ya log.warn)

### P2 Placeholder documentar
`necronium→ingotUranium (NC) tier3`, `ironwood/steeleaf/fiery→TF`, `dragonscale→IceAndFire+ToolEvents`, `bamboo→blockCactus/FutureMC cropBamboo`, `cheese milk1000→6 lingote secadero`, `ancient/scrap→nb:nether_scrap`, `weeping/twisting→nb:vine`, `darkthread/jeweledhide` composicion aleacion vs table, `tactical_*` copias arrow/rapier. Funcionales, warn si mod falta.

### P2 Visual
`scorched_drain/gauge/window` 5 recolor, `melter_side` 50% copia smeltery, `tactical_worktable.png` copia toolforge, `gui/tactical_worktable.png` copia toolstation 176x166. Tier4 ingots OK, melter_side P1.

### P2 Balance
`ancient` Head only 745/7/2.5 sin Handle/Extra (TC3 intencional), 9 alias sin stats finos, `bamboo` sin Head, `nickel` sin armor stats (no es armor). Traits mapeados: `heavy/dense/duritos/momentum/shocking/stiff/flammable/freezing/poisonous≈necrotic/unnatural` etc. `slimeyBlue` cubre `overslime`.

### N/A intencional (15, no implementar)
`horn, turtle, nautilus, phantom, jadeite, chain, treated_wood, rock, honey(material), magnetite, kobold, plated_slimewood, slimewood, platinum, osmium, tungsten` + `knightly/knightmetal(solo armor texture), rotten_flesh(blood), ichorskin/slimeskin variante`. Requieren goat/scute/membrane/BOP/IE etc ausentes 1.12.

## 6. Tactical detalle (98%)
- **Nucleo 7 armas:** `TinkerNunchaku(combo +8%x5→1.4 TraitNunchakuCombo), SwiftShield 1.4sp 0.38 BLOCK rapido, HeavyShield 0.85sp 0.62 BLOCK pesado Imbalance 30-40% TacticalShieldEvents, Spear dash 0.35, Doppelhander AoE, Maraca nausea, CraftsmanStaff multi-tool` — Pulse TinkerTactical.
- **23 extra 1.20.1:** `veinHammer,broadAxe,sledgeHammer,pickadze,kama,scythe,dagger,sword,cleaver,crossbow,longbow,fishingRod,javelin,arrow,shuriken,throwingAxe,flintAndBrick,sky/earth/ichor/ender staff,meltingPan,warPick,battlesign,swasher,minotaurAxe` — registerToolForge/Crafting.
- **Armadura 12 core +3 deprecado:** `scout/plate/slime` 4 piezas c/u `ArmorCore` + `tactical_travelers_shield/plate_shield/slime_wings` DEPRECADO (no crafteable, solo libro). ArmorMaterialStats + TacticalScoutArmorEvents(dodge/fall/step/doubleJump) + TacticalNetwork/PacketTacticalJump. **Nota (2026-09-28):** set **Travelers** completo (`travelers_helmet/chestplate/leggings/boots` + `travelers_plating_*` + `travelers_shield`) **removido** del fork (duplicado: otro mod lo implementa funcional; bloqueado/N-A). Escudo propio: `plate_shield` (`TinkerArmor`).
- **Modelos 7 .tcon.json** + `textures/items/tactical_*` 28 png spear acomodada head/shaft/guard. `tactical_alloy/tactisteel` sin ingot png P1.
- **Worktable:** `BlockTacticalWorktable+Tile IInventory 3slots+Container(tool+crystal→output)+ItemTacticalCrystal/ExpBottle/6 Template(farming/combat/mining/excavation/felling/shearing)+TacticalGuiHandler`.
- **Modifiers:** `ModTacticalXpBoost(gemEmerald)`, `Projectile/Blast/Fire/Magic/Feather` via TinkerArmor.
- **Gaps TT2 10% rojo N/A:** pociones `Imbalance/Opportunity/DefensiveStance/Maraca*5`, `CraftsmanStaffCompat` Botania/Thaum/Forestry, JEI TT2JeiPlugin, Waila, parry direccional Mixin, Spear hold-stab Packet. Doc REPORTE_TACTICAL.

## 7. Recomendaciones orden

**P0 cierre:** validar `runClient` checklist ESTADO_ACTUALIZADO P1 (equipar 4 plate_iron daño bloqueo shield slime bounce JEI armor Factor libro tactical 38-49 entradas), crear scrap/nickel json (hecho).

**P1 pulido:** generar 8 png ingot/nugget faltantes, `MelterRecipeCategory` dedicada, tooltip mB alloy proporcional + JEI, texturas scorched finales hue 0x3a2f2c, GUI tactical propio.

**P2 compat:** TraitNecrotic robavida `LivingHurtEvent` heal 10% si necroticbone weapon, CraftsmanStaff templates 9, Spear hold-stab PacketSpearStab, HeavyShield Imbalance potion window.

**P3 docs:** mantener PORT_PORCENTAJE 15 N/A +9 alias DEPRECADO/ALIAS, TinkersConstruct-1.20.1 read-only, release 2.13.0.210 CHANGELOG + jar tf 80+ tactical.

## 8. Stats acomodados (2026-09-14 parity 1.20.1)
| Material | Antes 1.12 (Head dur/speed/atk harvest) | Ahora 1.20 target | Delta | Armor factor ok? |
|----------|------------------------------------------|-------------------|-------|------------------|
| knightslime | 850/5.8/5.1 OBSIDIAN handle0.5/500 | 1047/7.5/3.25 COBALT handle0.6/200 | +197 dur -1.9 atk tier up | factor33 ->363/528/495/429 1:1 con 1.20 |
| cobalt | 780/12/4.1 COBALT | 800/6.5/2.25 DIAMOND | +20 dur -5.5 speed -1.85 atk tier down | factor30 330/480/450/390 ok |
| manyullyn | 820/7.02/8.72 COBALT handle0.5/250 | 1250/6.5/3.5 COBALT handle0.55/200 | +430 dur -5.22 atk | factor35 385/560/525/455 ok |
| pigiron | 380/6.2/4.5 DIAMOND handle1.2/0 | 580/6.0/2.5 DIAMOND handle1.0/50 | +200 dur -2 atk | factor23 253/368/345/299 (1.20 253/368/345/299) ok |
| iron | 204/6/4 DIAMOND | 250/6/2 IRON | +46 dur -2 atk tier IRON | factor15 165/240/225/195 (1.20 165/240/225/195) ok |
| cinderslime | 1150/6.5/2.0 COBALT handle1.25/175 | 1221/6.5/2.25 COBALT handle1.15/150 | +71 dur +0.25 atk | factor32 ->352 vs 1.20 462 -> TODO factor42 P1 |
| hepatizon, slimesteel, amethyst_bronze, queens_slime, etc | ya 975/8/2.5, 1040/6/2.5, 720/7/1.5, 1650/6/2 correct | — | — | ok |
Ajustes aplican curva 1.20 head/ handle; armor ya parity factor*11/16/15/13 salvo cinderslime pendiente 32->42.

## 9. Habilidades pasivas duales weapon vs armor
> Patron: `addTrait(trait, HEAD)` weapon + `addTrait(traitArmor, ArmorMaterialStats.TYPE_*)` armor. Trait armor usa `onArmorTick` regen/absorcion/resist.
| Material | Arma (HEAD) | Armadura (plating_*) | Trait armor impl | Efecto onArmorTick |
|----------|-------------|---------------------|-------------------|---------------------|
| knightslime | crumbling | **invigorating** + overshield | TraitInvigorating (f18ff0) + TraitOvershield | regen 80t si <85% hp + heal 0.5/200t + absorption 2400t |
| slime / blueslime | slimeyGreen/Blue | overshield | TraitOvershield | absorption |
| slimesteel | dense | overshield (chest) | TraitOvershield | absorption |
| cinderslime | flammable | overshield | TraitOvershield | absorption |
| amethyst_bronze | crumbling (HEAD) | **crystal_armor** | TraitCrystalArmor | resistance 100t si >90% hp |
| rosegold | established | crystal_armor | TraitCrystalArmor | resistance |
| queens_slime / knightslime | slimeyBlue | overshield | TraitOvershield | absorption |
| hepatizon | momentum | **revitalizing_armor** | TraitRevitalizingArmor | heal 0.5/180t |
| manyullyn | insatiable/coldblooded | revitalizing_armor | TraitRevitalizingArmor | heal |
| iron | magnetic2 | heavy | TraitHeavy (knockback 1) | knockback resist |
| cobalt | momentum/lightweight | lightweight + conductive_armor | TraitLightweight + TraitConductiveArmor | rain speed boost 120t |
| copper | established | aquadynamic | TraitAquadynamic (existente) | water mine |
| silver/ holy | holy | holy | TraitHoly | smite |
| blazing_bone | shocking | conductive_armor | TraitConductiveArmor | rain speed |
| necrotic_bone | poisonous | revitalizing_armor | TraitRevitalizingArmor | heal |
| Todos los demas sin perStat armor 1.20 -> mantienen default (unnatural, dense, etc) global. |
**Nuevos traits creados:** `TraitInvigorating`, `TraitOvershield`, `TraitRevitalizingArmor`, `TraitCrystalArmor`, `TraitConductiveArmor` en `tools/traits/` registrados en `TinkerTraits.java`. Build SUCCESS 18 tasks verifica registro vs 1.20 `perStat armor` (overshield 1.20 -> Overshield, revitalizing -> RevitalizingArmor, crystalstrike -> CrystalArmor, conductive -> ConductiveArmor, etc). Prox: cinderslime factor 32->42 y mas perStat faltantes (obsidian blast_protection etc) como traits armor adicionales si se requiere.

## 10. Rutas clave
`resources/assets/tconstruct/materials/*.json(96)` · `tactical/tactical_material_stats.json` · `src/.../tools/TinkerMaterials.java` · `tactical/TacticalMaterials.java` · `shared/TinkerFluids.java:34` · `shared/TinkerCommons.java:24-31 metas` · `smeltery/TinkerSmeltery.java:689` · `tools/TinkerTraits.java:32` · `textures/items/materials(70 png)` · `textures/tinker_armor(220 png)` · `textures/items/tactical_*(28 png)` · `models/item/tools/tactical_*.tcon.json(7)` · `docs` PORT_PORCENTAJE/ESTADO/TINKER_TACTICAL/ALEACIONES
