package org.cloud.auto;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.taobao.api.ApiException;

import java.util.List;
import java.util.stream.Collectors;

public class OldAPITest {

    public static DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");

    public static void main(String[] args) throws ApiException {

        // 1. 获取 token
        String accessToken = getAccessToken();

        getUseridByPhone("16639008682");

    }

    /**
     * 获取 access_token
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/obtain-orgapp-token
     */
    public static String getAccessToken() throws ApiException {
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey("dingvjbmmillamfrjnuw");
        request.setAppsecret("WYn7Zl4F6xAr7ptWPXlv6DxGx8zeFa4n2_T6WUHMDMSgR81vFY1r2t2xd74UKjkR");
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);

        System.out.println("【获取 token】" + response.getAccessToken());

        return response.getAccessToken();
    }

    /**
     * 根据部门名称获取部门 id
     * <p>
     * 获取部门列表：https://open.dingtalk.com/document/orgapp-server/obtain-the-department-list-v2
     */
    private static Long getDeptId(String accessToken, String deptName) throws ApiException {

        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
//        req.setDeptId(1L); // 不设置就获取根目录下的部门列表
        req.setLanguage("zh_CN");

        OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);

        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> res = rsp.getResult().stream().filter(e -> e.getName().equals(deptName)).collect(Collectors.toList());

        if(res.size() != 0){
            throw new RuntimeException("根据部门名【" + deptName + "】获取到的部门有【" + res.size() + "】个");
        }

        return res.get(0).getDeptId();

    }


    private static void getUseridByPhone(String phone){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
            OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
            req.setMobile(phone);
            OapiV2UserGetbymobileResponse rsp = client.execute(req, OldAPITest.getAccessToken());
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

}
