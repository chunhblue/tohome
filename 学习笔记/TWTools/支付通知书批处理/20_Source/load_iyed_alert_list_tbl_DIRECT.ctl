OPTIONS (errors=0,silent=feedback)
UNRECOVERABLE
LOAD DATA
infile 'edi_pay_deno_t.dat' "FIX 89 "
INSERT
PRESERVE BLANKS
INTO TABLE @TBL_01
(
      payment_no                        POSITION (01:08)    DOUBLE,
      payment_type                      POSITION (09:09)    CHAR,
      vendor                            POSITION (10:15)    CHAR,
      regist_no                         POSITION (16:35)    CHAR,
      grand_div                         POSITION (36:38)    CHAR,
      payment                           POSITION (39:46)    DOUBLE,
      payment_nontax                    POSITION (47:54)    DOUBLE,
      payment_tax                       POSITION (55:62)    DOUBLE,
      buy_tax_rate                      POSITION (63:66)    INTEGER,
      tax_type                          POSITION (67:67)    CHAR,
      receive_date                      POSITION (68:71)    INTEGER,
      delivery_date                     POSITION (72:75)    INTEGER,
      accounts                          POSITION (76:83)    DOUBLE,
      shori_date                        POSITION (84:87)    INTEGER,
      alert_flg                         POSITION (88:88)    CHAR
)
