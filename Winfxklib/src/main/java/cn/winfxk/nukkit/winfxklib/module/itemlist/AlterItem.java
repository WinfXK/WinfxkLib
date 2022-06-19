package cn.winfxk.nukkit.winfxklib.module.itemlist;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.api.CustomForm;
import cn.winfxk.nukkit.winfxklib.tool.Itemlist;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

public class AlterItem extends BaseItem {
    public AlterItem(Player player, BaseForm Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    protected boolean makeItem(Itemlist itemlist) {
        SItem = itemlist;
        CustomForm form = new CustomForm(getID(), getTitle());
        form.addLabel(getTitle());
        form.addInput(getString("ID"), itemlist.getID(), getString("ID"));
        form.addInput(getString("Damage"), itemlist.getDamage(), getString("Damage"));
        form.addInput(getString("Name"), itemlist.getName(), getString("Name"));
        form.addInput(getString("Path"), itemlist.getPath(), getString("Path"));
        form.addToggle(getString("Type"), !itemlist.isType());
        form.show(player, null, (a, b) -> isBack());
        return true;
    }

    @Override
    public boolean DisposeCustom(FormResponseCustom data) {
        String string = data.getInputResponse(1);
        int ID = Tool.ObjToInt(string);
        if (string == null || string.isEmpty() || !Tool.isInteger(string) || ID < 0)
            return sendMessage(getString("NotInputID"));
        string = data.getInputResponse(2);
        int Damage = Tool.ObjToInt(string);
        if (string == null || string.isEmpty() || !Tool.isInteger(string) || Damage < 0)
            return sendMessage(getString("NotInputDamage"));
        String Name = data.getInputResponse(3);
        if (Name == null || Name.isEmpty())
            return sendMessage(getString("NotInputName"));
        String Path = data.getInputResponse(4);
        if (Name == null || Name.isEmpty())
            return sendMessage(getString("NotInputPath"));
        boolean isNet = data.getToggleResponse(5);
        return Itemlist.setItem(SItem.getSID(), new SBItem(ID, Damage, Name, Path, SItem.getSID(), isNet)) && sendMessage(getString("AlterSucceed"));
    }

    protected static class SBItem extends Itemlist {
        protected SBItem(int ID, int Damage, String Name, String Path, String SID, boolean Type) {
            super(ID, Damage, Name, Path, SID, Type);
        }
    }
}
