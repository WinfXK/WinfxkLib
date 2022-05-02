package cn.winfxk.nukkit.winfxklib.module.leave_word;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;
import cn.winfxk.nukkit.winfxklib.tool.Config;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class LeaveWordForm extends BaseForm {
    protected final static File file = new File(main.getConfigDir(), WinfxkLib.LeaveWordFileName);
    protected final static String Permission = "WinfxkLib.use.LeaveWord";
    protected final static Config config = new Config(file);
    protected final static String[] LeaveWordkey = {"{Player}", "{Money}", "{LeaveWordCount}", "{MaxLeaveWordCount}"};
    protected final static String MainKey = "LeaveWord";
    /**
     * 未读
     */
    protected Map<String, Object> Unread;
    /**
     * 已读
     */
    protected Map<String, Object> MarkRead;
    protected Map<String, Object> LeaveWordMap;
    protected boolean UnrealLeaveWord;
    protected int LeaveWordCount, MaxLeaveWordCount;

    public LeaveWordForm(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
        Object obj = config.get(player.getName());
        if (!(obj instanceof Map)) {
            MarkRead = new HashMap<>();
            Unread = new HashMap<>();
            LeaveWordMap = new HashMap<>();
        } else {
            LeaveWordMap = (Map<String, Object>) obj;
            obj = LeaveWordMap.get("MarkRead");
            MarkRead = obj instanceof Map ? (Map<String, Object>) obj : new HashMap<>();
            obj = LeaveWordMap.get("Unread");
            Unread = obj instanceof Map ? (Map<String, Object>) obj : new HashMap<>();
        }
        UnrealLeaveWord = WinfxkLib.getconfig().getBoolean("UnrealLeaveWord", true);
        LeaveWordCount = Tool.ObjToInt(LeaveWordMap.get("LeaveWordCount"), 10);
        MaxLeaveWordCount = WinfxkLib.getconfig().getInt("LeaveWordAstrict", 10);
        K = LeaveWordkey;
        setD(player.getName(), myPlayer.getMoney(), LeaveWordCount, MaxLeaveWordCount);
    }

    public static Config getConfig() {
        return config;
    }

    public static File getFile() {
        return file;
    }

    @Override
    protected String getString(String Key) {
        return message.getSun(MainKey, FormKey, Key, this);
    }

    @Override
    protected String getConfirm() {
        return message.getSon(MainKey, "Confirm", this);
    }

    @Override
    protected String getString(String Key, String[] Keys, Object[] Datas) {
        return message.getSun(MainKey, FormKey, Key,
                getArrayKeys(Keys, K),
                Tool.Arrays(Datas, getD()), player);
    }

    protected String NotLeaveWord() {
        return getString("NotLeaveWord");
    }
}
