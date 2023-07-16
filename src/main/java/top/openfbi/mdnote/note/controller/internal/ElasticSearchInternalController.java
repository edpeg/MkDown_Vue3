package top.openfbi.mdnote.note.controller.internal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.*;
import top.openfbi.mdnote.common.exception.ResultException;
import top.openfbi.mdnote.config.ResponseResultBody;
import top.openfbi.mdnote.note.model.ElasticSearchNote;
import top.openfbi.mdnote.note.service.NoteSearchService;
import top.openfbi.mdnote.user.util.Session;

import java.util.List;


/**
 * es数据库请求接口
 */
@RestController
@RequestMapping("/internal/ElasticSearchNote")
@ResponseResultBody
public class ElasticSearchInternalController {
    @Autowired
    private NoteSearchService noteSearchService;
    private static final Logger logger
            = LoggerFactory.getLogger(ElasticSearchInternalController.class);

    /**
     * 保存笔记到es
     */
    @PostMapping("/save")
    public ElasticSearchNote save(@RequestBody ElasticSearchNote elasticSearchNote) {
        return noteSearchService.save(elasticSearchNote);
    }

    /**
     * 删除笔记
     */
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable("id") long id) throws ResultException {
        noteSearchService.delete(id);
    }

    /**
     * 根据id查询笔记
     */
    @GetMapping("/person/{id}")
    public ElasticSearchNote findById(@PathVariable("id") long id) {
        return noteSearchService.findById(id);
    }

    /**
     * 根据条件搜索笔记
     */
    @GetMapping("/search/{q}")
    public List<SearchHit<ElasticSearchNote>> search(@PathVariable("q") String q) {
        return noteSearchService.search(q, Session.getUser().getId());
    }
}

