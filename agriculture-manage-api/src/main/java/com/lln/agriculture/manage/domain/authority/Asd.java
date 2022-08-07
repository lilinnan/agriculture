package com.lln.agriculture.manage.domain.authority;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Asd {
    private Integer id;

    private String asd;

    private String asdd;

    private Date time;
}