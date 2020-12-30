struct ied020 {
char            company[4]; 
char             vendor[7]; 
char          regist_no[21]; 
int          pay_po_id; 
char          tax_po_no[16]; 
char        tax_po_type[4]; 
int          data_date; 
double nontaxable_cost_amnt; 
int           tax_rate; 
double      cost_tax_amnt; 
double  taxable_cost_amnt; 
char          state_flg[2]; 
char             bm_flg[2]; 
char     simple_err_flg[2]; 
char            note_40[41]; 
char   online_date_time[16]; 
int       recieve_date; 
char           store_mg[4];    /* store_mg            c[4]  */
};

