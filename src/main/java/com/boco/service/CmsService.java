package com.boco.service;

import com.boco.domain.CjxtGatherDataChr;
import com.boco.domain.JkptTxxtIssueLog;
import com.boco.mapper.CmsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CmsService {
    @Autowired
    private CmsMapper cmsMapper;

    private static final Logger logger= LoggerFactory.getLogger(CmsService.class);

    public List<JkptTxxtIssueLog> getJkptTxxtIssueLog(String orgid) {
         List<JkptTxxtIssueLog> JkptTxxtIssueLogs=new ArrayList<>();
        try{
            JkptTxxtIssueLogs =cmsMapper.getJkptTxxtIssueLog(orgid);
        } catch (Exception e) {
            logger.error("getJkptTxxtIssueLog数据转发异常"+e.toString());
        }
        return JkptTxxtIssueLogs;
    }

    public int insertJkptTxxtIssueLog(JkptTxxtIssueLog jkptTxxtIssueLog) {
        int flag=0;
        try{
//            logger.info("insertJkptTxxtIssueLog-->"+jkptTxxtIssueLog.getOrgId()+"   "+
//                    jkptTxxtIssueLog.getDeviceId()+"   "+jkptTxxtIssueLog.getDevVarTypeId()+"   "+
//                    jkptTxxtIssueLog.getDeviceTypeId()+"   "+jkptTxxtIssueLog.getOperatorId()+"   "+
//                    jkptTxxtIssueLog.getControlDate()+"   "+jkptTxxtIssueLog.getIssueWordContent()+"   "+
//                    jkptTxxtIssueLog.getIssueInfo()+"   "+
//                    jkptTxxtIssueLog.getOperatFlag());
            flag =cmsMapper.insertJkptTxxtIssueLog(jkptTxxtIssueLog.getOrgId(),
                jkptTxxtIssueLog.getDeviceId(),jkptTxxtIssueLog.getDevVarTypeId(),
                    jkptTxxtIssueLog.getDeviceTypeId(),jkptTxxtIssueLog.getOperatorId(),
                jkptTxxtIssueLog.getControlDate(),jkptTxxtIssueLog.getIssueWordContent(),
                    jkptTxxtIssueLog.getIssueInfo(),
                jkptTxxtIssueLog.getOperatFlag());
        } catch (Exception e) {
            logger.error("insertJkptTxxtIssueLog数据异常"+e.toString());
        }
        return flag;
    }

    public int insertCjxtGatherDataChr(CjxtGatherDataChr cjxtGatherDataChr)
    {
        int flag=0;
        try{
            CjxtGatherDataChr cjxtGatherDataChrTemp= cmsMapper.getSelectGatherDataChr(cjxtGatherDataChr.getOrgId(),cjxtGatherDataChr.getDeviceId(),
                    cjxtGatherDataChr.getDeviceVarTypeId(),cjxtGatherDataChr.getOrgId(),cjxtGatherDataChr.getDeviceId(),cjxtGatherDataChr.getDeviceVarTypeId());
            if(cjxtGatherDataChrTemp==null) {
                flag = cmsMapper.insertGatherDataChr(cjxtGatherDataChr.getOrgId(),cjxtGatherDataChr.getDeviceId(), cjxtGatherDataChr.getDeviceVarTypeId(),
                        cjxtGatherDataChr.getGatherTime(), cjxtGatherDataChr.getSendFlag(), cjxtGatherDataChr.getData());
            }else
            {
                flag = cmsMapper.updateGatherDataChr(cjxtGatherDataChr.getData(),cjxtGatherDataChr.getGatherTime(),
                        cjxtGatherDataChr.getOrgId(),cjxtGatherDataChrTemp.getDeviceId(),cjxtGatherDataChrTemp.getDeviceVarTypeId(),cjxtGatherDataChrTemp.getGatherTime());
            }
        } catch (Exception e) {
            logger.error("insertCjxtGatherDataChr数据转发异常"+e.toString());
        }
        return flag;
    }
}
