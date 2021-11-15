package org.openelisglobal.analysisqaevent.service;

import org.openelisglobal.analysisqaevent.dao.AnalysisQaEventDAO;
import org.openelisglobal.analysisqaevent.valueholder.AnalysisQaEvent;
import org.openelisglobal.common.service.BaseObjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalysisQaEventServiceImpl extends BaseObjectServiceImpl<AnalysisQaEvent, String>
        implements AnalysisQaEventService {
    @Autowired
    protected AnalysisQaEventDAO baseObjectDAO;

    AnalysisQaEventServiceImpl() {
        super(AnalysisQaEvent.class);
    }

    @Override
    protected AnalysisQaEventDAO getBaseObjectDAO() {
        return baseObjectDAO;
    }
}
