package cn.com.bbut.iy.itemmaster.entity.base;

import java.util.ArrayList;
import java.util.List;

public class IyOccupExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    protected int limitStart = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    protected int limitEnd = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    protected boolean needFoundRows = false;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public IyOccupExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
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
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public int getLimitStart() {
        return limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public int getLimitEnd() {
        return limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public void setNeedFoundRows(boolean needFoundRows) {
        this.needFoundRows=needFoundRows;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
     */
    public boolean getNeedFoundRows() {
        return needFoundRows;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
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

        public Criteria andCompanyIsNull() {
            addCriterion("COMPANY is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIsNotNull() {
            addCriterion("COMPANY is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyEqualTo(String value) {
            addCriterion("COMPANY =", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotEqualTo(String value) {
            addCriterion("COMPANY <>", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyGreaterThan(String value) {
            addCriterion("COMPANY >", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyGreaterThanOrEqualTo(String value) {
            addCriterion("COMPANY >=", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLessThan(String value) {
            addCriterion("COMPANY <", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLessThanOrEqualTo(String value) {
            addCriterion("COMPANY <=", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLike(String value) {
            addCriterion("COMPANY like", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotLike(String value) {
            addCriterion("COMPANY not like", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyIn(List<String> values) {
            addCriterion("COMPANY in", values, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotIn(List<String> values) {
            addCriterion("COMPANY not in", values, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyBetween(String value1, String value2) {
            addCriterion("COMPANY between", value1, value2, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotBetween(String value1, String value2) {
            addCriterion("COMPANY not between", value1, value2, "company");
            return (Criteria) this;
        }

        public Criteria andOccupCodeIsNull() {
            addCriterion("OCCUP_CODE is null");
            return (Criteria) this;
        }

        public Criteria andOccupCodeIsNotNull() {
            addCriterion("OCCUP_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andOccupCodeEqualTo(String value) {
            addCriterion("OCCUP_CODE =", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeNotEqualTo(String value) {
            addCriterion("OCCUP_CODE <>", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeGreaterThan(String value) {
            addCriterion("OCCUP_CODE >", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeGreaterThanOrEqualTo(String value) {
            addCriterion("OCCUP_CODE >=", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeLessThan(String value) {
            addCriterion("OCCUP_CODE <", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeLessThanOrEqualTo(String value) {
            addCriterion("OCCUP_CODE <=", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeLike(String value) {
            addCriterion("OCCUP_CODE like", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeNotLike(String value) {
            addCriterion("OCCUP_CODE not like", value, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeIn(List<String> values) {
            addCriterion("OCCUP_CODE in", values, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeNotIn(List<String> values) {
            addCriterion("OCCUP_CODE not in", values, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeBetween(String value1, String value2) {
            addCriterion("OCCUP_CODE between", value1, value2, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupCodeNotBetween(String value1, String value2) {
            addCriterion("OCCUP_CODE not between", value1, value2, "occupCode");
            return (Criteria) this;
        }

        public Criteria andOccupNameIsNull() {
            addCriterion("OCCUP_NAME is null");
            return (Criteria) this;
        }

        public Criteria andOccupNameIsNotNull() {
            addCriterion("OCCUP_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andOccupNameEqualTo(String value) {
            addCriterion("OCCUP_NAME =", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameNotEqualTo(String value) {
            addCriterion("OCCUP_NAME <>", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameGreaterThan(String value) {
            addCriterion("OCCUP_NAME >", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameGreaterThanOrEqualTo(String value) {
            addCriterion("OCCUP_NAME >=", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameLessThan(String value) {
            addCriterion("OCCUP_NAME <", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameLessThanOrEqualTo(String value) {
            addCriterion("OCCUP_NAME <=", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameLike(String value) {
            addCriterion("OCCUP_NAME like", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameNotLike(String value) {
            addCriterion("OCCUP_NAME not like", value, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameIn(List<String> values) {
            addCriterion("OCCUP_NAME in", values, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameNotIn(List<String> values) {
            addCriterion("OCCUP_NAME not in", values, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameBetween(String value1, String value2) {
            addCriterion("OCCUP_NAME between", value1, value2, "occupName");
            return (Criteria) this;
        }

        public Criteria andOccupNameNotBetween(String value1, String value2) {
            addCriterion("OCCUP_NAME not between", value1, value2, "occupName");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table IY_OCCUP
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table IY_OCCUP
     *
     * @mbg.generated
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