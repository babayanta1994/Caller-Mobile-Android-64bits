package ru.true_ip.trueip.app.add_new_object_screen;


import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.db.entity.ObjectDb;

/**
 * Created by user on 10-Sep-17.
 */

interface AddNewObjectContract extends BaseContract {

    void showObjectData(ObjectDb objectDb);

    void displayDialog(int stringId);

    void resetImeOption(boolean isConcierge);
}
