<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>图书列表</title>
</head>
<link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">


<body>
<div class="page-header container ">
    <div class="row">
        <div class="col-md-12">
            <div class="col-md-4">
                <h3>图书管理系统</h3>
            </div>
            <div class="pull-right">
                <span>当前账号</span>
                <%--  获取session的账号名--%>
                <span>${user!=null?user.username:""}</span>

                <a href="${pageContext.request.contextPath}/user/logout">退出</a>
            </div>
        </div>
    </div>
</div>


<%--搜索--%>
<div class="panel">
    <div class="panel-body" style="padding-bottom: 1px;">
        <form class="form-horizontal">
            <div class="form-group">
                <div class="col-sm-3">
                    <!-- 自定义搜索框 -->
                    <input type="text" name="searchString" id="searchString_id" class="form-control" placeholder="请输入书名或作者" onkeydown="javascript:if(event.keyCode==13) searchId();" />
                </div>
                <div class="col-sm-1">
                    <button type="button" class="btn btn-primary btn-w-m" id="queryBtn">
                        <span class="glyphicon glyphicon-search"></span> 搜索
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="container" >
    <div id="toolbar" class="btn-group">
        <button id="btn_add" type="button" class="btn btn-default" onclick="add()" data-toggle="modal"
                data-target="#studentModal" data-whatever="增加">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="btn_edit" type="button" class="btn btn-default" onclick="editOne()">
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
        </button>
        <button id="btn_delete" type="button" class="btn btn-default" onclick="delOne()">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>
        <button id="import" class="btn btn-default" class="btn btn-default" data-toggle="modal"
                data-target="#importModal" data-whatever="导入">
            <span class="glyphicon glyphicon-export" aria-hidden="true"></span>导入
        </button>
        <a id="export" class="btn btn-default" onclick="out()" >
            <span class="glyphicon glyphicon-export" aria-hidden="true"></span>导出
        </a>
    </div>
    <table id="table">
    </table>
</div>

<div class="ibox-content">
    <table id="myTable"></table>
</div>



<%--模态框弹窗--%>
<div class="modal fade" id="studentModal" tabindex="-1" role="dialog" aria-labelledby="titeLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="reset()" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="titeLabel">图书信息</h4>
            </div>
            <div class="modal-body">
                <form id="myforms">
                    <input type="hidden" class="form-control" id="bid">
                    <div class="form-group">
                        <label class="control-label" for="bookName">书名：</label>
                        <input type="text" class="form-control" id="bookName">
                    </div>
                    <div class="form-group">
                        <label for="author" class="control-label">作者:</label>
                        <input class="form-control" id="author"></input>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="reset()" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="saveOrUpdate()">确定</button>
            </div>
        </div>
    </div>
</div>

<%--导入模态框--%>
<div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="titeLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">导入</h4>
            </div>
            <div class="modal-body">
                <form  method="post" id="importForm" enctype="multipart/form-data">
                    <input type="file" name="file" >
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="importStudent()">确定</button>
            </div>
        </div>
    </div>
</div>










</body>
<script src="${pageContext.request.contextPath}/static/js/jquery-2.1.4.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap-table.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap-table-zh-CN.min.js"></script>
<link href="${pageContext.request.contextPath}/static/css/bootstrap-dialog.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/static/js/bootstrap-dialog.min.js"></script>




<script>



    // 使用DOM对象的reset()方法清除模态框弹窗上一次数据
    function reset(){
        $("#myforms")[0].reset();
    }

    var isUpdate = true;
    $(document).ready(function () {
        //调用函数，初始化表格
        initTable();
        //当点击查询按钮的时候执行,bootstrap-table前端分页是不能使用搜索功能，所以可以提取出来自定义搜索。后台代码，在后面给出
        $("#queryBtn").bind("click", initTable);
    });
    function initTable() {
        //先销毁表格
        $('#myTable').bootstrapTable('destroy');
        $('#myTable').bootstrapTable({
            url: '${pageContext.request.contextPath}/books/booksList',//请求后台的URL（*）
            method: 'get',
            dataType: "json",
            //dataField: 'rows',
            striped: true,//设置为 true 会有隔行变色效果
            undefinedText: "空",//当数据为 undefined 时显示的字符
            pagination: true, //设置为 true 会在表格底部显示分页条。
            pageNumber: 1,//初始化加载第一页，默认第一页
            pageSize: 10,//每页的记录行数（*）
            pageList: [10, 20, 30, 40],//可供选择的每页的行数（*），当记录条数大于最小可选择条数时才会出现
            paginationPreText: '上一页',
            paginationNextText: '下一页',
            search: false, //是否显示表格搜索,bootstrap-table服务器分页不能使用搜索功能，可以自定义搜索框，上面jsp中已经给出，操作方法也已经给出
            data_local: "zh-US",//表格汉化
            sidePagination: "server", //服务端处理分页
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            queryParamsType : "limit",//设置为 ‘limit’ 则会发送符合 RESTFul 格式的参数.
            queryParams: function (params) {//自定义参数，这里的参数是传给后台的，我这是是分页用的
//            请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
//　　　　　　　queryParamsType = 'limit' ,返回参数必须包含limit, offset, search, sort, order
//            queryParamsType = 'undefined', 返回参数必须包含: pageSize, pageNumber, searchText, sortName, sortOrder.
//            返回false将会终止请求。
                return {//这里的params是table提供的
                    offset: params.offset,//从数据库第几条记录开始
                    limit: params.limit,//找多少条
                    bookName:$("#searchString_id").val(), //这个就是搜索框中的内容，可以自动传到后台，搜索实现在xml中体现
                    author:$("#searchString_id").val()
                };
            },
            responseHandler: function (res) {
                //如果后台返回的json格式不是{rows:[{...},{...}],total:100},可以在这块处理成这样的格式
                return res;
            },
            columns: [
            {
                field: "checked",
                checkbox: true,
                title: "选择",
                align: "center",
                valign: "middle",
            }, {
                field: 'bid',
                title: '图书ID',
                visible: false   // 隐藏不显示


            }, {
                field: 'id',
                title: '序号',
                formatter: idFormatter

            }, {
                field: 'bookName',
                title: '书名',
                valign: 'middle',
            }, {
                field: 'author',
                title: '作者',
                align: 'center',
            }, {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter: function (value, row, index) {
                    var e = '<button class="btn bg-primary" onclick="edit(' + row.bid + ')" data-toggle="modal" data-target="#studentModal" data-whatever="编辑">编辑</button> ';
                    var d = '<button class="btn btn-danger" onclick="del(' + row.bid + ')">删除</button> ';
                    return e + d;
                }
            } ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            }



        });
    }

    function idFormatter(value, row, index){
        return index+1;
    }

    /**
     * 添加
     **/
    function add() {
        isUpdate = false;
    }

    /**
     * 编辑的事件处理
     * @param id
     */
    function edit(bid) {
        isUpdate = true;
        $.ajax({
            url: "${pageContext.request.contextPath}/books/updatePage/" + bid,
            type: "GET",
            dataType: "json",
            success: function (data) {
                if (data !=null) {
                    $("#bid").val(data.bid);
                    $("#bookName").val(data.bookName);
                    $("#author").val(data.author);
                } else {
                    alert("加载数据错误");
                }
            },
            error: function (data) {
                alert(data.msg);
            }
        });
    }

    // 新增或修改
    function saveOrUpdate() {
        var url = "${pageContext.request.contextPath}/books/addBooks";
        if (isUpdate) {
            url = "${pageContext.request.contextPath}/books/update/" + $("#bid").val();
        }
        $.ajax({
            url: url,
            type: "get",
            dataType: "json",
            data: {
                bookName: $("#bookName").val(),
                author: $("#author").val()
            },
            success : function(data) {
                if (data.flag) {//请求成功
                    alert(data.message);
                    location.reload();
                } else {
                    alert(data.message);
                }

            }
        });
    }
    /**
     * 删除
     * @param id
     */
    function del(bid) {
        $.ajax({
            url: "${pageContext.request.contextPath}/books/delete/" + bid,
            type: "GET",
            dataType: "json",
            success : function(data) {
                if (data.flag) {//请求成功
                    alert(data.message);
                    location.reload();
                } else {
                    alert(data.message);
                }

            },
            error: function (data) {
                alert("删除失败");
                location.reload();
            }
        })
    }
    function out(){
        BootstrapDialog.confirm({
            title : '确认',  // 标题
            message : "是否导出？",
            type : BootstrapDialog.TYPE_WARNING, //
            closable : true, // 默认为false，点击对话框以外的页面内容可关闭
            draggable : true, // 默认为false，可拖拽
            btnCancelLabel : '取消', // 取消命名
            btnOKLabel : '确定', // 确定命名,
            btnOKClass : 'btn-warning',
            size : BootstrapDialog.SIZE_SMALL,
            // 对话框关闭的时候执行方法
            onhide : function () {

            },
            // 对话框点击确定时执行方法
            callback : function(result) {
                // 点击确定按钮时，result为true
                if (result) {
                    $.ajax({
                        url: "${pageContext.request.contextPath}/books/out",
                        type: "GET",
                        dataType: "json",
                        success : function(data) {
                            if(data.flag) {
                                alert(data.message);
                            }
                            else{
                                alert(data.message);
                            }
                        }
                    })
                }
            }
        });
    }

    /**
     * 编辑一个
     */
    function editOne() {
        // 获取当前行数据
        var row=$("#myTable").bootstrapTable('getSelections');
        if(row.length > 1 || row.length == 0){
           alert("只能选择一条数据进行修改")
         }else{
            // 查询要修改的数据
            edit(row[0].bid)
            // 弹出模态框
            $('#studentModal').modal('toggle');
        }



    }

    /**
     * 删除多条
     */
    function delOne() {
        var bids=new Array();
        // 获取当前行数据
        var row=$("#myTable").bootstrapTable('getSelections');

        // 获得选中bid，存储到数组bids
        for(var i=0;i<row.length;i++){
            if(row[i].checked){
                bids[i]=row[i].bid;
            }
        }
        deletes(bids);

        // del();
    }


    function deletes(bids){
        BootstrapDialog.confirm({
            title : "确认", // 标题
            message :'确认要删除所选数据吗？',
            btnCancelLabel : '取消', // 取消命名
            btnOKLabel : '确定', // 确定命名,
            // 对话框关闭的时候执行方法
            onhide : function () {

            },
            // 对话框点击确定时执行方法
            callback : function(result) {
        // 点击确定按钮时，result为true
        if (result) {
            $.ajax({
                url: "${pageContext.request.contextPath}/books/deletes/"+ bids,
                type: "GET",
                dataType: "json",
                success : function(data) {
                    if(data.flag) {
                        alert(data.message);
                        location.reload();
                    }
                    else{
                        alert(data.message);
                        location.reload();
                    }
                }

            })
        }
       }
    })
 }

    function importStudent() {
        var formData = new FormData($('#importForm')[0]);
        $.ajax({
            data : formData,
            type: 'post',
            url:  "${pageContext.request.contextPath}/books/import/", //上传文件的请求路径必须是绝对路劲
            cache:false,
            processData:false,
            contentType:false,
        }).success(function (data) {
            alert(data.message);
            location.reload();
        }).error(function (data) {
            alert(data.message);
            location.reload();
        });
    }

</script>
</html>
