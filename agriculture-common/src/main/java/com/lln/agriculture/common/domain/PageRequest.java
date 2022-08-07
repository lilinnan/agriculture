package com.lln.agriculture.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求需要的数据
 *
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/21 18:56
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    @Builder.Default
    private int pageNum = 1;

    @Builder.Default
    private int pageSize = 0;

    @Builder.Default
    private String sort = "";

    @Builder.Default
    private String key = null;


    public String getSort() {
        if (sort.length() == 0) {
            return sort;
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = sort.toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                stringBuilder.append("_");
                stringBuilder.append(Character.toLowerCase(c));
                continue;
            }
            stringBuilder.append(c);
        }
        sort = stringBuilder.toString();
        sort = sort.replaceAll("\\+([\\w]+)", " $1" + " asc,");
        sort = sort.replaceAll("-([\\w]+)", " $1" + " desc,");
        sort = sort.substring(0, sort.length() - 1);
        return sort;
    }
}
