package cn.winfxk.nukkit.winfxklib.form;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.nukkit.winfxklib.MyPlayer;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;

public class Dispose implements Listener {
    private final WinfxkLib lib;
    private static final FormID formID = new FormID();

    public Dispose(WinfxkLib lib) {
        this.lib = lib;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        WinfxkLib.getMyPlayers().put(player.getName(), new MyPlayer(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        WinfxkLib.getMyPlayers().remove(player.getName());
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
            player.sendMessage(lib.getName() + "出现问题！请联系服务器管理员。\nErrorCode: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
