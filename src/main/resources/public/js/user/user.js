layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    var  tableIns = table.render({
        elem: '#userList',
        url : ctx + '/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });


    /**
     * 多条件搜索
     *      点击搜索按钮时，表格重载进行条件查询
     */
    $(".search_btn").click(function () {
        // 表格重载
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                userName:$("[name='userName']").val(),
                email:$("[name='email']").val(),
                phone:$("[name='phone']").val()
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });


    /**
     * 头部工具栏
     */
    table.on('toolbar(users)', function (obj) {
        if (obj.event == "add") {
            // 添加操作
            openAddOrUpdateUserDialog();
        } else if (obj.event == "del") {
            // 得到所有被选中的数据
            var checkStatus = table.checkStatus(obj.config.id);
            // 删除操作
            deleteUser(checkStatus.data);
        }
    });

    /**
     * 批量删除
     * @param data
     */
    function deleteUser(data) {
        // 判断用户是否选择删除的记录
        if (data.length == 0) {
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }

        // 询问用户是否确认删除
        layer.confirm('确定删除选中的用户记录？', {
            btn: ['确定','取消'] //按钮
        }, function(index) {

            layer.close(index);

            // 参数是一个数组，数组格式是ids=1&ids=2&ids=3
            var ids = "ids=";
            for(var i = 0; i < data.length; i++) {
                if (i < data.length - 1) {
                    ids += data[i].id + "&ids=";
                } else {
                    ids += data[i].id;
                }
            }
            // console.log(ids);

            // 发送ajax请求
            $.ajax({
                type:"post",
                url:ctx + "/user/delete",
                data:ids,
                success:function (data) {
                    if (data.code == 200) {
                        layer.msg("删除成功！",{icon:6});
                        // 表格重载
                        tableIns.reload();
                    } else {
                        layer.msg(data.msg, {icon:5});
                    }
                }
            });

        });


    }


    /**
     * 行工具栏
     */
    table.on('tool(users)', function (obj) {
        if (obj.event == "edit") {
            // 更新操作
            openAddOrUpdateUserDialog(obj.data.id);
        } else if (obj.event == "del") {
            // 删除操作
            layer.confirm('确定删除当前用户？', {icon: 3, title: "用户管理"}, function (index) {
                // 发送ajax请求
                $.post(ctx + "/user/delete",{ids:obj.data.id}, function (data) {
                    if (data.code == 200) {
                        layer.msg("删除成功！",{icon:6});
                        // 表格重载
                        tableIns.reload();
                    } else {
                        layer.msg(data.msg, {icon:5});
                    }
                });
            });
        }
    });


    /**
     * 打开添加/修改用户的对话框
     * @param userId
     */
    function openAddOrUpdateUserDialog(userId) {

        var title = "用户管理 - 添加用户";
        var url = ctx + "/user/toAddOrUpdateUserPage";

        // 判断userId是否为空
        if (userId != null && userId != "") {
            title = "用户管理 - 更新用户";
            url += "?userId=" +userId;
        }

        layui.layer.open({
            title:title,
            type:2,
            area:["650px","400px"],
            maxmin:true,
            content:url
        });
    }




});