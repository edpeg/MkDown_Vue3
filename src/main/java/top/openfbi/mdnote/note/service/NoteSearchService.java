package top.openfbi.mdnote.note.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;
import top.openfbi.mdnote.common.exception.ResultException;
import top.openfbi.mdnote.note.model.ElasticSearchNote;

import java.util.ArrayList;
import java.util.List;

/**
 * es业务层
 */
@Service
public class NoteSearchService {
    private ElasticsearchOperations elasticsearchOperations;
    private static final Logger logger
            = LoggerFactory.getLogger(NoteSearchService.class);

    public NoteSearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * 保存笔记到es
     */
    public ElasticSearchNote save(ElasticSearchNote elasticSearchNote) {
        return elasticsearchOperations.save(elasticSearchNote);
    }

    /**
     * 更新笔记到es
     */
//    public void update(ElasticSearchNote elasticSearchNote) {
//        elasticsearchOperations.update(elasticSearchNote);
//    }

    /**
     * 删除笔记
     */
    public void delete(long noteId) throws ResultException {
        // 创建es对象
        ElasticSearchNote elasticSearchNote = new ElasticSearchNote();
        // 设置es对象id
        elasticSearchNote.setId(noteId);
        // 根据id删除指定es文档
        elasticsearchOperations.delete(elasticSearchNote);
    }

    public void deleteAllNoteOfUser(long userId) {
        // 创建es对象
        Query byUserId = MatchQuery.of(m -> m.field("userId").query(userId))._toQuery();
        // 设置查询对象 条件为byUserId and byKey。查询目标为ElasticSearchNote，高亮目标为highlight
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b.must(byUserId)
                ))
                .build();
        elasticsearchOperations.delete(nativeQuery, ElasticSearchNote.class);
    }

    /**
     * 根据id查询笔记
     */
    public ElasticSearchNote findById(long id) {
        // 根据ID查询es中的文档，返回值为ElasticSearchNote
        return elasticsearchOperations.get(Long.toString(id), ElasticSearchNote.class);
    }

    /**
     * 根据笔记标题和内容搜索指定用户笔记
     */
    public List<SearchHit<ElasticSearchNote>> search(String keyword, Long userId) {
        // 需要高亮的字段particulars，title
        ArrayList<HighlightField> highlightFields = new ArrayList<HighlightField>();
        highlightFields.add(new HighlightField("content"));
        highlightFields.add(new HighlightField("title"));

        // 创建需要搜索的字段，content，title
        ArrayList<String> searchFields = new ArrayList<>() {
            {
                add("content");
                add("title");
            }
        };
        //设置上下文搜索宽度为5
        HighlightParameters highlightParameters = HighlightParameters.builder().withBoundaryMaxScan(5).build();
        // 设置高亮对象为上面设置的highlightFields
        Highlight highlight = new Highlight(highlightParameters, highlightFields);
        // 设置搜索根据userId条件语句对象
        Query byUserId = MatchQuery.of(m -> m.field("userId").query(userId))._toQuery();
        // 使用searchFields设置搜索条件语句对象，查询目标为searchKey
        Query byKey = MultiMatchQuery.of(m -> m.fields(searchFields).query(keyword))._toQuery();

        // 设置查询对象 条件为byUserId and byKey。查询目标为ElasticSearchNote，高亮目标为highlight
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b.must(byUserId, byKey)
                ))
                .withHighlightQuery(new HighlightQuery(highlight, ElasticSearchNote.class))
                .build();
        // 执行搜索语句，并设置返回类型为：ElasticSearchNote
        SearchHits<ElasticSearchNote> elasticSearchNoteSearchHits = elasticsearchOperations.search(nativeQuery, ElasticSearchNote.class);
        // 获取查询高亮结果

//        List<ElasticSearchNote> notes = elasticSearchNoteSearchHits.map(hit -> hit.getContent()).toList();
        return elasticSearchNoteSearchHits.getSearchHits();
    }
}
