package com.spider.repo;

import com.spider.domain.NewsMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by wq on 2017/5/7.
 */
public interface NewsMessageRepo extends PagingAndSortingRepository<NewsMessage,Integer>,
        JpaSpecificationExecutor<NewsMessage> {
}
