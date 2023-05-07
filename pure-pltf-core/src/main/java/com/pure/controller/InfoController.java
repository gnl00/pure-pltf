package com.pure.controller;

import com.pure.config.CoreConfig;
import com.pure.entity.info.BaseInfo;
import com.pure.entity.info.SysInfo;
import com.pure.entity.vo.BaseInfoVo;
import com.pure.entity.vo.SysInfoVo;
import com.pure.service.impl.InfoServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * InfoController
 *
 * @author gnl
 * @since 2023/5/7
 */
@RestController
@RequestMapping("/info")
public class InfoController {

    // @Value("#{${server.port:8080}}")
    // @Value("#{${management.server.port} ?: ${server.port} ?: 8080}")
    // private String actuatorPort;

    private static final String DEFAULT_PORT = "8080";
    private static final String SERVER_PORT = "server.port";
    private static final String ACTUATOR_SERVER_PORT = "management.server.port";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CoreConfig coreConfig;

    @Autowired
    private InfoServiceImpl infoService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/sys")
    public ResponseEntity<SysInfoVo> getSysInfo() {

        SysInfo sysInfo = infoService.getSysInfo();
        SysInfoVo sysInfoVo = new SysInfoVo();
        BeanUtils.copyProperties(sysInfo, sysInfoVo);

        // Object health = getActuatorRestTemplate();

        return new ResponseEntity<>(sysInfoVo, HttpStatus.OK);
    }

    private Object getActuatorRestTemplate() {
        String actuatorServerPort = coreConfig.getValueFromProperties(ACTUATOR_SERVER_PORT);
        String serverPort = coreConfig.getValueFromProperties(SERVER_PORT);

        String port = null;
        if (Objects.nonNull(actuatorServerPort)) {
            port = actuatorServerPort;
        } else if (Objects.nonNull(serverPort)) {
            port = serverPort;
        } else {
            port = DEFAULT_PORT;
        }

        String urlPrefix = "http://localhost:" + port;
        String urlSuffix = "/actuator/health";

        Map<String, Object> healthMap = restTemplate.getForObject(urlPrefix + urlSuffix, Map.class);
        System.out.println(healthMap);

        return healthMap;
    }

    @GetMapping("/base")
    public ResponseEntity<BaseInfoVo> getBaseInfo() {

        BaseInfoVo baseVo = new BaseInfoVo();
        BaseInfo baseInfo = infoService.getBaseInfo();
        BeanUtils.copyProperties(baseInfo, baseVo);

        return new ResponseEntity<>(baseVo, HttpStatus.OK);
    }

}
