# Armadura de Placas — Mejoras y Pendientes

> Documento vivo. Lista de mejoras posibles y cosas que pueden faltar en el sistema
> de armadura de placas (`tools/armor`). No es una lista de bugs confirmados: cada punto
> indica el estado actual, el hueco y la acción sugerida.

**Última actualización:** 2026-09-29

---

## 0. Estado actual (resumen)

| Área | Implementación |
|---|---|
| Piezas | `plate_helmet`, `plate_chestplate`, `plate_leggings`, `plate_boots` (`TinkerArmor`) |
| Partes | `plating_helmet/chestplate/leggings/boots` + `maille` |
| Stats base por material | `durability`, `defense`, `toughness`, `knockbackResistance` |
| Stats nuevas | `hardness` (Solidez), `environmentalProtection`, `weight` (Peso), `ricochet` (Rebote) |
| Aplicación | `ArmorCore.getAttributeModifiers` (ARMOR, ARMOR_TOUGHNESS, MOVEMENT_SPEED, KNOCKBACK_RESISTANCE) + `ArmorEventHandler` |
| Render vestido | `LayerTinkerArmor` (capa plating + capa maille) con texturas `textures/tinker_armor/plate/*` |
| Tooltip | `ArmorCore.getTooltip` / `getInformation` muestran todas las stats; se quitó el daño/"when in main hand" |
| Modificadores | Protección (gen + melee/proyectil/blast/fuego/magia), Thorns, Strength, Shulking, Revitalizing, KB Resist, Feather Falling, Double Jump + (twin, spilling, wetting, slurping, shattering, slippery, pyroclastic, chip, zooming) |

---

## 1. Estadísticas — mejoras

### 1.1 Valores explícitos por material (en vez de derivados) — ✅ HECHO
La tabla de `TinkerArmor` ahora tiene **4 columnas explícitas** por material
(`hardness`, `environmental`, `weight`, `ricochet`) además de `toughness`/`knockback`:
```
{TinkerMaterials.X, factor, h,c,l,b, toughness, knockback, hardness, environmental, weight, ricochet},
```
`addMatStats(...)` usa el valor explícito si es `> 0`; si no, cae a la fórmula derivada:
```
hardness      = min(0.60, 0.05 + toughness*0.15 + factor*0.004)
environmental = min(0.50, factor*0.010)
weight        = min(0.06, factor*0.0015)  // ×factor por pieza
ricochet      = min(0.35, toughness*0.10)
```
Los valores actuales de la tabla provienen de esas fórmulas (mismo comportamiento),
pero ahora se pueden **editar por material**.

### 1.2 Stats por pieza
- Solo **Weight** varía por pieza (pechera ×1.6 > pantalón ×1.3 > casco ×0.6 > botas ×0.5).
- `hardness`, `environmental` y `ricochet` son **iguales en las 4 piezas**.
- **Acción:** decidir si tiene sentido que, p. ej., el casco dé menos Environmental que la
  pechera (cobertura/área), del mismo modo que el peso.

### 1.3 Aporte del peso a KB/Hardness
- `weightedKb = kb + peso*0.5`, `weightedHardness = hardness + peso*0.4`.
- **Hueco:** con la escala actual (`weightBase ≤ 0.06`) el aporte es <2% — prácticamente invisible.
- **Acción:** o subir la escala del peso (más penalización de velocidad), o usar multiplicadores
  más agresivos para KB/Hardness, o mostrarlo mejor en el tooltip.

### 1.4 Maille
- En 1.20.1 la maille es **statless** (`StatlessMaterialStats.MAILLE`); en el fork da `factor*5`
  durabilidad + armor 1 y la capa contribuye `×0.5` (durabilidad `/4`) en `ArmorNBT.maille()`.
- **Acción:** confirmar si se deja con stats (útil para gameplay) o 100% statless como la referencia.

### 1.5 Balance general
- Multiplicadores de durabilidad `12/18/17/14/20` (+10% respecto a la referencia).
- `+1` de armor para tiers iron+.
- **Acción:** partida de balance tras pruebas con sets reales (iron/steel/manyullyn/netherite).

### 1.6 Display de Knockback
- 1.20.1 multiplica **×10** el KB para mostrarlo ("vanilla multiplies toughness by 10").
- **Acción:** verificar que `formatNumberPercent` del fork no quede inconsistente con el valor real.

---

## 2. Modificadores faltantes (vs 1.20.1)

### ✅ Implementados (2026-09-29)
- **`wings` (ELYTRA)** — habilidad del peto: planeo simplificado (slow-fall) + renderiza la capa
  `maille_wings` (condicionada al tag `Tags.WINGS`).
- **`respiration`** (casco) — respiración submarina (`MobEffects.WATER_BREATHING`).
- **`aqua_affinity`** (casco) — prisa bajo el agua (`MobEffects.HASTE`).
- **`depth_strider`** (botas) — velocidad en agua (`MobEffects.SPEED`).
- **`long_fall`** (botas) — reducción extra de daño por caída (se suma a Feather Falling).

### Aún ausentes en el fork

| Pieza | Modificadores de 1.20.1 ausentes |
|---|---|
| Casco | `item_frame` |
| Pechera | `ambidextrous`, `haste`, `reach`, `sleeves` |
| Pantalón | `leaping`, `luck`, `shield_strap`, `speedy`, `step_up`, `swift_sneak` |
| Botas | `freezing`, `lightspeed`, `soulspeed`, `springy` |
| Generales | `diamond`, `emerald`, `fiery`, `netherite` (hoy solo como modificadores de herramienta), `ricochet` (hoy stat base) |

- Los de estado/atributo (`haste`, `reach`, `speedy`, `step_up`, `swift_sneak`, `luck`) requieren
  `AttributeModifier`/poción.

---

## 3. Render / visual

- **`maille_wings`:** ✅ HECHO — `LayerTinkerArmor` ahora renderiza una capa de alas en el **peto**
  usando `maille_wings_tconstruct_<material>` (fallback `maille_wings.png`) como tercera pasada
  sobre el modelo bípedo, con caché propia (`WINGS_CACHE`). Se muestra cuando el peto tiene maille.
  (Pendiente de decidir disparador fino — planeo / modificador dedicado — si se quiere parity 1.20.1.)
- **Texturas `*_broken`:** `plating_broken` / `maille_broken` no se usan; 1.12.2 usa
  tooltip/NBT para el estado roto (`ToolHelper.isBroken`). → decisión de diseño, no bug.
- **Render en entidades no-jugador:** `ArmorClientProxy` agrega la capa a todo
  `RenderLivingBase`, pero conviene validar zombis/bots con armadura puesta.
- **Glint/trim:** sin soporte de brillo de encantamiento propio ni "trims".

---

## 4. Sistema / integraciones

- **Escudo (`plating_shield`):** ✅ RESUELTO — se quitó el **stat huérfano**: ya no se registra
  `TYPE_SHIELD` en `TinkerArmor.addMatStats` ni se muestra en JEI (`Reference.ARMOR_TYPES` +
  `ArmorCategory.drawStats`). Alinea con la decisión de "duplicado por otro mod". (El stat type
  `TYPE_SHIELD` sigue existiendo como constante, sin uso.)
- **JEI (`plugin/jei/material/ArmorCategory`):** ✅ HECHO — se agregó `TYPE_MAILLE` a
  `Reference.ARMOR_TYPES` y a `ArmorCategory.drawStats` (+ lang `stat.maille.name`), así que la
  categoría muestra Helmet/Chestplate/Leggings/Boots/Shield/**Maille**. Los stat types iteran y
  emiten cabecera en las líneas "Durability".
- **Libro (`Tinkers' book`):** ✅ HECHO — actualizadas las 4 páginas `tools/plate_*.json`
  (mencionan Durability/Defense/Toughness/Knockback + Hardness/Environmental/Weight/Ricochet) y
  agregada la página `tools/maille.json` + entrada en `sections/tools.json`.
- **Data-pack 1.20.1 (`data/tconstruct/tinkering/materials/stats/*.json`):** ✅ HECHO —
  `JsonMaterialLoader.loadDataPackStats()` ahora **aplica** las stats de `plating_helmet/chestplate/
  leggings/boots` + `maille` (durability/armor/toughness/knockback_resistance + opcional
  hardness/environmental_protection/weight/ricochet) y **sobreescribe** la tabla del fork.
  Se invoca al final de `TinkerArmor.registerArmorMaterialStats` para tener prioridad final.
  Verificado en runtime: `Data-pack stats applied: 1 (1.20.1 compat)` (netherite).
- **Enchantability:** el modificador existe; falta confirmar que funciona sobre las 4 piezas de placas.

---

## 5. Bugs / riesgos detectados

1. **Doble conteo de caída** — ✅ HECHO: se quitó `"fall"` de `isEnvironmentalDamage`
   (`ArmorEventHandler`). El daño por caída ahora solo lo reduce Feather Falling.
2. **Ricochet solo flechas:** solo procesa `EntityArrow`; otros proyectiles (fireball, etc.)
   rebotan como "cancelados" sin invertir trayectoria. → ampliar a `EntityThrowable`/proyectiles.
3. **`getAttributeModifiers` vacío para slots que no son el de armadura:** verificar que no
   rompa dispensers / ArmorStand (`ArmorDispenserBehavior`) ni el shift-click.
4. **`ArmorNBT.maille()`** suma `×0.5` a **toughness/KB/hardness/environmental/weight/ricochet**
   y `/4` a durabilidad: confirmar que es la intención (afecta el balance final).

---

## 6. Prioridades sugeridas

| Prioridad | Item |
|---|---|
| ✅ Hecho | Quitar doble conteo de caída (§5.1) |
| ✅ Hecho | Valores explícitos por material para las stats nuevas (§1.1) |
| Media | Modificadores `respiration`/`depth_strider`/`aqua_affinity`/`long_fall` (§2) |
| Media | Decidir escudo (§4) y stats de maille (§1.4) |
| Media | Escala del aporte peso→KB/Hardness (§1.3) |
| Baja | `maille_wings` en render (§3) |
| Baja | Stats por pieza para hardness/environmental/ricochet (§1.2) |
| Baja | Extender `JsonMaterialLoader` para data-packs 1.20.1 (§4) |

---

## Referencias de código

- `src/main/java/slimeknights/tconstruct/tools/armor/TinkerArmor.java` — tabla + registro de partes/modificadores.
- `.../tools/armor/ArmorNBT.java` — NBT de la armadura (defense/toughness/KB + nuevas).
- `.../tools/armor/ArmorEventHandler.java` — protecciones, ricochet, hardness, knockback.
- `.../tools/armor/item/ArmorCore.java` — atributos + tooltip.
- `.../tools/armor/client/LayerTinkerArmor.java` — render vestido.
- `.../library/materials/ArmorMaterialStats.java` — stats + localización.
- `.../plugin/jei/material/ArmorCategory.java` — display JEI de las stats.

---

## 7. Balance / Tuning (final 2026-09-29)

Curvas y decisiones finales del sistema de placas.

### Durabilidad (multiplicadores base)
`helmet 12 · chestplate 18 · leggings 17 · boots 14` sobre el `factor` del material (+10% vs 1.20.1).

### Armor (H/C/L/B)
Valores del JSON 1.20.1 + **+1 en tiers iron+**. Coherente por tier:
`tier1 ~1,3,2,1 · tier2 3,6,5,3 · tier3 3,7,6,3 · tier4 4,9,7,4`.

### Escalado por pieza (cobertura del cuerpo)
| Pieza | Peso x | Hardness/Env/Ricochet x |
|---|---|---|
| Casco | 0.6 | 0.9 |
| Pechera | 1.6 | 1.4 |
| Pantalon | 1.3 | 1.1 |
| Botas | 0.5 | 0.6 |
| Maille | 0.4 | 0.4 |

### Peso
- `weightBase = min(0.08, factor*0.0020)` (explicito por material en la tabla).
- El peso **aporta 50% a Knockback Resistance y 40% a Hardness**, escalado por pieza.
- Tradeoff: mas peso -> mas lento, pero mas KB/Hardness.

### Topes
`hardness/environmental/ricochet <= 0.9`, `knockback <= 1.0`, reduccion de durabilidad por hardness `<= 0.9`.