layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //角色列表展示
    var  tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click",function(){
        table.reload("roleListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                roleName: $("input[name='roleName']").val()
            }
        })
    });




    /**
     * 头部工具栏
     */
    table.on('toolbar(roles)', function (obj) {
        if (obj.event == "add") {
            // 添加操作
            openAddOrUpdateRoleDialog();
        } else if (obj.event == "grant") {
            var checkStatus = table.checkStatus(obj.config.id);
            // 授权操作
            openAddRoleGrantDialog(checkStatus.data);
        }
    });

    /**
     * 打开授权页面
     */
    function openAddRoleGrantDialog(data){
        // 判断 是否选择了记录
        if (data.length < 1) {
            layer.msg("请选择要授权的角色！",{icon:5});
            return;
        }
        // 如果选择了多个
        if (data.length >1) {
            layer.msg("暂不支持批量操作！",{icon:5});
            return;
        }

        var url  =  ctx+"/role/toAddGrantPage?roleId="+data[0].id;
        var title="角色管理-角色授权";
        layui.layer.open({
            title : title,
            type : 2,
            area:["600px","280px"],
            maxmin:true,
            content : url
        });
    }

    /**
     * 行工具栏
     */
    table.on('tool(roles)', function (obj) {
        if (obj.event == "edit") {
            // 更新操作
            openAddOrUpdateRoleDialog(obj.data.id);
        } else if (obj.event == "del") {
            // 删除操作
            layer.confirm('确定删除当前角色？', {icon: 3, title: "角色管理"}, function (index) {
                // 发送ajax请求
                $.post(ctx + "/role/delete",{roleId:obj.data.id}, function (data) {
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
     * 打开添加或修改的角色对话框
     * @param roleId
     */
    function openAddOrUpdateRoleDialog(roleId) {
        var title = "角色管理 - 添加角色";
        var url = ctx + "/role/toAddOrUpdateRolePage";

        // roleId
        if (roleId != null && roleId != "") {
            title = "角色管理 - 更新角色";
            url += "?roleId=" +roleId;
        }

        layui.layer.open({
            title:title,
            type:2,
            area:["600px","280px"],
            maxmin:true,
            content:url
        });
    }









});