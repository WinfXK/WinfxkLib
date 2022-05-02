package cn.winfxk.nukkit.winfxklib.module.leave_word;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.tool.Tool;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LeaveWord extends LeaveWordForm {

    public LeaveWord(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
        FormKey = "Main";
    }

    @Override
    public boolean MakeForm() {
        if (!player.hasPermission(Permission))
            return makeShow(player, NotPermissions());
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        if (Unread.size() + MarkRead.size() <= 0)
            form.setContent(form.getContent() + "\n" + NotLeaveWord());
        if (Unread.size() > 1)
            form.addButton(message.getSun(MainKey, FormKey, "Unread", LeaveWordkey, new Object[]{player.getName(), WinfxkLib.getMyPlayer(player).getMoney(), Unread.size(), WinfxkLib.getconfig().getInt("LeaveWordAstrict", 10)}), (a, b) -> show(new Unread(player, this, true)));
        if (MarkRead.size() > 1)
            form.addButton(message.getSun(MainKey, FormKey, "MarkRead", LeaveWordkey, new Object[]{player.getName(), WinfxkLib.getMyPlayer(player).getMoney(), MarkRead.size(), WinfxkLib.getconfig().getInt("LeaveWordAstrict", 10)}), (a, b) -> show(new MarkRead(player, this, true)));
        if (MarkRead.size() > 0 || Unread.size() > 0)
            form.addButton(getString("ClearMessage"), (player1, data) -> {
                Unread = new HashMap<>();
                MarkRead = new HashMap<>();
                LeaveWordMap.put("MarkRead", MarkRead);
                LeaveWordMap.put("Unread", Unread);
                config.set(player1.getName(), LeaveWordMap);
                player1.sendMessage(getString("ClearMessageSucceed"));
                return config.save() | isBack();
            });
        form.addButton(getString("AddLeaveWord"), (a, b) -> show(new AddLeaveWord(player, this, true)));
        form.addButton(getBack(), (player1, data) -> isBack());
        form.show(player);
        return true;
    }

    public static void PlayerJoinEvent(@NotNull Player player) {
        Map<String, Object> map;
        if (!config.containsKey(player.getName())) {
            map = new HashMap<>();
            map.put("Unread", new HashMap<>());
            map.put("MarkRead", new HashMap<>());
        }
        Object o = config.get("Player");
        map = o instanceof Map ? (Map<String, Object>) o : new HashMap<>();
        o = map.get("Unread");
        Map<String, Object> Unread = o instanceof Map ? (Map<String, Object>) o : new HashMap<>();
        if (Unread.size() > 0)
            player.sendMessage(message.getSon(MainKey, "JoinServer", LeaveWordkey, new Object[]{player.getName(), WinfxkLib.getMyPlayer(player).getMoney(), Unread.size(), WinfxkLib.getconfig().getInt("LeaveWordAstrict", 10)}));
        String LeaveWordDate = Tool.objToString(map.get("LeaveWordDate"), null);
        if (LeaveWordDate == null || !LeaveWordDate.equals(Tool.getDate())) {
            map.put("LeaveWordCount", WinfxkLib.getconfig().getInt("LeaveWordAstrict", 10));
            map.put("LeaveWordDate", Tool.getDate());
        }
        config.set(player.getName(), map);
        config.save();
    }
}
