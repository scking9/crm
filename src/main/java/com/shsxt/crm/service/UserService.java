package com.shsxt.crm.service;

import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录
     1. 参数校验 （非空校验）
        通过工具类 AssertUtil 判断参数是否满足需求。
        如果用户名或密码为空，则通过isTrue方法抛出异常。
     2. 调用Dao层，通过用户名查询用户对象，返回用户对象
     3. 判断用户对象是否存在
        不存在，则抛出异常
     4. 比较前台传递的用户密码与数据库中查询到用户对象的用户密码
        不相等，则抛出异常
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName, String userPwd){
        //1. 参数校验 （非空校验）
        checkUserLoginParams(userName,userPwd);
        //2调用Dao层，通过用户名查询用户对象，返回用户对象
        User user = userMapper.queryUserByName(userName);
        //3判断用户对象是否存在
        AssertUtil.isTrue(null==user,"用户不存在，请重试");
        /* 4. 比较前台传递的用户密码与数据库中查询到用户对象的用户密码 */
        // 先将前台传递的用户密码 通过MD5加密
        String pwd = Md5Util.encode(userPwd);
        // 比较加密后的密码与数据库中的密码是否相等
        AssertUtil.isTrue(!pwd.equals(user.getUserPwd()), "用户密码不正确！");
        //封装UserModel对象
        UserModel userModel=buildUserModel(user);
        return userModel;
    }

    private UserModel buildUserModel(User user) {
        UserModel userModel = new UserModel();
        //将用户ID加密
        String userIdStr = UserIDBase64.encoderUserID(user.getId());
        userModel.setUserIdStr(userIdStr);
        userModel.setTrueName(user.getTrueName());
        userModel.setUserName(user.getUserName());
        return userModel;
    }

    private void checkUserLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }

    /**
     修改用户密码
     *     1. 调用Dao层，通过用户ID得到用户对象
     *     2. 判断用户对象是否存在
     *     3. 原始密码是否为空
     *     4. 原始密码是否与数据库中的用户对象的密码一致
     *     5. 新密码是否为空
     *     6. 新密码是否不与原始密码一致
     *     7. 重复密码是否为空
     *     8. 重复密码是否与新密码一致
     *     9. 通过用户ID更新用户密码，判断受影响的行数
     * @param oldPwd
     * @param newPwd
     * @param againPwd
     * @param userId
     */
    public void updateUserPassWord(String oldPwd,String newPwd,String againPwd,Integer userId){
        //1. 调用Dao层，通过用户ID得到用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //2. 判断用户对象是否存在
        AssertUtil.isTrue(null==user,"待修改用户不存在");
        //3. 判断参数是否为空且满足需求
        checkUpdatePwd(oldPwd,newPwd,againPwd,user.getUserPwd());
        //4.通过用户ID更新用户密码，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateUserPwd(userId,Md5Util.encode(newPwd))!=1,"用户密码修改失败");
    }

    private void checkUpdatePwd(String oldPwd, String newPwd, String againPwd, String userPwd) {
        //3. 原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
        //4. 原始密码是否与数据库中的用户对象的密码一致（加密后再比较）
        String pwd =Md5Util.encode(oldPwd);
        AssertUtil.isTrue(!pwd.equals(userPwd),"原始密码不正确");
        //5. 新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        //6. 新密码是否不与原始密码一致
        AssertUtil.isTrue(newPwd.equals(oldPwd),"新密码不能与原密码一致");
        //7. 重复密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(againPwd),"重复密码不能为空");
        //8. 重复密码是否与新密码一致
        AssertUtil.isTrue(!againPwd.equals(newPwd),"重复密码与新密码不一致");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String ,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 添加用户
     *      1. 参数校验
     *         用户名      非空，用户名唯一
     *         邮箱        非空
     *         真实姓名     非空
     *         手机号      非空，格式正确
     *      2. 设置参数的默认值
     *          createDate、updateDate、isValid
     *          默认密码123456 （加密）
     *      3. 添加操作
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        // 参数校验
        checkUserParams(user);

        // 设置默认值
        user.setUserPwd(Md5Util.encode("123456"));
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setIsValid(1);

        // 添加操作
        // AssertUtil.isTrue(userMapper.insertSelective(user) != 1, "用户添加失败！");
        // 会返回主键，主键会自动设置到user对象中
        AssertUtil.isTrue(userMapper.insertHasKey(user) != 1, "用户添加失败！");

        System.out.println(user.getId());


        // 添加用户角色关联数据
        userRoleService.relaionUserRole(user.getId(), user.getRoleIds());

    }

    /**
     * 参数校验
     *         用户名      非空，用户名唯一
     *         邮箱        非空
     *         真实姓名     非空
     *         手机号      非空，格式正确
     * @param user
     */
    private void checkUserParams(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");
        // 通过用户名查询用户对象
        User temp =  userMapper.queryUserByName(user.getUserName());

        // 判断用户Id是否为空，不为空则为修改操作
        if (user.getId() == null) {
            // 如果用户对象存在，则不可用
            AssertUtil.isTrue(temp != null, "用户名已存在，请重新输入！");
        } else {
            // 如果用户对象存在，则不可用
            AssertUtil.isTrue(temp != null && !(user.getId().equals(temp.getId())), "用户名已存在，请重新输入！");
        }

        AssertUtil.isTrue(StringUtils.isBlank(user.getTrueName()), "真实姓名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "邮箱不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()), "手机号不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "手机号格式不正确！");
    }

    /**
     * 更新用户
     *      1. 参数校验
     *          id          非空，数据存在
     *         用户名      非空，用户名唯一
     *         邮箱        非空
     *         真实姓名     非空
     *         手机号      非空，格式正确
     *      2. 设置参数的默认值
     *          createDate、updateDate、isValid
     *          默认密码123456 （加密）
     *      3. 添加操作
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        AssertUtil.isTrue(null == user.getId(), "数据异常，请重试！");
        // 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断数据是否存在
        AssertUtil.isTrue(temp == null, "待更新用户不存在！");

        // 参数校验
        checkUserParams(user);

        // 设置默认值
        user.setUpdateDate(new Date());

        // 更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1, "用户更新失败！");


        // 添加用户角色关联数据
        userRoleService.relaionUserRole(user.getId(), user.getRoleIds());

    }

    /**
     * 删除用户
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length < 1, "待删除记录不存在！");

        // 删除用户角色关联信息
        for (Integer userId : ids) {
            // 通过用户Id查询对应的用户角色关联信息
            Integer count =  userRoleMapper.selectUserRoleCountByUserId(userId);
            // 判断是否存在当前用户的用户关联信息
            if (count > 0) {
                // 如果存在，则删除原有的关联数据
                AssertUtil.isTrue(userRoleMapper.deleteRelationByUserId(userId) != count,"用户角色关联失败！");
            }
        }

        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "用户数据删除失败！");
    }
}
