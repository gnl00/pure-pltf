package com.pure.entity.info.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pure.entity.info.SysInfo;
import lombok.*;

/**
 * SysInfoVo
 *
 * @author gnl
 * @date 2023/5/7
 */
@Getter
@Setter
public class SysInfoVo extends SysInfo {
    @JsonIgnore
    private String osVersion;
}
