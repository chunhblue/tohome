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

 Date: 20/08/2021 10:40:37
*/


-- ----------------------------
-- Table structure for t_review
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_review";
CREATE TABLE "public"."t_review" (
  "n_reviewid" int4 NOT NULL DEFAULT nextval('t_review_id_seq'::regclass),
  "c_reviewcode" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "n_typeid" int2 NOT NULL,
  "c_reviewname" varchar(100) COLLATE "pg_catalog"."default",
  "start_role" varchar(30) COLLATE "pg_catalog"."default",
  "c_reserved_2" varchar(20) COLLATE "pg_catalog"."default",
  "c_reserved_3" varchar(20) COLLATE "pg_catalog"."default",
  "n_insert_user_id" numeric(10),
  "n_update_user_id" numeric(10),
  "d_insert_time" timestamp(6),
  "d_update_time" timestamp(6),
  "c_enable" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 1,
  "c_review_dep_cd" varchar(10) COLLATE "pg_catalog"."default",
  "c_review_pma_cd" varchar(20) COLLATE "pg_catalog"."default",
  "c_review_region_cd" varchar(6) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of t_review
-- ----------------------------
INSERT INTO "public"."t_review" VALUES (12, '000006', 8, '供应商退货', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (9, '000003', 5, '库存调整', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (11, '000005', 7, 'DC退货', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (19, '000013', 15, '库存调拨in', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (14, '000008', 10, '成本项目调整管理', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (16, '000010', 12, 'DC收货', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (17, '000011', 13, '供应商收货', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (1, 'V00001', 2, 'DC紧急订货', 'Category Staff', NULL, NULL, NULL, NULL, '2020-03-11 16:31:18', '2020-03-06 16:32:01', '1', '', '', '000001');
INSERT INTO "public"."t_review" VALUES (20, '000014', 16, '收货退货调整', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (21, '000015', 17, '库存调拨调整', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (8, '000002', 4, '库存调拨out', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (2, '000002', 1, '订货', 'Category Staff', NULL, NULL, NULL, NULL, '2020-03-11 16:31:18', '2020-03-06 16:32:01', '1', '', '', '');
INSERT INTO "public"."t_review" VALUES (22, '000016', 18, '支出管理', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (23, '000017', 19, 'DC退货确认', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (24, '000018', 20, '供应商退货确认', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (25, '000019', 21, 'POS支付方式管理', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (26, '000020', 22, 'POG管理', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (27, '000021', 23, '店内转移商品', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (10, '000004', 6, '盘点差异报告', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (28, '000022', 24, '盘点计划', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (7, '000001', 3, '库存报废', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
INSERT INTO "public"."t_review" VALUES (15, '000023', 11, '原材料管理', 'Category Staff', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL);
