package org.cloud.utils.ding;

import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 保存各种客户端和新旧 API
 */
@Slf4j
public class DingUtil {

    private static final String APP_KEY = "dingvjbmmillamfrjnuw";

    private static final String APP_SECRET = "WYn7Zl4F6xAr7ptWPXlv6DxGx8zeFa4n2_T6WUHMDMSgR81vFY1r2t2xd74UKjkR";


    /**
     * 新版 API
     */

    // 获取 token
    private static com.aliyun.dingtalkoauth2_1_0.Client tokenNewClient;
    // 智能填表客户端
    private static com.aliyun.dingtalkswform_1_0.Client smartFormNewClient;


    /**
     * 旧版 AIP
     */

    // 获取 token
    public static DingTalkClient tokenOldClient;

    public static DingTalkClient userOldClient;

    public static DingTalkClient deptOldClient;

    // 发送消息
    private static DingTalkClient sendMsgOldClient;

    private static DingTalkClient userListOldclient;


    // 初始化客户端
    static {

        Config config = new Config();

        config.protocol = "https";

        config.regionId = "central";

        try {

            tokenNewClient = new com.aliyun.dingtalkoauth2_1_0.Client(config);

            smartFormNewClient = new com.aliyun.dingtalkswform_1_0.Client(config);

        } catch (Exception e) {

            e.printStackTrace();

        }

        tokenOldClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        userOldClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        deptOldClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");

        sendMsgOldClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

        userListOldclient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");

    }

    private DingUtil() {
        throw new UnsupportedOperationException("工具类不能被实例化");
    }


    /**
     * 获取 access_token，新版 API
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/obtain-the-access_token-of-an-internal-app
     */
    public static String getNewVersionToken() throws Exception {

        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest().setAppKey(APP_KEY).setAppSecret(APP_SECRET);

        // 有时候连公司网络无法访问到接口，需要连自己热点
        GetAccessTokenResponse accessToken = tokenNewClient.getAccessToken(getAccessTokenRequest);

        String accessTokenStr = accessToken.getBody().getAccessToken();

        log.info("【获取到 access_token】{}", accessTokenStr);

        return accessTokenStr;
    }

    /**
     * 获取 access_token，老版 API
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/obtain-orgapp-token
     */
    public static String getOldVersionToken() throws ApiException {

        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(APP_KEY);
        request.setAppsecret(APP_SECRET);
        request.setHttpMethod("GET");

        OapiGettokenResponse response = tokenOldClient.execute(request);

        String token = response.getAccessToken();

//        log.info("【获取 token】{}", token);

        return token;
    }

    /**
     * 根据员工 userid 获取详细信息
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/query-user-details
     */
    public static OapiV2UserGetResponse getUserDetail(String userId) throws ApiException {

        OapiV2UserGetRequest req = new OapiV2UserGetRequest();

        req.setUserid(userId);

        return userOldClient.execute(req, getOldVersionToken());

    }

    /**
     * 查询指定部门的直接子部门
     * <p>
     * 如果参数 fartherDeptId 为 null，默认查询根部门的直接子部门
     * <p>
     * 返回结果一定不是 null，如果查不到结果，就返回一个空集合
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/obtain-the-department-list-v2
     */
    public static List<OapiV2DepartmentListsubResponse.DeptBaseResponse> listChildDeptIdsBy(Long fartherDeptId) throws ApiException {
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();

        if (fartherDeptId != null) {
            req.setDeptId(fartherDeptId);
        }

        req.setLanguage("zh_CN");

        OapiV2DepartmentListsubResponse rsp = deptOldClient.execute(req, DingUtil.getOldVersionToken());

//        System.out.println(rsp.getBody());

        return rsp.getResult() == null ? Collections.emptyList() : rsp.getResult();
    }

    public static List<OapiUserListsimpleResponse.ListUserSimpleResponse> listEmpByDeptId(Long deptId) throws ApiException {

        OapiUserListsimpleRequest req = new OapiUserListsimpleRequest();
        req.setDeptId(deptId);
        req.setCursor(0L);
        req.setSize(10L);
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage("zh_CN");
        OapiUserListsimpleResponse rsp = userListOldclient.execute(req, DingUtil.getOldVersionToken());
//        System.out.println(rsp.getBody());

        return rsp.getResult() == null || rsp.getResult().getList() == null ? Collections.emptyList() : rsp.getResult().getList();

    }

    /**
     * 获取研发部的部门 id 号（现在要求研发部门必须是企业跟部门下的指数子部门）
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/obtain-a-sub-department-id-list-v2
     */
    public static List<Long> getDevDeptId() throws ApiException {
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        req.setDeptId(1L);
        req.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse rsp = deptOldClient.execute(req, getOldVersionToken());

        return rsp.getResult().stream().filter(dept -> dept.getName().contains("研发")).map(OapiV2DepartmentListsubResponse.DeptBaseResponse::getDeptId).collect(Collectors.toList());
    }

    /**
     * 以应用名义发送消息
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/asynchronous-sending-of-enterprise-session-messages
     *
     * @param userIdsStr 示例 user123,user456。逗号分割 userid
     */
    public static boolean sendDingTextMsg(String userIdsStr, String content) throws ApiException {
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(1792907093L); // 应用的 appid
        request.setUseridList(userIdsStr);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        msg.getText().setContent(content);
        request.setMsg(msg);
        // TODO: 2022/8/17  忘了用客户端发送请求
        OapiMessageCorpconversationAsyncsendV2Response rsp = sendMsgOldClient.execute(request, getOldVersionToken());

        return true;
    }

    // 查询指定 userid 的所有父部门 id
    public static List<OapiV2DepartmentListparentbyuserResponse.DeptParentResponse> listAllParentDeptIds(String userid) throws ApiException {
        // 1.获取指定用户的所有父部门列表
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listparentbyuser");
        OapiV2DepartmentListparentbyuserRequest req = new OapiV2DepartmentListparentbyuserRequest();
        req.setUserid(userid);
        OapiV2DepartmentListparentbyuserResponse rsp = client.execute(req, DingUtil.getOldVersionToken());

        log.info("【查到 userid {} 的所有父部门 id】{}", userid, rsp.getBody());

        return rsp.getResult().getParentList();
    }

    // 查询所有 dept_id 的部门名是否有所需要的关键字
    public static boolean hasNeedKeyInThese(String key, List<OapiV2DepartmentListparentbyuserResponse.DeptParentResponse> dptsList) throws ApiException {

        String token = DingUtil.getOldVersionToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/get");

        boolean canBreak = false;

        for (OapiV2DepartmentListparentbyuserResponse.DeptParentResponse dpts : dptsList) {
            for (Long deptId : dpts.getParentDeptIdList()) {
                OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
                req.setDeptId(deptId);
                req.setLanguage("zh_CN");
                try {
                    OapiV2DepartmentGetResponse rsp1 = client.execute(req, token);
                    System.out.println(rsp1.getResult().getName() + " " + rsp1.getResult().getDeptId());

                    if(rsp1.getResult().getName().contains(key)){
                        canBreak = true;
                        break;
                    }

                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            if(canBreak) break;
        }
        return canBreak;
    }

}
