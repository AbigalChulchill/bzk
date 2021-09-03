package net.bzk.flow.api;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.flow.model.RunLog;
import net.bzk.flow.run.dao.RunLogDao;

@CrossOrigin(maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
        RequestMethod.OPTIONS, RequestMethod.HEAD}, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "run/log/")
public class RunLogController {
    @Inject
    private RunLogDao dao;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "{uid}", method = RequestMethod.GET, params = "type=runflow")
    public Page<RunLog> listByRunFlowUid(@PathVariable String uid, @RequestParam int page, @RequestParam int size) {
        Pageable paging = PageRequest.of(page, size);
        return dao.findByRunFlowUidOrderByCreateAtAsc(uid, paging);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "{uid}", method = RequestMethod.GET, params = "type=action")
    public Page<RunLog> listByActionUid(@PathVariable String uid, @RequestParam int page, @RequestParam int size) {
        Pageable paging = PageRequest.of(page, size);
        return dao.findByActionUidOrderByCreateAtAsc(uid, paging);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteBefore(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        dao.deleteByCreateAtBefore(date);
    }

}
