package cn.icframework.system.init;

import cn.icframework.common.enums.Status;
import cn.icframework.system.consts.InitMd5Keys;
import cn.icframework.system.init.helper.InitHelper;
import cn.icframework.system.module.position.Position;
import cn.icframework.system.module.position.def.PositionDef;
import cn.icframework.system.module.position.service.PositionService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author hzl
 * @date 2024/9/4
 */
@Component
@RequiredArgsConstructor
public class PosInit {

    private final PositionService positionService;
    private final InitHelper initHelper;

    public void initPos() throws IOException {
        initHelper.processDic("/init/pos/", InitMd5Keys.POS_INIT_MD5, content -> {
            PositionDef positionDef = PositionDef.table();
            JSONArray rolesArr = JSONArray.parseArray(content);
            for (int i = 0; i < rolesArr.size(); i++) {
                JSONObject object = rolesArr.getJSONObject(i);
                Position position = positionService.selectOne(positionDef.sign.eq(object.getString("sign")));
                if (position == null) {
                    position = Position.def();
                    position.setStatus(Status.instanceOf(object.getInteger("status")));
                    position.setSign(object.getString("sign"));
                    position.setName(object.getString("name"));
                    position.setLevel(object.getInteger("level"));
                    positionService.insert(position);
                } else {
                    position.setStatus(Status.instanceOf(object.getInteger("status")));
                    position.setName(object.getString("name"));
                    position.setLevel(object.getInteger("level"));
                    positionService.updateById(position);
                }
            }
        });
    }
}
