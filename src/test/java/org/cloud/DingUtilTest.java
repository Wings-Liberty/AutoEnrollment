package org.cloud;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentGetRequest;
import com.dingtalk.api.request.OapiV2DepartmentListparentbydeptRequest;
import com.dingtalk.api.request.OapiV2DepartmentListparentbyuserRequest;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentListparentbydeptResponse;
import com.dingtalk.api.response.OapiV2DepartmentListparentbyuserResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import org.cloud.utils.ding.DingUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DingUtilTest {


    public static void main(String[] args) throws ApiException {

//        OapiV2UserGetResponse userDetail = DingUtil.getUserDetail("234834491823542976");
//
//        System.out.println(userDetail.getBody());

        // 1.获取指定用户的所有父部门列表
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listparentbyuser");
        OapiV2DepartmentListparentbyuserRequest req = new OapiV2DepartmentListparentbyuserRequest();
        req.setUserid("684512313540013208");
        OapiV2DepartmentListparentbyuserResponse rsp = client.execute(req, DingUtil.getOldVersionToken());


        System.out.println(rsp.getBody());
//        log.info("【查到 userid {} 的所有父部门 id】{}", userid, rsp.getBody());

    }

}
