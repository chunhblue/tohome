package cn.com.bbut.iy.itemmaster.service.bm;

import java.util.Collection;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.IyResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.AjaxResultBmDto;
import cn.com.bbut.iy.itemmaster.dto.bm.BmCodeDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmItemResultDto;
import cn.com.bbut.iy.itemmaster.dto.bm.BmJsonParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmResuleDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmSubmitDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmSysSubmitDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmUserInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmViewDetailDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmViewMainDTO;
import cn.com.bbut.iy.itemmaster.entity.IyBm;
import cn.com.bbut.iy.itemmaster.entity.IyBmCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCk;
import cn.com.bbut.iy.itemmaster.entity.User;

/**
 * @author songxz
 */
public interface BmService {
    /**
     * 根据bm类型取得对应bm编码<br>
     * IY_BM_CODE表中取得对应编码作为基数，向bm正式表和ck表中判断是否存在此code，如果存在则加1
     * 在此进行验证，直到有未使用的编码，或编码全部被占用<br>
     * 编码规则：<br>
     * 01捆绑类型bm编码范围为001~199。 <br>
     * 02混合类型bm编码范围为200~299<br>
     * 03固定组合类型bm编码范围为300~599，共300个编码可用。<br>
     * 04阶梯折扣类型编码范围为001~999，共999个编码可用。<br>
     * 05AB组类型编码范围为001~999，共999个编码可用。 <br>
     * 注意：类型01 02 03 规则可能有变更(暂时不用以下规则，待需要使用时，将修改注释)<br>
     * 01捆绑类型bm编码范围为001~199以及900~999，共299个编码可用。<br>
     * 02混合类型bm编码范围为200~299以及700~899，共300个编码可用。<br>
     * 03固定组合类型bm编码范围为300~699，共400个编码可用。<br>
     * 
     * @param type
     * @return
     */
    BmCodeDTO getBmCodeByType(String type);

    /**
     * 得到bm 登录序列号<br>
     * 从ck表中得到最大的登陆序列号+1后返回10位序号,例如：0000000001
     * 
     * @return
     */
    String getBmNewNo();

    /**
     * 根据bm登录序列号得到下一个副序列号<br>
     * 
     * @return
     */
    String getBmNewNoSub(String newNo);

    /**
     * 更新bm编号，根据类型变更code
     * 
     * @param type
     * @param code
     * @return
     */
    int updateBmCodeByTypeAndCode(String type, String code);

    /**
     * 取得指定商品编号的商品(m表)
     * 
     * @param item1
     * @return
     */
    BmItemResultDto getItemInfoByItem1(String item1);

    /**
     * 验证指定商品和店铺在bm流程中是否存在（包含ck和正式）
     * 
     * @param item1
     * @param stores
     * @return
     */
    AjaxResultDto isItemBmExist(String item1, String stores);

    /**
     * 验证各个店铺的控制记录中是否包含了商品的销售日期期间
     * 
     * @param itemSystem
     *            商品系统码
     * @param stores
     *            店铺号集合逗号分隔
     * @param startDate
     *            yyyyMMdd
     * @param endDate
     *            yyyyMMdd
     * @return
     */
    AjaxResultDto verdictItemIndate(String itemSystem, String stores, String startDate,
            String endDate);

    /**
     * 根据参数得到 该单品的c表数据，其中由销售日期作为进货/销售单价的取值范围
     * 
     * @param itemSystem
     * @param stores
     * @param startDate
     * @param endDate
     * @return
     */
    AjaxResultBmDto getItemStoreInfo(String itemSystem, String stores, String startDate,
            String endDate);

    /**
     * Addbm 此方法用于权限为 P-BM-011和P-BM-031 的采购和系统部Add修改使用 <br>
     * 该方法Add时会向ck表和his表增加数据
     * 
     * @param identity
     *            提交人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
     * @param paramJson
     *            提交的bm所有信息 json格式
     * @param u
     *            当前登录人
     * @return
     */
    AjaxResultDto insertBmData(Integer identity, String paramJson, User u);

    /**
     * 取得采购人员的所属dpt，如果 该采购人员的dpt为多个 并且属于一个事业部 则用xx9 等方式显示
     * 
     * @param pCode
     *            操作权限
     * @return
     */
    BmUserInfoDTO getStaffDpt(String pCode);

    /**
     * 取得店铺人员的所属店铺
     * 
     * @param pCode
     *            操作权限
     * @return
     */
    BmUserInfoDTO getStaffStore(String pCode);

    /**
     * Addck和his表数据
     * 
     * @param bmDataCk
     * @param u
     * @return
     */
    AjaxResultDto insertBmCkAndHis(BmSubmitDataDTO bmDataCk, User u);

    /**
     * Add正是表数据和his表数据
     * 
     * @param bmData
     * @param u
     * @return
     */
    AjaxResultDto insertBmAndHis(BmSysSubmitDataDTO bmData, User u);

    /**
     * 得到bm数据，其中根据bm商品状态会向不同的表取数据，最终拼接为画面表格使用的dto返回
     * 
     * @param param
     *            其中包含了分页和检索使用的json数据，json解析使用BmJsonParamDTO
     * 
     * @return
     */
    GridDataDTO<BmListGridDataDTO> getData(BmParamDTO param);

    /**
     * 根据检索条件得到bm ck表数据
     * 
     * @param param
     * @param limitStart
     * @param limitEnd
     * @param orderByClause
     * @return
     */
    BmResuleDataDTO getBmCkList(BmJsonParamDTO param);

    /**
     * 根据检索条件得到bm 正是表数据
     * 
     * @param param
     * @param limitStart
     * @param limitEnd
     * @param orderByClause
     * @return
     */
    BmResuleDataDTO getBmList(BmJsonParamDTO bmJsonParam);

    /**
     * 根据参数得到bm数据对象
     * 
     * @param bmCode
     *            bm编号
     * @param bmType
     *            bm类型
     * @param tabletype
     *            数据源表 0 正式表，1 ck表
     * @return
     */
    BmViewMainDTO getBmViewData(String bmCode, String bmType, Integer tabletype);

    /**
     * 删除bm数据，根据 tableType 判断删除的目标表
     * 
     * @param bmCode
     * @param bmType
     * @param tableType
     *            数据源表 0 正式表，1 ck表
     * @param identity
     *            操作人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
     * @return
     */
    AjaxResultDto deleteBmByParam(String bmCode, String bmType, Integer tableType,
            Integer identity, User u);

    /**
     * 查询item m表数据，得到商品名称
     * 
     * @param itemCode
     * @return
     */
    AjaxResultBmDto getItemInfoByCode(String itemCode);

    /**
     * 拿到当前登录的人的权限所属的需要审核的数据量
     * 
     * @param pCode
     *            指定权限
     * @return
     */
    List<BmListGridDataDTO> getSecretaryCheckCount(String pCode);

    /**
     * 需要紧急审核的数据量
     * 
     * @return
     */
    Integer getUrgencyCountCount(List<BmListGridDataDTO> checkCount);

    /**
     * 向ck表取得主档数据
     * 
     * @param bmCode
     * @param bmType
     * @return
     */
    IyBmCk getBmCkBaseByCodeAndType(String bmCode, String bmType);

    /**
     * 向正式表取得主档数据
     * 
     * @param bmCode
     * @param bmType
     * @return
     */
    IyBm getBmBaseByCodeAndType(String bmCode, String bmType);

    /**
     * ck取得明细数据
     * 
     * @param bmCode
     * @param bmType
     * @return
     */
    List<IyBmItemCk> getBmCkItemsByCoundAndType(String bmCode, String bmType);

    /**
     * 正是表取得明细数据 <br>
     * 将返回的数据结构转换为ck格式后再返回给调用接口，主要是为了统一结构好处理
     * 
     * @param bmCode
     * @param bmType
     * @return
     */
    List<IyBmItemCk> getBmItemsByCoundAndType(String bmCode, String bmType);

    /**
     * 根据角色id集合和身份得到当前人的资源字符串，“，”分隔，<br>
     * 如果当前人是部资源 则返回 xx9
     * 
     * @param roleIds
     * @param identity
     * @return
     */
    String getCheckResources(Collection<Integer> roleIds, Integer identity);

    /**
     * 确认/驳回 order
     * 
     * @param bmCode
     *            编码
     * @param bmType
     *            类型
     * @param staffResource
     *            操作人资源
     * @param dataResource
     *            该bm被评审的资源
     * @param opFlg
     *            操作类型
     * @param rejectreason
     *            理由
     * @param identity
     *            操作人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
     * @param u
     *            操作人/登录人
     * @return
     */
    AjaxResultDto updateAffirmBm(String bmCode, String bmType, String staffResource,
            String dataResource, String opFlg, String rejectreason, Integer identity, User u);

    /**
     * 审核/驳回 order
     * 
     * @param bmCode
     * @param bmType
     * @param staffResource
     *            操作人资源
     * @param opFlg
     * @param rejectreason
     * @param identity
     * @param u
     * @return
     */
    AjaxResultDto updateCheckBm(String bmCode, String bmType, String staffResource, String opFlg,
            String rejectreason, Integer identity, User u);

    /**
     * 审核/驳回 order
     * 
     * @param bmCode
     * @param bmType
     * @param bmTypeCode
     * @param staffResource
     *            操作人资源
     * @param opFlg
     * @param rejectreason
     * @param identity
     * @param u
     * @return
     */
    AjaxResultDto updateCheckBm(String bmCode, String bmType, String bmTypeCode,
            String staffResource, String opFlg, String rejectreason, Integer identity, User u);

    /**
     * 根据bm编码和类型得到正是表中bm对应数据
     * 
     * @param bmCode
     * @param bmType
     * @param identity
     * @return
     */
    AjaxResultBmDto getDataByCodeType(String bmCode, String bmType, Integer identity);

    /**
     * 批量删除指定日期之前的正式表bm数据（不包含指定日期），
     * 
     * @param endDate
     *            指定日期之前
     * @param u
     * @return
     */
    AjaxResultDto deleteListBmByEndDate(String endDate, User u);

    /**
     * 将数据解析为查看画面中input所使用的对象
     * 
     * @param bmType
     * @param bmMain
     * @param bmDetail
     * @return
     */
    List<BmViewDetailDTO> createInputHtml(String bmType, IyBmCk bmMain, List<IyBmItemCk> bmDetail);

    /**
     * 根据 角色id和权限得到最终的权限资源匹配值，仅适用于bm相关内容
     * 
     * @param roleIds
     * @param pCode
     * @return
     */
    List<IyResourceDTO> paramResourceList(List<Integer> roleIds, String pCode);
}
