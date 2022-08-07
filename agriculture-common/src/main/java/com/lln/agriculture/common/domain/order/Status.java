package com.lln.agriculture.common.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/1 17:19
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    private Integer id;
    private String statusName;
}