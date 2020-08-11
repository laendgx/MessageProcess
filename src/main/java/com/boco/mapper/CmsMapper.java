package com.boco.mapper;

import com.boco.domain.CjxtGatherDataChr;
import com.boco.domain.JkptTxxtIssueLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CmsMapper {
    //查询情报板发布日志信息
    @Select("select * from JKPT_TXXT_ISSUELOG where ORGID=#{orgid}")
    public List<JkptTxxtIssueLog> getJkptTxxtIssueLog(@Param("orgid") String orgid);
    //查询情报板发布日志信息
    @Insert("insert into jkpt_txxt_issuelog(id, orgid, deviceid,devicevartypeid, devicetypeid, operatorid, controldate, issuewordcontent, issueinfo, operatflag)" +
            " values(" +
            " JKPT_TXXT_ISSUELOG_ID_SEQ.nextval," +
            " #{orgid,jdbcType=NUMERIC}," +
            " #{deviceid,jdbcType=NUMERIC}," +
            " #{devicevartypeid,jdbcType=NUMERIC}," +
            " #{devicetypeid,jdbcType=NUMERIC}," +
            " #{operatorid,jdbcType=VARCHAR}," +
            " to_date(#{controldate},'yyyy-MM-dd HH24:mi:ss')," +
            " #{issuewordcontent,jdbcType=VARCHAR}," +
            " #{issueinfo,jdbcType=VARCHAR}," +
            " #{operatflag,jdbcType=VARCHAR} " +
            " )")
    public int insertJkptTxxtIssueLog(@Param("orgid") Integer orgid, @Param("deviceid") Integer deviceid,
                                      @Param("devicevartypeid") Integer devicevartypeid,
                                      @Param("devicetypeid") Integer devicetypeid,
                               @Param("operatorid") String operatorid, @Param("controldate") String controldate,
                               @Param("issuewordcontent") String issuewordcontent,
                                      @Param("issueinfo") String issueinfo,
                               @Param("operatflag") String operatflag);

    //更新情报板发布日志信息
    @Update("update JKPT_TXXT_ISSUELOG t" +
            " tset t.operatflag = #{status,jdbcType=VARCHAR}" +
            " where t.orgid = #{orgId,jdbcType=NUMERIC}" +
            "   and t.deviceid = #{deviceId,jdbcType=NUMERIC}" +
            "   and t.operatflag = '1'" +
            "   and t.controldate in (select max(k.controldate)" +
            "   from JKPT_TXXT_ISSUELOG k" +
            "   where k.orgid = #{orgId1,jdbcType=NUMERIC}" +
            "   and k.deviceid = #{deviceId1,jdbcType=NUMERIC}" +
            "   and k.operatflag = '1')")
    public  int updateJKPT_TXXT_ISSUELOG( @Param("operatflag") String operatflag,@Param("orgid") Integer orgid,@Param("deviceid") Integer deviceid,
                                  @Param("orgid1") Integer orgid1,@Param("deviceid1") Integer deviceid1);

    //插入情报板发布信息
    @Insert("insert into JKPT_CJXT_GATHERDATA_CHR(orgid,deviceid, devicevartypeid, gathertime, sendflag, data) " +
            "values(" +
            " #{orgid, jdbcType=NUMERIC}, " +
            " #{deviceid, jdbcType=NUMERIC}, " +
            " #{devicevartypeid, jdbcType=NUMERIC}, " +
            " to_date(#{gathertime},'yyyy-MM-dd HH24:mi:ss')," +
            " #{sendflag, jdbcType=NUMERIC}, " +
            " #{data, jdbcType=VARCHAR}" +
            ")")
    public int insertGatherDataChr(@Param("orgid") Integer orgid,@Param("deviceid") Integer deviceid,@Param("devicevartypeid") Integer devicevartypeid,
                            @Param("gathertime") String gathertime,@Param("sendflag") Integer sendflag,@Param("data") String data);

    //更新情报板发布信息
    @Update("update JKPT_CJXT_GATHERDATA_CHR " +
            " set data = #{data, jdbcType=VARCHAR}," +
            " gathertime = to_date(#{gathertime},'yyyy-MM-dd HH24:mi:ss')" +
            " where orgid=#{orgid, jdbcType=NUMERIC} and deviceid=#{deviceid, jdbcType=NUMERIC} and devicevartypeid=#{devicevartypeid, jdbcType=NUMERIC} and " +
            " gathertime=to_date(#{oldgathertime},'yyyy-MM-dd HH24:mi:ss')")
    public int updateGatherDataChr(@Param("data") String data,@Param("gathertime") String gathertime,
                                   @Param("orgid") Integer orgid,@Param("deviceid") Integer deviceid,
                            @Param("devicevartypeid") Integer devicevartypeid,
                            @Param("oldgathertime") String oldgathertime);

    //获取指定情报板的发布信息
    @Select("select orgid,deviceid,devicevartypeid, gathertime, sendflag, data from jkpt_cjxt_gatherdata_chr " +
            " where orgid=#{orgid, jdbcType=NUMERIC} and deviceid = #{deviceid, jdbcType=NUMERIC} and devicevartypeid = #{devicevartypeid, jdbcType=NUMERIC} and " +
            " gathertime=(select max(gathertime) from jkpt_cjxt_gatherdata_chr" +
            " where orgid=#{torgid, jdbcType=NUMERIC} and deviceid=#{tdeviceid, jdbcType=NUMERIC}  and devicevartypeid = #{tdevicevartypeid, jdbcType=NUMERIC} )")
    public CjxtGatherDataChr getSelectGatherDataChr(@Param("orgid") Integer orgid,@Param("deviceid") Integer deviceid, @Param("devicevartypeid") Integer devicevartypeid,
                                                    @Param("torgid") Integer torgid,  @Param("tdeviceid") Integer tdeviceid, @Param("tdevicevartypeid") Integer tdevicevartypeid);
}
