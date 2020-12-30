DROP   INDEX i_iyed_pay_list;

COMMIT;

whenever sqlerror exit sql.sqlcode;

CREATE INDEX i_iyed_pay_list ON iyed_pay_list
(
        payment_no ,
        payment_type,
        vendor,
        receive_date ,
        delivery_date ,
        buy_tax_rate 
)
TABLESPACE  MD_DAILY_IDX
         STORAGE( INITIAL   20M    NEXT 3M
         MINEXTENTS 1       MAXEXTENTS 10
         PCTINCREASE 0      );
COMMIT;

EXIT;
