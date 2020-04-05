/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020 STG_Allen
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
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.service.StaffListService;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class CommonStaffListService<TPlayer extends TCommandSource, TString, TCommandSource> implements StaffListService<TString> {

    @Inject
    private TextService<TString, TCommandSource> textService;

    public List<TString> staffNames = new ArrayList<>();
    public List<TString> adminNames = new ArrayList<>();
    public List<TString> ownerNames = new ArrayList<>();

    @Override
    public List<TString> staffNames() {
        return staffNames;
    }

    @Override
    public List<TString> adminNames() {
        return adminNames;
    }

    @Override
    public List<TString> ownerNames() {
        return ownerNames;
    }


    @Override
    public void getStaffNames(String player, boolean adminPermission, boolean staffPermission, boolean ownerPermission) {
        if (ownerPermission) {
            ownerNames.add(textService.of(player));
        } else if (staffPermission) {
            if (adminPermission) {
                adminNames.add(textService.of(player));
            } else {
                staffNames.add(textService.of(player));
            }
        }
    }

    @Override
    public void removeStaffNames(String player) {
        staffNames.remove(textService.of(player));
        adminNames.remove(textService.of(player));
        ownerNames.remove(textService.of(player));
    }
}
