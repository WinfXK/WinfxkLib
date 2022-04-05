package cn.winfxk.nukkit.winfxklib.form;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.winfxk.nukkit.winfxklib.Message;
import cn.winfxk.nukkit.winfxklib.MyPlayer;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseForm {
    private String[] K = {"{Player}", "{Money}"};
    public static WinfxkLib main = WinfxkLib.getMain();
    protected static Message message = WinfxkLib.getMessage();
    protected MyPlayer myPlayer;
    protected Player player;
    private BaseForm UpForm;
    private boolean isBack;
    private Object[] D;
    protected static final String MainKey = "Form";

    public BaseForm(Player player, BaseForm Update, boolean isBack) {
        this.player = player;
        this.myPlayer = WinfxkLib.getMyPlayer(player);
        this.UpForm = Update;
        this.isBack = isBack;
        D = new Object[]{player.getName(), myPlayer.getMoney()};
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
        return !isBack || UpForm == null ? false : UpForm.MakeForm();
    }

    protected String getBack() {
        return message.getSon(MainKey, !isBack || UpForm == null ? "Exit" : "Back");
    }

    protected String getContent() {
        return getString("Content");
    }

    protected String getTitle() {
        return getString("Title");
    }

    protected String getString(String Key) {
        return message.getSun(MainKey, getClass().getSimpleName(), Key, this);
    }

    protected String getConfirm() {
        return message.getSon(MainKey, "Confirm", this);
    }

    protected String getString(String Key, String[] Keys, Object[] Datas) {
        return message.getSun(MainKey, getClass().getSimpleName(), Key,
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
        return FormID.formID.getID(myPlayer.ID);
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

    protected boolean dispose(FormResponse data) {
        if (data instanceof FormResponseCustom)
            return DisposeCustom((FormResponseCustom) data);
        if (data instanceof FormResponseModal)
            return DisposeModal((FormResponseModal) data);
        if (data instanceof FormResponseSimple)
            return DisposeSimple((FormResponseSimple) data);
        return clear();
    }

    public String[] getK() {
        return K;
    }

    public Object[] getD() {
        return D;
    }

    public Player getPlayer() {
        return player;
    }

    public void setD(Object... d) {
        D = d;
    }

    public void setK(String... k) {
        K = k;
    }

    public boolean show(BaseForm form) {
        return (myPlayer.form = form).MakeForm();
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
        myPlayer.form = null;
        myPlayer.fun = null;
        return false;
    }
}
