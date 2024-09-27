package kroryi.demo.repository;

import kroryi.demo.domain.Board;
import kroryi.demo.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);


    @Query(value = "select  now()", nativeQuery = true)
    String getTime();

    //Board fetch LAZY를 사용했기 떄문에 쿼리 두번 해야한다.
    // 그래서 Board findById 조회 후에 연관관꼐있는 Entity에 findBYIdWithImages를 추가로 호출한다.
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno=:bno")
    Optional<Board> findByIdWithImages(@Param("bno") Long bno);

}
