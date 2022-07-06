package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.plugin.Plugin;
import cn.winfxk.nukkit.winfxklib.cmd.AdminCommand;
import cn.winfxk.nukkit.winfxklib.cmd.PlayerCommand;
import cn.winfxk.nukkit.winfxklib.form.FormID;
import cn.winfxk.nukkit.winfxklib.module.LeaveWord;
import cn.winfxk.nukkit.winfxklib.money.EasyEconomy;
import cn.winfxk.nukkit.winfxklib.money.EconomyAPI;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import cn.winfxk.nukkit.winfxklib.tool.*;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WinfxkLib extends MyBase implements Listener {
    private static final String[] newEconomyKey = {"{NewEconomyName}", "{NewMoneyName}"};
    public static final String MsgConfigName = "Message.yml";
    public static final String CommandFileName = "Command.yml";
    public static final String ConfigFileName = "Config.yml";
    public static final String ItemListFileName = "Itemlist.yml";
    public static final String LeaveWordFileName = "LeaveWord.yml";
    public static final String EffectlistFileName = "Effectlist.yml";
    public static final String EnchantListFileName = "Enchantlist.yml";
    public static final String[] Meta = {MsgConfigName, ConfigFileName, ItemListFileName, CommandFileName, EnchantListFileName, EffectlistFileName};
    private static final MyMap<String, MyEconomy> Economys = new MyMap<>();
    protected static MyMap<String, MyPlayer> MyPlayers = new MyMap<>();
    private static File ConfigDir;
    protected static MyEconomy economy;
    protected static WinfxkLib main;
    private static Config config;
    private static Message message;
    private static List<String> BlacklistEconomy;
    private Itemlist itemlist;
    private Effectlist effectlist;
    private Enchantlist enchantlist;
    private static FormID formID;

    public static List<String> getBlacklistEconomy() {
        return BlacklistEconomy;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        WinfxkLib.getMyPlayers().put(player.getName(), new MyPlayer(player));
        LeaveWord.PlayerJoinEvent(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        MyPlayers.remove(player.getName());
    }

    @EventHandler
    public void onFormResponded(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        MyPlayer myPlayer = WinfxkLib.getMyPlayer(player);
        int ID = event.getFormID();
        if (!formID.hasID(ID)) return;
        try {
            if (event.wasClosed())
                if (myPlayer.fun != null)
                    if (myPlayer.fun.wasClosed(player)) return;
            FormResponse data = event.getResponse();
            if (data == null) return;
            if (myPlayer.fun != null)
                myPlayer.fun.resolveResponded(player, data);
            if (myPlayer.form != null) myPlayer.form.dispose(data);
        } catch (Exception e) {
            player.sendMessage(getName() + "出现问题！请联系服务器管理员。\nErrorCode: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<MyEconomy> getEconomys() {
        return new ArrayList<>(Economys.values());
    }

    public static Config getconfig() {
        return config;
    }

    public static MyMap<String, MyPlayer> getMyPlayers() {
        return MyPlayers;
    }

    public static Config getMyConfig() {
        return config;
    }

    public Itemlist getItemlist() {
        return itemlist;
    }

    @Override
    public File getConfigDir() {
        return ConfigDir;
    }

    public static Message getMessage() {
        return message;
    }

    public static WinfxkLib getMain() {
        return main;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info(message.getMessage("插件关闭", "{loadTime}", Tool.getTimeBy((float) Duration.between(loadTime, Instant.now()).toMillis() / 1000)));
    }

    @Override
    public void onEnable() {
        loadTime = Instant.now();
        ConfigDir = getDataFolder();
        new Check(this, Meta, null).start();
        config = new Config(new File(ConfigDir, ConfigFileName));
        Economys.put("EasyEconomy", economy = new EasyEconomy());
        getLogger().info("§4We will increase basic Economy support");
        BlacklistEconomy = config.getList("BlacklistEconomy", new ArrayList<>());
        Plugin plugin = getServer().getPluginManager().getPlugin("EconomyAPI");
        message = new Message(this, new File(getConfigDir(), MsgConfigName));
        if (plugin != null)
            addEconomy(new EconomyAPI(this));
        setEconomy(config.getString("Economy"));
        itemlist = new Itemlist();
        effectlist = new Effectlist();
        enchantlist = new Enchantlist();
        formID = new FormID();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getCommandMap().register(getFullName() + "-AdminCommand", new AdminCommand());
        getServer().getCommandMap().register(getFullName() + "-PlayerCommand", new PlayerCommand());
        super.onEnable();
        getLogger().info(message.getMessage("插件启动", "{loadTime}", (float) Duration.between(loadTime, Instant.now()).toMillis() + "ms"));
    }

    public Enchantlist getEnchantlist() {
        return enchantlist;
    }

    public Effectlist getEffectlist() {
        return effectlist;
    }

    @Override
    public void onLoad() {
        main = this;
        super.onLoad();
    }

    /**
     * 设置一个默认经济支持<Br>如果设置失败将会使用自带基本经济支持
     *
     * @param EconomyID 默认的经济支持ID
     * @return 设置是否成功
     */
    public static boolean setEconomy(String EconomyID) {
        if (BlacklistEconomy.contains(EconomyID)) return false;
        if (!Economys.containsKey(EconomyID)) {
            if (economy != null) return false;
            economy = EasyEconomy.MyEconomy;
            return false;
        }
        economy = Economys.get(EconomyID);
        message.load();
        return true;
    }

    /**
     * 根据币种ID返回一个币种支持
     *
     * @param EconomyID 币种ID
     * @return 币种API
     */
    public static MyEconomy getEconomy(String EconomyID) {
        if (BlacklistEconomy.contains(EconomyID)) return null;
        return Economys.getOrDefault(EconomyID, null);
    }

    /**
     * 移除一个币种支持<br>
     * <b>注：<b/>无法删除自带的基本经济支持
     *
     * @param EconomyID 币种ID
     * @return 是否删除成功
     */
    public static boolean removeEconomy(String EconomyID) {
        if (EconomyID.equals("EasyEconomy")) return false;
        if (Economys.containsKey(EconomyID)) {
            Economys.remove(EconomyID);
            return !Economys.containsKey(EconomyID);
        }
        return false;
    }

    /**
     * 添加一个币种支持
     *
     * @param Economy 币种对象
     */
    public static boolean addEconomy(MyEconomy Economy) {
        if (BlacklistEconomy.contains(Economy.getEconomyName()) || Economy.getEconomyName().equals("EasyEconomy"))
            return false;
        Economys.put(Economy.getEconomyName(), Economy);
        config.set("Economylist", new ArrayList<>(Economys.keySet())).save();
        WinfxkLib.main.getLogger().info(message.getMessage("增加经济支持", newEconomyKey, new Object[]{Economy.getEconomyName(), Economy.getMoneyName()}));
        return Economys.containsKey(Economy.getEconomyName());
    }

    /**
     * 添加一个币种支持
     *
     * @param EconomyID 币种ID
     * @param Economy   币种对象
     */
    public static boolean addEconomy(String EconomyID, MyEconomy Economy) {
        if (BlacklistEconomy.contains(EconomyID) || EconomyID.equals("EasyEconomy")) return false;
        Economys.put(EconomyID, Economy);
        config.set("Economylist", new ArrayList<>(Economys.keySet())).save();
        WinfxkLib.main.getLogger().info(message.getMessage("增加经济支持", newEconomyKey, new Object[]{Economy.getEconomyName(), Economy.getMoneyName()}));
        return Economys.containsKey(EconomyID);
    }

    /**
     * 获取默认货币
     *
     * @return
     */
    public static MyEconomy getEconomy() {
        if (economy == null) {
            List<String> list = new ArrayList<>(Economys.keySet());
            for (String s : list)
                if (!BlacklistEconomy.contains(s)) economy = Economys.get(s);
        }
        return economy == null ? economy = EasyEconomy.MyEconomy : economy;
    }

    public static MyPlayer getMyPlayer(String player) {
        return MyPlayers.get(player);
    }

    public static MyPlayer getMyPlayer(Player player) {
        return MyPlayers.get(player.getName());
    }
}
