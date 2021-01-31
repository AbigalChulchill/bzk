package net.bzk.auth.service;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.stereotype.Service;

import net.bzk.auth.model.CreateUpdateDate;
import net.bzk.infrastructure.CommUtils;

@Service
public class CommService {

	@PrePersist
	protected void onCreate(CreateUpdateDate i) {
		Date now = CommUtils.nowUtc0();
		i.setCreateAt(now);
		i.setUpdateAt(now);
	}

	@PreUpdate
	protected void onUpdate(CreateUpdateDate i) {
		i.setUpdateAt(new Date());
	}

}
