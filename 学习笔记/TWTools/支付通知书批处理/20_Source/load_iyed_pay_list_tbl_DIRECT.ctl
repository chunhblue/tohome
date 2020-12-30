OPTIONS (errors=0,silent=feedback)
UNRECOVERABLE
LOAD DATA
infile 'edi_pay_list_t.dat' "FIX 205 "
INSERT
PRESERVE BLANKS
INTO TABLE @TBL_01
SORTED INDEXES (i_iyed_pay_list)
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
      pay_type                          POSITION (68:69)    CHAR,
      delivery_date_real                POSITION (70:73)    INTEGER,
      check_mg                          POSITION (74:83)    CHAR,
      receive_date                      POSITION (84:87)    INTEGER,
      delivery_date                     POSITION (88:91)    INTEGER,
      accounts                          POSITION (92:99)    DOUBLE,
      realpay                           POSITION (100:107)  DOUBLE,
      shori_date                        POSITION (108:111)  INTEGER,
      account_flg                       POSITION (112:112)  CHAR,
      zero_flg                          POSITION (113:113)  CHAR,
      minus_flg                         POSITION (114:114)  CHAR,
      disquery_flg                      POSITION (115:115)  CHAR,
      print_flg                         POSITION (116:116)  CHAR,
      pay_flg                           POSITION (117:117)  CHAR,
      pay_po_id                         POSITION (118:121)  INTEGER,
      disquery_reason                   POSITION (122:201)  CHAR,
      store_mg                          POSITION (202:204)  CHAR
)
