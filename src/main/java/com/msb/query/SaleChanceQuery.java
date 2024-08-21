package com.msb.query;

import com.msb.base.BaseQuery;

// 营销机会的查询类（营销机会管理 和 客户开发计划 都用这个查询类）
public class SaleChanceQuery extends BaseQuery {
    /*营销机会管理 条件查询*/
    private String customerName;  // 客户名
    private String createMan;  // 创建人
    private Integer state;   // 分配状态 0 = 未分配   1= 已发呢配

    /*客户开发计划 条件查询*/
    private String devResult;  // 开发状态
    private Integer assignMan; // 指派人


    public SaleChanceQuery() {
    }

    public SaleChanceQuery(String customerName, String createMan, Integer state, String devResult, Integer assignMan) {
        this.customerName = customerName;
        this.createMan = createMan;
        this.state = state;
        this.devResult = devResult;
        this.assignMan = assignMan;
    }

    /**
     * 获取
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取
     * @return createMan
     */
    public String getCreateMan() {
        return createMan;
    }

    /**
     * 设置
     * @param createMan
     */
    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    /**
     * 获取
     * @return state
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取
     * @return devResult
     */
    public String getDevResult() {
        return devResult;
    }

    /**
     * 设置
     * @param devResult
     */
    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    /**
     * 获取
     * @return assignMan
     */
    public Integer getAssignMan() {
        return assignMan;
    }

    /**
     * 设置
     * @param assignMan
     */
    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public String toString() {
        return "SaleChanceQuery{customerName = " + customerName + ", createMan = " + createMan + ", state = " + state + ", devResult = " + devResult + ", assignMan = " + assignMan + "}";
    }
}
