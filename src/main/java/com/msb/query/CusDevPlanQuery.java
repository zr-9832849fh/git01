package com.msb.query;

import com.msb.base.BaseQuery;

/*查询类*/
public class CusDevPlanQuery extends BaseQuery {
    private Integer saleChanceId;  // 营销机会的主键[拿到主键才能获取当前营销机会对象]


    public CusDevPlanQuery() {
    }

    public CusDevPlanQuery(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }

    /**
     * 获取
     * @return saleChanceId
     */
    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    /**
     * 设置
     * @param saleChanceId
     */
    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }

    public String toString() {
        return "CusDevPlanQuery{saleChanceId = " + saleChanceId + "}";
    }
}
