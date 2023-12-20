package com.pure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pure.BaseInfo;
import com.pure.config.CoreConfig;
import com.pure.entity.info.AppInfo;
import com.pure.entity.info.SysInfo;
import com.pure.entity.info.vo.AppInfoVo;
import com.pure.entity.info.vo.SysInfoVo;
import com.pure.handler.DefaultInfoHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static com.pure.constant.CoreConstant.JAVA_CLASS_PATH;

/**
 * InfoController
 *
 * @author gnl
 * @date 2023/5/7
 */
@Slf4j
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
    private ConfigurableApplicationContext ac;

    @Autowired
    private CoreConfig config;

    @Autowired
    private DefaultInfoHandler defaultInfo;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Map<String, Object>> index() {
        Map<String, Object> infos = getInfos();
        return new ResponseEntity<>(infos, HttpStatus.OK);
    }

    private Map<String, Object> getInfos() {
        Map<String, Object> infos = new TreeMap<>();
        SysInfo sysInfo = defaultInfo.getSysInfo();
        AppInfo appInfo = defaultInfo.getAppInfo();
        BaseInfo additionalInfo = defaultInfo.getAdditionalInfo();
        try {
            ObjectMapper om = new ObjectMapper();
            String sysJSON = om.writeValueAsString(sysInfo);
            String appJSON = om.writeValueAsString(appInfo);
            String additionalJSON = om.writeValueAsString(additionalInfo);
            /*log.info(" ==== json ====");
            log.info(sysJSON);
            log.info(appJSON);*/
            Map<String, Object> sysMap = om.readValue(sysJSON, Map.class);
            Map<String, Object> appMap = om.readValue(appJSON, Map.class);
            Map<String, Object> additionalMap = om.readValue(additionalJSON, Map.class);
            /*log.info(" ==== map ====");
            log.info(sysMap.toString());
            log.info(appMap.toString());*/
            infos.putAll(sysMap);
            infos.putAll(appMap);
            infos.putAll(additionalMap);
        } catch (JsonProcessingException e) {
            log.error("getInfo JsonProcessingException: {}", e.getMessage());
        }
        return infos;
    }

    private Map<String, Object> systemProperties() {
        return ac.getEnvironment().getSystemProperties();
    }

    @GetMapping("/classpath")
    public String classpath() {
        return (String) systemProperties().get(JAVA_CLASS_PATH);
    }

    @GetMapping("/sys")
    public ResponseEntity<BaseInfo> getSysInfo() {
        SysInfo sysInfo = defaultInfo.getSysInfo();
        SysInfoVo sysInfoVo = new SysInfoVo();
        BeanUtils.copyProperties(sysInfo, sysInfoVo);
        return new ResponseEntity<>(sysInfoVo, HttpStatus.OK);
    }

    @GetMapping("/app")
    public ResponseEntity<BaseInfo> getAppInfo() {
        AppInfoVo baseVo = new AppInfoVo();
        AppInfo appInfo = defaultInfo.getAppInfo();
        BeanUtils.copyProperties(appInfo, baseVo);
        return new ResponseEntity<>(baseVo, HttpStatus.OK);
    }

    @GetMapping("/additional")
    public ResponseEntity<BaseInfo> getCustomInfo() {
        BaseInfo additionalInfo = defaultInfo.getAdditionalInfo();
        return new ResponseEntity<>(additionalInfo, HttpStatus.OK);
    }

    private Map<String, Object> getHealthFromActuator() {
        String actuatorServerPort = config.getValueFromProperties(ACTUATOR_SERVER_PORT);
        String serverPort = config.getValueFromProperties(SERVER_PORT);

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

}
