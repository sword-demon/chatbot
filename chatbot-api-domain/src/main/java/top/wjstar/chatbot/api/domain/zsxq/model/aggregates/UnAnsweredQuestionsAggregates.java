package top.wjstar.chatbot.api.domain.zsxq.model.aggregates;

import top.wjstar.chatbot.api.domain.zsxq.model.res.RespData;

/**
 * 未回答的问题的聚合信息
 */
public class UnAnsweredQuestionsAggregates {

    private boolean successed;
    private RespData resp_data; // 和知识星球的响应的接口的参数一样

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public RespData getResp_data() {
        return resp_data;
    }

    public void setResp_data(RespData resp_data) {
        this.resp_data = resp_data;
    }
}
