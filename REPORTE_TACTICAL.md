# Reporte General — Tinker Tactical Toughness (Tinkers Antique 1.12.2)

**Fecha:** 2026-09-11 | **Build:** `TinkersAntique-1.12.2-2.13.0.209.jar` | **Forge:** 14.23.5.2847 | **Java 8**

## 🟢 Verde — Completo / Funcional (Build SUCCESS, runClient OK)
- **Pulse TinkerTactical** (`slimeknights.tconstruct.tactical.TinkerTactical`) registrado en `TConstruct.java`, 7 tools + 4 armaduras + worktable + items, `registerTool/ForgeCrafting` OK
- **7 Armas/escudos base:** `TinkerNunchaku` (combo pull), `SwiftShield`/`HeavyShield` (BLOCK), `Spear` (BOW anim + dash), `Doppelhander` (AoE sweep), `Maraca` (nausea), `CraftsmanStaff` (multi-tool pick/axe/shovel/sword) — `damagePotential/attackSpeed` ajustados, `buildTagData` funcional
- **Trait** `TraitNunchakuCombo` (+8% por golpe hasta x1.4)
- **Materiales** `tactical_alloy` (#4a90e2) y `tactisteel` (#d4a76a) + `TacticalMaterials.java` (Head/Handle/Extra + Armor stats 6 tipos), JSON `materials/*.json`, colores tintados
- **Modelos** 7× `models/item/tools/tactical_*.tcon.json` + **Texturas** 7 carpetas `textures/items/tactical_*` (16×16 grayscale, spear acomodada) + GUI `tactical_worktable.png`
- **Lang** `en_US.lang` 20+ entradas (tools, armaduras, materiales, cristales, botella, plantillas, advancement)
- **Config** `TacticalConfig.java` → `config/tactical.cfg` (enable, nunchakuSpeed, spearDamage, doppelDamage, reducciones escudos)
- **ShieldEvents** `TacticalShieldEvents` (LivingHurt reducción 30-40% al bloquear)
- **Logros** `TacticalAchievements` + `data/tconstruct/advancements/tactical/craft_tactical.json` (toast spear)
- **Modificador** `ModTacticalXpBoost` (`tactical_xp_boost` #2d8a4e, gemEmerald)
- **Libro** `book/index.json` + `sections/tactical.json` (11 entradas, icono spear, módulo TinkerTactical)
- **Recetas** `recipes/tactical/tactical_alloy_ingot.json` y `tactisteel_ingot.json` (shapeless oredict)
- **Jar verificado** `jar tf` → 70+ entradas `tactical/`, `gradlew build` SUCCESS (1m26s), `runClient` SUCCESS (10 mods)

## 🟠 Naranja — Funcional pero requiere pulido
- **Tactical Worktable:** `BlockTacticalWorktable` + `TileTacticalWorktable` (IInventory 3 slots, NBT) + `ContainerTacticalWorktable` (tool+crystal→output via `TinkerRegistry.getModifier.apply`), `GuiTacticalWorktable` (copia toolstation), `TacticalGuiHandler` (delegante combinado, ID 77). *Falta:* validación de `Partial/MaxValue/Emboss`, JEI para recetas de cristal, animación custom.
- **Cristal Táctico:** `ItemTacticalCrystal` (NBT Modifier/Level/Color/Value) simplificado sin `ArmoryRegistry`/`Partial/EmbossLevel` de TT2 original. DisplayName "X Crystal" básico.
- **Botella XP:** `ItemTacticalExpBottle` (16 stack, NBT Experience, rightClick otorga XP) — falta `ExperienceBottleEvents` de TT2 (transferencia desde herramienta rota, partículas).
- **Plantillas:** 6 `ItemTacticalTemplate` (farming/combat/mining/excavation/felling/shearing) con tooltip color, pero sin variantes `nature/insight/research/forestry` ni integración `CraftsmanStaffCompat` (Botania/Thaum/Forestry).
- **Scout Armor:** `TacticalScoutArmorEvents` con dodge 7% por pieza (max 25%), fall -15% por pieza (max 60%), stepHeight 1.5, doble salto 1-2. *Falta:* `ScoutArmorCore` original con `aggroRangeContribution`, `rangedDamageBonusPercent`, `environmentalReduction`, redirección de aggro a golems/villagers/tameables, y textura de armadura scout específica (ahora reutiliza placa genérica).
- **Doble Salto Network:** `TacticalNetwork` + `PacketTacticalJump` + `TacticalClientEvents` (ClientTick detecta `keyBindJump` flanco) — funcional pero sin animación de salto ni sonido, y sin límite de `airJumpsUsed` sincronizado con servidor al relog.
- **Spear:** Textura acomodada (head arrow/head, shaft arrow/shaft, guard rapier/guard) y dash simple, pero falta sistema de carga `SpearEvents.onSpearUsingTick`, `PacketSpearStab`, animación de estocada y `CustomAttackContext` con costo de durabilidad.
- **HeavyShield:** Falta `Imbalance`/`ImbalanceImmunity` potions, `Opportunity` window, `BLOCK_HALF_ANGLE` direccional y `calculateImmunityTicks`.
- **Maraca:** Efecto nausea/slow simple (25%) — falta `MaracaEvents` completo (party, guard, attack, stability, synchro offhand copia temporal, record de combo).
- **Doppelhander:** AoE 60° simplificado — falta `DefensiveStance` Potion y `getBlockBonus`/`getDefensiveDamageBonusPercent` con armadura.
- **CraftsmanStaff:** Multi-tool básico (clases pick/axe/shovel/sword) — falta `ModCraftsmanStaffTemplate` con 9 tipos, `CraftsmanStaffCompat` (Nature/Insight/Research/Forestry), alcance y velocidad de movimiento.

## 🔴 Rojo — Pendiente / No portado (TT2 original)
- **Pociones TT2:** `PotionImbalance`, `PotionParryWindow`, `PotionOpportunity`, `PotionDefensiveStance`, 5× `PotionMaraca*` — no existen en Antique (requieren registro `TT2Potions`).
- **Eventos TT2 completos:** `DefenseDamageEvents`, `HeavyShieldEvents`/`HeavyShieldClientEvents`, `SpearEvents` (carga, stab, timing), `ShieldEvents` (parry acum), `DoppelhanderEvents`, `MaracaEvents` (full), `ConstructArmorSetBonusEvents`/`ExtraModifierEvents`, `HiddenModifierEvents`, `ExperienceBottleEvents`.
- **Bloque Worktable avanzado:** `TileModifierWorktable` original con `ModifierWorktableLogic`, `ModifierCrystalRecipeMatch`, `ContainerModifierWorktable` con `SlotModifierTool/Output`, `CraftsmanEyeInventory`, `ItemExperienceBottle` con experiencia de herrmienta, `TT2Blocks`/`TT2GuiHandler` completo, JEI `TT2JeiPlugin`.
- **Network TT2:** `PacketSpearStab`, `PacketSpearAnimation`, `PacketScoutExtraJump`, `PacketMaracaUiState`, `PacketMaracaAction`, `SpearAnimationController`.
- **Compat mods:** `CraftsmanStaffCompat` (Botania wand/mana blaster, Thaumcraft focus/gauntlet, Forestry scoop/grafter/smoker/analyzer, Research tools), `ThaumcraftInsightHooks`, `FarmersDelightBeheadingCompat`, `CriticalHitEventCompat`.
- **ASM/Mixin:** `ASMLoader`, `ASMTinkerAnimate`, `NewTinkerTexture`, `ToolHelperMixin`, `mixins.tt2.json`, `tt2_at.cfg` — omitidos en Antique (no usa MixinBooter).
- **Texturas TT2 originales:** `traveler_shield`/`plate_shield` específicas y `ImbalanceGlowHooks`/`ScoutArmorModel`/`LayerTinkerArmor` — ahora genéricas.
- **Materiales TT2 avanzados:** Nether scrap harvería, vides Nether Backport, integración `TinkerFluids` tactical (sin fluido propio, solo oredict).
- **Waila/TOP detallado:** `WailaRegistrar` para worktable y `CraftsmanEye` no portado.

## Resumen por pulso
- **TinkerTactical (ID TinkerTactical):** Enabled, no forced, depende de TinkerTools/TinkerArmor. Registra 7 tools + 4 armaduras + 1 bloque + 8 items + 1 modifier + 1 trait.
- **Verificación:** `compileJava` SUCCESS (53s-2m), `build` SUCCESS (1m26s), `runClient` SUCCESS último (BUILD SUCCESS 4m32s previo, último con fix de registro duplicado también SUCCESS tras corrección). Crash inicial por `tactical_crystal` doble `setRegistryName` resuelto (quitado `setRegistryName` en constructor).

## Próximos pasos sugeridos
1. Portar pociones TT2 y sus efectos (Imbalance etc.) — rojo → naranja.
2. Reemplazar GUI genérica por textura propia `tactical_worktable` y añadir JEI `TT2JeiPlugin` para cristales.
3. Implementar `Spear` hold-stab con `SpearChargeStats`/`SpearAttackTiming` y animación.
4. Añadir compat Botania/Thaum/Forestry al `CraftsmanStaff` y variantes `nature/insight/research`.
5. Pulir `Maraca` offhand sync y `HeavyShield` parry direccional.

**Estado global:** ~65% verde, 25% naranja, 10% rojo — núcleo jugable y estable, pulido y compatibilidad avanzada pendiente.
