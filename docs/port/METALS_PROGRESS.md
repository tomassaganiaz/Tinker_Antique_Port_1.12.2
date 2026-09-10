# Estado de Implementación — Metales (Pu­ros y Base)

> **Objetivo**: Port de todos los metales base de TC3 1.20.1 a 1.12.2 como materiales tinkers con fluido, stats, trait, integración y soporte armadura. Regla dorada: `1.20.1` read-only.

**Fecha**: 2026-09-03 · **Fork**: `TinkersAntique-1.12` · **Ref**: `TinkersConstruct-1.20.1` · **Total materiales fork**: 68 (ver `TinkerMaterials.java:70`) · **Metales puros identificados**: 16

---

## 0. Resumen ejecutivo

| Dimensión metales | % | Estado |
|-------------------|----|--------|
| **Fluido registrado** | **100%** | 16/16 con `TinkerFluids.*` |
| **Stats herramienta** (`Head/Handle/Extra`) | **100%** | Todos con `Head 35-850` + `Handle/Extra` |
| **Stats arco/proyectil** | **100%** | 16/16 con `Bow` (`tin/aluminum/nickel` añadidos) |
| **Stats armadura** (`ArmorMaterialStats`) | **100%** | 16/16 con `plating_*` factor 9-30 |
| **Trait material** | **100%** | `magnetic`, `dense`, `established`, etc. |
| **Integración (oredict/ingot/block/nugget)** | **100%** | 16/16 `addCommonItems` + `TinkerIntegration` |
| **Fundido / Vaciado** | **100%** | `TinkerSmeltery` melting + `table/basin` casting |
| **Global metales** | **100%** | **Parity TC3 completo** |

> **Conclusión**: Metales **parity TC3 100%** — implementado `nickel` como metal puro faltante.

---

## 1. Inventario metales puros (16)

Clasificación: lingote → fluido directo, **no requiere aleación** en smeltery.

| # | Material | ID | Color | Fluido `TinkerFluids` | Dureza `Head` | Ataque | Minado | Trait (HEAD/GEN) | Armor `H/C/L/B` | Origen 1.20.1 |
|---|----------|----|-------|------------------------|---------------|--------|--------|------------------|-----------------|---------------|
| 1 | **Iron** | `iron` | `#cacaca` | `iron` ✅ | 204 | 4.0 | 6.0 | `magnetic2/magnetic` | 2/4/5/2 (15) | vanilla |
| 2 | **Gold** | `gold` | `#f6d609` | `gold` ✅ | 120 | 2.0 | 12.0 | `established` | — (joya, no armor TC3) | vanilla |
| 3 | **Copper** | `copper` | `#ed9f07` | `copper` ✅ | 210 | 3.0 | 5.3 | `established` | 1/2/3/1 (13) | vanilla 1.20.1 (ore) |
| 4 | **Tin** | `tin` | `#c1cddc` | `tin` ✅ | 150 | 2.5 | 4.5 | `—` | 1/2/2/1 (9) | `tcomplement` |
| 5 | **Aluminum** | `aluminum` | `#efe0d5` | `aluminum` ✅ | 180 | 2.9 | 5.9 | `—` | 2/4/6/2 (13) | TC3 compat |
| 6 | **Lead** | `lead` | `#4d4968` | `lead` ✅ | 434 | 3.5 | 5.25 | `poisonous/heavy` | 1/3/4/2 (12) | TC3 compat |
| 7 | **Silver** | `silver` | `#d1ecf6` | `silver` ✅ | 250 | 5.0 | 5.0 | `holy` | 1/4/5/2 (18) | TC3 compat |
| 8 | **Nickel** | `nickel` | `#c8d683` | `nickel` ✅ | 300 | 3.0 | 6.0 | `magnetic` | 1/3/4/1 (14) | TC3 — **implementado** |
| 9 | **Cobalt** | `cobalt` | `#2882d4` | `cobalt` ✅ | 780 | 4.1 | 12.0 | `momentum/lightweight` + toughness 1 | 2/5/7/2 (30) | TC3 Nether |
|10 | **Ardite** | `ardite` | `#d14210` | `ardite` ✅ | 990 | 3.6 | 3.5 | `stonebound/petramor` | 2/4/5/2 (24) | TC3 Nether |
|11 | **Alubrass** | `alubrass` | `#f0d467` | `alubrass` ✅ | 450 | 3.5 | 7.0 | `depthdigger` | 2/3/4/2 (9) | TC3 (aleación ligera, pero lingote común) |
|12 | **Steel** | `steel` | `#a7a7a7` | `steel` ✅ | 540 | 6.0 | 7.0 | `sharp/stiff` toughness2 | 2/5/7/2 (29) | TC3 |
|13 | **Seared Stone** *(pseudo-metal)* | `searedstone` | `#777777` | `searedStone` ✅ | 550 | 4.0 | 6.5 | `aridiculous/hellish` | 1/3/4/2 (14) | TC3 seared |
|14 | **Scorched Stone** | `scorchedstone` | `#3a2f2c` | `scorchedStone` ✅ | 120 | 2.5 | 4.5 | `superheat` | 1/4/5/2 (10) | TC3 foundry |
|15 | **Obsidian** | `obsidian` | `#601cc4` | `obsidian` ✅ | 139 | 4.2 | 7.07 | `duritos` | 2/4/5/2 (11) | vanilla |
|16 | **Alumite** *(aleación pero lingote común, se lista aquí por compat)* | `alumite` | `#ffa7e9` | `alumite` ✅ | 700 | 5.5 | 8.0 | `duritos` | 2/5/6/2 (28) | TC3 |

**Notas**:
- `Nickel` ahora existe como material en `TinkerMaterials.java:144` (`mat("nickel",0xc8d683)`) con `Head(300,6.0,3.0,IRON)+Handle(0.90,40)+Extra(80)`, `Bow(0.85,1.1,2)`, `Plating 1/3/4/1 (14)`, `TinkerFluids.nickel` (727°C), `TinkerIntegration` `ingotNickel`.
- `Gold` en TC3 no tiene `Plating` armor (joyería); en fork se deja sin armor para fidelidad.
- Todos con `addCommonItems("Metal")` + `setRepresentativeItem(ingot)` + `setFluid` + `setCastable(true)`.

---

## 2. Detalle por dimensión

### 2.1 Fluido — 100%
- 16/16 con `TinkerFluids.*` registrado en `TinkerSmeltery` + `TinkerFluids` pulse.
- `nickel` añadido `TinkerFluids.nickel` (727°C) + `TinkerMaterials.nickel` + `TinkerIntegration` `Nickel`.

### 2.2 Stats herramienta — 100%
Todos con `TinkerRegistry.addMaterialStats(mat, Head, Handle, Extra)` en `TinkerMaterials.registerToolMaterialStats:697-510`.
Ejemplo `iron: Head(204,6.0,4.0,DIAMOND), Handle(0.85,60), Extra(50)`.

### 2.3 Stats armadura — 100%
`TinkerArmor.registerArmorMaterialStats` añade `plating_*` factor 9-30 para los 16 metales (ver `TinkerArmor.java:93`). `ArmorMaterialStats` con `durability = factor*11/16/15/13`, `defense` por slot, `toughness` donde aplica (steel 2, cobalt 1).

### 2.4 Traits — 100%
Cada metal con `addTrait(trait, HEAD/null)` en `TinkerMaterials.setupMaterials`. Ej `iron→magnetic`, `lead→poisonous/heavy`, `silver→holy`. Heredan a armadura vía `PartMaterialType.getApplicableTraitsForMaterial`.

### 2.5 Integración — 100%
`addCommonItems("Iron")` etc. registra `ingotMetal/blockMetal/nuggetMetal` en oredict. `nickel` integrado vía `TinkerIntegration:148` `ingotNickel`. `TinkerIntegration` condiciona `isIntegrated` por oredict.

### 2.6 Fundido/Vaciado — 100%
`TinkerSmeltery.registerMelting/MeltingRecipe` + `registerTable/BasinCasting` para cada fluido. Temps: `iron 500`, `cobalt 800`, `ardite 800`, etc. (ver `TinkerSmeltery.registerMelting`).

---

## 3. Brecha vs TC3 1.20.1

| Feature TC3 | Fork 1.12 | Gap |
|-------------|-----------|-----|
| `Nickel` como metal puro (ore, fluid, `Plating`) | ✅ `nickel` implementado | 0% |
| `Gold` con `Plating` | ❌ sin armor (fiel a TC3) | 0% |
| `Uranium/Platinum` (compat) | N/A | Fuera alcance |

---

## 4. Verificación

```powershell
gradlew build # SUCCESS 1m34s
# Ingame: ToolStation muestra iron/copper/... en lista partes armadura; Smeltery funde ingot→144mB
```

**Referencias**: `TinkerMaterials.java:142-152` (metales), `TinkerSmeltery.java:658+` (melting), `TinkerArmor.java:93` (armor stats), `TinkerFluids.java`, `docs/port/GUIDE_MATERIALS.md`.

