layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;

    /**
     * 表单submit绑定
     */
    form.on('submit(addOrUpdateUser)', function (obj) {

        var url = ctx + "/user/add";

        // 获取隐藏域中的用户ID
        var userId = $("[name='id']").val();
        if (userId != null && userId != "") {
            url = ctx + "/user/update";
        }

        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            success:function (data) {
                if (data.code == 200) {
                    layer.msg("操作成功！",{icon:6});
                    // 关闭对话框
                    layer.closeAll("iframe");
                    // 刷新
                    parent.location.reload();
                } else {
                    layer.msg(data.msg, {icon:5})
                }
            }
        });

        return false; // 阻止表单提交

    });


    /**
     * 加载角色下拉框
     *  formSelects.config(参数一,参数二):
     *      参数一：需要绑定的元素的xm-select属性值
     *      参数二：配置项
     *          type：请求类型  GET|POSt等
     *          searchUrl：请求的路径
     *          keyName：返回的数据对应的key值 （显示给用户看的）
     *          keyVal：返回的数据对应的value值 （传递到后台的）
     *       参数三：isJson，是否传输json数据, true将添加请求头 Content-Type: application/json; charset=UTF-8
     */
    formSelects.config("selectId",{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles?userId=" + $("[name='id']").val(),
        keyName: "roleName",
        keyVal: "id"
    },true);


});