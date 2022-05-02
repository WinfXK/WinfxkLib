package cn.winfxk.nukkit.winfxklib.cmd;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.winfxk.nukkit.winfxklib.MyPlayer;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.module.leave_word.LeaveWord;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.Locale;

public class PlayerCommand extends MyCommand {
    public PlayerCommand() {
        super("admin-" + lib.getName().toLowerCase(Locale.ROOT), msg.getSun("Command", "PlayerCommand", "Description"), msg.getSun("Command", "PlayerCommand", "usageMessage"), Tool.getArray(config.getList("PlayerCommand"), new String[]{}));
        commandParameters.clear();
        commandParameters.put(getString("Help"), new CommandParameter[]{new CommandParameter(getString("Help"), false, new String[]{"help", "h"})});
        commandParameters.put(getString("LeaveWord"), new CommandParameter[]{new CommandParameter(getString("LeaveWord"), false, new String[]{"leaveword", "lw"})});
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission(PlayerPermission)) {
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
        MyPlayer myPlayer = WinfxkLib.getMyPlayer(sender.getName());
        switch (mainKey.toLowerCase(Locale.ROOT)) {
            case "leaveword":
            case "lw":
                return myPlayer.showForm(new LeaveWord(myPlayer.getPlayer(), null, true));
            default:
                sender.sendMessage(Tool.getCommandHelp(this));
        }
        return true;
    }
}
