package net.bzk.flow.run.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.flow.model.SavedFlow;
@Repository
public interface SavedFlowDao extends CrudRepository<SavedFlow, String> {

}
