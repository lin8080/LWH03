package com.lwh.controller;

import com.lwh.pojo.User;
import com.lwh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

        @Autowired
        UserService userService;


        //进入login页面
        @RequestMapping("/login")
        public String userlogin(){
        return "login";
        }

       //执行login操作，匹配用户名和密码，建立session持久连接
        @RequestMapping(value="/login",method = RequestMethod.POST)
        public String login(User useradmin, Model model,HttpServletRequest request){
            String username = useradmin.getUsername();
            String password = useradmin.getPassword();
            User user = userService.getLogin(username,password);
            if(user!=null){
                request.getSession(true).setAttribute("user",user);
                return "redirect:/index.jsp";
            }else{
                model.addAttribute("message","登录名或密码错误！");
                return "error";
            }
        }

        /**
         * 登出
         * @param session
         * @return
        */
        @GetMapping("/logout")
        public String logout(HttpSession session){
            //通过session.invalidata()方法来注销当前的session
            session.invalidate();
            return "redirect:login";
        }

        /**
         * 注册
         * @return
         */
        @RequestMapping("/register")
        public String register(){
            return "register";
        }

        /**
         * 注册认证
         * @param user
         * @param attributes
         * @return
         */
        @PostMapping("/doRegister")
        public String doRegister(User user,RedirectAttributes attributes){
            try {
                User registerUser=userService.register(user);
            }catch (Exception e){

                attributes.addAttribute("msg",e.getMessage());
                return "redirect:register";

            }
            return "redirect:login";
        }


    }
