package org.cloud.auto;

import cn.hutool.core.util.StrUtil;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkswform_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.taobao.api.ApiException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewAPITest {

    // 登录用
    private static com.aliyun.dingtalkoauth2_1_0.Client tokenClient;

    // 智能填表用
    private static com.aliyun.dingtalkswform_1_0.Client smartFormClient;

    // 创建客户端
    static {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            tokenClient = new com.aliyun.dingtalkoauth2_1_0.Client(config);
            smartFormClient = new com.aliyun.dingtalkswform_1_0.Client(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. 获取 access_token
        String token = getAccessToken();

        // 获取 formCode
        List<String> formCodes = getFormCode(token);
        System.out.println("【获取到 formCode】" + Arrays.toString(formCodes.toArray()));

        // 获取表格提交数据
        formCodes.forEach(formCode -> {
            ListFormInstancesHeaders listFormInstancesHeaders = new ListFormInstancesHeaders();
            listFormInstancesHeaders.xAcsDingtalkAccessToken = token;
            ListFormInstancesRequest listFormInstancesRequest = new ListFormInstancesRequest()
                    .setBizType(0)
                    .setActionDate("2019-01-01")
                    .setNextToken(0)
                    .setMaxResults(10);
            ListFormInstancesResponse res = null;
            try {
                res = smartFormClient.listFormInstancesWithOptions(formCode, listFormInstancesRequest, listFormInstancesHeaders, new RuntimeOptions());
                List<ListFormInstancesResponseBody.ListFormInstancesResponseBodyResultList> forms = res.getBody().getResult().getList();

                forms.forEach(form->{
                    System.out.println("【表格：" + form.getFormCode() +"】内容如下");

                    String mobile = "";

                    for (ListFormInstancesResponseBody.ListFormInstancesResponseBodyResultListForms ele : form.getForms()) {
                        System.out.println(ele.getLabel() +  " : " + ele.getKey() + " : " + ele.getValue());
                        if(StrUtil.isNotEmpty(ele.getLabel()) && ("手机".equals(ele.getLabel())) || "手机号".equals(ele.getLabel())){
                            mobile = ele.getValue();
                        }
                    }

                    // 根据手机号查询 userid

                    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
                    OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
                    req.setMobile(mobile);
                    OapiV2UserGetbymobileResponse rsp = null;
                    try {
                        rsp = client.execute(req, OldAPITest.getAccessToken());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                    System.out.println(rsp.getBody());

                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        });


    }

    /**
     * 获取 formCode
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/new-obtains-the-template-that-a-user-creates
     */
    private static List<String> getFormCode(String token) throws Exception {

        ListFormSchemasByCreatorHeaders listFormSchemasByCreatorHeaders = new ListFormSchemasByCreatorHeaders();
        listFormSchemasByCreatorHeaders.xAcsDingtalkAccessToken = token;
        ListFormSchemasByCreatorRequest listFormSchemasByCreatorRequest = new ListFormSchemasByCreatorRequest()
                .setMaxResults(10)
                .setBizType(0)
                .setCreator("manager8053") // 表格创建人
                .setNextToken(0L);
        ListFormSchemasByCreatorResponse res = smartFormClient.listFormSchemasByCreatorWithOptions(listFormSchemasByCreatorRequest, listFormSchemasByCreatorHeaders, new RuntimeOptions());
        List<String> formCodes = res.getBody().getResult().getList().stream().map(ListFormSchemasByCreatorResponseBody.ListFormSchemasByCreatorResponseBodyResultList::getFormCode).collect(Collectors.toList());
        return formCodes;
    }


    /**
     * 获取 access_token
     * <p>
     * https://open.dingtalk.com/document/orgapp-server/obtain-the-access_token-of-an-internal-app
     */
    public static String getAccessToken() throws Exception {
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                .setAppKey("dingvjbmmillamfrjnuw")
                .setAppSecret("WYn7Zl4F6xAr7ptWPXlv6DxGx8zeFa4n2_T6WUHMDMSgR81vFY1r2t2xd74UKjkR");
        try {
            // 有时候连公司网络无法访问到接口，需要连自己热点
            GetAccessTokenResponse accessToken = tokenClient.getAccessToken(getAccessTokenRequest);

            String accessTokenStr = accessToken.getBody().getAccessToken();
            System.out.println("【获取到 access_token】" + accessTokenStr);
            return accessTokenStr;

        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }

        throw new RuntimeException("");
    }


}
