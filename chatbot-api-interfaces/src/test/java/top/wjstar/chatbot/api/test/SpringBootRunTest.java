package top.wjstar.chatbot.api.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.wjstar.chatbot.api.domain.ai.IOpenAI;
import top.wjstar.chatbot.api.domain.zsxq.IZsxqApi;
import top.wjstar.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import top.wjstar.chatbot.api.domain.zsxq.model.vo.Topics;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {

    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;
    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;

    @Resource
    private IOpenAI iOpenAI;

    @Test
    public void test_zsxq_api() throws IOException {
        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
        logger.info("测试结果: {}", JSON.toJSONString(unAnsweredQuestionsAggregates));

        List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
        for (Topics topic : topics) {
            String topicId = topic.getTopic_id();
            String text = topic.getQuestion().getText();
            logger.info("topicId: {}, text: {}", topicId, text);
        }
    }

    @Test
    public void test_openai() throws IOException {
        String res = iOpenAI.doChatGPT("使用 Java 写一个冒泡排序");
        logger.info("测试结果: {}", res);
    }
}
