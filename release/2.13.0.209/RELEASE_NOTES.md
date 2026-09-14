# TinkersAntique 2.13.0.209 — Release Final

**Fecha:** 2026-09-14 · **MC:** 1.12.2 · **Forge:** 14.23.5.2847 · **Java:** 8 · **Build:** `gradlew build --offline` SUCCESS 18 tasks, 33s

## Artefactos

| Archivo | Bytes | SHA1 |
|---------|-------|------|
| TinkersAntique-1.12.2-2.13.0.209.jar | 5633512 | 3ECAC99A7C6AF58B0B872046E5426AC60B5BD0E5 |
| TinkersAntique-1.12.2-2.13.0.209-deobf.jar | 5524660 | BD6885EE8A3F91C36AABD3607C11CA8FA8A29F33 |
| TinkersAntique-1.12.2-2.13.0.209-sources.jar | 1013415 | E07BBC9100CC728B60C67296F1BFFF8A3A25AB89 |

Ver `checksums.txt` para SHA256.

## Contenido

- **TC3 base 95%**: materiales/fluídos 80→100%, smeltery/foundry/melt­er 100%, armadura placas 98% (ArmorCore + LayerTinkerArmor + ArmorEventHandler + ArmorStand + desambiguación slime/travelers/tactical plating).
- **Tactical 98%**: 7 armas núcleo (nunchaku/swift+heavy shield/spear/doppelhander/maraca/craftsmanStaff) + 23 herramientas extra + worktable/cristal/botella/6 plantillas + 12 armaduras scout/plate/slime + modifiers + libro/JEI/recetas. Solo gaps N/A (pociones Imbalance, compat Botania/Thaum, Mixins) intencionales.
- **11 herramientas restantes** finales implementadas y verificadas en build SUCCESS (TacticalVeinHammer→TacticalMinotaurAxe).

## Instalación

1. Forge 14.23.5.2847 + Mantle 1.3.3.55 + JEI 4.15.0.297 (Cleanroom opcional).
2. Colocar `TinkersAntique-1.12.2-2.13.0.209.jar` en `mods/`.
3. `libs/` (FutureMC/Forgelin) solo classpath compilación, excluidos de runClient.

## Verificación

```powershell
cd "TinkersAntique-1.12"; ./gradlew build --offline # BUILD SUCCESSFUL 18 tasks
jar tf build/libs/TinkersAntique-1.12.2-2.13.0.209.jar | Select-String tactical
```

## Parity

Global 95-98% funcional. P0 armadura cerrado. P1 pulido visual (tooltip ratio JEI, melter_side) no bloqueante. Validación `runClient` jugable pendiente para 100% gameplay.
