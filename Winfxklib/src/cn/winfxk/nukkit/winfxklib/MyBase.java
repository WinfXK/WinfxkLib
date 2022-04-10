package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.plugin.PluginBase;

import java.io.File;

public abstract class MyBase extends PluginBase {
    public File ConfigDir;
    public abstract File getConfigDir();
}
