struct ied100 {
char        payment_no1[11];    /* payment_no1         c[11] */
char       payment_type[2];    /* payment_type        c[2]  */
char             vendor[7];    /* vendor              c[7]  */
char    recieve_date_pc[9];    /* recieve_date_pc     c[9]  */
char   delivery_date_dc[9];    /* delivery_date_dc    c[9]  */
char       po_number_mg[11];    /* po_number_mg        c[11] */
char        data_date_c[9];    /* data_date_c         c[9]  */
char           store_mg[4];    /* store_mg            c[4]  */
char             dpt_mg[5];    /* dpt_mg              c[5]  */
char    buy_tax_rate_mg[6];    /* buy_tax_rate_mg     c[6]  */
char           tax_type[2];    /* tax_type            c[2]  */
char          drcr_type[2];    /* drcr_type           c[2]  */
char         payment_mg[12];    /* payment_mg          c[12] */
char  payment_nontax_mg[12];    /* payment_nontax_mg   c[12] */
char     payment_tax_mg[12];    /* payment_tax_mg      c[12] */
char              fix_6[7];    /* fix_6               c[7]  */
char            po_type[4];    /* po_type             c[4]  */
char        line_nbrs_c[5];    /* line_nbrs_c         c[5]  */
char           star_flg[2];    /* star_flg            c[2]  */
char     c_r[2];
};

