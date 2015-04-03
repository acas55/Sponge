package org.spongepowered.common.plugin;

import org.slf4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;

public interface SpongePluginContainer extends PluginContainer {

    Logger getLogger();

}
