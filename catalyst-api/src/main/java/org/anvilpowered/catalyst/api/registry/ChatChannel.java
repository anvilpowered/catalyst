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

package org.anvilpowered.catalyst.api.registry;

import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ChatChannel {

    @Setting("id")
    public String id;

    @Setting("format")
    public String format;

    @Setting(value = "hoverMessage")
    public String hoverMessage;

    @Setting(value = "click")
    public String click;

    @Setting("servers")
    public List<String> servers;

    @Setting(value = "alwaysVisible")
    public boolean alwaysVisible;

    @Setting(value = "passthrough")
    public boolean passthrough;

    @Setting(value = "discordChannel")
    public String discordChannel;
}
