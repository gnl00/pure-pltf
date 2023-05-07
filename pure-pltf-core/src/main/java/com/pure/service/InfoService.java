package com.pure.service;

import com.pure.entity.info.BaseInfo;
import com.pure.entity.info.SysInfo;

/**
 * InfoService
 *
 * @author gnl
 * @since 2023/5/7
 */
public interface InfoService {

    BaseInfo getBaseInfo();

    SysInfo getSysInfo();

}
