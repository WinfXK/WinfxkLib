package com.winfxk.winfxklia.config;

import android.util.Log;
import com.alibaba.fastjson2.JSONObject;
import com.winfxk.winfxklia.Tool;
import com.winfxk.winfxklia.Utils;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: Winfx
 * date: 2024/5/3 17:57
 */
public class Config {
    private final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
    private static final DumperOptions dumperOptions = new DumperOptions();
    public static final String tag = "Config";
    private static final int Passwd = 1998;
    public final static Yaml yaml;
    private final Type type;
    private final File file;

    static {
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(dumperOptions);
    }

    /**
     * 使用指定的文件和默认的配置类型（JSON）构造Config对象。
     *
     * @param file 配置文件的File对象。
     */
    public Config(File file) {
        this(file, Type.Json);
    }

    /**
     * 使用指定的文件和配置类型构造Config对象。
     *
     * @param file 配置文件的File对象。
     * @param type 配置类型。
     */
    public Config(File file, Type type) {
        this(file, type, null);
    }

    /**
     * 使用指定的文件和预设数据构造Config对象。
     *
     * @param file 配置文件的File对象。
     * @param data 预设的配置数据。
     */
    public Config(File file, Map<String, Object> data) {
        this(file, Type.Json, data);
    }

    /**
     * 使用指定的文件、配置类型和预设数据构造Config对象。
     *
     * @param file 配置文件的File对象。
     * @param type 配置类型。
     * @param data 预设的配置数据。
     */
    public Config(File file, Type type, Map<String, Object> data) {
        if (file == null) throw new IllegalArgumentException("file cannot be null");
        if (type == null) type = Type.Json;
        this.file = file;
        this.type = type;
        boolean isReload = reload();
        if (!isReload && data != null && !data.isEmpty()) this.data.putAll(data);
    }

    public Map<String, Object> getMap(String key) {
        return getMap(key, null);
    }

    public Map<String, Object> getMap(String key, Map<String, Object> defaultValue) {
        Object obj = get(key);
        if (!(obj instanceof Map)) return defaultValue;
        try {
            return (Map<String, Object>) obj;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public List<String> getListOfString(String key) {
        List<String> list = new ArrayList<>();
        List<Object> objects = getList(key, null);
        if (objects == null) return null;
        for (Object obj : objects) list.add(String.valueOf(obj));
        return list;
    }

    public List<String> getListOfString(String key, List<String> defaultValue) {
        List<String> list = new ArrayList<>();
        List<Object> objects = getList(key, null);
        if (objects == null) return defaultValue;
        for (Object obj : objects) list.add(String.valueOf(obj));
        return list;
    }

    public <T> List<T> getListOf(String key, List<T> defaultValue) {
        Object obj = get(key);
        if (!(obj instanceof List)) return defaultValue;
        try {
            return (List<T>) obj;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public List<Object> getList(String key) {
        return getList(key, null);
    }

    public List<Object> getList(String key, List<Object> defaultValue) {
        Object obj = get(key);
        if (!(obj instanceof List)) return defaultValue;
        try {
            return (List<Object>) obj;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        Object obj = get(key);
        if (obj == null) return defaultValue;
        String value = getString(key, null);
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Object obj = get(key);
        if (obj == null) return defaultValue;
        String value = getString(key, null);
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        Object obj = get(key);
        if (obj == null) return defaultValue;
        String value = getString(key, null);
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double defaultValue) {
        Object obj = get(key);
        if (obj == null) return defaultValue;
        String value = getString(key, null);
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        Object obj = get(key);
        if (obj == null) return defaultValue;
        return String.valueOf(obj);
    }

    /**
     * 获取指定键对应的对象，如果键不存在或值为null，则返回null。
     *
     * @param key 要获取的键。
     * @return 键对应的对象或null。
     */
    public Object get(String key) {
        return data.getOrDefault(key, null);
    }

    /**
     * 设置指定键的值，并返回Config对象自身，以支持链式调用。
     *
     * @param key   要设置的键。
     * @param value 要设置的值。
     * @return Config
     */
    public Config set(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Config clear() {
        data.clear();
        return this;
    }

    public Config setAll(Map<String, Object> map) {
        return setAll(map, false);
    }

    public Config setAll(Map<String, Object> map, boolean isClear) {
        if (isClear) data.clear();
        data.putAll(map);
        return this;
    }

    /**
     * 将当前的配置数据保存到配置文件。
     *
     * @return 是否成功保存。
     */
    public synchronized boolean save() {
        try {
            if (!file.exists() || !file.isFile()) if (!file.createNewFile()) {
                Log.e(tag, "创建配置文件" + file + "时出现异常！");
                return false;
            }
            String content;
            switch (type) {
                case Text:
                    content = saveText();
                    break;
                case Yaml:
                    content = yaml.dump(data);
                    break;
                case Hax:
                    content = saveToHax();
                    break;
                case Int:
                    content = saveToInt();
                    break;
                case Json:
                default:
                    content = JSONObject.toJSONString(data);
            }
            Utils.writeFile(file, content);
        } catch (Exception e) {
            Log.e(tag, "创建配置文件" + file + "时出现异常！", e);
            return false;
        }
        return true;
    }

    private String saveText() {
        String json = JSONObject.toJSONString(data);
        StringBuilder content = new StringBuilder();
        int length = json.length();
        for (int i = 0; i < length; i++)
            content.append((char) (((int) json.charAt(i)) + Passwd));
        return content.toString();
    }

    private String saveToHax() {
        String json = JSONObject.toJSONString(data);
        StringBuilder content = new StringBuilder();
        int length = json.length();
        for (int i = 0; i < length; i++)
            content.append(Tool.CompressNumber(((int) json.charAt(i)) + Passwd)).append("/");
        return content.toString();
    }

    private String saveToInt() {
        String json = JSONObject.toJSONString(data);
        StringBuilder content = new StringBuilder();
        int length = json.length();
        for (int i = 0; i < length; i++)
            content.append(((int) json.charAt(i)) + Passwd).append(".");
        return content.toString();
    }

    /**
     * 从配置文件重新加载配置数据。
     *
     * @return 是否成功重新加载。
     */
    public synchronized boolean reload() {
        data.clear();
        if (!file.exists() || !file.isFile()) return false;
        try {
            Map<String, Object> map;
            String content = Utils.readFile(file);
            if (content.isEmpty()) return false;
            switch (type) {
                case Text:
                    map = readText(content);
                    break;
                case Yaml:
                    map = yaml.loadAs(content, Map.class);
                    break;
                case Int:
                    map = JSONObject.parseObject(readInt(content));
                    break;
                case Hax:
                    map = JSONObject.parseObject(readHax(content));
                    break;
                case Json:
                default:
                    map = JSONObject.parseObject(content);
            }
            if (map.isEmpty()) return false;
            data.putAll(map);
        } catch (Exception e) {
            Log.e(tag, "加载配置文件" + file + "时出现异常！", e);
            return false;
        }
        return true;
    }

    private Map<String, Object> readText(String content) {
        int length = content.length();
        StringBuilder json = new StringBuilder();
        for (int i = 0; i < length; i++) {
            json.append((char) (content.charAt(i) - Passwd));
        }
        return JSONObject.parseObject(json.toString());
    }


    /**
     * 已整数的方式加载数据
     *
     * @return
     */
    private String readInt(String content) {
        String[] strings = content.split("\\.");
        StringBuilder json = new StringBuilder();
        for (String s : strings) {
            if (s == null || s.isEmpty()) continue;
            json.append((char) (Integer.parseInt(s) - Passwd));
        }
        return json.toString();
    }

    /**
     * 已二进制的方式加载数据
     *
     * @return
     */
    private String readHax(String content) {
        String[] strings = content.split("/");
        StringBuilder json = new StringBuilder();
        for (String s : strings) {
            if (s == null || s.isEmpty()) continue;
            json.append((char) (Tool.UnCompressNumber(s) - Passwd));
        }
        return json.toString();
    }

    /**
     * 获取配置文件的File对象。
     *
     * @return 配置文件的File对象。
     */
    public File getFile() {
        return file;
    }

    /**
     * 获取配置类型。
     *
     * @return 配置类型。
     */
    public Type getType() {
        return type;
    }

    public ConcurrentHashMap<String, Object> getData() {
        return data;
    }

    @NotNull
    @Override
    public String toString() {
        return "Config{" + "data=" + data + ", type=" + type + ", file=" + file + '}';
    }
}
