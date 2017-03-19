package com.zhangwx.z_utils.Z_RecycleView.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */
public class DataService {

    public List<ListData> getData() {
        List<ListData> listDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listDatas.add(i, new ListData());
        }
        return listDatas;
    }

}
