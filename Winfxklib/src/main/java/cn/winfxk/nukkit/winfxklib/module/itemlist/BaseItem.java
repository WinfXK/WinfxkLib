package cn.winfxk.nukkit.winfxklib.module.itemlist;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.tool.Itemlist;

import java.util.List;

import static cn.winfxk.nukkit.winfxklib.module.Itemlist.Permissions;

public abstract class BaseItem extends BaseForm {
    private List<Itemlist> Items;
    private int MaxCount, index = 0, Page = 1;
    protected static final String[] BaseKey = {"{ID}", "{ItemName}", "{Damage}", "{Path}", "{Page}"};
    protected Itemlist SItem;

    public BaseItem(Player player, BaseForm Update, boolean isBack) {
        super(player, Update, isBack);
        Items = Itemlist.getItems();
        MaxCount = main.getConfig().getInt("物品列表页最大数量");
        MaxCount = MaxCount <= 10 ? 10 : MaxCount;
        setK("{Player}", "{Money}", "{Page}");
    }

    @Override
    public boolean MakeForm() {
        index = (Page - 1) * MaxCount;
        setD(player.getName(), myPlayer.getMoney(), Page + "/" + (Items.size() / MaxCount));
        if (!player.hasPermission(Permissions))
            return sendMessage(message.getSon(MainKey, "NotPermissions", this));
        if (Items.size() <= 0) return sendMessage(getString("NotData"));
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        if (Page > 1) {
            form.addButton(getString("UpPage"), (a, b) -> {
                Page--;
                return MakeForm();
            });
        }
        if (Page < (Items.size() / MaxCount))
            form.addButton(getString("NextPage"), (a, b) -> {
                Page++;
                return MakeForm();
            });
        for (int i = index; i < MaxCount * Page && i < Items.size(); i++) {
            Itemlist item = Items.get(i);
            form.addButton(getString("ItemStencil", BaseKey, new Object[]{item.getID(), item.getName(), item.getDamage(), item.getPath(), Page + "/" + (Items.size() / MaxCount)}), item.isType(), item.getPath(), (a, b) -> makeItem(item));
        }
        form.addButton(getBack(), (a, b) -> isBack());
        form.show(player);
        return true;
    }

    protected abstract boolean makeItem(Itemlist itemlist);
}
