package net.bzk.data.service;
import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.stereotype.Service;

import net.bzk.data.model.CreateUpdateDate;
import net.bzk.infrastructure.CommUtils;

@Service
public class CommService {

	@PrePersist
	protected void onCreate(CreateUpdateDate i) {
		CommUtils.pl("onCreate " + i);
		i.setCreateAt(new Date());
		i.setUpdateAt(new Date());
	}

	@PreUpdate
	protected void onUpdate(CreateUpdateDate i) {
		i.setUpdateAt(new Date());
	}

}