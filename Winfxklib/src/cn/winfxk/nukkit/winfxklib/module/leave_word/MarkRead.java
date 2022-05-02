package cn.winfxk.nukkit.winfxklib.module.leave_word;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;

public class MarkRead extends LeaveWordForm {
    public MarkRead(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    public boolean MakeForm() {
        return false;
    }
}
