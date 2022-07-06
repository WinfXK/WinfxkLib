package cn.winfxk.nukkit.winfxklib.form;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.winfxk.nukkit.winfxklib.Message;
import cn.winfxk.nukkit.winfxklib.MyPlayer;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.api.ModalForm;
import cn.winfxk.nukkit.winfxklib.form.api.SimpleForm;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFormin {
    protected Player player;
    private BaseFormin UpForm;
    private boolean isBack;
    private Object[] D;
    protected String[] K = {"{Player}", "{Money}"};
    protected final static String MainKey = "Form";
    protected static Message message;
    protected String FormKey;

    public void setD(Object... d) {
        D = d;
    }

    public void setK(String... k) {
        K = k;
    }

    public Object[] getD() {
        return D;
    }

    public String[] getK() {
        return K;
    }

    public BaseFormin(Player player, BaseFormin Update, boolean isBack) {
        this.player = player;
        this.UpForm = Update;
        this.isBack = isBack;
        D = new Object[]{player.getName(), WinfxkLib.getMyPlayer(player).getMoney()};
        FormKey = getClass().getSimpleName();
    }


    public Player getPlayer() {
        return player;
    }

    public boolean show(BaseFormin form) {
        return (WinfxkLib.getMyPlayer(player).form = form).MakeForm();
    }

    /**
     * 关闭窗口时调用
     *
     * @return 是否阻断进程
     */
    public boolean wasClosed() {
        clear();
        return true;
    }

    /**
     * 不需要Form清理垃圾
     */
    protected boolean clear() {
        MyPlayer myPlayer = WinfxkLib.getMyPlayer(player);
        myPlayer.form = null;
        myPlayer.fun = null;
        return false;
    }

    protected String NotPermissions() {
        return WinfxkLib.getMessage().getSon(MainKey, "NotPermissions", this);
    }

    protected String getString(String Key) {
        return message.getSun(MainKey, FormKey, Key, this);
    }

    protected String getConfirm() {
        return WinfxkLib.getMessage().getSon(MainKey, "Confirm", this);
    }

    protected String getString(String Key, String[] Keys, Object[] Datas) {
        return message.getSun(MainKey, FormKey, Key,
                getArrayKeys(Keys, K),
                Tool.Arrays(Datas, D), player);
    }

    protected String[] getArrayKeys(Object[]... strings_s) {
        List<String> list = new ArrayList<>();
        for (Object[] strings : strings_s)
            for (Object s : strings)
                list.add(Tool.objToString(s));
        return list.toArray(new String[list.size()]);
    }

    public static int getID(MyPlayer myPlayer) {
        return FormID.formID.getID(myPlayer.ID);
    }

    /**
     * 返回FormID
     *
     * @return
     */
    protected int getID() {
        return FormID.formID.getID(WinfxkLib.getMyPlayer(player).ID);
    }

    /**
     * 界面创建
     *
     * @return 是否构建成功
     */
    public abstract boolean MakeForm();

    public boolean DisposeCustom(FormResponseCustom data) {
        return true;
    }

    public boolean DisposeSimple(FormResponseSimple data) {
        return true;
    }

    public boolean DisposeModal(FormResponseModal data) {
        return true;
    }

    public boolean dispose(FormResponse data) {
        if (data instanceof FormResponseCustom)
            return DisposeCustom((FormResponseCustom) data);
        if (data instanceof FormResponseModal)
            return DisposeModal((FormResponseModal) data);
        if (data instanceof FormResponseSimple)
            return DisposeSimple((FormResponseSimple) data);
        return clear();
    }

    protected boolean sendMessage(String Message, boolean back) {
        player.sendMessage(Message);
        return back;
    }

    protected boolean sendMessage(String Message) {
        player.sendMessage(Message);
        return true;
    }

    protected boolean isBack() {
        return isBack && UpForm != null && UpForm.MakeForm();
    }

    protected String getBack() {
        return WinfxkLib.getMessage().getSon("Form", !isBack || UpForm == null ? "Exit" : "Back");
    }

    protected String getExitString() {
        return WinfxkLib.getMessage().getSon("Form", "Exit", this);
    }

    protected String getBackString() {
        return WinfxkLib.getMessage().getSon("Form", "Back", this);
    }

    protected String getConfirmString() {
        return WinfxkLib.getMessage().getSon("Form", "Confirm", this);
    }

    protected String getContent() {
        return getString("Content");
    }

    protected String getTitle() {
        return getString("Title");
    }

    /**
     * 弹出一个提示框
     *
     * @param player  玩家名称
     * @param Content 弹窗内容
     * @return Back
     */
    public static boolean makeShow(Player player, String Content) {
        return makeShow(true, player.getName(), message.getSon(MainKey, "Tip", player), Content, message.getSon(MainKey, "Confirm", player), null, message.getSon(MainKey, "Exit", player), null, false);
    }

    /**
     * 弹出一个提示框
     *
     * @param player  玩家名称
     * @param Title   弹窗标题
     * @param Content 弹窗内容
     * @param Back    返回值
     * @return Back
     */
    public static boolean makeShow(Player player, String Title, String Content, boolean Back) {
        return makeShow(true, player.getName(), Title, Content, message.getSon(MainKey, "Confirm", player), null, message.getSon(MainKey, "Exit", player), null, Back);
    }

    /**
     * 弹出一个提示框
     *
     * @param isModle 是否是Modle界面
     * @param player  玩家名称
     * @param Title   弹窗标题
     * @param Content 弹窗内容
     * @param Button1 弹窗按钮文本1
     * @param fun1    弹窗按钮1点击事件
     * @param Button2 弹窗按钮2文本
     * @param fun2    弹窗按钮2点击事件
     * @return
     */
    public static boolean makeShow(boolean isModle, String player, String Title, String Content, String Button1, Disposeform fun1, String Button2, Disposeform fun2) {
        MyPlayer myPlayer = WinfxkLib.getMyPlayer(player);
        if (myPlayer == null)
            return false;
        if (isModle) {
            ModalForm form = new ModalForm(myPlayer.getID(), Title, Content);
            form.setButton(Button1, fun1, Button2, fun2);
            form.show(myPlayer.getPlayer());
            return true;
        }
        SimpleForm form = new SimpleForm(myPlayer.getID(), Title, Content);
        form.addButton(Button1, fun1);
        form.addButton(Button2, fun2);
        form.show(myPlayer.getPlayer());
        return true;
    }

    /**
     * 弹出一个提示框
     *
     * @param isModle 是否是Modle界面
     * @param player  玩家名称
     * @param Title   弹窗标题
     * @param Content 弹窗内容
     * @param Button1 弹窗按钮文本1
     * @param fun1    弹窗按钮1点击事件
     * @param Button2 弹窗按钮2文本
     * @param fun2    弹窗按钮2点击事件
     * @param Back    返回值
     * @return Back
     */
    public static boolean makeShow(boolean isModle, String player, String Title, String Content, String Button1, Disposeform fun1, String Button2, Disposeform fun2, boolean Back) {
        MyPlayer myPlayer = WinfxkLib.getMyPlayer(player);
        if (myPlayer == null)
            return Back;
        if (isModle) {
            ModalForm form = new ModalForm(myPlayer.getID(), Title, Content);
            form.setButton(Button1, fun1, Button2, fun2);
            form.show(myPlayer.getPlayer());
            return Back;
        }
        SimpleForm form = new SimpleForm(myPlayer.getID(), Title, Content);
        form.addButton(Button1, fun1);
        form.addButton(Button2, fun2);
        form.show(myPlayer.getPlayer());
        return Back;
    }
}
