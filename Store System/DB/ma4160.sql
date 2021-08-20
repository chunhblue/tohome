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

 Date: 20/08/2021 10:35:00
*/


-- ----------------------------
-- Table structure for ma4160
-- ----------------------------
DROP TABLE IF EXISTS "public"."ma4160";
CREATE TABLE "public"."ma4160" (
  "job_type_cd" varchar(13) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "job_catagory_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "effective_sts" char(2) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::bpchar,
  "create_user_id" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "create_ymd" char(8) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "create_hms" char(6) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "update_user_id" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "update_ymd" char(8) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "update_hms" varchar(6) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "update_screen_id" varchar(12) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "update_ip_address" varchar(15) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "nr_update_flg" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "nr_ymd" char(8) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "nr_hms" char(6) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "flag" char(255) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "position" numeric(2)
)
;
COMMENT ON COLUMN "public"."ma4160"."job_type_cd" IS '职务类型CD';
COMMENT ON COLUMN "public"."ma4160"."job_catagory_name" IS '职务类型';
COMMENT ON COLUMN "public"."ma4160"."effective_sts" IS '生效状态';
COMMENT ON COLUMN "public"."ma4160"."create_user_id" IS '创建员工';
COMMENT ON COLUMN "public"."ma4160"."create_ymd" IS '创建日期';
COMMENT ON COLUMN "public"."ma4160"."create_hms" IS '创建时间';
COMMENT ON COLUMN "public"."ma4160"."update_user_id" IS '修改员工';
COMMENT ON COLUMN "public"."ma4160"."update_ymd" IS '修改日期';
COMMENT ON COLUMN "public"."ma4160"."update_hms" IS '修改时间';
COMMENT ON COLUMN "public"."ma4160"."update_screen_id" IS '更新画面ID';
COMMENT ON COLUMN "public"."ma4160"."update_ip_address" IS '更新用户ip';
COMMENT ON COLUMN "public"."ma4160"."nr_update_flg" IS '批处理更新flag';
COMMENT ON COLUMN "public"."ma4160"."nr_ymd" IS '批处理更新日期';
COMMENT ON COLUMN "public"."ma4160"."nr_hms" IS '批处理更新时间';
COMMENT ON COLUMN "public"."ma4160"."position" IS '4:SM  5:Operations Head 6：Finance';

-- ----------------------------
-- Records of ma4160
-- ----------------------------
INSERT INTO "public"."ma4160" VALUES ('10040', 'IT Support', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 15);
INSERT INTO "public"."ma4160" VALUES ('100060', 'CEO', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 20);
INSERT INTO "public"."ma4160" VALUES ('10033', 'Area Manager', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO "public"."ma4160" VALUES ('100070', 'Sales and Operations Manager', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2);
INSERT INTO "public"."ma4160" VALUES ('100069', 'Sales and Operations Controller', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3);
INSERT INTO "public"."ma4160" VALUES ('100041', 'Store Manager', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4);
INSERT INTO "public"."ma4160" VALUES ('000001', 'General Manager', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5);
INSERT INTO "public"."ma4160" VALUES ('100089', 'Sales & Deposit Supervisor', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11);
INSERT INTO "public"."ma4160" VALUES ('100091', 'Internal Control & Compliance Manager', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8);
INSERT INTO "public"."ma4160" VALUES ('100092', 'Stock-take Supervisor', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7);
INSERT INTO "public"."ma4160" VALUES ('10030', 'OPs Retail Support', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5);
INSERT INTO "public"."ma4160" VALUES ('10026', 'COO', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 21);
INSERT INTO "public"."ma4160" VALUES ('100042', 'Store Manager2', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4);
INSERT INTO "public"."ma4160" VALUES ('100043', 'Store Manager3', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4);
INSERT INTO "public"."ma4160" VALUES ('100090', 'Stock-take Team Leader', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6);
INSERT INTO "public"."ma4160" VALUES ('100093', 'Store Auditor', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10);
INSERT INTO "public"."ma4160" VALUES ('000010', 'Regional Director', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5);
INSERT INTO "public"."ma4160" VALUES ('000011', 'General Director', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5);
INSERT INTO "public"."ma4160" VALUES ('100045', 'Store Manager5', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4);
INSERT INTO "public"."ma4160" VALUES ('100046', 'Store Manager6', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4);
