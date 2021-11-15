/**
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is OpenELIS code.
 *
 * Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
 */
package org.openelisglobal.testreflex.daoimpl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.openelisglobal.analysis.valueholder.Analysis;
import org.openelisglobal.common.daoimpl.BaseDAOImpl;
import org.openelisglobal.common.exception.LIMSRuntimeException;
import org.openelisglobal.common.log.LogEvent;
import org.openelisglobal.common.util.StringUtil;
import org.openelisglobal.common.util.SystemConfiguration;
import org.openelisglobal.testanalyte.valueholder.TestAnalyte;
import org.openelisglobal.testreflex.dao.TestReflexDAO;
import org.openelisglobal.testreflex.valueholder.TestReflex;
import org.openelisglobal.testresult.valueholder.TestResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author diane benz 11/17/2007 instead of printing StackTrace log error
 */
@Component
@Transactional
public class TestReflexDAOImpl extends BaseDAOImpl<TestReflex, String> implements TestReflexDAO {

    public TestReflexDAOImpl() {
        super(TestReflex.class);
    }

//	@Override
//	public void deleteData(List testReflexs) throws LIMSRuntimeException {
//		// add to audit trail
//		try {
//
//			for (int i = 0; i < testReflexs.size(); i++) {
//				TestReflex data = (TestReflex) testReflexs.get(i);
//
//				TestReflex oldData = readTestReflex(data.getId());
//				TestReflex newData = new TestReflex();
//
//				String sysUserId = data.getSysUserId();
//				String event = IActionConstants.AUDIT_TRAIL_DELETE;
//				String tableName = "TEST_REFLEX";
//				auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
//			}
//		} catch (RuntimeException e) {
//			// bugzilla 2154
//			LogEvent.logError("TestReflexDAOImpl", "AuditTrail deleteData()", e.toString());
//			throw new LIMSRuntimeException("Error in TestReflex AuditTrail deleteData()", e);
//		}
//
//		try {
//			for (int i = 0; i < testReflexs.size(); i++) {
//				TestReflex data = (TestReflex) testReflexs.get(i);
//				// bugzilla 2206
//				data = readTestReflex(data.getId());
//				entityManager.unwrap(Session.class).delete(data);
//				// entityManager.unwrap(Session.class).flush(); // CSL remove old
//				// entityManager.unwrap(Session.class).clear(); // CSL remove old
//			}
//		} catch (RuntimeException e) {
//			// bugzilla 2154
//			LogEvent.logError("TestReflexDAOImpl", "deleteData()", e.toString());
//			throw new LIMSRuntimeException("Error in TestReflex deleteData()", e);
//		}
//	}

//	@Override
//	public boolean insertData(TestReflex testReflex) throws LIMSRuntimeException {
//		try {
//			// bugzilla 1482 throw Exception if record already exists
//			if (duplicateTestReflexExists(testReflex)) {
//				throw new LIMSDuplicateRecordException(
//						"Duplicate record exists for " + TestServiceImpl.getUserLocalizedTestName(testReflex.getTest())
//								+ BLANK + testReflex.getTestAnalyte().getAnalyte().getAnalyteName() + BLANK
//								+ testReflex.getTestResult().getValue() + BLANK
//								+ TestServiceImpl.getUserLocalizedTestName(testReflex.getAddedTest()));
//			}
//
//			String id = (String) entityManager.unwrap(Session.class).save(testReflex);
//			testReflex.setId(id);
//
//			// bugzilla 1824 inserts will be logged in history table
//
//			String sysUserId = testReflex.getSysUserId();
//			String tableName = "TEST_REFLEX";
//			auditDAO.saveNewHistory(testReflex, sysUserId, tableName);
//
//			// entityManager.unwrap(Session.class).flush(); // CSL remove old
//			// entityManager.unwrap(Session.class).clear(); // CSL remove old
//		} catch (RuntimeException e) {
//			// bugzilla 2154
//			LogEvent.logError("TestReflexDAOImpl", "insertData()", e.toString());
//			throw new LIMSRuntimeException("Error in TestReflex insertData()", e);
//		}
//
//		return true;
//	}

//	@Override
//	public void updateData(TestReflex testReflex) throws LIMSRuntimeException {
//		// bugzilla 1482 throw Exception if record already exists
//		try {
//			if (duplicateTestReflexExists(testReflex)) {
//				throw new LIMSDuplicateRecordException(
//						"Duplicate record exists for " + TestServiceImpl.getUserLocalizedTestName(testReflex.getTest())
//								+ BLANK + testReflex.getTestAnalyte().getAnalyte().getAnalyteName() + BLANK
//								+ testReflex.getTestResult().getValue() + BLANK
//								+ TestServiceImpl.getUserLocalizedTestName(testReflex.getAddedTest()));
//			}
//		} catch (RuntimeException e) {
//			// bugzilla 2154
//			LogEvent.logError("TestReflexDAOImpl", "updateData()", e.toString());
//			throw new LIMSRuntimeException("Error in TestReflex updateData()", e);
//		}
//
//		TestReflex oldData = readTestReflex(testReflex.getId());
//		TestReflex newData = testReflex;
//
//		// add to audit trail
//		try {
//
//			String sysUserId = testReflex.getSysUserId();
//			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
//			String tableName = "TEST_REFLEX";
//			auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
//		} catch (RuntimeException e) {
//			// bugzilla 2154
//			LogEvent.logError("TestReflexDAOImpl", "AuditTrail updateData()", e.toString());
//			throw new LIMSRuntimeException("Error in TestReflex AuditTrail updateData()", e);
//		}
//
//		try {
//			entityManager.unwrap(Session.class).merge(testReflex);
//			// entityManager.unwrap(Session.class).flush(); // CSL remove old
//			// entityManager.unwrap(Session.class).clear(); // CSL remove old
//			// entityManager.unwrap(Session.class).evict // CSL remove old(testReflex);
//			// entityManager.unwrap(Session.class).refresh // CSL remove old(testReflex);
//		} catch (RuntimeException e) {
//			// bugzilla 2154
//			LogEvent.logError("TestReflexDAOImpl", "updateData()", e.toString());
//			throw new LIMSRuntimeException("Error in TestReflex updateData()", e);
//		}
//	}

    @Override
    @Transactional(readOnly = true)
    public void getData(TestReflex testReflex) throws LIMSRuntimeException {
        try {
            TestReflex tr = entityManager.unwrap(Session.class).get(TestReflex.class, testReflex.getId());
            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old
            if (tr != null) {
                PropertyUtils.copyProperties(testReflex, tr);
            } else {
                testReflex.setId(null);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // bugzilla 2154
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in TestReflex getData()", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestReflex> getAllTestReflexs() throws LIMSRuntimeException {
        List<TestReflex> list = null;
        try {
            String sql = "from TestReflex t order by t.testAnalyte.analyte.analyteName";
            org.hibernate.Query query = entityManager.unwrap(Session.class).createQuery(sql);
            // query.setMaxResults(10);
            // query.setFirstResult(3);
            list = query.list();
            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old
        } catch (RuntimeException e) {
            // bugzilla 2154
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in TestReflex getAllTestReflexs()", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestReflex> getPageOfTestReflexs(int startingRecNo) throws LIMSRuntimeException {
        List<TestReflex> list;
        try {
            // calculate maxRow to be one more than the page size
            int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);

            // bugzilla 1399 - still need to figure out how to sort (3rd sort
            // column) for dictionary values - requires further step of getting
            // value from dictionary table before sorting
            String sql = "from TestReflex t order by t.testAnalyte.analyte.analyteName";
            org.hibernate.Query query = entityManager.unwrap(Session.class).createQuery(sql);
            query.setFirstResult(startingRecNo - 1);
            query.setMaxResults(endingRecNo - 1);

            list = query.list();
            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old
        } catch (RuntimeException e) {
            // bugzilla 2154
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in TestReflex getPageOfTestReflexs()", e);
        }

        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.openelisglobal.testreflex.dao.TestReflexDAO#
     * getTestReflexesByTestResult
     * (org.openelisglobal.testreflex.valueholder.TestReflex,
     * org.openelisglobal.testresult.valueholder.TestResult)
     */
    @Override
    @Transactional(readOnly = true)
    public List<TestReflex> getTestReflexesByTestResult(TestResult testResult) throws LIMSRuntimeException {
        try {
            String sql = "from TestReflex t where t.testResult.id = :testResultId";
            org.hibernate.Query query = entityManager.unwrap(Session.class).createQuery(sql);
            query.setInteger("testResultId", Integer.parseInt(testResult.getId()));

            List<TestReflex> list = query.list();
            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old

            return list;

        } catch (RuntimeException e) {
            // bugzilla 2154
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in TestReflex getTestReflexesByTestResult(TestResult testResult)", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.openelisglobal.testreflex.dao.TestReflexDAO#
     * getTestReflexesByTestResult
     * (org.openelisglobal.testreflex.valueholder.TestReflex,
     * org.openelisglobal.testresult.valueholder.TestResult)
     */
    @Override
    @Transactional(readOnly = true)
    public List<TestReflex> getTestReflexesByTestResultAndTestAnalyte(TestResult testResult, TestAnalyte testAnalyte)
            throws LIMSRuntimeException {
        try {
            // bugzilla 1404 testResultId is mapped as testResult.id now
            String sql = "from TestReflex t where t.testResult.id = :param and t.testAnalyte.id = :param2";
            org.hibernate.Query query = entityManager.unwrap(Session.class).createQuery(sql);
            query.setParameter("param", testResult.getId());
            query.setParameter("param2", testAnalyte.getId());

            List<TestReflex> list = query.list();
            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old

            return list;

        } catch (RuntimeException e) {
            // bugzilla 2154
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in TestReflex getTestReflexesByTestResult(TestResult testResult)", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.openelisglobal.testreflex.dao.TestReflexDAO#
     * getTestReflexesByTestResult
     * (org.openelisglobal.testreflex.valueholder.TestReflex,
     * org.openelisglobal.testresult.valueholder.TestResult) bugzilla 1798 find out
     * if a test with a parent was reflexed or linked
     */
    @Override
    public boolean isReflexedTest(Analysis analysis) throws LIMSRuntimeException {
        try {
            List<TestReflex> list = null;

            if (analysis.getParentAnalysis() != null && analysis.getParentResult() != null) {
                String sql = "from TestReflex t where t.testResult.id = :param and t.testAnalyte.analyte.id = :param2 and t.test.id = :param3 and t.addedTest.id = :param4";
                org.hibernate.Query query = entityManager.unwrap(Session.class).createQuery(sql);
                query.setParameter("param", analysis.getParentResult().getTestResult().getId());
                query.setParameter("param2", analysis.getParentResult().getAnalyte().getId());
                query.setParameter("param3", analysis.getParentAnalysis().getTest().getId());
                query.setParameter("param4", analysis.getTest().getId());

                list = query.list();
                // entityManager.unwrap(Session.class).flush(); // CSL remove old
                // entityManager.unwrap(Session.class).clear(); // CSL remove old
            }

            return list != null && list.size() > 0;

        } catch (RuntimeException e) {
            // bugzilla 2154
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in TestReflex getTestReflexesByTestResult(TestResult testResult)", e);
        }
    }

    @Override

    @Transactional(readOnly = true)
    public List<TestReflex> getTestReflexsByTestResultAnalyteTest(String testResultId, String analyteId, String testId)
            throws LIMSRuntimeException {
        if (!GenericValidator.isBlankOrNull(testResultId) && !GenericValidator.isBlankOrNull(analyteId)
                && !GenericValidator.isBlankOrNull(testId)) {
            try {
                List<TestReflex> list = null;

                String sql = "from TestReflex t where t.testResult.id = :testResultId and t.testAnalyte.analyte.id = :analyteId and t.test.id = :testId";
                org.hibernate.Query query = entityManager.unwrap(Session.class).createQuery(sql);
                query.setInteger("testResultId", Integer.parseInt(testResultId));
                query.setInteger("analyteId", Integer.parseInt(analyteId));
                query.setInteger("testId", Integer.parseInt(testId));

                list = query.list();
                // entityManager.unwrap(Session.class).flush(); // CSL remove old
                // entityManager.unwrap(Session.class).clear(); // CSL remove old

                return list;
            } catch (RuntimeException e) {
                LogEvent.logError(e.toString(), e);
                throw new LIMSRuntimeException(
                        "Error in TestReflex getTestReflexByTestResultAnalyteTest(String testResultId, String analyteId, String testId)",
                        e);
            }
        }
        return new ArrayList<>();
    }

    public TestReflex readTestReflex(String idString) {
        TestReflex tr = null;
        try {
            tr = entityManager.unwrap(Session.class).get(TestReflex.class, idString);
            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old
        } catch (RuntimeException e) {
            // bugzilla 2154
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in TestReflex readTestReflex()", e);
        }
        return tr;
    }

    // bugzilla 1411
    @Override
    @Transactional(readOnly = true)
    public Integer getTotalTestReflexCount() throws LIMSRuntimeException {
        return getCount();
    }

    // bugzilla 1482
    @Override
    public boolean duplicateTestReflexExists(TestReflex testReflex) throws LIMSRuntimeException {
        try {

            List<TestReflex> list = new ArrayList();

            // not case sensitive hemolysis and Hemolysis are considered
            // duplicates
            String sql = "from TestReflex t where t.test.localizedTestName = :localizedTestNameId and "
                    + "trim(lower(t.testAnalyte.analyte.analyteName)) = :analyteName and "
                    + "t.testResult.id = :resultId and " + "t.addedTest.localizedTestName = :addedTestNameId or "
                    + "trim(lower(t.actionScriptlet.scriptletName)) = :scriptletName  ) and " + " t.id != :testId";
            Query query = entityManager.unwrap(Session.class).createQuery(sql);
            query.setInteger("localizedTestName",
                    Integer.parseInt(testReflex.getTest().getLocalizedTestName().getId()));
            query.setString("analyteName",
                    testReflex.getTestAnalyte().getAnalyte().getAnalyteName().toLowerCase().trim());
            query.setInteger("resultId", Integer.parseInt(testReflex.getTestResult().getId()));
            query.setInteger("addedTestNameId", testReflex.getAddedTest() == null ? -1
                    : Integer.parseInt(testReflex.getAddedTest().getLocalizedTestName().getId()));
            query.setString("scriptletName", testReflex.getActionScriptlet() == null ? null
                    : testReflex.getActionScriptlet().getScriptletName().toLowerCase().trim());

            String testReflexId = StringUtil.isNullorNill(testReflex.getId()) ? "0" : testReflex.getId();

            query.setInteger("testId", Integer.parseInt(testReflexId));

            list = query.list();
            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old

            if (list.size() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (RuntimeException e) {
            LogEvent.logError(e.toString(), e);
            throw new LIMSRuntimeException("Error in duplicateTestReflexExists()", e);
        }
    }

    @Override

    @Transactional(readOnly = true)
    public List<TestReflex> getTestReflexsByTestAndFlag(String testId, String flag) throws LIMSRuntimeException {
        if (GenericValidator.isBlankOrNull(testId)) {
            return new ArrayList<>();
        }

        List<TestReflex> reflexList = null;

        try {
            Query query = null;
            if (GenericValidator.isBlankOrNull(flag)) {
                String sql = "from TestReflex tr where tr.testResult.test.id = :id";
                query = entityManager.unwrap(Session.class).createQuery(sql);
                query.setInteger("id", Integer.parseInt(testId));
            } else {
                String sql = "from TestReflex tr where tr.testResult.test.id = :id and tr.flags = :flag";
                query = entityManager.unwrap(Session.class).createQuery(sql);
                query.setInteger("id", Integer.parseInt(testId));
                query.setString("flag", flag);
            }
            reflexList = query.list();

            // entityManager.unwrap(Session.class).flush(); // CSL remove old
            // entityManager.unwrap(Session.class).clear(); // CSL remove old

        } catch (RuntimeException e) {
            handleException(e, "getTestReflexsByTestAndFlag()");
        }

        return reflexList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestReflex> getFlaggedTestReflexesByTestResult(TestResult testResult, String flag)
            throws LIMSRuntimeException {
        if (GenericValidator.isBlankOrNull(flag)) {
            return new ArrayList<>();
        }

        try {
            String sql = "from TestReflex t where t.testResult.id = :testResultId and t.flags = :flag";
            Query query = entityManager.unwrap(Session.class).createQuery(sql);
            query.setInteger("testResultId", Integer.parseInt(testResult.getId()));
            query.setString("flag", flag);
            List<TestReflex> list = query.list();
            // closeSession(); // CSL remove old
            return list;
        } catch (RuntimeException e) {
            handleException(e, "getFlaggedTestReflexesByTestResult");
        }

        return null;
    }

}