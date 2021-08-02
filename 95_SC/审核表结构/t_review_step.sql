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

 Date: 20/07/2021 14:31:17
*/


-- ----------------------------
-- Table structure for t_review_step
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_review_step";
CREATE TABLE "public"."t_review_step" (
  "n_recordid" numeric(10) NOT NULL DEFAULT nextval('t_review_step_id_seq'::regclass),
  "n_reviewid" numeric(10) NOT NULL,
  "c_step" numeric(10) NOT NULL,
  "c_stepname" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "n_roleid" numeric(10) NOT NULL,
  "c_auditstatus" numeric(10) NOT NULL,
  "c_reserved_1" varchar(20) COLLATE "pg_catalog"."default",
  "c_reserved_2" varchar(20) COLLATE "pg_catalog"."default",
  "c_reserved_3" varchar(20) COLLATE "pg_catalog"."default",
  "n_insert_user_id" numeric(10),
  "n_update_user_id" numeric(10),
  "d_insert_time" timestamp(6),
  "d_update_time" timestamp(6),
  "c_enable" char(1) COLLATE "pg_catalog"."default" DEFAULT 1
)
;

-- ----------------------------
-- Records of t_review_step
-- ----------------------------
INSERT INTO "public"."t_review_step" VALUES (1, 1, 1, '紧急订货SM', 4, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (6, 7, 1, '报废AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (8, 8, 1, '库存调出AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (9, 9, 1, '库存调整AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (31, 9, 2, '库存调整OM', 2, 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (12, 11, 1, 'DC退货AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (13, 11, 2, 'DC退货OM', 2, 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (14, 12, 1, '供应商退货AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (28, 19, 1, '库存调入AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (18, 14, 1, '成本项目调整管理AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (2, 2, 1, '订货SM', 4, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (22, 16, 1, 'DC收货SM', 4, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (24, 17, 1, '供应商收货SM', 4, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (25, 22, 1, '支出管理AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (30, 21, 1, '库存调拨调整AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (32, 23, 1, 'DC退货确认AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (33, 23, 2, 'DC退货确认OM', 2, 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (34, 24, 1, '供应商退货确认AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (35, 25, 1, 'POS支付方式管理OH', 5, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (36, 26, 1, 'POG管理AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (10, 10, 1, '盘点差异AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (11, 10, 2, '盘点差异OM', 2, 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (39, 1, 2, '紧急订货OM', 2, 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (40, 1, 3, '紧急订货OC', 2, 30, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (29, 20, 1, '收货退货调整SM', 4, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (41, 20, 2, '收货退货调整AM', 1, 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (42, 20, 3, '收货退货调整OM', 2, 30, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (43, 15, 1, '原材料管理AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (37, 27, 1, '店内转移商品AM', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (38, 28, 1, '盘点计划 财务STL', 6, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (44, 28, 2, '盘点计划 财务STS', 7, 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO "public"."t_review_step" VALUES (45, 28, 3, '盘点计划 财务ICCM', 8, 30, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
