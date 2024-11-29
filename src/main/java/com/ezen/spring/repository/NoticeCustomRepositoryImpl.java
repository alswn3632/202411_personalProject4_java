package com.ezen.spring.repository;

import com.ezen.spring.entity.Notice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ezen.spring.entity.QNotice.notice;

public class NoticeCustomRepositoryImpl implements NoticeCustomRepository{
    private final JPAQueryFactory queryFactory;

    public NoticeCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Notice> searchBoards(String type, String keyword, Pageable pageable) {
        // 조건이 없을 경우
        // select * from board where isDel = 'N' and title = '%aaa%';
        BooleanExpression condition = notice.isDel.eq("N");

        if(type != null && keyword != null){
            BooleanExpression extraCondition = null;
            String[] typearr = type.split("");
            for(String t : typearr){
                switch (t){
                    case "t" :
                        if(extraCondition == null){
                            extraCondition = notice.title.containsIgnoreCase(keyword);
                        }else{
                            extraCondition = extraCondition.or(notice.title.containsIgnoreCase(keyword));
                        }
                        break;
                    case "w" :
                        if(extraCondition == null){
                            extraCondition = notice.writer.containsIgnoreCase(keyword);
                        }else{
                            extraCondition = extraCondition.or(notice.writer.containsIgnoreCase(keyword));
                        }
                        break;
                    case "c" :
                        if(extraCondition == null){
                            extraCondition = notice.content.containsIgnoreCase(keyword);
                        }else{
                            extraCondition = extraCondition.or(notice.content.containsIgnoreCase(keyword));
                        }
                        break;
                }
            }
            condition = condition.and(extraCondition);
        }

        // 쿼리 삭성 및 페이징 적용
        List<Notice> result = queryFactory
                .selectFrom(notice)
                .where(condition)
                .orderBy(notice.noticeId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 검색된 데이터의 전체 개수
        long total = queryFactory
                .selectFrom(notice)
                .where(condition)
                .fetch().size();

        // .fetchCount(); >> .fetch().size()

        return new PageImpl<>(result, pageable, total);
    }
}
