package top.wjstar.chatbot.api.application.job;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.wjstar.chatbot.api.domain.ai.IOpenAI;
import top.wjstar.chatbot.api.domain.zsxq.IZsxqApi;
import top.wjstar.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import top.wjstar.chatbot.api.domain.zsxq.model.vo.Topics;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 * 问题任务
 */
@EnableScheduling
@Configuration
public class ChatBotSchedule {

    private Logger logger = LoggerFactory.getLogger(ChatBotSchedule.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;
    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;

    @Resource
    private IOpenAI iOpenAI;

    /**
     * 1分钟执行一次轮询
     */
    @Scheduled(cron = "0 0/1 * * * * ?")
    public void run() {
        try {

            // 防止特别规律的调用 防止接口风控
            if (new Random().nextBoolean()) {
                logger.info("随机打烊中。。。");
                return;
            }

            GregorianCalendar calendar = new GregorianCalendar();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour > 22 || hour < 7) {
                logger.info("打烊时间不工作，AI 下班了！");
                return;
            }

            // 1. 检索问题
            UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
            logger.info("测试结果: {}", JSON.toJSONString(unAnsweredQuestionsAggregates));

            List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
            if (null == topics || topics.isEmpty()) {
                logger.info("本次检索未查询到待回答问题");
                return;
            }

            // 2. AI 回答一次回答一个
            Topics topic = topics.get(0);
            String answer = iOpenAI.doChatGPT(topic.getQuestion().getText().trim());
            // 3. 问题回复
            boolean status = zsxqApi.answer(groupId, cookie, topic.getTopic_id(), answer, false);
            logger.info("编号: {} 问题: {}, 答案: {}, 状态: {}", topic.getTopic_id(), topic.getQuestion().getText(), answer, status);
        } catch (Exception e) {
            logger.error("自动回答问题异常: {}", e.getMessage(), e);
        }
    }
}
