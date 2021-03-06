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
import io.seata.samples.nacos.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The type Storage service.
 *
 * @author jimin.jm @alibaba-inc.com
 */
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

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
    public void deduct(String commodityCode, int count) {
        System.out.println("Storage Service Begin ... xid: " + RootContext.getXID());
        System.out.println("Deducting inventory SQL: update storage_tbl set count = count - " + count + "  where commodity_code = " +
            commodityCode);

        ClassPathXmlApplicationContext accountContext = new ClassPathXmlApplicationContext(
                new String[] {"spring/dubbo-storage-service.xml"});
        JdbcTemplate storageJdbcTemplate = (JdbcTemplate)accountContext.getBean("jdbcTemplate");


        storageJdbcTemplate.update("update storage_tbl set count = count - ? where commodity_code = ?",
            new Object[] {count, commodityCode});
        System.out.println("Storage Service End ... " + RootContext.getXID());

    }

}
