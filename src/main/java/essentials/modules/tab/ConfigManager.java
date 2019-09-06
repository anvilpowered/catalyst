package essentials.modules.tab;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import essentials.MSEssentials;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.velocitypowered.api.proxy.ServerConnection;

public class ConfigManager {
    public static JSONObject config = null;

    private static List<String> disabledServers = new ArrayList<String>();
    private static List<String> customTabs = new ArrayList<String>();

    public static void setupConfig() {
        if (!MSEssentials.defaultConfigPath.toFile().exists())
            MSEssentials.defaultConfigPath.toFile().mkdirs();
        if (!new File(MSEssentials.defaultConfigPath.toString() + "/globaltab.json").exists()) {
            try {
                writeInitialConfig();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        Object configobj = null;
        try {
            configobj = new JSONParser().parse(new FileReader(MSEssentials.defaultConfigPath.toString() + "/globaltab.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        config = (JSONObject) configobj;

        disabledServers = (List<String>) ConfigManager.config.get("disabled_servers");
        customTabs = (List<String>) ConfigManager.config.get("customtabs");
    }

    private static void writeInitialConfig() throws FileNotFoundException {
        JSONObject configfile = new JSONObject();

        configfile.put("header", "&4Welcome, &6%username%&4 on");
        configfile.put("footer", "&7the server !");
        configfile.put("updatedelay", "1");
        configfile.put("player_format", "%prefix% %username%");
        configfile.put("customtabsenabled", true);

        customTabs.clear();
        customTabs.add("&3Your ping : &e%ping%");
        customTabs.add("&3Current server : &e%server%");
        customTabs.add("&3Balance : &e%balance%");
        configfile.put("customtabs", customTabs);

        disabledServers.clear();
        disabledServers.add("thisserverwonthavethetab");
        configfile.put("disabled_servers", disabledServers);

        PrintWriter pw = new PrintWriter(MSEssentials.defaultConfigPath.toString() + "/globaltab.json");
        pw.write(JsonWriter.formatJson(configfile.toJSONString()));

        pw.flush();
        pw.close();
    }

    public static boolean isServerAllowed(Optional<ServerConnection> server) {
        String name = null;
        if (server.isPresent())
            name = server.get().getServerInfo().getName();
        else
            return false;

        if (disabledServers != null)
            return !disabledServers.contains(name);
        else
            return true;
    }

    public static List<String> getCustomTabs() {
        return customTabs;
    }

    public static boolean customTabsEnabled() {
        return (boolean) ConfigManager.config.get("customtabsenabled");
    }
}