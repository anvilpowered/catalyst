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
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.service.StaffListService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class CommonStaffListService<TString, TPlayer, TCommandSource> implements StaffListService<TString> {

    public List<TString> staffNames = new ArrayList<>();
    public List<TString> adminNames = new ArrayList<>();
    public List<TString> ownerNames = new ArrayList<>();
    final private Registry registry;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private PermissionService permissionService;

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

    @Inject
    public CommonStaffListService(Registry registry) {
        this.registry = registry;
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(this::update, 10, TimeUnit.MINUTES);
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

    private Runnable update() {
        return () ->
            CompletableFuture.runAsync(() -> {
                for (TPlayer player : userService.getOnlinePlayers()) {
                    String userName = userService.getUserName(player);
                    getStaffNames(
                        userName,
                        permissionService.hasPermission(
                            player,
                            registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN_PERMISSION)
                        ),
                        permissionService.hasPermission(
                            player,
                            registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF_PERMISSION)
                        ),
                        permissionService.hasPermission(
                            player,
                            registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER_PERMISSION)
                        ));
                }
            });
    }

    @Override
    public void removeStaffNames(String player) {
        staffNames.remove(textService.of(player));
        adminNames.remove(textService.of(player));
        ownerNames.remove(textService.of(player));
    }
}
