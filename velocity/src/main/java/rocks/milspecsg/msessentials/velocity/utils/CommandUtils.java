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

package rocks.milspecsg.msessentials.velocity.utils;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import rocks.milspecsg.anvil.api.plugin.PluginInfo;

import javax.inject.Inject;

public class CommandUtils {

    @Inject
    private PluginInfo<TextComponent> pluginInfo;


    public void createPluginInfoPage(final CommandSource source, final boolean hasPermissionForCommand)
    {
        source.sendMessage(
                TextComponent.builder()
                        .color(TextColor.GOLD)
                        .append(pluginInfo.getPrefix())
                        .append(TextComponent.of("Running Version "))
                        .color(TextColor.GREEN)
                        .append(pluginInfo.getVersion())
                        .color(TextColor.AQUA)
                        .append(TextComponent.of("\n") + "[ Plugin Page ]")
                        //.hoverEvent(HoverEvent.showText(TextComponent.of(pluginInfo.getURL())))
                        //.clickEvent(ClickEvent.openUrl(pluginInfo.getURL()))
                        .append(TextComponent.of("\n") + "[ MilspecSG ]")
                        .hoverEvent(HoverEvent.showText(TextComponent.of("https://www.milspecsg.rocks/")))
                        .clickEvent(ClickEvent.openUrl("https://ww.milspecsg.rocks/"))
                        .append(TextComponent.of("\n"))
                        .build()
        );
    }
}
