package net.bzk.flow.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.bzk.auth.model.CreateUpdateDate;
import net.bzk.auth.service.CommService;
import net.bzk.flow.dto.ConvertInfra;
import net.bzk.flow.dto.ConvertInfra.VarMapConvert;
import net.bzk.flow.enums.Enums;
import net.bzk.flow.model.var.VarMap;

@SuppressWarnings("serial")
@Data
@Entity
@EntityListeners(CommService.class)
@Table
public class RunLog implements Serializable, CreateUpdateDate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    private String flowUid;
    private String runFlowUid;
    private String boxUid;
    private String runBoxUid;
    private String actionUid;
    private String runActionUid;
    private String refRunFlowUid;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String msg;
    @Column(nullable = true, columnDefinition = "TEXT")
    @Convert(converter = VarMapConvert.class)
    private VarMap flowVar;
    @Column(nullable = true, columnDefinition = "TEXT")
    @Convert(converter = VarMapConvert.class)
    private VarMap boxVar;
    private String boxName;
    @Enumerated(EnumType.STRING)
    private Enums.RunState state;
    @Enumerated(EnumType.STRING)
    private Enums.LogLv logLv = Enums.LogLv.DEBUG;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String exception;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String exceptionClazz;
    @Column(nullable = true, columnDefinition = "TEXT")
    @Convert(converter = ConvertInfra.MapConvert.class)
    private HashMap<String, Object> varVals;
    private String actionName;

    private Date updateAt;
    private Date createAt;

}
