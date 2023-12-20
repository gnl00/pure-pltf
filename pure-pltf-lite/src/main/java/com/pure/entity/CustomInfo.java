package com.pure.entity;

import com.pure.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomInfo implements BaseInfo, Serializable {
    private static final long serialVersionUID = 7603067916535124527L;
    private String cusInfo1;
    private String cusInfo2;
    private String cusInfo3;
}
