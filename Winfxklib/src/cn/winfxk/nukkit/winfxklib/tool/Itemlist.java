package cn.winfxk.nukkit.winfxklib.tool;

import cn.nukkit.item.Item;
import cn.winfxk.nukkit.winfxklib.Message;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Itemlist {
    private static final File file = new File(WinfxkLib.getConfigDir(), WinfxkLib.ItemListFileName);
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
                item = new Itemlist(Tool.ObjToInt(map1.get("ID")), Tool.ObjToInt(map1.get("Damage")), Tool.objToString(map1.get("Name")), Tool.objToString(map1.get("Path")), entry.getKey(), !Tool.ObjToBool(map1.get("Type"), false));
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
        if (NameItem.containsKey(Name))
            return NameItem.get(Name);
        return Default;
    }

    /**
     * 内部构建函数
     *
     * @param ID     物品ID
     * @param Damage 物品特殊值
     * @param Name   物品名称
     * @param Path   物品图标路径
     */
    protected Itemlist(int ID, int Damage, String Name, String Path, String SID, boolean Type) {
        this.ID = ID;
        this.Damage = Damage;
        this.Name = Name;
        this.Path = Path;
        this.SID = SID;
        this.Type = Type;
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
