/*
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

package org.anvilpowered.catalyst.common.plugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.TextService;

@Singleton
public final class CatalystPluginInfo<TString, TCommandSource> implements PluginInfo<TString> {
    public static final String id = "catalyst";
    public static final String name = "Catalyst";
    public static final String version = "$modVersion";
    public static final String description = "An essentials plugin for velocity";
    public static final String url = "https://github.com/AnvilPowered/Catalyst";
    public static final String[] authors = {"STG_Allen", "Cableguy20"};
    public static final String organizationName = "AnvilPowered";
    public static final String buildDate = "$buildDate";
    public TString pluginPrefix;

    @Inject
    public void setPluginPrefix(TextService<TString, TCommandSource> textService) {
        pluginPrefix = textService.builder().gold().append("[", name, "] ").build();
    }

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
    public String getUrl() {
        return url;
    }

    @Override
    public String[] getAuthors() {
        return authors;
    }

    @Override
    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public String getBuildDate() {
        return buildDate;
    }

    @Override
    public TString getPrefix() {
        return pluginPrefix;
    }
}