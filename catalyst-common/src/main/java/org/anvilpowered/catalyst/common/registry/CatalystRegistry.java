/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.registry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.registry.Keys;
import org.anvilpowered.anvil.base.registry.BaseExtendedRegistry;

@Singleton
public class CatalystRegistry extends BaseExtendedRegistry {

    @Inject
    public CatalystRegistry() {
        setDefault(Keys.BASE_SCAN_PACKAGE, "org.anvilpowered.catalyst.common.model");
    }
}
