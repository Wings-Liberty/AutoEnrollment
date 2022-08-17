package org.cloud.constant;

public interface Constant {

    /**
     * 访问权限
     */
    interface AUTH_ROLE {
        String ROOT = "root";
        String ADMIN = "admin";
    }

    interface DING {
        /**
         * 用于钉钉事件订阅的常量
         */
        String AES_KEY = "HSqPDRVxeFv6ZRk3jR2ko0utkwbKLEDQybXfFueUrkz";

        String AES_TOKEN = "z04zo6BUptVu8HLq66gdLg7yy1jOi";

        String OWNER_KEY = "dingvjbmmillamfrjnuw";

        // 用户部门变更通知
        String USER_DEPT_MODIFY_EVENT = "user_modify_org";

        // 用户入群通知
        String USER_JOIN_GROUP = "chat_add_member";

        // 用户主动离群通知
        String USER_LEFT_GROUP = "chat_quit";

        // 用户被管理员删除通知
        String USER_REMOVE_GROUP = "chat_remove_member";

    }

    /**
     * 用户入职状态 & 用户资源分配状态 & 用户入职请求状态
     * <p>
     * 和 DB 表 fms_enroll_request 的 status 字段有关
     */
    interface ENROLL_STATUS {
        int UNTREATED = 0; // 未处理
        int ALLOCATING = 1; // 正在分配资源
        int ALLOCATE_ERROR = 2; // 资源分配时出现异常
        int FINISHED = 3; // 已同意 // TODO: 2022/8/16  list 接口不展示 3 状态的数据
        int REFUSE = 4; // 已拒绝
    }

    /**
     * 已经分配过资源的员工
     */
    interface EMP_STATUS {
        int ALLOCATING = 0; // 正在分配资源
        int RELEASING = 1; // 正在释放资源
        int WORKING = 2; // 在职
        int QUIT = 3; // 离职 // TODO: 2022/8/16  list 接口不展示 3 状态的数据
    }

}
