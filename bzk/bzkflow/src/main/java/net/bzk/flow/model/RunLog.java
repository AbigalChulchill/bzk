package net.bzk.flow.model;

import lombok.Data;
import net.bzk.flow.enums.Enums;
import net.bzk.flow.model.var.VarMap;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("serial")
@Data
@Document(collection = "run_log")
public class RunLog implements Serializable {

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

    private Date createAt;

}
