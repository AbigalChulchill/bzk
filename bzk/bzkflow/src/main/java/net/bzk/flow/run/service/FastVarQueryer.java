package net.bzk.flow.run.service;

import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import net.bzk.flow.model.var.VarVal;
import net.bzk.infrastructure.JsonUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import net.bzk.flow.api.dto.DtoVarQuery;
import net.bzk.flow.enums.VarLv;
import net.bzk.flow.enums.VarLv.VarKey;
import net.bzk.flow.run.action.ActionCall.Uids;

@Service
@Scope("prototype")
public class FastVarQueryer implements Function<String, Object> {
    @Inject
    private RunVarService service;


    @Getter
    private Uids uids;

    public FastVarQueryer init(Uids u) {
        uids = u;
        return this;
    }

    /**
     * @param fqe $= box #= flow {empty}= not spect
     * @return
     */
    public Object g(String fqe) {
        return f(fqe).get();
    }

    public Optional<Object> f(String fqe) {
        DtoVarQuery dq = new DtoVarQuery();
        VarKey fs = VarLv.checkLvByPrefix(fqe);
        dq.setKey(fs.getKey());
        dq.setPoint(fs.getLv());
        dq.setUids(uids);
        return service.findByQuery(dq);
    }

    public void put(String path,String o){
        Object v = JsonUtils.stringToValue(o);
        VarKey fs = VarLv.checkLvByPrefix(path);
        VarVal val = new VarVal();
        val.setVal(v);
        val.setKey(fs.getKey());
        val.setLv(fs.getLv());
        service.putVarVal(uids,val);
    }

    public void putObj(String path,Object o){
        VarKey fs = VarLv.checkLvByPrefix(path);
        VarVal val = new VarVal();
        val.setVal(o);
        val.setKey(fs.getKey());
        val.setLv(fs.getLv());
        service.putVarVal(uids,val);
    }


    @Override
    public Object apply(String s) {
        return f(s).orElse(null);
    }
}
