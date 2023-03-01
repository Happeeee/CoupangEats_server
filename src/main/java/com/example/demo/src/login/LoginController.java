package com.example.demo.src.login;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/app/accounts")
public class LoginController {
    @Autowired
    private final OAuthService oAuthService;

    public LoginController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }
    @ResponseBody
    @GetMapping("/auth/{socialLoginType}")
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType = SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(socialLoginType);
    }

    @ResponseBody
    @GetMapping("/auth/{socialLoginType}/callback")
    public BaseResponse<GetSocialOAuthRes> callback (
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code)throws IOException, BaseException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes=oAuthService.oAuthLogin(socialLoginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }

    }
