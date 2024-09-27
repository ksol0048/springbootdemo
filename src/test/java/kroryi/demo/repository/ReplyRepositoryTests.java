package kroryi.demo.repository;

import kroryi.demo.domain.Board;
import kroryi.demo.domain.Reply;
import kroryi.demo.dto.BoardListAllDTO;
import kroryi.demo.dto.BoardListReplyCountDTO;
import kroryi.demo.dto.ReplyDTO;
import kroryi.demo.repository.search.ReplyRepository;
import kroryi.demo.service.ReplyService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyService replyService;

    @Test
    public void testInsert() {
        Long bno = 123L;
        Board board = Board.builder()
                .bno(bno)
                .build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("첫번쨰 댓글")
                .replyer("구독자1")
                .build();

        replyRepository.save(reply);
    }

    @Test
    @Transactional
    public void testBoardReplies() {
        Long bno = 122L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> replies = replyRepository.listOfBoard(bno, pageable);

        replies.getContent().forEach(reply -> {
            log.info(reply);
            log.info(reply.getBoard());
        });
    }

    @Test
    public void testSearchReplyCount() {
        String[] types = {"t", "c", "w"};
        String keyword = "99";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getNumber());
        log.info(result.getSize());

        result.getContent().forEach(board -> log.info(board.toString()));
    }

    @Test
    public void testRegister() {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("ReplyDTO Text")
                .replyer("등록자")
                .bno(123L)
                .build();

        Long rno = replyService.register(replyDTO);

        log.info("replyService.register---->{}", rno);
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImage() {
        Optional<Board> result = boardRepository.findByIdWithImages(123L);

        Board board = result.orElseThrow();

        board.clearImages();

        for (int i = 0; i < 2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() {
        Long bno = 324L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll() {
        for (int i = 1; i <= 100; i++) {
            Board board = Board.builder()
                    .title("제목..." + i)
                    .content("내용..." + i)
                    .writer("작성자..." + i)
                    .build();

            for (int j = 0; j < 3; j++) {
                if (i % 5 == 0) {
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

//        boardRepository.searchWithAll(null,null,pageable);
        String[] types = {"t", "c", "w", "d"};
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, "제목", pageable);

        log.info("~~~~~~~~~~~~~~~~~~~~~");
        log.info(result.getTotalPages());

        result.getContent().forEach(boardListAllDTO -> {
            log.info("boardListAllDTO->{}",boardListAllDTO);
        });
    }

}
