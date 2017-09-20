package test;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.game.receive.rest.model.reward.RewardReportModel;

public class Test {
    public static void main(String[] args) {
        RewardReportModel model = new RewardReportModel();
        model.setServer_id(1);
        model.setCount(1);
        model.setId(100);
        model.setUid(1000);
        model.setType(100);
        System.out.println(JSONObject.toJSONString(model));
        String str = "{\"Uid\":2,\"Server_id\":1000,\"Id\":10101,\"Type\":1,\"Count\":1}";
        model = JSONObject.parseObject(str, RewardReportModel.class);
        System.out.println(model);
    }
}
