package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.plugin.PluginBase;
import cn.winfxk.nukkit.winfxklib.money.MyEconomy;
import cn.winfxk.nukkit.winfxklib.tool.Config;
import cn.winfxk.nukkit.winfxklib.tool.Update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class MyBase extends PluginBase {
    public File ConfigDir;
    protected Config BaseConfig;
    protected File BaseConfigFile;
    private List<String> BlockEconomys;

    public abstract File getConfigDir();

    /**
     * 获取插奸可用的经济支持<br/>
     * <b>注：</b><i>这个仅仅是插件级别，不会影响其他插件支持<i/>
     *
     * @return
     */
    public List<MyEconomy> getMyEconomys() {
        List<MyEconomy> list = new ArrayList<>();
        for (MyEconomy economy : WinfxkLib.getEconomys()) {
            if (BlockEconomys.contains(economy.getEconomyName())) continue;
            list.add(economy);
        }
        return list;
    }

    /**
     * 获取当前插件可用的经济支持
     *
     * @param s
     * @return
     */
    public MyEconomy getMyEconomy(String s) {
        if (BlockEconomys.contains(s)) return null;
        for (MyEconomy economy : WinfxkLib.getEconomys())
            if (s.equals(economy.getEconomyName())) return economy;
        return null;
    }

    @Deprecated
    @Override
    public cn.nukkit.utils.Config getConfig() {
        return null;
    }

    /**
     * 用于存放一些局部数据<br/>
     * <b>注：</b><i>这个仅仅是插件级别，不会影响其他插件支持<i/>
     *
     * @return
     */
    public Config getBaseConfig() {
        return BaseConfig;
    }

    /**
     * 获取插奸不可用的经济支持<br/>
     * <b>注：</b><i>这个仅仅是插件级别，不会影响其他插件支持<i/>
     *
     * @return
     */
    public List<String> getBlockEconomys() {
        return BlockEconomys;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        new Update(this).start();
        BaseConfig = new Config(BaseConfigFile = new File(getConfigDir(), "Economylist.yml"));
        BlockEconomys = BaseConfig.containsKey("BlockEconomys") ? BaseConfig.getList("BlockEconomys", new ArrayList<>()) : new ArrayList<>();
    }
}
