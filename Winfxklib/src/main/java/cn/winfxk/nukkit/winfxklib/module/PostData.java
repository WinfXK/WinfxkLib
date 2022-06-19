package cn.winfxk.nukkit.winfxklib.module;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;

public class PostData extends BaseForm {
    public PostData(Player player, BaseForm Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    public boolean MakeForm() {
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        form.addButton(getString("Itemlist"), (player, data) -> show(new Itemlist(player, this, true)));
        form.addButton(getBack(), (p, s) -> isBack());
        form.show(player);
        return false;
    }
}
