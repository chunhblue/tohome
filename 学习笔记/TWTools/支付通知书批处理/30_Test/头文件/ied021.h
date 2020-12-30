struct ied021 {
char        tax_po_type[4]; 
char          tax_po_no[16]; 
char        data_date_c[9]; 
char    receive_date_mg[9]; 
char             vendor[7]; 
char          regist_no[21]; 
char cost_nontax_amount_mg[12]; 
char    buy_tax_rate_mg[6]; 
char      tax_amount_mg[12]; 
char cost_tax_amount_mg[12]; 
char            user_id[8]; 
char           store_mg[4];    /* store_mg            c[4]  */
char c_r[2];
};
