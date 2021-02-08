package cn.com.bbut.iy.itemmaster.serviceimpl.bm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dao.IyBmCkMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmCodeMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmHisMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmItemCkMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmItemHisMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmItemMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmMapper;
import cn.com.bbut.iy.itemmaster.dao.IyDptMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.ConditionDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
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
import cn.com.bbut.iy.itemmaster.dto.bm.ItmeForStoreResultDto;
import cn.com.bbut.iy.itemmaster.entity.IyBm;
import cn.com.bbut.iy.itemmaster.entity.IyBmCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmCkExample;
import cn.com.bbut.iy.itemmaster.entity.IyBmCode;
import cn.com.bbut.iy.itemmaster.entity.IyBmCodeExample;
import cn.com.bbut.iy.itemmaster.entity.IyBmExample;
import cn.com.bbut.iy.itemmaster.entity.IyBmHis;
import cn.com.bbut.iy.itemmaster.entity.IyBmItem;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCkExample;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemExample;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemHis;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.IyDpt;
import cn.com.bbut.iy.itemmaster.entity.base.IyDptExample;
import cn.com.bbut.iy.itemmaster.entity.base.IyItemM;
import cn.com.bbut.iy.itemmaster.service.CommonService;
import cn.com.bbut.iy.itemmaster.service.StoreService;
import cn.com.bbut.iy.itemmaster.service.base.IyItemMService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.com.bbut.iy.itemmaster.service.bm.BmService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.entity.Resource;
import cn.shiy.common.pmgr.entity.ResourceGroup;

import com.google.gson.Gson;

/**
 * BM
 * 
 * @author songxz
 */
@Slf4j
@Service
public class BmServiceImpl implements BmService {

    @Autowired
    IyDptMapper iyDptMapper;
    @Autowired
    private IyItemMService itemService;
    @Autowired
    private IyRoleService iyRoleService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CommonService commonService;
    @Autowired
    IyBmCodeMapper bmCodeMapper;
    @Autowired
    private IyBmMapper bmMapper;
    @Autowired
    private IyBmItemMapper bmItemMapper;
    @Autowired
    private IyBmCkMapper bmCkMapper;
    @Autowired
    private IyBmItemCkMapper bmItemCkMapper;
    @Autowired
    private IyBmHisMapper bmHisMapper;
    @Autowired
    private IyBmItemHisMapper bmItemHisMapper;

    /** 字符串split分隔标记 */
    private final String SPLIT_COMMA = ",";

    /** 不够3位左补0 **/
    public final static DecimalFormat DF_Z_CODE = new DecimalFormat("000");

    /** 不够10位左补0 登录序号 **/
    public final static DecimalFormat BM_NEW_NO = new DecimalFormat("0000000000");

    /** bm类型最高循环次数 01、02 **/
    private final int BM_TYPE_COUNT_01 = 100;
    /** bm类型最高循环次数 03 **/
    private final int BM_TYPE_COUNT_03 = 300;
    /** bm类型最高循环次数 04 **/
    private final int BM_TYPE_COUNT_04 = 999;
    /** bm类型最高循环次数 05 **/
    private final int BM_TYPE_COUNT_05 = 999;

    private final BigDecimal HUNDRED = new BigDecimal(100);

    /** 05报表 ab组区分 a **/
    private final String AB_FLG_A = "a";

    /** 字符串：9 **/
    private final String STR_NINE = "9";
    /** 正则替换字符串 **/
    private final String REGULAR_TEXT = "\\d{1}";

    /** 字符串：99 **/
    private final String STR_NINES = "99";

    private static final Lock lock = new ReentrantLock();

    @Override
    public BmCodeDTO getBmCodeByType(String type) {
        lock.lock();
        BmCodeDTO baseCode = null;
        try {
            baseCode = getBaseCodeByType(type);
            updateBmCodeByTypeAndCode(type, baseCode.getCode());
            return baseCode;
        } catch (Exception e) {
            baseCode = new BmCodeDTO();
            baseCode.setMsg("获取BM编码失败...");
            baseCode.setUse(false);
            return baseCode;
        } finally {
            lock.unlock();
        }
    }

    private BmCodeDTO getBaseCodeByType(String type) {
        IyBmCodeExample example = new IyBmCodeExample();
        example.or().andBmTypeEqualTo(type);
        List<IyBmCode> selectList = bmCodeMapper.selectByExample(example);

        String tempCode = "";
        if (selectList != null && selectList.size() > ConstantsDB.COMMON_ZERO) {
            tempCode = selectList.get(ConstantsDB.COMMON_ZERO).getBmCode();
        }
        // 不同类型的可循环次数
        int whileTime = ConstantsDB.COMMON_ZERO;
        switch (type) {
        case ConstantsDB.BM_TYPE_01:
        case ConstantsDB.BM_TYPE_02:
            whileTime = BM_TYPE_COUNT_01;
            break;
        case ConstantsDB.BM_TYPE_03:
            whileTime = BM_TYPE_COUNT_03;
            break;
        case ConstantsDB.BM_TYPE_04:
            whileTime = BM_TYPE_COUNT_04;
            break;
        case ConstantsDB.BM_TYPE_05:
            whileTime = BM_TYPE_COUNT_05;
            break;
        default:
            whileTime = ConstantsDB.COMMON_ZERO;
            break;
        }
        // 循环次数
        int whileCount = ConstantsDB.COMMON_ZERO;
        boolean isSuccess = true;
        // 可执行变量
        boolean isGo = true;
        // 循环验证编号是否可用
        while (isGo) {
            // 取号
            tempCode = calculateBmCode(tempCode, type);
            // 验证号，系统未使用返回false
            isGo = isExistBmCode(tempCode, type);
            if (isGo && (whileCount >= whileTime)) {
                isGo = false;
                isSuccess = false;
            }
            // 循环次数
            whileCount++;
        }
        BmCodeDTO rest = new BmCodeDTO();
        if (isSuccess) {
            rest.setMsg("取号成功");
            rest.setCode(tempCode);
            rest.setType(type);
            rest.setUse(true);
        } else {
            rest.setMsg("BM编号已全部分配，取号失败");
            rest.setCode(null);
            rest.setType(type);
            rest.setUse(false);
        }
        return rest;
    }

    /**
     * 验证当前类型的bm编号是否可用，<br>
     * 在正式表和ck表中进行验证<br>
     * 
     * @param bmCode
     * @param type
     * @return 存在true，不存在false
     */
    private boolean isExistBmCode(String bmCode, String type) {
        IyBmExample bmExample = new IyBmExample();
        bmExample.or().andBmTypeEqualTo(type).andBmCodeEqualTo(bmCode);
        // 向BM正式表取得是否存在 bmCode
        Long count = bmMapper.countByExample(bmExample);
        if (count > ConstantsDB.COMMON_ZERO) {
            // 如果有则返回true
            return true;
        }
        IyBmCkExample ckExample = new IyBmCkExample();
        ckExample.or().andBmTypeEqualTo(type).andBmCodeEqualTo(bmCode);
        count = bmCkMapper.countByExample(ckExample);
        if (count > ConstantsDB.COMMON_ZERO) {
            // 如果有则返回true
            return true;
        }
        return false;
    }

    /**
     * 计算得到下一个bm code
     * 
     * @param code
     * @param type
     * @return
     */
    private static String calculateBmCode(String code, String type) {
        Integer tmp = null;
        // 转为int
        switch (type) {
        case ConstantsDB.BM_TYPE_01:
            tmp = codeScope(100, 199, code);
            break;
        case ConstantsDB.BM_TYPE_02:
            tmp = codeScope(200, 299, code);
            break;
        case ConstantsDB.BM_TYPE_03:
            tmp = codeScope(300, 599, code);
            break;
        case ConstantsDB.BM_TYPE_04:
            tmp = codeScope(1, 999, code);
            break;
        case ConstantsDB.BM_TYPE_05:
            tmp = codeScope(1, 999, code);
            break;
        default:
            tmp = ConstantsDB.COMMON_ZERO;
            break;
        }
        return DF_Z_CODE.format(tmp);
    }

    /**
     * 数值范围 是否合法
     * 
     * @param left
     *            左侧限定（包含）
     * @param right
     *            右侧限定（包含）
     * @param v
     *            当前值
     * @return
     */
    private static Integer codeScope(int left, int right, String v) {
        if (v == null || "".equals(v)) {
            return left;
        }
        Integer intV = Integer.parseInt(v);
        intV = intV + ConstantsDB.COMMON_ONE;
        if (left >= intV || intV <= right) {
            return intV;
        } else {
            return left;
        }
    }

    @Override
    public int updateBmCodeByTypeAndCode(String type, String code) {
        IyBmCodeExample example = new IyBmCodeExample();
        example.or().andCompanyEqualTo(ConstantsDB.COMPANY_CODE).andBmTypeEqualTo(type);
        List<IyBmCode> selectList = bmCodeMapper.selectByExample(example);
        IyBmCode record = new IyBmCode();
        record.setBmCode(code);
        record.setUpdateDate(TimeUtil.getDate());
        int upCount = ConstantsDB.COMMON_ZERO;
        // 插入DB，返回结果完成
        log.debug("更新bm_code表，code={},type={}", code, type);
        if (selectList != null && selectList.size() > ConstantsDB.COMMON_ZERO) {
            upCount = bmCodeMapper.updateByExampleSelective(record, example);
        } else {
            record.setCompany(ConstantsDB.COMPANY_CODE);
            record.setBmType(type);
            upCount = bmCodeMapper.insert(record);
        }
        return upCount;
    }

    @Override
    public BmItemResultDto getItemInfoByItem1(String item1) {
        BmItemResultDto res = new BmItemResultDto();
        IyItemM item = bmMapper.getItemByCode(item1);
        // 验证Item Barcode是否存在
        // item = itemService.getItemByCode(item1);
        if (item != null) {
            res.setSuccess(true);
            res.setItem(item.getItem1());
            res.setName(item.getItemName().trim());
            res.setItemSystem(item.getItemSystem());
        } else {
            res.setSuccess(false);
            res.setMessage("该商品编码不存在");
        }
        return res;
    }

    @Override
    public AjaxResultDto isItemBmExist(String item1, String stores) {
        AjaxResultDto res = new AjaxResultDto();
        String[] store = stores.split(SPLIT_COMMA);
        Map<String, String> map = new HashMap<>();
        map.put("item1", item1);
        for (String st : store) {
            map.put("store", st);
            long count = bmMapper.isItemBmExistByItemAndStore(map);
            count += bmMapper.isItemBmCkExistByItemAndStore(map);
            if (count > ConstantsDB.COMMON_ZERO) {
                res.setMessage("不允许一个单品(" + item1 + ")在同一个店铺(" + st + ")中参加多项捆绑");
                res.setSuccess(true);// 存在返回true
                return res;
            }
        }
        res.setMessage("该商品与店铺的组合不存在与任何一个BM中，可以使用");
        res.setSuccess(false);// 不存在返回false
        return res;
    }

    @Override
    public AjaxResultDto verdictItemIndate(String itemSystem, String stores, String startDate,
            String endDate) {
        AjaxResultDto res = new AjaxResultDto();
        String[] store = stores.split(SPLIT_COMMA);
        Map<String, String> selectMap = new HashMap<>();
        selectMap.put("itemSystem", itemSystem);
        selectMap.put("startDate", startDate);
        selectMap.put("endDate", endDate);
        for (String st : store) {
            selectMap.put("store", st);
            long count = bmMapper.verdictItemIndateByMap(selectMap);
            if (count <= ConstantsDB.COMMON_ZERO) {
                res.setMessage("该单品在" + st + "店没有对应期间的控制记录");
                res.setSuccess(false);
                return res;
            }
        }
        res.setMessage("对应期间的控制记录全部符合");
        res.setSuccess(true);
        return res;
    }

    @Override
    public AjaxResultBmDto getItemStoreInfo(String itemSystem, String stores, String startDate,
            String endDate) {
        AjaxResultBmDto res = new AjaxResultBmDto();
        // 使用系统码取得单品信息M表
        IyItemM itemObj = itemService.getItemInfoBySystem(itemSystem);
        if (itemObj == null) {
            res.setMessage("商品基本信息获取失败");
            res.setSuccess(false);
            return res;
        }
        // 单品基本信息
        String itemCode = itemObj.getItem1();
        String itemName = itemObj.getItemName().trim();
        String dpt = itemObj.getDpt();

        Collection<Integer> roleIds = commonService.getSessionUserRoleIds();

        Integer status = getItemAffirmStatus(dpt, PermissionCode.P_CODE_BM_PRO_EDIT, roleIds);

        // 拆分店铺得到该该店铺所属商品的控制记录
        String[] store = stores.split(SPLIT_COMMA);
        Map<String, String> selectMap = new HashMap<>();
        selectMap.put("itemSystem", itemSystem);
        selectMap.put("startDate", startDate);
        selectMap.put("endDate", endDate);
        List<ItmeForStoreResultDto> list = new ArrayList<>();
        for (String st : store) {
            selectMap.put("store", st);
            ItmeForStoreResultDto dto = bmMapper.getItemStoreInfoByDate(selectMap);
            if (dto != null) {
                dto.setDpt(dpt);
                dto.setItemName(itemName);
                dto.setItem(itemCode);
                dto.setItemSystem(itemSystem);
                dto.setStatus(status);
                list.add(dto);
            } else {
                res.setMessage("数据获取失败");
                res.setSuccess(false);
                return res;
            }
        }
        res.setData(list);
        res.setSuccess(true);
        return res;
    }

    /**
     * 判断当前商品所属的dpt是否为当前操作人的资源，如果是 返回确认状态 <br>
     * 不是0：未确认 <br>
     * 是 1：已确认
     * 
     * @param dpt
     * @param pcode
     *            权限（可以确定画面）
     * @param roleIds
     *            角色（当前操作人已分配的角色集合，session取得）
     * @return
     */
    private Integer getItemAffirmStatus(String dpt, String pcode, Collection<Integer> roleIds) {
        Collection<ResourceGroup> groups = new ArrayList<>();
        // 3 代表dpt
        ResourceGroup rp = new ResourceGroup(new Resource(dpt, ConstantsDB.COMMON_THREE));
        groups.add(rp);
        Collection<Integer> getRoleIds = iyRoleService.getRoleIdsByResource(groups, pcode);
        if (getRoleIds == null) {
            return ConstantsDB.COMMON_ZERO;
        }
        roleIds.retainAll(getRoleIds);
        if (roleIds != null && roleIds.size() > ConstantsDB.COMMON_ZERO) {
            return ConstantsDB.COMMON_ONE;
        } else {
            return ConstantsDB.COMMON_ZERO;
        }
    }

    @Override
    public String getBmNewNo() {
        String nowNo = commonService.getSequenceNext(ConstantsDB.IY_ITEM_M_CK_SEQ,
                ConstantsDB.NEW_NO_LENGTH);
        return nowNo;
    }

    @Override
    public String getBmNewNoSub(String newNo) {
        // 向his表查询当前newNo表的，初始化000，
        String subNo = bmMapper.getNewNoSubByNo(newNo);
        Integer intSubNo = (subNo == null) || (subNo.equals("")) ? 0 : Integer.parseInt(subNo) + 1;
        return DF_Z_CODE.format(intSubNo);
    }

    @Override
    public AjaxResultDto insertBmData(Integer identity, String paramJson, User u) {
        AjaxResultDto res = new AjaxResultDto();
        Gson gson = new Gson();
        BmService thisService = Container.getBean(BmService.class);
        if (identity.equals(ConstantsDB.COMMON_ONE)) {
            // 采购
            BmSubmitDataDTO bmDataCk = gson.fromJson(paramJson, BmSubmitDataDTO.class);
            res = thisService.insertBmCkAndHis(bmDataCk, u);
        } else if (identity.equals(ConstantsDB.COMMON_THREE)) {
            // 系统
            BmSysSubmitDataDTO bmData = gson.fromJson(paramJson, BmSysSubmitDataDTO.class);
            res = thisService.insertBmAndHis(bmData, u);
        }

        return res;
    }

    @Override
    public BmUserInfoDTO getStaffDpt(String pCode) {
        BmUserInfoDTO infoDto = new BmUserInfoDTO();
        List<IyResourceDTO> resourceList = getResourceList(pCode);// dto.getResources();
        if (resourceList == null || resourceList.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        Set<String> divSet = new HashSet<>();
        Set<String> depSet = new HashSet<>();
        Set<String> dptSet = new HashSet<>();
        for (IyResourceDTO d : resourceList) {
            String dpt = d.getDpt();
            dptSet.add(dpt);
            depSet.add(dpt.substring(ConstantsDB.COMMON_ZERO, ConstantsDB.COMMON_TWO));
            divSet.add(dpt.substring(ConstantsDB.COMMON_ZERO, ConstantsDB.COMMON_ONE));
        }
        // 事业部判断，是否为1个事业部
        if (divSet != null && divSet.size() == ConstantsDB.COMMON_ONE) {
            String dpt_left_1 = resourceList.get(ConstantsDB.COMMON_ZERO).getDpt()
                    .substring(ConstantsDB.COMMON_ZERO, ConstantsDB.COMMON_ONE);
            if (dpt_left_1.equals(STR_NINE)) {
                return null;
            }
            String dpt = dpt_left_1 + STR_NINES;
            IyDptExample dptExample = new IyDptExample();
            dptExample.or().andDptEqualTo(dpt);
            List<IyDpt> dptObj = iyDptMapper.selectByExample(dptExample);
            if (dptObj != null && dptObj.size() > ConstantsDB.COMMON_ZERO) {
                IyDpt d = dptObj.get(ConstantsDB.COMMON_ZERO);
                infoDto.setDiv(d.getGrandDiv());
                infoDto.setDivDpt(d.getDpt());
                infoDto.setDivName(d.getDptName());
            } else {
                return null;
            }

        } else {
            return null;
        }
        // 部门验证，是否为1个部门
        if (depSet != null && depSet.size() == ConstantsDB.COMMON_ONE) {
            String dpt = resourceList.get(ConstantsDB.COMMON_ZERO).getDpt()
                    .substring(ConstantsDB.COMMON_ZERO, ConstantsDB.COMMON_TWO)
                    + STR_NINE;
            IyDptExample dptExample = new IyDptExample();
            dptExample.or().andDptEqualTo(dpt);
            List<IyDpt> dptObj = iyDptMapper.selectByExample(dptExample);
            if (dptObj != null && dptObj.size() > ConstantsDB.COMMON_ZERO) {
                IyDpt d = dptObj.get(ConstantsDB.COMMON_ZERO);
                infoDto.setDep(d.getDepartment());
                infoDto.setDepDpt(d.getDpt());
                infoDto.setDepName(d.getDptName());
            } else {
                return null;
            }
        } else {
            return null;
        }
        if (dptSet != null && dptSet.size() == ConstantsDB.COMMON_ONE) {
            String dpt = resourceList.get(ConstantsDB.COMMON_ZERO).getDpt();
            IyDptExample dptExample = new IyDptExample();
            dptExample.or().andDptEqualTo(dpt);
            List<IyDpt> dptObj = iyDptMapper.selectByExample(dptExample);
            if (dptObj != null && dptObj.size() > ConstantsDB.COMMON_ZERO) {
                IyDpt d = dptObj.get(ConstantsDB.COMMON_ZERO);
                infoDto.setDpt(d.getDpt());
                infoDto.setDptDpt(d.getDpt());
                infoDto.setDptName(d.getDptName());
            } else {
                return null;
            }
        } else {
            infoDto.setDpt(infoDto.getDep());
            infoDto.setDptDpt(infoDto.getDepDpt());
            infoDto.setDptName(infoDto.getDepName());
        }

        return infoDto;
    }

    @Override
    public AjaxResultDto insertBmCkAndHis(BmSubmitDataDTO bmDataCk, User u) {
        AjaxResultDto res = new AjaxResultDto();

        if (bmDataCk.getBaseBmList() == null
                || bmDataCk.getBaseBmList().size() == ConstantsDB.COMMON_ZERO) {
            res.setMessage("创建bm失败，未得到bm主档信息");
            res.setSuccess(false);
            return res;
        }

        BmService thisService = Container.getBean(BmService.class);
        String newNo = bmDataCk.getNewNo();
        if (newNo == null || "".equals(newNo)) {
            newNo = thisService.getBmNewNo();
        }
        String newNoSub = thisService.getBmNewNoSub(newNo);
        String userid = u.getUserId();// 操作人id
        Date update = TimeUtil.getDate();

        /*
         * 计算得到标砖的 查看权限标志位 RIGHT_FLG, 验证当前主档表第1条数据的 DPT_ALL字段，分析得到所有需要被审核的事业部长，
         * 其中默认5位 1 1 1 1 1，分别代表5个事业部长，根据分析后根据dpt所需的事业部长替换对应位置变更为0，
         */
        String[] dtpAllObj = bmDataCk.getBaseBmList().get(0).getDptAll().split(SPLIT_COMMA);
        Set<String> setDptLeft1 = new HashSet<>();
        for (String dpt : dtpAllObj) {
            setDptLeft1.add(dpt.substring(ConstantsDB.COMMON_ZERO, ConstantsDB.COMMON_ONE));
        }
        String[] rightFlg = { "1", "1", "1", "1", "1" };
        for (String s : setDptLeft1) {
            switch (s) {
            case "1":
                rightFlg[0] = "0";
                break;
            case "2":
                rightFlg[1] = "0";
                break;
            case "3":
                rightFlg[2] = "0";
                break;
            case "4":
                rightFlg[3] = "0";
                break;
            case "5":
                rightFlg[4] = "0";
                break;
            }
        }
        String rightFlgStr = StringUtils.join(rightFlg);

        for (IyBmCk bm : bmDataCk.getBaseBmList()) {
            bm.setUserid(userid);
            bm.setUpdateDate(update);
            bm.setNewNo(newNo);
            bm.setRightFlg(rightFlgStr);
            // ck
            bmCkMapper.insert(bm);

            // his主档
            IyBmHis his = new IyBmHis();
            BeanUtils.copyProperties(bm, his);
            his.setNewNoSub(newNoSub);
            his.setUsername(u.getUserName());
            bmHisMapper.insert(his);
        }
        for (IyBmItemCk item : bmDataCk.getBmItems()) {
            item.setNewNo(newNo);
            // ck 明细
            bmItemCkMapper.insert(item);

            // his明细
            IyBmItemHis his = new IyBmItemHis();
            BeanUtils.copyProperties(item, his);
            his.setNewNoSub(newNoSub);
            bmItemHisMapper.insert(his);
        }
        res.setMessage("Operation Succeeded!");
        res.setSuccess(true);

        return res;
    }

    @Override
    public AjaxResultDto insertBmAndHis(BmSysSubmitDataDTO bmData, User u) {
        AjaxResultDto res = new AjaxResultDto();
        // 插入主档数据
        if (bmData.getBaseBmList() == null
                || bmData.getBaseBmList().size() == ConstantsDB.COMMON_ZERO) {
            res.setMessage("创建bm失败，未得到bm主档信息");
            res.setSuccess(false);
            return res;
        }
        BmService thisService = Container.getBean(BmService.class);
        String newNo = thisService.getBmNewNo();
        String newNoSub = thisService.getBmNewNoSub(newNo);
        Date insertDate = TimeUtil.getDate();
        String update = TimeUtil.formatShortDate3(insertDate);

        // 当系统部提交时，需要替换正是表的数据，所以需要在提交之前删除正是表的主档和明细档
        String bmType = bmData.getBaseBmList().get(0).getBmType();
        String bmCode = bmData.getBaseBmList().get(0).getBmCode();

        IyBmExample bmExample = new IyBmExample();
        bmExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
        bmMapper.deleteByExample(bmExample);

        IyBmItemExample itemExample = new IyBmItemExample();
        itemExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
        bmItemMapper.deleteByExample(itemExample);

        for (IyBm bm : bmData.getBaseBmList()) {
            bm.setOnlineDate(Integer.parseInt(update));
            // 正是表
            bmMapper.insert(bm);
            // his主档
            IyBmHis his = new IyBmHis();
            BeanUtils.copyProperties(bm, his);
            his.setOpFlg("21");
            his.setNewNo(newNo);
            his.setNewNoSub(newNoSub);
            his.setUpdateDate(insertDate);
            his.setUsername(u.getUserName());
            bmHisMapper.insert(his);
        }
        for (IyBmItem item : bmData.getBmItems()) {
            // 明细
            bmItemMapper.insert(item);
            // his 明细
            IyBmItemHis his = new IyBmItemHis();
            BeanUtils.copyProperties(item, his);
            his.setNewNo(newNo);
            his.setNewNoSub(newNoSub);
            bmItemHisMapper.insert(his);
        }
        res.setMessage("Operation Succeeded!");
        res.setSuccess(true);
        return res;
    }

    @Override
    public BmUserInfoDTO getStaffStore(String pCode) {
        List<IyResourceDTO> resourceList = getResourceList(pCode);// dto.getResources();
        if (resourceList == null || resourceList.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        Set<String> storeSet = new HashSet<>();
        for (IyResourceDTO d : resourceList) {
            String s = d.getStore();
            storeSet.add(s);
        }
        BmUserInfoDTO infoDto = new BmUserInfoDTO();
        // 事业部判断，是否为1个事业部
        if (storeSet != null && storeSet.size() == ConstantsDB.COMMON_ONE) {
            String stCode = resourceList.get(ConstantsDB.COMMON_ZERO).getStore();
            if (ConstantsDB.ALL_STORE.equals(stCode)) {
                return null;
            }
            LabelDTO st = storeService.getStoreName(stCode);
            if (st != null) {
                infoDto.setStore(st.getCode());
                infoDto.setStoreName(st.getName());
            } else {
                return null;
            }

        } else {
            return null;
        }

        return infoDto;

    }

    @Override
    public GridDataDTO<BmListGridDataDTO> getData(BmParamDTO param) {
        Gson gson = new Gson();
        BmJsonParamDTO bmJsonParam = gson.fromJson(param.getSearchJson(), BmJsonParamDTO.class);
        bmJsonParam.setLimitStart(param.getLimitStart());
        bmJsonParam.setLimitEnd(param.getLimitStart() + param.getLimitEnd());
        bmJsonParam.setOrderByClause(param.getOrderByClause());

        BmResuleDataDTO selectResule = null;
        BmService thisService = Container.getBean(BmService.class);
        String quote = Matcher.quoteReplacement(REGULAR_TEXT);
        String pCode = "";
        switch (bmJsonParam.getIdentity()) {
        case ConstantsDB.COMMON_ONE:
            pCode = PermissionCode.P_CODE_BM_PRO_VIEW;
            break;
        case ConstantsDB.COMMON_TWO:
            pCode = PermissionCode.P_CODE_BM_DIV_LEADER_VIEW;
            break;
        case ConstantsDB.COMMON_THREE:
            pCode = PermissionCode.P_CODE_BM_SYS_VIEW;
            break;
        case ConstantsDB.COMMON_FOUR:
            pCode = PermissionCode.P_CODE_BM_STORE_VIEW;
            break;
        default:
            break;
        }
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

        List<IyResourceDTO> paramResourceList = paramResourceList(param.getRoleIds(), pCode);
        bmJsonParam.setResources(paramResourceList);

        if (bmJsonParam.getTableType().equals(ConstantsDB.COMMON_ONE)) {
            // 判断数据源表为1=CK表
            selectResule = thisService.getBmCkList(bmJsonParam);
        } else if (bmJsonParam.getTableType().equals(ConstantsDB.COMMON_ZERO)) {
            // 判断数据源表为0=正式表
            selectResule = thisService.getBmList(bmJsonParam);
        }

        // 如果是事业部长（商品部长），设定每条数据的审核状态
        if (bmJsonParam.getIdentity().equals(ConstantsDB.COMMON_TWO)
                && selectResule.getList() != null && selectResule.getList().size() > 0) {
            for (BmListGridDataDTO grid : selectResule.getList()) {
                if (grid.getCheckFlg().equals(String.valueOf(ConstantsDB.COMMON_ZERO))) {
                    String checkResources = getCheckResourcesByRightFlg(grid.getRightFlg());
                    if (checkResources != null && !checkResources.equals("")) {
                        grid.setCanReview(ConstantsDB.COMMON_ZERO + "");
                        grid.setCheckResources(checkResources);
                    } else {
                        grid.setCanReview(ConstantsDB.COMMON_ONE + "");
                    }
                } else {
                    grid.setCanReview(ConstantsDB.COMMON_ONE + "");
                }
            }
        }

        return new GridDataDTO<>(selectResule.getList(), param.getPage(), selectResule.getCount(),
                param.getRows());
    }

    private String getCheckResourcesByRightFlg(String rightFlg) {
        String[] rightFlgObj = rightFlg.split("");
        List<String> checkResources = new ArrayList<>();
        int index = 0;
        boolean is = rightFlgObj[index++].equals("1") ? false : checkResources.add("199");
        is = rightFlgObj[index++].equals("1") ? false : checkResources.add("299");
        is = rightFlgObj[index++].equals("1") ? false : checkResources.add("399");
        is = rightFlgObj[index++].equals("1") ? false : checkResources.add("499");
        is = rightFlgObj[index++].equals("1") ? false : checkResources.add("599");
        return StringUtils.join(checkResources.toArray(), SPLIT_COMMA);
    }

    @Override
    public BmResuleDataDTO getBmCkList(BmJsonParamDTO param) {
        List<BmListGridDataDTO> list = bmCkMapper.getBmCkDataByJsonParam(param);
        if (list != null && list.size() > ConstantsDB.COMMON_ZERO) {
            String store = param.getStore();
            String itemCode = param.getItemCode();
            for (BmListGridDataDTO ls : list) {
                ls.setStroe(store);
                ls.setItemCode(itemCode);
                Integer number = getBmCkCount(ls.getBmType(), ls.getBmCode(), ls.getNumA(),
                        ls.getNumB(), ls.getBmNumber());
                ls.setBmCount(number);
            }
        }

        Long count = bmCkMapper.getBmCkCountByJsonParam(param);
        return new BmResuleDataDTO(list, count);
    }

    @Override
    public BmResuleDataDTO getBmList(BmJsonParamDTO param) {
        List<BmListGridDataDTO> list = bmMapper.getBmDataByJsonParam(param);
        if (list != null && list.size() > ConstantsDB.COMMON_ZERO) {
            String store = param.getStore();
            String itemCode = param.getItemCode();
            for (BmListGridDataDTO ls : list) {
                ls.setStroe(store);
                ls.setItemCode(itemCode);
                Integer number = getBmCount(ls.getBmType(), ls.getBmCode(), ls.getNumA(),
                        ls.getNumB(), ls.getBmNumber());
                ls.setBmCount(number);
            }
        }
        Long count = bmMapper.getBmCountByJsonParam(param);
        return new BmResuleDataDTO(list, count);
    }

    private Integer getBmCountBase(Integer tableType, String bmType, String bmCode, Integer numA,
            Integer numB, Integer bmNumber) {
        Integer number = ConstantsDB.COMMON_ZERO;
        // bm数量获取，根据当前bm的不同类型 使用不同的值，01 02 03 根据bm类型和code取得对应的商品数量
        // 04 最大的一条数量
        // 05 当前集合的ab组相加和
        switch (bmType) {
        case ConstantsDB.BM_TYPE_01:
        case ConstantsDB.BM_TYPE_02:
        case ConstantsDB.BM_TYPE_03:
            // number = bmCkMapper.getBmCkNumber(bmType, bmCode);
            number = bmNumber;
            break;
        case ConstantsDB.BM_TYPE_04:
            if (tableType.equals(ConstantsDB.COMMON_ONE)) {
                number = bmCkMapper.getBmCkNumberTypeFour(bmType, bmCode);
            } else {
                number = bmMapper.getBmNumberTypeFour(bmType, bmCode);
            }
            break;
        case ConstantsDB.BM_TYPE_05:
            numA = numA == null ? ConstantsDB.COMMON_ZERO : numA;
            numB = numB == null ? ConstantsDB.COMMON_ZERO : numB;
            number = numA + numB;
            break;
        }
        return number;
    }

    /**
     * 得到ck表bm数量
     * 
     * @return
     */
    private Integer getBmCkCount(String bmType, String bmCode, Integer numA, Integer numB,
            Integer bmNumber) {
        return getBmCountBase(ConstantsDB.COMMON_ONE, bmType, bmCode, numA, numB, bmNumber);
    }

    /**
     * 得到正式表bm数量
     * 
     * @return
     */
    private Integer getBmCount(String bmType, String bmCode, Integer numA, Integer numB,
            Integer bmNumber) {
        return getBmCountBase(ConstantsDB.COMMON_ZERO, bmType, bmCode, numA, numB, bmNumber);
    }

    @Override
    public BmViewMainDTO getBmViewData(String bmCode, String bmType, Integer tabletype) {
        BmService thisService = Container.getBean(BmService.class);
        BmViewMainDTO dto = new BmViewMainDTO();
        IyBmCk bmMain = null;
        List<IyBmItemCk> bmDetail = null;
        int bmCount = ConstantsDB.COMMON_ZERO;
        // 拿到bm基础信息
        if (tabletype.equals(ConstantsDB.COMMON_ONE)) {
            // CK表
            bmMain = thisService.getBmCkBaseByCodeAndType(bmCode, bmType);
            if (bmMain == null) {
                return null;
            }
            bmDetail = thisService.getBmCkItemsByCoundAndType(bmCode, bmType);
            bmCount = getBmCkCount(bmType, bmCode, bmMain.getNumA(), bmMain.getNumB(),
                    bmMain.getBmNumber());
        } else {
            // 正是表
            IyBm tempBm = thisService.getBmBaseByCodeAndType(bmCode, bmType);
            if (tempBm == null) {
                return null;
            }
            bmMain = convertBmToCk(tempBm);
            bmDetail = thisService.getBmItemsByCoundAndType(bmCode, bmType);
            bmCount = getBmCount(bmType, bmCode, bmMain.getNumA(), bmMain.getNumB(),
                    bmMain.getBmNumber());
        }

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
        List<BmViewDetailDTO> bmItemInput = createInputHtml(bmType, bmMain, bmDetail);
        dto.setBmItemInput(bmItemInput);
        Set<String> itemCodeSet = new HashSet<>();
        if (bmItemInput != null && bmItemInput.size() > ConstantsDB.COMMON_ZERO) {
            for (BmViewDetailDTO ls : bmItemInput) {
                itemCodeSet.add(ls.getItemcode());
            }
        }
        dto.setItems(new ArrayList<>(itemCodeSet));
        dto.setItemCodeStr(StringUtils.join(itemCodeSet.toArray(), SPLIT_COMMA));
        /*
         * 分析当前数据，是否需要审核，并根据当前数据的：审核状态 CHECK_FLG来判断审核类型; =4时拿到所有明细中未确认数据的dpt
         * =0时，根据查看权限标志位 RIGHT_FLG，计算得出应该有评审的事业部长（商品部长）资源
         */
        if (bmMain.getCheckFlg().equals(String.valueOf(ConstantsDB.COMMON_ZERO))) {
            // 0-所有采购员均已确认，商品部长未审核
            String checkResources = getCheckResourcesByRightFlg(bmMain.getRightFlg());
            if (checkResources != null && !checkResources.equals("")) {
                dto.setCanReview(String.valueOf(ConstantsDB.COMMON_ZERO));
                dto.setCheckResources(checkResources);
            } else {
                dto.setCanReview(String.valueOf(ConstantsDB.COMMON_ONE));
            }
        } else if (bmMain.getCheckFlg().equals(String.valueOf(ConstantsDB.COMMON_FOUR))) {
            // 4-采购员确认中
            if (bmDetail != null && bmDetail.size() > 0) {
                Set<String> setDpt = new HashSet<>();
                Set<String> setItemAndDpt = new HashSet<>();
                for (BmViewDetailDTO ds : bmItemInput) {
                    // 0：未确认 1：已确认 2：驳回
                    if (ds.getStatus().equals(String.valueOf(ConstantsDB.COMMON_ZERO))) {
                        setDpt.add(ds.getDpt());
                        // 将所有未确认的（单品+dpt）赋值于特定字段中
                        setItemAndDpt.add(ds.getItemcode() + "-" + ds.getDpt());
                    }
                }
                dto.setCanReview(String.valueOf(ConstantsDB.COMMON_ZERO));
                dto.setCheckResources(StringUtils.join(new ArrayList<>(setDpt).toArray(),
                        SPLIT_COMMA));
                dto.setAllItemAndDpt(StringUtils.join(new ArrayList<>(setItemAndDpt).toArray(),
                        SPLIT_COMMA));
            } else {
                dto.setCanReview(String.valueOf(ConstantsDB.COMMON_ONE));
            }
        } else if (bmMain.getCheckFlg().equals(String.valueOf(ConstantsDB.COMMON_ONE))) {
            // 1-商品部长已审核（通过），系统部未审核
            dto.setCanReview(String.valueOf(ConstantsDB.COMMON_ZERO));
        } else {
            // 不需要评审
            dto.setCanReview(String.valueOf(ConstantsDB.COMMON_ONE));
        }
        return dto;
    }

    private Map<String, String> getSelectMap(String itemSystem, String startDate, String endDate,
            String store) {
        Map<String, String> selectMap = new HashMap<>();
        selectMap.put("itemSystem", itemSystem);
        selectMap.put("startDate", startDate);
        selectMap.put("endDate", endDate);
        selectMap.put("store", store);
        return selectMap;
    }

    /**
     * 创建bm明细数据中html input数据
     * 
     * @param bmType
     * @param bmMain
     * @param bmDetail
     * @return
     */
    @Override
    public List<BmViewDetailDTO> createInputHtml(String bmType, IyBmCk bmMain,
            List<IyBmItemCk> bmDetail) {
        List<BmViewDetailDTO> rest = new ArrayList<>();

        if (bmDetail != null && bmDetail.size() > ConstantsDB.COMMON_ZERO) {
            for (IyBmItemCk ck : bmDetail) {
                String id = ck.getStore() + "_" + ck.getItem();
                String store = ck.getStore();
                String bmcode = ck.getBmCode();
                IyItemM itemM = itemService.getItemByCode(ck.getItem());
                // 拿到商品进售价信息
                ItmeForStoreResultDto itemInfo = bmMapper.getItemStoreInfoByDate(getSelectMap(
                        itemM.getItemSystem(), String.valueOf(bmMain.getBmEffFrom()),
                        String.valueOf(bmMain.getBmEffTo()), ck.getStore()));
                String itemsystem = itemInfo.getItemSystem();
                String itemcode = itemInfo.getItem();
                String itemname = (itemInfo.getItemName() == null ? "" : itemInfo.getItemName())
                        .trim();
                String dpt = itemInfo.getDpt();
                // 进货单价
                BigDecimal costtax = itemInfo.getCostTax();
                // 销售单价
                BigDecimal pricetax = itemInfo.getPriceTax();

                String status = ck.getBmItemFlg();
                // bm销售价格
                BigDecimal bmprice = division(bmMain.getBmPrice(), HUNDRED, ConstantsDB.COMMON_ONE);
                // 折扣销售单价
                BigDecimal disprice = division(ck.getPriceDisc(), HUNDRED, ConstantsDB.COMMON_ONE);

                // 毛利率=（折扣销售单价-进货单价）/折扣销售单价（保留二位小数）如毛利率 ≤ 0，显示为红色
                BigDecimal tempBmprice = disprice;
                if (tempBmprice == null) {
                    tempBmprice = BigDecimal.ZERO;
                }
                BigDecimal profitrate = division(tempBmprice.subtract(costtax), tempBmprice, 10);
                profitrate = profitrate.multiply(HUNDRED).setScale(BigDecimal.ROUND_HALF_UP, 2);
                BigDecimal buycount = division(bmMain.getBmNumber(), new BigDecimal(
                        ConstantsDB.COMMON_ONE), ConstantsDB.COMMON_ZERO);
                BigDecimal discount = division(bmMain.getBmDiscountRate(), new BigDecimal(
                        ConstantsDB.COMMON_ONE), ConstantsDB.COMMON_ZERO);

                if (bmType.equals(ConstantsDB.BM_TYPE_04)) {

                    disprice = division(ck.getPriceDisc(), HUNDRED, ConstantsDB.COMMON_ONE);
                    discount = division(ck.getDiscRate(), new BigDecimal(ConstantsDB.COMMON_ONE),
                            ConstantsDB.COMMON_ONE);
                    buycount = division(ck.getNumA(), new BigDecimal(ConstantsDB.COMMON_ONE),
                            ConstantsDB.COMMON_ZERO);

                    // 04 时 购买数量*折扣销售单价 = BM销售价格 上校确认
                    bmprice = disprice.multiply(buycount).setScale(BigDecimal.ROUND_HALF_UP, 2);
                }
                String ab = ck.getAbFlg();
                if (bmType.equals(ConstantsDB.BM_TYPE_05)) {
                    if (ab.equals(AB_FLG_A)) {
                        buycount = division(ck.getNumA(), new BigDecimal(ConstantsDB.COMMON_ONE),
                                ConstantsDB.COMMON_ZERO);
                    } else {
                        buycount = division(ck.getNumB(), new BigDecimal(ConstantsDB.COMMON_ONE),
                                ConstantsDB.COMMON_ZERO);
                    }
                }
                String value = ck.getItem();
                rest.add(new BmViewDetailDTO(id, store, bmcode, itemsystem, itemcode, itemname,
                        dpt, costtax, pricetax, status, bmprice, disprice, profitrate, buycount,
                        discount, ab, value));
            }
        }

        return rest;
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

    @Override
    public List<IyBmItemCk> getBmCkItemsByCoundAndType(String bmCode, String bmType) {
        IyBmItemCkExample example = new IyBmItemCkExample();
        example.or().andBmTypeEqualTo(bmType).andBmCodeEqualTo(bmCode);
        example.setOrderByClause("store asc");
        List<IyBmItemCk> list = bmItemCkMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<IyBmItemCk> getBmItemsByCoundAndType(String bmCode, String bmType) {
        IyBmItemExample example = new IyBmItemExample();
        example.or().andBmTypeEqualTo(bmType).andBmCodeEqualTo(bmCode);
        example.setOrderByClause("store asc");
        List<IyBmItem> list = bmItemMapper.selectByExample(example);
        return convertBmItemToCk(list);
    }

    /**
     * 将bm明细正式表数据转换为ck的数据结构，没有的字段为null保持
     * 
     * @param list
     * @return
     */
    private List<IyBmItemCk> convertBmItemToCk(List<IyBmItem> list) {
        List<IyBmItemCk> rest = new ArrayList<>();
        if (list != null && list.size() > ConstantsDB.COMMON_ZERO) {
            for (IyBmItem ls : list) {
                IyBmItemCk ck = new IyBmItemCk();
                BeanUtils.copyProperties(ls, ck);
                ck.setNewNo(null);
                // 所有正式表数据的确认状态都是 已确认
                ck.setBmItemFlg(ConstantsDB.COMMON_ONE + "");
                rest.add(ck);
            }
        }
        return rest;
    }

    /**
     * 将bm正式主档数据转换为ck主档，缺少或多于的字段均为null
     * 
     * @param bm
     * @return
     */
    private IyBmCk convertBmToCk(IyBm bm) {
        if (bm == null) {
            return null;
        }
        IyBmCk ck = new IyBmCk();
        BeanUtils.copyProperties(bm, ck);
        ck.setRightFlg("11111");
        ck.setCheckFlg("2");
        ck.setOpFlg("27");
        ck.setFirstFlg("0");
        return ck;
    }

    /**
     * 向ck表取得主档数据
     * 
     * @param bmCode
     * @param bmType
     * @return
     */
    @Override
    public IyBmCk getBmCkBaseByCodeAndType(String bmCode, String bmType) {
        IyBmCkExample ckExample = new IyBmCkExample();
        ckExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
        ckExample.setOrderByClause("store asc");
        List<IyBmCk> baseBmCk = bmCkMapper.selectByExample(ckExample);
        if (baseBmCk == null || baseBmCk.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        IyBmCk temp = baseBmCk.get(ConstantsDB.COMMON_ZERO);
        List<String> store = new ArrayList<>();
        for (IyBmCk ck : baseBmCk) {
            store.add(ck.getStore());
        }
        String str = StringUtils.join(store.toArray(), SPLIT_COMMA);
        temp.setStore(str);
        return temp;
    }

    @Override
    public IyBm getBmBaseByCodeAndType(String bmCode, String bmType) {
        IyBmExample example = new IyBmExample();
        example.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
        example.setOrderByClause("store asc");
        List<IyBm> baseBm = bmMapper.selectByExample(example);
        if (baseBm == null || baseBm.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        IyBm temp = baseBm.get(ConstantsDB.COMMON_ZERO);
        List<String> store = new ArrayList<>();
        for (IyBm ck : baseBm) {
            store.add(ck.getStore());
        }
        String str = StringUtils.join(store.toArray(), SPLIT_COMMA);
        temp.setStore(str);
        return temp;
    }

    @Override
    public AjaxResultDto deleteBmByParam(String bmCode, String bmType, Integer tableType,
            Integer identity, User u) {
        AjaxResultDto rest = new AjaxResultDto();
        String hisOpFlg = "";
        if (identity.equals(1)) {
            hisOpFlg = "06";
        } else {
            hisOpFlg = "26";
        }
        Date updateDate = TimeUtil.getDate();

        if (tableType.equals(ConstantsDB.COMMON_ONE)) {
            BmService thisService = Container.getBean(BmService.class);
            // 判断数据源表为1=CK表
            // 删除时要取得要删除的内容，然后将内容插入his表后 在进行删除
            IyBmCkExample ckExample = new IyBmCkExample();
            ckExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);

            IyBmItemCkExample itemCkExample = new IyBmItemCkExample();
            itemCkExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);

            List<IyBmCk> bmCkList = bmCkMapper.selectByExample(ckExample);

            String newNo = bmCkList.get(0).getNewNo();
            String newNoSub = thisService.getBmNewNoSub(newNo);
            // 得到将要删除的数据主档
            for (IyBmCk ck : bmCkList) {
                IyBmHis his = new IyBmHis();
                BeanUtils.copyProperties(ck, his);
                his.setNewNo(newNo);
                his.setNewNoSub(newNoSub);
                his.setUpdateDate(updateDate);
                his.setUserid(u.getUserId());
                his.setUsername(u.getUserName());
                his.setOpFlg(hisOpFlg);
                his.setNewFlg("2");
                bmHisMapper.insert(his);
            }
            // 得到将要删除的数据明细档
            List<IyBmItemCk> bmItemList = bmItemCkMapper.selectByExample(itemCkExample);
            for (IyBmItemCk item : bmItemList) {
                IyBmItemHis itemHis = new IyBmItemHis();
                BeanUtils.copyProperties(item, itemHis);
                itemHis.setNewNo(newNo);
                itemHis.setNewNoSub(newNoSub);
                bmItemHisMapper.insert(itemHis);
            }
            // ck主档
            int delCount = bmCkMapper.deleteByExample(ckExample);
            // ck明细
            bmItemCkMapper.deleteByExample(itemCkExample);

            rest.setMessage("Deleted succeed!");
            rest.setSuccess(true);

        } else if (tableType.equals(ConstantsDB.COMMON_ZERO)) {
            // 判断数据源表为0=正式表
            // 删除时要取得要删除的内容，然后将内容插入his表后 在进行删除

            IyBmExample example = new IyBmExample();
            example.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
            IyBmItemExample itemExample = new IyBmItemExample();
            itemExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);

            BmService thisService = Container.getBean(BmService.class);
            String newNo = thisService.getBmNewNo();
            String newNoSub = thisService.getBmNewNoSub(newNo);

            List<IyBm> bmList = bmMapper.selectByExample(example);
            for (IyBm hs : bmList) {
                IyBmHis his = new IyBmHis();
                BeanUtils.copyProperties(hs, his);
                his.setNewNo(newNo);
                his.setNewNoSub(newNoSub);
                his.setUpdateDate(updateDate);
                his.setUserid(u.getUserId());
                his.setUsername(u.getUserName());
                his.setOpFlg(hisOpFlg);
                his.setNewFlg("2");
                bmHisMapper.insert(his);
            }

            List<IyBmItem> bmItemList = bmItemMapper.selectByExample(itemExample);
            for (IyBmItem item : bmItemList) {
                IyBmItemHis itemHis = new IyBmItemHis();
                BeanUtils.copyProperties(item, itemHis);
                itemHis.setNewNo(newNo);
                itemHis.setNewNoSub(newNoSub);
                bmItemHisMapper.insert(itemHis);
            }

            // ck主档
            bmMapper.deleteByExample(example);
            // ck明细
            bmItemMapper.deleteByExample(itemExample);
            rest.setMessage("Deleted succeed!");
            rest.setSuccess(true);
        }
        return rest;
    }

    @Override
    public AjaxResultBmDto getItemInfoByCode(String itemCode) {
        IyItemM item = itemService.getItemByCode(itemCode);
        AjaxResultBmDto dto = new AjaxResultBmDto();
        if (item != null) {
            dto.setData(item);
            dto.setSuccess(true);
        } else {
            dto.setData(null);
            dto.setMessage("根据Item Barcode未取得商品信息");
            dto.setSuccess(false);
        }
        return dto;
    }

    @Override
    public List<BmListGridDataDTO> getSecretaryCheckCount(String pCode) {
        List<BmListGridDataDTO> rest = new ArrayList<>();
        List<IyResourceDTO> resourceList = getResourceList(pCode);
        // dpt 资源
        List<String> dptList = new ArrayList<>();
        // dpt资源组合
        String quote = Matcher.quoteReplacement(REGULAR_TEXT);
        // 去重得到该用户的所有事业部 只保存第一位
        Set<String> divSet = new HashSet<>();
        for (IyResourceDTO rs : resourceList) {
            String tempDpt = rs.getDpt();
            divSet.add(tempDpt.substring(ConstantsDB.COMMON_ZERO, ConstantsDB.COMMON_ONE));
            dptList.add(tempDpt.replaceAll(STR_NINE, quote));
        }
        // 初始化标记，标记全部处理完成后，将标记内所有为9的值变为正则，
        String[] rightFlg = { "9", "9", "9", "9", "9" };
        for (String s : divSet) {
            switch (s) {
            case "1":
                rightFlg[0] = "0";
                break;
            case "2":
                rightFlg[1] = "0";
                break;
            case "3":
                rightFlg[2] = "0";
                break;
            case "4":
                rightFlg[3] = "0";
                break;
            case "5":
                rightFlg[4] = "0";
                break;
            }
        }
        String selectRightFlg = StringUtils.join(rightFlg);
        selectRightFlg = selectRightFlg.replaceAll(STR_NINE, quote);
        // 0-所有采购员均已确认，商品部长未审核
        String checkFlg = "0";
        rest = bmCkMapper.getSecretaryCheckCount(checkFlg, selectRightFlg, dptList);
        return rest;
    }

    /**
     * 根据权限得到处理好的资源数据
     * 
     * @param pCode
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<IyResourceDTO> getResourceList(String pCode) {
        List<Integer> roleIds = (List<Integer>) commonService.getSessionUserRoleIds();
        Collection<ResourceGroup> resourceGroup = iyRoleService.getResourcesByRoleAndPCode(roleIds,
                pCode);
        if (resourceGroup == null || resourceGroup.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        // 格式化 资源 翻译成dpt+store
        ConditionDTO dto = iyRoleService.createConditionsFromResourceGroup(resourceGroup);
        return dto.getResources();
    }

    @Override
    public Integer getUrgencyCountCount(List<BmListGridDataDTO> checkCount) {
        if (checkCount == null || checkCount.size() == ConstantsDB.COMMON_ZERO) {
            return ConstantsDB.COMMON_ZERO;
        } else {
            int urgencyCount = ConstantsDB.COMMON_ZERO;
            for (BmListGridDataDTO dto : checkCount) {
                if (dto.getFirstFlg() != null && dto.getFirstFlg().equals(ConstantsDB.COMMON_ONE)) {
                    urgencyCount++;
                }
            }
            return urgencyCount;
        }
    }

    @Override
    public String getCheckResources(Collection<Integer> roleIds, Integer identity) {
        String pcode = "";
        if (identity.equals(ConstantsDB.COMMON_ONE)) {
            pcode = PermissionCode.P_CODE_BM_PRO_AFFIRM;
        } else if (identity.equals(ConstantsDB.COMMON_TWO)) {
            pcode = PermissionCode.P_CODE_BM_DIV_LEADER_CHECK;
        } else if (identity.equals(ConstantsDB.COMMON_THREE)) {
            pcode = PermissionCode.P_CODE_BM_SYS_CHECK;
        }

        Collection<ResourceGroup> resourceGroup = iyRoleService.getResourcesByRoleAndPCode(
                (List<Integer>) roleIds, pcode);
        if (resourceGroup == null || resourceGroup.size() == ConstantsDB.COMMON_ZERO) {
            return "";
        }
        // 格式化 资源 翻译成dpt+store
        ConditionDTO dto = iyRoleService.createConditionsFromResourceGroup(resourceGroup);
        if (dto == null) {
            return "";
        } else {
            List<String> restList = new ArrayList<>();
            for (IyResourceDTO rs : dto.getResources()) {
                restList.add(rs.getDpt());
            }
            return StringUtils.join(restList.toArray(), SPLIT_COMMA);
        }
    }

    @Override
    public AjaxResultDto updateAffirmBm(String bmCode, String bmType, String staffResource,
            String dataResource, String opFlg, String rejectreason, Integer identity, User u) {
        AjaxResultDto rest = new AjaxResultDto();

        String checkFlg = getCheckFlgByOpflg(opFlg);
        String bmItemFlg = "0";
        if (checkFlg.equals("3")) {
            bmItemFlg = "2";
        } else {
            bmItemFlg = "1";
        }
        List<String> items = new ArrayList<>();
        // 将未审核的数据字符串转换为数组 ，等到与用户的审核资源做匹配，并将匹配上的单品号码加入修改条件中
        String[] ItemAndDptObj = dataResource.split(SPLIT_COMMA);
        staffResource = staffResource.replaceAll(STR_NINE, "");
        String[] staffResourceObj = staffResource.split(SPLIT_COMMA);
        for (int i = 0; i < staffResourceObj.length; i++) {
            String r = staffResourceObj[i];
            // 因为有可用有位数不匹配，
            int r_lastIndex = r.length();
            for (int j = 0; j < ItemAndDptObj.length; j++) {
                // 去除的内容的格式是 item#dpt的组合形式 需要将此字符串切分
                String itemAndDptStr = ItemAndDptObj[j];
                // tempStr[0] = item,tempStr[1] = dpt
                String[] tempStr = itemAndDptStr.split("-");
                // 要疯
                if (r.equals(tempStr[1].subSequence(0, r_lastIndex))) {
                    items.add(tempStr[0]);
                }
            }

        }

        if (items == null || items.size() == 0) {
            rest.setSuccess(false);
            rest.setMessage("当前bm没有需要确认或驳回的内容");
            return rest;
        }

        // 更新明细数据，要区分不同的采购确认时由不同的资源，
        IyBmItemCkExample itemCkExample = new IyBmItemCkExample();
        itemCkExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType).andItemIn(items);
        IyBmItemCk itemRecord = new IyBmItemCk();
        itemRecord.setBmItemFlg(bmItemFlg);
        bmItemCkMapper.updateByExampleSelective(itemRecord, itemCkExample);

        IyBmItemCkExample notAffirmItemCountExample = new IyBmItemCkExample();
        notAffirmItemCountExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType)
                .andBmItemFlgEqualTo("0");
        long notAffirmItemCount = bmItemCkMapper.countByExample(notAffirmItemCountExample);
        if (opFlg.equals("07") && notAffirmItemCount > 0) {
            // 存在未确认的数据，则设定主档数据为4-采购员确认中
            checkFlg = "4";
        }

        IyBmCkExample bmCkExample = new IyBmCkExample();
        bmCkExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
        IyBmCk record = new IyBmCk();
        Date updateDate = TimeUtil.getDate();
        record.setCheckFlg(checkFlg);
        record.setOpFlg(opFlg);
        record.setRejectreason(rejectreason);
        record.setUpdateDate(updateDate);
        record.setUserid(u.getUserId());

        bmCkMapper.updateByExampleSelective(record, bmCkExample);

        // 增加HIS记录，先拿到已经变更的内容，然后继续
        List<IyBmCk> bmCk = bmCkMapper.selectByExample(bmCkExample);
        BmService thisService = Container.getBean(BmService.class);
        String newNo = bmCk.get(0).getNewNo();
        String newNoSub = thisService.getBmNewNoSub(newNo);

        for (IyBmCk ck : bmCk) {
            IyBmHis hs = new IyBmHis();
            BeanUtils.copyProperties(ck, hs);
            hs.setNewNo(newNo);
            hs.setNewNoSub(newNoSub);
            hs.setUsername(u.getUserName());
            bmHisMapper.insert(hs);
        }

        IyBmItemCkExample bmItemCkExample = new IyBmItemCkExample();
        bmItemCkExample.or().andBmTypeEqualTo(bmType).andBmCodeEqualTo(bmCode);
        List<IyBmItemCk> bmItemCk = bmItemCkMapper.selectByExample(bmItemCkExample);
        for (IyBmItemCk ck : bmItemCk) {
            IyBmItemHis hs = new IyBmItemHis();
            BeanUtils.copyProperties(ck, hs);
            hs.setNewNo(newNo);
            hs.setNewNoSub(newNoSub);
            bmItemHisMapper.insert(hs);
        }
        rest.setSuccess(true);
        rest.setMessage("Operation Succeeded!");
        return rest;
    }

    @Override
    public AjaxResultDto updateCheckBm(String bmCode, String bmType, String opFlg,
            String staffResource, String rejectreason, Integer identity, User u) {
        AjaxResultDto rest = new AjaxResultDto();

        IyBmCkExample example = new IyBmCkExample();
        example.or().andBmTypeEqualTo(bmType).andBmCodeEqualTo(bmCode);
        List<IyBmCk> iyBmCkList = bmCkMapper.selectByExample(example);
        IyBmCk bmCk = null;
        if (iyBmCkList != null && iyBmCkList.size() > 0) {
            bmCk = iyBmCkList.get(0);
        } else {
            rest.setSuccess(false);
            rest.setMessage("审核失败，数据不存在");
            return rest;
        }
        String checkFlg = getCheckFlgByOpflg(opFlg);
        IyBmCk record = new IyBmCk();

        // 如果是事业部（商品部审核）需要处理 right_flg标记，
        if (opFlg.equals("17")) {
            // 将当前操作人的事业部资源用逗号切分为数组
            String[] staffResourceObj = staffResource.split(SPLIT_COMMA);
            // 每一个字符切分为数组，正常应该为5位
            String[] rightFlgObj = bmCk.getRightFlg().split("");
            for (String r : staffResourceObj) {
                // 只使用第一位
                String sub = r.substring(0, 1);
                switch (sub) {
                case "1":
                    rightFlgObj[0] = "1";
                    break;
                case "2":
                    rightFlgObj[1] = "1";
                    break;
                case "3":
                    rightFlgObj[2] = "1";
                    break;
                case "4":
                    rightFlgObj[3] = "1";
                    break;
                case "5":
                    rightFlgObj[4] = "1";
                    break;
                case "9":
                    rightFlgObj[0] = "1";
                    rightFlgObj[1] = "1";
                    rightFlgObj[2] = "1";
                    rightFlgObj[3] = "1";
                    rightFlgObj[4] = "1";
                    break;
                }
            }
            record.setRightFlg(StringUtils.join(rightFlgObj));// rightFlg
            if (!record.getRightFlg().equals("11111")) {
                checkFlg = null;
            }
        }
        Date updateDate = TimeUtil.getDate();
        record.setOpFlg(opFlg);
        record.setCheckFlg(checkFlg);
        record.setRejectreason(rejectreason);
        record.setUpdateDate(updateDate);
        record.setUserid(u.getUserId());
        bmCkMapper.updateByExampleSelective(record, example);

        // 增加HIS记录，先拿到已经变更的内容，然后继续
        List<IyBmCk> lastBmCk = bmCkMapper.selectByExample(example);
        BmService thisService = Container.getBean(BmService.class);
        String newNo = lastBmCk.get(0).getNewNo();
        String newNoSub = thisService.getBmNewNoSub(newNo);
        for (IyBmCk ck : lastBmCk) {
            IyBmHis hs = new IyBmHis();
            BeanUtils.copyProperties(ck, hs);
            hs.setNewNo(newNo);
            hs.setNewNoSub(newNoSub);
            hs.setUsername(u.getUserName());
            bmHisMapper.insert(hs);
        }
        IyBmItemCkExample bmItemCkExample = new IyBmItemCkExample();
        bmItemCkExample.or().andBmTypeEqualTo(bmType).andBmCodeEqualTo(bmCode);
        List<IyBmItemCk> bmItemCk = bmItemCkMapper.selectByExample(bmItemCkExample);
        for (IyBmItemCk ck : bmItemCk) {
            IyBmItemHis hs = new IyBmItemHis();
            BeanUtils.copyProperties(ck, hs);
            hs.setNewNo(newNo);
            hs.setNewNoSub(newNoSub);
            bmItemHisMapper.insert(hs);
        }

        if (opFlg.equals("27")) {
            // 搜寻正式主档是否有数据，如果有 则先删除然后在Add，并his表的记录是修改
            IyBmExample bmExample = new IyBmExample();
            bmExample.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
            long bmCount = bmMapper.countByExample(bmExample);
            if (bmCount > 0) {
                // 需要删除正式主档和明细数据
                bmMapper.deleteByExample(bmExample);
                IyBmItemExample bmItemEx = new IyBmItemExample();
                bmItemEx.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
                bmItemMapper.deleteByExample(bmItemEx);

            }
            // 主档数据
            List<IyBm> newBmMaster = convertCkListToBmList(iyBmCkList);

            // 明细档数据
            IyBmItemCkExample itemCkExa = new IyBmItemCkExample();
            itemCkExa.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
            List<IyBmItemCk> itemCkList = bmItemCkMapper.selectByExample(itemCkExa);
            List<IyBmItem> newBmItem = convertCkItemListToBmItemList(itemCkList);

            String newNoHis = thisService.getBmNewNo();
            String newNoSubHis = thisService.getBmNewNoSub(newNoHis);

            // for (IyBm order : newBmMaster) {
            // bmMapper.insert(order);
            // // 增加his记录
            // IyBmHis hs = new IyBmHis();
            // BeanUtils.copyProperties(order, hs);
            // hs.setNewNo(newNoHis);
            // hs.setNewNoSub(newNoSubHis);
            // hs.setUserid(u.getUserId());
            // hs.setUsername(u.getUserName());
            // hs.setUpdateDate(updateDate);
            // hs.setCheckFlg("2");
            // hs.setOpFlg("27");
            // bmHisMapper.insert(hs);
            // }
            // for (IyBmItem item : newBmItem) {
            // bmItemMapper.insert(item);
            // // 增加his记录
            // IyBmItemHis hs = new IyBmItemHis();
            // BeanUtils.copyProperties(item, hs);
            // hs.setNewNo(newNoHis);
            // hs.setNewNoSub(newNoSubHis);
            // bmItemHisMapper.insert(hs);
            // }

            // 系统部审核通过后，将数据载入正式表和his表后，删除当前ck表中对应type和code的所有数据
            bmCkMapper.deleteByExample(example);
            bmItemCkMapper.deleteByExample(itemCkExa);
        }

        rest.setSuccess(true);
        rest.setMessage("审核成功");
        return rest;
    }

    /**
     * 将ck明细实体类集合转换为正式明细实体类集合
     * 
     * @param iyBmCkList
     * @return
     */
    private List<IyBmItem> convertCkItemListToBmItemList(List<IyBmItemCk> ckList) {
        List<IyBmItem> bmList = new ArrayList<>();
        if (ckList != null && ckList.size() > 0) {
            for (IyBmItemCk ck : ckList) {
                IyBmItem bm = convertCkItemToBmItem(ck);
                bmList.add(bm);
            }
        }
        return bmList;
    }

    /**
     * 将ck主档实体类 转换 为正式主档实体类
     * 
     * @param iyBmCkList
     * @return
     */
    private IyBmItem convertCkItemToBmItem(IyBmItemCk ck) {
        if (ck == null) {
            return null;
        }
        IyBmItem bm = new IyBmItem();
        BeanUtils.copyProperties(ck, bm);
        return bm;
    }

    /**
     * 将ck主档实体类集合转换为正式主档实体类集合
     * 
     * @param iyBmCkList
     * @return
     */
    private List<IyBm> convertCkListToBmList(List<IyBmCk> iyBmCkList) {
        List<IyBm> bmList = new ArrayList<>();
        if (iyBmCkList != null && iyBmCkList.size() > 0) {
            Integer onlineDate = Integer.parseInt(TimeUtil.formatShortDate3(TimeUtil.getDate()));
            for (IyBmCk ck : iyBmCkList) {
                IyBm bm = convertCkToBm(ck, onlineDate);
                bmList.add(bm);
            }
        }
        return bmList;
    }

    /**
     * 将ck主档实体类 转换 为正式主档实体类
     * 
     * @param iyBmCkList
     * @return
     */
    private IyBm convertCkToBm(IyBmCk ck, Integer onlineDate) {
        if (ck == null) {
            return null;
        }
        IyBm bm = new IyBm();
        BeanUtils.copyProperties(ck, bm);
        bm.setOnlineDate(onlineDate);
        return bm;
    }

    @Override
    public AjaxResultDto updateCheckBm(String bmCode, String bmType, String bmTypeCode,
            String opFlg, String staffResource, String rejectreason, Integer identity, User u) {
        AjaxResultDto rest = new AjaxResultDto();
        if ((bmCode == null && bmType == null) && bmTypeCode == null) {
            rest.setSuccess(false);
            rest.setMessage("审核失败，必要的参数没有值");
            return rest;
        }
        List<String> bmParam = new ArrayList<>();
        if (bmCode != null && !bmCode.equals("") && bmType != null && !bmType.equals("")) {
            bmParam.add(bmCode + "-" + bmType);
        }
        if (bmTypeCode != null && !bmTypeCode.equals("")) {
            List<String> tempList = new ArrayList<>();
            Collections.addAll(tempList, bmTypeCode.split(SPLIT_COMMA));
            bmParam.addAll(tempList);
        }
        rest.setSuccess(true);
        rest.setMessage("审核成功");
        for (String param : bmParam) {
            String[] bm = param.split("-");
            AjaxResultDto eachRest = updateCheckBm(bm[0], bm[1], opFlg, staffResource,
                    rejectreason, identity, u);
            if (!eachRest.isSuccess()) {
                rest = eachRest;
                // 若批量处理执行时，执行到此位置后，前面已经执行成功的内容都会成功载入系统，后续将不会，此处不设定事物回滚机制
                break;
            }
        }

        return rest;
    }

    private String getCheckFlgByOpflg(String opFlg) {
        /*
         * opFlg
         * 01-采购员提交，07-采购员确认BM明细单品，08-采购员驳回BM明细单品，17-商品部长审核通过，18-商品部长驳回，21-
         * 系统部提交， 27-系统部审核通过，28-系统部驳回;
         * 
         * checkFlg 0-所有采购员均已确认，商品部长未审核 1-商品部长已审核（通过），系统部未审核 2-系统部审核通过
         * 3-已驳回(包括采购员驳回，部长驳回，系统部驳回) 4-采购员确认中
         */
        String checkFlg = "";
        switch (opFlg) {
        case "07":
            checkFlg = "0";
            break;
        case "17":
            checkFlg = "1";
            break;
        case "27":
            checkFlg = "2";
            break;
        case "08":
        case "18":
        case "28":
            checkFlg = "3";
            break;
        }
        return checkFlg;
    }

    @Override
    public AjaxResultBmDto getDataByCodeType(String bmCode, String bmType, Integer identity) {
        AjaxResultBmDto rest = new AjaxResultBmDto();
        BmService thisService = Container.getBean(BmService.class);
        // 如果ck表存在当前bm则给出提示
        IyBmCkExample example = new IyBmCkExample();
        example.or().andBmCodeEqualTo(bmCode).andBmTypeEqualTo(bmType);
        long count = bmCkMapper.countByExample(example);
        if (count > 0) {
            rest.setSuccess(false);
            rest.setMessage("BM编码：" + bmCode + "  类型：" + bmType + " 的数据已存在流程中，请处理完成后，再进行修改 ");
            rest.setData(null);
            return rest;
        }
        BmViewMainDTO data = thisService.getBmViewData(bmCode, bmType, 0);
        if (data != null) {
            rest.setSuccess(true);
            rest.setData(data);
        } else {
            rest.setSuccess(false);
            rest.setMessage("根据BM编码：" + bmCode + " 和 类型：" + bmType + "  未得到相关BM数据");
            rest.setData(null);
        }
        return rest;
    }

    @Override
    public AjaxResultDto deleteListBmByEndDate(String endDate, User u) {
        AjaxResultDto result = new AjaxResultDto();
        IyBmExample selectExample = new IyBmExample();
        selectExample.or().andBmEffToLessThan(Integer.parseInt(endDate));
        List<IyBm> bmList = bmMapper.selectByExample(selectExample);
        if (bmList != null && bmList.size() > 0) {

            Map<String, List<IyBm>> map = new HashMap<>();
            // 将一组bm组合在一起方便删除使用
            for (IyBm bm : bmList) {
                String key = bm.getBmType() + "-" + bm.getBmCode();
                List<IyBm> list = map.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(bm);
                map.put(key, list);
            }
            Date updateDate = TimeUtil.getDate();
            BmService thisService = Container.getBean(BmService.class);
            for (Map.Entry<String, List<IyBm>> entry : map.entrySet()) {
                String newNo = thisService.getBmNewNo();
                String newNoSub = thisService.getBmNewNoSub(newNo);
                List<IyBm> list = entry.getValue();
                for (IyBm bm : list) {
                    IyBmHis record = new IyBmHis();
                    BeanUtils.copyProperties(bm, record);
                    record.setOpFlg("26");
                    record.setNewFlg("2");
                    record.setNewNo(newNo);
                    record.setNewNoSub(newNoSub);
                    record.setUserid(u.getUserId());
                    record.setUsername(u.getUserName());
                    record.setUpdateDate(updateDate);
                    bmHisMapper.insert(record);
                }
                String[] keyObj = entry.getKey().split("-");
                IyBmItemExample itemExample = new IyBmItemExample();
                itemExample.or().andBmTypeEqualTo(keyObj[0]).andBmCodeEqualTo(keyObj[1]);
                List<IyBmItem> itenList = bmItemMapper.selectByExample(itemExample);
                for (IyBmItem item : itenList) {
                    IyBmItemHis record = new IyBmItemHis();
                    BeanUtils.copyProperties(item, record);
                    record.setNewNo(newNo);
                    record.setNewNoSub(newNoSub);
                    bmItemHisMapper.insert(record);
                }
                // 历史记录都载入完成后 开始删除正式表的数据
                IyBmExample delBmExample = new IyBmExample();
                delBmExample.or().andBmTypeEqualTo(keyObj[0]).andBmCodeEqualTo(keyObj[1]);
                bmMapper.deleteByExample(delBmExample);
                IyBmItemExample delBmItemExample = new IyBmItemExample();
                delBmItemExample.or().andBmTypeEqualTo(keyObj[0]).andBmCodeEqualTo(keyObj[1]);
                bmItemMapper.deleteByExample(delBmItemExample);

            }

        }
        result.setSuccess(true);
        result.setMessage("Deleted succeed!");
        return result;
    }

    @Override
    public List<IyResourceDTO> paramResourceList(List<Integer> roleIds, String pCode) {
        // 拿到当前操作人的dpt资源
        // dpt资源组合
        Collection<ResourceGroup> resourceGroup = iyRoleService.getResourcesByRoleAndPCode(roleIds,
                pCode);
        if (resourceGroup == null || resourceGroup.size() == ConstantsDB.COMMON_ZERO) {
            return null;
        }
        // 格式化 资源 翻译成dpt+store
        ConditionDTO dto = iyRoleService.createConditionsFromResourceGroup(resourceGroup);
        List<IyResourceDTO> resourceList = dto.getResources();// dto.getResources();
        if (resourceList == null || resourceList.size() == ConstantsDB.COMMON_ZERO) {
            // 当前人没有资源，不可以进行数据获取
            return null;
        }
        List<IyResourceDTO> rest = new ArrayList<>();
        String quote = Matcher.quoteReplacement(REGULAR_TEXT);
        for (IyResourceDTO rs : resourceList) {
            IyResourceDTO changeDto = new IyResourceDTO();
            String tempDpt = rs.getDpt();
            if (tempDpt.equals(ConstantsDB.ALL_DPT)) {
                rest = null;
                break;
            }
            tempDpt = tempDpt.replaceAll(STR_NINE, quote);
            changeDto.setDpt(tempDpt);
            if (ConstantsDB.ALL_STORE.equals(rs.getStore())) {
                changeDto.setStore(null);
            } else {
                changeDto.setStore(rs.getStore());
            }
            rest.add(changeDto);
        }
        return rest;
    }

}
