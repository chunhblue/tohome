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

 Date: 20/08/2021 10:34:01
*/


-- ----------------------------
-- Table structure for ma4200
-- ----------------------------
DROP TABLE IF EXISTS "public"."ma4200";
CREATE TABLE "public"."ma4200" (
  "emp_num_id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "emp_id" int4 NOT NULL DEFAULT nextval('ma4200_emp_id_seq'::regclass),
  "store_cd" varchar(6) COLLATE "pg_catalog"."default",
  "job_type_cd" varchar(13) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::bpchar,
  "emp_password" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "effective_status" char(2) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "emp_name" varchar(30) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "emp_gender" char(2) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "emp_country" varchar(20) COLLATE "pg_catalog"."default",
  "telephone_no" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "mobile_phone_no" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_birthdate" varchar(8) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_education" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_date" varchar(8) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_leave_date" varchar(8) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_address" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_postal_code" varchar(10) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_comment" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "emp_email" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "create_user_id" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "create_ymd" varchar(8) COLLATE "pg_catalog"."default",
  "create_hms" char(6) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "update_user_id" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "update_ymd" char(8) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "update_hms" char(6) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "update_screen_id" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "update_ip_address" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "nr_update_flg" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "nr_ymd" char(8) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "nr_hms" char(6) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "job_catagory_name" varchar(255) COLLATE "pg_catalog"."default",
  "md" char(1) COLLATE "pg_catalog"."default" DEFAULT 0,
  "md_user_id" int4
)
;
COMMENT ON COLUMN "public"."ma4200"."emp_num_id" IS '用户账号 唯一';
COMMENT ON COLUMN "public"."ma4200"."emp_id" IS '编号';
COMMENT ON COLUMN "public"."ma4200"."store_cd" IS '店铺';
COMMENT ON COLUMN "public"."ma4200"."job_type_cd" IS '职务和ma4160表相连接使用id';
COMMENT ON COLUMN "public"."ma4200"."emp_password" IS '职务密码';
COMMENT ON COLUMN "public"."ma4200"."effective_status" IS '职务状态';
COMMENT ON COLUMN "public"."ma4200"."emp_name" IS '职工姓名';
COMMENT ON COLUMN "public"."ma4200"."emp_gender" IS '职工性别';
COMMENT ON COLUMN "public"."ma4200"."emp_country" IS '职工国家';
COMMENT ON COLUMN "public"."ma4200"."telephone_no" IS '电话';
COMMENT ON COLUMN "public"."ma4200"."mobile_phone_no" IS '手机号';
COMMENT ON COLUMN "public"."ma4200"."emp_birthdate" IS '出生时间';
COMMENT ON COLUMN "public"."ma4200"."emp_education" IS '学历';
COMMENT ON COLUMN "public"."ma4200"."emp_date" IS '入职时间';
COMMENT ON COLUMN "public"."ma4200"."emp_leave_date" IS '员工离职时间';
COMMENT ON COLUMN "public"."ma4200"."emp_address" IS '员工住址';
COMMENT ON COLUMN "public"."ma4200"."emp_postal_code" IS '邮编';
COMMENT ON COLUMN "public"."ma4200"."emp_comment" IS '员工备注';
COMMENT ON COLUMN "public"."ma4200"."emp_email" IS '员工邮件';
COMMENT ON COLUMN "public"."ma4200"."create_user_id" IS '创建员工';

-- ----------------------------
-- Primary Key structure for table ma4200
-- ----------------------------
ALTER TABLE "public"."ma4200" ADD CONSTRAINT "ma4200_pkey" PRIMARY KEY ("emp_num_id");


INSERT INTO "public"."ma4200"("emp_num_id", "emp_id", "store_cd", "job_type_cd", "emp_password", "effective_status", "emp_name", "emp_gender", "emp_country", "telephone_no", "mobile_phone_no", "emp_birthdate", "emp_education", "emp_date", "emp_leave_date", "emp_address", "emp_postal_code", "emp_comment", "emp_email", "create_user_id", "create_ymd", "create_hms", "update_user_id", "update_ymd", "update_hms", "update_screen_id", "update_ip_address", "nr_update_flg", "nr_ymd", "nr_hms", "job_catagory_name", "md", "md_user_id") VALUES ('system', 421, NULL, '000001', 'e10adc3949ba59abbe56e057f20f883e', '10', 'System', '02', NULL, '', '', NULL, NULL, NULL, NULL, '', '', NULL, '3763503161@163.com                                        _lyz', NULL, NULL, NULL, 'system', '20210616', '051930', NULL, NULL, NULL, NULL, NULL, NULL, '0', 245);
INSERT INTO "public"."ma4200"("emp_num_id", "emp_id", "store_cd", "job_type_cd", "emp_password", "effective_status", "emp_name", "emp_gender", "emp_country", "telephone_no", "mobile_phone_no", "emp_birthdate", "emp_education", "emp_date", "emp_leave_date", "emp_address", "emp_postal_code", "emp_comment", "emp_email", "create_user_id", "create_ymd", "create_hms", "update_user_id", "update_ymd", "update_hms", "update_screen_id", "update_ip_address", "nr_update_flg", "nr_ymd", "nr_hms", "job_catagory_name", "md", "md_user_id") VALUES ('CK-HCM032496', 399, 'SG0100', '10033', 'e10adc3949ba59abbe56e057f20f883e', '10', 'Nguyen Thi Thanh Loan', '02', '', '', '', '', '', '', '', '', '', 'SM - Bui Thi Xuan', 'loan.nguyenthithanh@circlek.com.vn_lyz', 'system', '20210605', '043715', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL);
INSERT INTO "public"."ma4200"("emp_num_id", "emp_id", "store_cd", "job_type_cd", "emp_password", "effective_status", "emp_name", "emp_gender", "emp_country", "telephone_no", "mobile_phone_no", "emp_birthdate", "emp_education", "emp_date", "emp_leave_date", "emp_address", "emp_postal_code", "emp_comment", "emp_email", "create_user_id", "create_ymd", "create_hms", "update_user_id", "update_ymd", "update_hms", "update_screen_id", "update_ip_address", "nr_update_flg", "nr_ymd", "nr_hms", "job_catagory_name", "md", "md_user_id") VALUES ('CK-HCM039329', 413, 'SG0031', '100041', 'e10adc3949ba59abbe56e057f20f883e', '90', 'Lưu Hữu Trúc', '01', '', '', '', '', '', '', '', '', '', '', '_lyz', NULL, NULL, NULL, 'system', '20210616', '062600', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO "public"."ma4200"("emp_num_id", "emp_id", "store_cd", "job_type_cd", "emp_password", "effective_status", "emp_name", "emp_gender", "emp_country", "telephone_no", "mobile_phone_no", "emp_birthdate", "emp_education", "emp_date", "emp_leave_date", "emp_address", "emp_postal_code", "emp_comment", "emp_email", "create_user_id", "create_ymd", "create_hms", "update_user_id", "update_ymd", "update_hms", "update_screen_id", "update_ip_address", "nr_update_flg", "nr_ymd", "nr_hms", "job_catagory_name", "md", "md_user_id") VALUES ('CK-HCM036558', 453, 'SG0243', '100041', 'e10adc3949ba59abbe56e057f20f883e', '10', 'Đoàn Thị Kiều Anh', '01', '', '', '', '', '', '', '', '', '', '', 'SG0243.sm@circlek.com.vn   lyz', NULL, NULL, NULL, 'system', '20210616', '075031', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
