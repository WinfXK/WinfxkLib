package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.api.RootForm;

public class MyPlayer {
    public RootForm fun;
    public BaseForm form;
    private static WinfxkLib ac = WinfxkLib.main;
    private Player player;
    public int ID;

    public MyPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public double getMoney() {
        return WinfxkLib.getEconomy().getMoney(player);
    }

    public double setMoney(double Money) {
        return WinfxkLib.getEconomy().setMoney(player, Money);
    }

    public double addMoney(double Money) {
        return WinfxkLib.getEconomy().addMoney(player, Money);
    }

    public double reduceMoney(double Money) {
        return WinfxkLib.getEconomy().reduceMoney(player, Money);
    }

    /**
     * 设置操作的Form
     *
     * @param rootForm form
     */
    public void setFun(RootForm rootForm) {
        this.fun = rootForm;
    }

    /**
     * 显示一个界面
     *
     * @param player 要显示界面的玩家
     * @param form   要显示的界面
     * @return 界面是否构建成功
     */
    public static boolean showForm(Player player, BaseForm form) {
        return showForm(player.getName(), form);
    }

    /**
     * 显示一个界面
     *
     * @param player 要显示界面的玩家
     * @param form   要显示的界面
     * @return 界面是否构建成功
     */
    public static boolean showForm(String player, BaseForm form) {
        MyPlayer myPlayer = WinfxkLib.getMyPlayer(player);
        return myPlayer == null ? false : myPlayer.showForm(form);
    }

    /**
     * 像是一个界面
     *
     * @param form 界面对象
     * @return 界面是否构建成功
     */
    public boolean showForm(BaseForm form) {
        return (this.form = form).MakeForm();
    }
}
