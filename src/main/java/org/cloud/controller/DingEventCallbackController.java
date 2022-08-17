package org.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.Constant;
import org.cloud.service.impl.DingEventCallbackService;
import org.cloud.utils.ding.DingCallbackCrypto;
import org.cloud.utils.res.EventRes;
import org.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 钉钉的事件通知接口，供钉钉服务器调用
 */
@RestController
@RequestMapping("/api/ding")
@Slf4j
public class DingEventCallbackController {

    @Autowired
    private DingEventCallbackService dingEventCallbackService;

    // TODO: 2022/8/16 不知道原因，钉钉对同一个事件，会向这个接口连续迅速发送两个相同的请求，导致代码被执行两次。接口存在 先删除再创建的流程，所以存在并发异常，导致 userid 在表中重复出现
    @PostMapping("/call")
    public Map<String, String> callBack(
            @RequestParam(value = "msg_signature", required = false) String msg_signature,
            @RequestParam(value = "timestamp", required = false) String timeStamp,
            @RequestParam(value = "nonce", required = false) String nonce,
            @RequestBody(required = false) JSONObject json) {
        try {

            DingCallbackCrypto callbackCrypto = new DingCallbackCrypto(
                    Constant.DING.AES_TOKEN,
                    Constant.DING.AES_KEY,
                    Constant.DING.OWNER_KEY);

            // 获取请求中的加密数据
            String encryptMsg = json.getString("encrypt");

            // 解密
            String decryptMsg = callbackCrypto.getDecryptMsg(msg_signature, timeStamp, nonce, encryptMsg);

            log.info("【钉钉事件订阅】收到钉钉的事件订阅消息\n{}", decryptMsg);

            // 反序列化回调事件 json 数据
            JSONObject eventJson = JSON.parseObject(decryptMsg);

            String eventType = eventJson.getString("EventType");

            // 4. 根据EventType分类处理
            if ("check_url".equals(eventType)) {
                // 测试回调url的正确性
                log.info("测试回调url的正确性");
            } else {
                log.info("发生了：" + eventType + "事件");
                EventRes res = dingEventCallbackService.process(eventJson);
            }

            //  返回success的加密数据
            return callbackCrypto.getEncryptedMap("success");

        } catch (DingCallbackCrypto.DingTalkEncryptException | ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/call")
    public Map<String, String> getCallBack(
            HttpServletRequest request,
            @RequestParam(value = "msg_signature", required = false) String msg_signature,
            @RequestParam(value = "timestamp", required = false) String timeStamp,
            @RequestParam(value = "nonce", required = false) String nonce,
            @RequestBody(required = false) JSONObject json) throws DingCallbackCrypto.DingTalkEncryptException {

        log.info("【收到钉钉的 get 回调】url 为 {}", request.getRequestURL());

        DingCallbackCrypto callbackCrypto = new DingCallbackCrypto(
                Constant.DING.AES_TOKEN,
                Constant.DING.AES_KEY,
                Constant.DING.OWNER_KEY);

        String encryptMsg = json.getString("encrypt");

        // 解密
        String decryptMsg = callbackCrypto.getDecryptMsg(msg_signature, timeStamp, nonce, encryptMsg);

        log.info("【钉钉事件订阅】收到钉钉的事件订阅消息\n{}", decryptMsg);

        // 反序列化回调事件 json 数据
        JSONObject eventJson = JSON.parseObject(decryptMsg);

        //  返回success的加密数据
        return callbackCrypto.getEncryptedMap("success");

    }

}
