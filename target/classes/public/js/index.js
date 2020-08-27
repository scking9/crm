layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
      监听submit提交
         form.on('submit(按钮的lay-filter属性值)', function (data) {

        });
     */
    form.on('submit(login)', function (data) {

        // 得到表单元素的值
        //console.log(data.field);

        // 用户名称
        var userName = data.field.username;
        // 用户密码
        var userPwd = data.field.password;

        // TODO 参数的非空校验
        if (isEmpty(userName)) {
            layer.msg("用户名称不能为空！",{icon:5});
            return false;
        }
        if (isEmpty(userPwd)) {
            layer.msg("用户密码不能为空！",{icon:5});
            return false;
        }

        // 当数据不为空时，发送ajax请求
        $.ajax({
            type:"post",
            url: ctx + "/user/login",
            data:{
                userName:userName,
                userPwd:userPwd
            },
            success:function (data) {
                // console.log(data);
                // 判断是否登录成功，code=200表示成功
                if (data.code == 200) {
                    // 提醒用户登录成功
                    layer.msg("登录成功！", {icon:6});

                    // 将用户信息设置到cookie中
                    $.cookie("userIdStr",data.result.userIdStr);
                    $.cookie("userName",data.result.userName);
                    $.cookie("trueName",data.result.trueName);

                    // 判断用户是否选择记住密码
                    if ($("#rememberMe").prop("checked")) {
                        // 如果选择记住密码，则设置7天失效
                        $.cookie("userIdStr", data.result.userIdStr, { expires: 7 });
                        $.cookie("userName", data.result.userName, { expires: 7 });
                        $.cookie("trueName", data.result.trueName, { expires: 7 });
                    }


                    // 延时2秒执行
                    window.setTimeout(function () {
                        // 跳转到首页
                        window.location.href = ctx + "/main";
                    },2000);
                } else {
                    layer.msg(data.msg, {icon:5});
                }
            }
        });


        return false; // 阻止表单提交
    });

    /**
     * 判断字符串是否为空
     *      为空，返回true；否则返回false
     * @param str
     * @returns {boolean}
     */
    function isEmpty(str) {
        if (str == null || str.trim() == "") {
            return true;
        }
        return false;
    }
    
    
});