package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import rocks.milspecsg.msessentials.MSEssentials;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.events.ProxyStaffChatEvent;
import rocks.milspecsg.msessentials.misc.LuckpermsHook;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;
import rocks.milspecsg.msessentials.modules.chatutils.ChatFilter;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ProxyChatListener {

    @Inject
    public PluginMessages pluginMessages;

    @Inject
    public MemberManager<TextComponent> memberManager;

    @Inject
    public ChatFilter chatFilter;

    @Inject
    private ProxyServer proxyServer;


    @Subscribe
    public void onChat(PlayerChatEvent e) throws ExecutionException, InterruptedException {
        String message = e.getMessage();
        Player player = e.getPlayer();
        CompletableFuture<Boolean> muted = memberManager.getMutedStatus(player.getUsername());

        if (muted.get()) {
            player.sendMessage(pluginMessages.currentlyMuted);
            return;
        }

        if(ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
            ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(player, message, TextComponent.of(message));
            proxyServer.getEventManager().fire(proxyStaffChatEvent).join();
            return;
        }

        List<String> swearList = chatFilter.isswear(message);
        if (swearList != null) {
            if (e.getResult().isAllowed()) {
                if (!player.hasPermission(PluginPermissions.LANGUAGEADMIN)) {
                    for (String swear : swearList) {
                        message = message.replace(swear, "****");
                    }
                    sendMessage(e, checkPlayerName(message));
                } else {
                    sendMessage(e, checkPlayerName(message));
                }
            } else {
                e.setResult(PlayerChatEvent.ChatResult.denied());
            }
        } else {
            if (e.getResult().isAllowed()) {
                sendMessage(e, checkPlayerName(message));
            }
        }
    }

    public static String checkPlayerName(String message) {
        for (Player onlinePlayer : MSEssentials.getServer().getAllPlayers()) {
            if (message.toLowerCase().equalsIgnoreCase(onlinePlayer.getUsername())) {
                message = message.replaceAll(onlinePlayer.getUsername().toLowerCase(), "&b@" + onlinePlayer.getUsername() + "&r");
            }
        }
        return message;
    }

    public String getRank(Player player) {
        return LuckpermsHook.getPrefix(player);
    }

    public String getSuffix(Player player) {
        return LuckpermsHook.getSuffix(player);
    }

    public String getChatColor(Player player) {
        return LuckpermsHook.getChatColor(player);
    }

    public String getNameColor(Player player) {
        return LuckpermsHook.getNameColor(player);
    }

    public void sendMessage(PlayerChatEvent e, String message) throws ExecutionException, InterruptedException {
        //Set the result to denied to broadcast our own message
        e.setResult(PlayerChatEvent.ChatResult.denied());
        Player player = e.getPlayer();
        //Grab all the information we will need from luckperms
        //The only supported permissions plugin to date
        String prefix = getRank(player);
        String chatColor = getChatColor(player);
        String nameColor = getNameColor(player);
        String suffix = getSuffix(player);
        if (chatColor == null) {
            chatColor = "&r";
        }
        Tristate hasColorPermission = player.getPermissionValue(PluginPermissions.CHATCOLOR);

        CompletableFuture<TextComponent> unFormatted = memberManager.formatMessage(prefix, nameColor, player.getUsername(), chatColor + message, suffix, hasColorPermission.asBoolean());
        TextComponent mes = unFormatted.get();

        for (Player p : MSEssentials.server.getAllPlayers()) {
            p.sendMessage(mes);
        }
    }
}
