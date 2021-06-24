/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.api.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.registry.AdvancedServerInfo;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;

@Singleton
public class AdvancedServerInfoService {

  private final Registry registry;
  private List<AdvancedServerInfo> advancedServerInfos = new ArrayList<>();
  private final Map<String, String> playerServerMap = new HashMap<>();

  @Inject
  public AdvancedServerInfoService(Registry registry) {
    this.registry = registry;
    registry.whenLoaded(this::whenLoaded).register();
  }

  private void whenLoaded() {
    advancedServerInfos = registry.getOrDefault(CatalystKeys.INSTANCE.getADVANCED_SERVER_INFO());
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
