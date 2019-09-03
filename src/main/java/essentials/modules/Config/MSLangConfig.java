package essentials.modules.Config;

import com.velocitypowered.api.command.CommandSource;
import essentials.MSEssentials;
import net.kyori.text.TextComponent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

//essentials.Config used to control WordCatch
public class MSLangConfig {
    static MSEssentials plugin;
    static List<String> swears;
    static List<String> customset;
    static List<String> exceptions;
    static String message;
    static Path configPath;


    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode config;

    public MSLangConfig(MSEssentials main){
        plugin = main;
    }

    public CommentedConfigurationNode getConfig(){
        return config;
    }

    public static void enable(){
        try
        {
            configPath = Paths.get(MSEssentials.defaultConfigPath + "/languageconfig.json");
            if(!Files.exists(configPath)){
                Files.createDirectories(MSEssentials.defaultConfigPath);
                Files.createFile(configPath);
            }
            loader = HoconConfigurationLoader.builder().setPath(configPath).build();
            config = loader.load();

            config.getNode("swears").setComment("Add swear words to be censored with /language add [word]");
            config.getNode("exception").setComment("Add swear words to be exempted with /language exempt [word]");
            config.getNode("ChatMessage").setValue("I <3 MilspecSG!").setComment("Message to replace messages containing swear words.");

            save();
            load();
        }
        catch
        (IOException e){
            e.printStackTrace();
        }
    }


    public static void save(){
        try{
            loader.save(plugin.getMSLangConfig().getConfig());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void load(){
        try{
            config = loader.load();
            swears = config.getNode("swears").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());

            exceptions  = config.getNode("exception").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
            message = config.getNode("ChatMessage").getValue().toString();

        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static boolean addSwear(String swear, CommandSource src) {
        if (!swears.contains(swear))
        {
            src.sendMessage(TextComponent.of("swears doesnt contain the swear"));
            if (!swears.isEmpty())
            {
                src.sendMessage(TextComponent.of("swears isn't empty"));
                for (String s : swears)
                {
                    if (swear.equalsIgnoreCase(s))
                    {
                        return false;
                    }
                }


                swears.add(swear);

                src.sendMessage(TextComponent.of(swears.toString()));
                swears.replaceAll(s -> s.replaceAll("[\\[\\]]", ""));
                config.getNode("swears").setValue(swears);
                save();
                load();
                return true;
            } else {
                swears.add(swear);
                swears.replaceAll(s -> s.replaceAll("[\\[\\]]", ""));
                src.sendMessage(TextComponent.of(swears.toString()));
                config.getNode("swears").setValue(swears);
                save();
                load();
                return true;
            }


        }
        return false;
    }

    public boolean addExempt(String exempt, CommandSource src){
        if(!exceptions.contains(exempt))
            if(!exceptions.isEmpty()){
                for(String s : exceptions){
                    if(exempt.equalsIgnoreCase(s)){
                        return false;
                    }
                }
                exceptions.add(exempt);
                config.getNode("exception").setValue(exceptions);
                save();
                load();
                return true;
            }else{
                exceptions.add(exempt);
                config.getNode("exception").setValue(exceptions);
                save();
                load();
                return true;
            }
        return false;
    }

    public boolean removeSwear(String swear, CommandSource src){
        if(!swears.isEmpty())
        {
            for (String s : swears){
                if(swear.equalsIgnoreCase(s))
                {
                    swears.remove(swear);
                    config.getNode("swears").setValue(swears);
                    save();
                    load();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeExempt(String e, CommandSource src){
        if(!exceptions.isEmpty()){
            for(String s : exceptions){
                if(e.equalsIgnoreCase(s)){
                    exceptions.remove(e);
                    config.getNode("exception").setValue(exceptions);
                    save();
                    load();
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getSwears(){
        return swears;
    }

    public  List<String> getExceptions() {
        return exceptions;
    }


/* private Path filteredPath;
 private Path exemptPath;
 private GsonConfigurationLoader filterLoader;
 private ConfigurationNode filterNode;
 private GsonConfigurationLoader exemptLoader;
 private ConfigurationNode exemptNode;

 private WatchService _watchService;
 private WatchKey _key;

 private Scheduler.TaskBuilder taskBuilder;


 public ConfigManager(Path configDir){
     try{
         filteredPath = Paths.get(configDir + "/filtered.json");
         exemptPath = Paths.get(configDir + "/exempt.json");

         if(!Files.exists(filteredPath)){
             Files.createFile(filteredPath);
         }else{
             return;
         }

         if(!Files.exists(exemptPath)){
        Files.createFile(exemptPath);
     }

         filterLoader = GsonConfigurationLoader.builder().setPath(filteredPath).build();
         filterNode.getNode("VelocityChatFilter-FilteredWords").setValue("shit");
         filterNode = filterLoader.load();

         exemptLoader = GsonConfigurationLoader.builder().setPath(exemptPath).build();
         exemptNode = exemptLoader.load();


         //Register file change protocol (coming in later release)
         //_watchService = configDir.getFileSystem().newWatchService();
        // _key = configDir.register(_watchService, StandardWatchEventKinds.ENTRY_MODIFY);



 }catch (IOException e){
         e.printStackTrace();
     }

 }

 public boolean addExempt(String word){

     try{
         exemptNode.getNode("VelocityChatFilter-ExemptWords").setValue(word);
         exemptLoader.save(exemptNode);
         return true;
     }catch(IOException e){
         e.printStackTrace();
     }
     return false;
 }


public boolean removeExempt(String word){
     try{
         exemptNode.getNode("VelocityChatFilter-ExemptWords").removeChild(word);
         exemptLoader.save(exemptNode);
         return true;
     }catch (IOException e){
         e.printStackTrace();
     }
     return false;
 }



public boolean addFiltered(String filterWord){
    try{
        filterNode.getNode("VelocityChatFilter-FilteredWords").setValue(filterWord);
        filterLoader.save(filterNode);
        return true;
    }catch(IOException e){
        e.printStackTrace();
    }
    return false;
}

public boolean removeFiltered(String filterWord){
     try{
         filterNode.getNode("VelocityChatFilter").removeChild(filterWord);
         filterLoader.save(filterNode);
         return true;
     }catch(IOException e){
         e.printStackTrace();
     }
     return false;
}

private String getExemptFromFile(String exempt){
     final Object word = exemptNode.getNode("VelocityChatFilter").getValue();

     String name = (String) word;
     return name;
}
private String getFilteredFromFile(String filter){
     final Object word = filterNode.getNode("VelocityChatFilter").getNode();
     String name = (String) word;
     return name;
}

    @Override
    public List<String> getExemptWords() {
        List<String> exemptList = new ArrayList<>();
        for(String word : getFilteredWords()){
            String e = getFilteredFromFile(word);
            exemptList.add(e);
        }
        return exemptList;
    }

    @Override
    public List<String> getFilteredWords() {
     List<String> filteredList = new ArrayList<>();

     for(String word : getFilteredWords()){
         String e = getExemptFromFile(word);
         filteredList.add(e);
     }
        return filteredList;
    }

    public Runnable checkFileUpdate(){
     return new Runnable(){
         @Override
         public void run(){
             try{
                 for(WatchEvent<?> event : _key.pollEvents()){
                     final Path changedFilePath = (Path) event.context();

                     if(changedFilePath.toString().contains("filtered.json")){
                         filterNode = filterLoader.load();
                     }
                     if(changedFilePath.toString().contains("exempt.json")){
                         exemptNode = exemptLoader.load();
                     }
                     _key.reset();
                 }
             }catch(IOException e){
                 e.printStackTrace();
             }
         }
     };
    }*/
}



