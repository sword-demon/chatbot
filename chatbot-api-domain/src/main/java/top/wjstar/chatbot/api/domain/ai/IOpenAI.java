package top.wjstar.chatbot.api.domain.ai;

import java.io.IOException;

/**
 * chatGTP openai 接口
 */
public interface IOpenAI {

    String doChatGPT(String question) throws IOException;
}
