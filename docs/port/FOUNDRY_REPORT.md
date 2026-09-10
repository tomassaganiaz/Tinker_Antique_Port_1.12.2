# Informe Forja Mejorada (Scorched Foundry) — Revisión 2026-09-04

Basado en capturas JEI + WAILA + bloque en mundo. `gradlew build` SUCCESS previo.

## Resumen
| # | Componente | Estado | Captura | Causa |
|---|------------|--------|---------|-------|
| F-01 | `scorched_glass` 3 variantes | **ROTO** | `tile.tconstruct.scorched_glass.*.name` en tooltip | Lang faltante en `en_us`/`es_es` — bloque existe con 3 `GlassType` pero sin traducción |
| F-02 | `scorched_channel` | **ROTO leve** | No visible en capturas pero JEI lo referencia | Sin `tile.tconstruct.scorched_channel.*` — fallback a raw key |
| F-03 | `scorched_tank` model | **OK** | WAILA `Scorched Tank / Empty` + bloque 3D con hueco | Variante `tank` usa `foundry/tank` + knob submodel. Hueco interior es cavidad del tanque vacío (correcto). Texturas `scorched_tank_side/top` + `gauge/window` existen. No bug. |
| F-04 | `block_scorched` 8 tipos | **OK** | `Scorched Cobblestone/Bricks` JEI correctos | Lang + textures `scorched_*` completos (8/8) |
| F-05 | `scorched_drain` | **OK** | `Scorched Drain` JEI correcto | Lang + blockstate `cube` + frente/atrás correctos |
| F-06 | Texturas `foundry/glass` + `connected/foundry` | **OK** | No missingTexture en logs (0) | 3×16 variantes `glass/soul_glass/tinted_glass` + `d.png` existen |

## Detalle F-01
`BlockScorchedGlass extends BlockEnumSmeltery<GlassType>` con `GLASS, SOUL_GLASS, TINTED_GLASS`. Blockstate `scorched_glass.json` define `type: glass/soul_glass/tinted_glass` + CTM `mantle:connected_*`. Sin lang, JEI/WAILA muestra `tile.tconstruct.scorched_glass.glass.name` etc.

**Fix:** Añadir en `lang/en_us` + `es_es`:
```
tile.tconstruct.scorched_glass.glass.name=Scorched Glass / Vidrio requemado
tile.tconstruct.scorched_glass.soul_glass.name=Soul Scorched Glass / Vidrio de almas requemado
tile.tconstruct.scorched_glass.tinted_glass.name=Tinted Scorched Glass / Vidrio tintado requemado
tile.tconstruct.scorched_glass.tooltip=Part of the Foundry...
tile.tconstruct.scorched_channel.name=Scorched Channel / Canal requemado
```

## Verificación
```powershell
gradlew build --offline   # SUCCESS 18 tasks
runClient: JEI Foundry muestra Scorched Glass x3 con nombre, WAILA sin raw key, tanque 3 variantes (tank/gauge/window) sin missingTexture
```

## No tocado (fuera de alcance)
- `wings` no es item (solo capa `tinker_armor/slime/wings_*`); no JEI.
- `fishing_hook` es anzuelo, no armadura.
