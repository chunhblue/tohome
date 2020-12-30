struct ied108 {
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
int       recieve_date;    /* recieve_date             long   */
int      delivery_date;    /* delivery_date            long   */
double           accounts;    /* accounts                 double */
int         shori_date;    /* shori_date               long   */
char          alert_flg[2];    /* alert_flg                c[2]   */
};

