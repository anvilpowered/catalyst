package org.anvilpowered.catalyst.velocity.discord;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.Plugin;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.velocity.plugin.Catalyst;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

@Singleton
public class JDAHook {

    private Registry registry;

    private JDA jda;

    @Inject
    Plugin<?> catalyst;

    @Inject
    private DiscordProxyListener discordProxyListener;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Logger logger;

    @Inject
    public JDAHook(Registry registry) {
        this.registry = registry;
        this.registry.addRegistryLoadedListener(this::registryLoaded);
    }

    public JDA getJDA() {
        return jda;
    }

    public void registryLoaded() {
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            proxyServer.getEventManager().register(catalyst, catalyst.getPrimaryEnvironment().getInjector().getInstance(DiscordProxyListener.class));
            try {
                jda = new JDABuilder(registry.getOrDefault(CatalystKeys.BOT_TOKEN)).build().awaitReady();
                jda.getPresence().setActivity(Activity.playing(registry.getOrDefault(CatalystKeys.NOW_PLAYING_MESSAGE)));
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
            jda.addEventListener(discordProxyListener);
            updateTopic();
        } else {
            logger.warn("The discord module is currently disabled! Chat will not be transmitted to discord.");
        }
    }

    public void updateTopic() {
        proxyServer.getScheduler().buildTask(catalyst, () -> {
            TextChannel c = jda.getTextChannelById(registry.getOrDefault(CatalystKeys.MAIN_CHANNEL));
            String playerCount = "There are currently no players online!";
            if (!(proxyServer.getPlayerCount() == 0)) {
                playerCount = Integer.toString(proxyServer.getPlayerCount());
            }
            c.getManager().setTopic(registry.getOrDefault(CatalystKeys.TOPIC_FORMAT).replace("%players%", playerCount)).queue();
        }).repeat(registry.getOrDefault(CatalystKeys.TOPIC_UPDATE_DELAY).longValue(), TimeUnit.SECONDS).schedule();
    }
}
