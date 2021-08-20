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

 Date: 20/08/2021 10:31:05
*/


-- ----------------------------
-- Table structure for iy_m_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."iy_m_role";
CREATE TABLE "public"."iy_m_role" (
  "id" int4 NOT NULL DEFAULT nextval('iy_m_role_id_seq'::regclass),
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "super_id" int4,
  "grant_flg" int2 NOT NULL,
  "r_type" int4,
  "r_id" varchar(50) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of iy_m_role
-- ----------------------------
INSERT INTO "public"."iy_m_role" VALUES (57, 'Retail Support Head / I.T', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (45, 'Alice', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (60, 'Sales and Operations Manager ', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (61, 'Sales and Operations Controller', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (62, 'Religional Director', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (64, 'General Manager', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (63, 'system', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (4, 'Administrators', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (65, 'Test 8811', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (67, 'OPs Retail Support', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (66, 'Stock-take Supervisor', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (58, 'Store Manager', NULL, 1, NULL, NULL);
INSERT INTO "public"."iy_m_role" VALUES (59, 'Area Manager', NULL, 1, NULL, NULL);
