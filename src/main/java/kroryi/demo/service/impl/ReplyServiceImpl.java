package kroryi.demo.service.impl;

import kroryi.demo.domain.Board;
import kroryi.demo.domain.Reply;
import kroryi.demo.dto.PageRequestDTO;
import kroryi.demo.dto.PageResponseDTO;
import kroryi.demo.dto.ReplyDTO;
import kroryi.demo.repository.BoardRepository;
import kroryi.demo.repository.search.ReplyRepository;
import kroryi.demo.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final PageRequestDTO pageRequestDTO;

    @Value("5")
    private int defaultPageRange;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Board board = boardRepository.findById(replyDTO.getBno())
                .orElseThrow(() -> new IllegalArgumentException("게시물 없음"));


        Reply reply = modelMapper.map(replyDTO, Reply.class);
        reply.setBoard(board);

        Long rno = replyRepository.save(reply).getRno();

        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepository.findById(rno);

        Reply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());

        Reply reply = replyOptional.orElseThrow();

        reply.changeText(replyDTO.getReplyText());

        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage() < 0 ? 0 : pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by("rno").ascending());

        Page<Reply> result=replyRepository.listOfBoard(bno,pageable);

        List<ReplyDTO> dtoList =result.getContent().stream().map(reply -> modelMapper.map(reply,ReplyDTO.class))
                .collect(Collectors.toList());


        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequest)
                .dtoList(dtoList)
                .pageRange(defaultPageRange)
                .total((int) result.getTotalElements())
                .build();
    }
}
