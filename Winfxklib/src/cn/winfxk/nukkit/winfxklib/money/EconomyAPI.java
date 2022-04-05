package cn.winfxk.nukkit.winfxklib.money;

import cn.winfxk.nukkit.winfxklib.WinfxkLib;

/**
 * @author Winfxk
 */
public class EconomyAPI extends MyEconomy {
    private me.onebone.economyapi.EconomyAPI eApi;
    public final static String Name = "EconomyAPI";

    /**
     * 增加对EconomyAPI的支持</br>
     * Added support for EconomyAPI
     *
     * @param ac
     */
    public EconomyAPI(WinfxkLib ac) {
        super(Name, getMoneyEconomyAPIName());
        eApi = me.onebone.economyapi.EconomyAPI.getInstance();
    }

    public static String getMoneyEconomyAPIName() {
        String string = WinfxkLib.getMyConfig().getString("EconomyAPI-MoneyName");
        return string != null && !string.isEmpty() ? string : WinfxkLib.getMyConfig().getString("MoneyName");
    }

    @Override
    public double getMoney(String player) {
        return eApi.myMoney(player);
    }

    @Override
    public double addMoney(String player, double Money) {
        eApi.addMoney(player, Money);
        return getMoney(player);
    }

    @Override
    public double reduceMoney(String player, double Money) {
        eApi.reduceMoney(player, Money);
        return getMoney(player);
    }

    @Override
    public double setMoney(String player, double Money) {
        eApi.setMoney(player, Money);
        return getMoney(player);
    }

    @Override
    public boolean allowArrears() {
        return false;
    }
}
