package cn.winfxk.nukkit.winfxklib.money;

import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.tool.MyMap;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.Map;

public class EasyEconomy extends MyEconomy {
    private static final String Economy, Money;
    public static EasyEconomy MyEconomy;

    static {
        Map<String, Object> map = WinfxkLib.getMyConfig().getMap("EasyEconomy", new MyMap<>());
        Economy = Tool.objToString(map.get("ID"), "EasyEconomy");
        Money = Tool.objToString(map.get("Name"), "狗屎");
    }

    /**
     * 经济支持管理 </br>
     * Economy support management
     */
    public EasyEconomy() {
        super(Economy, Money);
        if (MyEconomy == null) MyEconomy = this;
    }

    @Override
    public double getMoney(String player) {
        return 0;
    }

    @Override
    public double addMoney(String player, double Money) {
        return 0;
    }

    @Override
    public double reduceMoney(String player, double Money) {
        return 0;
    }

    @Override
    public double setMoney(String player, double Money) {
        return 0;
    }

    @Override
    public boolean allowArrears() {
        return false;
    }
}
