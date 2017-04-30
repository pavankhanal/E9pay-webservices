package com.e9pay.e9pay.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import com.e9pay.e9pay.api.dao.AbstractTransactionalDaoTest.NullDatasetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.testng.annotations.AfterClass;

@ContextConfiguration(classes = DaoTestContext.class)
@Transactional
@TestExecutionListeners(listeners = TransactionDbUnitTestExecutionListener.class)
@DbUnitConfiguration(dataSetLoader = NullDatasetLoader.class)
public abstract class AbstractTransactionalDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    @AfterClass
    public void deleteTestData() {
        Session ss = sessionFactory.openSession();
        try {
            ss.doWork(new Work() {
                @Override
                public void execute(Connection conn) throws SQLException {
                    setReferentialIntegrity(conn, Boolean.FALSE);

                    List<String> tableNames = getTableNames(conn);

                    deleteFromTables(conn, tableNames);

                    setReferentialIntegrity(conn, Boolean.TRUE);
                }

                private void deleteFromTables(Connection conn, List<String> tableNames) throws SQLException {

                    for (String tableName : tableNames) {
                        try (
                            PreparedStatement ps = conn.prepareStatement(new StringBuilder("DELETE FROM ").append(tableName)
                                .append(";")
                                .toString())
                        ) {
                            ps.executeUpdate();
                        }
                    }
                }

                private List<String> getTableNames(Connection conn) throws SQLException {
                    List<String> tableNames = new LinkedList<>();
                    try (PreparedStatement preparedStatement = conn.prepareStatement("SHOW TABLES;")) {
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            while (resultSet.next()) {
                                tableNames.add(resultSet.getString(1));
                            }
                        }
                    }
                    return tableNames;
                }

                private void setReferentialIntegrity(Connection conn, Boolean bool) throws SQLException {
                    try (PreparedStatement truePS = conn.prepareStatement("SET REFERENTIAL_INTEGRITY " + Boolean.toString(bool).toUpperCase())) {
                        truePS.execute();
                    }
                }
            });
        } finally {
            ss.close();
        }
    }

    public static class NullDatasetLoader extends FlatXmlDataSetLoader {

        @Override
        protected IDataSet createDataSet(Resource resource) throws Exception {
            ReplacementDataSet dataSet = new ReplacementDataSet(super.createDataSet(resource));

            dataSet.addReplacementObject("[null]", null);

            return dataSet;
        }
    }
}
