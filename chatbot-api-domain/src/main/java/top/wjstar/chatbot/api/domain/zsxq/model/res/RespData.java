package top.wjstar.chatbot.api.domain.zsxq.model.res;

import top.wjstar.chatbot.api.domain.zsxq.model.vo.Topics;

import java.util.List;

/**
 * 响应结果数据
 */
public class RespData {
    private List<Topics> topics;

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
    }
}
