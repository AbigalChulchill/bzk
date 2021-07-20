package net.bzk.flow.model.var;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class VarVal implements Serializable {

    private VarLv lv;
    private String key;
    private Object val;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VarVal other = (VarVal) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (lv != other.lv)
            return false;
        if (val == null) {
            if (other.val != null)
                return false;
        } else if (!val.equals(other.val))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((lv == null) ? 0 : lv.hashCode());
        result = prime * result + ((val == null) ? 0 : val.hashCode());
        return result;
    }

    public static VarVal gen(VarLv lv, String k, Object o) {
        VarVal ans = new VarVal();
        ans.setVal(o);
        ans.setKey(k);
        ans.setLv(lv);
        return ans;
    }

    public static HashMap<String,Object> toMap(List<VarVal> vvs){
        HashMap<String,Object> ans  = new HashMap<>();
        for(var vv : vvs){
            ans.put(vv.getKey(),vv.getVal());
        }
        return ans;
    }

}