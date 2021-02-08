package cn.com.bbut.iy.itemmaster.constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 审核
 * @author zcz
 */
public class ConstantsAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 审核类型、流程、表、字段对应 */



    // --------------------- 订货  dc紧急订货 ---------------------
    public static final int TYPE_ORDER = 1;

    public static final int REVIEW_ORDER = 2;

    public static final String TABLE_ORDER = "od8025";

    public static final String KEY_ORDER = "order_id";

    // --------------------- 订货  dc紧急订货 ---------------------
    public static final int TYPE_ORDER_DC = 2;

    public static final int REVIEW_ORDER_DC = 1;

    public static final String TABLE_ORDER_DC = "od0000";

    public static final String KEY_ORDER_DC = "order_id";

    // --------------------- 库存废弃       ---------------------
    public static final int TYPE_ORDER_STOCK_SCRAP = 3;

    public static final int REVIEW_ORDER_STOCK_SCRAP = 7;

    public static final String TABLE_ORDER_STOCK_SCRAP = "sk0010";

    public static final String KEY_ORDER_STOCK_SCRAP = "voucher_no";

    // --------------------- 库存调拨 out      ---------------------
    public static final int TYPE_ORDER_TRANSFEROUT = 4;

    public static final int REVIEW_ORDER_TRANSFEROUT = 8;

    public static final String TABLE_ORDER_TRANSFEROUT = "sk0010";

    public static final String KEY_ORDER_TRANSFEROUT = "voucher_no";

    // --------------------- 库存调拨 in      ---------------------
    public static final int TYPE_ORDER_TRANSFERIN = 15;

    public static final int REVIEW_ORDER_TRANSFERIN = 19;

    public static final String TABLE_ORDER_TRANSFERIN = "sk0010";

    public static final String KEY_ORDER_TRANSFERIN = "voucher_no";

    // --------------------- 库存调整       ---------------------
    public static final int TYPE_ORDER_STOCK_ADJUSTMENT = 5;

    public static final int REVIEW_ORDER_STOCK_ADJUSTMENT = 9;

    public static final String TABLE_ORDER_STOCK_ADJUSTMENT = "sk0010";

    public static final String KEY_ORDER_STOCK_ADJUSTMENT = "voucher_no";

    // --------------------- 库存店内调拨      ---------------------
    public static final int TYPE_ORDER_ITEM_TRANSFER = 23;

    public static final int REVIEW_ORDER_ITEM_TRANSFER = 27;

    public static final String TABLE_ORDER_ITEM_TRANSFER = "sk0010";

    public static final String KEY_ORDER_ITEM_TRANSFER = "voucher_no";


    // --------------------- 盘点       ---------------------
    public static final int TYPE_ORDER_TAKE_STOCK = 6;

    public static final int REVIEW_ORDER_TAKE_STOCK = 10;

    public static final String TABLE_ORDER_TAKE_STOCK = "pi0100";

    public static final String KEY_ORDER_TAKE_STOCK = "pi_cd";

    // --------------------- 退货    仓库退货   ---------------------
    public static final int TYPE_ORDER_RETURN_WAREHOUSE = 7;

    public static final int REVIEW_ORDER_RETURN_WAREHOUSE = 11;

    public static final String TABLE_ORDER_RETURN_WAREHOUSE = "od0000";

    public static final String KEY_ORDER_RETURN_WAREHOUSE = "order_id";

    // --------------------- 退货    供应商退货   ---------------------
    public static final int TYPE_ORDER_RETURN_VENDOR = 8;

    public static final int REVIEW_ORDER_RETURN_VENDOR = 12;

    public static final String TABLE_ORDER_RETURN_VENDOR = "od0000";

    public static final String KEY_ORDER_RETURN_VENDOR = "order_id";

    // --------------------- FS 库存登录   ---------------------
    public static final int TYPE_FS_STOCK = 9;

    public static final int REVIEW_FS_STOCK = 13;

    public static final String TABLE_FS_STOCK = "pi0145";

    public static final String KEY_FS_STOCK = "pi_cd";

    // --------------------- 费用录入   ---------------------
    public static final int TYPE_CUST_STOCK = 10;

    public static final int REVIEW_CUST_STOCK = 14;

    public static final String TABLE_CUST_STOCK = "pi0135";

    public static final String KEY_CUST_STOCK = "pi_cd";

    // --------------------- 原材料库存登录   ---------------------
    public static final int TYPE_MATERIAL_STOCK = 11;

    public static final int REVIEW_MATERIAL_STOCK = 15;

    public static final String TABLE_MATERIAL_STOCK = "pi0155";

    public static final String KEY_MATERIAL_STOCK = "pi_cd";


    // --------------------- 收货    仓库收货   ---------------------
    public static final int TYPE_RECEIPT_WAREHOUSE = 12;

    public static final int REVIEW_RECEIPT_WAREHOUSE = 16;

    public static final String TABLE_RECEIPT_WAREHOUSE = "od0000_t";

    public static final String KEY_RECEIPT_WAREHOUSE = "receive_id";

    // --------------------- 收货    供应商收货   ---------------------
    public static final int TYPE_RECEIPT_VENDOR = 13;

    public static final int REVIEW_RECEIPT_VENDOR = 17;

    public static final String TABLE_RECEIPT_VENDOR = "od0000_t";

    public static final String KEY_RECEIPT_VENDOR = "receive_id";

    // --------------------- 销售    收银员收款   ---------------------
    public static final int TYPE_CASHIER_AMOUNT = 14;

    public static final int REVIEW_CASHIER_AMOUNT = 18;

    public static final String TABLE_CASHIER_AMOUNT = "sa0170";

    public static final String KEY_CASHIER_AMOUNT = "pay_id";

    // --------------------- 收退货调整   ---------------------
    public static final int TYPE_REC_RET_CORRECTION = 16;

    public static final int REVIEW_REC_RET_CORRECTION = 20;

    public static final String TABLE_REC_RET_CORRECTION = "od0000";

    public static final String KEY_REC_RET_CORRECTION = "order_id";

    // --------------------- 转移调整   ---------------------
    public static final int TYPE_TRANSFER_CORRECTION = 17;

    public static final int REVIEW_TRANSFER_CORRECTION = 21;

    public static final String TABLE_TRANSFER_CORRECTION = "sk0010";

    public static final String KEY_TRANSFER_CORRECTION = "voucher_no";

    // --------------------- 支出管理   ---------------------
    public static final int TYPE_EXPENDITURE = 18;

    public static final int REVIEW_EXPENDITURE = 22;

    public static final String TABLE_EXPENDITURE = "op0040";

    public static final String KEY_EXPENDITURE = "voucher_no";

    // --------------------- dc退货确认管理   ---------------------
    public static final int TYPE_RETURN_ENTRY_WAREHOUSE = 19;

    public static final int REVIEW_RETURN_ENTRY_WAREHOUSE = 23;

    public static final String TABLE_RETURN_ENTRY_WAREHOUSE = "od0005";

    public static final String KEY_RETURN_ENTRY_WAREHOUSE = "order_id";

    // --------------------- vendor退货确认管理   ---------------------
    public static final int TYPE_RETURN_ENTRY_VENDOR = 20;

    public static final int REVIEW_RETURN_ENTRY_VENDOR = 24;

    public static final String TABLE_RETURN_ENTRY_VENDOR = "od0005";

    public static final String KEY_RETURN_ENTRY_VENDOR = "order_id";

    // --------------------- pos支付方式管理   ---------------------
    public static final int TYPE_POS_PAYMENT = 21;

    public static final int REVIEW_POS_PAYMENT = 25;

    public static final String TABLE_POS_PAYMENT = "sa0000";

    public static final String KEY_POS_PAYMENT = "pay_cd";

    // --------------------- POG管理   ---------------------
    public static final int TYPE_POG_MNG = 22;

    public static final int REVIEW_POG_MNG = 26;

    public static final String TABLE_POG_MNG = "ma1106";

    public static final String KEY_POG_MNG = "store_cd";

    // --------------------- 盘点计划   ---------------------
    public static final int TYPE_PLAN_STOCK = 24;

    public static final int REVIEW_PLAN_STOCK = 28;

    public static final String TABLE_PLAN_STOCK = "pi0105";

    public static final String KEY_PLAN_STOCK = "pi_cd";

    /**
     * 获取审核类型对应的主档表名、主键
     * @param typeId 审核类型
     * @return
     */
    public static Map<String,Object> getAuditInfo(int typeId){
        Map<String,Object> auditInfo = new HashMap<>();
        switch (typeId){
            case TYPE_ORDER:
                auditInfo.put("table",TABLE_ORDER);
                auditInfo.put("key",KEY_ORDER);
                break;
            case TYPE_ORDER_DC:
                auditInfo.put("table",TABLE_ORDER_DC);
                auditInfo.put("key",KEY_ORDER_DC);
            break;
            case TYPE_ORDER_STOCK_SCRAP:
                auditInfo.put("table",TABLE_ORDER_STOCK_SCRAP);
                auditInfo.put("key",KEY_ORDER_STOCK_SCRAP);
                break;
            case TYPE_ORDER_TRANSFEROUT:
                auditInfo.put("table",TABLE_ORDER_TRANSFEROUT);
                auditInfo.put("key",KEY_ORDER_TRANSFEROUT);
                break;
            case TYPE_ORDER_TRANSFERIN:
                auditInfo.put("table",TABLE_ORDER_TRANSFERIN);
                auditInfo.put("key",KEY_ORDER_TRANSFERIN);
                break;
            case TYPE_ORDER_STOCK_ADJUSTMENT:
                auditInfo.put("table",TABLE_ORDER_STOCK_ADJUSTMENT);
                auditInfo.put("key",KEY_ORDER_STOCK_ADJUSTMENT);
                break;
            case TYPE_ORDER_ITEM_TRANSFER:
                auditInfo.put("table",TABLE_ORDER_ITEM_TRANSFER);
                auditInfo.put("key",KEY_ORDER_ITEM_TRANSFER);
                break;
            case TYPE_ORDER_TAKE_STOCK:
                auditInfo.put("table",TABLE_ORDER_TAKE_STOCK);
                auditInfo.put("key",KEY_ORDER_TAKE_STOCK);
                break;
            case TYPE_ORDER_RETURN_WAREHOUSE:
                auditInfo.put("table",TABLE_ORDER_RETURN_WAREHOUSE);
                auditInfo.put("key",KEY_ORDER_RETURN_WAREHOUSE);
                break;
            case TYPE_ORDER_RETURN_VENDOR:
                auditInfo.put("table",TABLE_ORDER_RETURN_VENDOR);
                auditInfo.put("key",KEY_ORDER_RETURN_VENDOR);
                break;
            case TYPE_FS_STOCK:
                auditInfo.put("table",TABLE_FS_STOCK);
                auditInfo.put("key",KEY_FS_STOCK);
                break;
            case TYPE_CUST_STOCK:
                auditInfo.put("table",TABLE_CUST_STOCK);
                auditInfo.put("key",KEY_CUST_STOCK);
                break;
            case TYPE_MATERIAL_STOCK:
                auditInfo.put("table",TABLE_MATERIAL_STOCK);
                auditInfo.put("key",KEY_MATERIAL_STOCK);
                break;
            case TYPE_RECEIPT_WAREHOUSE:
                auditInfo.put("table",TABLE_RECEIPT_WAREHOUSE);
                auditInfo.put("key",KEY_RECEIPT_WAREHOUSE);
                break;
            case TYPE_RECEIPT_VENDOR:
                auditInfo.put("table",TABLE_RECEIPT_VENDOR);
                auditInfo.put("key",KEY_RECEIPT_VENDOR);
                break;
            case TYPE_CASHIER_AMOUNT:
                auditInfo.put("table",TABLE_CASHIER_AMOUNT);
                auditInfo.put("key",KEY_CASHIER_AMOUNT);
                break;
            case TYPE_REC_RET_CORRECTION://收退货修正
                auditInfo.put("table",TABLE_REC_RET_CORRECTION);
                auditInfo.put("key",KEY_REC_RET_CORRECTION);
                break;
            case TYPE_TRANSFER_CORRECTION://库存调入调出修正
                auditInfo.put("table",TABLE_TRANSFER_CORRECTION);
                auditInfo.put("key",KEY_TRANSFER_CORRECTION);
                break;
            case TYPE_EXPENDITURE://支付管理
                auditInfo.put("table",TABLE_EXPENDITURE);
                auditInfo.put("key",KEY_EXPENDITURE);
                break;
            case TYPE_RETURN_ENTRY_WAREHOUSE://退dc确认
                auditInfo.put("table",TABLE_RETURN_ENTRY_WAREHOUSE);
                auditInfo.put("key",KEY_RETURN_ENTRY_WAREHOUSE);
                break;
            case TYPE_RETURN_ENTRY_VENDOR://退vendor确认
                auditInfo.put("table",TABLE_RETURN_ENTRY_VENDOR);
                auditInfo.put("key",KEY_RETURN_ENTRY_VENDOR);
                break;
            case TYPE_POS_PAYMENT://pos支付方式
                auditInfo.put("table",TABLE_POS_PAYMENT);
                auditInfo.put("key",KEY_POS_PAYMENT);
                break;
            case TYPE_POG_MNG://POG管理
                auditInfo.put("table",TABLE_POG_MNG);
                auditInfo.put("key",KEY_POG_MNG);
                break;
            case TYPE_PLAN_STOCK://盘点计划
                auditInfo.put("table",TABLE_PLAN_STOCK);
                auditInfo.put("key",KEY_PLAN_STOCK);
                break;
            default:
                auditInfo.put("table",null);
                auditInfo.put("key",null);
            break;
        }
        return auditInfo;
    }

    /**
     * 获取审核类型
     * @param reviewId 流程id
     * @return
     */
    public static int getTypeIdByReviewId(int reviewId){
        int typeId = 0;
        switch (reviewId){
            case REVIEW_ORDER:
                typeId = TYPE_ORDER;
                break;
            case REVIEW_ORDER_DC:
                typeId = TYPE_ORDER_DC;
                break;
            case REVIEW_ORDER_STOCK_SCRAP:
                typeId = TYPE_ORDER_STOCK_SCRAP;
                break;
            case REVIEW_ORDER_TRANSFEROUT:
                typeId = TYPE_ORDER_TRANSFEROUT;
                break;
            case REVIEW_ORDER_TRANSFERIN:
                typeId = TYPE_ORDER_TRANSFERIN;
                break;
            case REVIEW_ORDER_STOCK_ADJUSTMENT:
                typeId = TYPE_ORDER_STOCK_ADJUSTMENT;
                break;
            case REVIEW_ORDER_ITEM_TRANSFER:
                typeId = TYPE_ORDER_ITEM_TRANSFER;
                break;
            case REVIEW_ORDER_TAKE_STOCK:
                typeId = TYPE_ORDER_TAKE_STOCK;
                break;
            case REVIEW_ORDER_RETURN_WAREHOUSE:
                typeId = TYPE_ORDER_RETURN_WAREHOUSE;
                break;
            case REVIEW_ORDER_RETURN_VENDOR:
                typeId = TYPE_ORDER_RETURN_VENDOR;
                break;
            case REVIEW_FS_STOCK:
                typeId = TYPE_FS_STOCK;
                break;
            case REVIEW_CUST_STOCK:
                typeId = TYPE_CUST_STOCK;
                break;
            case REVIEW_MATERIAL_STOCK:
                typeId = TYPE_MATERIAL_STOCK;
                break;
            case REVIEW_RECEIPT_WAREHOUSE:
                typeId = TYPE_RECEIPT_WAREHOUSE;
                break;
            case REVIEW_RECEIPT_VENDOR:
                typeId = TYPE_RECEIPT_VENDOR;
                break;
            case REVIEW_CASHIER_AMOUNT:
                typeId = TYPE_CASHIER_AMOUNT;
                break;
            case REVIEW_REC_RET_CORRECTION://收退货修正
                typeId = TYPE_REC_RET_CORRECTION;
                break;
            case REVIEW_TRANSFER_CORRECTION://库存调入调出修正
                typeId = TYPE_TRANSFER_CORRECTION;
                break;
            case REVIEW_EXPENDITURE://支出管理
                typeId = TYPE_EXPENDITURE;
                break;
            case REVIEW_RETURN_ENTRY_WAREHOUSE://退dc确认
                typeId = TYPE_RETURN_ENTRY_WAREHOUSE;
                break;
            case REVIEW_RETURN_ENTRY_VENDOR://退vendor确认
                typeId = TYPE_RETURN_ENTRY_VENDOR;
                break;
            case REVIEW_POS_PAYMENT://pos支付方式
                typeId = TYPE_POS_PAYMENT;
                break;
            case REVIEW_POG_MNG://POG管理
                typeId = TYPE_POG_MNG;
                break;
            case REVIEW_PLAN_STOCK://盘点计划
                typeId = TYPE_PLAN_STOCK;
                break;
            default:
                typeId = 0;
                break;
        }
        return typeId;
    }
}
