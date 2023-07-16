package top.openfbi.mdnote.note.model.fe;

import lombok.Data;
import top.openfbi.mdnote.note.model.Note;

@Data
public class SimpleNote {

    public SimpleNote() {
    }

    public SimpleNote(Note note) {
        this.setId(note.getId());
        this.setTitle(note.getTitle());
        this.setContent(note.getContent());
    }

    /**
     * ID
     */
    private Long id;

    /**
     * 笔记标题
     */
    private String title;

    /**
     * 笔记内容
     */
    private String content;
}
