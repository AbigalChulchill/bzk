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
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("serial")
@Data
@EntityListeners(CommService.class)
@Document(collection = "run_log")
public class RunLog implements Serializable, CreateUpdateDate {

    @Id
    private String uid;

    private String flowUid;
    private String runFlowUid;
    private String boxUid;
    private String runBoxUid;
    private String actionUid;
    private String runActionUid;
    private String refRunFlowUid;

    private String msg;

    private VarMap flowVar;

    private VarMap boxVar;
    private String boxName;

    private Enums.RunState state;

    private Enums.LogLv logLv = Enums.LogLv.DEBUG;

    private String exception;

    private String exceptionClazz;

    private HashMap<String, Object> varVals;
    private String actionName;

    private Date updateAt;
    private Date createAt;

}
