package com.tapioca.BE.application.service.gptCommon;

import com.tapioca.BE.application.prompt.GptResultPrompt;
import com.tapioca.BE.application.prompt.GptTypePrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MakePromptService {
    public String makeTypePrompt(String userInput){
        String prompt = GptTypePrompt.TYPE_PROMPT;
        return prompt.replace("${user_request}",userInput);
    }

    // 트래픽 테스트 Prompt
    public String makeResultPrompt(String type,String userInput,String ec2Url,String loginPath){
        String prompt= GptResultPrompt.RESULT_PROMPT;
        return prompt.replace("${type}",type)
                        .replace("${user_request}",userInput)
                        .replace("${ec2_url}",ec2Url)
                        .replace("${login_path}",loginPath);
    }
}
