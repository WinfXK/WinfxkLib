package cn.winfxk.nukkit.winfxklib.cmd;

import cn.nukkit.command.Command;
import cn.winfxk.nukkit.winfxklib.Message;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.tool.Config;

import java.io.File;

public abstract class MyCommand extends Command {
    protected static WinfxkLib lib = WinfxkLib.getMain();
    protected static final Message msg = WinfxkLib.getMessage();
    protected static final File file = new File(WinfxkLib.getMain().getConfigDir(), WinfxkLib.CommandFileName);
    protected static final Config config = new Config(file);
    protected static final String AdminPermission = "WinfxkLib.Command.Admin";
    protected static final String PlayerPermission = "WinfxkLib.Command.Player";
    protected String CommandKey;

    public MyCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        CommandKey = getClass().getSimpleName();
    }

    protected String getString(String Key) {
        return msg.getSun("Command", CommandKey, Key);
    }
}
