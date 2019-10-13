package com.fehead.yiban_interface.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.yiban_interface.dao.TokenMapper;
import com.fehead.yiban_interface.dao.dataobject.TokenDO;
import com.fehead.yiban_interface.error.BusinessException;
import com.fehead.yiban_interface.error.EmBusinessError;
import com.fehead.yiban_interface.model.TokenModel;
import com.fehead.yiban_interface.util.PostUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 写代码 敲快乐
 * だからよ...止まるんじゃねぇぞ
 * ▏n
 * █▏　､⺍
 * █▏ ⺰ʷʷｨ
 * █◣▄██◣
 * ◥██████▋
 * 　◥████ █▎
 * 　　███▉ █▎
 * 　◢████◣⌠ₘ℩
 * 　　██◥█◣\≫
 * 　　██　◥█◣
 * 　　█▉　　█▊
 * 　　█▊　　█▊
 * 　　█▊　　█▋
 * 　　 █▏　　█▙
 * 　　 █
 *
 * @author Nightnessss 2019/10/13 15:07
 */
@Controller
@RequestMapping("/page")
public class PageController extends BaseController {

    private static final String clientId = "f7fb6eeea56e6c2c";

    private static final String appSecret = "7d4c12146e5cf2104f99d3f5de1bf65f";

//    private static final String redirectUri = "http://127.0.0.1:8018/page/oauth";
    private static final String redirectUri = "http://nightnessss.cn:8018/page/oauth";

    Logger logger = LoggerFactory.getLogger(PageController.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PostUtil postUtil;

    @Autowired
    private TokenMapper tokenMapper;

    /**
     * 获取access_token
     * @param request  首次调用时有参数 callback 传入，即回调地址
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/oauth")
    public ModelAndView oauth_(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String callback = request.getParameter("callback");
        // 判断回调地址非空，将回调地址写入session
        if (callback != null) {
            logger.info("PARAM: callback{" + callback + "}");
            request.getSession().setAttribute("callback", callback);
        }


        /*
         * 判断有传入参数有没有code
         * 如果没有，则请求code，并回调本地址
         * 有则请求access_token
         */
        String code = request.getParameter("code");
        logger.info("code: " + code);
        String accessToken = request.getParameter("access_token");
        if (code==null) {
            response.sendRedirect("https://openapi.yiban.cn" +
                    "/oauth/authorize?client_id="+clientId+"&redirect_uri="+redirectUri);

            return null;
        } else {
            if (accessToken==null) {

                String getTokenUrl = "https://openapi.yiban.cn/oauth/access_token";

                // post请求时所带的参数
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                formParams.add(new BasicNameValuePair("client_id", clientId));
                formParams.add(new BasicNameValuePair("client_secret", appSecret));
                formParams.add(new BasicNameValuePair("code", code));
                formParams.add(new BasicNameValuePair("redirect_uri", redirectUri));

                // s为请求返回值
                String s = postUtil.sendPost(getTokenUrl, formParams);

                // 将请求返回值封装为 TokenModel 对象
                TokenModel tokenModel = new TokenModel();
                try {
                    tokenModel = mapper.readValue(s, TokenModel.class);
                } catch (Exception e) {
                    response.sendRedirect("https://openapi.yiban.cn" +
                            "/oauth/authorize?client_id="+clientId+"&redirect_uri="+redirectUri);
                    return null;
                }

                // 因为请求错误返回的参数为 {"status": 404...} 的格式，通过token的值是否为空判断
                if (tokenModel.getAccess_token() == null) {
                    throw new BusinessException(EmBusinessError.ACCESS_TOKEN_GET_ERROR);
                }

                accessToken = tokenModel.getAccess_token();
                logger.info(tokenModel.toString());

                // 将用户的 token 信息存入数据库
                if (!tokenModel.getUserid().isEmpty()) {
                    TokenDO tokenDO = tokenMapper.selectByUserId(tokenModel.getUserid());

                    if (tokenDO == null) {
                        TokenDO tokenDO1 = new TokenDO();
                        tokenDO1.setUserId(tokenModel.getUserid());
                        tokenDO1.setAccessToken(tokenModel.getAccess_token());
                        tokenDO1.setExpires(tokenModel.getExpires());

                        tokenMapper.insert(tokenDO1);
                    } else {
                        tokenMapper.updateToken(tokenModel.getUserid(), tokenModel.getAccess_token(), tokenModel.getExpires());
                    }
                }
            }
        }

        // 从 session 获取 callback，带着 access_token 访问 callback
        callback = (String) request.getSession().getAttribute("callback");
        String target = callback + "?access_token=" + accessToken;
        logger.info("发送请求：" + target);

        return new ModelAndView(new RedirectView(target));

    }
}
