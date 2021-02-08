package cn.com.bbut.iy.itemmaster.entity.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class IymActorAssignmentRecordExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    protected int limitStart = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    protected int limitEnd = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    protected boolean needFoundRows = false;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public IymActorAssignmentRecordExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
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
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
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
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
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
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public int getLimitStart() {
        return limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public int getLimitEnd() {
        return limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public void setNeedFoundRows(boolean needFoundRows) {
        this.needFoundRows=needFoundRows;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
     *
     * @mbg.generated
     */
    public boolean getNeedFoundRows() {
        return needFoundRows;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
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

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andActorAssIdIsNull() {
            addCriterion("ACTOR_ASS_ID is null");
            return (Criteria) this;
        }

        public Criteria andActorAssIdIsNotNull() {
            addCriterion("ACTOR_ASS_ID is not null");
            return (Criteria) this;
        }

        public Criteria andActorAssIdEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID =", value, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdNotEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID <>", value, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdGreaterThan(Integer value) {
            addCriterion("ACTOR_ASS_ID >", value, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID >=", value, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdLessThan(Integer value) {
            addCriterion("ACTOR_ASS_ID <", value, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdLessThanOrEqualTo(Integer value) {
            addCriterion("ACTOR_ASS_ID <=", value, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdIn(List<Integer> values) {
            addCriterion("ACTOR_ASS_ID in", values, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdNotIn(List<Integer> values) {
            addCriterion("ACTOR_ASS_ID not in", values, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdBetween(Integer value1, Integer value2) {
            addCriterion("ACTOR_ASS_ID between", value1, value2, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andActorAssIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ACTOR_ASS_ID not between", value1, value2, "actorAssId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdIsNull() {
            addCriterion("OLD_USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andOldUserIdIsNotNull() {
            addCriterion("OLD_USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andOldUserIdEqualTo(String value) {
            addCriterion("OLD_USER_ID =", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdNotEqualTo(String value) {
            addCriterion("OLD_USER_ID <>", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdGreaterThan(String value) {
            addCriterion("OLD_USER_ID >", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("OLD_USER_ID >=", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdLessThan(String value) {
            addCriterion("OLD_USER_ID <", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdLessThanOrEqualTo(String value) {
            addCriterion("OLD_USER_ID <=", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdLike(String value) {
            addCriterion("OLD_USER_ID like", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdNotLike(String value) {
            addCriterion("OLD_USER_ID not like", value, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdIn(List<String> values) {
            addCriterion("OLD_USER_ID in", values, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdNotIn(List<String> values) {
            addCriterion("OLD_USER_ID not in", values, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdBetween(String value1, String value2) {
            addCriterion("OLD_USER_ID between", value1, value2, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andOldUserIdNotBetween(String value1, String value2) {
            addCriterion("OLD_USER_ID not between", value1, value2, "oldUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdIsNull() {
            addCriterion("NEW_USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andNewUserIdIsNotNull() {
            addCriterion("NEW_USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andNewUserIdEqualTo(String value) {
            addCriterion("NEW_USER_ID =", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdNotEqualTo(String value) {
            addCriterion("NEW_USER_ID <>", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdGreaterThan(String value) {
            addCriterion("NEW_USER_ID >", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("NEW_USER_ID >=", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdLessThan(String value) {
            addCriterion("NEW_USER_ID <", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdLessThanOrEqualTo(String value) {
            addCriterion("NEW_USER_ID <=", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdLike(String value) {
            addCriterion("NEW_USER_ID like", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdNotLike(String value) {
            addCriterion("NEW_USER_ID not like", value, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdIn(List<String> values) {
            addCriterion("NEW_USER_ID in", values, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdNotIn(List<String> values) {
            addCriterion("NEW_USER_ID not in", values, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdBetween(String value1, String value2) {
            addCriterion("NEW_USER_ID between", value1, value2, "newUserId");
            return (Criteria) this;
        }

        public Criteria andNewUserIdNotBetween(String value1, String value2) {
            addCriterion("NEW_USER_ID not between", value1, value2, "newUserId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("ROLE_ID is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("ROLE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(Integer value) {
            addCriterion("ROLE_ID =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(Integer value) {
            addCriterion("ROLE_ID <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(Integer value) {
            addCriterion("ROLE_ID >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ROLE_ID >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(Integer value) {
            addCriterion("ROLE_ID <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(Integer value) {
            addCriterion("ROLE_ID <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<Integer> values) {
            addCriterion("ROLE_ID in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<Integer> values) {
            addCriterion("ROLE_ID not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(Integer value1, Integer value2) {
            addCriterion("ROLE_ID between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ROLE_ID not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andOperationFlgIsNull() {
            addCriterion("OPERATION_FLG is null");
            return (Criteria) this;
        }

        public Criteria andOperationFlgIsNotNull() {
            addCriterion("OPERATION_FLG is not null");
            return (Criteria) this;
        }

        public Criteria andOperationFlgEqualTo(Integer value) {
            addCriterion("OPERATION_FLG =", value, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgNotEqualTo(Integer value) {
            addCriterion("OPERATION_FLG <>", value, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgGreaterThan(Integer value) {
            addCriterion("OPERATION_FLG >", value, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgGreaterThanOrEqualTo(Integer value) {
            addCriterion("OPERATION_FLG >=", value, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgLessThan(Integer value) {
            addCriterion("OPERATION_FLG <", value, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgLessThanOrEqualTo(Integer value) {
            addCriterion("OPERATION_FLG <=", value, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgIn(List<Integer> values) {
            addCriterion("OPERATION_FLG in", values, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgNotIn(List<Integer> values) {
            addCriterion("OPERATION_FLG not in", values, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgBetween(Integer value1, Integer value2) {
            addCriterion("OPERATION_FLG between", value1, value2, "operationFlg");
            return (Criteria) this;
        }

        public Criteria andOperationFlgNotBetween(Integer value1, Integer value2) {
            addCriterion("OPERATION_FLG not between", value1, value2, "operationFlg");
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

        public Criteria andStartTimeIsNull() {
            addCriterion("START_TIME is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("START_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterionForJDBCDate("START_TIME =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("START_TIME <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("START_TIME >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("START_TIME >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterionForJDBCDate("START_TIME <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("START_TIME <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterionForJDBCDate("START_TIME in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("START_TIME not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("START_TIME between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("START_TIME not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("END_TIME is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("END_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterionForJDBCDate("END_TIME =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("END_TIME <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("END_TIME >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("END_TIME >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterionForJDBCDate("END_TIME <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("END_TIME <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterionForJDBCDate("END_TIME in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("END_TIME not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("END_TIME between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("END_TIME not between", value1, value2, "endTime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
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
     * This class corresponds to the database table IY_M_ACTOR_ASSIGNMENT_RECORD
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