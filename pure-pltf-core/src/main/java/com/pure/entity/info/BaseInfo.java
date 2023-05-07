package com.pure.entity.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * BaseInfo
 *
 * @author gnl
 * @since 2023/5/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseInfo implements Serializable {

    private static final long serialVersionUID = -240755954909637933L;

    private String appName;
    private String appVersion;

    private String serverPort;
    private String status; // UP or DOWN

    private String appEncoding;
    private Map<String, Object> ext;
}
