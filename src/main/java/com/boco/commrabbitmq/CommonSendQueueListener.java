package com.boco.commrabbitmq;

import com.boco.cmsprotocolBody.ItemList;
import com.boco.cmsprotocolBody.PlayList;
import com.boco.cmsprotocolBody.WordList;
import com.boco.domain.CjxtGatherDataChr;
import com.boco.domain.JkptTxxtIssueLog;
import com.boco.protocolBody.*;
import com.boco.service.CmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
@RabbitListener(queues = "${dataProcessQueueName}")
public class CommonSendQueueListener {
    private static final Logger logger= LoggerFactory.getLogger(CommonSendQueueListener.class);

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @Autowired
    private CmsService cmsService;

    @Autowired
    private Environment env;

    @RabbitHandler
    public void process(String revdatabody) {
        try {
            System.out.println("接收DataProcess队列消息-->" + revdatabody);
            //JSONObject jsonobject = JSONObject.fromObject(revdatabody);
            Protocolbody revprotocolbody =  (Protocolbody)JSONToObj(revdatabody,Protocolbody.class);

            String busno = revprotocolbody.getBusinessNo();
            //System.out.println("接收Businessno: " + busno);
            String InfoTypeRev=revprotocolbody.getInfoType();

            String databody="";
            switch (InfoTypeRev)
            {
                case InfoType.MSG_CMD_CMS:
                    //System.out.println("InfoTypeRev-->" + InfoTypeRev);
                    MSG_CMD_CMS_DataProcess(revprotocolbody);
                    break;
                case InfoType.MSG_DATA_VD:
                case InfoType.MSG_DATA_CMS:
                    break;
                default :
                    System.out.println("非入库处理数据Businessno-->" + busno);
            }

        }catch ( Exception e) {
            logger.error("数据转发异常"+e.toString());
        }
    }

    /**
     * 监听队列数据处理
     * @param revprotocolbody
     */
    public void MSG_CMD_CMS_DataProcess(Protocolbody revprotocolbody) {
        try {
            if (revprotocolbody.getReturnState() != null) {
                ReturnState returnState = revprotocolbody.getReturnState();
                if (returnState.getReturnCode().equals(ReturnCode.ReturnCode_success)) {
                    DataProcess_Cjxt_GatherData_Chr(revprotocolbody);//情报板发送成功处理
                }
                DataProcess_jkpt_txxt_issuelog(revprotocolbody);//情报板发送成功失败处理
            }
        } catch (Exception e) {
            System.out.println("数据发送异常" + e.toString());
        }
    }

    /**
     * 情报板发送成功处理
     * @param revprotocolbody
     */
    public void DataProcess_Cjxt_GatherData_Chr(Protocolbody revprotocolbody){
        try{
            //String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String curTime = df.format(new Date());
            CjxtGatherDataChr cjxtGatherDataChr = new CjxtGatherDataChr();
            if (revprotocolbody.getSubPackage() != null) {
                cjxtGatherDataChr.setOrgId(Integer.valueOf(revprotocolbody.getSubPackage().getOrgId()));
                cjxtGatherDataChr.setDeviceId(Integer.valueOf(revprotocolbody.getSubPackage().getDevId()));
                if (revprotocolbody.getSubPackage().getDevVarInfoList().size() > 0) {
                    DevVarInfo devVarInfotemp=(DevVarInfo)(revprotocolbody.getSubPackage().getDevVarInfoList().get(0));

                    cjxtGatherDataChr.setDeviceVarTypeId(Integer.valueOf(devVarInfotemp.getDevVarTypeId()));
                    cjxtGatherDataChr.setGatherTime(curTime);
                    cjxtGatherDataChr.setData(devVarInfotemp.getDevVarValue());
                    cjxtGatherDataChr.setSendFlag(1);
                }
            }
            System.out.println("插入下发数据" + cjxtGatherDataChr.getDeviceId() + "  " + cjxtGatherDataChr.getData());
            int flag = cmsService.insertCjxtGatherDataChr(cjxtGatherDataChr);
            if (flag == 1) System.out.println(revprotocolbody.getSubPackage().getDevId() + "发送数据插入成功！");

        } catch (Exception e) {
            System.out.println("DataProcessCjxtGatherDataChr异常" + e.toString());
        }
    }
    /**
     * 情报板发送成功处理
     * @param revprotocolbody
     */
    public void DataProcess_jkpt_txxt_issuelog(Protocolbody revprotocolbody){
        try{
            //String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String curTime = df.format(new Date());
            JkptTxxtIssueLog jkptTxxtIssueLog = new JkptTxxtIssueLog();
            if (revprotocolbody.getSubPackage() != null) {
                jkptTxxtIssueLog.setOrgId(Integer.valueOf(revprotocolbody.getSubPackage().getOrgId()));
                jkptTxxtIssueLog.setDeviceId(Integer.valueOf(revprotocolbody.getSubPackage().getDevId()));
                jkptTxxtIssueLog.setDeviceTypeId(Integer.valueOf(revprotocolbody.getSubPackage().getDevId().substring(0,4)));

                if (revprotocolbody.getSubPackage().getDevVarInfoList().size() > 0) {
                    DevVarInfo devVarInfotemp = (DevVarInfo) (revprotocolbody.getSubPackage().getDevVarInfoList().get(0));
                    jkptTxxtIssueLog.setDevVarTypeId(Integer.valueOf(devVarInfotemp.getDevVarTypeId()));
                    jkptTxxtIssueLog.setIssueInfo(devVarInfotemp.getDevVarValue());
                    String PlaylistTemp = devVarInfotemp.getDevVarValue();
                    String wordStr=buildProtocal(PlaylistTemp);
                    jkptTxxtIssueLog.setIssueWordContent(wordStr);
                    ReturnState returnState = revprotocolbody.getReturnState();
                    String OperatFlag = "0";
                    if(returnState!=null) {
                        if (returnState.getReturnCode().equals(ReturnCode.ReturnCode_success))
                            OperatFlag = "0";
                        else OperatFlag = "1";
                    }
                    jkptTxxtIssueLog.setOperatFlag(OperatFlag);
                    jkptTxxtIssueLog.setOperatorId(revprotocolbody.getSubPackage().getUseId());
                    jkptTxxtIssueLog.setControlDate(curTime);
                }
                List<JkptTxxtIssueLog> list = cmsService.getJkptTxxtIssueLog(jkptTxxtIssueLog.getOrgId().toString());
                int flag = cmsService.insertJkptTxxtIssueLog(jkptTxxtIssueLog);
                if (flag == 1) System.out.println(revprotocolbody.getSubPackage().getDevId() + "插入消息日志成功！");
            }

        } catch (Exception e) {
            System.out.println("DataProcessCjxtGatherDataChr异常" + e.toString());
        }
    }

    /**
     * 取出播放表文字信息
     */
    public String buildProtocal(String PlaylistTemp) {
        String result = "";
        try {
            PlayList PlaylistValue=  (PlayList)JSONToObj(PlaylistTemp,PlayList.class);//下发播放表信息

            int id = 0;
            result = "";
            for(int i=0;i<PlaylistValue.getItemList().size();i++) {
                ItemList entity = PlaylistValue.getItemList().get(i);
                String itemProtocal = this.buildItemProtocal(entity, PlaylistValue.getDpt());
                if (i == PlaylistValue.getItemList().size() - 1)
                    result = result + itemProtocal;
                else
                    result = result + itemProtocal + "|";
            }


        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    /**
     * 将单条播放表转换为情报板协议
     * @return
     */
    private String buildItemProtocal(ItemList item,int dispScrType){
        String protocolString = "";
        protocolString += this.wordParaToString(item,dispScrType);
        return protocolString;
    }

    /**
     * 将可变情报板的文字参数转换为协议字符串
     * @param //Itemlist
     * @return
     */
    private String wordParaToString(ItemList item, int dispScrType){
        String result = "";
        if (item == null){
            return result;
        }

        List<WordList> list = item.getWordList();
        if (list == null || list.size() == 0){
            return result;
        }

        for(WordList para : list){
            //文字内容
            if (para.getWc() != null){
                result = result+para.getWc()+";";
            }
        }

        return result;
    }



    /**
     * json字符串与对象之间的转换
     * @param jsonStr
     * @param obj
     * @param <T>
     * @return
     */
    public static<T> Object JSONToObj(String jsonStr,Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
