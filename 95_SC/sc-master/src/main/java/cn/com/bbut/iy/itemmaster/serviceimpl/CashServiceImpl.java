package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.CashMapper;
import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetailParam;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CashService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class CashServiceImpl implements CashService {
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private CashMapper cashMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    @Override
    public GridDataDTO<CashDetail> getCashDetailyList(CashDetailParam dto) {
        String businessDate = getBusinessDate();
        dto.setBusinessDate(businessDate);
        String payAmtFlg = dto.getPayAmtFlg();
        List<String> stores = new ArrayList<>(dto.getStores());
        String vStartDate = dto.getVStartDate();
        String vEndDate = dto.getVEndDate();
        String payAmtDiff = dto.getPayAmtDiff();
        String am = dto.getAm();
        int count = cashMapper.getCashDetailycount(stores, vStartDate, vEndDate, am);
        if (count<1) {
            return new GridDataDTO<CashDetail>();
        }
        List<CashDetail> list = cashMapper.getCashDetailyList(dto);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).getPayDate().equals(list.get(i + 1).getPayDate())
                    && list.get(i).getStoreCd().equals(list.get(i + 1).getStoreCd())
                    && list.get(i).getPayId().equals(list.get(i + 1).getPayId())) {
                    list.get(i).setPayInAmt0(list.get(i).getPayInAmt0().add(list.get(i + 1).getPayInAmt0()));
                    list.get(i).setPayInAmt1(list.get(i).getPayInAmt1().add(list.get(i + 1).getPayInAmt1()));
                    list.get(i).setPayInAmt2(list.get(i).getPayInAmt2().add(list.get(i + 1).getPayInAmt2()));
                    list.get(i).setAdditional(list.get(i).getAdditional().add(list.get(i + 1).getAdditional()));
                    list.get(i).setOffsetClaim(list.get(i).getOffsetClaim().add(list.get(i + 1).getOffsetClaim()));
                    list.remove(i + 1);
                    i--;
                }
            }
        }
        List<CashDetail> cashDetailList = cashMapper.getPayAmtList(dto);
        for(CashDetail cash:list){
            cash.setPayAmt(BigDecimal.ZERO);
            cash.setPayAmt0(BigDecimal.ZERO);
            cash.setPayAmt1(BigDecimal.ZERO);
            cash.setPayAmt2(BigDecimal.ZERO);
            cash.setPayAmtDiff(BigDecimal.ZERO);
            for(CashDetail cashDetail:cashDetailList){
                if(cash.getPayDate().equals(cashDetail.getPayDate())&&cash.getStoreCd().equals(cashDetail.getStoreCd())){
                    //应收各项金额
                    cash.setPayAmt(cashDetail.getPayAmt());
                    cash.setPayAmt0(cashDetail.getPayAmt0());
                    cash.setPayAmt1(cashDetail.getPayAmt1());
                    cash.setPayAmt2(cashDetail.getPayAmt2());
                    //差异金额
                    cash.setPayAmtDiff(cash.getPayInAmt().subtract(cashDetail.getPayAmt()));
                }
            }
        }
        if (StringUtils.isNotBlank(payAmtFlg) && StringUtils.isNotBlank(payAmtDiff)) {
            BigDecimal payAmtDiff1 = new BigDecimal(payAmtDiff);
            //payAmtFlg 1 <   2  ≤   3 >   4 ≥
            for(int i =0;i<list.size();i++){
                if ("1".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) >= 0) {
                    list.remove(i);
                    i--;
                } else if ("2".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) > 0) {
                    list.remove(i);
                    i--;
                } else if ("3".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) <= 0) {
                    list.remove(i);
                    i--;
                } else if ("4".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) < 0) {
                    list.remove(i);
                    i--;
                }
            }
        }

        for(CashDetail detail:list){
            String createYmd = detail.getCreateYmd();
            String createHms = detail.getCreateHms();
            String dateTime = dateFomte(createYmd, createHms);
            detail.setCreateYmdFull(dateTime);
        }
        GridDataDTO<CashDetail> data = new GridDataDTO<>(list, dto.getPage(), count, dto.getRows());
        return data;
    }

    @Override
    public String getUserName(String userId) {
        return cashMapper.getUserName(userId);
    }

    @Override
    public List<CashDetail> getPayList(CashDetail cashDetail, List<CashDetail> cashDetails) {
        List<CashDetail> payList;
        List<CashDetail> payList1;
        BigDecimal sumOfficam = BigDecimal.ZERO; // payCd:05
        BigDecimal sumShift1 = BigDecimal.ZERO; // 01
        BigDecimal sumShift2 = BigDecimal.ZERO; // 02
        BigDecimal sumShift3 = BigDecimal.ZERO; // 03
        BigDecimal  sumAdd = BigDecimal.ZERO; // 04
        BigDecimal  sumPiffAmount1=BigDecimal.ZERO;
        BigDecimal  sumPiffAmount2=BigDecimal.ZERO;
        BigDecimal  sumPiffAmount3=BigDecimal.ZERO;
        BigDecimal  sumPiffAmount4=BigDecimal.ZERO;
        BigDecimal  sumPiffAmount5=BigDecimal.ZERO;
        if (cashDetail != null) {
            if (cashDetail.getVoucherNo() == null || cashDetail.getVoucherNo().equals("")){
                // 如果选择下拉的voucherNo,就将数据导过来
                payList = cashMapper.getPayListByCondition(cashDetail);
            } else {
                payList = cashMapper.getPayListByVoucherNo(cashDetail);
                // payList = cashMapper.getPayListByCondition(cashDetail);
                for (CashDetail cahs : cashDetails) {
                    payList1 = cashMapper.getPayListByVoucherNo(cahs);
                    for (CashDetail cash2 : payList1) {
                        if (cash2.getPayCd().equals("01")){
                            sumShift1 =sumShift1.add(cash2.getPayInAmt());
                            sumPiffAmount1 =sumPiffAmount1.add(cash2.getPayAmtDiff());

                        }
                        if (cash2.getPayCd().equals("02")){
                            sumShift2 =sumShift2.add(cash2.getPayInAmt());
                            sumPiffAmount2 =sumPiffAmount2.add(cash2.getPayAmtDiff());
                        }
                        if (cash2.getPayCd().equals("03")){
                            sumShift3 =sumShift3.add(cash2.getPayInAmt());
                            sumPiffAmount3 =sumPiffAmount3.add(cash2.getPayAmtDiff());
                        }

                        if (cash2.getPayCd().equals("04")){
                            sumAdd =sumAdd.add(cash2.getPayInAmt());
                            sumPiffAmount4 =sumPiffAmount4.add(cash2.getPayAmtDiff());
                        }
                        if (cash2.getPayCd().equals("05")) {
                            sumOfficam = sumOfficam.add(cash2.getPayInAmt());
                            sumPiffAmount5 = sumPiffAmount5.add(cash2.getPayAmtDiff());
                        }
                    }
                }
                for (CashDetail cahs :payList) {
                    if (cahs.getPayCd().equals("01")) {
                        cahs.setPayInAmt(sumShift1);
                        cahs.setPayAmtDiff(sumPiffAmount1);
                    }
                    if (cahs.getPayCd().equals("02")) {
                        cahs.setPayInAmt(sumShift2);
                        cahs.setPayAmtDiff(sumPiffAmount2);
                    }
                    if (cahs.getPayCd().equals("03")) {
                        cahs.setPayInAmt(sumShift3);
                        cahs.setPayAmtDiff(sumPiffAmount3);
                    }
                    if (cahs.getPayCd().equals("04")) {
                        cahs.setPayInAmt(sumAdd);
                        cahs.setPayAmtDiff(sumPiffAmount4); // additional
                    }
                    if (cahs.getPayCd().equals("05")) {
                        cahs.setPayInAmt(sumOfficam);
                        cahs.setPayAmtDiff(sumPiffAmount5); // offset_claim
                    }
                }
            }
            cashDetail.setBusinessDate(getBusinessDate());
            if (payList.size() == 0) {
                payList = cashMapper.getPayList();

            } else {
                if (cashDetail.getVoucherNo() == null || cashDetail.getVoucherNo().equals("")) {
                    cashDetail = cashMapper.selectBaseInfo(cashDetail);
                }
            }
            cashDetail.setVoucherDate(cashDetail.getPayDate());
            // 日期格式转换
            cashDetail.setPayDate(Utils.getTimeStamp(cashDetail.getPayDate()));
            CashDetail payAmt = cashMapper.getPayAmt(cashDetail.getPayDate(), cashDetail.getStoreCd(),cashDetail.getVoucherDate());
            for (int i = 0; i < payList.size(); i++) {
                switch (payList.get(i).getPayCd()) {
                    case "01":
                        payList.get(i).setPayAmt(payAmt.getPayAmt0());  // shift1
                        payList.get(i).setSalesByHht(payAmt.getPayHHTAmt0());
                        if(payList.get(i).getPayInAmt()!= null && payAmt.getPayAmt0()!= null){
                            payList.get(i).setPayAmtDiff(payList.get(i).getPayInAmt().subtract(payAmt.getPayAmt0()));//计算差异金额
                        }
                        break;
                    case "02":
                        payList.get(i).setPayAmt(payAmt.getPayAmt1());  // shift2
                        payList.get(i).setSalesByHht(payAmt.getPayHHTAmt1());
                        if(payList.get(i).getPayInAmt()!= null && payAmt.getPayAmt1()!= null){
                            payList.get(i).setPayAmtDiff(payList.get(i).getPayInAmt().subtract(payAmt.getPayAmt1()));
                        }
                        break;
                    case "03":
                        payList.get(i).setPayAmt(payAmt.getPayAmt2());  // shift3
                        payList.get(i).setSalesByHht(payAmt.getPayHHTAmt2());
                        if(payList.get(i).getPayInAmt()!= null && payAmt.getPayAmt2()!= null) {
                            payList.get(i).setPayAmtDiff(payList.get(i).getPayInAmt().subtract(payAmt.getPayAmt2()));
                        }
                        break;
                }
            }
        } else {
            payList = cashMapper.getPayList();
        }
        return payList;
    }

    @Override
    public List<CashDetail> getCashList(CashDetail cashDetail) {
        List<CashDetail> cashList = new ArrayList<>();
        if (cashDetail != null && StringUtils.isNotBlank(cashDetail.getPayId())) {
            cashList = cashMapper.getCashListByCondition(cashDetail);
        } else {
            cashList = cashMapper.getCashList();
        }
        return cashList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CashDetail insertOrUpdate(CashDetail cashDetail, String payList, String cashList) {
        String businessDate = getBusinessDate();
        cashDetail.setBusinessDate(businessDate);
        List<CashDetail> payList1 = new ArrayList<>();
        List<CashDetail> cashList1 = new ArrayList<>();
        if (StringUtils.isNotBlank(payList)) {
            payList1 = new Gson().fromJson(payList, new TypeToken<List<CashDetail>>() {
            }.getType());
        }
        if (StringUtils.isNotBlank(cashList)) {
            cashList1 = new Gson().fromJson(cashList, new TypeToken<List<CashDetail>>() {
            }.getType());
        }
        boolean flg = false;
        if (StringUtils.isBlank(cashDetail.getPayId())) {
            String payId = sequenceService.getSequence("sa0170_pay_id_seq");
            if (StringUtils.isBlank(payId)) {
                //获取序列失败
                log.error("获取序列失败 getSequence: {}", "sa0170_pay_id_seq");
                RuntimeException e = new RuntimeException("获取序列失败[ sa0170_pay_id_seq ]");
                throw e;
            }
            cashDetail.setPayId(payId);
            flg = true;
        }
        boolean flg1 = flg;
        //Cash split 栏位选择Yes时才进行插入操作
        if (cashDetail.getCashSplitFlag().equals("1")) {
            cashList1.forEach(cash -> {
                cash.setPayId(cashDetail.getPayId());
                cash.setStoreCd(cashDetail.getStoreCd());
                cash.setPayDate(cashDetail.getPayDate());
                cash.setUserId(cashDetail.getUserId());
                cash.setUserName(cashDetail.getUserName());
                cash.setUpdateUserId(cashDetail.getUpdateUserId());
                cash.setUpdateYmd(cashDetail.getUpdateYmd());
                cash.setUpdateHms(cashDetail.getUpdateHms());
                if (flg1) {
                    cashMapper.insertSa0190(cash);
                } else {
                    cashMapper.updateSa0190(cash);
                }
            });
        }
        payList1.forEach(pay -> {
            pay.setPayId(cashDetail.getPayId());
            pay.setStoreCd(cashDetail.getStoreCd());
            pay.setPayDate(cashDetail.getPayDate());
            pay.setUserId(cashDetail.getUserId());
            pay.setUserName(cashDetail.getUserName());
            pay.setUpdateUserId(cashDetail.getUpdateUserId());
            pay.setUpdateYmd(cashDetail.getUpdateYmd());
            pay.setUpdateHms(cashDetail.getUpdateHms());
            pay.setRemark(cashDetail.getRemark());
            if (flg1) {
                cashMapper.insertSa0180(pay);
            } else {
                cashMapper.updateSa0180(pay);
            }
        });
        if (flg1) {
            cashMapper.insertSa0170(cashDetail);
        } else {
            cashMapper.updateSa0170(cashDetail);
        }
        return cashDetail;
    }

    /**
     * 获取收银员下拉列表
     */
    @Override
    public List<AutoCompleteDTO> getCashierList(String v, String storeCd) {
        return cashMapper.getCashierList(v, storeCd);

    }

    @Override
    public CashDetail getDetailBypayId(String payId) {
        return cashMapper.getDetailBypayId(payId);
    }

    @Override
    public List<AutoCompleteDTO> getStaff(String v, Collection<String> stores) {
        return cashMapper.getStaff(v, stores);
    }

    @Override
    public CashDetail getCashDetail(CashDetail cashDetail) {
        cashDetail.setBusinessDate(getBusinessDate());
        CashDetail cashInfo = null;
        if (cashDetail != null && StringUtils.isNotBlank(cashDetail.getPayId())) {
            //  view 状态
            cashInfo = cashMapper.selectBaseInfo(cashDetail);
            List<String> vouchNoS = Arrays.asList(cashInfo.getVoucherNo().split(","));
            if (vouchNoS!=null || !vouchNoS.equals("")){
                cashInfo.setVoucherNoList(vouchNoS);
            }
        } else {
            // 直接带出来
            cashInfo = cashMapper.getCashDetail(cashDetail);
//           List<String> vouchNo = Arrays.asList(cashInfo.getVoucherNo().split(","));
//            cashDetail.setVoucherNoList(vouchNo);
        }
        return cashInfo;
    }

    @Override
    public List<CashDetail> getCashDetailList(CashDetailParam dto) {
        dto.setFlg(false);
        String businessDate = getBusinessDate();
        dto.setBusinessDate(businessDate);
        String payAmtFlg = dto.getPayAmtFlg();
        String payAmtDiff = dto.getPayAmtDiff();

        List<CashDetail> list = cashMapper.getCashDetailyList(dto);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).getPayDate().equals(list.get(i + 1).getPayDate())
                        && list.get(i).getStoreCd().equals(list.get(i + 1).getStoreCd())
                        && list.get(i).getPayId().equals(list.get(i + 1).getPayId())) {
                    list.get(i).setPayInAmt0(list.get(i).getPayInAmt0().add(list.get(i + 1).getPayInAmt0()));
                    list.get(i).setPayInAmt1(list.get(i).getPayInAmt1().add(list.get(i + 1).getPayInAmt1()));
                    list.get(i).setPayInAmt2(list.get(i).getPayInAmt2().add(list.get(i + 1).getPayInAmt2()));
                    list.get(i).setAdditional(list.get(i).getAdditional().add(list.get(i + 1).getAdditional()));
                    list.get(i).setOffsetClaim(list.get(i).getOffsetClaim().add(list.get(i + 1).getOffsetClaim()));
                    list.remove(i + 1);
                    i--;
                }
            }
        }
        List<CashDetail> cashDetailList = cashMapper.getPayAmtList(dto);
        for(CashDetail cash:list){
            cash.setPayAmt(BigDecimal.ZERO);
            cash.setPayAmt0(BigDecimal.ZERO);
            cash.setPayAmt1(BigDecimal.ZERO);
            cash.setPayAmt2(BigDecimal.ZERO);
            cash.setPayAmtDiff(BigDecimal.ZERO);
            for(CashDetail cashDetail:cashDetailList){
                if(cash.getPayDate().equals(cashDetail.getPayDate())&&cash.getStoreCd().equals(cashDetail.getStoreCd())){
                    //应收各项金额
                    cash.setPayAmt(cashDetail.getPayAmt());
                    cash.setPayAmt0(cashDetail.getPayAmt0());
                    cash.setPayAmt1(cashDetail.getPayAmt1());
                    cash.setPayAmt2(cashDetail.getPayAmt2());
                    //差异金额
                    cash.setPayAmtDiff(cash.getPayInAmt().subtract(cashDetail.getPayAmt()));
                }
            }
        }
        if (StringUtils.isNotBlank(payAmtFlg) && StringUtils.isNotBlank(payAmtDiff)) {
            BigDecimal payAmtDiff1 = new BigDecimal(payAmtDiff);
            //payAmtFlg 1 <   2  ≤   3 >   4 ≥
            for(int i =0;i<list.size();i++){
                if ("1".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) >= 0) {
                    list.remove(i);
                    i--;
                } else if ("2".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) > 0) {
                    list.remove(i);
                    i--;
                } else if ("3".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) <= 0) {
                    list.remove(i);
                    i--;
                } else if ("4".equals(payAmtFlg) && list.get(i).getPayAmtDiff().compareTo(payAmtDiff1) < 0) {
                    list.remove(i);
                    i--;
                }
            }
        }
        return list;
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto = cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
  private String dateFomte(String date,String time){
      String year=date.substring(0,4);
      String month=date.substring(4,6);
      String day=date.substring(6,8);
      String hour=time.substring(0,2);
      String min=time.substring(2,4);
      String sec=time.substring(4,6);
      return day+"/"+month+"/"+year+" "+hour+":"+min+":"+sec;
  }
}
