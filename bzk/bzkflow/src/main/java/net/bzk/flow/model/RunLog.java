package net.bzk.flow.model;

import lombok.Data;
import net.bzk.flow.enums.Enums;
import net.bzk.flow.model.var.VarMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    @Indexed(name = "flow_uid_index", direction = IndexDirection.DESCENDING)
    private String runFlowUid;
    private String boxUid;
    private String runBoxUid;
    @Indexed(name = "action_uid_index", direction = IndexDirection.DESCENDING)
    private String actionUid;
    private String runActionUid;
    private String refFlowUid;
    @Indexed(name = "ref_run_flow_uid_index",  direction = IndexDirection.ASCENDING)
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

    @Indexed(name = "create_at_index", direction = IndexDirection.DESCENDING, expireAfterSeconds = 60 * 60 * 20)
    private Date createAt;

}
