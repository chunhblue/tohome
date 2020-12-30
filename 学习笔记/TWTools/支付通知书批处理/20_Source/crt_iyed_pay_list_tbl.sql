DROP TABLE @TBL_01;

COMMIT;

whenever sqlerror exit sql.sqlcode;

CREATE TABLE @TBL_01
(
      payment_no             number(12),
      payment_type           char(1),
      vendor                 char(6),
      regist_no              char(20),
      grand_div              char(3),
      payment                number(11),
      payment_nontax         number(11),
      payment_tax            number(11),
      buy_tax_rate           number(5),
      tax_type               char(1),
      pay_type               char(2),
      delivery_date_real     number(8),
      check_mg               char(10),
      receive_date           number(8),
      delivery_date          number(8),
      accounts               number(11),
      realpay                number(11),
      shori_date             number(8),
      account_flg            char(1),
      zero_flg               char(1),
      minus_flg              char(1),
      disquery_flg           char(1),
      print_flg              char(1),
      pay_flg                char(1),
      pay_po_id              number(8),
      disquery_reason        nvarchar2(80),
      store_mg               char(3)
)
TABLESPACE  MD_DAILY_DATA
STORAGE( INITIAL   7M    NEXT 700K
         MINEXTENTS 1       MAXEXTENTS 99
         PCTINCREASE 0      );
COMMIT;

EXIT;
