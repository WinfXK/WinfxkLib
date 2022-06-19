package cn.winfxk.nukkit.winfxklib.tool;

import cn.nukkit.item.Item;
import cn.winfxk.nukkit.winfxklib.Message;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Itemlist {
    private static final File file = new File(WinfxkLib.getMain().getConfigDir(), WinfxkLib.ItemListFileName);
    private static final MyMap<String, Itemlist> AllItems = new MyMap<>();
    private static final MyMap<String, Itemlist> NameItem = new MyMap<>();
    private static final MyMap<Integer, Itemlist> IDItem = new MyMap<>();
    private static final MyMap<String, Itemlist> SIDItem = new MyMap<>();
    private static final MyList<Itemlist> Items = new MyList<>();
    private static final Config config = new Config(file);
    private static final Message message = WinfxkLib.getMessage();
    private static final WinfxkLib main = WinfxkLib.getMain();
    private static Itemlist itemlist;
    protected int ID;
    protected int Damage;
    protected String Name;
    protected String Path;
    protected String SID;
    protected boolean Type;
    protected Map<String, Object> map;

    static {
        Map<String, Object> map = config.getMap();
        if (map == null) {
            main.getLogger().error(message.getSon("异常", "无法加载物品列表"));
        } else {
            Map<String, Object> map1;
            Itemlist item;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!(entry.getValue() instanceof Map)) continue;
                map1 = (Map<String, Object>) entry.getValue();
                item = new Itemlist(map1, entry.getKey());
                AllItems.put(item.ID + ":" + item.Damage, item);
                SIDItem.add(item.getSID(), item);
                if (!IDItem.containsKey(item.ID))
                    IDItem.put(item.ID, item);
                if (!NameItem.containsKey(item.Name))
                    NameItem.put(item.Name, item);
                Items.add(item);
            }
        }
    }

    /**
     * 根据'ID:Damage'的格式返回Item对象，哪怕没有该物品的自定义数据，除非系统真的不支持该物品
     *
     * @param AllID 物品ID
     * @return
     */
    public Item setItem(String AllID) {
        return setItem(AllID, (Item) null);
    }

    /**
     * 根据'ID:Damage'的格式返回Item对象，哪怕没有该物品的自定义数据，除非系统真的不支持该物品
     *
     * @param AllID   物品ID
     * @param Default 默认值
     * @return
     */
    public Item setItem(String AllID, Item Default) {
        if (AllID == null || AllID.isEmpty()) return Default;
        String[] S = AllID.split(":");
        if (S[0] == null || S[0].isEmpty() || !Tool.isInteger(S[0])) return Default;
        return new Item(Tool.ObjToInt(S[0]), S.length >= 2 ? Tool.ObjToInt(S[1]) : 0);
    }

    protected Itemlist(int id, int damage, String name, String path, String sid, boolean type) {
        ID = id;
        Damage = damage;
        Name = name;
        Path = path;
        SID = sid;
        Type = type;
    }

    public static MyMap<String, Itemlist> getSIDItem() {
        return SIDItem;
    }

    /**
     * 设置或者添加一个物品进入系统
     *
     * @param SID
     * @param item
     * @return
     */
    public static boolean setItem(String SID, Itemlist item) {
        if (SIDItem.containsKey(SID)) {
            Itemlist itemList = SIDItem.get(SID);
            removeItem(itemList);
        }
        SIDItem.put(SID, item);
        AllItems.put(item.getID() + ":" + item.getDamage(), item);
        if (!NameItem.containsKey(item.Name))
            NameItem.put(item.Name, item);
        if (!IDItem.containsKey(item.ID))
            IDItem.put(item.getID(), item);
        Items.add(item);
        config.set(item.getSID(), item.getItemMap());
        return config.save();
    }

    public MyMap<String, Object> getItemMap() {
        MyMap<String, Object> map = new MyMap<>();
        map.put("Name", Name);
        map.put("ID", ID);
        map.put("Damage", Damage);
        map.put("Path", Path);
        map.put("Type", !Type);
        return map;
    }

    @Override
    public String toString() {
        return getItemMap().add("SID", SID).toString();
    }

    /**
     * 删除一个物品支持
     *
     * @param item
     * @return
     */
    public static boolean removeItem(Itemlist item) {
        AllItems.remove(item.getID() + ":" + item.getDamage());
        NameItem.removeValues(item);
        IDItem.removeValues(item);
        Items.remove(item);
        SIDItem.remove(item.SID);
        config.remove(item.getSID());
        return config.save();
    }

    public static Itemlist getItemlist() {
        return itemlist;
    }

    public Itemlist() {
        itemlist = this;
    }

    public static MyList<Itemlist> getItems() {
        return Items;
    }

    /**
     * 返回物品名称
     * <br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj     判断依据（这个值可以是存ID、ID与Damage的组合、物品名称）
     * @param Default 检索实拍将会返回的数据
     * @return 物品名称
     */
    public String getName(Object obj, String Default) {
        return getName(obj, true, Default);
    }

    /**
     * 返回物品名称
     * <br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj 判断依据（这个值可以是存ID、ID与Damage的组合、物品名称）
     * @return 物品名称
     */
    public String getName(Object obj) {
        return getName(obj, true, null);
    }

    /**
     * 返回物品名称
     * <br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj    判断依据（这个值可以是存ID、ID与Damage的组合、物品名称）
     * @param strict 是否严格判断（若该值为True则Damage必须完全一样，否则返回Default）
     * @return 物品名称
     */
    public String getName(Object obj, boolean strict) {
        return getName(obj, strict, null);
    }

    /**
     * 返回物品名称
     * <br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj     判断依据（这个值可以是存ID、ID与Damage的组合、物品名称）
     * @param strict  是否严格判断（若该值为True则Damage必须完全一样，否则返回Default）
     * @param Default 检索实拍将会返回的数据
     * @return 物品名称
     */
    public String getName(Object obj, boolean strict, String Default) {
        Itemlist item = getItem(obj, strict, null);
        if (item == null) return Default;
        return item.Name;
    }

    /**
     * 根据任意值返回MyItem对象<br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj    判断依据
     * @param strict 是否严格判断（若该值为True则Damage必须完全一样，否则返回Default）
     * @return MyItem对象
     */
    public Itemlist getItem(Object obj, boolean strict) {
        return getItem(obj, strict, null);
    }

    /**
     * 根据任意值返回MyItem对象<br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj 判断依据
     * @return MyItem对象
     */
    public Itemlist getItem(Object obj) {
        return getItem(obj, true, null);
    }

    /**
     * 根据任意值返回MyItem对象<br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj     判断依据
     * @param Default 检索实拍将会返回的数据
     * @return MyItem对象
     */
    public Itemlist getItem(Object obj, Itemlist Default) {
        return getItem(obj, true, Default);
    }

    /**
     * 根据任意值返回MyItem对象<br>这个值可以是存ID、ID与Damage的组合、物品名称
     *
     * @param obj     判断依据
     * @param strict  是否严格判断（若该值为True则Damage必须完全一样，否则返回Default）
     * @param Default 检索实拍将会返回的数据
     * @return MyItem对象
     */
    public Itemlist getItem(Object obj, boolean strict, Itemlist Default) {
        if (obj == null) return Default;
        int ID, Damage;
        if (obj instanceof Item) {
            Item item = (Item) obj;
            ID = item.getId();
            Damage = item.getDamage();
            if (AllItems.containsKey(ID + ":" + Damage))
                return AllItems.get(ID + ":" + Damage);
            return getNullItemlist(ID, Damage, item.hasCustomName() ? item.getCustomName() : item.getName(), null, null, true);
        }
        String Name = Tool.objToString(obj);
        if (Name.isEmpty()) return Default;
        if (Tool.isInteger(obj)) {
            ID = Tool.ObjToInt(obj);
            if (IDItem.containsKey(ID))
                return IDItem.get(ID);
        }
        if (Name.contains(":")) {
            if (AllItems.containsKey(Name))
                return AllItems.get(Name);
            String[] ID_Damage = Name.split(":");
            if (ID_Damage.length >= 2 && Tool.isInteger(ID_Damage[0])) {
                ID = Tool.ObjToInt(ID_Damage[0]);
                if (Tool.isInteger(ID_Damage[1])) {
                    Damage = Tool.ObjToInt(ID_Damage[1]);
                    if (AllItems.containsKey(ID + ":" + Damage))
                        return AllItems.get(ID + ":" + Damage);
                }
                if (!strict)
                    if (AllItems.containsKey(ID + ":0"))
                        return AllItems.get(ID + ":0");
            }
        }
        if (NameItem.containsKey(Name.toLowerCase(Locale.ROOT)))
            return NameItem.get(Name.toLowerCase(Locale.ROOT));
        return Default;
    }

    /**
     * 根据提供的数据返回一个不存在的数据
     *
     * @param ID      物品ID
     * @param Damage  物品Damage
     * @param Name    物品Name
     * @param SID     物品SID
     * @param Path    物品Path
     * @param isLocal 物品是否是本地图标
     * @return 不在系统集合内的数据
     */
    protected static Itemlist getNullItemlist(int ID, int Damage, String Name, String SID, String Path, boolean isLocal) {
        Map<String, Object> map = new HashMap<>();
        if (SID == null) {
            String s = Tool.getRandString();
            while (SIDItem.containsKey(s))
                s += Tool.getRandString();
        }
        map.put("SID", SID);
        map.put("ID", ID);
        map.put("Damage", Damage);
        map.put("Name", Name);
        map.put("Path", Path);
        map.put("isLocal", !isLocal);
        return new Itemlist(map, SID);
    }

    /**
     * 内部构建函数
     */
    protected Itemlist(Map<String, Object> map, String Key) {
        this.ID = Tool.ObjToInt(map.get("ID"));
        this.Damage = Tool.ObjToInt(map.get("Damage"));
        this.Name = Tool.objToString(map.get("Name")).toLowerCase(Locale.ROOT);
        this.Path = Tool.objToString(map.get("Path"));
        this.SID = Key;
        this.Type = !Tool.ObjToBool(map.get("Type"), false);
        this.map = map;
    }

    /**
     * 返回物品的图标路径
     *
     * @return
     */
    public String getPath() {
        return Path;
    }

    /**
     * 返回当前数据代表的Item对象
     *
     * @return
     */
    public Item getItem() {
        return new Item(ID, Damage);
    }

    public int getDamage() {
        return Damage;
    }

    public String getName() {
        return Name;
    }

    public int getID() {
        return ID;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * 是否是本地贴图
     *
     * @return
     */
    public boolean isType() {
        return Type;
    }

    public String getSID() {
        return SID;
    }
}
