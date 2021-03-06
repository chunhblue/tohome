package cn.com.bbut.iy.itemmaster.entity;

import java.util.ArrayList;
import java.util.List;

public class MySubmissionsExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected int limitStart = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected int limitEnd = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected boolean needFoundRows = false;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public MySubmissionsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public int getLimitStart() {
        return limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public int getLimitEnd() {
        return limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public void setNeedFoundRows(boolean needFoundRows) {
        this.needFoundRows=needFoundRows;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public boolean getNeedFoundRows() {
        return needFoundRows;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andApprovalCategoryIsNull() {
            addCriterion("approval_category is null");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryIsNotNull() {
            addCriterion("approval_category is not null");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryEqualTo(String value) {
            addCriterion("approval_category =", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryNotEqualTo(String value) {
            addCriterion("approval_category <>", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryGreaterThan(String value) {
            addCriterion("approval_category >", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryGreaterThanOrEqualTo(String value) {
            addCriterion("approval_category >=", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryLessThan(String value) {
            addCriterion("approval_category <", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryLessThanOrEqualTo(String value) {
            addCriterion("approval_category <=", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryLike(String value) {
            addCriterion("approval_category like", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryNotLike(String value) {
            addCriterion("approval_category not like", value, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryIn(List<String> values) {
            addCriterion("approval_category in", values, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryNotIn(List<String> values) {
            addCriterion("approval_category not in", values, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryBetween(String value1, String value2) {
            addCriterion("approval_category between", value1, value2, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalCategoryNotBetween(String value1, String value2) {
            addCriterion("approval_category not between", value1, value2, "approvalCategory");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeIsNull() {
            addCriterion("approval_type is null");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeIsNotNull() {
            addCriterion("approval_type is not null");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeEqualTo(String value) {
            addCriterion("approval_type =", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeNotEqualTo(String value) {
            addCriterion("approval_type <>", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeGreaterThan(String value) {
            addCriterion("approval_type >", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeGreaterThanOrEqualTo(String value) {
            addCriterion("approval_type >=", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeLessThan(String value) {
            addCriterion("approval_type <", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeLessThanOrEqualTo(String value) {
            addCriterion("approval_type <=", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeLike(String value) {
            addCriterion("approval_type like", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeNotLike(String value) {
            addCriterion("approval_type not like", value, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeIn(List<String> values) {
            addCriterion("approval_type in", values, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeNotIn(List<String> values) {
            addCriterion("approval_type not in", values, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeBetween(String value1, String value2) {
            addCriterion("approval_type between", value1, value2, "approvalType");
            return (Criteria) this;
        }

        public Criteria andApprovalTypeNotBetween(String value1, String value2) {
            addCriterion("approval_type not between", value1, value2, "approvalType");
            return (Criteria) this;
        }

        public Criteria andDescriptionsIsNull() {
            addCriterion("descriptions is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionsIsNotNull() {
            addCriterion("descriptions is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionsEqualTo(String value) {
            addCriterion("descriptions =", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsNotEqualTo(String value) {
            addCriterion("descriptions <>", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsGreaterThan(String value) {
            addCriterion("descriptions >", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsGreaterThanOrEqualTo(String value) {
            addCriterion("descriptions >=", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsLessThan(String value) {
            addCriterion("descriptions <", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsLessThanOrEqualTo(String value) {
            addCriterion("descriptions <=", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsLike(String value) {
            addCriterion("descriptions like", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsNotLike(String value) {
            addCriterion("descriptions not like", value, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsIn(List<String> values) {
            addCriterion("descriptions in", values, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsNotIn(List<String> values) {
            addCriterion("descriptions not in", values, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsBetween(String value1, String value2) {
            addCriterion("descriptions between", value1, value2, "descriptions");
            return (Criteria) this;
        }

        public Criteria andDescriptionsNotBetween(String value1, String value2) {
            addCriterion("descriptions not between", value1, value2, "descriptions");
            return (Criteria) this;
        }

        public Criteria andNRecordidIsNull() {
            addCriterion("record_id is null");
            return (Criteria) this;
        }

        public Criteria andNRecordidIsNotNull() {
            addCriterion("record_id is not null");
            return (Criteria) this;
        }

        public Criteria andNRecordidEqualTo(Integer value) {
            addCriterion("record_id =", value, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidNotEqualTo(Integer value) {
            addCriterion("record_id <>", value, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidGreaterThan(Integer value) {
            addCriterion("record_id >", value, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidGreaterThanOrEqualTo(Integer value) {
            addCriterion("record_id >=", value, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidLessThan(Integer value) {
            addCriterion("record_id <", value, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidLessThanOrEqualTo(Integer value) {
            addCriterion("record_id <=", value, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidIn(List<Integer> values) {
            addCriterion("record_id in", values, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidNotIn(List<Integer> values) {
            addCriterion("record_id not in", values, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidBetween(Integer value1, Integer value2) {
            addCriterion("record_id between", value1, value2, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andNRecordidNotBetween(Integer value1, Integer value2) {
            addCriterion("record_id not between", value1, value2, "nRecordid");
            return (Criteria) this;
        }

        public Criteria andURLIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andURLIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andURLEqualTo(String value) {
            addCriterion("url =", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLNotEqualTo(String value) {
            addCriterion("url <>", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLGreaterThan(String value) {
            addCriterion("url >", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLLessThan(String value) {
            addCriterion("url <", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLLike(String value) {
            addCriterion("url like", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLNotLike(String value) {
            addCriterion("url not like", value, "URL");
            return (Criteria) this;
        }

        public Criteria andURLIn(List<String> values) {
            addCriterion("url in", values, "URL");
            return (Criteria) this;
        }

        public Criteria andURLNotIn(List<String> values) {
            addCriterion("url not in", values, "URL");
            return (Criteria) this;
        }

        public Criteria andURLBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "URL");
            return (Criteria) this;
        }

        public Criteria andURLNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "URL");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeIsNull() {
            addCriterion("submitter_code is null");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeIsNotNull() {
            addCriterion("submitter_code is not null");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeEqualTo(String value) {
            addCriterion("submitter_code =", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeNotEqualTo(String value) {
            addCriterion("submitter_code <>", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeGreaterThan(String value) {
            addCriterion("submitter_code >", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeGreaterThanOrEqualTo(String value) {
            addCriterion("submitter_code >=", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeLessThan(String value) {
            addCriterion("submitter_code <", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeLessThanOrEqualTo(String value) {
            addCriterion("submitter_code <=", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeLike(String value) {
            addCriterion("submitter_code like", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeNotLike(String value) {
            addCriterion("submitter_code not like", value, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeIn(List<String> values) {
            addCriterion("submitter_code in", values, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeNotIn(List<String> values) {
            addCriterion("submitter_code not in", values, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeBetween(String value1, String value2) {
            addCriterion("submitter_code between", value1, value2, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterCodeNotBetween(String value1, String value2) {
            addCriterion("submitter_code not between", value1, value2, "submitterCode");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameIsNull() {
            addCriterion("submitter_name is null");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameIsNotNull() {
            addCriterion("submitter_name is not null");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameEqualTo(String value) {
            addCriterion("submitter_name =", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameNotEqualTo(String value) {
            addCriterion("submitter_name <>", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameGreaterThan(String value) {
            addCriterion("submitter_name >", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameGreaterThanOrEqualTo(String value) {
            addCriterion("submitter_name >=", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameLessThan(String value) {
            addCriterion("submitter_name <", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameLessThanOrEqualTo(String value) {
            addCriterion("submitter_name <=", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameLike(String value) {
            addCriterion("submitter_name like", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameNotLike(String value) {
            addCriterion("submitter_name not like", value, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameIn(List<String> values) {
            addCriterion("submitter_name in", values, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameNotIn(List<String> values) {
            addCriterion("submitter_name not in", values, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameBetween(String value1, String value2) {
            addCriterion("submitter_name between", value1, value2, "submitterName");
            return (Criteria) this;
        }

        public Criteria andSubmitterNameNotBetween(String value1, String value2) {
            addCriterion("submitter_name not between", value1, value2, "submitterName");
            return (Criteria) this;
        }

        public Criteria andOperatornameIsNull() {
            addCriterion("operatorname is null");
            return (Criteria) this;
        }

        public Criteria andOperatornameIsNotNull() {
            addCriterion("operatorname is not null");
            return (Criteria) this;
        }

        public Criteria andOperatornameEqualTo(String value) {
            addCriterion("operatorname =", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameNotEqualTo(String value) {
            addCriterion("operatorname <>", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameGreaterThan(String value) {
            addCriterion("operatorname >", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameGreaterThanOrEqualTo(String value) {
            addCriterion("operatorname >=", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameLessThan(String value) {
            addCriterion("operatorname <", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameLessThanOrEqualTo(String value) {
            addCriterion("operatorname <=", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameLike(String value) {
            addCriterion("operatorname like", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameNotLike(String value) {
            addCriterion("operatorname not like", value, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameIn(List<String> values) {
            addCriterion("operatorname in", values, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameNotIn(List<String> values) {
            addCriterion("operatorname not in", values, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameBetween(String value1, String value2) {
            addCriterion("operatorname between", value1, value2, "operatorname");
            return (Criteria) this;
        }

        public Criteria andOperatornameNotBetween(String value1, String value2) {
            addCriterion("operatorname not between", value1, value2, "operatorname");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsIsNull() {
            addCriterion("approval_records is null");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsIsNotNull() {
            addCriterion("approval_records is not null");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsEqualTo(String value) {
            addCriterion("approval_records =", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsNotEqualTo(String value) {
            addCriterion("approval_records <>", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsGreaterThan(String value) {
            addCriterion("approval_records >", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsGreaterThanOrEqualTo(String value) {
            addCriterion("approval_records >=", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsLessThan(String value) {
            addCriterion("approval_records <", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsLessThanOrEqualTo(String value) {
            addCriterion("approval_records <=", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsLike(String value) {
            addCriterion("approval_records like", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsNotLike(String value) {
            addCriterion("approval_records not like", value, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsIn(List<String> values) {
            addCriterion("approval_records in", values, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsNotIn(List<String> values) {
            addCriterion("approval_records not in", values, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsBetween(String value1, String value2) {
            addCriterion("approval_records between", value1, value2, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApproval_recordsNotBetween(String value1, String value2) {
            addCriterion("approval_records not between", value1, value2, "approval_records");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusIsNull() {
            addCriterion("approval_status is null");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusIsNotNull() {
            addCriterion("approval_status is not null");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusEqualTo(String value) {
            addCriterion("approval_status =", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusNotEqualTo(String value) {
            addCriterion("approval_status <>", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusGreaterThan(String value) {
            addCriterion("approval_status >", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusGreaterThanOrEqualTo(String value) {
            addCriterion("approval_status >=", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusLessThan(String value) {
            addCriterion("approval_status <", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusLessThanOrEqualTo(String value) {
            addCriterion("approval_status <=", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusLike(String value) {
            addCriterion("approval_status like", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusNotLike(String value) {
            addCriterion("approval_status not like", value, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusIn(List<String> values) {
            addCriterion("approval_status in", values, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusNotIn(List<String> values) {
            addCriterion("approval_status not in", values, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusBetween(String value1, String value2) {
            addCriterion("approval_status between", value1, value2, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andApprovalStatusNotBetween(String value1, String value2) {
            addCriterion("approval_status not between", value1, value2, "approvalStatus");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateIsNull() {
            addCriterion("submissions_date is null");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateIsNotNull() {
            addCriterion("submissions_date is not null");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateEqualTo(String value) {
            addCriterion("submissions_date =", value, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateNotEqualTo(String value) {
            addCriterion("submissions_date <>", value, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateGreaterThan(String value) {
            addCriterion("submissions_date >", value, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateGreaterThanOrEqualTo(String value) {
            addCriterion("submissions_date >=", value, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateLessThan(String value) {
            addCriterion("submissions_date <", value, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateLessThanOrEqualTo(String value) {
            addCriterion("submissions_date <=", value, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateIn(List<String> values) {
            addCriterion("submissions_date in", values, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateNotIn(List<String> values) {
            addCriterion("submissions_date not in", values, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateBetween(String value1, String value2) {
            addCriterion("submissions_date between", value1, value2, "submissionsDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionsDateNotBetween(String value1, String value2) {
            addCriterion("submissions_date not between", value1, value2, "submissionsDate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table my_submissions
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}