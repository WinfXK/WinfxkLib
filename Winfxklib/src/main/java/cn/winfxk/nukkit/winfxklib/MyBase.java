package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.plugin.PluginBase;
import cn.winfxk.nukkit.winfxklib.tool.Update;

import java.io.File;

public abstract class MyBase extends PluginBase {
    public File ConfigDir;
    public abstract File getConfigDir();

    @Override
    public void onEnable() {
        super.onEnable();
        new Update(this).start();
    }
}
