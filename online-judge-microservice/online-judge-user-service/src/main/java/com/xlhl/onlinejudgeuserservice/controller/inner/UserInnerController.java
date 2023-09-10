package com.xlhl.onlinejudgeuserservice.controller.inner;

import com.xlhl.onlinejudgemodel.model.entity.User;
import com.xlhl.onlinejudgeserviceclient.service.UserFeignClient;
import com.xlhl.onlinejudgeuserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 内部使用的用户接口
 *
 * @author xlhl
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;


    /**
     * 根据id获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("id") Long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据多个 id获取用户列表
     *
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
