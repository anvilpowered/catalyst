package rocks.milspecsg.msessentials;

import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msrepository.BasicPluginInfo;
import rocks.milspecsg.msrepository.BindingExtensions;
import rocks.milspecsg.msrepository.CommonBindingExtensions;
import rocks.milspecsg.msrepository.PluginInfo;
import rocks.milspecsg.msrepository.api.CurrentServerService;
import rocks.milspecsg.msrepository.api.UserService;
import rocks.milspecsg.msrepository.api.tools.resultbuilder.StringResult;
import rocks.milspecsg.msrepository.service.velocity.VelocityCurrentServerService;
import rocks.milspecsg.msrepository.service.velocity.VelocityStringResult;
import rocks.milspecsg.msrepository.service.velocity.VelocityUserService;

@SuppressWarnings({"unchecked", "UnstableApiUsage"})
public class VelocityModule extends CommonModule<
        Player,
        TextComponent,
        CommandSource> {
    @Override
    protected void configure() {
        super.configure();

        BindingExtensions be = new CommonBindingExtensions(binder());

        bind(new TypeLiteral<UserService<Player>>() {
        }).to(VelocityUserService.class);

        bind(BasicPluginInfo.class).to(MSEssentialsPluginInfo.class);

        bind(new TypeLiteral<PluginInfo<TextComponent>>(){
        }).to(MSEssentialsPluginInfo.class);

        bind(new TypeLiteral<StringResult<TextComponent, CommandSource>>(){
        }).to(new TypeLiteral<VelocityStringResult>() {
        });

        bind(CurrentServerService.class).to(VelocityCurrentServerService.class);
    }
}
