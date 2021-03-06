package cn.winfxk.nukkit.winfxklib.tool;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.command.Command;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Utils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Winfxk
 */
public class Tool implements X509TrustManager, HostnameVerifier {
    private static final String colorKeyString = "123456789abcdef";
    private static final String randString = "-+abcdefghijklmnopqrstuvwxyz_";
    private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
    private final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '+', '/'};
    private static final String[] FileSizeUnit = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};

    public static <V, T> V[] getArray(List<T> list, V[] v) {
        if (list != null)
            try {
                return list.toArray(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return v;
    }
    /**
     * ??????????????????
     *
     * @param file ??????????????????????????????
     * @return
     */
    public static String getSize(File file) {
        return getSize(file.length());
    }

    /**
     * ??????????????????
     *
     * @param size
     * @return
     */
    public static String getSize(long size) {
        if (size < 1024)
            return size + "B";
        int index = 1;
        float Size = size / (float) 1024;
        while (Size > 1024) {
            index++;
            Size /= 1024;
        }
        return Tool.Double2(Size) + FileSizeUnit[Math.min(index, FileSizeUnit.length - 1)];
    }

    /**
     * ??????????????????
     *
     * @param FileName
     * @return
     * @throws IOException
     */
    public static InputStream getResourceStream(String FileName) throws IOException {
        return Tool.class.getResourceAsStream("/res/" + FileName);
    }

    /**
     * ??????????????????
     *
     * @param FileName
     * @return
     * @throws IOException
     */
    public static String getResource(String FileName) throws IOException {
        return Utils.readFile(Tool.class.getResourceAsStream("/res/" + FileName));
    }

    /**
     * ??????????????????Command?????????Help??????
     *
     * @return
     */
    public static String getCommandHelp(Command... commands) {
        String string = "";
        for (Command command : commands)
            for (Entry<String, CommandParameter[]> entry : command.getCommandParameters().entrySet())
                string += getCommandHelp(command, entry.getValue(), entry.getKey()) + "\n";
        return string;
    }

    /**
     * ??????????????????Command?????????Help??????
     *
     * @param command
     * @return
     */
    public static String getCommandHelp(Command command, CommandParameter[] cp, String Key) {
        String string = "", cmd, zy;
        CommandParameter cs;
        if (cp.length > 0) {
            cmd = command.getName();
            for (String s : command.getAliases())
                if (String_length(s) < String_length(cmd))
                    cmd = s;
            string += "??f/" + cmd + " ??b";
            for (int i = 0; i < cp.length; i++) {
                cs = cp[i];
                if (cs.type.equals(CommandParamType.RAWTEXT) && cs.enumData != null && cs.enumData.getValues() != null) {
                    zy = cs.enumData.getValues().get(0);
                } else {
                    zy = cs.name;
                }
                if (cs.enumData != null)
                    for (String s : cs.enumData.getValues())
                        if (String_length(s) < String_length(zy))
                            zy = s;
                string += i == 0 ? zy : "??6 " + (cp[i].type.equals(CommandParamType.RAWTEXT) ? (cs.enumData != null && cs.enumData.getValues() != null) ? cp[i].enumData.getValues().get(0) : cs.name : cp[i].name);
            }
            string += "??f??? ??9" + Key;
        }
        return string;
    }

    /**
     * ??????????????????
     *
     * @param Level
     * @param x
     * @param y
     * @param z
     * @param list
     */
    public static void setSign(String Level, double x, double y, double z, String... list) {
        Level level = Server.getInstance().getLevelByName(Level);
        if (level == null)
            return;
        setSign(new Location(x, y, z, level), list);
    }

    /**
     * ??????????????????
     *
     * @param location
     * @param list
     */
    public static void setSign(Location location, String... list) {
        setSign(location.level.getBlock(location), list);
    }

    /**
     * ??????????????????
     *
     * @param Level
     * @param vector3
     * @param list
     */
    public static void setSign(String Level, Vector3 vector3, String... list) {
        Level level = Server.getInstance().getLevelByName(Level);
        if (level == null)
            return;
        setSign(new Location(vector3.x, vector3.y, vector3.z, level), list);
    }

    /**
     * ??????????????????
     *
     * @param level
     * @param vector3
     * @param list
     */
    public static void setSign(Level level, Vector3 vector3, String... list) {
        setSign(new Location(vector3.x, vector3.y, vector3.z, level), list);
    }

    /**
     * ??????????????????
     *
     * @param block
     * @param list
     */
    public static void setSign(Block block, String... list) {
        if (list == null || block == null)
            return;
        BlockEntity blockEntity = block.getLevel().getBlockEntity(block);
        BlockEntitySign sign = blockEntity instanceof BlockEntitySign ? (BlockEntitySign) blockEntity
                : new BlockEntitySign(block.getLevel().getChunk(block.getFloorX() >> 4, block.getFloorZ() >> 4), BlockEntity.getDefaultCompound(block, BlockEntity.SIGN));
        String[] Tile = {" ", " ", " ", " "};
        for (int i = 0; i < list.length; i++) {
            Tile[i] = list[i] == null ? "" : list[i];
            if (i >= 3)
                break;
        }
        sign.setText(Tile);
    }

    /**
     * ????????????????????????
     *
     * @param map
     * @return
     */
    public static Item loadItem(Map<String, Object> map) {
        if (map == null)
            return null;
        Item item = new Item((int) map.get("ID"), (int) map.get("Damage"), (int) map.get("Count"));
        String name = (String) map.get("Name");
        if (name != null && !name.isEmpty())
            item.setCustomName(name);
        try {
            item.setCompoundTag((byte[]) map.get("Nbt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object o = map.get("Lore");
        if (o instanceof List) {
            List<String> list = (List<String>) o;
            item.setLore(list.toArray(new String[]{}));
        } else if (o != null) item.setLore(Tool.objToString(o));
        return item;
    }

    /**
     * ????????????????????????
     *
     * @param item
     * @return
     */
    public static Map<String, Object> saveItem(Item item) {
        Map<String, Object> map = new HashMap<>();
        map.put("Nbt", item.getCompoundTag());
        map.put("ID", item.getId());
        map.put("Damage", item.getDamage());
        map.put("Name", item.hasCustomName() ? item.getName() : null);
        map.put("Count", item.getCount());
        return map;
    }

    /**
     * ??????????????????????????????????????????????????? </br>
     * <b>player.getInventory().setContents(???????????????);</b>
     *
     * @return
     */
    public static Map<Integer, Item> loadInventory(Map<Integer, Map<String, Object>> list) {
        Map<Integer, Item> Contents = new HashMap<>();
        for (Integer i : list.keySet()) {
            Map<String, Object> map = list.get(i);
            Item item = new Item((int) map.get("ID"), (int) map.get("Damage"), (int) map.get("Count"), (String) map.get("Name"));
            item.setCompoundTag((byte[]) map.get("Nbt"));
            Contents.put(i, item);
        }
        return Contents;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param player ??????????????????????????????
     * @return
     */
    public static Map<Integer, Map<String, Object>> saveInventory(Player player) {
        Map<Integer, Map<String, Object>> list = new HashMap<>();
        Map<Integer, Item> Contents = player.getInventory().getContents();
        for (Integer i : Contents.keySet()) {
            Item item = Contents.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("Nbt", item.getCompoundTag());
            map.put("ID", item.getId());
            map.put("Damage", item.getDamage());
            map.put("Name", item.getName());
            map.put("Count", item.getCount());
            list.put(i, map);
        }
        return list;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    public static String getLanguage() {
        return getLanguages().get(Server.getInstance().getLanguage().getName());
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    public static Map<String, String> getLanguages() {
        Map<String, String> Languages = new HashMap<>();
        Languages.put("English", "eng");
        Languages.put("??????(??????)", "chs");
        Languages.put("??????(??????)", "cht");
        Languages.put("?????????", "jpn");
        Languages.put("Pycc??????", "rus");
        Languages.put("Espa??ol", "spa");
        Languages.put("Polish", "pol");
        Languages.put("Portugu??s", "bra");
        Languages.put("?????????", "kor");
        Languages.put("????????????????????", "ukr");
        Languages.put("Deutsch", "deu");
        Languages.put("Lietuvi??", "ltu");
        Languages.put("Indonesian", "idn");
        Languages.put("Czech", "cze");
        Languages.put("Turkish", "tur");
        Languages.put("Suomi", "fin");
        return Languages;
    }

    /**
     * ???10????????????????????????64??????
     *
     * @param number
     * @return
     */
    public static String CompressNumber(long number) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << 6;
        long mask = radix - 1;
        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= 6;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }

    /**
     * ???64???????????????????????????10??????
     *
     * @param decompStr
     * @return
     */
    public static long UnCompressNumber(String decompStr) {
        long result = 0;
        for (int i = decompStr.length() - 1; i >= 0; i--) {
            for (int j = 0; j < digits.length; j++) {
                if (decompStr.charAt(i) == digits[j]) {
                    result += ((long) j) << 6 * (decompStr.length() - 1 - i);
                }
            }
        }
        return result;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param date1 ????????????????????????
     * @param date2 ????????????????????????
     * @return
     */
    public static long getDay(String date1, String date2) {
        return getDay(date1, "yyyy-MM-dd", date2, "yyyy-MM-dd");
    }

    /**
     * ???????????????????????????????????????
     *
     * @param date1format ?????????????????????????????????<yyyy-MM-dd>
     * @param date2format ????????????????????????<yyyy-MM-dd>
     */
    public static long getDay(String date1sStr, String date1format, String date2Str, String date2format) {
        Date date1 = parseDate(date1sStr, date1format);
        Date date2 = parseDate(date2Str, date2format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(date2);
        long timeInMillis2 = calendar.getTimeInMillis();
        return (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param dateStr ???????????????
     * @return ????????????
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param dateStr ???????????????
     * @param pattern ??????
     * @return ????????????
     */
    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("??????????????????");
        }
        return date;
    }

    /**
     * ????????????
     *
     * @param <T>    ???????????????
     * @param arrays ????????????????????????
     * @return
     */
    @SafeVarargs
    public static <T> T[] Arrays(T[]... arrays) {
        List<T> list = new ArrayList<>();
        for (T[] t : arrays)
            Collections.addAll(list, t);
        return (T[]) list.toArray();
    }

    /**
     * ???????????????????????????????????????Long
     *
     * @param obj
     * @return
     */
    public static long objToLong(Object obj) {
        return objToLong(obj, 0L);
    }

    /**
     * ???????????????????????????????????????Long
     *
     * @param obj
     * @param d
     * @return
     */
    public static long objToLong(Object obj, long d) {
        String string = String.valueOf(obj);
        if (obj == null || string.isEmpty())
            return d;
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param obj
     * @return
     */
    public static float objToFloat(Object obj) {
        return objToFloat(obj, 0f);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param obj
     * @param d
     * @return
     */
    public static float objToFloat(Object obj, Float d) {
        String string = String.valueOf(obj);
        if (obj == null || string.isEmpty())
            return d;
        try {
            return Float.valueOf(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param obj
     * @return
     */
    public static double objToDouble(Object obj) {
        return objToDouble(obj, 0d);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param obj
     * @param d
     * @return
     */
    public static double objToDouble(Object obj, Double d) {
        String string = String.valueOf(obj);
        if (obj == null || string.isEmpty())
            return d;
        try {
            return Double.valueOf(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * ??????????????????
     *
     * @param num1
     * @param num2
     * @return
     */
    public static long getGys(long num1, long num2) {
        num1 = Math.abs(num1);
        num2 = Math.abs(num2);
        while (num2 != 0) {
            long remainder = num1 % num2;
            num1 = num2;
            num2 = remainder;
        }
        return num1;
    }

    /**
     * ??????????????????
     *
     * @param f
     * @return
     */
    public static int getDecimalsLength(float f) {
        String string = String.valueOf(f);
        if (f == 0 || string.indexOf(".") < 0)
            return 0;
        return string.substring(string.indexOf(".") + 1).length();
    }

    /**
     * ????????????????????????
     *
     * @param value
     * @return
     */
    public static int String_length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese))
                valueLength += 2;
            else
                valueLength += 1;
        }
        return valueLength;
    }

    /**
     * ???????????????
     *
     * @param f int[??????,??????]
     * @return
     */
    public static long[] getGrade(float f, int floatLength) {
        if (f == 0)
            return new long[]{0, 0};
        String string = String.valueOf(f);
        if (string.indexOf(".") < 0)
            return new long[]{(long) f, 1};
        String sint = string.substring(0, string.indexOf("."));
        String sfloat = string.substring(string.indexOf(".") + 1);
        long Fenmu = 1;
        for (int k = 0; k < floatLength; k++)
            Fenmu *= 10;
        long Fenzi = Long.parseLong(sint + sfloat);
        long lXs = Fenzi < Fenmu ? Fenzi : Fenmu, j = 1;
        for (j = lXs; j > 1; j--)
            if (Fenzi % j == 0 && Fenmu % j == 0)
                break;
        Fenzi = Fenzi / j;
        Fenmu = Fenmu / j;
        return new long[]{Fenzi, Fenmu};
    }

    /**
     * Object???????????????String
     *
     * @param obj
     * @return
     */
    public static String objToString(Object obj) {
        return objToString(obj, null);
    }

    /**
     * Object???????????????String
     *
     * @param obj
     * @param string
     * @return
     */
    public static String objToString(Object obj, String string) {
        if (obj == null)
            return string;
        try {
            return String.valueOf(obj);
        } catch (Exception e) {
            return string;
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param time ?????????
     * @return
     */
    public static String getTimeBy(double time) {
        int y = (int) (time / 31556926);
        time = time % 31556926;
        int d = (int) (time / 86400);
        time = time % 86400;
        int h = (int) (time / 3600);
        time = time % 3600;
        int i = (int) (time / 60);
        double s = time % 60;
        return (y > 0 ? y + "???" : "") + (d > 0 ? d + "???" : "") + (h > 0 ? h + "??????" : "") + (i > 0 ? i + "??????" : "") + (s > 0 ? Double2(s, 1) + "???" : "");
    }

    /**
     * ????????????ID???????????????x????????????
     *
     * @return
     */
    public static boolean isMateID(Object id1, Object id2) {
        String ID1 = String.valueOf(id1), ID2 = String.valueOf(id2);
        if (ID1 == null || ID2 == null)
            return false;
        if (!ID1.contains(":"))
            ID1 += ":0";
        if (!ID2.contains(":"))
            ID2 += ":0";
        String[] ID1s = ID1.split(":"), ID2s = ID2.split(":");
        if (ID1s[0].equals("x") || ID2s[0].equals("x") || ID1s[0].equals(ID2s[0])) {
            if (ID1s[1].equals("x") || ID2s[1].equals("x") || ID2s[1].equals(ID1s[1]))
                return true;
            return false;
        }
        return false;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public static String getTime() {
        return time.format(new Date());
    }

    /**
     * ????????????
     *
     * @param file1 ?????????
     * @param file2 ????????????
     * @return
     */
    public static boolean Copy(String file1, String file2) {
        return Copy(new File(file1), new File(file2));
    }

    /**
     * ????????????
     *
     * @param file1 ?????????
     * @param file2 ????????????
     * @return
     */
    public static boolean Copy(File file1, File file2) {
        if (!file1.exists())
            return false;
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file1);
            OutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file2, true);
                int temp = 0;
                try {
                    while ((temp = fileInputStream.read()) != -1)
                        fileOutputStream.write(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * ?????????????????? <???-???-???>
     *
     * @return
     */
    public static String getDate() {
        return data.format(new Date());
    }

    /**
     * ????????????ID?????????????????????????????????????????????????????????0???????????????
     *
     * @param ID ??????????????????ID
     * @return int[]{ID, Damage}
     */
    public static int[] IDtoFullID(Object ID) {
        return IDtoFullID(ID, 0);
    }

    /**
     * ????????????ID???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param Damage ???????????????????????????
     * @return int[]{ID, Damage}
     */
    public static int[] IDtoFullID(Object obj, int Damage) {
        String ID = "0";
        if (obj != null && !String.valueOf(obj).isEmpty())
            ID = String.valueOf(obj);
        if (!ID.contains(":"))
            ID += ":" + Damage;
        String[] strings = ID.split(":");
        try {
            return new int[]{Integer.valueOf(strings[0]), Integer.valueOf(strings[1])};
        } catch (Exception e) {
            return new int[]{0, 0};
        }
    }

    /**
     * ???????????????
     *
     * @return
     */
    public static int getRand(int Min, int Max) {
        int Rand, Fn = Min;
        Min = Min <= 0 ? 0 : Min;
        Rand = (int) (Min + Math.random() * (Max - Min + 1));
        if (Fn < 0) {
            int x = (int) (Math.random() * (Fn * -1 + 1));
            Rand = (int) (Math.random() * 2) == 1 ? Rand : x * -1;
        }
        return Rand;
    }

    /**
     * ???????????????
     *
     * @return
     */
    public static int getRand() {
        return getRand(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    public static String getRandColor() {
        return getRandColor(colorKeyString);
    }

    /**
     * ??????????????????????????????
     *
     * @param ColorFont ??????????????????????????????
     * @return
     */
    public static String getRandColor(String ColorFont) {
        int rand = Tool.getRand(0, ColorFont.length() - 1);
        return "??" + ColorFont.substring(rand, rand + 1);
    }

    /**
     * ??????????????????????????????
     *
     * @param Font ?????????????????????
     * @return
     */
    public static String getColorFont(String Font) {
        return getColorFont(Font, colorKeyString);
    }

    /**
     * ????????????????????????
     *
     * @return ????????????
     */
    public static String getRandString() {
        return getRandString(randString);
    }

    /**
     * ????????????????????????
     *
     * @param string ????????????????????????
     * @return ????????????
     */
    public static String getRandString(String string) {
        int r1 = getRand(0, string.length() - 1);
        return string.substring(r1, r1 + 1);
    }

    /**
     * ??????????????????????????????
     *
     * @param Font      ?????????????????????
     * @param ColorFont ???????????????????????????
     * @return
     */
    public static String getColorFont(String Font, String ColorFont) {
        String text = "";
        for (int i = 0; i < Font.length(); i++) {
            int rand = Tool.getRand(0, ColorFont.length() - 1);
            text += "??" + ColorFont.substring(rand, rand + 1) + Font.substring(i, i + 1);
        }
        return text;
    }

    /**
     * ?????????????????????????????????
     *
     * @param str
     * @return
     */
    public static boolean isInteger(Object str) {
        try {
            Float.valueOf(String.valueOf(str)).intValue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9[.]]*");
        return pattern.matcher(str).matches();
    }

    /**
     * ???????????????Unicode
     *
     * @param string ?????????????????????
     * @return
     */
    public static String StringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++)
            unicode.append("\\u" + Integer.toHexString(string.charAt(i)));
        return unicode.toString();
    }

    /**
     * unicode ????????????
     *
     * @param unicode ?????? Unicode ????????????
     * @return
     */
    public static String UnicodeToString(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++)
            string.append((char) Integer.parseInt(hex[i], 16));
        return string.toString();
    }

    /**
     * ??????????????????</br>
     * ????????????????????????</br>
     *
     * @param d ??????????????????
     * @return
     */
    public static double Double2(double d) {
        return Double2(d, 2);
    }

    /**
     * ??????????????????</br>
     *
     * @param d      ???????????????
     * @param length ?????????????????????
     * @return
     */
    public static double Double2(double d, int length) {
        if (d == 0)
            return 0;
        String s = "#.0";
        for (int i = 1; i < length; i++)
            s += "0";
        DecimalFormat df = new DecimalFormat(s);
        return Double.valueOf(df.format(d));
    }

    /**
     * ??????HTTP??????
     *
     * @param httpUrl ????????????
     * @return
     * @throws Exception
     */
    public static String getHttp(String httpUrl) throws Exception {
        return getHttp(httpUrl, "POST", null);
    }

    /**
     * ??????HTTP??????
     *
     * @param httpUrl ????????????
     * @param param   ???????????????
     * @return
     * @throws Exception
     */
    public static String getHttp(String httpUrl, String param) throws Exception {
        return getHttp(httpUrl, "POST", param);
    }

    /**
     * ??????HTTP??????
     *
     * @param httpUrl ????????????
     * @param Type    ???????????????
     * @param param   ???????????????
     * @return
     * @throws Exception
     */
    public static String getHttp(String httpUrl, String Type, String param) throws Exception {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        URL url = new URL(httpUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(Type);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(60000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        if (param != null && !param.isEmpty()) {
            OutputStream os = null;
            os = connection.getOutputStream();
            os.write(param.getBytes());
            os.close();
        }
        if (connection.getResponseCode() == 200) {
            is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sbf = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();
        }
        if (null != br)
            br.close();
        if (null != is)
            is.close();
        connection.disconnect();
        return result;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param Context ????????????????????????
     * @return ?????????????????????
     */
    public static String cutString(String Context, String strStart, String strEnd) {
        int strStartIndex = Context.indexOf(strStart);
        int strEndIndex = Context.indexOf(strEnd, strStartIndex + 1);
        if (strStartIndex < 0 || strEndIndex < 0)
            return null;
        return Context.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }

    /**
     * ????????????
     *
     * @param urlStr   ???????????????????????????
     * @param fileName ????????????????????????
     * @param savePath ??????????????????
     * @throws IOException
     */
    public static void DownFile(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3 * 1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream = conn.getInputStream();
        byte[] getData = readInputStream(inputStream);
        File saveDir = new File(savePath);
        if (!saveDir.exists())
            saveDir.mkdir();
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        fos.close();
        if (inputStream != null)
            inputStream.close();
    }

    /**
     * ???????????????
     *
     * @param inputStream ?????????
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1)
            bos.write(buffer, 0, len);
        bos.close();
        return bos.toByteArray();
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param object
     * @return
     */
    public static int ObjToInt(Object object) {
        return ObjToInt(object, 0);
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param object
     * @param i      ???????????????????????????????????????
     * @return
     */
    public static int ObjToInt(Object object, int i) {
        if (object == null)
            return i;
        String string = String.valueOf(object);
        if (string.isEmpty() || !isInteger(object))
            return i;
        return string.contains(".") ? Float.valueOf(object.toString()).intValue() : Integer.valueOf(string);
    }

    /**
     * ??????Object????????????bool????????????????????????false
     *
     * @param obj
     * @return
     */
    public static boolean ObjToBool(Object obj) {
        return ObjToBool(obj, false);
    }

    /**
     * ??????Object????????????bool????????????????????????false
     *
     * @param obj
     * @param Del
     * @return
     */
    public static boolean ObjToBool(Object obj, boolean Del) {
        if (obj == null)
            return Del;
        try {
            return Boolean.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return Del;
        }
    }

    /**
     * ??????Https??????
     *
     * @param requestUrl ???????????????
     * @return
     * @throws KeyManagementException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getHttps(String requestUrl) throws KeyManagementException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
        return getHttps(requestUrl, "POST", null);
    }

    /**
     * ??????Https??????
     *
     * @param requestUrl ???????????????
     * @param outputStr  ??????????????????
     * @return
     * @throws KeyManagementException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getHttps(String requestUrl, String outputStr) throws KeyManagementException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
        return getHttps(requestUrl, "POST", outputStr);
    }

    /**
     * ??????Https??????
     *
     * @param requestUrl    ???????????????
     * @param requestMethod ???????????????
     * @param outputStr     ??????????????????
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static String getHttps(String requestUrl, String requestMethod, String outputStr) throws UnsupportedEncodingException, IOException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer buffer = null;
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new Tool()};
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(requestMethod);
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        if (null != outputStr && !outputStr.isEmpty()) {
            OutputStream os = conn.getOutputStream();
            os.write(outputStr.getBytes("utf-8"));
            os.close();
        }
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        buffer = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null)
            buffer.append(line);
        return buffer.toString();
    }

    /**
     * ?????????????????????html??????
     *
     * @return
     */
    public static String delHtmlString(String htmlStr) {
        if (htmlStr == null || htmlStr.isEmpty())
            return htmlStr;
        htmlStr = htmlStr.replace("<p>", "\r\n\t").replace("<span>", "\r\n\t").replace("<br>", "\r\n").replace("</br>", "\r\n");
        Pattern p_script = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        Pattern p_style = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        return htmlStr.replaceAll("&nbsp;", "").trim();
    }

    /**
     * Https????????????
     *
     * @param urlStr   ????????????????????????
     * @param fileName ???????????????????????????
     * @param savePath ?????????????????????
     * @throws Exception
     */
    public static void downLoadFromUrlHttps(String urlStr, String fileName, String savePath) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new Tool()};
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new Tool());
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        byte[] getData = readInputStream(inputStream);
        File saveDir = new File(savePath);
        if (!saveDir.exists())
            saveDir.mkdirs();
        FileOutputStream fos = new FileOutputStream(new File(saveDir, fileName));
        fos.write(getData);
        fos.close();
        if (inputStream != null)
            inputStream.close();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        return true;
    }

    /**
     * ??????????????????????????????
     *
     * @param obj
     * @return
     */
    public static Double ObjToDouble(Object obj) {
        return ObjToDouble(obj, 0d);
    }

    /**
     * ??????????????????????????????
     *
     * @param obj
     * @param double1
     * @return
     */
    public static Double ObjToDouble(Object obj, Double double1) {
        if (obj == null)
            return double1;
        double d;
        try {
            String S = String.valueOf(obj);
            if (S == null || S.isEmpty())
                return double1;
            d = Double.parseDouble(S);
        } catch (Exception e) {
            return double1;
        }
        return d;
    }

    /**
     * ???Map?????????????????????
     *
     * @param <K>
     * @param <V>
     * @param map
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }

    /**
     * ???Map????????????
     *
     * @param <K>
     * @param <V>
     * @param map
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort((a, b) -> -(a.getValue().compareTo(b.getValue())));
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }

    /**
     * ?????????????????????
     *
     * @param d
     * @param e
     * @return
     */
    public static double getRand(double d, double e) {
        return getRand(ObjToInt(d), ObjToInt(e));
    }
}
