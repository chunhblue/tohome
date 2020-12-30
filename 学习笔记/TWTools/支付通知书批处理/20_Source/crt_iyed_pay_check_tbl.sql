DROP TABLE @TBL_01;

COMMIT;

whenever sqlerror exit sql.sqlcode;

CREATE TABLE @TBL_01
(     PAYMENT_NO            NUMBER(12),
      VENDOR                CHAR(6),
      PAYMENT               NUMBER(11),
      RECEIVE_DATE          NUMBER(8),
      DISQUERY_FLG          CHAR(1),
      DISQUERY_REASON       NVARCHAR2(80)
)
TABLESPACE  MD_DAILY_DATA
STORAGE( INITIAL   7M    NEXT 700K
         MINEXTENTS 1       MAXEXTENTS 99
         PCTINCREASE 0      );
COMMIT;

EXIT;
