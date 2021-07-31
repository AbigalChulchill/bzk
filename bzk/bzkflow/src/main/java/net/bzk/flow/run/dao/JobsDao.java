package net.bzk.flow.run.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.flow.model.Job;
import net.bzk.flow.model.Action.SubFlowAction;

@Repository
public interface JobsDao extends CrudRepository<Job, String> {

    public default Collection<Job> listDepends(String uid) {
        Set<Job> ans = new HashSet<>();
        var tar = findById(uid).get();
        recListDepends(ans, tar);
        return ans;
    }

    private void recListDepends(Set<Job> ans, Job sf) {
        if (ans.stream().anyMatch(_f -> StringUtils.equals(_f.getUid(), sf.getUid()))) {
            return;
        }
        ans.add(sf);
        Set<SubFlowAction> sas = sf.getModel().listAllActions().stream().filter(a -> a instanceof SubFlowAction)
                .map(a -> (SubFlowAction) a).collect(Collectors.toSet());

        Set<Job> csf = sas.stream().map(a -> findJob(a)).collect(Collectors.toSet());
        for (Job sfchild : csf) {
            recListDepends(ans, sfchild);
        }

    }

    private Job findJob(SubFlowAction a) {
        try {
            Job ans = findById(a.getFlowUid()).get();
            return ans;
        } catch (Exception e) {
            String j = JsonUtils.toJson(a);
            throw new BzkRuntimeException(j, e);
        }


    }


}
