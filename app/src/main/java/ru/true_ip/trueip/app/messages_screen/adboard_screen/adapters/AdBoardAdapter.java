package ru.true_ip.trueip.app.messages_screen.adboard_screen.adapters;

import java.util.List;

import ru.true_ip.trueip.app.messages_screen.adapters.AbstractMessagesAdapter;
import ru.true_ip.trueip.models.responses.AdvertModel;

/**
 * Created by ektitarev on 09.01.2018.
 */

public class AdBoardAdapter extends AbstractMessagesAdapter<AdvertModel> {

    public AdBoardAdapter(List<AdvertModel> items) { super(items); }

    @Override
    protected String getTheme(int position) {
        return items.get(position).getTheme();
    }

    @Override
    protected String getMessage(int position) {
        return items.get(position).getText();
    }

    @Override
    protected String getActualDate(int position) {
        return items.get(position).getActual_from_date();
    }

    @Override
    protected int isViewed(int position) {
        return items.get(position).isIs_viewed();
    }

}
