package cn.winfxk.nukkit.winfxklib.tool;

import cn.winfxk.nukkit.winfxklib.Message;
import cn.winfxk.nukkit.winfxklib.MyBase;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Update extends Thread {
    private static final String[] UpdateKey = {"{Name}", "{newName}", "{Version}", "{newVersion}", "{Size}", "{newSize}", "{Download}", "{UpdateMessage}"};
    private static final Message msg = WinfxkLib.getMessage();
    public static final String Server = "NukkitPluginUpdate.Winfxk.cn";
    private static final String MainKey = "Update";
    public static final int Port = 1998;
    private final MyBase main;

    public Update(MyBase base) {
        main = base;
    }

    @Override
    public void run() {
        super.run();
        try {
            Config config = new Config(new File(main.getDataFolder(), "Update.yml"));
            if (config.getBoolean("StopUpdate")) return;
            sleep(Tool.getRand(10, 10000));
            main.getLogger().info(msg.getSon(MainKey, "检查更新"));
            Map<String, Object> map = new HashMap<>();
            map.put("Name", main.getName());
            map.put("Version", main.getDescription().getVersion());
            map = get(map);
            if (map == null || map.size() < 1) {
                main.getLogger().warning(msg.getSon(MainKey, "连接失败"));
                return;
            }
            if (Tool.ObjToBool(map.get("Update"))) {
                File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
                main.getLogger().info(msg.getSon(MainKey, "发现更新", UpdateKey, new Object[]{main.getName(), map.get("Name"), main.getDescription().getVersion(), map.get("Version"), Tool.getSize(file),
                        Tool.getSize(Tool.objToLong(map.get("Size"))), map.get("Download"), msg.getText(map.get("UpdateMessage"))}));
                config.set("StopUpdate", false);
                config.set("更新时间", Tool.getDate() + " " + Tool.getTime());
                config.set("名称", map.get("Name"));
                config.set("版本", map.get("Version"));
                config.set("文件大小", Tool.getSize(Tool.objToLong(map.get("Size"))));
                config.set("下载地址", map.get("Download"));
                config.set("更新内容", msg.getText(map.get("UpdateMessage")));
                config.save();
                return;
            }
            main.getLogger().info(msg.getSon(MainKey, "暂无更新"));
        } catch (Exception e) {
            main.getLogger().warning(msg.getSon(MainKey, "更新异常", "{Error}", e.getMessage()));
            e.printStackTrace();
        }
    }

    public Map<String, Object> get(Map<String, Object> map) throws Exception {
        Socket socket = new Socket(Server, Port);
        DataInputStream Input = new DataInputStream(socket.getInputStream());
        DataOutputStream Output = new DataOutputStream(socket.getOutputStream());
        Output.writeUTF(MyMap.yaml.dump(map));
        String string = Input.readUTF();
        try {
            Input.close();
            Output.close();
            socket.close();
        } catch (Exception e) {
        }
        return MyMap.yaml.loadAs(string, Map.class);
    }
}
