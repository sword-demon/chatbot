package top.wjstar.chatbot.api.application.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;
import org.springframework.util.StringUtils;
import top.wjstar.chatbot.api.application.job.ChatbotTask;
import top.wjstar.chatbot.api.common.PropertyUtil;
import top.wjstar.chatbot.api.domain.ai.IOpenAI;
import top.wjstar.chatbot.api.domain.zsxq.IZsxqApi;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EnableScheduling
@Configuration
public class TaskRegistrarAutoConfig implements EnvironmentAware, SchedulingConfigurer {

    private Logger logger = LoggerFactory.getLogger(TaskRegistrarAutoConfig.class);

    /**
     * 任务配置组
     */
    private Map<String, Map<String, Object>> taskGroupMap = new HashMap<>();

    @Resource
    private IZsxqApi iZsxqApi;

    @Resource
    private IOpenAI openAI;

    /**
     * 读取自定义配置并解析
     * Set the {@code Environment} that this component runs in.
     *
     * @param environment Environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "chatbot-api";
        String launchListStr = environment.getProperty(prefix + "launchList");
        if (StringUtils.isEmpty(launchListStr)) return;
        for (String groupKey : launchListStr.split(",")) {
            Map<String, Object> taskGroupPros = PropertyUtil.handle(environment, prefix + groupKey, Map.class);
            taskGroupMap.put(groupKey, taskGroupPros);
        }
    }

    /**
     * Callback allowing a {@link TaskScheduler
     * TaskScheduler} and specific {@link Task Task}
     * instances to be registered against the given the {@link ScheduledTaskRegistrar}.
     *
     * @param taskRegistrar the registrar to be configured.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Set<String> taskGroups = taskGroupMap.keySet();
        for (String groupKey : taskGroups) {
            Map<String, Object> taskGroup = taskGroupMap.get(groupKey);
            String groupName = taskGroup.get("groupName").toString();
            String groupId = taskGroup.get("groupId").toString();
            String cookie = taskGroup.get("cookie").toString();
            String openAiKey = taskGroup.get("openAiKey").toString();
            String cronExpressionBase64 = taskGroup.get("cronExpression").toString();
            String cronExpression = new String(Base64.getDecoder().decode(cronExpressionBase64), StandardCharsets.UTF_8);
            logger.info("创建任务: groupName: {}, groupId: {}, cronExpression: {}", groupName, groupId, cronExpression);
            // 添加任务
            taskRegistrar.addCronTask(new ChatbotTask(groupName, groupId, cookie, openAiKey, iZsxqApi, openAI, false), cronExpression);
        }
    }
}
