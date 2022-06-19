package cn.winfxk.nukkit.winfxklib.tool;

import cn.nukkit.item.Item;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.module.LeaveWord;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemLoad {
    @Contract("null, !null -> param2")
    public static @NotNull List<LeaveWord.Economy> getEconomy(Map<String, Object> map, List<LeaveWord.Economy> list) {
        if (list == null) list = new ArrayList<>();
        if (map == null || map.isEmpty()) return list;
        Map<String, Object> map1;
        MyEconomy economy;
        String EconomyID;
        double Money;
        for (Object obj : map.entrySet()) {
            try {
                if (obj == null) continue;
                if (!(obj instanceof Map)) {
                    if (!Tool.isInteger(obj)) continue;
                    list.add(new LeaveWord.Economy(WinfxkLib.getEconomy(), Tool.objToDouble(obj)));
                    continue;
                }
                map1 = (Map<String, Object>) obj;
                if (map1.isEmpty()) continue;
                EconomyID = Tool.objToString(map1.get("Money"));
                if (EconomyID == null || EconomyID.isEmpty() || !Tool.isInteger(EconomyID)) continue;
                Money = Tool.objToDouble(EconomyID);
                if (Money == 0) continue;
                EconomyID = Tool.objToString(map1.get("ID"), null);
                if (EconomyID == null || EconomyID.isEmpty()) continue;
                economy = WinfxkLib.getEconomy(EconomyID);
                if (economy == null) continue;
                list.add(new LeaveWord.Economy(economy, Money));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Contract("null, !null -> param2")
    public static @NotNull List<Item> getItem(Map<String, Object> map, List<Item> list) {
        if (list == null) list = new ArrayList<>();
        if (map == null || map.isEmpty()) return list;
        String[] strings;
        String SID;
        Item item;
        Itemlist itemlist;
        for (Object obj : map.entrySet()) {
            if (obj == null) continue;
            try {
                if (obj instanceof Map) {
                    list.add(Tool.loadItem((Map<String, Object>) obj));
                    continue;
                }
                SID = Tool.objToString(obj);
                if (Tool.isNumeric(SID)) {
                    list.add(Item.get(Tool.ObjToInt(SID)));
                    continue;
                }
                if (SID.contains("|")) {
                    strings = SID.split("|");
                    itemlist = WinfxkLib.getMain().getItemlist().getItem(strings[0]);
                    if (itemlist == null) continue;
                    item = itemlist.getItem();
                    if (strings.length > 1 && strings[1] != null && !strings[1].isEmpty() && Tool.isInteger(strings[1]))
                        item.setCount(Tool.ObjToInt(strings[1]));
                    list.add(item);
                    continue;
                }
                itemlist = WinfxkLib.getMain().getItemlist().getItem(SID);
                if (itemlist == null) continue;
                list.add(itemlist.getItem());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
