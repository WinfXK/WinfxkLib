package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.winfxk.nukkit.winfxklib.form.Dispose;
import cn.winfxk.nukkit.winfxklib.money.EasyEconomy;
import cn.winfxk.nukkit.winfxklib.money.EconomyAPI;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import cn.winfxk.nukkit.winfxklib.tool.Config;
import cn.winfxk.nukkit.winfxklib.tool.Itemlist;
import cn.winfxk.nukkit.winfxklib.tool.MyMap;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WinfxkLib extends PluginBase implements Listener {
    private static final Instant loadTime = Instant.now();
    public static final String MsgConfigName = "Message.yml";
    public static final String CommandFileName = "Command.yml";
    public static final String ConfigFileName = "Config.yml";
    public static final String ItemListFileName = "Itemlist.yml";
    public static final String[] Meta = {MsgConfigName, ConfigFileName, ItemListFileName, CommandFileName};
    private static final MyMap<String, MyEconomy> Economys = new MyMap<>();
    protected static MyMap<String, MyPlayer> MyPlayers = new MyMap<>();
    private static File ConfigDir;
    protected static MyEconomy economy;
    protected static WinfxkLib main;
    private static Config config;
    private static Message message;
    private static List<String> BlacklistEconomy;
    private Itemlist itemlist;

    public static MyMap<String, MyPlayer> getMyPlayers() {
        return MyPlayers;
    }

    public static Config getMyConfig() {
        return config;
    }

    public Itemlist getItemlist() {
        return itemlist;
    }

    public static File getConfigDir() {
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
        new Check(this).start();
        config = new Config(new File(ConfigDir, ConfigFileName));
        Economys.put("EasyEconomy", new EasyEconomy());
        BlacklistEconomy = config.getList("BlacklistEconomy", new ArrayList<>());
        Plugin plugin = getServer().getPluginManager().getPlugin("EconomyAPI");
        if (plugin != null)
            addEconomy(new EconomyAPI(this));
        setEconomy(config.getString("Economy"));
        message = new Message(this);
        itemlist = new Itemlist();
        getServer().getCommandMap().register(getFullName() + "-Command", new AdminCommand(this));
        getServer().getPluginManager().registerEvents(new Dispose(this), this);
        super.onEnable();
        getLogger().info(message.getMessage("插件启动", "{loadTime}", (float) Duration.between(loadTime, Instant.now()).toMillis() + "ms"));
    }

    @Override
    public void onLoad() {
        main = this;
        ConfigDir = getDataFolder();
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
            economy = getEconomy("EasyEconomy");
            return false;
        }
        economy = Economys.get(EconomyID);
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
        return Economys.get(EconomyID);
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
        return Economys.containsKey(EconomyID);
    }

    /**
     * 获取默认货币
     *
     * @return
     */
    public static MyEconomy getEconomy() {
        return economy;
    }

    public static MyPlayer getMyPlayer(String player) {
        return MyPlayers.get(player);
    }

    public static MyPlayer getMyPlayer(Player player) {
        return MyPlayers.get(player.getName());
    }
}
