/*
 *     MSEssentials - MilSpecSG
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rocks.milspecsg.msessentials;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import rocks.milspecsg.msrepository.PluginInfo;

import javax.inject.Singleton;

@Singleton
public final class MSEssentialsPluginInfo implements PluginInfo<TextComponent> {
    public static final String id = "msessentials";
    public static final String name = "MSEssentials";
    public static final String version = "$modVersion";
    public static final String description = "An essentials plugin for velocity";
    public static final String url = "https://github.com/MilSpecSG/MSDataSync";
    public static final String authors = "STG_Allen";
    public static final TextComponent pluginPrefix = TextComponent.of("[MSEssentials] ").color(TextColor.GOLD);

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getAuthors() {
        return authors;
    }

    @Override
    public TextComponent getPrefix() {
        return pluginPrefix;
    }
}