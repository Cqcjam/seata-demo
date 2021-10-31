/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.seata.samples.nacos.service.impl;

import io.seata.core.context.RootContext;
import io.seata.samples.nacos.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The type Account service.
 *
 * @author jimin.jm @alibaba-inc.com
 */
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private JdbcTemplate jdbcTemplate;

    /**
     * Sets jdbc template.
     *
     * @param jdbcTemplate the jdbc template
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void debit(String userId, int money) {
        System.out.println("Account Service Begin... xid: " + RootContext.getXID());
        System.out.println("Deducting balance SQL: update account_tbl set money = money -" + money + "  where user_id = " +  userId);

        ClassPathXmlApplicationContext accountContext = new ClassPathXmlApplicationContext(
                new String[] {"spring/dubbo-account-service.xml"});
        JdbcTemplate storageJdbcTemplate = (JdbcTemplate)accountContext.getBean("jdbcTemplate");

        storageJdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
        System.out.println("Account Service End... xid: " + RootContext.getXID());
    }
}
