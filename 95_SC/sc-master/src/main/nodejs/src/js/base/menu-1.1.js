require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("treeview");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('menu', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
    var m = {
    		toKen : null,
    		use : null,
    		up : null,
			tableGrid : null,
        	error_pcode : null,
        	alert_div : null,//页面提示框
        	search : null,//检索按钮
			searchJson:null,
        	main_box : null//检索按钮
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left=_common.config.surl+"/menu";
		initTable1();
		initEvent();
		$.myAjaxs({
			url:url_left+"/list",
			async:true,
			cache:false,
			type :"get",
			// data :"",
			dataType:"json",
			success:showResponse,
			complete:_common.myAjaxComplete
		});
		m.search.click();
    }
	var showResponse = function(data,textStatus, xhr){
		selectTrTemp = null;
		var resp = xhr.responseJSON;
		if( resp.result == false){
			top.location = resp.s+"?errMsg="+resp.errorMessage;
			return ;
		}
		$('#tree').treeview({
			data: data.list,         // 数据源
			showCheckbox: false,   //是否显示复选框
			highlightSelected: true,    //是否高亮选中
			multiSelect: false,    //多选
			levels : 2,
			preventUnselect: true,
			enableLinks : false,//必须在节点属性给出href属性
			// hierarchicalCheck:true,//级联勾选
			color: "#010A0E",
			onNodeChecked : function (event,node) {
				var selectNodes = getChildNodeIdArr(node); //获取所有子节点
				if (selectNodes) { //子节点不为空，则选中所有子节点
					$('#tree').treeview('checkNode', [selectNodes, { silent: true }]);
				}
			},
			onNodeUnchecked : function(event, node) { //取消选中节点
				var selectNodes = getChildNodeIdArr(node); //获取所有子节点
				if (selectNodes) { //子节点不为空，则取消选中所有子节点
					$('#tree').treeview('uncheckNode', [selectNodes, { silent: true }]);
				}
			},
			onNodeExpanded : function(event, data) {

			},
			onNodeSelected: function (event, data) {
				initEvent(data.text);
				// alert(data.text);
				// alert(data.nodeId);
				// if(data.nodes!=null){
				// 	var select_node = $('#tree').treeview('getSelected');
				// 	if(select_node[0].state.expanded){
				// 		$('#tree').treeview('collapseNode',select_node);
				// 		select_node[0].state.selected=false;
				// 	}
				// 	else{
				// 		$('#tree').treeview('expandNode',select_node);
				// 		select_node[0].state.selected=false;
				// 	}
				// }
			}
		});
	}

	//拼接检索参数
	var setParamJson = function(text){
		// 创建请求字符串
		var searchJsonStr ={
			'name':text
		};
		searchJsonStr = encodeURIComponent(JSON.stringify(searchJsonStr));
		m.searchJson.val(searchJsonStr);
	}

    //列表展示
    var initEvent = function(text){
		//拼接检索参数
		setParamJson(text);
		paramGrid = "searchJson="+m.searchJson.val();
		tableGrid.setting("url",url_left+"/getdata");
		tableGrid.setting("param", paramGrid);
		tableGrid.setting("page", 1);
		tableGrid.loadData();
    }

	//选中/取消父节点时选中/取消所有子节点
	 function getChildNodeIdArr(node) {
		     var ts = [];
		     if (node.nodes) {
			         for (x in node.nodes) {
				             ts.push(node.nodes[x].nodeId);
				             if (node.nodes[x].nodes) {
					                 var getNodeDieDai = getChildNodeIdArr(node.nodes[x]);
					                 for (j in getNodeDieDai) {
						                     ts.push(getNodeDieDai[j]);
						                 }
					             }
				         }
			     } else {
			         ts.push(node.nodeId);
			     }
		     return ts;
		 }
	 //选中所有子节点时选中父节点
	 function setParentNodeCheck(node) {
		     var parentNode = $("#tree").treeview("getNode", node.parentId);
		     if (parentNode.nodes) {
			         var checkedCount = 0;
			         for (x in parentNode.nodes) {
				             if (parentNode.nodes[x].state.checked) {
					                 checkedCount ++;
							 } else {
								 break;
							 }
					 }
			         if (checkedCount === parentNode.nodes.length) {
				             $("#tree").treeview("checkNode", parentNode.nodeId);
				             setParentNodeCheck(parentNode);
					 }
			 }
	 }

	//表格初始化
	var initTable1 = function(){
		tableGrid = $("#zgGridTable").zgGrid({
			// title:"菜单列表",//查询结果
			title:"Menu List",//查询结果
			param:paramGrid,
			// localSort: true,
			// lineNumber: true,
			// colNames:["菜单Id","菜单父Id","菜单名称","Url","排序"],
			colNames:["Menu ID","Menu Name","Parent Menu ID","Parent Menu Name","Url","Sort"],
			colModel:[
				{name:"id",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"name",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"parentId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"parentName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"url",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"sort",type:"text",text:"right",width:"130",ishide:true,css:""}
			],//列内容
			//traverseData:data,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"id,parentId",//排序字段
			sord:"asc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTemp = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
			buttonGroup:[
				//{butType:"custom",butHtml:"<button id='add' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-plus'></span>Add</button>"},//增加 默认："",lg:最大，sm小，xs超小
				//{butType:"custom",butHtml:"<button id='modify' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-edit'></span>Modify</button>"},//增加 默认："",lg:最大，sm小，xs超小
				//{butType:"custom",butHtml:"<button id='delete' type='button' class='btn btn-danger btn-sm'><span class='glyphicon glyphicon-trash'></span>Delete</button>"}//增加 默认："",lg:最大，sm小，xs超小
			]
		});
	}

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('menu');
_start.load(function (_common) {
	_index.init(_common);
});
