package cn.winfxk.nukkit.winfxklib.module.leave_word;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.module.leave_word.reading.RepeatReading;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class MarkRead extends LeaveWordForm {
    private static final String[] ItemKey = {"{Player}", "{Money}", "{LeaveWordTitle}", "{LeaveWordDate}", "{LeaveWordPlayer}", "{LeaveWordContent}", "{Streamline}"};

    public MarkRead(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    public boolean MakeForm() {
        if (!player.hasPermission(Permission))
            return makeShow(player, NotPermissions());
        if (Unread.size() <= 0)
            return makeShow(true, player.getName(), getTitle(), NotLeaveWord(), getConfirm(), (a, b) -> isBack(), getExitString(), (a, b) -> false);
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        for (Map.Entry<String, Object> entry : MarkRead.entrySet()) {
            if (!(entry.getValue() instanceof Map)) continue;
            Map<String, Object> item = (Map<String, Object>) entry.getValue();
            form.addButton(message.getSun(MainKey, FormKey, "ItemKey", ItemKey, new Object[]{player.getName(), myPlayer.getMoney(), item.get("Title"), item.get("Date"), item.get("Player"), item.get("Content"), getStreamline(Tool.objToString(item.get("Content")))}), (a, b) -> show(new RepeatReading(a, this, true, entry.getKey())));
        }
        form.addButton(getString("ClearMessage"), (player1, data) -> makeShow(true, player.getName(), getTitle(), getString("Sure"), getConfirm(), (a, b) -> {
            MarkRead = new HashMap<>();
            LeaveWordMap.put("MarkRead", MarkRead);
            config.set(player.getName(), LeaveWordMap);
            player1.sendMessage(getString("ClearMessageSucceed"));
            return config.save();
        }, getBack(), (a, b) -> isBack()));
        form.addButton(getBack(), (player1, data) -> isBack());
        form.show(player);
        return true;
    }

    private String getStreamline(String s) {
        return s == null || s.isEmpty() ? s : s.length() > 10 ? s.substring(0, 10) : s;
    }
}
