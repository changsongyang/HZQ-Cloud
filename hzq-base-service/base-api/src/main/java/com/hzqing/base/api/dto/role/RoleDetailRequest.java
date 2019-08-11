package com.hzqing.base.api.dto.role;

import com.hzqing.common.core.result.AbstractRequest;
import lombok.Data;

/**
 * @author hzqing
 * @date 2019-08-10 00:35
 */
@Data
public class RoleDetailRequest extends AbstractRequest {

    private int id;

    /**
     * 请求参数进行校验
     */
    @Override
    public void checkParams() {
        // 如果教师失败，抛出异常
        if (id == 0){
            throw new RuntimeException("..");
        }
    }
}