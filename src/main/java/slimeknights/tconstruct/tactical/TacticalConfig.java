package slimeknights.tconstruct.tactical;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.File;

public final class TacticalConfig {
    public static Configuration cfg;
    public static double nunchakuSpeed = 2.1;
    public static double spearDamage = 0.85;
    public static double doppelDamage = 1.1;
    public static double swiftShieldReduction = 0.6;
    public static double heavyShieldReduction = 0.8;
    public static boolean enableTactical = true;

    public static void init(File f){
        cfg = new Configuration(f);
        try{
            cfg.load();
            enableTactical = cfg.getBoolean("enableTactical", "general", true, "Activa herramientas tacticas");
            nunchakuSpeed = cfg.getFloat("nunchakuSpeed", "balance", 2.1f, 0.5f, 4.0f, "Vel. ataque nunchaku");
            spearDamage = cfg.getFloat("spearDamage", "balance", 0.85f, 0.2f, 2.0f, "Coef. daño lanza");
            doppelDamage = cfg.getFloat("doppelDamage", "balance", 1.1f, 0.5f, 2.5f, "Coef. daño doppelhander");
            swiftShieldReduction = cfg.getFloat("swiftReduction", "balance", 0.6f, 0.1f, 1.0f, "Reduccion escudo ligero");
            heavyShieldReduction = cfg.getFloat("heavyReduction", "balance", 0.8f, 0.1f, 1.0f, "Reduccion escudo pesado");
        } finally { if(cfg.hasChanged()) cfg.save(); }
    }
}
