package com.boco.controller;

import com.boco.domain.JkptTxxtIssueLog;
import com.boco.service.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private CmsService cmsService;

    @RequestMapping(value="/hi")
    @ResponseBody
    public String query(){
        JkptTxxtIssueLog jkptTxxtIssueLog=new JkptTxxtIssueLog();
        jkptTxxtIssueLog.setOrgId(20300);
        jkptTxxtIssueLog.setDeviceId(22210001);
        jkptTxxtIssueLog.setDeviceTypeId(2221);
        jkptTxxtIssueLog.setDevVarTypeId(222101);
        //jkptTxxtIssueLog.setIssueInfo("{\"dph\":64,\"dpt\":1,\"dpw\":128,\"itemlist\":[{\"delay\":3,\"fc\":\"r\",\"fn\":\"s\",\"fs\":32,\"graphList\":[],\"mode\":1,\"wordList\":[{\"wc\":\"安全驾驶\",\"wx\":0,\"wy\":0},{\"wc\":\"平安回家\",\"wx\":0,\"wy\":32}]},{\"delay\":3,\"fc\":\"r\",\"fn\":\"s\",\"fs\":32,\"graphList\":[],\"mode\":1,\"wordList\":[{\"wc\":\"路途漫漫\",\"wx\":0,\"wy\":0},{\"wc\":\"文明相伴\",\"wx\":0,\"wy\":32}]}]}");
        jkptTxxtIssueLog.setIssueInfo("play.lst");
        jkptTxxtIssueLog.setIssueWordContent("测试");
        jkptTxxtIssueLog.setOperatFlag("1");
        jkptTxxtIssueLog.setOperatorId("333344");
        jkptTxxtIssueLog.setControlDate("2020-05-20 11:11:11");
        List<JkptTxxtIssueLog> list= cmsService.getJkptTxxtIssueLog(jkptTxxtIssueLog.getOrgId().toString());
        int flag=cmsService.insertJkptTxxtIssueLog(jkptTxxtIssueLog);
        //return "good luck："+list.get(10).getControldate().toString();


//        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        CjxtGatherDataChr cjxtGatherDataChr = new CjxtGatherDataChr();
//        cjxtGatherDataChr.setDeviceid(22210001);
//        cjxtGatherDataChr.setDevicevartypeid(123123);
//        cjxtGatherDataChr.setGathertime(curTime);
//        cjxtGatherDataChr.setData("org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='gathertime', mode=IN, javaType=class java.lang.Object, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting null for parameter #3 with JdbcType OTHER . Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. Cause: java.sql.SQLException: 无效的列类型: 1111");
//        cjxtGatherDataChr.setSendflag(1);
//        int flag1 = cmsService.insertCjxtGatherDataChr(cjxtGatherDataChr);
//        if (flag1 == 1) System.out.println("数据插入成功！");
//        else System.out.println("数据插入失败！");

        return "good luck：";
    }
}
