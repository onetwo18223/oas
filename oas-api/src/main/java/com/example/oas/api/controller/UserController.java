package com.example.oas.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.example.oas.api.common.util.PageUtils;
import com.example.oas.api.common.util.R;
import com.example.oas.api.controller.form.*;
import com.example.oas.api.db.pojo.TbUser;
import com.example.oas.api.service.UserService;
import io.micrometer.core.instrument.search.Search;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户Web接口")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 生成登陆二维码的字符串
     */
    @GetMapping("/createQrCode")
    @Operation(summary = "生成二维码Base64格式的字符串")
    public R createQrCode() {
        HashMap map = userService.createQrCode();
        return R.ok(map);
    }

//    /**
//     * 检测登陆验证码
//     *
//     * @param form
//     * @return
//     */
//    @PostMapping("/checkQrCode")
//    @Operation(summary = "检测登陆验证码")
//    public R checkQrCode(@Valid @RequestBody CheckQrCodeForm form) {
//        boolean bool = userService.checkQrCode(form.getCode(), form.getUuid());
//        return R.ok().put("result", bool);
//    }

    @PostMapping("/wechatLogin")
    @Operation(summary = "微信小程序登陆")
    public R wechatLogin(@Valid @RequestBody WechatLoginForm form) {
        HashMap map = userService.wechatLogin(form.getUuid());
        boolean result = (boolean) map.get("result");
        if (result) {
            int userId = (int) map.get("userId");
            StpUtil.setLoginId(userId);
            Set<String> permissions = userService.searchUserPermissions(userId);
            map.remove("userId");
            map.put("permissions", permissions);
        }
        return R.ok(map);
    }

    /**
     * 登陆成功后加载用户的基本信息
     */
    @GetMapping("/loadUserInfo")
    @Operation(summary = "登陆成功后加载用户的基本信息")
    @SaCheckLogin
    public R loadUserInfo() {
        int userId = StpUtil.getLoginIdAsInt();
        HashMap summary = userService.searchUserSummary(userId);
        return R.ok(summary);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查找用户")
    @SaCheckPermission(value = {"ROOT", "USER:SELECT"}, mode = SaMode.OR)
    public R searchById(@Valid @RequestBody SearchUserByIdForm form) {
        HashMap map = userService.searchById(form.getUserId());
        return R.ok(map);
    }

    @GetMapping("/searchAllUser")
    @Operation(summary = "查询所有用户")
    @SaCheckLogin
    public R searchAllUser() {
        ArrayList<HashMap> list = userService.searchAllUser();
        return R.ok().put("list", list);
    }

    /**
     * 登录模块
     *
     * @param form 用户名 + 用户密码
     * @return R(包含result, 权限, token)
     */
    @PostMapping("/login")
    @Operation(summary = "登陆系统")
    // @Valid 验证注解
    // @RequestBody 对象属性填充
    public R login(@Valid @RequestBody LoginForm form) {
        HashMap param = new HashMap() {{
            put("username", form.getUsername());
            put("password", form.getPassword());
        }};
        Integer userId = userService.login(param);
        R r = R.ok().put("result", userId != null);
        if (userId != null) {
            StpUtil.login(userId);
            Set<String> permissions = userService.searchUserPermissions(userId);
            /*
             * 因为新版的Chrome浏览器不支持前端Ajax的withCredentials，
             * 导致Ajax无法提交Cookie，所以我们要取出生成的Token返回给前端，
             * 让前端保存在Storage中，然后每次在Ajax的Header上提交Token
             */
            String token = StpUtil.getTokenInfo().getTokenValue();
            r.put("permissions", permissions).put("token", token);
        }
        return r;
    }

    /**
     * 修改密码
     *
     * @param form 密码
     * @return R(包含影响行数)
     */
    @PostMapping("/updatePassword")
    @Operation(summary = "修改密码")
    public R update(@Valid @RequestBody UpdatePasswordForm form) {
        // 获取会话Id, 因为登录系统会话存储的是用户Id, 所以这个方法获取到的是用户Id
        int userId = StpUtil.getLoginIdAsInt();
        HashMap<String, Object> param = new HashMap<String, Object>() {{
            put("userId", userId);
            put("password", form.getPassword());
        }};
        Integer rows = userService.updatePassword(param);
        return R.ok().put("rows", rows);
    }

    /**
     * 退出登录
     *
     * @return R(包含result)
     */
    @GetMapping("/logout")
    @Operation(summary = "退出登录")
    public R logout() {
        StpUtil.logout();
        return R.ok();
    }


    /**
     * 弹性搜索用户信息
     *
     * @param form 搜索条件
     * @return PageUtils
     */
    @PostMapping("/searchUserByPage")
    @Operation(summary = "分页查询用户")
    @SaCheckPermission(value = {"ROOT", "USER:SELECT"}, mode = SaMode.OR)
    public R searchUserByPage(@Valid @RequestBody SearchUserByPageForm form) {
        int page = form.getPage();
        int length = form.getLength();
        int pageIndex = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("pageIndex", pageIndex);
        PageUtils pages = userService.searchUserByPage(param);
        return R.ok().put("page", pages);
    }

    /**
     * 添加用户
     * 使用TBUser来承接form
     *
     * @param form 用户信息
     * @return
     */
    @PostMapping("/insert")
    @Operation(summary = "添加用户")
    @SaCheckPermission(value = {"ROOT", "USER:INSERT"}, mode = SaMode.OR)
    public R insert(@Valid @RequestBody InsertUserForm form) {
        TbUser user = JSONUtil.parse(form).toBean(TbUser.class);
        // 添加角色时, 需要三个必须字段 : 状态字段, 角色字段, 时间字段
        user.setStatus((byte) 1);
        user.setRole(JSONUtil.parseArray(form.getRole()).toString());
        user.setCreateTime(new Date());
        int rows = userService.insert(user);
        return R.ok().put("rows", rows);
    }

    /**
     * 修改用户信息
     * 使用HashMap承接form, 参数除了添加使用DAO, 一般都是HashMap
     *
     * @param form 需要修改的信息, 会将用户ID和需要修改的数据传入
     * @return
     */
    @PostMapping("/update")
    @Operation(summary = "修改用户信息")
    @SaCheckPermission(value = {"ROOT", "USER:UPDATE"}, mode = SaMode.OR)
    public R update(@Valid @RequestBody UpdateUserForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("role", JSONUtil.parseArray(form.getRole()).toString());
        int rows = userService.update(param);
        if (rows == 1) {
            StpUtil.logoutByLoginId(form.getUserId());
        }
        return R.ok().put("rows", rows);
    }

    @PostMapping("/deleteUserByIds")
    @Operation(summary = "批量删除用户")
    @SaCheckPermission(value = {"ROOT", "USER:DELETE"}, mode = SaMode.OR)
    public R deleteUserByIds(@Valid @RequestBody DeleteUserByIdsForm form) {
        int userId = StpUtil.getLoginIdAsInt();

        if (ArrayUtil.contains(form.getIds(), userId)) {
            return R.error("不可以删除自己的账户!");
        }
//        if (form.getIds().contains(userId)) {
//            return R.error("不可以删除自己的账户!");
//        }
        int rows = userService.deleteUserByIds(form.getIds());
        if (rows > 0) {
            for (Integer id : form.getIds()) {
                StpUtil.logoutByLoginId(id);
            }
        }
        return R.ok().put("rows", rows);
    }
}
