package com.niuzhendong.frame.modules.sys.controller;

import com.niuzhendong.frame.common.enums.LoginEnum;
import com.niuzhendong.frame.core.exception.LoginException;
import com.niuzhendong.frame.common.util.returns.ReturnVOUtils;
import com.niuzhendong.frame.core.jwt.JwtUtils;
import com.niuzhendong.frame.core.shiro.PasswordHelper;
import com.niuzhendong.frame.modules.sys.entity.SysRole;
import com.niuzhendong.frame.modules.sys.entity.SysUser;
import com.niuzhendong.frame.modules.sys.model.SysUserModel;
import com.niuzhendong.frame.modules.sys.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class HomeController {
    @Autowired
    SysUserService sysUserService;

    @Autowired
    PasswordHelper passwordHelper;

    @GetMapping("login")
    public Object login() {
        return "pages/login";
    }

    @GetMapping("logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        //用户不为空，则手动登出
        if(subject !=null){
            subject.logout();
        }
        return "pages/login";
    }

    @GetMapping("unauthorized")
    public Object unauthc() {
        return "pages/unauthorized";
    }

    @GetMapping("index")
    public Object index() {
        return "pages/index";
    }

    @GetMapping("admin")
    @ResponseBody
    @RequiresRoles("admin")
    public Object admin() {
        return "admin success";
    }

    @GetMapping("edit")
    @ResponseBody
    @RequiresPermissions("Update")
    public Object edit() {
        return "edit success";
    }

    @GetMapping("removeable")
    @ResponseBody
    @RequiresPermissions("Delete")
    public Object removeable() {
        return "removeable success";
    }

//    @PostMapping("doLogin")
//    public Object doLogin(@RequestParam String username,@RequestParam String password) {
//        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            subject.login(token);
//
//            SysUserModel sysUserModel = (SysUserModel) subject.getPrincipal();
//            subject.getSession().setAttribute("user",sysUserModel);
//        } catch (IncorrectCredentialsException ice) {
//            return "login";
//        } catch (UnknownAccountException uae) {
//            return "login";
//        }
//
//        return "index";
//    }

    @PostMapping("doLogin")
    @ResponseBody
    public Object doLogin(@RequestParam String username,@RequestParam String password) {
        SysUserModel sysUserModel = sysUserService.findUserByName(username);
        if(ObjectUtils.isEmpty(sysUserModel)) {
            throw new LoginException(LoginEnum.USERNAME_ERROR.getMessage());
        } else if(!sysUserModel.getPassword().equals(passwordHelper.encryptPassword(password,sysUserModel.getCredentialsSalt()))) {
            throw new LoginException(LoginEnum.PASSWORD_ERROR.getMessage());
        }else {
            return ReturnVOUtils.success(JwtUtils.createToken(sysUserModel.getTsuId()));
        }
    }

//    @PostMapping("register")
    @GetMapping("register")
    @ResponseBody
    public Object register(@RequestParam String username,@RequestParam String password) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(password);
        passwordHelper.encryptPassword(sysUser);

        SysRole sysRole = new SysRole();
        sysRole.setTsrId(1);
        List<SysRole> roles = new ArrayList<>();
        roles.add(sysRole);
        sysUser.setRoles(roles);

        sysUserService.save(sysUser);

        return "SUCCESS";
    }
}
