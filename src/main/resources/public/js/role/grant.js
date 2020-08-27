$(function () {
    loadModuleInfo(); 
});

/**
 * 加载资源数据
 */
function loadModuleInfo() {
    // 发送ajax请求
    $.ajax({
        type:"get",
        url:ctx +"/module/queryAllModules?roleId=" + $("[name='roleId']").val(),
        data:{},
        success:function (data) {
             //console.log(data);
            var zTreeObj;
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback:{
                    // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                   //  onCheck: zTreeOnCheck // 绑定具体的函数名
                }
            };

            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);

        }
    });
}

