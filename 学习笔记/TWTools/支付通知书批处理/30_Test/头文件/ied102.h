struct ied102 {
double         payment_no;    /* payment_no               double */
char       payment_type[2];    /* payment_type             c[2]   */
char         payment_kb[2];    /* payment_kb               c[2]   */
char             vendor[7];    /* vendor                   c[7]   */
int       recieve_date;    /* recieve_date             long   */
int      delivery_date;    /* delivery_date            long   */
char       po_number_mg[11];    /* po_number_mg             c[11]  */
int          data_date;    /* data_date                long   */
char            po_type[4];    /* po_type                  c[4]   */
char           store_mg[4];    /* store_mg                 c[4]   */
char                dpt[4];    /* dpt                      c[4]   */
int       buy_tax_rate;    /* buy_tax_rate             long   */
char           tax_type[2];    /* tax_type                 c[2]   */
double            payment;    /* payment                  double */
double     payment_nontax;    /* payment_nontax           double */
double        payment_tax;    /* payment_tax              double */
int          line_nbrs;    /* line_nbrs                long   */
double             id_num;    /* id_num                   double */
char           star_flg[2];    /* star_flg                 c[2]   */
char             fix_20[21];    /* fix_20                   c[21]  */
};

