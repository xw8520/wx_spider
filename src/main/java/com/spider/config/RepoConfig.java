package com.spider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by wq on 2017/5/7.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.spider.repo")
public class RepoConfig {

}
