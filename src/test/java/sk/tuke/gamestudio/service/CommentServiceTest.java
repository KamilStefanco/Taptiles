package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tuke.gamestudio.SpringClient;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = SpringClient.class)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    public void testAddComment() {
        commentService.reset();

        commentService.addComment(new Comment("kamil", "Taptiles", "super", new Date()));

        List<Comment> comments = commentService.getComments("Taptiles");
        assertEquals(1, comments.size());
        assertEquals("Taptiles", comments.get(0).getGame());
        assertEquals("kamil", comments.get(0).getPlayer());
        assertEquals("super", comments.get(0).getComment());

    }

    @Test
    public void testGetComments() {
        commentService.reset();

        Date date = new Date();

        commentService.addComment(new Comment("Zuza", "Taptiles", "jej", date));
        commentService.addComment(new Comment("Peto", "Taptiles", "wau", date));
        commentService.addComment(new Comment("Alfonz", "Taptiles", "jaj", date));
        commentService.addComment(new Comment("Kubo", "Taptiles", "super", date));
        commentService.addComment(new Comment("Anna", "mahjong", "neviem", date));

        List<Comment> comments = commentService.getComments("Taptiles");

        assertEquals(4, comments.size());

        assertEquals("Taptiles", comments.get(3).getGame());
        assertEquals("Kubo", comments.get(3).getPlayer());
        assertEquals("super", comments.get(3).getComment());
        assertEquals(date, comments.get(3).getCommentedOn());

        assertEquals("Taptiles", comments.get(2).getGame());
        assertEquals("Alfonz", comments.get(2).getPlayer());
        assertEquals("jaj", comments.get(2).getComment());
        assertEquals(date, comments.get(2).getCommentedOn());

        assertEquals("Taptiles", comments.get(0).getGame());
        assertEquals("Zuza", comments.get(0).getPlayer());
        assertEquals("jej", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

    @Test
    public void testReset() {
        commentService.reset();

        assertEquals(0, commentService.getComments("Taptiles").size());
    }
}
