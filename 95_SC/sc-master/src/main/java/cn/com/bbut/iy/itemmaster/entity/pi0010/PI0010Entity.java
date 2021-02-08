package cn.com.bbut.iy.itemmaster.entity.pi0010;

/**
 * Created by lz Cotter on 2019/12/30.
 */
public class PI0010Entity {
    private String accDate;//盘点计划日期
    private String depCd;//部门编号
    private String depName;//部门名称
    private String pmaCd;//大分类编号
    private String pmaName;//大分类名称
    private String categoryCd;//中分类编号
    private String categoryName;//中分类名称
    private String subCategoryCd;//小分类编号
    private String subCategoryName;//小分类名称
//    private Integer count;//
    private Integer articleCount;//品项数
    private Integer firstQty;//初盘品项数
    private Integer secondQty;//复盘品项数
    private Integer noQty;//未盘品项数
//    private String piType;//盘点发起方
//    private String piAccountFlg;//是否扎帐flg
    private String piAccountFlgName;//是否扎帐flgName
//    private String piFirstFinishFlg;//是否初盘完成flg
    private String piFirstFinishFlgName;//是否初盘完成flgName
//    private String piCommitFlg;//是否认列flg
    private String piCommitFlgName;//是否认列flgName

    public String getAccDate() {
        return accDate;
    }

    public void setAccDate(String accDate) {
        this.accDate = accDate;
    }

    public String getDepCd() {
        return depCd;
    }

    public void setDepCd(String depCd) {
        this.depCd = depCd;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getPmaCd() {
        return pmaCd;
    }

    public void setPmaCd(String pmaCd) {
        this.pmaCd = pmaCd;
    }

    public String getPmaName() {
        return pmaName;
    }

    public void setPmaName(String pmaName) {
        this.pmaName = pmaName;
    }

    public String getCategoryCd() {
        return categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryCd() {
        return subCategoryCd;
    }

    public void setSubCategoryCd(String subCategoryCd) {
        this.subCategoryCd = subCategoryCd;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    /*public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
*/
    public Integer getFirstQty() {
        return firstQty;
    }

    public void setFirstQty(Integer firstQty) {
        this.firstQty = firstQty;
    }

    public Integer getSecondQty() {
        return secondQty;
    }

    public void setSecondQty(Integer secondQty) {
        this.secondQty = secondQty;
    }

    public Integer getNoQty() {
        return noQty;
    }

    public void setNoQty(Integer noQty) {
        this.noQty = noQty;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

  /*  public String getPiType() {
        return piType;
    }

    public void setPiType(String piType) {
        this.piType = piType;
    }*/

  /*  public String getPiAccountFlg() {
        return piAccountFlg;
    }

    public void setPiAccountFlg(String piAccountFlg) {
        this.piAccountFlg = piAccountFlg;
    }*/

    public String getPiAccountFlgName() {
        return piAccountFlgName;
    }

    public void setPiAccountFlgName(String piAccountFlgName) {
        this.piAccountFlgName = piAccountFlgName;
    }

  /*  public String getPiFirstFinishFlg() {
        return piFirstFinishFlg;
    }

    public void setPiFirstFinishFlg(String piFirstFinishFlg) {
        this.piFirstFinishFlg = piFirstFinishFlg;
    }*/

    public String getPiFirstFinishFlgName() {
        return piFirstFinishFlgName;
    }

    public void setPiFirstFinishFlgName(String piFirstFinishFlgName) {
        this.piFirstFinishFlgName = piFirstFinishFlgName;
    }

   /* public String getPiCommitFlg() {
        return piCommitFlg;
    }

    public void setPiCommitFlg(String piCommitFlg) {
        this.piCommitFlg = piCommitFlg;
    }
*/
    public String getPiCommitFlgName() {
        return piCommitFlgName;
    }

    public void setPiCommitFlgName(String piCommitFlgName) {
        this.piCommitFlgName = piCommitFlgName;
    }
}
