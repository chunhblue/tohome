OPTIONS (errors=0,silent=feedback)
UNRECOVERABLE
LOAD DATA
infile 'edi_pay_deno_t.dat' "FIX 105 "
INSERT
PRESERVE BLANKS
INTO TABLE @TBL_01
SORTED INDEXES (i_iyed_pay_deno)
(
      payment_no                        POSITION (01:08)    DOUBLE,
      payment_kb                        POSITION (09:09)    CHAR,
      page_nbrs                         POSITION (10:13)    INTEGER,
      line_nbrs                         POSITION (14:17)    INTEGER,
      vendor                            POSITION (18:23)    CHAR,
      po_num                            POSITION (24:33)    CHAR,
      po_date                           POSITION (37:40)    INTEGER,
      po_type                           POSITION (34:36)    CHAR,
      store_mg                          POSITION (41:43)    CHAR,
      dpt                               POSITION (44:46)    CHAR,
      buy_tax_rate                      POSITION (47:50)    INTEGER,
      payment                           POSITION (52:59)    DOUBLE,
      star_flg                          POSITION (76:76)    CHAR,
      id_num                            POSITION (77:84)    DOUBLE,
      fix                               POSITION (85:104)   CHAR
)
