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

 Date: 20/07/2021 14:30:58
*/


-- ----------------------------
-- Table structure for t_audit_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_audit_type";
CREATE TABLE "public"."t_audit_type" (
  "n_typeid" int2 NOT NULL,
  "c_type_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "c_url" varchar(100) COLLATE "pg_catalog"."default",
  "n_distinguish_typeid" varchar(5) COLLATE "pg_catalog"."default",
  "c_url2" varchar(100) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of t_audit_type
-- ----------------------------
INSERT INTO "public"."t_audit_type" VALUES (7, 'Item Return Request Entry(To DC)', 'return/warehouse/returnedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (8, 'Item Return Request Entry(To Supplier)', 'return/vendor/returnedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (3, 'Inventory Write-off Entry', 'stock/scrap/scrapedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (5, 'Inventory Adjustment Entry', 'stock/adjustment/adjustmentedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (12, 'Item Receiving Entry(From DC)', 'receipt/warehouse/receiptedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (13, 'Item Receiving Entry(From Supplier)', 'receipt/vendor/receiptedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (2, 'Urgent Order Entry(to DC)', 'order_dc_urgent/orderEdit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (16, 'Receiving And Return Document Correction Entry', 'rrModifyQuery/rrModifyEdit', '1', NULL);
INSERT INTO "public"."t_audit_type" VALUES (1, 'Order Entry', 'order_newStore/orderlist', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (18, 'Expenditure Entry', 'fundentry/fundEntryEdit', '1', NULL);
INSERT INTO "public"."t_audit_type" VALUES (19, 'Item Return Entry(To DC)', 'receipt/return_warehouse/returnedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (20, 'Item Return Entry(To Supplier)', 'receipt/return_vendor/returnedit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (21, 'POS Payment Methods Management', 'paymentMode/paymentModeEdit', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (22, 'POG Entry and Query', 'storeItemRelationship/storeItemRelationship', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (6, 'Stocktaking Variance Report', 'stocktakeprocess/stocktakeProcessEdit', '1', 'stocktakeentry/stocktakeDataEntry');
INSERT INTO "public"."t_audit_type" VALUES (24, 'Stocktaking Plan Setting', 'stocktakeplan/stocktakePlanEdit', '1', 'stocktakeentry/stocktakeDataEntry');
INSERT INTO "public"."t_audit_type" VALUES (23, 'Item Transfer Entry', 'itemTransfer/itemTransferedit', '1', NULL);
INSERT INTO "public"."t_audit_type" VALUES (10, 'Store Expense Item Stock Management', 'costdataentryLd/costDataEntry', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (11, 'Month End Stock Count Management', 'materialinventoryentryLd/materInventoryDataEntry', '1', '');
INSERT INTO "public"."t_audit_type" VALUES (4, 'Store Transfer Entry', 'storetransfers/storetransfersedit', '1', '');
