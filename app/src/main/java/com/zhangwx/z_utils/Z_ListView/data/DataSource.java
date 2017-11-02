package com.zhangwx.z_utils.Z_ListView.data;

import com.zhangwx.z_utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangweixiong on 2017/10/15.
 */

public class DataSource {

    public static List<DataModel> getData() {
        final List<DataModel> modelList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final DataModel dataModel = new DataModel();
            dataModel.setText("data-" + i);
            if (i > 10) {
                if ((i & 1) == 0) {
                    dataModel.setType(DataModel.TYPE_ONE);
                    dataModel.setImageId(R.drawable.password_bao1);
                } else {
                    dataModel.setType(DataModel.TYPE_TWO);
                    dataModel.setImageId(R.drawable.minisite_loading_default);
                }
            } else {
                if ((i & 1) == 0) {
                    dataModel.setType(DataModel.TYPE_ONE);
                    dataModel.setImageId(R.drawable.password_bao1);
                } else {
                    dataModel.setType(DataModel.TYPE_THREE);
                    dataModel.setImageId(R.drawable.screen3_feed_icon_loading);
                }
            }
            modelList.add(new DataModel());
        }
        return modelList;
    }
}
