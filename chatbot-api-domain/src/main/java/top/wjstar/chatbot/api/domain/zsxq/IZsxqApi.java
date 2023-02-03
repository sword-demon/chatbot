package top.wjstar.chatbot.api.domain.zsxq;

import top.wjstar.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;

import java.io.IOException;

public interface IZsxqApi {

    /**
     * 爬取知识星球未回答的问题的 topicId
     *
     * @param groupId 知识星球的 group 案例：https://wx.zsxq.com/dweb2/index/group/28885518425541
     * @param cookie  网页版知识星球的 cookie
     * @throws IOException
     */
    UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException;

    /**
     * 回答问题
     *
     * @param groupId  知识星球的 group
     * @param cookie   网页版知识星球的 cookie
     * @param topicId  回答的问题的 topicId
     * @param text     回答的文本内容
     * @param silenced 是否全星球可见
     * @return boolean
     * @throws IOException
     */
    boolean answer(String groupId, String cookie, String topicId, String text, boolean silenced) throws IOException;
}
