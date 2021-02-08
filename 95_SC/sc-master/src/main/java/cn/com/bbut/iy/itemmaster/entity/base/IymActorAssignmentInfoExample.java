package cn.com.bbut.iy.itemmaster.entity.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class IymActorAssignmentInfoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    protected int limitStart = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    protected int limitEnd = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    protected boolean needFoundRows = false;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public IymActorAssignmentInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
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
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
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
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
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
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public int getLimitStart() {
        return limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public int getLimitEnd() {
        return limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public void setNeedFoundRows(boolean needFoundRows) {
        this.needFoundRows=needFoundRows;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
     *
     * @mbg.generated
     */
    public boolean getNeedFoundRows() {
        return needFoundRows;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("ACTOR_ASS_ID is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("ACTOR_ASS_ID is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(Integer value) {
            addCriterion("ACTOR_ASS_ID >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(Integer value) {
            addCriterion("ACTOR_ASS_ID <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<Integer> values) {
            addCriterion("ACTOR_ASS_ID in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<Integer> values) {
            addCriterion("ACTOR_ASS_ID not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(Integer value1, Integer value2) {
            addCriterion("ACTOR_ASS_ID between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ACTOR_ASS_ID not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andFlgIsNull() {
            addCriterion("FLG is null");
            return (Criteria) this;
        }

        public Criteria andFlgIsNotNull() {
            addCriterion("FLG is not null");
            return (Criteria) this;
        }

        public Criteria andFlgEqualTo(Integer value) {
            addCriterion("FLG =", value, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgNotEqualTo(Integer value) {
            addCriterion("FLG <>", value, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgGreaterThan(Integer value) {
            addCriterion("FLG >", value, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("FLG >=", value, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgLessThan(Integer value) {
            addCriterion("FLG <", value, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgLessThanOrEqualTo(Integer value) {
            addCriterion("FLG <=", value, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgIn(List<Integer> values) {
            addCriterion("FLG in", values, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgNotIn(List<Integer> values) {
            addCriterion("FLG not in", values, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgBetween(Integer value1, Integer value2) {
            addCriterion("FLG between", value1, value2, "flg");
            return (Criteria) this;
        }

        public Criteria andFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("FLG not between", value1, value2, "flg");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNull() {
            addCriterion("START_DATE is null");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNotNull() {
            addCriterion("START_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andStartDateEqualTo(Date value) {
            addCriterionForJDBCDate("START_DATE =", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("START_DATE <>", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThan(Date value) {
            addCriterionForJDBCDate("START_DATE >", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("START_DATE >=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThan(Date value) {
            addCriterionForJDBCDate("START_DATE <", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("START_DATE <=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateIn(List<Date> values) {
            addCriterionForJDBCDate("START_DATE in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("START_DATE not in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("START_DATE between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("START_DATE not between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNull() {
            addCriterion("END_DATE is null");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNotNull() {
            addCriterion("END_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andEndDateEqualTo(Date value) {
            addCriterionForJDBCDate("END_DATE =", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("END_DATE <>", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThan(Date value) {
            addCriterionForJDBCDate("END_DATE >", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("END_DATE >=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThan(Date value) {
            addCriterionForJDBCDate("END_DATE <", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("END_DATE <=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIn(List<Date> values) {
            addCriterionForJDBCDate("END_DATE in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("END_DATE not in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("END_DATE between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("END_DATE not between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterionForJDBCDate("UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterionForJDBCDate("UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterionForJDBCDate("UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridIsNull() {
            addCriterion("UPDATE_USERID is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridIsNotNull() {
            addCriterion("UPDATE_USERID is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridEqualTo(String value) {
            addCriterion("UPDATE_USERID =", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridNotEqualTo(String value) {
            addCriterion("UPDATE_USERID <>", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridGreaterThan(String value) {
            addCriterion("UPDATE_USERID >", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridGreaterThanOrEqualTo(String value) {
            addCriterion("UPDATE_USERID >=", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridLessThan(String value) {
            addCriterion("UPDATE_USERID <", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridLessThanOrEqualTo(String value) {
            addCriterion("UPDATE_USERID <=", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridLike(String value) {
            addCriterion("UPDATE_USERID like", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridNotLike(String value) {
            addCriterion("UPDATE_USERID not like", value, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridIn(List<String> values) {
            addCriterion("UPDATE_USERID in", values, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridNotIn(List<String> values) {
            addCriterion("UPDATE_USERID not in", values, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridBetween(String value1, String value2) {
            addCriterion("UPDATE_USERID between", value1, value2, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andUpdateUseridNotBetween(String value1, String value2) {
            addCriterion("UPDATE_USERID not between", value1, value2, "updateUserid");
            return (Criteria) this;
        }

        public Criteria andAssUserIdIsNull() {
            addCriterion("ASS_USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andAssUserIdIsNotNull() {
            addCriterion("ASS_USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAssUserIdEqualTo(String value) {
            addCriterion("ASS_USER_ID =", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdNotEqualTo(String value) {
            addCriterion("ASS_USER_ID <>", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdGreaterThan(String value) {
            addCriterion("ASS_USER_ID >", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("ASS_USER_ID >=", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdLessThan(String value) {
            addCriterion("ASS_USER_ID <", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdLessThanOrEqualTo(String value) {
            addCriterion("ASS_USER_ID <=", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdLike(String value) {
            addCriterion("ASS_USER_ID like", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdNotLike(String value) {
            addCriterion("ASS_USER_ID not like", value, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdIn(List<String> values) {
            addCriterion("ASS_USER_ID in", values, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdNotIn(List<String> values) {
            addCriterion("ASS_USER_ID not in", values, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdBetween(String value1, String value2) {
            addCriterion("ASS_USER_ID between", value1, value2, "assUserId");
            return (Criteria) this;
        }

        public Criteria andAssUserIdNotBetween(String value1, String value2) {
            addCriterion("ASS_USER_ID not between", value1, value2, "assUserId");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("STATUS is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("STATUS =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("STATUS <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("STATUS >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("STATUS >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("STATUS <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("STATUS <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("STATUS in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("STATUS not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("STATUS between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("STATUS not between", value1, value2, "status");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
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
     * This class corresponds to the database table IY_M_ACTOR_ASSIGNMENT_INFO
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