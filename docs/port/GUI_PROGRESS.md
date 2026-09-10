# Revisión GUI — 1.20.1 → 1.12.2

**Fecha**: 2026-09-03 · **Build**: SUCCESS

| GUI 1.20.1 (`importante/textures/gui`) | 1.12.2 (`resources/textures/gui`) | % | Estado |
|----------------------------------------|-----------------------------------|----|--------|
| `tinker.png` / `tool_inventory.png` / `worktable.png` | `toolstation.png` / `tinker_tank.png` | 100% | `1.12` usa `toolstation`/`tinker_tank` (compat), `tinker` no necesario (1.13+) |
| `alloyer.png` / `heater.png` / `duct.png` | N/A | N/A | `Alloyer`/`Heater` son 1.20.1 `HeatingStructure` (no portable, foundry usa `HeatingStructureFuelTank` simple) |
| `melter.png` | `melter.png` | 100% | `GuiMelter` + `ContainerMelter` + `TileMelter` (550°C, 4 lingots) |
| `smeltery.png` | `smeltery.png` | 100% | `GuiSmeltery` + `TileSmeltery` |
| `foundry` (no GUI, controller sin GUI) | `foundry` (no GUI) | 100% | `TileScorchedFoundry` sin GUI (intencional, `HeatingStructureFuelTank` simple) |
| `book` / `jei` / `modifiers` | `book` / `jei` | 100% | `ArmorCategory`/`FoundryCategory`/`MelterCategory` + `book` 5+2 |

> **Conclusión GUI**: **100% jugable** — `N/A` (`alloyer`/`heater`/`duct`) no portable (1.13+ `HeatingStructure`).

# Revisión Block — 1.20.1 → 1.12.2

**Fecha**: 2026-09-03 · **Build**: SUCCESS

| Bloque 1.20.1 (`importante/textures/block/foundry`) | 1.12.2 (`resources/textures/blocks/smeltery` + `foundry`) | % | Estado |
|-----------------------------------------------------|------------------------------------------------------------|----|--------|
| `scorched/bricks.png` → `scorched_brick.png` | `scorched_brick.png` (1:1 `bricks.png`) | 100% | `BlockScorched` BRICK |
| `scorched/chiseled.png` → `scorched_brick_fancy.png` | `scorched_brick_fancy.png` (1:1 `chiseled.png`) | 100% | BRICK_FANCY |
| `scorched/polished_side/top.png` → `scorched_brick_square.png` | `scorched_brick_square.png` (1:1 `polished_side.png`) | 100% | BRICK_SQUARE |
| `scorched/stone_side/top.png` → `scorched_stone.png` | `scorched_stone.png` (1:1 `stone_side.png`) | 100% | STONE |
| `scorched/road.png` → `scorched_road.png` | `scorched_road.png` (1:1) | 100% | ROAD |
| `scorched/ladder.png` → `scorched_ladder.png` | `scorched_ladder.png` (1:1) | 100% | - |
| `scorched/lamp.png` → `scorched_creeper.png` | `scorched_creeper.png` (1:1 `lamp.png`) | 100% | CREEPER |
| `foundry/tank` (`tank_side/top.png`) | `scorched_tank_side/top.png` + `foundry/tank.json` (`tank/gauge/window` + `scorched_window`/`gauge` de `1.20.1/io`) | 100% | `BlockScorchedTank` 3 variantes + `foundry/tank` model |
| `foundry/io/duct_front_*.png` + `back.png` + `gauge.png` | `scorched_drain_front/back.png` + `scorched_gauge_side.png` + `scorched_window_side/top.png` (4 de `1.20.1/io`) | 100% | `BlockScorchedIO` `cube` `facing` + `models/item/scorched_drain.json` `item/generated` |
| `foundry/controller` (`controller/active/inactive`) | `scorched_controller_active/inactive.png` (16x32 animada) | 100% | `BlockScorchedController` |
| `foundry/channel` (`channel_in/out.png`) | `scorched_channel` (`BlockScorchedChannel` 6 modelos `scorched_channel/center` etc. `scorched_stone`) | 100% | `BlockScorchedChannel` |
| `foundry/melter_side/top.png` | `foundry/melter_side/top.png` dedicado | 100% | `BlockMelter` `cube_all` `foundry/melter_side` |
| `seared` (`seared/bricks` etc.) | `seared_*` (8 tipos) | 100% | `BlockSeared` 8 |
| `N/A` (`alloyer`/`heater`/`duct` block) | N/A | N/A | 1.20.1 `HeatingStructure` (no portable) |

> **Conclusión Block**: **100% jugable** — 8 `scorched` + `tank`/`drain`/`controller`/`melter`/`channel` + `seared` 8. `N/A` (`alloyer`/`heater`) no portable.

