package com.tenco.csr_blog_v1.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query("""
            SELECT b FROM Board b
                JOIN FETCH b.user
                LEFT JOIN FETCH b.replies r 
                LEFT JOIN FETCH r.user 
                WHERE b.id = :boardId
        """)
    Optional<Board> findByIdJoinUserAndReplies(@Param("boardId") Integer boardId);
}
