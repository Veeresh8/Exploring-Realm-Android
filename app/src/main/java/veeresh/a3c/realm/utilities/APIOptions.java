package veeresh.a3c.realm.utilities;

import java.util.HashMap;

/**
 * Created by Veeresh on 3/11/17.
 */
public class APIOptions {

    public static HashMap<String, String> getOptions() {
        HashMap<String, String> options = new HashMap<>();
        options.put("type", "json");
        options.put("query", "list_player");
        return options;

    }

}
