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

 Date: 20/08/2021 11:18:25
*/


-- ----------------------------
-- Table structure for t_backlog
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_backlog";
CREATE TABLE "public"."t_backlog" (
  "n_recordid" int8 NOT NULL DEFAULT nextval('t_backlog_id_seq'::regclass),
  "n_roleid" int4 NOT NULL,
  "n_reviewid" int4 NOT NULL,
  "c_priority" int2 DEFAULT 91,
  "c_url" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "d_insert_time" timestamp(6),
  "store_cd" varchar(8) COLLATE "pg_catalog"."default",
  "record_cd" varchar(30) COLLATE "pg_catalog"."default",
  "n_typeid" int2,
  "sub_no" int2,
  "c_enable" varchar(2) COLLATE "pg_catalog"."default" DEFAULT 1,
  "c_url2" varchar(256) COLLATE "pg_catalog"."default",
  "effective_start_date" varchar(8) COLLATE "pg_catalog"."default" DEFAULT 0
)
;
