package cn.com.bbut.iy.itemmaster.constant;

import java.io.Serializable;

/**
 * 权限常量类
 * 
 * @author lilw
 */
public class PermissionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 代审-个人 权限 */
    public static final String P_CODE_OTHER_SELF = "P-OTHER-001";
    /** 代审-系统 权限 */
    public static final String P_CODE_OTHER_SYSTEM = "P-OTHER-002";

    /** 角色 列表查看权限 */
    public static final String CODE_ROLE_LIST_VIEW = "P-ROLE-004";
    /** 角色 编辑权限 */
    public static final String CODE_ROLE_EDIT = "P-ROLE-002";
    /** 角色 查看权限 */
    public static final String CODE_ROLE_VIEW = "P-ROLE-001";
    /** 角色 删除权限 */
    public static final String CODE_ROLE_DEL = "P-ROLE-003";

    // --------------------- 角色管理 默认角色授权

    /** 默认角色授权-查看一览 */
    public static final String P_CODE_DEFASS_VIEW = "P-DEFASS-001";
    /** 默认角色授权-添加修改 */
    public static final String P_CODE_DEFASS_EDIT = "P-DEFASS-002";
    /** 默认角色授权-取消 */
    public static final String P_CODE_DEFASS_CANCEL = "P-DEFASS-003";

    // --------------------- 角色管理 特殊角色授权

    /** 特殊角色授权-查看一览 */
    public static final String P_CODE_UA_LIST_VIEW = "P-UAAS-001";
    /** 特殊角色授权-添加修改 */
    public static final String P_CODE_UA_EDIT = "P-UAAS-002";
    /** 特殊角色授权-取消 */
    public static final String P_CODE_UA_CANCEL = "P-UAAS-003";

    // --------------------- 角色管理 特殊角色授权

    /** BM-excel导出 */
    public static final String P_CODE_BM_EXCEL = "P-BM-001";
    /** BM-采购-一览查看 */
    public static final String P_CODE_BM_PRO_VIEW = "P-BM-010";
    /** BM-采购Add修改 */
    public static final String P_CODE_BM_PRO_EDIT = "P-BM-011";
    /** BM-采购确认驳回 */
    public static final String P_CODE_BM_PRO_AFFIRM = "P-BM-012";
    /** BM-采购驳回 不再使用该权限 该权限与确认合并 */
    public static final String P_CODE_BM_PRO_REJECT = "P-BM-013";
    /** BM-采购删除 */
    public static final String P_CODE_BM_PRO_DEL = "P-BM-014";
    /** BM-事业部长-查看一览 */
    public static final String P_CODE_BM_DIV_LEADER_VIEW = "P-BM-020";
    /** BM-事业部长-审核驳回 */
    public static final String P_CODE_BM_DIV_LEADER_CHECK = "P-BM-021";
    /** BM-事业部长-驳回 不在使用该权限 与事业部审核合并 */
    public static final String P_CODE_BM_DIV_LEADER_REJECT = "P-BM-022";
    /** BM-系统部-查看一览 */
    public static final String P_CODE_BM_SYS_VIEW = "P-BM-030";
    /** BM-系统部-Add */
    public static final String P_CODE_BM_SYS_ADD = "P-BM-031";
    /** BM-系统部审核驳回 */
    public static final String P_CODE_BM_SYS_CHECK = "P-BM-032";
    /** BM-系统部-删除 */
    public static final String P_CODE_BM_SYS_DEL = "P-BM-033";
    /** BM-系统部-过期BM批量删除 */
    public static final String P_CODE_BM_SYS_BATCH_REMOVE = "P-BM-034";
    /** BM-店铺-查看一览 */
    public static final String P_CODE_BM_STORE_VIEW = "P-BM-040";
    /** BM历史查询-一览权限 */
    public static final String P_CODE_BM_HIS_VIEW = "P-BM-HIS-010";
    /** BM历史查询-excel导出权限 */
    public static final String P_CODE_BM_HIS_EXCEL = "P-BM-HIS-020";


    /** Order Entry 查看权限 */
    public static final String CODE_SC_ORDER_VIEW = "SC-ORDER-001";
    /** Order Entry 编辑权限 */
    public static final String CODE_SC_ORDER_EDIT = "SC-ORDER-002";
    /** Order Entry 删除权限 */
    public static final String CODE_SC_ORDER_DEL = "SC-ORDER-003";
    /** Order Entry 列表查看权限 */
    public static final String CODE_SC_ORDER_LIST_VIEW = "SC-ORDER-004";
    /** Order Entry 导出权限 */
    public static final String CODE_SC_ORDER_EXPORT = "SC-ORDER-005";
    /** Order Entry 打印权限 */
    public static final String CODE_SC_ORDER_PRINT = "SC-ORDER-006";

    /** 收货登录(DC配送) 查看权限 */
    public static final String CODE_SC_RECEIPT_VIEW = "SC-RECEIPT-001";
    /** 收货登录(DC配送) 确定订货收货详细权限 */
    public static final String CODE_SC_RECEIPT_CONFIRM = "SC-RECEIPT-002";
    /** 收货登录(DC配送) Receiving权限 */
    public static final String CODE_SC_RECEIPT_RECEIVE = "SC-RECEIPT-003";
    /** 收货登录(DC配送) 列表查看权限 */
    public static final String CODE_SC_RECEIPT_LIST_VIEW = "SC-RECEIPT-004";
    /** 收货登录(DC配送) 打印权限 */
    public static final String CODE_SC_RECEIPT_PRINT = "SC-RECEIPT-005";
    /** 收货登录(DC配送) 导出权限 */
    public static final String CODE_SC_RECEIPT_EXPORT = "SC-RECEIPT-006";

    /** 新开店订货 查看权限 */
    public static final String CODE_SC_NS_ORDER_VIEW = "SC-NS-ORDER-001";
    /** 新开店订货 编辑权限 */
    public static final String CODE_SC_NS_ORDER_EDIT = "SC-NS-ORDER-002";
    /** 新开店订货 删除权限 */
    public static final String CODE_SC_NS_ORDER_DEL = "SC-NS-ORDER-003";
    /** 新开店订货 列表查看权限 */
    public static final String CODE_SC_NS_ORDER_LIST_VIEW = "SC-NS-ORDER-004";
    /** 新开店订货 导出权限 */
    public static final String CODE_SC_NS_ORDER_EXPORT = "SC-NS-ORDER-005";
    /** 新开店订货 打印权限 */
    public static final String CODE_SC_NS_ORDER_PRINT = "SC-NS-ORDER-006";

    /** 收货登录(自送供应商) 查看权限 */
    public static final String CODE_SC_V_RECEIPT_VIEW = "SC-V-RECEIPT-001";
    /** 收货登录(自送供应商) 确定订货收货详细权限 */
    public static final String CODE_SC_V_RECEIPT_CONFIRM = "SC-V-RECEIPT-002";
    /** 收货登录(自送供应商) Receiving权限 */
    public static final String CODE_SC_V_RECEIPT_RECEIVE = "SC-V-RECEIPT-003";
    /** 收货登录(自送供应商) 列表查看权限 */
    public static final String CODE_SC_V_RECEIPT_LIST_VIEW = "SC-V-RECEIPT-004";
    /** 收货登录(自送供应商) 打印权限 */
    public static final String CODE_SC_V_RECEIPT_PRINT = "SC-V-RECEIPT-005";
    /** 收货登录(自送供应商) 导出权限 */
    public static final String CODE_SC_V_RECEIPT_EXPORT = "SC-V-RECEIPT-006";

    /** 订单配送差异查询及调整 查看权限 */
    public static final String CODE_SC_DIFFERENCE_VIEW = "SC-DIFFERENCE-001";
    /** 订单配送差异查询及调整 编辑权限 */
    public static final String CODE_SC_DIFFERENCE_EDIT = "SC-DIFFERENCE-002";
    /** 订单配送差异查询及调整 新增权限 */
    public static final String CODE_SC_DIFFERENCE_ADD = "SC-DIFFERENCE-003";
    /** 订单配送差异查询及调整 列表查看权限 */
    public static final String CODE_SC_DIFFERENCE_LIST_VIEW = "SC-DIFFERENCE-004";
    /** 订单配送差异查询及调整 导出权限 */
    public static final String CODE_SC_DIFFERENCE_EXPORT = "SC-DIFFERENCE-005";
    /** 订单配送差异查询及调整 打印权限 */
    public static final String CODE_SC_DIFFERENCE_PRINT = "SC-DIFFERENCE-006";


    /** 发货单配送差异查询及调整 查看列表权限 */
    public static final String CODE_SC_SHOP_DIFFER_VIEW_LIST = "SC-SHOP-DIFFER-001";
    /** 发货单配送差异查询及调整 查看明细权限 */
    public static final String CODE_SC_SHOP_DIFFER_VIEW = "SC-SHOP-DIFFER-002";
    /** 发货单配送差异查询及调整 导出权限 */
    public static final String CODE_SC_SHOP_DIFFER_EXPORT = "SC-SHOP-DIFFER-003";

    /** Store Order and Receiving(From Vendor) 查看权限 */
    public static final String CODE_SC_DETAILS_VIEW = "SC-DETAILS-001";
    /** Store Order and Receiving(From Vendor) 编辑权限 */
    public static final String CODE_SC_DETAILS_EDIT = "SC-DETAILS-002";
    /** Store Order and Receiving(From Vendor) 新增权限 */
    public static final String CODE_SC_DETAILS_Add = "SC-DETAILS-003";
    /** Store Order and Receiving(From Vendor) 列表查看权限 */
    public static final String CODE_SC_DETAILS_LIST_VIEW = "SC-DETAILS-004";
    /** Store Order and Receiving(From Vendor) 导出权限 */
    public static final String CODE_SC_DETAILS_EXPORT = "SC-DETAILS-005";
    /** Store Order and Receiving(From Vendor) 查询审核记录权限 */
    public static final String CODE_SC_DETAILS_APPROVALRECORDS = "SC-DETAILS-006";



    /** Store Order and Receiving(From Vendor) 查看权限 */
    public static final String CODE_SC_ORDER_DC_VIEW = "SC-ORDER-DC-001";
    /** Store Order and Receiving(From Vendor) 新增权限 */
    public static final String CODE_SC_ORDER_DC_Add = "SC-ORDER-DC-002";
    /** Store Order and Receiving(From Vendor) 编辑权限 */
    public static final String CODE_SC_ORDER_DC_EDIT = "SC-ORDER-DC-003";
    /** Store Order and Receiving(From Vendor) 列表查看权限 */
    public static final String CODE_SC_ORDER_DC_LIST_VIEW = "SC-ORDER-DC-004";
    /** Store Order and Receiving(From Vendor) 导出权限 */
    public static final String CODE_SC_ORDER_DC_EXPORT = "SC-ORDER-DC-005";
    /** Store Order and Receiving(From Vendor) 查询审核记录权限 */
    public static final String CODE_SC_ORDER_DC_APPROVALRECORDS = "SC-ORDER-DC-006";

    /** 退货单登录（向DC） 查看权限 */
    public static final String CODE_SC_W_RETURN_VIEW = "SC-W-RETURN-001";
    /** 退货单登录（向DC） 编辑权限 */
    public static final String CODE_SC_W_RETURN_EDIT = "SC-W-RETURN-002";
    /** 退货单登录（向DC） 新增权限 */
    public static final String CODE_SC_W_RETURN_ADD= "SC-W-RETURN-003";
    /** 退货单登录（向DC） 列表查看权限 */
    public static final String CODE_SC_W_RETURN_LIST_VIEW = "SC-W-RETURN-004";
    /** 退货单登录（向DC） 导出权限 */
    public static final String CODE_SC_W_RETURN_EXPORT = "SC-W-RETURN-005";
    /** 退货单登录（向DC） 打印权限 */
    public static final String CODE_SC_W_RETURN_PRINT = "SC-W-RETURN-006";
    /** 退货单登录（向DC） 查看审核记录权限 */
    public static final String CODE_SC_W_RETURN_APPROVALRECORDS = "SC-W-RETURN-007";

    /** 退货单登录（向供应商） 查看权限 */
    public static final String CODE_SC_V_RETURN_VIEW = "SC-V-RETURN-001";
    /** 退货单登录（向供应商） 编辑权限 */
    public static final String CODE_SC_V_RETURN_EDIT = "SC-V-RETURN-002";
    /** 退货单登录（向供应商） 新增权限 */
    public static final String CODE_SC_V_RETURN_ADD = "SC-V-RETURN-003";
    /** 退货单登录（向供应商） 列表查看权限 */
    public static final String CODE_SC_V_RETURN_LIST_VIEW = "SC-V-RETURN-004";
    /** 退货单登录（向供应商） 导出权限 */
    public static final String CODE_SC_V_RETURN_EXPORT = "SC-V-RETURN-005";
    /** 退货单登录（向供应商） 打印权限 */
    public static final String CODE_SC_V_RETURN_PRINT = "SC-W-RETURN-006";
    /** 退货单登录（向供应商） 查看审核记录权限 */
    public static final String CODE_SC_V_RETURN_APPROVALRECORDS = "SC-W-RETURN-007";

    /** Store TransferOut Voucher Entry 新增权限 */
    public static final String CODE_SC_ST_TRANSFEROUT_ADD = "SC-ST-TRANSFER-001";
    /** Store TransferOut Voucher Entry 查看权限 */
    public static final String CODE_SC_ST_TRANSFEROUT_VIEW = "SC-ST-TRANSFER-002";
    /** Store TransferOut Voucher Entry 编辑权限 */
    public static final String CODE_SC_ST_TRANSFEROUT_EDIT = "SC-ST-TRANSFER-003";
    /** Store TransferOut Voucher Entry 列表查看权限 */
    public static final String CODE_SC_ST_TRANSFEROUT_LIST_VIEW = "SC-ST-TRANSFER-004";
    /** Store TransferOut Voucher Entry 审核记录查看权限 */
    public static final String CODE_SC_ST_TRANSFEROUT_APPROVALRECORDS = "SC-ST-TRANSFER-005";
    /** Store TransferOut Voucher Entry 导出权限 */
    public static final String CODE_SC_ST_TRANSFEROUT_EXPORT = "SC-ST-TRANSFER-006";

    /** Store TransferIn Voucher Entry 新增权限 */
    public static final String CODE_SC_ST_TRANSFERIN_ADD = "SC-ST-TRANSIN-001";
    /** Store TransferIn Voucher Entry 查看权限 */
    public static final String CODE_SC_ST_TRANSFERIN_VIEW = "SC-ST-TRANSIN-002";
    /** Store Transfer Voucher Entry 编辑权限 */
    public static final String CODE_SC_ST_TRANSFERIN_EDIT = "SC-ST-TRANSIN-003";
    /** Store Transfer Voucher Entry 列表查看权限 */
    public static final String CODE_SC_ST_TRANSFERIN_LIST_VIEW = "SC-ST-TRANSIN-004";
    /** Store Transfer Voucher Entry 审核记录查看权限 */
    public static final String CODE_SC_ST_TRANSFERIN_APPROVALRECORDS = "SC-ST-TRANSIN-005";
    /** Store Transfer Voucher Entry 导出权限 */
    public static final String CODE_SC_ST_TRANSFERIN_EXPORT = "SC-ST-TRANSIN-006";

    /** 库存调整 新增权限 */
    public static final String CODE_SC_ST_ADJUST_ADD = "SC-ST-ADJUST-001";
    /** 库存调整 查看权限 */
    public static final String CODE_SC_ST_ADJUST_VIEW = "SC-ST-ADJUST-002";
    /** 库存调整 编辑权限 */
    public static final String CODE_SC_ST_ADJUST_EDIT = "SC-ST-ADJUST-003";
    /** 库存调整 列表查看权限 */
    public static final String CODE_SC_ST_ADJUST_LIST_VIEW = "SC-ST-ADJUST-004";
    /** 库存调整 审核记录查看权限 */
    public static final String CODE_SC_ST_ADJUST_APPROVALRECORDS = "SC-ST-ADJUST-005";
    /** 库存调整 导出权限 */
    public static final String CODE_SC_ST_ADJUST_EXPORT = "SC-ST-ADJUST-006";

    /** 库存报废 新增权限 */
    public static final String CODE_SC_ST_SCRAP_ADD = "SC-ST-SCRAP-001";
    /** 库存报废 查看权限 */
    public static final String CODE_SC_ST_SCRAP_VIEW = "SC-ST-SCRAP-002";
    /** 库存报废 编辑权限 */
    public static final String CODE_SC_ST_SCRAP_EDIT = "SC-ST-SCRAP-003";
    /** 库存报废 列表查看权限 */
    public static final String CODE_SC_ST_SCRAP_LIST_VIEW = "SC-ST-SCRAP-004";
    /** 库存报废 审核记录查看权限 */
    public static final String CODE_SC_ST_SCRAP_APPROVALRECORDS = "SC-ST-SCRAP-005";
    /** 库存报废 导出权限 */
    public static final String CODE_SC_ST_SCRAP_EXPORT = "SC-ST-SCRAP-006";

    /** Stocktaking Entry（盘点结果录入及查看） 查看权限 */
    public static final String CODE_SC_PD_ENTRY_VIEW = "SC-PD-ENTRY-001";
    /** Stocktaking Entry（盘点结果录入及查看） 编辑权限 */
    public static final String CODE_SC_PD_ENTRY_EDIT = "SC-PD-ENTRY-002";
    /** Stocktaking Entry（盘点结果录入及查看） 导出权限 */
    public static final String CODE_SC_PD_ENTRY_EXPORT = "SC-PD-ENTRY-003";
    /** Stocktaking Entry（盘点结果录入及查看） 导入权限 */
    public static final String CODE_SC_PD_ENTRY_IMOIRT = "SC-PD-ENTRY-004";
    /** Stocktaking Entry（盘点结果录入及查看） 打印权限 */
    public static final String CODE_SC_PD_ENTRY_PRINT = "SC-PD-ENTRY-005";
    /** Stocktaking Entry（盘点结果录入及查看） 列表查看权限 */
    public static final String CODE_SC_PD_ENTRY_LIST_VIEW = "SC-PD-ENTRY-006";
    /** Stocktaking Entry（盘点结果录入及查看） 审核记录查看权限 */
    public static final String CODE_SC_PD_ENTRY_APPROVALRECORDS = "SC-PD-ENTRY-007";


    /** Stocktaking Recognition Query（盘点认列表） 查看权限 */
    public static final String CODE_SC_PD_PROCESS_VIEW = "SC-PD-PROCESS-001";
    /** Stocktaking Recognition Query（盘点认列表） 打印权限 */
    public static final String CODE_SC_PD_PROCESS_PRINT= "SC-PD-PROCESS-002";
    /** Stocktaking Recognition Query（盘点认列表） 导出权限 */
    public static final String CODE_SC_PD_PROCESS_EXPORT = "SC-PD-PROCESS-003";
    /** Stocktaking Recognition Query（盘点认列表） 列表查看权限 */
    public static final String CODE_SC_PD_PROCESS_LIST_VIEW = "SC-PD-PROCESS-004";
    /** Stocktaking Recognition Query（盘点认列表） 查看审核记录权限 */
    public static final String CODE_SC_PD_PROCESS_APPROVALRECORDS = "SC-PD-PROCESS-005";

    /** Unpacking sales 列表查看权限 */
    public static final String CODE_SC_XS_USALE_LIST_VIEW = "SC-XS-USALE-001";
    /** Unpacking sales 查看权限 */
    public static final String CODE_SC_XS_USALE_VIEW = "SC-XS-USALE-002";
    /** Unpacking sales 编辑权限 */
    public static final String CODE_SC_XS_USALE_EDIT = "SC-XS-USALE-003";
    /** Unpacking sales 删除权限 */
    public static final String CODE_SC_XS_USALE_DEL = "SC-XS-USALE-004";
    /** Unpacking sales 新增权限 */
    public static final String CODE_SC_XS_USALE_ADD = "SC-XS-USALE-005";
    /** Unpacking sales 导出权限 */
    public static final String CODE_SC_XS_USALE_EXPORT = "SC-XS-USALE-006";

    /** Formula sales order 查看权限 */
    public static final String CODE_SC_XS_ORDER_VIEW = "SC-XS-ORDER-001";
    /** Formula sales order 列表查看权限 */
    public static final String CODE_SC_XS_ORDER_LIST_VIEW = "SC-XS-ORDER-002";
    /** Formula sales order 导出权限 */
    public static final String CODE_SC_XS_ORDER_EXPORT = "SC-XS-ORDER-003";

    /** BOM Item Sales Report 列表查看权限 */
    public static final String CODE_SC_RF_BOM_LIST_VIEW = "SC-RF-BOM-001";
    /** BOM Item Sales Report 打印权限 */
    public static final String CODE_SC_RF_BOM_PRINT = "SC-RF-BOM-002";
    /** BOM Item Sales Report 导出权限 */
    public static final String CODE_SC_RF_BOM_EXPORT = "SC-RF-BOM-005";

    /** Price Info(by Date) Query 列表查看权限 */
    public static final String CODE_SC_ZD_PBD_LIST_VIEW = "SC-ZD-PBD-001";
    /** Price Info(by Date) Query 导出权限 */
    public static final String CODE_SC_ZD_PBD_EXPORT = "SC-ZD-PBD-002";

    /** Expenditure Entry 新增权限 */
    public static final String CODE_SC_JF_ENTRY_ADD = "SC-JF-ENTRY-001";

    /** Expenditure QUERY 查看权限 */
    public static final String CODE_SC_JF_QUERY_VIEW = "SC-JF-QUERY-001";
    /** Expenditure QUERY 编辑权限 */
    public static final String CODE_SC_JF_QUERY_EDIT = "SC-JF-QUERY-002";
    /** Expenditure QUERY 删除权限 */
    public static final String CODE_SC_JF_QUERY_DEL = "SC-JF-QUERY-003";
    /** Expenditure QUERY 新增权限 */
    public static final String CODE_SC_JF_QUERY_ADD = "SC-JF-QUERY-004";
    /** Expenditure QUERY 列表查看权限 */
    public static final String CODE_SC_JF_QUERY_LIST_VIEW = "SC-JF-QUERY-005";
    /** Expenditure QUERY 导出权限 */
    public static final String CODE_SC_JF_QUERY_EXPORT = "SC-JF-QUERY-006";

    /** MM Promotion Query 查看权限 */
    public static final String CODE_SC_ZD_BPQ_VIEW = "SC-ZD-BPQ-001";
    /** MM Promotion Query 列表查看权限 */
    public static final String CODE_SC_ZD_BPQ_LIST_VIEW = "SC-ZD-BPQ-002";
    /** MM Promotion Query 导出权限 */
    public static final String CODE_SC_ZD_BPQ_EXPORT = "SC-ZD-BPQ-003";

    /** Inventory Voucher Query 查看权限 */
    public static final String CODE_SC_ST_KCCPYL_VIEW = "SC-ST-KCCPYL-001";
    /** Inventory Voucher Query 编辑权限 */
    public static final String CODE_SC_ST_KCCPYL_EDIT = "SC-ST-KCCPYL-002";
    /** Inventory Voucher Query 列表查看权限 */
    public static final String CODE_SC_ST_KCCPYL_LIST_VIEW = "SC-ST-KCCPYL-003";
    /** Inventory Voucher Query 导出权限 */
    public static final String CODE_SC_ST_KCCPYL_EXPORT = "SC-ST-KCCPYL-004";

    /** Store Transfer Correction Entry 新增权限 */
    public static final String CODE_SC_TRANSFER_MOD_ADD = "SC-TRANSFER-MOD-001";
    /** Store Transfer Correction Entry 查看权限 */
    public static final String CODE_SC_TRANSFER_MOD_VIEW = "SC-TRANSFER-MOD-002";
    /** Store Transfer Correction Entry 编辑权限 */
    public static final String CODE_SC_TRANSFER_MOD_EDIT = "SC-TRANSFER-MOD-003";
    /** Store Transfer Correction Entry 列表查看权限 */
    public static final String CODE_SC_TRANSFER_MOD_LIST_VIEW = "SC-TRANSFER-MOD-004";

    /** Failed Order Query 列表查看权限 */
    public static final String CODE_SC_OD_FAILED_LIST_VIEW = "SC-OD-FAILED-001";
    /** Failed Order Query 导出权限 */
    public static final String CODE_SC_OD_FAILED_EXPORT = "SC-OD-FAILED-002";

    /** Failed order of the day Query 列表查看权限 */
    public static final String CODE_SC_OD_FAILED_DAY_VIEWLIST = "SC-DAY-OD-FAILED-001";
    /** Failed order of the day Query 导出权限 */
    public static final String CODE_SC_OD_FAILED_DAY_EXPORT = "SC-DAY-OD-FAILED-002";
    /** Failed order of the day Query 查看明细权限 */
    public static final String CODE_SC_OD_FAILED_DAY_VIEW = "SC-DAY-OD-FAILED-003";

    /** Item Master Query 查看权限 */
    public static final String CODE_SC_ZD_IMQ_VIEW = "SC-ZD-IMQ-001";
    /** Item Master Query 列表查看权限 */
    public static final String CODE_SC_ZD_IMQ_LIST_VIEW = "SC-ZD-IMQ-002";
    /** Item Master Query 导出权限 */
    public static final String CODE_SC_ZD_IMQ_EXPORT = "SC-ZD-IMQ-003";

    /** Store Master Query 查看权限 */
    public static final String CODE_SC_ZD_SMQ_VIEW = "SC-ZD-SMQ-001";
    /** Store Master Query 列表查看权限 */
    public static final String CODE_SC_ZD_SMQ_LIST_VIEW = "SC-ZD-SMQ-002";
    /** Store Master Query 导出权限 */
    public static final String CODE_SC_ZD_SMQ_EXPORT = "SC-ZD-SMQ-003";

    /** Vendor Master Query 查看权限 */
    public static final String CODE_SC_ZD_VMQ_VIEW = "SC-ZD-VMQ-001";
    /** Vendor Master Query 列表查看权限 */
    public static final String CODE_SC_ZD_VMQ_LIST_VIEW = "SC-ZD-VMQ-002";
    /** Vendor Master Query 导出权限 */
    public static final String CODE_SC_ZD_VMQ_EXPORT = "SC-ZD-VMQ-003";

    /** Group Sale Item Sales Report 列表查看权限 */
    public static final String CODE_SC_RF_GROUP_LIST_VIEW = "SC-RF-GROUP-001";
    /** Group Sale Item Sales Report 打印权限 */
    public static final String CODE_SC_RF_GROUP_PRINT = "SC-RF-GROUP-002";
    /** Group Sale Item Sales Report 导出权限 */
    public static final String CODE_SC_RF_GROUP_EXPORT = "SC-RF-GROUP-005";

    /** Cashier Management 新增权限 */
    public static final String CODE_SC_CASHIER_ADD = "SC-CASHIER-001";
    /** Cashier Management Modify Privilege权限 */
    public static final String CODE_SC_CASHIER_EDIT = "SC-CASHIER-002";
    /** Cashier Management Eliminate权限 */
    public static final String CODE_SC_CASHIER_ELIMINATE = "SC-CASHIER-003";
    /** Cashier Management Reactivate权限 */
    public static final String CODE_SC_CASHIER_REACTIVATE = "SC-CASHIER-004";
    /** Cashier Management Restore Initial Password权限 */
    public static final String CODE_SC_CASHIER_INIT_PASSWORD = "SC-CASHIER-005";
    /** Cashier Management Reset Password权限 */
    public static final String CODE_SC_CASHIER_RESET_PASSWORD = "SC-CASHIER-006";
    /** Cashier Management 列表查看权限 */
    public static final String CODE_SC_CASHIER_LIST_VIEW = "SC-CASHIER-007";
    /** Cashier Management 删除权限 */
    public static final String CODE_SC_CASHIER_LIST_DELETE = "SC-CASHIER-008";

       /** Cashier Management 导出权限 */
    public static final String CODE_SC_CASHIER_EXPORT = "SC-CASHIER-009";


    /** Electronic Receipts Query 列表查看权限 */
    public static final String CODE_SC_RECEIPTS_LIST_VIEW = "SC-RECEIPTS-001";
    /** Electronic Receipts Query 导出权限 */
    public static final String CODE_SC_RECEIPTS_EXPORT = "SC-RECEIPTS-002";





    /** Cashier Details Query 列表查看权限 */
    public static final String CODE_SC_CS_DETAIL_LIST_VIEW = "SC-CS-DETAIL-001";
    /** Cashier Details Query 导出权限 */
    public static final String CODE_SC_CS_DETAIL_EXPORT = "SC-CS-DETAIL-002";

    /** Receiving and Return Voucher Modification Query 查看权限 */
    public static final String CODE_SC_RR_MODIF_VIEW = "SC-RR-MODIF-001";
    /** Receiving and Return Voucher Modification Query 编辑权限 */
    public static final String CODE_SC_RR_MODIF_EDIT = "SC-RR-MODIF-002";
    /** Receiving and Return Voucher Modification Query 列表查看权限 */
    public static final String CODE_SC_RR_MODIF_LIST_VIEW = "SC-RR-MODIF-003";
    /** Receiving and Return Voucher Modification Query 导出权限 */
    public static final String CODE_SC_RR_MODIF_EXPORT = "SC-RR-MODIF-004";

    /** Receiving and Return Voucher Query 查看权限 */
    public static final String CODE_SC_RR_QUERY_VIEW = "SC-RR-QUERY-001";
    /** Receiving and Return Voucher Confirm Order Details and Receiving权限 */
    public static final String CODE_SC_RR_QUERY_CONFIRM = "SC-RR-QUERY-002";
    /** Receiving and Return Voucher Query Quick Receiving权限 */
    public static final String CODE_SC_RR_QUERY_RECEIVE = "SC-RR-QUERY-003";
    /** Receiving and Return Voucher Query 列表查看权限 */
    public static final String CODE_SC_RR_QUERY_LIST_VIEW = "SC-RR-QUERY-004";
    /** Receiving and Return Voucher Query 导出权限 */
    public static final String CODE_SC_RR_QUERY_EXPORT = "SC-RR-QUERY-005";
    /** Receiving and Return Voucher Query 打印权限 */
    public static final String CODE_SC_RR_QUERY_PRINT = "SC-RR-QUERY-006";

    /** 销售对帐与财务 列表查看权限 */
    public static final String CODE_SC_SL_CF_LIST_VIEW = "SC-SL-CF-001";
    /** 销售对帐与财务 导出权限 */
    public static final String CODE_SC_SL_CF_EXPORT = "SC-SL-CF-002";

    /** Stocktaking Query 初盘完成权限 */
    public static final String CODE_SC_PD_QUERY_STOCK_TAKE_COMPLETE = "SC-PD-QUERY-001";
    /** Stocktaking Query 盘点差异认列权限 */
    public static final String CODE_SC_PD_QUERY_DIFFERENCE_CONFIRM = "SC-PD-QUERY-002";
    /** Stocktaking Query 查看盘点表权限 */
    public static final String CODE_SC_PD_QUERY_VIEW = "SC-PD-QUERY-003";
    /** Stocktaking Query 初盘量录入权限 */
    public static final String CODE_SC_PD_QUERY_FIRST_STOCK = "SC-PD-QUERY-004";
    /** Stocktaking Query 复盘量录入权限 */
    public static final String CODE_SC_PD_QUERY_LAST_STOCK = "SC-PD-QUERY-005";
    /** Stocktaking Query 列表查看权限 */
    public static final String CODE_SC_PD_QUERY_LIST_VIEW = "SC-PD-QUERY-006";

    /** 营业日报 列表查看权限 */
    public static final String CODE_SC_BD_LIST_VIEW = "SC-BD-001";
    /** 营业日报 打印权限 */
    public static final String CODE_SC_BD_PRINT = "SC-BD-002";
    /** 营业日报 导出权限 */
    public static final String CODE_SC_BD_EXPORT = "SC-BD-003";

    /** Real-time Inventory Query 查看一览权限 */
    public static final String CODE_SC_RF_INVENTORY_LIST_VIEW = "SC-RF-INVENTORY-001";
    /** Real-time Inventory Query 打印权限 */
    public static final String CODE_SC_RF_INVENTORY_PRINT = "SC-RF-INVENTORY-002";
    /** Real-time Inventory Query 导出权限 */
    public static final String CODE_SC_RF_INVENTORY_EXPORT = "SC-RF-INVENTORY-003";

    /** 菜单管理 查看权限 */
    public static final String CODE_P_MENU_VIEW = "P-MENU-001";
    /** 菜单管理 编辑权限 */
    public static final String CODE_P_MENU_EDIT = "P-MENU-002";
    /** 菜单管理 删除权限 */
    public static final String CODE_P_MENU_DEL = "P-MENU-003";
    /** 菜单管理 列表查看权限 */
    public static final String CODE_P_MENU_LIST_VIEW = "P-MENU-004";
    /** 菜单管理 导出权限 */
    public static final String CODE_P_MENU_EXPORT = "P-MENU-005";

    /** 门店陈列导入有查看（门店POG导入） 列表查看权限 */
    public static final String CODE_SC_S_I_R_LIST_VIEW = "SC-S-I-R-001";
    /** 门店陈列导入有查看（门店POG导入） 上传权限 */
    public static final String CODE_SC_S_I_R_UPLOAD = "SC-S-I-R-002";
    /** 门店陈列导入有查看（门店POG导入） 导出权限 */
    public static final String CODE_SC_S_I_R_EXPORT = "SC-S-I-R-003";

    /** 收银员收款金额登录 编辑权限 */
    public static final String CODE_SC_CA_EDIT = "SC-CA-001";
    /** 收银员收款金额登录 列表查看权限 */
    public static final String CODE_SC_CA_LIST_VIEW = "SC-CA-002";

    /** 银行缴款 新增权限 */
    public static final String CODE_SC_PI_VIEW = "SC-PI-001";
    /** 银行缴款 编辑权限 */
    public static final String CODE_SC_PI_EDIT = "SC-PI-002";
    /** 银行缴款 删除权限 */
    public static final String CODE_SC_PI_DEL = "SC-PI-003";
    /** 银行缴款 列表查看权限 */
    public static final String CODE_SC_PI_LIST_VIEW = "SC-PI-004";
    /** 银行缴款 导出权限 */
    public static final String CODE_SC_PI_EXPORT = "SC-PI-005";

    /**验收 退货登录(自送供应商) 查看权限 */
    public static final String CODE_SC_RV_VIEW = "SC-RV-001";
    /**验收 退货登录(自送供应商) 编辑权限 */
    public static final String CODE_SC_RV_EDIT = "SC-RV-002";
    /**验收 退货登录(自送供应商) 列表查看权限 */
    public static final String CODE_SC_RV_LIST_VIEW = "SC-RV-003";
    /**验收 退货登录(自送供应商) 打印权限 */
    public static final String CODE_SC_RV_PRINT = "SC-RV-004";
    /**验收 退货登录(自送供应商) 导出权限 */
    public static final String CODE_SC_RV_EXPORT = "SC-RV-005";

    /**验收 退货登录(退物流) 查看权限 */
    public static final String CODE_SC_RW_VIEW = "SC-RW-001";
    /**验收 退货登录(退物流) 编辑权限 */
    public static final String CODE_SC_RW_EDIT = "SC-RW-002";
    /**验收 退货登录(退物流) 列表查看权限 */
    public static final String CODE_SC_RW_LIST_VIEW = "SC-RW-003";
    /**验收 退货登录(退物流) 打印权限 */
    public static final String CODE_SC_RW_PRINT = "SC-RW-004";
    /**验收 退货登录(退物流) 导出权限 */
    public static final String CODE_SC_RW_EXPORT = "SC-RW-005";

    /** 紧急变价申请登录 编辑权限 */
    public static final String CODE_SC_PRI_CAG_EDIT = "SC-PRI-CAG-001";

    /** 紧急变价一览 列表查看权限 */
    public static final String CODE_SC_PRI_CHANGE_LIST_VIEW = "SC-PRI-CHANGE-001";
    /** 紧急变价一览 导出权限 */
    public static final String CODE_SC_PRI_CHANGE_EXPORT = "SC-PRI-CHANGE-005";

    /** 职务管理 列表查看权限 */
    public static final String CODE_SC_OFF_MANAGE_LIST_VIEW = "SC-OFF-MANAGE-001";

    /** 门店销售日报 查看权限 */
    public static final String CODE_SC_STORE_DAILY_VIEW = "SC-STORE-DAILY-001";
    /** 门店销售日报 编辑权限 */
    public static final String CODE_SC_STORE_DAILY_EDIT = "SC-STORE-DAILY-002";
    /** 门店销售日报 删除权限 */
    public static final String CODE_SC_STORE_DAILY_DEL = "SC-STORE-DAILY-003";
    /** 门店销售日报 列表查看权限 */
    public static final String CODE_SC_STORE_DAILY_LIST_VIEW = "SC-STORE-DAILY-004";
    /** 门店销售日报 导出权限 */
    public static final String CODE_SC_STORE_DAILY_EXPORT = "SC-STORE-DAILY-005";

    /** 门店原因 新增权限 */
    public static final String CODE_SC_SR_ADD = "SC-SR-001";
    /** 门店原因 编辑权限 */
    public static final String CODE_SC_SR_EDIT = "SC-SR-002";
    /** 门店原因 删除权限 */
    public static final String CODE_SC_SR_DEL = "SC-SR-003";
    /** 门店原因 列表查看权限 */
    public static final String CODE_SC_SR_LIST_VIEW = "SC-SR-004";
    /** 门店原因 导出权限 */
    public static final String CODE_SC_SR_EXPORT = "SC-SR-005";

    /** POS支付方式维护 查看权限 */
    public static final String CODE_SC_PM_VIEW = "SC-PM-001";
    /** POS支付方式维护 编辑权限 */
    public static final String CODE_SC_PM_ADD = "SC-PM-002";
    /** POS支付方式维护 删除权限 */
    public static final String CODE_SC_PM_EDIT = "SC-PM-003";
    /** POS支付方式维护 列表查看权限 */
    public static final String CODE_SC_PM_LIST_VIEW = "SC-PM-004";
    /** POS支付方式维护 导出权限 */
    public static final String CODE_SC_PM_EXPORT = "SC-PM-005";

    /** Stocktaking Plan Setting（盘点计划录入及查看） 新增权限 */
    public static final String CODE_SC_PD_SETTING_ADD = "SC-PD-SETTING-001";
    /** Stocktaking Plan Setting（盘点计划录入及查看） 编辑权限 */
    public static final String CODE_SC_PD_SETTING_EDIT = "SC-PD-SETTING-002";
    /** Stocktaking Plan Setting（盘点计划录入及查看） 查看权限 */
    public static final String CODE_SC_PD_SETTING_VIEW = "SC-PD-SETTING-003";
    /** Stocktaking Plan Setting（盘点计划录入及查看） 打印权限 */
    public static final String CODE_SC_PD_SETTING_PRINT = "SC-PD-SETTING-004";
    /** Stocktaking Plan Setting（盘点计划录入及查看） 导出商品信息权限 */
    public static final String CODE_SC_PD_SETTING_EXPORT_BASIC = "SC-PD-SETTING-005";
    /** Stocktaking Plan Setting（盘点计划录入及查看） 导出商品信息权限 */
    public static final String CODE_SC_PD_SETTING_EXPORT_ITEM = "SC-PD-SETTING-006";
    /** Stocktaking Plan Setting（盘点计划录入及查看） 列表查看权限 */
    public static final String CODE_SC_PD_SETTING_LIST_VIEW = "SC-PD-SETTING-007";

    /** 组织架构 列表查看权限 */
    public static final String CODE_SC_OR_STRUCT_LIST_VIEW = "SC-OR-STRUCT-001";

    /** 员工资料维护 新增权限 */
    public static final String CODE_SC_EM_INFOR_ADD= "SC-EM-INFOR-001";
    /** 员工资料维护 重置密码权限 */
    public static final String CODE_SC_EM_INFOR_RESET = "SC-EM-INFOR-002";
    /** 员工资料维护 修改权限 */
    public static final String CODE_SC_EM_INFOR_EDIT = "SC-EM-INFOR-003";
    /** 员工资料维护 查看权限 */
    public static final String CODE_SC_EM_INFOR_VIEW = "SC-EM-INFOR-004";
    /** 员工资料维护 删除权限 */
    public static final String CODE_SC_EM_INFOR_DEL = "SC-EM-INFOR-005";
    /** 员工资料维护 列表查看权限 */
    public static final String CODE_SC_EM_INFOR_LIST_VIEW = "SC-EM-INFOR-006";

    /** 通知查看及回复 查看权限 */
    public static final String CODE_SC_INF_REPLY_VIEW = "SC-INF-REPLY-001";
    /** 通知查看及回复 回复权限 */
    public static final String CODE_SC_INF_REPLY_REPLY = "SC-INF-REPLY-002";
    /** 通知查看及回复 列表查看权限 */
    public static final String CODE_SC_INF_REPLY_LIST_VIEW = "SC-INF-REPLY-003";
    /** 通知查看及回复 导出权限 */
    public static final String CODE_SC_INF_REPLY_EXPORT = "SC-INF-REPLY-004";

    /** 通报管理 查看权限 */
    public static final String CODE_SC_INF_HQ_VIEW = "SC-INF-HQ-001";
    /** 通报管理 新增权限 */
    public static final String CODE_SC_INF_HQ_ADD = "SC-INF-HQ-002";
    /** 通报管理 列表查看权限 */
    public static final String CODE_SC_INF_HQ_LIST_VIEW = "SC-INF-HQ-003";
    /** 通报管理 导出权限 */
    public static final String CODE_SC_INF_HQ_EXPORT = "SC-INF-HQ-005";

    /** 直送供应商订单查询 列表查看权限 */
    public static final String CODE_SC_DIRECR_ORDER_LIST_VIEW = "SC-DIRECR-ORDER-001";
    /** 直送供应商订单查询 打印权限 */
    public static final String CODE_SC_DIRECR_ORDER_PRINT = "SC-DIRECR-ORDER-002";
    /** 直送供应商订单查询 导出权限 */
    public static final String CODE_SC_DIRECR_ORDER_EXPORT = "SC-DIRECR-ORDER-003";

    /** DC订单查询 列表查看权限 */
    public static final String CODE_SC_ORDER_CD_LIST_VIEW = "SC-ORDER-CD-001";
    /** DC订单查询 打印权限 */
    public static final String CODE_SC_ORDER_CD_PRINT = "SC-ORDER-CD-002";
    /** DC订单查询 导出权限 */
    public static final String CODE_SC_ORDER_CD_EXPORT = "SC-ORDER-CD-003";

    /** 通报日志 列表查看权限 */
    public static final String CODE_SC_INF_LOG_VIEW = "SC-INF-LOG-001";

    /** 我的审核任务 审核权限 */
    public static final String CODE_SC_INF_AUDIT_AUDIT = "SC-INF-AUDIT-001";
    /** 我的审核任务 审核记录查看权限 */
    public static final String CODE_SC_INF_AUDIT_VIEW = "SC-INF-AUDIT-002";
    /** 我的审核任务 列表查看权限 */
    public static final String CODE_SC_INF_AUDIT_LIST_VIEW = "SC-INF-AUDIT-003";
    /** 我的审核任务 导出权限 */
    public static final String CODE_SC_INF_AUDIT_EXPORT = "SC-INF-AUDIT-004";

    /** 拒绝的审核任务 审核记录查看权限 */
    public static final String CODE_SC_INF_REJECTED_VIEW = "SC-INF-REJECTED-001";
    /** 拒绝的审核任务 列表查看权限 */
    public static final String CODE_SC_INF_REJECTED_LIST_VIEW = "SC-INF-REJECTED-002";
    /** 拒绝的审核任务 导出权限 */
    public static final String CODE_SC_INF_REJECTED_EXPORT = "SC-INF-AUDIT-004";

    /** 我提交的单据 撤回权限 */
    public static final String CODE_SC_INF_DOCUMENT_WITHDRAW = "SC-INF-DOCUMENT-001";
    /** 我提交的单据 审核记录查看权限 */
    public static final String CODE_SC_INF_DOCUMENT_VIEW = "SC-INF-DOCUMENT-002";
    /** 我提交的单据 列表查看权限 */
    public static final String CODE_SC_INF_DOCUMENT_LIST_VIEW = "SC-INF-DOCUMENT-003";
    /** 我提交的单据 导出权限 */
    public static final String CODE_SC_INF_DOCUMENT_EXPORT = "SC-INF-DOCUMENT-004";

    /** 我的消息 列表查看权限 */
    public static final String CODE_SC_INF_MESSAGE_LIST_VIEW = "SC-INF-MESSAGE-001";
    /** 我的消息 导出权限 */
    public static final String CODE_SC_INF_MESSAGE_EXPORT = "SC-INF-MESSAGE-002";

    /** 费用录入 新增权限 */
    public static final String CODE_SC_COST_ENTRY_ADD = "SC-COST-ENTRY-001";
    /** 费用录入 编辑权限 */
    public static final String CODE_SC_COST_ENTRY_EDIT = "SC-COST-ENTRY-002";
    /** 费用录入 查看权限 */
    public static final String CODE_SC_COST_ENTRY_VIEW = "SC-COST-ENTRY-003";
    /** 费用录入 Export StocktakeFiles权限 */
    public static final String CODE_SC_COST_ENTRY_EXPORT = "SC-COST-ENTRY-004";
    /** 费用录入 Import Stocktake权限 */
    public static final String CODE_SC_COST_ENTRY_IMPROT = "SC-COST-ENTRY-005";
    /** 费用录入 Print Actual Stocktake Files权限 */
    public static final String CODE_SC_COST_ENTRY_PRINT = "SC-COST-ENTRY-006";
    /** 费用录入 列表查看权限 */
    public static final String CODE_SC_COST_ENTRY_LIST_VIEW = "SC-COST-ENTRY-007";

    /** 新品信息 列表查看权限 */
    public static final String CODE_SC_NEWPRODUCR_LIST_VIEW = "SC-NEWPRODUCR-001";

    /** 促销信息 查看权限 */
    public static final String CODE_SC_PROMOTION_VIEW = "SC-PROMOTION-001";
    /** 促销信息 列表查看权限 */
    public static final String CODE_SC_PROMOTION_LIST_VIEW = "SC-PROMOTION-002";
    /** 促销信息 导出权限 */
    public static final String CODE_SC_PROMOTION_EXPORT = "SC-PROMOTION-003";

    /** FS库存录入 新增权限 */
    public static final String CODE_SC_FS_ENTRY_ADD = "SC-FS-ENTRY-001";
    /** FS库存录入 编辑权限 */
    public static final String CODE_SC_FS_ENTRY_EDIT = "SC-FS-ENTRY-002";
    /** FS库存录入 查看权限 */
    public static final String CODE_SC_FS_ENTRY_VIEW = "SC-FS-ENTRY-003";
    /** FS库存录入 Export StocktakeFiles权限 */
    public static final String CODE_SC_FS_ENTRY_EXPORT = "SC-FS-ENTRY-004";
    /** FS库存录入 Import Stocktake权限 */
    public static final String CODE_SC_FS_ENTRY_IMPROT = "SC-FS-ENTRY-005";
    /** FS库存录入 Print Actual Stocktake Files权限 */
    public static final String CODE_SC_FS_ENTRY_PRINT = "SC-FS-ENTRY-006";
    /** FS库存录入 列表查看权限 */
    public static final String CODE_SC_FS_ENTRY_LIST_VIEW = "SC-FS-ENTRY-007";
    /** FS库存录入 列表查看权限 */
    public static final String CODE_SC_FS_ENTRY_APPROVALRECORDS = "SC-FS-ENTRY-008";

    /** 原材料录入 新增权限 */
    public static final String CODE_SC_MATEL_ENTRY_ADD = "SC-MATEL-ENTRY-001";
    /** 原材料录入 编辑权限 */
    public static final String CODE_SC_MATEL_ENTRY_EDIT = "SC-MATEL-ENTRY-002";
    /** 原材料录入 查看权限 */
    public static final String CODE_SC_MATEL_ENTRY_VIEW = "SC-MATEL-ENTRY-003";
    /** 原材料录入 Export StocktakeFiles权限 */
    public static final String CODE_SC_MATEL_ENTRY_EXPORT = "SC-MATEL-ENTRY-004";
    /** 原材料录入 Import Stocktake权限 */
    public static final String CODE_SC_MATEL_ENTRY_IMPROT = "SC-MATEL-ENTRY-005";
    /** 原材料录入 Print Actual Stocktake Files权限 */
    public static final String CODE_SC_MATEL_ENTRY_PRINT = "SC-MATEL-ENTRY-006";
    /** 原材料录入 列表查看权限 */
    public static final String CODE_SC_MATEL_ENTRY_LIST_VIEW = "SC-MATEL-ENTRY-007";
    /** 原材料录入 审核记录查看权限 */
    public static final String CODE_SC_MATEL_ENTRY_APPROVALRECORDS = "SC-MATEL-ENTRY-008";


    /** 销售日报new 查看一览权限 */
    public static final String CODE_SC_STORE_DAY_LIST_VIEW = "SC-STORE-DAY-001";
    /** 销售日报new 打印权限 */
    public static final String CODE_SC_STORE_DAY_PRINT = "SC-STORE-DAY-002";
    /** 销售日报new 导出权限 */
    public static final String CODE_SC_STORE_DAY_EXPORT = "SC-STORE-DAY-003";

    /** 产品分类销售日报 列表查看权限 */
    public static final String CODE_SC_STORE_SALE_LIST_VIEW = "SC-STORE-SALE-001";
    /** 产品分类销售日报 打印权限 */
    public static final String CODE_SC_STORE_SALE_PRINT = "SC-STORE-SALE-002";
    /** 产品分类销售日报 导出权限 */
    public static final String CODE_SC_STORE_SALE_EXPORT = "SC-STORE-SALE-003";
    /** 服务类型销售日报 导出权限 */
    public static final String CODE_SC_SERVICE_DAILY_EXPORT = "SC-SERVE-DAILY-001";
    public static final String CODE_SC_SERVICE_DAILY_LIST_VIEW = "SC-SERVE-DAILY-002";

    /** 门店商品收货日报(Form 供应商) 列表查看权限 */
    public static final String CODE_SC_VENDOR_DAILY_LIST_VIEW = "SC-VENDOR-DAILY-001";
    /** 门店商品收货日报(Form 供应商) 打印权限 */
    public static final String CODE_SC_VENDOR_DAILY_PRINT = "SC-VENDOR-DAILY-002";
    /** 门店商品收货日报(Form 供应商) 导出权限 */
    public static final String CODE_SC_VENDOR_DAILY_EXPORT = "SC-VENDOR-DAILY-003";

    /** 门店商品收货日报(Form DC) 列表查看权限 */
    public static final String CODE_SC_DC_DAILY_LIST_VIEW = "SC-DC-DAILY-001";
    /** 门店商品收货日报(Form DC) 打印权限 */
    public static final String CODE_SC_DC_DAILY_PRINT = "SC-DC-DAILY-002";
    /** 门店商品收货日报(Form DC) 导出权限 */
    public static final String CODE_SC_DC_DAILY_EXPORT = "SC-DC-DAILY-003";

    /** 门店商品退货日报(Form 供应商) 列表查看权限 */
    public static final String CODE_SC_RETURN_VENDOR_DAILY_LIST_VIEW = "SC-RETURN-DAILY-001";
    /** 门店商品退货日报(Form 供应商) 打印权限 */
    public static final String CODE_SC_RETURN_VENDOR_DAILY_PRINT = "SC-RETURN-DAILY-002";
    /** 门店商品退货日报(Form 供应商) 导出权限 */
    public static final String CODE_SC_RETURN_VENDOR_DAILY_EXPORT = "SC-RETURN-DAILY-003";

    /** 门店商品退货日报(Form 供应商) 列表查看权限 */
    public static final String CODE_SC_RETURN_DC_DAILY_LIST_VIEW = "SC-DC-RET-DAILY-001";
    /** 门店商品退货日报(Form 供应商) 打印权限 */
    public static final String CODE_SC_RETURN_DC_DAILY_PRINT = "SC-DC-RET-DAILY-002";
    /** 门店商品退货日报(Form 供应商) 导出权限 */
    public static final String CODE_SC_RETURN_DC_DAILY_EXPORT = "SC-DC-RET-DAILY-003";

    /** MM Promotion Sales Daily Report 列表查看权限 */
    public static final String CODE_SC_MM_PROMOTION_LIST_VIEW = "SC-MM-PROMOTION-001";
    /** MM Promotion Sales Daily Report 打印权限 */
    public static final String CODE_SC_MM_PROMOTION_PRINT = "SC-MM-PROMOTION-002";
    /** MM Promotion Sales Daily Report 导出权限 */
    public static final String CODE_SC_MM_PROMOTION_EXPORT = "SC-MM-PROMOTION-003";

    /** 门店库存报废日报 列表查看权限 */
    public static final String CODE_SC_WRITE_OFF_LIST_VIEW = "SC-WRITE-OFF-001";
    /** 门店库存报废日报 打印权限 */
    public static final String CODE_SC_WRITE_OFF_PRINT = "SC-WRITE-OFF-002";
    /** 门店库存报废日报 导出权限 */
    public static final String CODE_SC_WRITE_OFF_EXPORT = "SC-WRITE-OFF-003";

    /** 门店库存调整日报 列表查看权限 */
    public static final String CODE_SC_ADJUSTMENT_LIST_VIEW = "SC-ADJUSTMENT-001";
    /** 门店库存调整日报 打印权限 */
    public static final String CODE_SC_ADJUSTMENT_PRINT = "SC-ADJUSTMENT-002";
    /** 门店库存调整日报 导出权限 */
    public static final String CODE_SC_ADJUSTMENT_EXPORT = "SC-ADJUSTMENT-003";

    /** 商品销售日报 列表查看权限 */
    public static final String CODE_SC_GS_RE_LIST_VIEW = "SC-GS-RE-001";
    /** 商品销售日报 打印权限 */
    public static final String CODE_SC_GS_RE_PRINT = "SC-GS-RE-002";
    /** 商品销售日报 导出权限 */
    public static final String CODE_SC_GS_RE_EXPORT = "SC-GS-RE-003";

    /** 门店库存调拨日报 列表查看权限 */
    public static final String CODE_SC_TRANSFER_LIST_VIEW = "SC-TRANSFER-001";
    /** 门店库存调拨日报 打印权限 */
    public static final String CODE_SC_TRANSFER_PRINT = "SC-TRANSFER-002";
    /** 门店库存调拨日报 导出权限 */
    public static final String CODE_SC_TRANSFER_EXPORT = "SC-TRANSFER-003";

    /**  店内库存转移日报  打印权限*/
    public static final String CODE_SC_IT_TR_PRINT = "SC-IT-TR-DAILY-001";
    /** 店内库存转移日报 导出权限 */
    public static final String CODE_SC_IT_TR_EXPORT = "SC-IT-TR-DAILY-002";
    /** 店内库存转移日报 列表查看权限 */
    public static final String CODE_SC_IT_TR_LIST_VIEW = "SC-IT-TR-DAILY-003";

    /** 重点商品销售日报 列表查看权限 */
    public static final String CODE_SC_IM_SALE_RE_LIST_VIEW = "SC-IM-SALE-RE-001";
    /** 重点商品销售日报 打印权限 */
    public static final String CODE_SC_IM_SALE_RE_PRINT = "SC-IM-SALE-RE-002";
    /** 重点商品销售日报 导出权限 */
    public static final String CODE_SC_IM_SALE_RE_EXPORT = "SC-IM-SALE-RE-003";

    /** 重点商品销售日报(by Hierarchy) 导出权限 */
    public static final String CODE_SC_IM_SALE_RE_HEXPORT="SC-CORE-DAILY-001";
    /** 重点商品销售日报(by Hierarchy) 列表查看权限 */
    public static final String CODE_SC_IM_SALE_LIST_VIEW="SC-CORE-DAILY-002";

    /**  Store Order Daily Report 列表查看权限 */
    public static final String SC_ORDER_REPORT_LIST_VIEW="SC-ORDER-REPORT-001";

    /** 门店库存日报 列表查看权限 */
    public static final String CODE_SC_INVENTORY_LIST_VIEW = "SC-INVENTORY-001";
    /** 门店库存日报 打印权限 */
    public static final String CODE_SC_INVENTORY_PRINT = "SC-INVENTORY-002";
    /** 门店库存日报 导出权限 */
    public static final String CODE_SC_INVENTORY_EXPORT = "SC-INVENTORY-003";

    /*HHT Summy-HHT Report 查看权限*/
    public static final String CODE_SC_HHT_VIEW = "SC-DC-HHT-DAILY-001";

    /** 每日顾客退货明细 查看一览权限 */
    public static final String CODE_SC_RETURNS_LIST_VIEW = "SC-RETURNS-001";
    /** 每日顾客退货明细 打印权限 */
    public static final String CODE_SC_RETURNS_PRINT = "SC-RETURNS-002";
    /** 每日顾客退货明细 导出权限 */
    public static final String CODE_SC_RETURNS_EXPORT = "SC-RETURNS-003";

    /** 暂停销售紧急录入 编辑权限 */
    public static final String CODE_SC_SUS_ENTRY_EDIT = "SC-SUS-ENTRY-001";

    /** 暂停销售紧急查询 查看一览权限 */
    public static final String CODE_SC_SUS_LIST_LIST_VIEW = "SC-SUS-LIST-001";

    /** 暂停销售紧急查询 导出权限 */
    public static final String CODE_SC_SUS_LIST_EXPORT = "SC-SUS-LIST-005";

    /** Invoice Entry (To Customer) 新增权限 */
    public static final String CODE_SC_INVOICE_ADD = "SC-INVOICE-001";
    /** Invoice Entry (To Customer) 查看权限 */
    public static final String CODE_SC_INVOICE_VIEW = "SC-INVOICE-002";
    /** Invoice Entry (To Customer) Issue Invoice权限 */
    public static final String CODE_SC_INVOICE_ISSUE = "SC-INVOICE-003";
    /** Invoice Entry (To Customer) 查看一览权限 */
    public static final String CODE_SC_INVOICE_LIST_VIEW = "SC-INVOICE-004";
    /** Invoice Entry (To Customer) 导出权限 */
    public static final String CODE_SC_INVOICE_EXPORT = "SC-INVOICE-005";

    /** Item Transfer Entry 新增权限 */
    public static final String CODE_SC_ITEM_TRANSFER_ADD = "SC-IT-TRANSFER-001";
    /** Item Transfer Entry 查看权限 */
    public static final String CODE_SC_ITEM_TRANSFER_VIEW = "SC-IT-TRANSFER-002";
    /** Item Transfer Entry 修改权限 */
    public static final String CODE_SC_ITEM_TRANSFER_EDIT = "SC-IT-TRANSFER-003";
    /** Item Transfer Entry 查看一览权限 */
    public static final String CODE_SC_ITEM_TRANSFER_LIST_VIEW = "SC-IT-TRANSFER-004";
    /** Item Transfer Entry 导出权限 */
    public static final String CODE_SC_ITEM_TRANSFER_EXPORT = "SC-IT-TRANSFER-005";
    /** Item Transfer Entry 审核记录查看权限 */
    public static final String CODE_SC_ITEM_TRANSFER_APPROVALRECORDS = "SC-IT-TRANSFER-006";

    /** todo-task 查看一览权限 */
    public static final String CODE_SC_TODO_TASKS_LIST_VIEW = "SC-TODO-TASKS-001";

    /** Operator Log 查看一览权限 */
    public static final String CODE_SC_OL_LIST_VIEW = "SC-OL-001";

    /** Price Label Management 查看一览权限 */
    public static final String CODE_SC_PRICE_LABEL_LIST_VIEW = "SC-PRICE-LABEL-001";

    /** Price Label Management 打印权限 */
    public static final String CODE_SC_PRICE_LABEL_PRINT = "SC-PRICE-LABEL-002";

    /** Reconciliation Management 导出权限 */
    public static final String SC_RECONCILE_EXPORT = "SC-RECONCILE-001";
    /** Reconciliation Management 列表查看权限 */
    public static final String SC_RECONCILE_LIST_VIEW = "SC-RECONCILE-002";

    /** Stamp Summary Report 列表查看权限 */
    public static final String CODE_SC_STAMP_SUMMARY_LIST_VIEW = "SC-SUMMARY-REP-001";
    /** Stamp Summary Report 打印权限 */
    public static final String CODE_SC_STAMP_SUMMARY_PRINT = "SC-SUMMARY-REP-002";
    /** Stamp Summary Report 导出权限 */
    public static final String CODE_SC_STAMP_SUMMARY_EXPORT = "SC-SUMMARY-REP-003";

    /** Stamp Detail Report 列表查看权限 */
    public static final String CODE_SC_STAMP_DETAIL_LIST_VIEW = "SC-SUMMARY-REP-001";
    /** Stamp Detail Report 导出权限 */
    public static final String CODE_SC_STAMP_DETAIL_EXPORT = "SC-SUMMARY-REP-002";

    /** Store Staff Attendance Detail Report HCM 列表查看权限 */
    public static final String CODE_SC_STAFF_ATTENDANCE_LIST_VIEW = "SC-STAFF-DAILY-001";
    /** Store Staff Attendance Detail Report HCM 导出权限 */
    public static final String CODE_SC_STAFF_ATTENDANCE__EXPORT = "SC-STAFF-DAILY-002";
	
    /** 上传文件到pos 页面权限 **/
    public static final String CODE_SC_RU_VIEW_LIST = "SC-RU";
    /** 上传文件到pos 列表查看权限 **/
    public static final String CODE_SC_RU_VIEW = "SC-RU-VIEW";
    /** 上传文件到pos 添加权限 **/
    public static final String CODE_SC_RU_ADD = "SC-RU-ADD";
    /** 上传文件到pos 编辑权限 **/
    public static final String CODE_SC_RU_EDIT = "SC-RU-EDIT";
    
    /** Clearance Of Inventory Entry 列表查看权限 */
    public static final String CODE_SC_ST_CLEAR_LIST_VIEW = "SC-ST-CLEAR-001";
    /** Clearance Of Inventory Entry 新增权限 */
    public static final String CODE_SC_ST_CLEAR_ADD = "SC-ST-CLEAR-002";
    /** Clearance Of Inventory Entry 导入权限 */
    public static final String CODE_SC_ST_CLEAR_IMPORT = "SC-ST-CLEAR-003";


}
