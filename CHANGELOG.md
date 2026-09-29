# Changelog

## 2.13.0.x - 2026-09-28 - Refactor SOLID + Enchantability
- Refactor SOLID (7 commits): ~10 god classes eliminadas, ~75 clases cohesivas nuevas, API publica intacta via fachadas (TinkerRegistry, ToolHelper, TinkerSmeltery, TinkerMaterials, TinkerFluids, Config, ToolBuilder, BlockSlimeChannel, eventos). AGENTS.md con reglas obligatorias.
- Limpieza: 10 clases legacy sin uso eliminadas; errores de modelo 125 -> 0.
- Nuevo modificador Enchantability (receta: manzana de oro encantada): hace la herramienta encantable vanilla (mesa + yunque) pero bloquea modificaciones y reemplazo de partes. Toggle Config.enchantability.
- Removidos (duplicado: otro mod del pack los implementa funcional sin bugs; bloqueado/N-A): set **Travelers** (`travelers_*` + `travelers_shield`), set **Slime armor** (`slime_helmet/chestplate/leggings/armor_boots` + `slime_plating_*`), `plate_shield`, `throwing_axe`, `flint_and_brick`. Se mantiene `plate_*` (`TinkerArmor`).

## 2.13.0.209 — 2026-09-14 — Release final
- Build SUCCESS 18 tasks, 33s offline. Jar 5.6 MB.
- Tactical: 11 herramientas restantes (vein/broad/sledge/pickadze/kama/scythe/dagger/sword/cleaver/crossbow/longbow/fishing/javelin/arrow/shuriken/throwing_axe/flint_and_brick/4 staffs/melting_pan/war_pick/battlesign/swasher/minotaur_axe) + templates/crystal/exp_bottle/worktable/scout armor/modifiers/libro/JEI/recetas 100%.
- TC3: foundry scorched Tier4+blazingBlood, melter 550C/576mB, 14 partes armor desambiguadas (tactical/slime/travelers plating), LayerTinkerArmor NPE fix, ArmorStand, enchantability 10.
- Empaquetado release/2.13.0.209 + checksums + RELEASE_NOTES.

## 2.13.0.x previos
- Ver ESTADO_ACTUALIZADO_2026-09-11.md, PORT_PORCENTAJE.md, PLAN_ARREGLO_ESTADO_FINAL.md
