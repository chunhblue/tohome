struct ied104 {
double         payment_no;    /* payment_no               double */
char       payment_type[2];    /* payment_type             c[2]   */
char             vendor[7];    /* vendor                   c[7]   */
char          regist_no[21];    /* regist_no                c[21]  */
char          grand_div[4];    /* grand_div                c[4]   */
double            payment;    /* payment                  double */
double     payment_nontax;    /* payment_nontax           double */
double        payment_tax;    /* payment_tax              double */
int       buy_tax_rate;    /* buy_tax_rate             long   */
char           tax_type[2];    /* tax_type                 c[2]   */
char           pay_type[3];    /* pay_type                 c[3]   */
int delivery_date_real;    /* delivery_date_real       long   */
char           check_mg[11];    /* check_mg                 c[11]  */
int       recieve_date;    /* recieve_date             long   */
int      delivery_date;    /* delivery_date            long   */
double           accounts;    /* accounts                 double */
double            realpay;    /* realpay                  double */
int         shori_date;    /* shori_date               long   */
char        account_flg[2];    /* account_flg              c[2]   */
char           zero_flg[2];    /* zero_flg                 c[2]   */
char          minus_flg[2];    /* minus_flg                c[2]   */
char       disquery_flg[2];    /* disquery_flg             c[2]   */
char          print_flg[2];    /* print_flg                c[2]   */
char            pay_flg[2];    /* pay_flg                  c[2]   */
int          pay_po_id;    /* pay_po_id                long   */
char    disquery_reason[81];    /* disquery_reason          c[81]  */
char           store_mg[4];    /* store_mg                 c[4]   */
};

