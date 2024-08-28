package kroryi.demo.repository;

import kroryi.demo.domain.Board;
import kroryi.demo.repository.search.BoardSearch;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);


    @Query(value="select  now()", nativeQuery = true)
    String getTime();


}
