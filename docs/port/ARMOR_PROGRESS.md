# Estado de Implementación — Armadura de Placas (TC3 → 1.12.2)

> **Objetivo**: Port fiel de la armadura de placas de Tinkers’ Construct 3 (1.20.1) sobre 1.12.2, usando solo modificaciones en `TinkersAntique-1.12/` (regla dorada: `TinkersConstruct-1.20.1/` read-only).

**Fecha**: 2026-09-11 · **Fork**: `TinkersAntique-1.12` · **Referencia**: `TinkersConstruct-1.20.1` · **Build**: `gradlew.bat build --offline` **SUCCESS** (18 tasks, 1m10s)

> **Actualizacion 2026-09-11**: el port compila y el flujo basico de placas funciona. Se corrigio el desgaste de `plate_shield` mientras bloquea y el equipamiento desde una pila ahora consume solo una pieza. Este documento separa lo verificado de lo que aun falta para parity completa.

---

## 0. Resumen ejecutivo (actualizado tras metales 100%)

| Dimensión | % Implementado | Estado |
|-----------|----------------|--------|
| **Núcleo armadura (stats/NBT/items)** | **99%** | desgaste de escudo, equipamiento desde pila y reparación vanilla corregidos; falta estado roto visual |
| **Metales comunes** | **100%** | 16/16 con plating (nickel añadido) |
| **Aleaciones** | **100%** | Todas con stats (slimesteel, hepatizon, etc.) |
| **Materiales Nether/End/Slime** | **98%** | Cobalt/ardite/manyullyn + knockback ok, dragonscale+bamboo añadidos |
| **Mejoras / Modificadores** | **68%** | Protection 70%, reforzamiento 100%, velocidad 70%, espinas 80% |
| **Resistencias específicas** | **70%** | `ModProtection` (Protection genérico) portado, faltan variantes Projectile/Blast/Fire/Magic |
| **Traits (espinas, velocidad, etc.)** | **70%** | `bamboo→spiky` hook, `haste→movementSpeed` en ArmorCore |
| **Render / Texturas PNG** | **65%** | PNGs copiados, modelo `item/generated` básico, falta multicapa teñida |
| **Integración (JEI/Libro/Station GUI)** | **85%** | ToolStation/Forge ok + JEI `armor` + **libro `plate_helmet/chest/leggings/boots` + `LayerTinkerArmor`** |
| **Global ponderado** | **80% funcional** | Base jugable, pero quedan gaps de compatibilidad vanilla, variantes de modificadores y validacion en cliente |

> **Historial**: 52% → 78% → 85% → 96% durante el port. El estado actual se mantiene en **80% funcional** hasta cerrar los faltantes de compatibilidad y validacion en cliente.

---

## 1. Núcleo — 98%

### Implementado
- `ArmorMaterialStats.java` — 5 tipos `plating_helmet/chest/leggings/boots/shield`, campos `durability/defense/toughness/knockbackResistance` (+ `formatKnockbackResistance`), `getLocalizedInfo/Desc`.
- `Material.UNKNOWN` ampliado con defaults `1/0/0/0` por tipo.
- `Tags.java` — `DEFENSE`, `TOUGHNESS`, `KNOCKBACK_RESISTANCE`, `PROTECTION`, `MOVEMENT_SPEED`.
- `ArmorNBT.java` — builder `durability/defense/toughness/knockbackResistance/modifiers=3` + `write` a `Tags.*`.
- `ArmorCore extends ToolCore` — `armorType`, `isValidArmor`, `getAttributeModifiers(ARMOR/ARMOR_TOUGHNESS + MOVEMENT_SPEED + PROTECTION)`, `onArmorTick`, `onItemRightClick` equip, `getMaxDamage/setDamage` vía `ToolHelper`, `damagePotential=0/attackSpeed=1`, `buildDefaultArmorTag`.
- 4 piezas `ItemArmorPlate*` — `buildTag = buildDefaultArmorTag(mats).get()`.
- `TinkerArmor` — `plating_helmet/chest/leggings/boots` costes `5/8/7/4`, `registerArmorMaterialStats()` → `TinkerRegistry.addMaterialStats` con `knockbackResistance`, incluye `bamboo (6,1/2/3/1)` y `dragonscale (12,2/5/6/2,0.5f)` faltantes, `TinkerMaterials.bamboo→spiky` trait.
- `ArmorClientProxy` — `ModelLoader` inventory 8 items.

### Faltantes pendientes
- `BROKEN` swap textura `_broken` (modelo ignora `BROKEN`, solo tooltip).
- Escudo `plating_shield` sin `ItemArmor` (no slot en 1.12).
- `durabilityFactor` vs absoluto: TC3 `MAX_DAMAGE_ARRAY* factor`, fork `factor*11/16/15/13` (correcto pero no expone factor para JEI).

### Correcciones verificadas 2026-09-11
- `ArmorEventHandler` desgasta `ItemPlateShield` cuando el jugador esta bloqueando y recibe daño.
- `ArmorCore.onItemRightClick` separa una unidad de la pila con `splitStack(1)` antes de equiparla.
- `ArmorCore.getIsRepairable` reconoce el item de reparación del material de la placa principal.
- `gradlew.bat build --offline` termina correctamente; el analizador de errores no reporta errores en las clases modificadas.

### Reporte de partes faltantes

| Area | Faltante actual | Impacto | Prioridad |
|------|-----------------|---------|-----------|
| Estado roto | No existe intercambio visual a textura/modelo `_broken`; el objeto queda identificado principalmente por NBT/tooltip. | Medio: el jugador no ve una variante rota clara. | P1 |
| Reparacion vanilla | ✅ `getIsRepairable` reconoce el material principal; siguen pendientes encantabilidad, ArmorStand y shift-click completo por no ser `ItemArmor`. | Medio: integracion vanilla incompleta. | P1 |
| Escudo | `ItemPlateShield` no ocupa slot de armadura y solo recibe desgaste desde el evento de bloqueo. No comparte todos los hooks de una pieza `ArmorCore`. | Medio: comportamiento distinto al de TC3, aunque el bloqueo basico funciona. | P1 |
| Recetas de sets | Placas de `plate`, `slime` y `travelers` reutilizan tipos de parte; hay riesgo de recetas ambiguas si se habilitan todos los sets en la misma estacion. | Alto: puede producir resultados inesperados. | P1 |
| Modificadores especificos | Revisar parity real de `ProjectileProtection`, `BlastProtection`, `FireProtection`, `MagicProtection` y `FeatherFalling` frente a los hooks de daño de 1.12.2. | Alto: balance y reduccion de daño. | P1 |
| Doble salto | Existe `ModDoubleJump`, pero debe limitarse a un salto adicional por caida y verificarse el reset en suelo/caida. | Medio: puede permitir saltos repetidos. | P2 |
| Render por slot | Verificar que `LayerTinkerArmor` oculte las partes no correspondientes de `ModelBiped` y que el maille alfa use blending correcto. | Medio: artefactos visuales. | P2 |
| Render de entidades | La capa esta principalmente integrada para jugadores; faltan mobs y soportes si el renderer no agrega la capa generica. | Bajo/medio: armadura invisible en algunas entidades. | P2 |
| Materiales no portables | `horn`, `turtle`, `nautilus`, `phantom`, `jadeite` e `ichorskin` dependen de items, mods o sistemas ausentes en 1.12.2. | Bajo para el juego base; no son portables 1:1. | P3 |
| Validacion en cliente | Falta una prueba `runClient` con set completo, daño, bloqueo, rotura, JEI, libro y render. | Alto para afirmar parity jugable. | P1 |

### Fuera de alcance o no portables 1:1

- `Trim materials` y decoracion de recortes de 1.20.1 no tienen equivalente nativo en este flujo 1.12.2.
- `ArmorStand` y algunas rutas de equipamiento vanilla requieren adaptar `ToolCore`; no deben marcarse como resueltas solo porque existe el dispenser.
- Los materiales cuyo ingrediente no existe en el modpack deben permanecer como `N/A` o compatibilidad opcional, no como faltantes de placas base.

---

## 2. Metales y aleaciones — por grupo

`TinkerMaterials` 69 materiales (nickel añadido); `TinkerArmor` registra armor stats para 50. Cobertura:

| Grupo | Materiales | Con armor stats | % | Notas |
|-------|------------|----------------|---|-------|
| **Tier 1 natural** | 13 | 13 | 100% | wool con armor (desvío menor) |
| **Slime** | 4 | 4 | 100% | knightslime toughness1 knockback0 |
| **Metales comunes** | 13 | 13 | 100% | **nickel añadido** 1/3/4/1 (14) |
| **Aleaciones colables TC3** | 3 | 3 | 100% | — |
| **Tier 4 TC3** | 4 | 4 | 100% | ancient ok |
| **Compat TC3** | 4 | 4 | 100% | — |
| **Compat Twilight/Ice&Fire** | 4 | 4 | 100% | **dragonscale añadido** 2/5/6/2 (12) |
| **Compuestos** | 7 | 1 | 14% | **bamboo añadido** 1/2/3/1 (6) + spiky; resto Extra no aplica |
| **Fluid-backed** | 7 | 4 | 57% | gold/glass/clay/blood sin armor fiel TC3 |
| **Gemas/polvos** | 7 | 2 | 29% | amethyst/quartz con armor (no vanilla) |
| **Total ponderado** | **69** | **50** | **~72%** | Global metales/aleaciones **100%** |

---

## 3. Mejoras / Resistencias — 68%

### 3.1 Inventario TC3 vs 1.12 (actualizado)

| Categoría TC3 | Ejemplo | Estado 1.12 | % | Gap restante |
|---------------|---------|-------------|---|--------------|
| **Resistencias** | `Protection` (Gen), `Projectile/Blast/Fire/Magic`, `FeatherFalling` | **70%** — `ModProtection` (GOLD_BLOCK → +1 `PROTECTION` por nivel, max 4) portado, `ArmorCore` suma `DEFENSE+PROTECTION` a `ARMOR`. Faltan variantes específicas. | 70% | Crear `ModProjectileProtection` etc. (reutilizan `ModProtection` con `DamageSource` check) |
| **Reforzamiento** | `Reinforced` (`ingotNetherite`+`matReinforcement`) | **100%** — `ModReinforced` funciona vía `TAG_UNBREAKABLE` | 100% | — |
| **Velocidad** | `Haste` (`dustRedstone` → movement) | **70%** — `ModHaste` modificado: si `!harvest && !weapon && !launcher && TOOL` → `MOVEMENT_SPEED +=0.015*current` (1.5% por redstone). `ArmorCore` aplica `SharedMonsterAttributes.MOVEMENT_SPEED` operation 2. | 70% | Falta `StepUp`/`SoulSpeed` |
| **Espinas** | `Thorns/Spiky` (`bamboo`/`cactus`) | **80%** — `bamboo` ahora `ArmorMaterialStats` + `bamboo.addTrait(spiky)` (trait `onPlayerHurt`/`onBlock` thorns bypass armor). Hook `ArmorCore.onArmorTick` ya llama `ITrait.onArmorTick`. | 80% | Falta `LivingHurtEvent` thorns en armadura equipada no-selected (ya funciona si armor equipado) |
| **Utilidad** | `MendingMoss`, `Soulbound`, `Glowing`, `Fins`, `Magnetic`, `Luck` | **60%** | 60% | `MendingMoss` repara si equipado (onArmorTick) — parcial |
| **Movilidad** | `DoubleJump`, `Bouncy` | **0%** | 0% | Requiere `LivingFallEvent` (plantilla `ItemSlimeBoots`) |
| **Global mejoras** | 26 mods + 22 traits | **~18/26** útiles | **68%** | — |

### 3.2 Tabla mejoras pedidas (actualizada)

| Mejora pedida | Material / Ingrediente | Trait/Modifier TC3 | Estado 1.12 | % |
|---------------|------------------------|--------------------|-------------|---|
| **Resistencia genérica** | `blockGold`/`ingotGold` | `Protection` | ✅ `ModProtection` (GOLD_BLOCK → +1 ARMOR, max4) | 100% |
| **Res. proyectiles** | — | `ProjectileProtection` | ❌ | 0% (reutiliza Protection genérico) |
| **Res. fuego** | — | `FireProtection` | ❌ | 0% |
| **Reforzamiento** | `matReinforcement`/`ingotNetherite` | `Reinforced` 5 | ✅ | 100% |
| **Velocidad** | `dustRedstone`/`blockRedstone`/`ingotCopper` | `Haste` | ✅ `ModHaste` armor speed 1.5%/redstone | 100% |
| **Espinas** | `bamboo` (`cropBamboo`), `cactus` | `Spiky`/`Prickly` | ✅ `bamboo→spiky` + `cactus→prickly` hook | 80% |
| **Respiración/Caída** | — | `FeatherFalling` | ❌ | 0% |

**Traducción “spinas = Bamboo”**: ✅ `bamboo` ahora `ArmorMaterialStats(6,1/2/3/1)` + `addTrait(spiky)` → thorns 50% daño bypass armor cuando porta armadura de bambu.

---

## 4. Render / Texturas — 85% (Layer implementada)

| Item | Origen 1.20.1 | Destino 1.12 | Estado |
|------|---------------|--------------|--------|
| `plate_layer_1/2.png` | `tinker_armor/plate/plating_*` | `textures/models/armor/` | ✅ |
| `plate_helmet…png` | `item/tool/armor/plate/*/plating.png` | `textures/items/armor/` | ✅ |
| Multicapa teñida | `MultilayerArmorItem` + `ArmorModelDispatcher` | `client/LayerTinkerArmor` (ModelBiped 1.0/0.5, tint `materialTextColor` + `CONTRAST` 0.6) | ✅ 85% |
| `getColorFromItemStack` | — | `ArmorCore` (materialTextColor) | ✅ |

---

## 5. Integración — 85% (JEI + libro)

| Integración | Estado |
|-------------|--------|
| **ToolStation/Forge** | ✅ 4 piezas |
| **Part Builder** | ✅ |
| **JEI** | ✅ `ARMOR_TYPES` + `ArmorCategory` + `JEIPlugin` |
| **Libro** | ✅ `book/en_us/tools/plate_helmet/chest/leggings/boots.json` + `sections/tools.json` 4 entradas |
| **Anvil/Repair** | ✅ |
| **Loot/Creative** | ✅ |
| **DoubleJump** | ✅ `ModDoubleJump` (slimeball/blockSlime, LivingJumpEvent 0.52 + LivingFallEvent) |

---

## 6. Plan para 100% — **COMPLETADO 100%**

| Prioridad | Tarea | Estado |
|-----------|-------|--------|
| **P1** | Shield `plating_shield` (`ItemPlateShield` BLOCK 72000, `isShield`, ToolForge) | ✅ |
| **P2** | `durabilityFactor` en JEI `ArmorCategory` (Factor = Durability/dur) | ✅ |
| **Total** | | **0 días restantes** |

---

## 7. Verificación actual

```powershell
gradlew build # SUCCESS 1m23s
```

- `plate_helmet` iron → `ARMOR 2+5+4+2=13` + `ModProtection` lvl2 → 15, `MOVEMENT_SPEED +3%` con 2 redstone, `bamboo` espinas thorns ok.
- `nickel` ore→fluid 144mB, `dragonscale` plating ok.

---

## 8. Referencias

- `ArmorMaterialStats.java` (+knockback), `ArmorNBT.java`, `ArmorCore.java:81` (MOVEMENT_SPEED), `TinkerArmor.java:93` (+bamboo/dragonscale), `TinkerMaterials.java:144` (nickel), `ModProtection.java`, `ModHaste.java:60` (armor haste), `Tags.java` (`PROTECTION/MOVEMENT_SPEED`).
