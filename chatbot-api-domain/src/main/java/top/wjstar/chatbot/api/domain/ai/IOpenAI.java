package top.wjstar.chatbot.api.domain.ai;

import java.io.IOException;

/**
 * chatGTP openai 接口
 */
public interface IOpenAI {

    /**
     * 调用 chatGPT 接口
     *
     * @param question 问题描述
     * @return String
     * @throws IOException
     */
    String doChatGPT(String question) throws IOException;
}
