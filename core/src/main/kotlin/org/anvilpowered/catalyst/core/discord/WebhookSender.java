/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.core.discord;

import net.dv8tion.jda.api.entities.Webhook;

public interface WebhookSender {

  void sendWebhookMessage(String webHook, String player, String message, String channelId,
                          Object source);

  void sendConsoleWebhookMessage(String webHook, String message, String channelId);

  void sendWebhook(Webhook webhook, Webhook webhookUtils);

  Webhook getWebhook(String channelID);
}
