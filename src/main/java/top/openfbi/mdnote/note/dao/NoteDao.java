package top.openfbi.mdnote.note.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.openfbi.mdnote.note.model.Note;

@Mapper
public interface NoteDao extends BaseMapper<Note> {

}