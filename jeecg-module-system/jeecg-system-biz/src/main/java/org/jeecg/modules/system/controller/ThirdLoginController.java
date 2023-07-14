package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.*;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.config.thirdapp.ThirdAppTypeItemVo;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.demo.water.constant.PromoteConstant;
import org.jeecg.modules.demo.water.entity.WaterPromoteActivity;
import org.jeecg.modules.demo.water.entity.WaterPromoteWinning;
import org.jeecg.modules.demo.water.service.IWaterPromoteActivityService;
import org.jeecg.modules.demo.water.service.IWaterPromoteWinningService;
import org.jeecg.modules.system.entity.SysThirdAccount;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.ThirdLoginModel;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysThirdAccountService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.service.impl.ThirdAppDingtalkServiceImpl;
import org.jeecg.modules.system.service.impl.ThirdAppWechatEnterpriseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author scott
 * @since 2018-12-17
 */
@Controller
@RequestMapping("/sys/thirdLogin")
@Slf4j
public class ThirdLoginController {
    @Autowired
    ThirdAppConfig thirdAppConfig;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysThirdAccountService sysThirdAccountService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private BaseCommonService baseCommonService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AuthRequestFactory factory;
    @Autowired
    private ThirdAppWechatEnterpriseServiceImpl thirdAppWechatEnterpriseService;
    @Autowired
    private ThirdAppDingtalkServiceImpl thirdAppDingtalkService;
    @Autowired
    private IWaterPromoteWinningService winningService;
    @Autowired
    private IWaterPromoteActivityService activityService;

    @ApiOperation("小程序登录")
    @RequestMapping(value = "/loginByWeixin")
    @ResponseBody
    public ThinkResult login(@RequestBody JSONObject requestJSON, HttpServletResponse response) throws IOException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?grant_type=" + "authorization_code"
                + "&js_code=" + requestJSON.get("code")
                + "&secret=" + thirdAppConfig.getWechatSmall().getClientSecret()
                + "&appid=" + thirdAppConfig.getWechatSmall().getClientId();
        String responseBody = RestUtil.request(url, HttpMethod.POST, RestUtil.getHeaderApplicationJson(), null, null, String.class).getBody();
        JSONObject body = JSONObject.parseObject(responseBody);
        if (responseBody == null || body.getString("openid") == null) {
            return ThinkResult.error("登录失败,openid失效");
        }
        String appid = body.getString("openid");
        // 判断账号是否存在
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<SysThirdAccount>();
        query.eq(SysThirdAccount::getThirdType, ThirdAppConfig.WECHAT_SMALL.toLowerCase());
        query.and(q -> q.eq(SysThirdAccount::getThirdUserUuid, appid).or().eq(SysThirdAccount::getThirdUserId, appid));
        List<SysThirdAccount> thridList = sysThirdAccountService.list(query);
        SysThirdAccount user = null;
        int is_new = 0;
        if (thridList == null || thridList.size() == 0) {
            JSONObject userInfo = requestJSON.getJSONObject("userInfo");
            if (userInfo == null) {
                return ThinkResult.error(101);
            }
            userInfo = userInfo.getJSONObject("userInfo");
            //否则直接创建新账号
            //构造第三方登录信息存储对象
            ThirdLoginModel tlm = new ThirdLoginModel(ThirdAppConfig.WECHAT_SMALL.toLowerCase(), appid, userInfo.getString("nickName"), userInfo.getString("avatarUrl"));
            //update-begin-author:wangshuai date:20201118 for:修改成查询第三方账户表
            user = sysThirdAccountService.saveThirdUser(tlm);
            is_new = 1;
        } else {
            //已存在 只设置用户名 不设置头像
            user = thridList.get(0);
        }
        SysUser sysUser;
        String token;
        // 生成token
        //update-begin-author:wangshuai date:20201118 for:从第三方登录查询是否存在用户id，不存在绑定手机号
        if (oConvertUtils.isNotEmpty(user.getSysUserId())) {
            String sysUserId = user.getSysUserId();
            sysUser = sysUserService.getById(sysUserId);
        } else {
            sysUser = sysThirdAccountService.createUser(appid);
            //            添加邀请信息
            String string = requestJSON.getString("inviteCode");
            if (StringUtils.isNotBlank(string)) {
                try {
                    JSONObject jsonObject = JSON.parseObject((String) redisUtil.get("wechat_scene_" + string));
                    if (jsonObject != null) {
                        String username = jsonObject.getString("username");
                        String activityId = jsonObject.getString("activeId");
                        if (username != null && activityId != null) {
                            WaterPromoteActivity activity = activityService.getById(activityId);
                            WaterPromoteWinning winning = new WaterPromoteWinning();
                            winning.setActivityId(activityId);
                            winning.setRegisterUserId(sysUser.getUsername());
                            winning.setUserId(username);
                            winning.setRelief(activity.getRelief());
                            winning.setShopItemId(activity.getShopItemId());
                            winning.setStatus(PromoteConstant.REGISTER);
                            winningService.save(winning);
                        }
                    }
                } catch (Exception ignore) {
                }
            }
        }
        token = JwtUtil.sign(sysUser.getUsername(), sysUser.getPassword());
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", sysUser.getId());
        jsonObject1.put("username", sysUser.getUsername());
        jsonObject1.put("nickname", sysUser.getRealname());
        jsonObject1.put("avatar", sysUser.getAvatar());
        JSONObject resultJSON = new JSONObject();
        resultJSON.put("token", token);
        resultJSON.put("is_new", is_new);
        resultJSON.put("userInfo", jsonObject1);
        return ThinkResult.ok(resultJSON);
    }

    @RequestMapping("/render/{source}")
    public void render(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        log.info("第三方登录进入render：" + source);
        AuthRequest authRequest = factory.get(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info("第三方登录认证地址：" + authorizeUrl);
        response.sendRedirect(authorizeUrl);
    }

    @RequestMapping("/{source}/callback")
    public String loginThird(@PathVariable("source") String source, AuthCallback callback, ModelMap modelMap) {
        log.info("第三方登录进入callback：" + source + " params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = factory.get(source);
        AuthResponse response = authRequest.login(callback);
        log.info(JSONObject.toJSONString(response));
        Result<JSONObject> result = new Result<JSONObject>();
        if (response.getCode() == 2000) {

            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
            String username = data.getString("username");
            String avatar = data.getString("avatar");
            String uuid = data.getString("uuid");
            //构造第三方登录信息存储对象
            ThirdLoginModel tlm = new ThirdLoginModel(source, uuid, username, avatar);
            //判断有没有这个人
            //update-begin-author:wangshuai date:20201118 for:修改成查询第三方账户表
            LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<SysThirdAccount>();
            query.eq(SysThirdAccount::getThirdType, source);
            query.and(q -> q.eq(SysThirdAccount::getThirdUserUuid, uuid).or().eq(SysThirdAccount::getThirdUserId, uuid));
            List<SysThirdAccount> thridList = sysThirdAccountService.list(query);
            SysThirdAccount user = null;
            if (thridList == null || thridList.size() == 0) {
                //否则直接创建新账号
                user = sysThirdAccountService.saveThirdUser(tlm);
            } else {
                //已存在 只设置用户名 不设置头像
                user = thridList.get(0);
            }
            // 生成token
            //update-begin-author:wangshuai date:20201118 for:从第三方登录查询是否存在用户id，不存在绑定手机号
            if (oConvertUtils.isNotEmpty(user.getSysUserId())) {
                String sysUserId = user.getSysUserId();
                SysUser sysUser = sysUserService.getById(sysUserId);
                String token = saveToken(sysUser);
                modelMap.addAttribute("token", token);
            } else {
                modelMap.addAttribute("token", "绑定手机号," + "" + uuid);
            }
            //update-end-author:wangshuai date:20201118 for:从第三方登录查询是否存在用户id，不存在绑定手机号
            //update-begin--Author:wangshuai  Date:20200729 for：接口在签名校验失败时返回失败的标识码 issues#1441--------------------
        } else {
            modelMap.addAttribute("token", "登录失败");
        }
        //update-end--Author:wangshuai  Date:20200729 for：接口在签名校验失败时返回失败的标识码 issues#1441--------------------
        result.setSuccess(false);
        result.setMessage("第三方登录异常,请联系管理员");
        return "thirdLogin";
    }

    /**
     * 创建新账号
     *
     * @param model
     * @return
     */
    @PostMapping("/user/create")
    @ResponseBody
    public Result<String> thirdUserCreate(@RequestBody ThirdLoginModel model) {
        log.info("第三方登录创建新账号：");
        Result<String> res = new Result<>();
        Object operateCode = redisUtil.get(CommonConstant.THIRD_LOGIN_CODE);
        if (operateCode == null || !operateCode.toString().equals(model.getOperateCode())) {
            res.setSuccess(false);
            res.setMessage("校验失败");
            return res;
        }
        //创建新账号
        //update-begin-author:wangshuai date:20201118 for:修改成从第三方登录查出来的user_id，在查询用户表尽行token
        SysThirdAccount user = sysThirdAccountService.saveThirdUser(model);
        if (oConvertUtils.isNotEmpty(user.getSysUserId())) {
            String sysUserId = user.getSysUserId();
            SysUser sysUser = sysUserService.getById(sysUserId);
            // 生成token
            String token = saveToken(sysUser);
            //update-end-author:wangshuai date:20201118 for:修改成从第三方登录查出来的user_id，在查询用户表尽行token
            res.setResult(token);
            res.setSuccess(true);
        }
        return res;
    }

    /**
     * 绑定账号 需要设置密码 需要走一遍校验
     *
     * @param json
     * @return
     */
    @PostMapping("/user/checkPassword")
    @ResponseBody
    public Result<String> checkPassword(@RequestBody JSONObject json) {
        Result<String> result = new Result<>();
        Object operateCode = redisUtil.get(CommonConstant.THIRD_LOGIN_CODE);
        if (operateCode == null || !operateCode.toString().equals(json.getString("operateCode"))) {
            result.setSuccess(false);
            result.setMessage("校验失败");
            return result;
        }
        String username = json.getString("uuid");
        SysUser user = this.sysUserService.getUserByName(username);
        if (user == null) {
            result.setMessage("用户未找到");
            result.setSuccess(false);
            return result;
        }
        String password = json.getString("password");
        String salt = user.getSalt();
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(), password, salt);
        if (!passwordEncode.equals(user.getPassword())) {
            result.setMessage("密码不正确");
            result.setSuccess(false);
            return result;
        }

        sysUserService.updateById(user);
        result.setSuccess(true);
        // 生成token
        String token = saveToken(user);
        result.setResult(token);
        return result;
    }

    private String saveToken(SysUser user) {
        // 生成token
        String token = JwtUtil.sign(user.getUsername(), user.getPassword());
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        // 设置超时时间
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        return token;
    }

    /**
     * 第三方登录回调接口
     *
     * @param token
     * @param thirdType
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getLoginUser/{token}/{thirdType}", method = RequestMethod.GET)
    @ResponseBody
    public Result<JSONObject> getThirdLoginUser(@PathVariable("token") String token, @PathVariable("thirdType") String thirdType) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = JwtUtil.getUsername(token);

        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }
        //update-begin-author:wangshuai date:20201118 for:如果真实姓名和头像不存在就取第三方登录的
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getSysUserId, sysUser.getId());
        query.eq(SysThirdAccount::getThirdType, thirdType);
        SysThirdAccount account = sysThirdAccountService.getOne(query);
        if (oConvertUtils.isEmpty(sysUser.getRealname())) {
            sysUser.setRealname(account.getRealname());
        }
        if (oConvertUtils.isEmpty(sysUser.getAvatar())) {
            sysUser.setAvatar(account.getAvatar());
        }
        //update-end-author:wangshuai date:20201118 for:如果真实姓名和头像不存在就取第三方登录的
        JSONObject obj = new JSONObject();
        //TODO 第三方登确定登录租户和部门逻辑

        //用户登录信息
        obj.put("userInfo", sysUser);
        //获取字典缓存【解决 #jeecg-boot/issues/3998】
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        //token 信息
        obj.put("token", token);
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        baseCommonService.addLog("用户名: " + username + ",登录成功[第三方用户]！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }

    /**
     * 第三方绑定手机号返回token
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation("手机号登录接口")
    @PostMapping("/bindingThirdPhone")
    @ResponseBody
    public Result<String> bindingThirdPhone(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String phone = jsonObject.getString("mobile");
        String thirdUserUuid = jsonObject.getString("thirdUserUuid");
        // 校验验证码
        String captcha = jsonObject.getString("captcha");
        //update-begin-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906
        String redisKey = CommonConstant.PHONE_REDIS_KEY_PRE + phone;
        Object captchaCache = redisUtil.get(redisKey);
        //update-end-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906
        if (oConvertUtils.isEmpty(captcha) || !captcha.equals(captchaCache)) {
            result.setMessage("验证码错误");
            result.setSuccess(false);
            return result;
        }
        //校验用户有效性
        SysUser sysUser = sysUserService.getUserByPhone(phone);
        if (sysUser != null) {
            // 存在用户，直接绑定
            sysThirdAccountService.updateThirdUserId(sysUser, thirdUserUuid);
        } else {
            // 不存在手机号，创建用户
            sysUser = sysThirdAccountService.createUser(phone, thirdUserUuid);
        }
        String token = saveToken(sysUser);
        result.setSuccess(true);
        result.setResult(token);
        return result;
    }

    /**
     * 企业微信/钉钉 OAuth2登录
     *
     * @param source
     * @param state
     * @return
     */
    @ResponseBody
    @GetMapping("/oauth2/{source}/login")
    public String oauth2LoginCallback(@PathVariable("source") String source, @RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url;
        if (ThirdAppConfig.WECHAT_ENTERPRISE.equalsIgnoreCase(source)) {
            ThirdAppTypeItemVo config = thirdAppConfig.getWechatEnterprise();
            StringBuilder builder = new StringBuilder();
            // 构造企业微信OAuth2登录授权地址
            builder.append("https://open.weixin.qq.com/connect/oauth2/authorize");
            // 企业的CorpID
            builder.append("?appid=").append(config.getClientId());
            // 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
            String redirectUri = CommonUtils.getBaseUrl(request) + "/sys/thirdLogin/oauth2/wechat_enterprise/callback";
            builder.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
            // 返回类型，此时固定为：code
            builder.append("&response_type=code");
            // 应用授权作用域。
            // snsapi_base：静默授权，可获取成员的的基础信息（UserId与DeviceId）；
            builder.append("&scope=snsapi_base");
            // 重定向后会带上state参数，长度不可超过128个字节
            builder.append("&state=").append(state);
            // 终端使用此参数判断是否需要带上身份信息
            builder.append("#wechat_redirect");
            url = builder.toString();
        } else if (ThirdAppConfig.DINGTALK.equalsIgnoreCase(source)) {
            ThirdAppTypeItemVo config = thirdAppConfig.getDingtalk();
            StringBuilder builder = new StringBuilder();
            // 构造钉钉OAuth2登录授权地址
            builder.append("https://login.dingtalk.com/oauth2/auth");
            // 授权通过/拒绝后回调地址。
            // 注意 需要与注册应用时登记的域名保持一致。
            String redirectUri = CommonUtils.getBaseUrl(request) + "/sys/thirdLogin/oauth2/dingtalk/callback";
            builder.append("?redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
            // 固定值为code。
            // 授权通过后返回authCode。
            builder.append("&response_type=code");
            // 步骤一中创建的应用详情中获取。
            // 企业内部应用：client_id为应用的AppKey。
            builder.append("&client_id=").append(config.getClientId());
            // 授权范围，授权页面显示的授权信息以应用注册时配置的为准。
            // openid：授权后可获得用户userid
            builder.append("&scope=openid");
            // 跟随authCode原样返回。
            builder.append("&state=").append(state);
            //update-begin---author:wangshuai ---date:20220613  for：[issues/I5BOUF]oauth2 钉钉无法登录------------
            builder.append("&prompt=").append("consent");
            //update-end---author:wangshuai ---date:20220613  for：[issues/I5BOUF]oauth2 钉钉无法登录--------------
            url = builder.toString();
        } else {
            return "不支持的source";
        }
        log.info("oauth2 login url:" + url);
        response.sendRedirect(url);
        return "login…";
    }

    /**
     * 企业微信/钉钉 OAuth2登录回调
     *
     * @param code
     * @param state
     * @param response
     * @return
     */
    @ResponseBody
    @GetMapping("/oauth2/{source}/callback")
    public String oauth2LoginCallback(
            @PathVariable("source") String source,
            // 企业微信返回的code
            @RequestParam(value = "code", required = false) String code,
            // 钉钉返回的code
            @RequestParam(value = "authCode", required = false) String authCode,
            @RequestParam("state") String state,
            HttpServletResponse response) {
        SysUser loginUser;
        if (ThirdAppConfig.WECHAT_ENTERPRISE.equalsIgnoreCase(source)) {
            log.info("【企业微信】OAuth2登录进入callback：code=" + code + ", state=" + state);
            loginUser = thirdAppWechatEnterpriseService.oauth2Login(code);
            if (loginUser == null) {
                return "登录失败";
            }
        } else if (ThirdAppConfig.DINGTALK.equalsIgnoreCase(source)) {
            log.info("【钉钉】OAuth2登录进入callback：authCode=" + authCode + ", state=" + state);
            loginUser = thirdAppDingtalkService.oauth2Login(authCode);
            if (loginUser == null) {
                return "登录失败";
            }
        } else {
            return "不支持的source";
        }
        try {

            //update-begin-author:taoyan date:2022-6-30 for: 工作流发送消息 点击消息链接跳转办理页面
            String redirect = "";
            if (state.indexOf("?") > 0) {
                String[] arr = state.split("\\?");
                state = arr[0];
                if (arr.length > 1) {
                    redirect = arr[1];
                }
            }

            String token = saveToken(loginUser);
            state += "/oauth2-app/login?oauth2LoginToken=" + URLEncoder.encode(token, "UTF-8");
            //update-begin---author:wangshuai ---date:20220613  for：[issues/I5BOUF]oauth2 钉钉无法登录------------
            state += "&thirdType=" + source;
            //state += "&thirdType=" + "wechat_enterprise";
            if (redirect != null && redirect.length() > 0) {
                state += "&" + redirect;
            }
            //update-end-author:taoyan date:2022-6-30 for: 工作流发送消息 点击消息链接跳转办理页面

            //update-end---author:wangshuai ---date:20220613  for：[issues/I5BOUF]oauth2 钉钉无法登录------------
            log.info("OAuth2登录重定向地址: " + state);
            try {
                response.sendRedirect(state);
                return "ok";
            } catch (IOException e) {
                e.printStackTrace();
                return "重定向失败";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "解码失败";
        }
    }

}