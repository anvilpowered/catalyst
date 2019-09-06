package essentials.modules.tab;
/*
Code used from this module was provieded by Aang23's Globaltab
can be found at : https://github.com/Aang23/GlobalTab/
*/

import essentials.MSEssentials;
import essentials.modules.server.MSServer;
import me.lucko.luckperms.api.Contexts;


public class UserInfoGetter {
    public static String getPrefixFromUsername(String username)
    {
        if(MSEssentials.api.getUserSafe(username).isPresent())
        {
            Contexts contexts = MSEssentials.api.getContextForUser(MSEssentials.api.getUser(username)).get();
            if(MSEssentials.api.getUser(username).getCachedData().getMetaData(contexts).getPrefix() != null)
            {
                return MSEssentials.api.getUser(username).getCachedData().getMetaData(contexts).getPrefix().toString();
            }else
                return "";
        }else return "";
    }

    public static String getSuffixFromUsername(String username)
    {
        if(MSEssentials.api.getUserSafe(username).isPresent())
        {
            Contexts contexts = MSEssentials.api.getContextForUser(MSEssentials.api.getUser(username)).get();
            if(MSEssentials.api.getUser(username).getCachedData().getMetaData(contexts).getSuffix() != null)
            {
                return MSEssentials.api.getUser(username).getCachedData().getMetaData(contexts).getSuffix();
            }else return "";
        }else
            return "";
    }


}
