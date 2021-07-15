package net.bzk.flow.model;

import java.io.Serializable;
import java.util.Date;

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
import net.bzk.flow.dto.ConvertInfra.VarMapConvert;
import net.bzk.flow.dto.ConvertInfra.VarValList;
import net.bzk.flow.dto.ConvertInfra.VarValListConvert;
import net.bzk.flow.model.var.VarMap;

@SuppressWarnings("serial")
@Data
@Entity
@EntityListeners(CommService.class)
@Table
public class RunLog implements Serializable, CreateUpdateDate {

    public static enum RunState {
        BoxStart, BoxLoop, EndFlow, LinkTo,
        StartAction, EndAction, ActionCall, ActionCallFail,
        ActionCallWarn, ActionResult, WhileLoopBottom,
        ConditionFail, ModelReplaced, PolyglotExecute, ConditionResult, BoxError, ScriptError

    }

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
    private RunState state;
    private boolean failed = false;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String exception;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String exceptionClazz;
    @Column(nullable = true, columnDefinition = "TEXT")
    @Convert(converter = VarValListConvert.class)
    private VarValList varVals;
    private String actionName;

    private Date updateAt;
    private Date createAt;

}
