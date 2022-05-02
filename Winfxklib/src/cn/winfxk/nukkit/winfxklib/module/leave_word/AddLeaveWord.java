package cn.winfxk.nukkit.winfxklib.module.leave_word;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;

public class AddLeaveWord extends LeaveWordForm {
    public AddLeaveWord(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    public boolean MakeForm() {
        return false;
    }
}
