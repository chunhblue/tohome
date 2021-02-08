package cn.com.bbut.iy.itemmaster.entity;

import java.util.ArrayList;
import java.util.List;

public class MyMessageExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_message
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_message
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_message
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_message
     *
     * @mbggenerated
     */
    protected int limitStart = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_message
     *
     * @mbggenerated
     */
    protected int limitEnd = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table my_message
     *
     * @mbggenerated
     */
    protected boolean needFoundRows = false;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public MyMessageExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
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
     * This method corresponds to the database table my_message
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
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
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
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public int getLimitStart() {
        return limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public int getLimitEnd() {
        return limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public void setNeedFoundRows(boolean needFoundRows) {
        this.needFoundRows=needFoundRows;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_message
     *
     * @mbggenerated
     */
    public boolean getNeedFoundRows() {
        return needFoundRows;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table my_message
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

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andSubmitterIsNull() {
            addCriterion("submitter_code is null");
            return (Criteria) this;
        }

        public Criteria andSubmitterIsNotNull() {
            addCriterion("submitter_code is not null");
            return (Criteria) this;
        }

        public Criteria andSubmitterEqualTo(String value) {
            addCriterion("submitter_code =", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterNotEqualTo(String value) {
            addCriterion("submitter_code <>", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterGreaterThan(String value) {
            addCriterion("submitter_code >", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterGreaterThanOrEqualTo(String value) {
            addCriterion("submitter_code >=", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterLessThan(String value) {
            addCriterion("submitter_code <", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterLessThanOrEqualTo(String value) {
            addCriterion("submitter_code <=", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterLike(String value) {
            addCriterion("submitter_code like", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterNotLike(String value) {
            addCriterion("submitter_code not like", value, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterIn(List<String> values) {
            addCriterion("submitter_code in", values, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterNotIn(List<String> values) {
            addCriterion("submitter_code not in", values, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterBetween(String value1, String value2) {
            addCriterion("submitter_code between", value1, value2, "submitter");
            return (Criteria) this;
        }

        public Criteria andSubmitterNotBetween(String value1, String value2) {
            addCriterion("submitter_code not between", value1, value2, "submitter");
            return (Criteria) this;
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

        public Criteria andSubmissionDateIsNull() {
            addCriterion("submission_date is null");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateIsNotNull() {
            addCriterion("submission_date is not null");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateEqualTo(String value) {
            addCriterion("submission_date =", value, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateNotEqualTo(String value) {
            addCriterion("submission_date <>", value, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateGreaterThan(String value) {
            addCriterion("submission_date >", value, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateGreaterThanOrEqualTo(String value) {
            addCriterion("submission_date >=", value, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateLessThan(String value) {
            addCriterion("submission_date <", value, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateLessThanOrEqualTo(String value) {
            addCriterion("submission_date <=", value, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateIn(List<String> values) {
            addCriterion("submission_date in", values, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateNotIn(List<String> values) {
            addCriterion("submission_date not in", values, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateBetween(String value1, String value2) {
            addCriterion("submission_date between", value1, value2, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andSubmissionDateNotBetween(String value1, String value2) {
            addCriterion("submission_date not between", value1, value2, "submissionDate");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsIsNull() {
            addCriterion("approval_records is null");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsIsNotNull() {
            addCriterion("approval_records is not null");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsEqualTo(String value) {
            addCriterion("approval_records =", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsNotEqualTo(String value) {
            addCriterion("approval_records <>", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsGreaterThan(String value) {
            addCriterion("approval_records >", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsGreaterThanOrEqualTo(String value) {
            addCriterion("approval_records >=", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsLessThan(String value) {
            addCriterion("approval_records <", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsLessThanOrEqualTo(String value) {
            addCriterion("approval_records <=", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsLike(String value) {
            addCriterion("approval_records like", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsNotLike(String value) {
            addCriterion("approval_records not like", value, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsIn(List<String> values) {
            addCriterion("approval_records in", values, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsNotIn(List<String> values) {
            addCriterion("approval_records not in", values, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsBetween(String value1, String value2) {
            addCriterion("approval_records between", value1, value2, "approvalRecords");
            return (Criteria) this;
        }

        public Criteria andApprovalRecordsNotBetween(String value1, String value2) {
            addCriterion("approval_records not between", value1, value2, "approvalRecords");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table my_message
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
     * This class corresponds to the database table my_message
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