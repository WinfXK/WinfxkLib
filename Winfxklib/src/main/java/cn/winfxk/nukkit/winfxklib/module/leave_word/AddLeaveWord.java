package cn.winfxk.nukkit.winfxklib.module.leave_word;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.item.Item;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;
import cn.winfxk.nukkit.winfxklib.form.api.CustomForm;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.module.LeaveWord;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public class AddLeaveWord extends LeaveWordForm {
    private static final String[] EconomyKey = {"{Player}", "{Money}", "{EconomyID}", "{EconomyMoney}", "{EconomyUnits}"};
    private static final String[] ItemKey = {"{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemDamage}"};
    private String LeaveWordTitle = "", LeaveWordContent = "", Recipients;
    private final List<LeaveWord.Economy> Economys = new ArrayList<>();
    private final List<MyEconomy> SelectEconomys = new ArrayList<>();
    private final List<Item> Items = new ArrayList<>();
    private final List<Item> SelectItem = new ArrayList<>();
    private boolean Succeed = false;
    private boolean isOnline = false;

    public AddLeaveWord(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    public boolean MakeForm() {
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        form.addButton(getString("AddLeaveWord"), (a, b) -> addLeaveWord());
        form.addButton(getString("AddItem"), (a, b) -> AddItem());
        form.addButton(getString("AddEconomy"), (a, b) -> AddEconomy());
        form.addButton(getConfirm(), (a, b) -> (Succeed = true) & (Recipients == null || Recipients.isEmpty() ? addLeaveWord() : LeaveWord.addLeaveWord(Recipients, LeaveWordTitle, LeaveWordContent, Items, Economys)));
        form.addButton(getBack(), (a, b) -> isBack());
        return true;
    }

    private boolean AddEconomy() {
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        if (Economys.size() > 0) form.setContent(getContent() + "\n" + getString("ClickDeleteEconomy"));
        LeaveWord.Economy economy;
        for (int i = 0; i < Economys.size(); i++) {
            economy = Economys.get(i);
            int finalI = i;
            form.addButton(message.getSun(MainKey, FormKey, "EconomyContent", EconomyKey, new Object[]{player.getName(), myPlayer.getMoney(), economy.getEconomyID(), economy.getMoney(), economy.getEconomy().getMoneyName()}), (a, b) -> {
                Economys.remove(finalI);
                return AddEconomy();
            });
        }
        form.addButton(getString("AddEconomy"), (a, b) -> MakeEconomy());
        form.addButton(getBackString(), (a, b) -> MakeForm());
        form.show(player);
        return true;
    }

    private boolean MakeEconomy() {
        CustomForm form = new CustomForm(getID(), getTitle());
        form.addLabel(getContent());
        form.addDropdown(getString("SelectEconomy"), getEconomys());
        form.addInput(getString("EconomyMoney"), 1);
        form.show(player, (a, b) -> {
            FormResponseCustom data = (FormResponseCustom) b;
            int index = data.getDropdownResponse(1).getElementID();
            MyEconomy economy = SelectEconomys.get(index);
            String s = data.getInputResponse(2);
            double Money = Tool.objToDouble(s);
            if (s == null || s.isEmpty() || !Tool.isInteger(s) || Money <= 0)
                return sendMessage(getString("EconomyNUll")) | MakeEconomy();
            if (Money > economy.getMoney(player))
                return sendMessage(getString("EconomyExcessive")) | MakeEconomy();
            Economys.add(new LeaveWord.Economy(economy.getEconomyName(), Money));
            return sendMessage(getString("EconomyAddSucceed")) | AddEconomy();
        }, (a, b) -> MakeForm());
        return true;
    }

    private List<String> getEconomys() {
        SelectEconomys.clear();
        List<String> list = new ArrayList<>();
        MyEconomy def = WinfxkLib.getEconomy();
        SelectEconomys.add(def);
        list.add(message.getSun(MainKey, FormKey, "EconomyContent", EconomyKey, new Object[]{player.getName(), myPlayer.getMoney(), def.getEconomyName(), "0", def.getMoneyName()}));
        for (MyEconomy economy : WinfxkLib.getEconomys()) {
            if (economy.equals(def) || WinfxkLib.getBlacklistEconomy().contains(economy.getEconomyName())) continue;
            list.add(message.getSun(MainKey, FormKey, "EconomyContent", EconomyKey, new Object[]{player.getName(), myPlayer.getMoney(), economy.getEconomyName(), "0", economy.getMoneyName()}));
            SelectEconomys.add(economy);
        }
        return list;
    }

    private boolean AddItem() {
        SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
        if (Items.size() > 0) form.setContent(form.getContent() + "\n" + getString("点击可以删除物品"));
        Item item;
        for (int i = 0; i < Items.size(); i++) {
            item = Items.get(i);
            int finalI = i;
            form.addButton(message.getSun(MainKey, FormKey, "ItemContent", ItemKey, new Object[]{player.getName(), myPlayer.getMoney(), item.hasCustomName() ? item.getCustomName() : WinfxkLib.getMain().getItemlist().getName(item), item.getId(), item.getDamage()}), (a, b) -> {
                Items.remove(finalI);
                return AddItem();
            });
        }
        form.addButton(getString("AddItem"), (a, b) -> MakeItem());
        form.addButton(getBackString(), (a, b) -> MakeForm());
        form.show(player);
        return true;
    }

    private boolean MakeItem() {
        CustomForm form = new CustomForm(getID(), getTitle());
        form.addLabel(getContent());
        form.addDropdown(getString("SelectItem"), getItems());
        form.addInput(getString("ItemCount"), 1, getString("ItemCount"));
        form.show(player, (a, b) -> {
            FormResponseCustom data = (FormResponseCustom) b;
            int index = data.getDropdownResponse(1).getElementID();
            Item item = SelectItem.get(index);
            String s = data.getInputResponse(2);
            int Count = Tool.ObjToInt(s);
            if (s == null || s.isEmpty() || !Tool.isInteger(s) || Count <= 0)
                return sendMessage(getString("ItemCountNUll")) & MakeItem();
            if (Count > item.getCount())
                return sendMessage(getString("ItemCountExcessive"));
            item.setCount(Count);
            Items.add(item);
            return sendMessage(getString("ItemAddSucceed")) | AddItem();
        }, (a, b) -> AddItem());
        return true;
    }

    private List<String> getItems() {
        SelectItem.clear();
        List<String> list = new ArrayList<>();
        for (Item item : player.getInventory().getContents().values()) {
            list.add(message.getSon(MainKey, FormKey, ItemKey, new Object[]{player.getName(), myPlayer.getMoney(), item.hasCustomName() ? item.getCustomName() : WinfxkLib.getMain().getItemlist().getName(item), item.getId(), item.getDamage()}));
            SelectItem.add(item);
        }
        return list;
    }

    private boolean addLeaveWord() {
        CustomForm form = new CustomForm(getID(), getTitle());
        form.addLabel(getContent());
        form.addInput(getString("LeaveWordTitle"), LeaveWordTitle, getString("LeaveWordTitle"));
        form.addInput(getString("LeaveWordContent"), LeaveWordContent, getString("LeaveWordContent"));
        form.addInput(getString("Recipients"), Recipients == null ? "" : Recipients, getString("Recipients"));
        if (isOnline) form.addDropdown(getString("RecipientsDropdown"), getPlayerlist());
        form.show(player, (a, b) -> {
            FormResponseCustom data = (FormResponseCustom) b;
            String s = data.getInputResponse(2);
            if (s == null || s.isEmpty()) {
                sendMessage(getString("LeaveWordContentIsNull"));
                return addLeaveWord();
            }
            LeaveWordContent = s;
            s = data.getInputResponse(1);
            if (s == null || s.isEmpty())
                s = LeaveWordContent;
            LeaveWordTitle = s.length() > 10 ? s.substring(0, 10) : s;
            s = data.getInputResponse(3);
            if (s == null || s.isEmpty()) {
                if (isOnline)
                    s = data.getDropdownResponse(4).getElementContent();
                else {
                    sendMessage(getString("PlayerNull"));
                    return addLeaveWord();
                }
            }
            Recipients = s;
            return true;
        }, (a, b) -> Succeed ? Deduct() | sendMessage("Succeed") | LeaveWord.addLeaveWord(Recipients, LeaveWordTitle, LeaveWordContent, Items, Economys) : MakeForm());
        return true;
    }

    private boolean Deduct() {
        for (Item item : Items)
            player.getInventory().removeItem(item);
        for (LeaveWord.Economy economy : Economys)
            economy.getEconomy().reduceMoney(player, economy.getMoney());
        return true;
    }

    private List<String> getPlayerlist() {
        isOnline = false;
        List<String> list = new ArrayList<>();
        for (Player player : Server.getInstance().getOnlinePlayers().values())
            if (player.isOnline() && !player.getName().equals(this.player.getName())) {
                list.add(player.getName());
                isOnline = true;
            }
        return list;
    }
}