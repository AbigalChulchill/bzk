package net.bzk.flow.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.bzk.auth.model.CreateUpdateDate;
import net.bzk.auth.service.CommService;
import net.bzk.flow.model.ConvertInfra.VarMapConvert;
import net.bzk.flow.model.ConvertInfra.VarValList;
import net.bzk.flow.model.ConvertInfra.VarValListConvert;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.utils.LogUtils.BoxRunState;

@SuppressWarnings("serial")
@Data
@Entity
@EntityListeners(CommService.class)
@Table
public class RunLog implements Serializable, CreateUpdateDate {
	
	public static enum RunState {
		BoxStart, BoxLoop, BoxLoopDone, EndFlow, LinkTo, StartAction, EndAction, ActionCall, ActionCallFail,
		ActionCallWarn, ActionResult, WhileLoopBottom,

	}
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private long id;
    @Embedded
	private Uids uids;

	private String msg;
	@Column(nullable = true, columnDefinition = "TEXT")
	@Convert(converter = VarMapConvert.class)
	private VarMap flowVar;
	@Column(nullable = true, columnDefinition = "TEXT")
	@Convert(converter = VarMapConvert.class)
	private VarMap boxVar;
	private String boxName;
	private BoxRunState state;
	private boolean failed = false;
	private String exception;
	private String exceptionClazz;
	@Column(nullable = true, columnDefinition = "TEXT")
	@Convert(converter = VarValListConvert.class)
	private VarValList varVals;
	private String actionName;

	private Date updateAt;
	private Date createAt;




}
