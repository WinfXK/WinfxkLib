package cn.winfxk.nukkit.winfxklib.module;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.module.leave_word.AddLeaveWord;
import cn.winfxk.nukkit.winfxklib.module.leave_word.LeaveWordForm;
import cn.winfxk.nukkit.winfxklib.module.leave_word.MarkRead;
import cn.winfxk.nukkit.winfxklib.module.leave_word.Unread;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import cn.winfxk.nukkit.winfxklib.tool.MyMap;
import cn.winfxk.nukkit.winfxklib.tool.Tool;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
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

    /**
     * 给一个玩家留言
     *
     * @param player   玩家名称
     * @param Content  留言内容
     * @param Items    包含的物品
     * @param Economys 包含的经济
     * @return 留言是否成功
     */
    public static boolean addLeaveWord(@NotNull String player, @NotNull String Title, @NotNull String Content, List<Item> Items, List<Economy> Economys) {
        if (!config.containsKey(player) && !WinfxkLib.getconfig().getBoolean("UnrealLeaveWord")) return false;
        MyMap<String, Object> playerMap = config.getMap(player, new MyMap<>());
        Map<String, Object> Unread = playerMap.getMap("Unread", new MyMap<>());
        StringBuilder Key = new StringBuilder(Tool.getRandString());
        Map<String, Object> ItemMap = new HashMap<>();
        Map<String, Object> LeaveWordMap = new HashMap<>();
        for (Item item : Items) {
            while (ItemMap.containsKey(Key.toString()))
                Key.append(Tool.getRandString());
            ItemMap.put(Key.toString(), Tool.saveItem(item));
        }
        LeaveWordMap.put("Items", ItemMap);
        ItemMap = new HashMap<>();
        Key = new StringBuilder(Tool.getRandString());
        for (Economy economy : Economys) {
            while (ItemMap.containsKey(Key.toString()))
                Key.append(Tool.getRandString());
            ItemMap.put(Key.toString(), MyMap.make("Economy", (Object) economy.EconomyID).add("Money", economy.Money));
        }
        Key = new StringBuilder(Tool.getRandString());
        while (Unread.containsKey(Key.toString()))
            Key.append(Tool.getRandString());
        LeaveWordMap.put("Economy", ItemMap);
        LeaveWordMap.put("Title", Title);
        LeaveWordMap.put("Content", Content);
        Unread.put(Key.toString(), LeaveWordMap);
        playerMap.put("Unread", Unread);
        return config.set(player, playerMap).save();
    }

    /**
     * 给一个玩家留言
     *
     * @param player   玩家对象
     * @param Content  留言内容
     * @param Items    包含的物品
     * @param Economys 包含的经济
     * @return 留言是否成功
     */
    public static boolean addLeaveWord(@NotNull Player player, @NotNull String Title, @NotNull String Content, List<Item> Items, List<Economy> Economys) {
        return addLeaveWord(player.getName(), Title, Content, Items, Economys);
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

    public static class Economy {
        private final String EconomyID;
        private final double Money;
        private MyEconomy economy;

        public Economy(String EconomyID, double Money) {
            this.EconomyID = EconomyID;
            this.Money = Money;
            economy = WinfxkLib.getEconomy(EconomyID);
        }

        public Economy(MyEconomy myEconomy, double Money) {
            this.EconomyID = myEconomy.getEconomyName();
            this.Money = Money;
            economy = myEconomy;
        }

        public String getEconomyID() {
            return EconomyID;
        }

        public double getMoney() {
            return Money;
        }

        public MyEconomy getEconomy() {
            return economy;
        }
    }
}
