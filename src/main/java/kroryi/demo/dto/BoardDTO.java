package kroryi.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import kroryi.demo.domain.Board;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Board}
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO implements Serializable {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;
    private Long bno;
    private List<String> fileNames;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    private String writer;
    private String address;
}