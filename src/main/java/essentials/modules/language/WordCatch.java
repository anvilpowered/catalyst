package essentials.modules.language;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import essentials.MSEssentials;

import java.util.ArrayList;
import java.util.List;

public class WordCatch {
    ProxyServer proxyServer;


    public static MSEssentials plugin;

    public WordCatch(MSEssentials main, ProxyServer server){
        plugin = main;
        proxyServer = server;
    }

    public static String getFinalArg(final String[] args, final int start){
        final StringBuilder bldr = new StringBuilder();

        for(int i = start; i< args.length; i++){
            if(i != start){
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }

    public List<String> aggressiveMode(String swear){

        String message = swear.toLowerCase();

        List<String> finalwords = new ArrayList<>();

        String mess = message.replace("*", " ");
        String mes3 = mess.replace("()", "o");
        String mes2 = mes3.replace("(", " ");
        String mes = mes2.replace(")", " ");
        String me1 = mes.replace("/", " ");
        String me2 = me1.replace(".", " ");
        String me3 = me2.replace(",", " ");
        String me4 = me3.replace("4", "a");
        String me5 = me4.replace(";", " ");
        String me6 = me5.replace("'", " ");
        String me7 = me6.replace("#", " ");
        String me8 = me7.replace("~", " ");
        String me9 = me8.replace("^", " ");
        String me10 = me9.replace("-", " ");
        String me11 = me10.replace("+", " ");
        String me12 = me11.replace("1", "i");
        String me13 = me12.replace("0", "o");
        String me14 = me13.replace("$", "s");
        String messageo = me14.replace("@", "o");
        String messagea = me14.replace("@", "a");

        String removespaceso = messageo.replaceAll(" ", "");
        String removespacesa = messagea.replaceAll(" ", "");

        String finalchecko = removeDups(removespaceso);
        String finalchecka = removeDups(removespacesa);

        finalwords.add(finalchecko);
        finalwords.add(finalchecka);
        finalwords.add(message);
        return finalwords;

    }

    public String removeDups(String s){
        if(s.length() <= 1) return s;
        if(s.substring(1, 2).equalsIgnoreCase(s.substring(0,1)))return removeDups(s.substring(1));
        else return s.substring(0,1) + removeDups(s.substring(1));
    }

    public List<String> checkforforbidden(List<String> finalwords){
        List<String> forbiddenlist = plugin.getMSLangConfig().getSwears();
        for (String exception : plugin.getMSLangConfig().getExceptions()){
            for(String swear : finalwords){
                if(swear.contains(exception)){
                    return null;
                }
            }
        }
        for(String forbidden : plugin.getMSLangConfig().getSwears()){
            for(String swear: finalwords){
                if(swear.toLowerCase().contains(forbidden)){
                    if(!(forbiddenlist.contains(forbidden))){
                        forbiddenlist.add(forbidden);
                    }
                }
            }
        }
        if(forbiddenlist.isEmpty()) return null;
        return forbiddenlist;
    }

    public List<String> checkswear(List<String> finalwords){
        List<String> swearlist = new ArrayList<>();
        for(String exception: plugin.getMSLangConfig().getExceptions()){
            for(String swear :finalwords){
                if(swear.contains(exception)){
                    return null;
                }
            }
        }
        for(String swears : plugin.getMSLangConfig().getSwears()){
            for (String swear : finalwords){
                String newswear = swear.toLowerCase();
                if(newswear.contains(swears.toLowerCase())){
                    if(!(swearlist.contains(swears.toLowerCase()))){
                        swearlist.add(swears.toLowerCase());
                    }
                }
            }
        }
        if(swearlist.isEmpty())return null;

        return swearlist;
    }

    public String checkPlayerName(List<String> msg){
        String finalPNMSG = null;
        String message = msg.toString();



        for(Player player : proxyServer.getAllPlayers()){
            if(msg.contains(player.getUsername()))
            {
                String mess = message.replace(player.getUsername(), "@"+player.getUsername());

                finalPNMSG = mess;
            }
        }
        return finalPNMSG;
    }

    public Player getPlayerFromName(String s){
        for(Player player : proxyServer.getAllPlayers()){
            if(s.contains(player.getUsername())){
                return player;
            }
        }
        return null;
    }

    public String containsPlayerName(String s){return checkPlayerName(aggressiveMode(s));}

    public List<String> isswear(String s){ return checkswear(aggressiveMode(s));}

    public List<String> isforbidden(String s){
        return checkforforbidden(aggressiveMode(s));
    }

}
