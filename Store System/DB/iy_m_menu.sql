/*
 Navicat Premium Data Transfer

 Source Server         : 119.23.201.148_5432
 Source Server Type    : PostgreSQL
 Source Server Version : 110012
 Source Host           : nribits.vicp.io:46399
 Source Catalog        : storedb_store_uat
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 110012
 File Encoding         : 65001

 Date: 20/08/2021 10:29:59
*/


-- ----------------------------
-- Table structure for iy_m_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iy_m_menu";
CREATE TABLE "public"."iy_m_menu" (
  "id" int4 NOT NULL DEFAULT nextval('iy_m_menu_id_seq'::regclass),
  "parent_id" int4,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "url" varchar(200) COLLATE "pg_catalog"."default",
  "sort" int4 NOT NULL,
  "group_code" varchar(15) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iy_m_menu"."sort" IS '10、20、30、40，按照升序获取';
COMMENT ON COLUMN "public"."iy_m_menu"."group_code" IS '对应权限许可表中的“group_code”字段，用于菜单+权限显示';

-- ----------------------------
-- Records of iy_m_menu
-- ----------------------------
INSERT INTO "public"."iy_m_menu" VALUES (114, 36, 'Urgent Sale Pause Entry ', 'suspendSaleEntry', 130, 'SC-SUS-ENTRY');
INSERT INTO "public"."iy_m_menu" VALUES (116, 85, 'Rejected Approvals ', 'auditMessage/rejectedApprovals', 80, 'SC-INF-REJECTED');
INSERT INTO "public"."iy_m_menu" VALUES (118, 36, 'Invoice Entry (To Customer)', 'invoiceEntry', 150, 'SC-INVOICE');
INSERT INTO "public"."iy_m_menu" VALUES (115, 36, 'Urgent Sale Pause Query ', 'suspendSaleDetail', 140, 'SC-SUS-LIST');
INSERT INTO "public"."iy_m_menu" VALUES (117, 85, 'TODO Tasks', 'index', 20, 'SC-TODO-TASKS');
INSERT INTO "public"."iy_m_menu" VALUES (51, 36, 'Cashier Management', 'cashier', 30, 'SC-CASHIER');
INSERT INTO "public"."iy_m_menu" VALUES (54, 36, 'Electronic Receipts Query', 'electronicReceipt', 40, 'SC-RECEIPTS');
INSERT INTO "public"."iy_m_menu" VALUES (44, 40, 'MM Promotion Query', 'mmPromotion', 30, 'SC-ZD-BPQ');
INSERT INTO "public"."iy_m_menu" VALUES (68, 1, 'Menu Management', 'menu', 40, 'P-MENU');
INSERT INTO "public"."iy_m_menu" VALUES (47, 40, 'Item Master Query', 'itemMaster', 20, 'SC-ZD-IMQ');
INSERT INTO "public"."iy_m_menu" VALUES (48, 40, 'Vendor Master Query', 'vendorMaster', 10, 'SC-ZD-VMQ');
INSERT INTO "public"."iy_m_menu" VALUES (122, 49, 'MM Promotion Sales Daily Report', 'mmPromotionSaleDaily', 150, 'SC-MM-PROMOTION');
INSERT INTO "public"."iy_m_menu" VALUES (106, 49, 'Store Inventory Adjustment Daily Report', 'adjustmentDaily', 90, 'SC-ADJUSTMENT');
INSERT INTO "public"."iy_m_menu" VALUES (55, 36, 'Sale Information Query', 'cashierDetail', 50, 'SC-CS-DETAIL');
INSERT INTO "public"."iy_m_menu" VALUES (57, NULL, 'Receiving', NULL, 20, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (108, 49, 'Item Sales Daily Report  ', 'GoodsSaleReport', 100, 'SC-GS-RE');
INSERT INTO "public"."iy_m_menu" VALUES (28, NULL, 'Inventory', NULL, 30, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (32, NULL, 'Stocktaking', NULL, 40, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (87, 85, 'Notifications', 'informReply', 30, 'SC-INF-REPLY');
INSERT INTO "public"."iy_m_menu" VALUES (102, 49, 'Store Sales Daily Report ', 'sellDayReport', 40, 'SC-STORE-DAY');
INSERT INTO "public"."iy_m_menu" VALUES (36, NULL, 'Sale', NULL, 50, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (49, NULL, 'Report', NULL, 60, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (30, 28, 'Inventory  Adjustment Entry', 'stockAdjustment', 20, 'SC-ST-ADJUST');
INSERT INTO "public"."iy_m_menu" VALUES (74, 36, 'Urgent Price Info Update Entry ', 'priceChange', 110, 'SC-PRI-CAG');
INSERT INTO "public"."iy_m_menu" VALUES (88, 85, 'Notifications Management', 'informHQ', 10, 'SC-INF-HQ');
INSERT INTO "public"."iy_m_menu" VALUES (75, 36, 'Urgent Price Info Update Query ', 'priceChangeDetail', 120, 'SC-PRI-CHANGE');
INSERT INTO "public"."iy_m_menu" VALUES (38, 36, 'Material Item Sales Query', 'maItemSale', 20, 'SC-XS-ORDER');
INSERT INTO "public"."iy_m_menu" VALUES (35, 32, 'Stocktaking  Variance Report', 'stocktakeProcess', 30, 'SC-PD-PROCESS');
INSERT INTO "public"."iy_m_menu" VALUES (58, 57, 'Receiving And Return Document Correction Query', 'rrModifyQuery', 20, 'SC-RR-MODIF');
INSERT INTO "public"."iy_m_menu" VALUES (79, NULL, 'Basic', NULL, 110, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (4, 1, 'Special User Privilege Management', 'ua', 30, 'P-UAAS');
INSERT INTO "public"."iy_m_menu" VALUES (3, 1, 'General Privilege Management', 'defaultrole', 20, 'P-ROLE-DEF');
INSERT INTO "public"."iy_m_menu" VALUES (2, 1, 'Privilege Management', 'role', 10, 'P-ROLE');
INSERT INTO "public"."iy_m_menu" VALUES (71, 36, 'Bank Deposit', 'bankPayIn', 75, 'SC-PI');
INSERT INTO "public"."iy_m_menu" VALUES (110, 49, 'Store Transfer Daily Report', 'storeTransferDaily', 110, 'SC-TRANSFER');
INSERT INTO "public"."iy_m_menu" VALUES (72, 57, 'Item Return Entry(To Supplier)', 'returnVendor/receipt', 90, 'SC-RV');
INSERT INTO "public"."iy_m_menu" VALUES (59, 57, 'Receiving And Return Query', 'rrVoucherQuery', 10, 'SC-RR-QUERY');
INSERT INTO "public"."iy_m_menu" VALUES (23, 18, 'Delivery Variance Query', 'difference', 60, 'SC-DIFFERENCE');
INSERT INTO "public"."iy_m_menu" VALUES (82, 32, 'Stocktaking  Plan Setting', 'stocktakePlanEntry', 10, 'SC-PD-SETTING');
INSERT INTO "public"."iy_m_menu" VALUES (41, 40, 'Price Query', 'priceByDay', 40, 'SC-ZD-PBD');
INSERT INTO "public"."iy_m_menu" VALUES (107, 49, 'Store Receiving Daily Report(To DC)', 'dcReceiptDaily', 70, 'SC-DC-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (105, 49, 'Store Inventory Write Off Daily Report', 'writeOff', 80, 'SC-WRITE-OFF');
INSERT INTO "public"."iy_m_menu" VALUES (112, 49, 'Store Inventory Daily Report', 'storeInventoryDaily', 130, 'SC-INVENTORY');
INSERT INTO "public"."iy_m_menu" VALUES (37, 36, 'Unpacking Sales', 'unpackSale', 10, 'SC-XS-USALE');
INSERT INTO "public"."iy_m_menu" VALUES (96, 85, 'Message ', 'auditMessage/myMessage', 100, 'SC-INF-MESSAGE');
INSERT INTO "public"."iy_m_menu" VALUES (98, 85, 'New Item Notifications', 'informNewProduct', 50, 'SC-NEWPRODUCR');
INSERT INTO "public"."iy_m_menu" VALUES (95, 85, 'My Submission ', 'auditMessage/mySubmissions', 90, 'SC-INF-DOCUMENT');
INSERT INTO "public"."iy_m_menu" VALUES (84, 76, 'User Management ', 'employesInformation', 30, 'SC-EM-INFOR');
INSERT INTO "public"."iy_m_menu" VALUES (63, 28, 'Real-Time Inventory Query', 'rtInventoryQuery', 10, 'SC-RF-INVENTORY');
INSERT INTO "public"."iy_m_menu" VALUES (45, 28, 'Inventory Document Query', 'inventoryVoucher', 15, 'SC-ST-KCCPYL');
INSERT INTO "public"."iy_m_menu" VALUES (81, 79, 'POS Payment Methods Management ', 'paymentMode', 20, 'SC-PM');
INSERT INTO "public"."iy_m_menu" VALUES (99, 85, 'MM Promotion Notifications', 'informPromotion', 60, 'SC-PROMOTION');
INSERT INTO "public"."iy_m_menu" VALUES (94, 85, 'Pending Approvals ', 'auditMessage/pendingApprovals', 70, 'SC-INF-AUDIT');
INSERT INTO "public"."iy_m_menu" VALUES (83, 76, 'Hierarchy Management', 'organizationalStructure', 20, 'SC-OR-STRUCT');
INSERT INTO "public"."iy_m_menu" VALUES (77, 76, 'Position Query', 'officeManagement', 10, 'SC-OFF-MANAGE');
INSERT INTO "public"."iy_m_menu" VALUES (22, 57, 'Item Receiving Entry(From Supplier)', 'vendorReceipt', 50, 'SC-V-RECEIPT');
INSERT INTO "public"."iy_m_menu" VALUES (34, 32, 'Stocktaking  Result Entry And Query', 'stocktakeEntry', 20, 'SC-PD-ENTRY');
INSERT INTO "public"."iy_m_menu" VALUES (76, NULL, 'Hierarchy', NULL, 90, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (73, 57, 'Item Return Entry(To DC)', 'returnWarehouse/receipt', 80, 'SC-RW');
INSERT INTO "public"."iy_m_menu" VALUES (1, NULL, 'Privilege', NULL, 120, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (103, 49, 'Department Sales Daily Report', 'classifiedSaleReport', 50, 'SC-STORE-SALE');
INSERT INTO "public"."iy_m_menu" VALUES (20, 57, 'Item Receiving Entry(From DC)', 'receipt', 40, 'SC-RECEIPT');
INSERT INTO "public"."iy_m_menu" VALUES (40, NULL, 'Master', NULL, 70, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (42, NULL, 'Operation', NULL, 80, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (104, 49, 'Store Receiving Daily Report(To Supplier)', 'vendorReceiptDaily', 60, 'SC-VENDOR-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (61, 57, 'Receiving And Return Document Correction Entry', 'rrModifyQuery/add', 30, 'SC-RR-MODIF-E');
INSERT INTO "public"."iy_m_menu" VALUES (85, NULL, 'Message', NULL, 100, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (26, 57, 'Item Return Request Entry(To DC)', 'returnWarehouse', 60, 'SC-W-RETURN');
INSERT INTO "public"."iy_m_menu" VALUES (69, 18, 'POG Entry&Query ', 'storeItemRelationship', 90, 'SC-S-I-R');
INSERT INTO "public"."iy_m_menu" VALUES (39, 49, 'BOM Sales Report', 'bomSale', 10, 'SC-RF-BOM');
INSERT INTO "public"."iy_m_menu" VALUES (50, 49, 'Group Sale Item Sales Report', 'groupSale', 20, 'SC-RF-GROUP');
INSERT INTO "public"."iy_m_menu" VALUES (92, 85, 'Notifications Log', 'informLog', 40, 'SC-INF-LOG');
INSERT INTO "public"."iy_m_menu" VALUES (21, 18, 'Order Entry', 'storeOrder', 30, 'SC-NS-ORDER');
INSERT INTO "public"."iy_m_menu" VALUES (111, 49, 'Core Item Daily Report(By Company)', 'importantGoodsSaleReport', 120, 'SC-IM-SALE-RE');
INSERT INTO "public"."iy_m_menu" VALUES (27, 57, 'Item Return Request Entry(To Supplier)', 'returnVendor', 70, 'SC-V-RETURN');
INSERT INTO "public"."iy_m_menu" VALUES (119, 40, 'Store Master Query', 'storeMaster', 50, 'SC-ZD-SMQ');
INSERT INTO "public"."iy_m_menu" VALUES (90, 18, 'Direct Store Purchase Order Query', 'directSupplierOrder', 50, 'SC-DIRECR-ORDER');
INSERT INTO "public"."iy_m_menu" VALUES (70, 36, 'Cash Balancing Entry', 'cashierAmount', 80, 'SC-CA');
INSERT INTO "public"."iy_m_menu" VALUES (113, 49, 'Non Sale Daily Report', 'returnsDaily', 140, 'SC-RETURNS');
INSERT INTO "public"."iy_m_menu" VALUES (60, 36, 'Cash Balancing  Query', 'saleReconciliation', 60, 'SC-SL-CF');
INSERT INTO "public"."iy_m_menu" VALUES (97, 42, 'Store Expense Item Stock Management', 'custEntry', 50, 'SC-COST-ENTRY');
INSERT INTO "public"."iy_m_menu" VALUES (62, 36, 'Operations Daily Report ', 'businessDaily', 70, 'SC-BD');
INSERT INTO "public"."iy_m_menu" VALUES (101, 42, 'Month End Stock Count Management', 'materialEntry', 70, 'SC-MATEL-ENTRY');
INSERT INTO "public"."iy_m_menu" VALUES (43, 42, 'Expenditure Management', 'fundQuery', 10, 'SC-JF-QUERY');
INSERT INTO "public"."iy_m_menu" VALUES (31, 28, 'Inventory Write-Off Entry', 'stockScrap', 30, 'SC-ST-SCRAP');
INSERT INTO "public"."iy_m_menu" VALUES (123, 79, 'Operator Log', 'operatorLog', 30, 'SC-OL');
INSERT INTO "public"."iy_m_menu" VALUES (127, 49, 'Core Item Daily Report(By  Hierarchy)', 'coreItemByhi', 170, 'SC-CORE-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (125, 49, 'Store Order Daily Report', 'orderReportQuery', 160, 'SC-ORDER-REPORT');
INSERT INTO "public"."iy_m_menu" VALUES (128, 49, 'Service Type Daily Report', 'serviceTypeDaily', 180, 'SC-SERVE-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (130, 28, 'Item Transfer Entry', 'itemTransfer', 70, 'SC-IT-TRANSFER');
INSERT INTO "public"."iy_m_menu" VALUES (129, 49, 'Reconciliation Management', 'storeReconciliationMng', 190, 'SC-RECONCILE');
INSERT INTO "public"."iy_m_menu" VALUES (131, 49, 'Item Transfer Daily Report', 'itemTransferDaily', 200, 'SC-IT-TR-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (132, 36, 'Price Label Management', 'priceLabel', 160, 'SC-PRICE-LABEL');
INSERT INTO "public"."iy_m_menu" VALUES (25, 18, 'Urgent Order Entry(To DC)', 'urgentOrderDc', 110, 'SC-ORDER-DC');
INSERT INTO "public"."iy_m_menu" VALUES (133, 18, 'POG Shelf Maintenance Management', 'shelfMaintenance', 100, 'SC-SHELF-MAINT');
INSERT INTO "public"."iy_m_menu" VALUES (134, 49, 'Store Returned Daily Report(To Supplier)', 'vendorReturnedDaily', 75, 'SC-RETURN-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (135, 49, 'Store Returned Daily Report(To DC)', 'dcReturnedDaily', 76, 'SC-DC-RET-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (91, 18, 'DC Store Order Query', 'cdOrder', 40, 'SC-ORDER-CD');
INSERT INTO "public"."iy_m_menu" VALUES (19, 18, 'Order New Store Entry', 'order', 35, 'SC-ORDER');
INSERT INTO "public"."iy_m_menu" VALUES (33, 32, 'Stocktaking Non-Count List', 'stockTakeNonCount', 40, 'SC-PD-NON-COUNT');
INSERT INTO "public"."iy_m_menu" VALUES (146, 28, 'Clearance Of Inventory Entry', 'clearInventory', 80, 'SC-ST-CLEAR');
INSERT INTO "public"."iy_m_menu" VALUES (148, 18, 'Failed Order List', 'orderFailed', 75, 'SC-OD-FAILED');
INSERT INTO "public"."iy_m_menu" VALUES (24, 18, 'DC Picked Vs Actual Received Variance Detail', 'shoppingOrderDifference', 70, 'SC-SHOP-DIFFER');
INSERT INTO "public"."iy_m_menu" VALUES (18, NULL, 'Ordering
', NULL, 10, NULL);
INSERT INTO "public"."iy_m_menu" VALUES (149, 18, 'HHT Order  List', 'orderHHTR', 220, 'SC-OD-orderHHTR');
INSERT INTO "public"."iy_m_menu" VALUES (136, 49, 'PO Status Daily Report', 'hhtReport', 210, 'SC-DC-HHT-DAILY');
INSERT INTO "public"."iy_m_menu" VALUES (147, 79, 'POS Remote Update Management', 'remoteUpdate', 10, 'SC-RU');
INSERT INTO "public"."iy_m_menu" VALUES (29, 28, 'Store Transfer Entry', 'storeTransfers', 40, 'SC-ST-TRANSFER');
