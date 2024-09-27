package kroryi.demo.repository;

import kroryi.demo.dto.*;
import kroryi.demo.service.BoardService;
import kroryi.demo.service.impl.BoardServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService service;
    @Autowired
    private BoardServiceImpl boardServiceImpl;

    @Test
    public void testRegister() {
        log.info("register->{}", service.getClass().getName());

        // 첫 번째 게시물 등록
        BoardDTO boardDTO1 = BoardDTO.builder()
                .title("게시판 등록 1")
                .content("내용 추가 1")
                .writer("작성자 1")
                .build();

        Long bno1 = service.register(boardDTO1);
        log.info("게시물 등록 1 -> {}", bno1);

        // 두 번째 게시물 등록
        BoardDTO boardDTO2 = BoardDTO.builder()
                .title("게시판 등록 2")
                .content("내용 추가 2")
                .writer("작성자 2")
                .build();

        Long bno2 = service.register(boardDTO2);
        log.info("게시물 등록 2 -> {}", bno2);
    }


    @Test
    public void testModify() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(98L)
                .title("게시판 수정1")
                .content("내용 수정1")
                .writer("이재준")
                .build();
        service.modify(boardDTO);
        log.info("게시판 수정--->{}", boardDTO);
    }

    @Test
    public void testRemove() {
        Long bno = 99L;
        service.remove(bno);
        log.info("remove->{}", bno);
    }

    @Test
    public void pageTest() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = service.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testRegisterWithImages() {
        log.info(service.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("file.... sample title")
                .content("Sample Content")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_ccc.jpg"
                )
        );

        Long bno = service.register(boardDTO);

        log.info("bno ->{}", bno);
    }

    @Test
    public void testReadAll() {
        Long bno = 101L;

        BoardDTO boardDTO = service.readOne(bno);

        log.info(boardDTO);
        for (String filename : boardDTO.getFileNames()) {
            log.info(filename);
        }
    }

    @Test
    public void testModifyFile() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("파일 업로그 게시물 수정 101번")
                .content("내용 수정 101번")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));

        service.modify(boardDTO);
    }

    @Test
    public void testListWithAll() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = service.listWithAll(pageRequestDTO);
        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());

            if (boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }

            log.info("------------------------");
        });
    }
}
