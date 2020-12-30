DROP TABLE @TBL_01;

COMMIT;

whenever sqlerror exit sql.sqlcode;

CREATE TABLE @TBL_01
(
  company      char(3),
  vendor       char(6), 
  regist_no    char(20),
  pay_po_id    number(8),
  tax_po_no    char(15),
  tax_po_type  char(3),
  data_date    number(8),
  nontaxable_cost_amount  number(15),
  tax_rate     number(5),
  tax_amount   number(10),
  taxable_cost_amount number(15),
  state_flag   char(1),
  bm_flag      char(1),
  error_flag   char(1),
  note         nvarchar2(40),
  online_date_time char(15),
  receive_date number(8),
  store_mg     char(3)
    )
TABLESPACE MD_DAILY_DATA
STORAGE( INITIAL     20M   NEXT 10M
 MINEXTENTS 1       MAXEXTENTS 99
 PCTINCREASE 0      );
 COMMIT;
 EXIT;
