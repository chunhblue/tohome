package cn.com.bbut.iy.itemmaster.serviceimpl.bmhis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dao.IyBmHisMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmItemHisMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ConditionDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.IyResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmViewDetailDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmViewMainDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisInitDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisJsonParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisResuleDataDTO;
import cn.com.bbut.iy.itemmaster.entity.IyBmCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmHis;
import cn.com.bbut.iy.itemmaster.entity.IyBmHisExample;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemHis;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemHisExample;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.StoreService;
import cn.com.bbut.iy.itemmaster.service.base.DptService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.com.bbut.iy.itemmaster.service.bm.BmService;
import cn.com.bbut.iy.itemmaster.service.bmhis.BmHisService;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.entity.ResourceGroup;

import com.google.gson.Gson;

/**
 * BM历史查询
 * 
 * @author songxz
 */
@Slf4j
@Service
public class BmHisServiceImpl implements BmHisService {
    /** 字符串：9 **/
    private final String STR_NINE = "9";
    /** 字符串：99 **/
    private final String STR_NINES = "99";
    /** 正则替换字符串 **/
    private final String REGULAR_TEXT = "\\d{1}";
    /** 字符串split分隔标记 */
    private final String SPLIT_COMMA = ",";
    private final BigDecimal HUNDRED = new BigDecimal(100);
    @Autowired
    private IyBmHisMapper bmHisMapper;
    @Autowired
    private IyBmItemHisMapper bmItemHisMapper;
    @Autowired
    private DptService dptService;
    @Autowired
    private BmService bmService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private IyRoleService iyRoleService;

    @Override
    public BmHisInitDTO getInitData(Collection<Integer> roleIds, User u) {
        BmHisInitDTO restDto = new BmHisInitDTO();
        // 得到事业部集合
        List<AutoCompleteDTO> divs = dptService
                .getDivisionByPrmission(PermissionCode.P_CODE_BM_HIS_VIEW);
        restDto.setDiv(divs);
        // 联动赋值，这里必须设置null
        restDto.setDpt(null);
        List<AutoCompleteDTO> stores = storeService.getStoreByRoleAndPcode(roleIds, u,
                PermissionCode.P_CODE_BM_HIS_VIEW);
        restDto.setStores(stores);
        return restDto;
    }

    @Override
    public List<AutoCompleteDTO> getdepartments(String pCode, String division) {
        return dptService.getDepartmentByPrmission(division, pCode);
    }

    /**
     * 根据权限得到处理好的资源数据
     * 
     * @param pCode
     * @return
     */
    private List<IyResourceDTO> getResourceList(Collection<Integer> roleIds, String pCode) {
        Collection<ResourceGroup> resourceGroup = iyRoleService.getResourcesByRoleAndPCode(
                (List<Integer>) roleIds, pCode);
        if (resourceGroup == null || resourceGroup.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        // 格式化 资源 翻译成dpt+store
        ConditionDTO dto = iyRoleService.createConditionsFromResourceGroup(resourceGroup);
        return dto.getResources();
    }

    @Override
    public GridDataDTO<BmHisListGridDataDTO> getData(BmHisParamDTO param) {

        Gson gson = new Gson();
        BmHisJsonParamDTO bmJsonParam = gson.fromJson(param.getSearchJson(),
                BmHisJsonParamDTO.class);
        bmJsonParam.setLimitStart(param.getLimitStart());
        bmJsonParam.setLimitEnd(param.getLimitStart() + param.getLimitEnd());
        bmJsonParam.setOrderByClause(param.getOrderByClause());
        String quote = Matcher.quoteReplacement(REGULAR_TEXT);
        if (StringUtils.isNotEmpty(bmJsonParam.getDiv())) {
            String temp = bmJsonParam.getDiv().substring(ConstantsDB.COMMON_TWO,
                    ConstantsDB.COMMON_THREE)
                    + STR_NINES;
            bmJsonParam.setDiv(temp.replaceAll(STR_NINE, quote));
        }
        if (StringUtils.isNotEmpty(bmJsonParam.getDpt())) {
            // 画面中dpt实际上是部，2位
            String temp = bmJsonParam.getDpt() + STR_NINE;
            bmJsonParam.setDpt(temp.replaceAll(STR_NINE, quote));
        }

        // dpt资源组合
        List<IyResourceDTO> resourceList = getResourceList(param.getRoleIds(), param.getPCode());// dto.getResources();
        if (resourceList == null || resourceList.size() == ConstantsDB.COMMON_ZERO) {
            // 当前人没有资源，不可以进行数据获取
            return new GridDataDTO<>(null, param.getPage(), ConstantsDB.COMMON_ZERO,
                    param.getRows());
        }
        List<IyResourceDTO> paramResourceList = new ArrayList<>();
        for (IyResourceDTO rs : resourceList) {
            IyResourceDTO changeDto = new IyResourceDTO();
            String tempDpt = rs.getDpt();
            if (tempDpt.equals(ConstantsDB.ALL_DPT)) {
                paramResourceList = null;
                break;
            }
            tempDpt = tempDpt.replaceAll(STR_NINE, quote);
            changeDto.setDpt(tempDpt);
            if (ConstantsDB.ALL_STORE.equals(rs.getStore())) {
                changeDto.setStore(null);
            } else {
                changeDto.setStore(rs.getStore());
            }
            paramResourceList.add(changeDto);
        }
        BmHisService thisService = Container.getBean(BmHisService.class);
        bmJsonParam.setResources(paramResourceList);
        BmHisResuleDataDTO selectResule = thisService.getBmHisList(bmJsonParam);

        return new GridDataDTO<>(selectResule.getList(), param.getPage(), selectResule.getCount(),
                param.getRows());
    }

    @Override
    public BmHisResuleDataDTO getBmHisList(BmHisJsonParamDTO bmJsonParam) {
        List<BmHisListGridDataDTO> list = bmHisMapper.getBmHisListByParam(bmJsonParam);
        if (list != null && list.size() > 0) {
            for (BmHisListGridDataDTO ls : list) {
                ls.setStatusText(getStatusFmt(ls.getStatus()));
                ls.setBmTypeText(bmTypeFmt(ls.getBmType()));
                ls.setNewFlgText(newFlgFmt(ls.getNewFlg()));
                ls.setOpFlgText(opFlgFmt(ls.getOpFlg()));
                ls.setBmPrice(division(ls.getBmPrice(), HUNDRED, ConstantsDB.COMMON_ONE));
                ls.setKey(ls.getBmCode() + "-" + ls.getBmType() + "-" + ls.getNewNo() + "-"
                        + ls.getNewNoSub());

                Integer bmCount = getBmCountBase(ls.getNewNo(), ls.getNewNoSub(), ls.getBmType(),
                        ls.getNumA(), ls.getNumB(), ls.getBmCount());
                ls.setBmCount(bmCount);
            }
        }
        long count = bmHisMapper.getBmHisCountByParam(bmJsonParam);
        return new BmHisResuleDataDTO(list, count);
    }

    private BigDecimal division(Integer val, BigDecimal divisor, int scale) {
        if (val == null) {
            return null;
        }
        return division(new BigDecimal(val), divisor, scale);
    }

    private BigDecimal division(BigDecimal val, BigDecimal divisor, int scale) {
        if (val == null) {
            return null;
        }
        if (divisor.doubleValue() == 0) {
            return BigDecimal.ZERO;
        }
        return val.divide(divisor, scale, RoundingMode.HALF_UP);
    }

    // 操作类型 字段格式化中文
    private String opFlgFmt(String value) {
        if (value == null) {
            return "";
        }
        switch (value) {
        case "01":
            return "采购员提交";
        case "07":
            return "采购员提交";
        case "08":
            return "采购员驳回";
        case "17":
            return "事业部长通过";
        case "18":
            return "事业部长驳回";
        case "27":
            return "系统部通过";
        case "28":
            return "系统部驳回";
        case "06":
            return "商品部删除";
        case "26":
            return "系统部删除";
        case "21":
            return "系统部提交";
        case "29":
            return "过期BM删除";
        default:
            return "";
        }
    }

    // 审核区分 字段格式化中文
    private String newFlgFmt(String value) {
        if (value == null) {
            return "";
        }
        switch (value) {
        case "0":
            return "Add";
        case "2":
            return "手工删除";
        case "1":
            return "修改";
        default:
            return "";
        }
    }

    // bmType 字段格式化中文
    private String bmTypeFmt(String value) {
        if (value == null) {
            return "";
        }
        switch (value) {
        case "01":
            return "01 捆绑";
        case "02":
            return "02 混合";
        case "03":
            return "03 固定组合";
        case "04":
            return "04 阶梯折扣";
        case "05":
            return "05 AB组";
        default:
            return "";
        }
    }

    private String getStatusFmt(String value) {
        if (value == null) {
            return "";
        }
        switch (value) {
        case "0":
            return "事业部长未审核";
        case "1":
            return "系统部未审核";
        case "2":
            return "已生效";
        case "3":
            return "已驳回";
        case "4":
            return "采购员待确认";
        default:
            return "";
        }
    }

    private Integer getBmCountBase(String newNo, String newNoSub, String bmType, Integer numA,
            Integer numB, Integer bmNumber) {
        Integer number = ConstantsDB.COMMON_ZERO;
        // bm数量获取，根据当前bm的不同类型 使用不同的值，01 02 03 根据bm类型和code取得对应的商品数量

        switch (bmType) {
        case ConstantsDB.BM_TYPE_01:
        case ConstantsDB.BM_TYPE_02:
        case ConstantsDB.BM_TYPE_03:
            number = bmNumber;
            break;
        case ConstantsDB.BM_TYPE_04:
            // 04 最大的一条数量
            number = bmHisMapper.getBmNumberTypeFour(newNo, newNoSub);
            break;
        case ConstantsDB.BM_TYPE_05:
            // 05 当前集合的ab组相加和
            numA = numA == null ? ConstantsDB.COMMON_ZERO : numA;
            numB = numB == null ? ConstantsDB.COMMON_ZERO : numB;
            number = numA + numB;
            break;
        }
        return number;
    }

    @Override
    public BmViewMainDTO getBmHisViewData(String bmType, String newNo, String newNoSub) {
        // 该方法与bm查询画面差异不大，所以这里使用bm管理中的对象进行处理，
        BmHisService thisService = Container.getBean(BmHisService.class);
        BmViewMainDTO dto = new BmViewMainDTO();
        int bmCount = ConstantsDB.COMMON_ZERO;
        IyBmCk bmMain = thisService.getBmCkBaseByKey(newNo, newNoSub);
        if (bmMain == null) {
            log.debug("根据 new_no=" + newNo + " new_no_sub=" + newNoSub + "未得到BM历史数据");
            return dto;
        }
        List<IyBmItemCk> bmDetail = thisService.getBmCkItemsByKey(newNo, newNoSub);
        dto.setBaseBm(bmMain);
        // 得到优惠店铺集合
        List<String> stroes = new ArrayList<>();
        String stroeStr = bmMain.getStore();
        String[] store = bmMain.getStore().split(SPLIT_COMMA);
        for (String st : store) {
            LabelDTO label = storeService.getStoreName(st);
            stroes.add(label.getCodeName());
        }

        dto.setStroes(stroes);
        dto.setStroeStr(stroeStr);
        dto.setBmCount(bmCount);
        dto.setBuyCount(bmCount);
        dto.setBmDiscount(bmMain.getBmDiscountRate());
        dto.setBmPrice(division(bmMain.getBmPrice(), HUNDRED, ConstantsDB.COMMON_ONE));
        dto.setNumA(bmMain.getNumA());
        dto.setNumB(bmMain.getNumB());

        // 明细
        dto.setBmItems(bmDetail);
        // 根据明细 创建隐藏域的input结构
        List<BmViewDetailDTO> bmItemInput = bmService.createInputHtml(bmType, bmMain, bmDetail);
        dto.setBmItemInput(bmItemInput);
        Set<String> itemCodeSet = new HashSet<>();
        if (bmItemInput != null && bmItemInput.size() > ConstantsDB.COMMON_ZERO) {
            for (BmViewDetailDTO ls : bmItemInput) {
                itemCodeSet.add(ls.getItemcode());
            }
        }
        dto.setItems(new ArrayList<>(itemCodeSet));
        dto.setItemCodeStr(StringUtils.join(itemCodeSet.toArray(), SPLIT_COMMA));
        // 不需要评审
        dto.setCanReview(String.valueOf(ConstantsDB.COMMON_ONE));
        return dto;

    }

    @Override
    public IyBmCk getBmCkBaseByKey(String newNo, String newNoSub) {
        IyBmHisExample example = new IyBmHisExample();
        example.or().andNewNoEqualTo(newNo).andNewNoSubEqualTo(newNoSub);
        example.setOrderByClause("store asc");
        List<IyBmHis> list = bmHisMapper.selectByExample(example);
        if (list == null || list.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        IyBmHis temp = list.get(ConstantsDB.COMMON_ZERO);
        List<String> store = new ArrayList<>();
        for (IyBmHis ck : list) {
            store.add(ck.getStore());
        }
        String str = StringUtils.join(store.toArray(), SPLIT_COMMA);
        temp.setStore(str);
        IyBmCk rest = new IyBmCk();
        BeanUtils.copyProperties(temp, rest);
        return rest;
    }

    @Override
    public List<IyBmItemCk> getBmCkItemsByKey(String newNo, String newNoSub) {
        IyBmItemHisExample example = new IyBmItemHisExample();
        example.or().andNewNoEqualTo(newNo).andNewNoSubEqualTo(newNoSub);
        example.setOrderByClause("store asc");
        List<IyBmItemHis> list = bmItemHisMapper.selectByExample(example);
        List<IyBmItemCk> rest = new ArrayList<>();
        if (list != null && list.size() > ConstantsDB.COMMON_ZERO) {
            for (IyBmItemHis ls : list) {
                IyBmItemCk ck = new IyBmItemCk();
                BeanUtils.copyProperties(ls, ck);
                // ck.setNewNo(null);
                // ck.setBmItemFlg();
                rest.add(ck);
            }
        }
        return rest;
    }
}
