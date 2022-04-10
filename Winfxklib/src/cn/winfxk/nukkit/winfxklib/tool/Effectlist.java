package cn.winfxk.nukkit.winfxklib.tool;

import cn.nukkit.potion.Effect;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Effectlist {
    private String Name, Path, Key;
    private int ID;
    private Map<String, Object> map;
    private static Effectlist effect;
    private static final Map<String, Effectlist> Namelist, Keylist;
    private static final Map<Integer, Effectlist> IDlist;
    private static final File file = new File(WinfxkLib.getMain().getConfigDir(), WinfxkLib.EffectlistFileName);
    private static final Config config = new Config(file);

    static {
        Namelist = new HashMap<>();
        Keylist = new HashMap<>();
        IDlist = new HashMap<>();
        Map<String, Object> map = config.getMap();
        Object obj;
        Effectlist effect;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            obj = entry.getValue();
            if (!(obj instanceof Map)) continue;
            effect = new Effectlist((Map<String, Object>) obj);
            Namelist.put(effect.Name, effect);
            Keylist.put(effect.Key, effect);
            IDlist.put(effect.ID, effect);
        }
    }

    /**
     * 根据未知数据返回附魔ID
     *
     * @param obj 任意值<ID|Name|Key>
     * @return
     */
    public int getID(Object obj) {
        return getID(obj, -1);
    }

    /**
     * 根据未知数据返回附魔ID
     *
     * @param obj     任意值<ID|Name|Key>
     * @param Default 默认值
     * @return
     */
    public int getID(Object obj, int Default) {
        Effectlist effet = getEffet(obj, null);
        return effet == null ? Default : effet.ID;
    }

    /**
     * 根据未知数据返回附魔Key
     *
     * @param obj 任意值<ID|Name|Key>
     * @return
     */
    public String getKey(Object obj) {
        return getKey(obj, null);
    }

    /**
     * 根据未知数据返回附魔Key
     *
     * @param obj     任意值<ID|Name|Key>
     * @param Default 默认值
     * @return
     */
    public String getKey(Object obj, String Default) {
        Effectlist effet = getEffet(obj, null);
        return effet == null ? Default : effet.Key;
    }

    /**
     * 根据未知数据返回附魔名称
     *
     * @param obj 任意值<ID|Name|Key>
     * @return
     */
    public String getName(Object obj) {
        return getName(obj, null);
    }

    /**
     * 根据未知数据返回附魔名称
     *
     * @param obj     任意值<ID|Name|Key>
     * @param Default 默认值
     * @return
     */
    public String getName(Object obj, String Default) {
        Effectlist effet = getEffet(obj, null);
        return effet == null ? Default : effet.Name;
    }

    /**
     * 根据未知值返回数据对象
     *
     * @param obj 任意值<ID|Name|Key>
     * @return
     */
    public Effectlist getEffet(Object obj) {
        return getEffet(obj, null);
    }

    /**
     * 根据未知值返回数据对象
     *
     * @param obj     任意值<ID|Name|Key>
     * @param Default 默认值
     * @return
     */
    public Effectlist getEffet(Object obj, Effectlist Default) {
        if (obj == null) return Default;
        if (Tool.isInteger(obj)) {
            int ID = Tool.ObjToInt(obj);
            if (IDlist.containsKey(ID))
                return IDlist.get(ID);
        }
        String Name = Tool.objToString(obj, "").toLowerCase(Locale.ROOT);
        if (Keylist.containsKey(Name))
            return Keylist.get(Name);
        if (Namelist.containsKey(Name)) return Namelist.get(Name);
        return Default;
    }

    public Effectlist() {
        effect = this;
    }

    private Effectlist(Map<String, Object> map) {
        Name = Tool.objToString(map.get("Name"), "").toLowerCase(Locale.ROOT);
        Path = Tool.objToString(map.get("Path"), "").toLowerCase(Locale.ROOT);
        Key = Tool.objToString(map.get("Key"), "").toLowerCase(Locale.ROOT);
        ID = Tool.ObjToInt(map.get("ID"));
        this.map = map;
    }

    public Effect getEffect() {
        return Effect.getEffect(ID);
    }

    public static Effectlist getEffectlist() {
        return effect;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public String getKey() {
        return Key;
    }

    public String getPath() {
        return Path;
    }

    public String getName() {
        return Name;
    }

    public int getID() {
        return ID;
    }
}
