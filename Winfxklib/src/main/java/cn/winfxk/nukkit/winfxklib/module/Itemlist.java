package cn.winfxk.nukkit.winfxklib.module;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.module.itemlist.AddItem;
import cn.winfxk.nukkit.winfxklib.module.itemlist.AlterItem;
import cn.winfxk.nukkit.winfxklib.module.itemlist.DelItem;

import java.security.Permissions;

public class Itemlist extends BaseForm {
    public Itemlist(Player player, BaseForm Update, boolean isBack) {
        super(player, Update, isBack);
    }
    public static final String Permissions = "WinfxkLib.Form.Item";

    @Override
    public boolean MakeForm() {
        if (!player.hasPermission(Permissions))
            return sendMessage(message.getSon(MainKey, "NotPermissions", this));
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        form.addButton(getString("AddItem"), (a, b) -> show(new AddItem(player, this, true)));
        form.addButton(getString("DelItem"), (a, b) -> show(new DelItem(player, this, true)));
        form.addButton(getString("AlterItem"), (a, b) -> show(new AlterItem(player, this, true)));
        form.addButton(getBack(), (a, b) -> isBack());
        form.show(player);
        return true;
    }
}
