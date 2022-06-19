package cn.winfxk.nukkit.winfxklib.tool;

import cn.nukkit.item.enchantment.Enchantment;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Enchantlist {
    private static Enchantlist enchant;
    private int ID;
    private String Name, Key;
    private Map<String, Object> map;
    private static final Map<String, Enchantlist> Namelist, Keylist;
    private static final Map<Integer, Enchantlist> IDlist;
    private static final File file = new File(WinfxkLib.getMain().getConfigDir(), WinfxkLib.EnchantListFileName);
    private static final Config config = new Config(file);

    static {
        Namelist = new HashMap<>();
        Keylist = new HashMap<>();
        IDlist = new HashMap<>();
        Map<String, Object> map = config.getMap();
        Enchantlist enchant;
        Object obj;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            obj = entry.getValue();
            if (!(obj instanceof Map)) continue;
            enchant = new Enchantlist((Map<String, Object>) obj);
            Namelist.put(enchant.Name, enchant);
            Keylist.put(enchant.Key, enchant);
            IDlist.put(enchant.ID, enchant);
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
        Enchantlist enchantlist = getEnchant(obj, null);
        return enchantlist == null ? Default : enchantlist.ID;
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
        Enchantlist enchantlist = getEnchant(obj, null);
        return enchantlist == null ? Default : enchantlist.Key;
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
        Enchantlist enchantlist = getEnchant(obj, null);
        return enchantlist == null ? Default : enchantlist.Name;
    }

    /**
     * 根据未知值返回数据对象
     *
     * @param obj 任意值<ID|Name|Key>
     * @return
     */
    public Enchantlist getEnchant(Object obj) {
        return getEnchant(obj, null);
    }

    /**
     * 根据未知值返回数据对象
     *
     * @param obj     任意值<ID|Name|Key>
     * @param Default 默认值
     * @return
     */
    public Enchantlist getEnchant(Object obj, Enchantlist Default) {
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

    private Enchantlist(Map<String, Object> map) {
        this.ID = Tool.ObjToInt(map.get("ID"));
        this.Name = Tool.objToString(map.get("Name"));
        this.Key = Tool.objToString(map.get("Key"), "").toLowerCase(Locale.ROOT);
        this.map = map;
    }

    public String getKey() {
        return Key;
    }

    public String getName() {
        return Name;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public int getID() {
        return ID;
    }

    public Enchantlist() {
        enchant = this;
    }

    public Enchantment getEnchantment() {
        return Enchantment.getEnchantment(ID);
    }

    public static Enchantlist getEnchantlist() {
        return enchant;
    }
}
