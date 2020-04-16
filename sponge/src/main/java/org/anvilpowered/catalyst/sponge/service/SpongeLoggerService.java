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

package org.anvilpowered.catalyst.sponge.service;

import com.google.inject.Inject;
import org.anvilpowered.catalyst.api.service.LoggerService;
import org.slf4j.Logger;
import org.spongepowered.api.text.Text;

public class SpongeLoggerService implements LoggerService<Text> {

    @Inject
    private Logger logger;

    @Override
    public void info(Text message) {
        logger.info(message.toPlain());
    }

    @Override
    public void info(String message) {
        info(Text.of(message));
    }

    @Override
    public void warn(Text message) {
        logger.warn(message.toPlain());
    }

    @Override
    public void warn(String message) {
        warn(Text.of(message));
    }
}
