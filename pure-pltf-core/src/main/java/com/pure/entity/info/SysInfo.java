package com.pure.entity.info;

import com.pure.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * System info
 *
 * @author gnl
 * @date 2023/5/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysInfo implements BaseInfo, Serializable {
    private static final long serialVersionUID = 3781143876648649225L;

    private String status;

    // environment
    private String javaRuntimeVersion;

    private String os;
    private String osVersion;
    private String osArch; // 64 or 32

    private String language;
    private String timezone;
    private String country;

}
