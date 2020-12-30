DROP   INDEX i_iyed_pay_deno;

COMMIT;

whenever sqlerror exit sql.sqlcode;

CREATE INDEX i_iyed_pay_deno ON iyed_pay_deno
(
        payment_no,
        payment_kb,
        page_nbrs,
        line_nbrs
)
TABLESPACE  MD_DAILY_IDX
         STORAGE( INITIAL   20M    NEXT 3M
         MINEXTENTS 1       MAXEXTENTS 10
         PCTINCREASE 0      );
COMMIT;

EXIT;
