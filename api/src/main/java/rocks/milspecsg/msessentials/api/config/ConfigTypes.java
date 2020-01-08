package rocks.milspecsg.msessentials.api.config;

import com.google.common.reflect.TypeToken;

import java.util.List;


@SuppressWarnings("UnstableApiUsage")
public interface ConfigTypes {

    TypeToken<List<String>> STRINGLIST = new TypeToken<List<String>>() {};
}
