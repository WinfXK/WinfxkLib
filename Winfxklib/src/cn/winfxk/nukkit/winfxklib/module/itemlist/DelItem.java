package cn.winfxk.nukkit.winfxklib.module.itemlist;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.tool.Itemlist;

public class DelItem extends BaseItem {
    public DelItem(Player player, BaseForm Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    protected boolean makeItem(Itemlist itemlist) {
        SItem = itemlist;
        SimpleForm form = new SimpleForm(getID(), getTitle(), getString("Affirm", BaseKey, new Object[]{SItem.getID(), SItem.getName(), SItem.getDamage(), SItem.getPath()}));
        form.addButton(getConfirm(), (a, b) -> Itemlist.removeItem(SItem) && sendMessage(getString("DelSucceed")));
        form.addButton(getBack(), (a, b) -> isBack());
        form.show(player);
        return true;
    }
}
