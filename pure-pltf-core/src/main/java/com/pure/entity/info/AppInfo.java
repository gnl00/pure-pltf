package com.pure.entity.info;

import com.pure.base.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * AppInfo
 *
 * @author gnl
 * @date 2023/5/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo implements BaseInfo, Serializable {
    private static final long serialVersionUID = -240755954909637933L;
    private String appName;
    private String appVersion;
    private Map<String, Object> ext;
}
