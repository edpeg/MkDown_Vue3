package top.openfbi.mdnote.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.openfbi.mdnote.common.exception.ResultException;
import top.openfbi.mdnote.config.ResponseResultBody;
import top.openfbi.mdnote.user.service.UserService;
import top.openfbi.mdnote.user.util.Session;

/**
 * 用户操作接口
 */
@RequestMapping("/user")
@ResponseResultBody
@RestController()
public class UserController {
    @Autowired
    private UserService userService;
    private static final Logger logger
            = LoggerFactory.getLogger(UserController.class);

    /**
     * 注销账户
     */
    @GetMapping("/logOff")
    public void logOff() throws ResultException {
        userService.logOff(Session.getUser().getId());
    }
}