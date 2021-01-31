package net.bzk.auth.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.bzk.auth.model.Bill;
import net.bzk.auth.model.Bill.State;

public interface BillDao extends CrudRepository<Bill, String> {

	List<Bill> findAllByState(State s);

}
