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

package org.anvilpowered.catalyst.api.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.config.AdvancedServerInfo;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class AdvancedServerInfoService {

    private Registry registry;
    private List<AdvancedServerInfo> advancedServerInfos = new ArrayList<>();
    private Map<String, String> playerServerMap = new HashMap<>();

    @Inject
    public AdvancedServerInfoService(Registry registry) {
        this.registry = registry;
        registry.whenLoaded(this::whenLoaded).register();
    }

    private void whenLoaded() {
        advancedServerInfos = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO);
    }

    public String getPrefix(String virtualHost) {
        for (AdvancedServerInfo advancedServerInfo : advancedServerInfos) {
            if (advancedServerInfo.hostName.equals(virtualHost)) {
                return advancedServerInfo.prefix;
            }
        }
        return "null";
    }

    public void insertPlayer(String userName, String server) {
        playerServerMap.put(userName, server);
    }

    public String getPrefixForPlayer(String userName) {
        return playerServerMap.get(userName);
    }
}
