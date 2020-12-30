DROP TABLE @TBL_01;

COMMIT;

whenever sqlerror exit sql.sqlcode;

CREATE TABLE @TBL_01
(
      payment_no             number(12),
      payment_kb             char(1),
      page_nbrs              number(10),
      line_nbrs              number(10),
      vendor                 char(6),
      po_num                 char(10),
      po_date                number(8),
      po_type                char(3),
      store_mg               char(3),
      dpt                    char(3),
      buy_tax_rate           number(5),
      payment                number(11),
      star_flg               char(1),
      id_num                 number(10),
      fix                    nvarchar2(20)
)
TABLESPACE  MD_DAILY_DATA
STORAGE( INITIAL   7M    NEXT 700K
         MINEXTENTS 1       MAXEXTENTS 99
         PCTINCREASE 0      );
COMMIT;

EXIT;
