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

 Date: 20/07/2021 14:31:55
*/


-- ----------------------------
-- Table structure for t_audit_tab
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_audit_tab";
CREATE TABLE "public"."t_audit_tab" (
  "n_recordid" numeric(10) NOT NULL DEFAULT nextval('t_audit_tab_id_seq'::regclass),
  "n_reviewid" numeric(10) NOT NULL,
  "n_typeid" int2 NOT NULL,
  "record_cd" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "store_cd" varchar(8) COLLATE "pg_catalog"."default" NOT NULL,
  "sub_no" int2 NOT NULL,
  "c_step" numeric(10) NOT NULL,
  "c_auditstatus" numeric(10) NOT NULL,
  "n_operatorid" varchar(100) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "audit_time" timestamp(6),
  "audit_content" varchar(200) COLLATE "pg_catalog"."default",
  "c_reserved_1" varchar(20) COLLATE "pg_catalog"."default",
  "c_reserved_2" varchar(20) COLLATE "pg_catalog"."default",
  "c_reserved_3" varchar(20) COLLATE "pg_catalog"."default",
  "d_insert_time" timestamp(6),
  "d_update_time" timestamp(6),
  "c_enable" char(1) COLLATE "pg_catalog"."default" DEFAULT 1,
  "n_distinguish_typeid" varchar(5) COLLATE "pg_catalog"."default",
  "effective_start_date" varchar(8) COLLATE "pg_catalog"."default" DEFAULT 0
)
;
