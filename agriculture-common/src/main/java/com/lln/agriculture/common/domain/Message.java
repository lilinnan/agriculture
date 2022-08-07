package com.lln.agriculture.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/6 16:01
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Builder.Default
    private int status = 200;

    @Builder.Default
    private String message = "操作成功";

    @Builder.Default
    private Object data = "";
}
