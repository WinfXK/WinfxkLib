package cn.winfxk.nukkit.winfxklib.module.leave_word.reading;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.module.leave_word.LeaveWordForm;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class RepeatReading extends LeaveWordForm {
    private final String Key;
    private final Map<String, Object> Item;
    private final Map<String, Object> Items;
    private final Map<String, Object> Economy;
    private static final String[] ErrorKey = {"{Player}", "{Money}", "{Error}"};
    private static final String[] EconomyKey = {"{Player}", "{Money}", "{EconomyID}", "{EconomyMoney}", "{EconomyUnits}"};
    private static final String[] ItemKey = {"{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemDamage}"};

    public RepeatReading(Player player, BaseFormin Update, boolean isBack, String Key) {
        super(player, Update, isBack);
        this.Key = Key;
        Item = (Map<String, Object>) MarkRead.get(Key);
        FormKey = "MarkRead";
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
        form.addButton(getString("ShowAccessory"), (player1, data) -> showAccessory());
        form.addButton(getString("Delete"), (player1, data) -> Delete());
        form.addButton(getBack(), (a, b) -> isBack());
        form.show(player);
        return true;
    }

    private boolean Delete() {
        MarkRead.remove(Key);
        LeaveWordMap.put("MarkRead", MarkRead);
        config.set(player.getName(), LeaveWordMap);
        player.sendMessage(getString("DeleteSucceed"));
        return config.save();
    }

    private boolean showAccessory() {
        SimpleForm form = new SimpleForm(getID(), getTitle() + " " + getString("ShowAccessory"), getContent() + " " + getString("ShowAccessoryContent"));
        Map<String, Object> EsMap;
        MyEconomy economy;
        Item item;
        for (Map.Entry<String, Object> entry : Items.entrySet()) {
            item = Tool.loadItem((Map<String, Object>) entry.getValue());
            form.addButton(message.getSun(MainKey, FormKey, "ItemButton", ItemKey, new Object[]{player.getName(), myPlayer.getMoney(), item.hasCustomName() ? item.getCustomName() : WinfxkLib.getMain().getItemlist().getName(item), item.getId(), item.getDamage()}), (A, b) -> MakeForm());
        }
        for (Map.Entry<String, Object> entry : Economy.entrySet()) {
            if (!(entry.getValue() instanceof Map)) continue;
            EsMap = (Map<String, Object>) entry.getValue();
            economy = WinfxkLib.getEconomy(Tool.objToString(EsMap.get("Economy")));
            if (economy == null) {
                player.sendMessage(message.getSun(MainKey, FormKey, "Exception", ErrorKey, new Object[]{player.getName(), myPlayer.getMoney(), "不支持的经济支持 " + EsMap.get("Economy")}));
                continue;
            }
            if (WinfxkLib.getBlacklistEconomy().contains(economy.getEconomyName())) {
                player.sendMessage(message.getSun(MainKey, FormKey, "Exception", ErrorKey, new Object[]{player.getName(), myPlayer.getMoney(), "被管理员禁用的经济支持 " + EsMap.get("Economy")}));
                continue;
            }
            form.addButton(message.getSun(MainKey, FormKey, "EconomyButton", EconomyKey, new Object[]{player.getName(), myPlayer.getMoney(), EsMap.get("Economy"), Tool.objToDouble(EsMap.get("Money")), economy.getMoneyName()}), (a, b) -> MakeForm());
        }
        form.addButton(getBackString(), (a, b) -> MakeForm());
        form.show(player);
        return true;
    }
}
