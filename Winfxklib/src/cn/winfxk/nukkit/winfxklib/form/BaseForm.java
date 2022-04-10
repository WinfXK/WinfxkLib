package cn.winfxk.nukkit.winfxklib.form;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.MyPlayer;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;

public abstract class BaseForm extends BaseFormin {
    public static WinfxkLib main = WinfxkLib.getMain();
    protected MyPlayer myPlayer;
    protected Player player;
    private BaseFormin UpForm;
    protected static final String MainKey = "Form";

    static {
        message = WinfxkLib.getMessage();
    }

    public BaseForm(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
        this.myPlayer = WinfxkLib.getMyPlayer(player);
    }
}
