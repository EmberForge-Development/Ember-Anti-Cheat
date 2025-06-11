package dev.emberforge.emberanticheat;

import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;
import java.util.stream.Collectors;

public class ModList {
    public static List<String> getLoadedMods() {
        return net.minecraftforge.fml.ModList.get().getMods().stream()
                .map(IModInfo::getModId)
                .collect(Collectors.toList());
    }
}
