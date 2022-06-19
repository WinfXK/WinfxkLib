package cn.winfxk.nukkit.winfxklib.module.itemlist;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.api.CustomForm;
import cn.winfxk.nukkit.winfxklib.tool.Itemlist;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import static cn.winfxk.nukkit.winfxklib.module.Itemlist.Permissions;

public class AddItem extends BaseForm {
    protected Itemlist SItem;
    protected String SID;

    public AddItem(Player player, BaseForm Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    public boolean MakeForm() {
        if (!player.hasPermission(Permissions))
            return sendMessage(message.getSon(MainKey, "NotPermissions", this));
        CustomForm form = new CustomForm(getID(), getTitle());
        form.addLabel(getTitle());
        form.addInput(getString("ID"), "", getString("ID"));
        form.addInput(getString("Damage"), "", getString("Damage"));
        form.addInput(getString("Name"), "", getString("Name"));
        form.addInput(getString("Path"), "", getString("Path"));
        form.addToggle(getString("Type"), false);
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
        SID = Tool.getRandString();
        while (Itemlist.getSIDItem().containsKey(SID))
            SID += Tool.getRandString();
        return Itemlist.setItem(SID, new AlterItem.SBItem(ID, Damage, Name, Path, SID, !isNet)) && sendMessage(getString("AlterSucceed"));
    }

    protected static class SBItem extends Itemlist {
        protected SBItem(int ID, int Damage, String Name, String Path, String SID, boolean Type) {
            super(ID, Damage, Name, Path, SID, Type);
        }
    }
}
