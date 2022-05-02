package cn.winfxk.nukkit.winfxklib;

import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Utils;
import cn.winfxk.nukkit.winfxklib.tool.Config;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 配置文件操作类
 *
 * @author Winfxk
 * @Createdate 2021/07/31 18:34:33
 */
public class Check {
    private final MyBase kis;
    private int index = 0;
    public static Check check;
    /**
     * 会与预留文件做参数匹配是否一致
     */
    private String[] Meta;
    /**
     * 想要存在的文件夹
     */
    private String[] Mkdir;
    /**
     * 只需判断文件是否存在，如果不存在写入一份默认文件不进行参数检查
     */
    private String[] Load;
    private File ConfigDir;

    public Check(MyBase kis, String[] Meta, String[] Mkdir, String[] Load) {
        this.kis = kis;
        check = this;
        this.Meta = Meta;
        this.Mkdir = Mkdir;
        this.Load = Load;
    }

    public Check(MyBase kis, String[] Meta, String[] Mkdir) {
        this.kis = kis;
        check = this;
        this.Meta = Meta;
        this.Mkdir = Mkdir;
        Load = null;
    }

    /**
     * 导出所有自带语言文件
     *
     * @return
     */
    protected boolean Message() {
        File file;
        PluginLogger log = kis.getLogger();
        String lang = Tool.getLanguage();
        file = new File(kis.getConfigDir(), "language");
        if (!file.exists())
            file.mkdirs();
        try {
            JarFile localJarFile = new JarFile(new File(kis.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()));
            Enumeration<JarEntry> entries = localJarFile.entries();
            File Jf, JFB;
            String JN, JP, JE;
            File langFile = new File(kis.getDataFolder(), "language");
            if (!langFile.exists()) langFile.mkdirs();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry == null)
                    continue;
                JE = jarEntry.getName();
                Jf = new File(JE);
                JP = Jf.getParent();
                if (JP == null)
                    continue;
                JN = Jf.getName();
                if (JP.equals("language")) {
                    JFB = new File(langFile, JN);
                    if (!JFB.exists())
                        try {
                            Utils.writeFile(JFB, Utils.readFile(kis.getClass().getResourceAsStream("/language/" + JN)));
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.warning("Unable to get list of language files!");
                            localJarFile.close();
                            return false;
                        }
                }
            }
            localJarFile.close();
        } catch (IOException e2) {
            log.error("Unable to load language file");
            return false;
        }
        if (!file.exists())
            if (lang != null && getClass().getResource("/language/" + lang + ".yml") != null)
                try {
                    log.info("Writing to the default language:" + lang);
                    Utils.writeFile(WinfxkLib.getMessage().file, Utils.readFile(kis.getClass().getResourceAsStream("/language/" + lang + ".yml")));
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("§4The default language could not be initialized！");
                    try {
                        Utils.writeFile(WinfxkLib.getMessage().file, Utils.readFile(kis.getClass().getResourceAsStream("/res/Message.yml")));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        log.error("§4The default language could not be initialized！");
                        kis.setEnabled(false);
                        return false;
                    }
                }
            else
                try {
                    Utils.writeFile(WinfxkLib.getMessage().file, Utils.readFile(kis.getClass().getResourceAsStream("/res/Message.yml")));
                } catch (IOException e1) {
                    e1.printStackTrace();
                    log.error("§4The default language could not be initialized！");
                    kis.setEnabled(false);
                    return false;
                }
        return true;
    }

    /**
     * 检查文件是否与默认的格式有错误
     */
    public int start() {
        File file;
        if (Mkdir != null)
            for (String s : Mkdir) {
                file = new File(kis.getDataFolder(), s);
                if (!file.exists()) file.mkdirs();
            }
        Message();
        Config config;
        Map<String, Object> map;
        if (Meta != null)
            for (String string : Meta) {
                config = new Config(new File(kis.getConfigDir(), string));
                try {
                    map = Matc(Config.yaml.loadAs(kis.getClass().getResourceAsStream("/res/" + string), Map.class), config.getMap());
                    config.setAll(map);
                    config.save();
                } catch (Exception e) {
                    kis.getLogger().error("Unable to obtain resource: " + string);
                    continue;
                }
            }
        if (Load != null)
            for (String s : Load) {
                file = new File(kis.getConfigDir(), s);
                if (!file.exists()) {
                    try {
                        Utils.writeFile(file, Utils.readFile(kis.getClass().getResourceAsStream("/res/" + s)));
                    } catch (Exception e) {
                        kis.getLogger().error("Unable to obtain resource: " + s);
                        e.printStackTrace();
                    }
                }
            }
        return index;
    }

    /**
     * 匹配自定义Map和预设Map是否有差别 有的话覆盖写入
     *
     * @param root
     * @param data
     * @return
     */
    public Map<String, Object> Matc(Map<String, Object> root, Map<String, Object> data) {
        if (root == null)
            return data;
        String Key;
        Object obj, obj2;
        for (Map.Entry<String, Object> entry : root.entrySet()) {
            Key = Tool.objToString(entry.getKey());
            obj = entry.getValue();
            if (!data.containsKey(Key)) {
                data.put(Key, obj);
                index++;
                continue;
            }
            obj2 = data.get(Key);
            if (obj != null && obj2 != null && !obj.getClass().getName().equals(obj2.getClass().getName())) {
                data.put(Key, obj);
                index++;
                continue;
            }
            if (obj instanceof Map) {
                if (obj2 == null) {
                    data.put(Key, obj);
                    index++;
                    continue;
                }
                data.put(Key, Matc((Map<String, Object>) obj, obj2 instanceof Map ? (HashMap<String, Object>) obj2 : new HashMap<>()));
                continue;
            }
        }
        return data;
    }

}
