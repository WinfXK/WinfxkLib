package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.winfxk.nukkit.winfxklib.module.PostData;
import cn.winfxk.nukkit.winfxklib.tool.Config;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.io.File;
import java.util.Locale;

public class AdminCommand extends Command {
    private WinfxkLib lib;
    private static final Message msg = WinfxkLib.getMessage();
    private static final File file = new File(WinfxkLib.getConfigDir(), WinfxkLib.CommandFileName);
    private static final Config config = new Config(file);
    private static final String Permission = "WinfxkLib.Command.Admin";

    public AdminCommand(WinfxkLib lib) {
        super(lib.getName().toLowerCase(Locale.ROOT), msg.getSun("Command", "AdminCommand", "Description"), msg.getSun("Command", "AdminCommand", "usageMessage"), Tool.getArray(config.getList("AdminCommand"), new String[]{}));
        this.lib = lib;
        commandParameters.clear();
        commandParameters.put(getString("Help"), new CommandParameter[]{new CommandParameter(getString("Help"), false, new String[]{"help"})});
        commandParameters.put(getString("Add"), new CommandParameter[]{new CommandParameter(getString("Add"), false, new String[]{"data", "数据"})});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission(Permission)) {
            sender.sendMessage(getString("无权执行"));
            return true;
        }
        if (!sender.isPlayer()) {
            sender.sendMessage(getString("NotPlayer"));
            return true;
        }
        String mainKey;
        if (args == null || args.length <= 0 || (mainKey = args[0]) == null || mainKey.isEmpty()) {
            sender.sendMessage(Tool.getCommandHelp(this));
            return true;
        }
        String minorKey;
        switch (mainKey.toLowerCase(Locale.ROOT)) {
            case "data":
            case "数据":
                return MyPlayer.showForm(sender.getName(), new PostData((Player) sender, null, true));
            default:
                sender.sendMessage(Tool.getCommandHelp(this));
        }
        return true;
    }

    private String getString(String Key) {
        return msg.getSun("Command", "AdminCommand", Key);
    }
}
