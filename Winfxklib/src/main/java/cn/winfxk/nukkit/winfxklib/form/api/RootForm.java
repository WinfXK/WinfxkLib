package cn.winfxk.nukkit.winfxklib.form.api;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;
import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.form.Disposeform;

/**
 * @author Winfxk
 */
public abstract class RootForm {
    protected int ID;
    protected String Title;
    protected Disposeform stopDispose = null, wasClosed = null;

    public void setStopDispose(Disposeform stopDispose) {
        this.stopDispose = stopDispose;
    }

    public void setWasClosed(Disposeform wasClosed) {
        this.wasClosed = wasClosed;
    }

    /**
     * @param ID 表单ID
     */
    public RootForm(int ID) {
        this(ID, "");
    }

    /**
     * 根页面
     *
     * @param ID
     * @param Title
     */
    public RootForm(int ID, String Title) {
        this.ID = ID;
        this.Title = Title;
    }

    /**
     * 返回要显示的窗口
     *
     * @return
     */
    public abstract FormWindow getFormWindow();

    /**
     * 设置表单ID
     *
     * @param ID
     * @return
     */
    public RootForm setID(int ID) {
        this.ID = ID;
        return this;
    }

    /**
     * 设置表单标题
     *
     * @param Title
     * @return
     */
    public RootForm setTitle(String Title) {
        this.Title = Title;
        return this;
    }

    /**
     * 将表单发送给指定玩家列表
     *
     * @param player    要发送的玩家
     * @param callback  玩家提交界面会处理的事件
     * @param wasClosed 玩家关闭界面将会处理的事件
     * @return
     */
    public int show(Player player, Disposeform callback, Disposeform wasClosed) {
        stopDispose = callback;
        this.wasClosed = wasClosed;
        return show(player);
    }

    /**
     * 将表单发送给指定玩家列表
     *
     * @param player   要发送的玩家
     * @param callback 玩家提交界面会处理的事件
     * @return
     */
    public int show(Player player, Disposeform callback) {
        stopDispose = callback;
        return show(player);
    }

    /**
     * 将表单发送给指定玩家列表
     *
     * @param player
     * @return
     */
    public int show(Player player) {
        try {
            WinfxkLib.getMyPlayer(player).setFun(this);
            WinfxkLib.getMyPlayer(player).ID = ID;
        } catch (Exception e) {
        }
        player.showFormWindow(getFormWindow(), ID);
        return ID;
    }

    /**
     * 处理玩家关闭界面事件
     *
     * @param player
     * @return 是否阻断进程
     */
    public boolean wasClosed(Player player) {
        if (wasClosed != null)
            return wasClosed.dispose(player, null);
        return true;
    }

    public void resolveResponded(Player player, FormResponse data) {
        if (stopDispose != null) stopDispose.dispose(player, data);
    }

    /**
     * 返回界面的标题
     *
     * @return
     */
    public String getTitle() {
        return Title;
    }

    /**
     * 返回界面的ID
     *
     * @return
     */
    public int getID() {
        return ID;
    }
}
