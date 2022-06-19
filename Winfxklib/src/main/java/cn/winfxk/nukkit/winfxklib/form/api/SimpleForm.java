package cn.winfxk.nukkit.winfxklib.form.api;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.winfxk.nukkit.winfxklib.form.Disposeform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Winfxk
 */
public class SimpleForm extends RootForm {
    private List<ElementButton> buttons = new ArrayList<>();
    private String Content = "";
    public final Map<Integer, Disposeform> map = new HashMap<>();

    /**
     * @param ID      表单ID
     * @param Title   表单标题
     * @param Content 表单内容
     */
    public SimpleForm(int ID, String Title, String Content) {
        super(ID, Title);
        this.Content = Content;
    }

    @Override
    public FormWindow getFormWindow() {
        return new FormWindowSimple(Title == null ? "null" : Title, Content == null ? "null" : Content, buttons);
    }

    /**
     * @param Text     按钮内容
     * @param function
     * @return
     */
    public SimpleForm addButton(String Text, Disposeform function) {
        map.put(buttons.size(), function);
        buttons.add(new ElementButton(Text));
        return this;
    }

    /**
     * @param Text 按钮内容
     * @return
     */
    public SimpleForm addButton(String Text) {
        map.put(buttons.size(), null);
        buttons.add(new ElementButton(Text));
        return this;
    }

    /**
     * 添加一个按钮
     *
     * @param Text    按钮内容
     * @param isLocal 是否为本地贴图
     * @param Path    贴图路径
     * @return
     */
    public SimpleForm addButton(String Text, boolean isLocal, String Path) {
        if (Path == null || Path.isEmpty())
            return addButton(Text, null);
        map.put(buttons.size(), null);
        buttons.add(new ElementButton(Text == null ? "null" : Text, new ElementButtonImageData(
                isLocal ? ElementButtonImageData.IMAGE_DATA_TYPE_PATH : ElementButtonImageData.IMAGE_DATA_TYPE_URL,
                Path)));
        return this;
    }

    /**
     * 添加一个按钮
     *
     * @param Text     按钮内容
     * @param isLocal  是否为本地贴图
     * @param Path     贴图路径
     * @param function
     * @return
     */
    public SimpleForm addButton(String Text, boolean isLocal, String Path, Disposeform function) {
        if (Path == null || Path.isEmpty())
            return addButton(Text, function);
        map.put(buttons.size(), function);
        buttons.add(new ElementButton(Text == null ? "null" : Text, new ElementButtonImageData(
                isLocal ? ElementButtonImageData.IMAGE_DATA_TYPE_PATH : ElementButtonImageData.IMAGE_DATA_TYPE_URL,
                Path)));
        return this;
    }

    /**
     * 获取按钮数量
     *
     * @return
     */
    public int getButtonSize() {
        return buttons.size();
    }

    /**
     * 获取按钮列表
     *
     * @return
     */
    public List<ElementButton> getButtons() {
        return buttons;
    }

    /**
     * 设置界面内容
     *
     * @param content
     */
    public void setContent(String content) {
        Content = content;
    }

    /**
     * @return
     */
    public String getContent() {
        return Content;
    }

    /**
     * @param player
     * @param data
     */
    @Override
    public void resolveResponded(Player player, FormResponse data) {
        int index = ((FormResponseSimple) data).getClickedButtonId();
        Disposeform function = map.get(index);
        if (function != null)
            function.dispose(player, data);
        super.resolveResponded(player, data);
    }
}
