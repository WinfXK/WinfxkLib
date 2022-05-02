package cn.winfxk.nukkit.winfxklib.module.leave_word.reading;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.module.leave_word.LeaveWordForm;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class Reading extends LeaveWordForm {
    private final String Key;
    private final Map<String, Object> Item;
    private final Map<String, Object> Items;
    private final Map<String, Object> Economy;
    private static final String[] ErrorKey = {"{Player}", "{Money}", "{Error}"};

    public Reading(Player player, BaseFormin Update, boolean isBack, String Key) {
        super(player, Update, isBack);
        this.Key = Key;
        Item = (Map<String, Object>) Unread.get(Key);
        FormKey = "Unread";
        Object o = Item.get("Items");
        Items = o instanceof Map ? (Map<String, Object>) o : new HashMap<>();
        o = Item.get("Economy");
        Economy = o instanceof Map ? (Map<String, Object>) o : new HashMap<>();
    }

    @Override
    public boolean MakeForm() {
        SimpleForm form = new SimpleForm(getID(), Tool.objToString(Item.get("Title")), Tool.objToString(Item.get("Content")));
        if (Economy.size() + Items.size() > 0)
            form.setContent(getContent() + "\n" + getString("Attachment"));
        form.addButton(getString("Accept"), (player1, data) -> read());
        form.addButton(getString("AcceptAndDelete"), (a, b) -> Delete() & read());
        form.addButton(getString("Delete"), (player1, data) -> Delete());
        form.addButton(getBack(), (a, b) -> isBack());
        form.show(player);
        return true;
    }

    private boolean read() {
        String s = Tool.getRandString();
        while (MarkRead.containsKey(s))
            s += Tool.getRandString();
        Level level = player.getLevel();
        for (Map.Entry<String, Object> entry : Items.entrySet()) {
            try {
                if (entry.getValue() instanceof Map)
                    level.dropItem(player, Tool.loadItem((Map<String, Object>) entry.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(message.getSun(MainKey, FormKey, "Exception", ErrorKey, new Object[]{player.getName(), myPlayer.getMoney(), e.getMessage()}));
            }
        }
        Map<String, Object> EsMap;
        MyEconomy economy;
        for (Map.Entry<String, Object> entry : Economy.entrySet()) {
            if (!(entry.getValue() instanceof Map)) continue;
            EsMap = (Map<String, Object>) entry.getValue();
            economy = WinfxkLib.getEconomy(Tool.objToString(EsMap.get("Economy")));
            if (economy == null) {
                player.sendMessage(message.getSun(MainKey, FormKey, "Exception", ErrorKey, new Object[]{player.getName(), myPlayer.getMoney(), "不支持的经济支持"}));
                continue;
            }
            if (WinfxkLib.getBlacklistEconomy().contains(economy.getEconomyName())) {
                player.sendMessage(message.getSun(MainKey, FormKey, "Exception", ErrorKey, new Object[]{player.getName(), myPlayer.getMoney(), "被管理员禁用的经济支持"}));
                continue;
            }
            economy.addMoney(player.getName(), Tool.objToDouble(EsMap.get("Money")));
        }
        MarkRead.put(s, Item);
        LeaveWordMap.put("MarkRead", MarkRead);
        config.set(player.getName(), LeaveWordMap);
        player.sendMessage(getString("DeleteSucceed"));
        return config.save();
    }

    private boolean Delete() {
        Unread.remove(Key);
        LeaveWordMap.put("Unread", Unread);
        config.set(player.getName(), LeaveWordMap);
        player.sendMessage(getString("DeleteSucceed"));
        return config.save();
    }
}
