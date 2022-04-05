package cn.winfxk.nukkit.winfxklib.form;

import cn.winfxk.nukkit.winfxklib.WinfxkLib;
import cn.winfxk.nukkit.winfxklib.tool.Tool;

public class FormID {
    private int FormID1, FormID2;
    public static FormID formID;

    public FormID() {
        formID = this;
        FormID1 = WinfxkLib.getMyConfig().getInt("FormID", Tool.getRand(10086, 1008611));
        FormID2 = WinfxkLib.getMyConfig().getInt("FormIDBak", Tool.getRand(10086, 1008611));
        while (FormID1 == FormID2)
            FormID2++;
    }

    public int getID(int oldID) {
        return oldID == 0 ? FormID1 : oldID == FormID1 ? FormID2 : FormID1;
    }

    public boolean hasID(int ID) {
        return ID == FormID1 || ID == FormID2;
    }
}
