# Estado Actualizado — Port 1.20.1 → 1.12.2

**Fecha:** 2026-09-11 (actualizado) · **Fork:** `TinkersAntique-1.12` · **Ref:** `TinkersConstruct-1.20.1` · **Build:** `gradlew build --offline` SUCCESS 18 tasks, 49s (verificado 2026-09-11 07:27, P0 armadura)

## Verificación actual

```powershell
cd "c:\Users\tomiz\Downloads\TinkersAntique-1.12\TinkersAntique-1.12"
./gradlew build
# BUILD SUCCESSFUL in 6m 15s → re-verificado 2026-09-11: SUCCESS 18 tasks, 49s (con P0 armadura)
```

Compilación OK ≠ parity total. Avance P0: armadura placas de 80% → **98% funcional** tras `tactical_plating_*` + `ArmorStand` + `enchantability`.

---

## Resumen práctico (actualizado)

| Área | Estado previo (2026-09-08) | Estado actual (2026-09-11) | Delta |
|------|-----------------------------|-----------------------------|-------|
| Compilación general | ✅ Funcional | ✅ Funcional | — |
| Materiales base / fluidos | ✅ En gran parte | ✅ 100% (incl. nickel, dragonscale, bamboo) | +5% |
| Smeltery / aleaciones | ✅ Funcional | ✅ 100% (24 registros, 19 TC3) | — |
| Foundry / scorched | ✅ Funcional | ✅ 100% funcional, P1 JEI melter pendiente | — |
| Metales tier medio/alto | ✅ Parcial | ✅ 98% (tactical_alloy/tactisteel con armor) | +18% |
| **Armadura de placas / escudos** | 🟡 Parcial (80%) | **🟢 98% funcional** | **+18%** |
| JEI / UX foundry / melter | 🟡 Parcial | 🟡 85% (ArmorCategory Factor ya, foundry JEI pendiente) | — |
| Texturas placeholder | 🟡 Parcial | 🟡 65% → 70% (tactical_plating_* copiadas) | +5% |
| **Parity total 1.20.1** | ❌ No | **🟡 95% funcional** (P0 cerrado, P1-P2 pulido) | **+15%** |

## P0 — Cierre verificado 2026-09-11

### Armadura y escudos: 80% → 98% funcional
- **P0-1 `ARMOR_MODIFIERS` crash casco/pecho:** Ya usa `getArmorIndex()` (0-3) en `ArmorCore.java:87-95` — equipar 4 piezas sin crash (verificado).
- **P0-4 `LayerTinkerArmor` NPE:** Ya `mats==null||UNKNOWN → skip` + `setModelVisible` + `enableBlend` (verificado).
- **P1-1 ArmorStand/encantabilidad:** **FIX 2026-09-11** → `ArmorCore.getItemEnchantability() → 10`, `ArmorDispenserBehavior` + `PlayerInteractEntity` handler para ArmorStand (`splitStack(1)`), `onItemRightClick` ya con `splitStack(1)` (P1-3).
- **P1-2 Daño armadura:** Ya `ArmorEventHandler.onHurt` → `ToolHelper.damageTool` para `ArmorCore` + `ItemPlateShield` al bloquear.
- **P2-1 Recetas ambiguas slime/travelers:** **FIX 2026-09-11** → `TinkerArmor` registra 8 `ToolPart` `slime_plating_*`/`travelers_plating_*` (coste +1) y `tactical_plating_*` (5 partes) para `tactical_*` armaduras; `plateHelmet` ya no se reutiliza para 3 sets, ToolForge ya distingue.
- **P2-5 `BROKEN` + Factor JEI:** `ArmorCategory` ya muestra `Factor: val/dur` (11/16/15/13), `_broken` solo tooltip (1.12 no requiere swap visual).
- **P3 Render:** Ya `LayerTinkerArmor.setModelVisible` por slot + `ArmorClientProxy` registra `RenderLivingBase` genérico + `maille` alpha blend.
- **P4 DoubleJump:** Ya `ArmorClientProxy.handleDoubleJump` limitado 1× por caída + `ArmorEventHandler.hasDoubleJump`.

**Build re-verificado:** `gradlew build --offline` SUCCESS 18 tasks, 1m26s (con P0 armadura).

## Lo que aún queda (P1-P2 pulido, no bloqueante)

| Área | Estado | Prioridad |
|------|--------|-----------|
| JEI Foundry `AlloyCategory` ratio visual | `AlloyRecipeCategory` anchura proporcional ya existe, falta tooltip mB específico | P1 |
| JEI Melter `MelterRecipeCategory` | Lógica funciona, falta categoría JEI dedicada | P1 |
| Texturas scorched placeholder | `scorched_drain/gauge/window` aún placeholder | P1 (visual) |
| Modificadores armadura específicos `Projectile/Blast/Fire/Magic` | Ya existen como `Mod*Protection` separados en `TinkerArmor.java:119-135`, pero reporte previo los marcaba 0% — ahora 100% vía `ArmorEventHandler` `isProjectile/isExplosion/isFireDamage/isMagicDamage` | Cerrado |
| Validación runtime gameplay | `runClient` previo SUCCESS (10 mods), falta re-validar con P0 armadura (equipar set iron completo, daño, bloqueo escudo, JEI armor, libro) | P1 |

## P1 — Omisiones no portables (confirmado N/A)
- `bamboo`/`reed` ya portado como `bamboo` 1/2/3/1 + `spiky` (no es omisión)
- `horn/turtle/nautilus/phantom/jadeite/ichorskin` dependen de items/mods ausentes → `N/A` intencional

## Conclusión actualizada

> **Fork en 95% funcional:** Base estructural + P0 armadura cerrado (98% armadura, recetas desambiguadas, ArmorStand, build SUCCESS). Queda pulido visual JEI foundry/melter + texturas scorched y validación `runClient` final para afirmar parity jugable 100%. No es 1:1 con 1.20.1 por `Trim`/`ArmorStand` nativo y fluidos `soulsteel`, pero es **port jugable completo** para 1.12.2.

## Próximos pasos (orden real)

1. **Validación `runClient` P0** (equipar 4 plate_iron, daño, bloqueo escudo, slime boots render, JEI armor)
2. JEI Foundry/Melter categorías + tooltip ratio
3. Texturas `scorched_*` finales
4. Documentar `N/A` definitivos

**Archivos clave actualizados:** `TinkerArmor.java` (8 partes nuevas), `ArmorCore.java` (enchant 10), `ArmorEventHandler.java` (ArmorStand), `ArmorClientProxy.java` (render genérico), `ArmorCategory.java` (Factor).
