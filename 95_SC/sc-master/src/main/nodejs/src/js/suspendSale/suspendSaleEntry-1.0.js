require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('suspendSaleEntry', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrObjValue = {},//临时行数据存储
        a_store = null,
        a_articleId = null,
        a_region = null,
        common = null;
    var m = {
        toKen: null,
        use: null,
        reset: null,
        businessDate: null,//业务日期
        storeCd: null,
        acc_date: null,
        changeId: null,
        operateFlg: null,//判断新增修改 1新增 2修改
        cancel: null,//取消
        affirm: null,// 确认
        barcode: null,
        articleId: null,
        articleName: null,
        spec: null,
        unitName: null,
        saleStatus: null,//销售状态
        oldPrice: null,//当前价格
        nextBtn: null,//下一变价单按钮
        submitBtn: null,//提交按钮
        aRegion: null,
        aCity: null,
        aDistrict: null,
        aStore: null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/suspendSaleEntry";
        systemPath = _common.config.surl;
        reThousands = _common.reThousands;
        toThousands = _common.toThousands;
        getThousands = _common.getThousands;
        //初始化下拉
        initAutoMatic();
        // 初始化组织架构
        initOrganization();
        //事件绑定
        but_event();
        //列表初始化
        initTable1();
        //表格内按钮事件
        table_event();
        // initArticleId();
        m.changeId.val("");
    }

    /**
     * 初始化店铺运营组织检索
     *
     */
    var initOrganization = function () {
        // 初始化，子级禁用
        $("#aCity").attr("disabled", true);
        $("#aDistrict").attr("disabled", true);
        $("#aStore").attr("disabled", true);
        $("#cityRefresh").hide();
        $("#cityRemove").hide();
        $("#districtRefresh").hide();
        $("#districtRemove").hide();
        $("#storeRefresh").hide();
        $("#storeRemove").hide();
        // 输入框事件绑定
        a_region = $("#aRegion").myAutomatic({
            url: url_left + "/selectListByLevel",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_city);
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                $("#aCity").attr("disabled", true);
                $("#aDistrict").attr("disabled", true);
                $("#aStore").attr("disabled", true);
                $("#cityRefresh").hide();
                $("#cityRemove").hide();
                $("#districtRefresh").hide();
                $("#districtRemove").hide();
                $("#storeRefresh").hide();
                $("#storeRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    $("#aCity").attr("disabled", false);
                    $("#cityRefresh").show();
                    $("#cityRemove").show();
                }
                var rinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&level=1&adminId=" + rinput + "&articleId=" + m.articleId.attr("k") + '&accDate=' + subfmtDate(m.acc_date.val());
                $.myAutomatic.replaceParam(a_city, str);
            }
        });
        a_city = $("#aCity").myAutomatic({
            url: url_left + "/selectListByLevel",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                $("#aDistrict").attr("disabled", true);
                $("#aStore").attr("disabled", true);
                $("#districtRefresh").hide();
                $("#districtRemove").hide();
                $("#storeRefresh").hide();
                $("#storeRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    $("#aDistrict").attr("disabled", false);
                    $("#districtRefresh").show();
                    $("#districtRemove").show();
                }
                var cinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&level=2&adminId=" + cinput + "&articleId=" + m.articleId.attr("k") + '&accDate=' + subfmtDate(m.acc_date.val());
                $.myAutomatic.replaceParam(a_district, str);
            }
        });
        a_district = $("#aDistrict").myAutomatic({
            url: url_left + "/selectListByLevel",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_store);
                $("#aStore").attr("disabled", true);
                $("#storeRefresh").hide();
                $("#storeRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    $("#aStore").attr("disabled", false);
                    $("#storeRefresh").show();
                    $("#storeRemove").show();
                }
                var dinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&level=3&adminId=" + dinput + "&articleId=" + m.articleId.attr("k") + '&accDate=' + subfmtDate(m.acc_date.val());
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_store = $("#aStore").myAutomatic({
            url: url_left + "/selectListByLevel",
            ePageSize: 10,
            startCount: 0,
            cleanInput: function () {
                m.barcode.val("");
            },
            selectEleClick: function (thisObj) {
                let sinput = thisObj.attr("k");
                let itemCode = m.articleId.attr("k");
                getBarcodeByInfo(sinput, itemCode, subfmtDate(m.acc_date.val()));
            }
        });

        // 选值栏位清空按钮事件绑定
        $("#regionRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_city);
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $("#aCity").attr("disabled", true);
            $("#aDistrict").attr("disabled", true);
            $("#aStore").attr("disabled", true);
            $("#cityRefresh").hide();
            $("#cityRemove").hide();
            $("#districtRefresh").hide();
            $("#districtRemove").hide();
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        });
        $("#cityRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $("#aDistrict").attr("disabled", true);
            $("#aStore").attr("disabled", true);
            $("#districtRefresh").hide();
            $("#districtRemove").hide();
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        });
        $("#districtRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_store);
            $("#aStore").attr("disabled", true);
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        });
    };

    var getBarcodeByInfo = function (storeCd,articleId,accDate) {
        $.myAjaxs({
            url:systemPath+"/priceChange/getBarcode",
            async:true,
            cache:false,
            type :"post",
            data :{
                storeCd:storeCd,
                articleId: articleId,
                businessDate:accDate
            },
            dataType:"json",
            success:function(result){
                m.barcode.val(result);
            },
            error:function () {
                _common.prompt("Get barcode error!",5,"error");
            }
        })
    };

    var getItemInfo = function (articleId, accDate) {
        if (!articleId || !accDate) {
            return;
        }
        $.myAjaxs({
            url: url_left + "/getItemInfo",
            async: true,
            cache: false,
            type: "get",
            data: {
                articleId: articleId,
                effectiveDate: accDate
            },
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    let record = result.o;
                    m.spec.val(record.spec);
                    m.unitName.val(record.unitName);
                    m.barcode.val(record.barcode);
                    m.oldPrice.val(toThousands(record.oldPrice));
                    // 替换子级查询参数
                    var str = "&level=0&adminId=''&articleId=" + articleId + '&accDate=' + subfmtDate(m.acc_date.val());
                    $.myAutomatic.replaceParam(a_region, str);
                } else {
                    _common.prompt(result.msg, 5, "error", function () {
                        clearItemInfo();
                    }, true);
                }
            },
            error: function (e) {
                _common.prompt("Request failed!", 5, "error");
            }
        });
    };

    var clearItemInfo = function () {
        m.articleId.val("").attr('k', '').attr('v', '');
        m.spec.val("");
        m.barcode.val("");
        m.unitName.val("");
        m.oldPrice.val("");
    }

    var table_event = function () {
        $("#add").on("click", function () {
            var articleId = $("#articleId").attr("k");
            var accDate = subfmtDate(m.acc_date.val());
            if (articleId == null || articleId === "") {
                _common.prompt("Please select a item first!", 5, "error");/*请选择商品*/
                $("#articleId").css("border-color", "red");
                $("#articleId").focus();
                return false;
            } else {
                $("#articleId").css("border-color", "#CCCCCC");
            }
            if (accDate == null || accDate === "") {
                _common.prompt("Please select business date！", 5, "error");/*请选择营业日期*/
                $("#acc_date").css("border-color", "red");
                m.acc_date.focus();
                return false;
            } else {
                $("#acc_date").css("border-color", "#CCCCCC");
            }
            m.operateFlg.val("1");
            $("#regionRemove").click();
            $('#priceChange_dialog').modal("show");
        });

        m.cancel.on("click", function () {
            _common.myConfirm("Are you sure you want to cancel", function (result) {
                $("#articleId").css("border-color", "#CCCCCC");
                if (result == "true") {
                    $('#priceChange_dialog').modal("hide");
                }
            })
        })

        //删除
        $("#delete").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select store！", 5, "info");/*请选择商品*/
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp, "storeCd");
            _common.myConfirm("Are you sure you want to delete the selected store?", function (result) {/*请确认是否要删除选中的商品*/
                if (result == "true") {
                    $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                        var storeCd = $(this).find('td[tag=storeCd]').text();
                        if (storeCd == cols["storeCd"]) {
                            $(this).remove();
                            return;
                        }
                    });
                    _common.prompt("Data delected successfully", 2, "success");/*数据删除成功*/
                }
            });
        });

        //新增加确认
        m.affirm.on("click", function () {
            let operateFlg = m.operateFlg.val();
            let regionCd = m.aRegion.attr("k");
            let cityCd = m.aCity.attr("k");
            let districtCd = m.aDistrict.attr("k");
            let storeCd = m.aStore.attr("k");
            m.aRegion.css("border-color", "#CCCCCC");
            m.aCity.css("border-color", "#CCCCCC");
            m.aDistrict.css("border-color", "#CCCCCC");
            if (!regionCd) {
                m.aRegion.css("border-color", "red");
                return;
            }
            if (!cityCd) {
                m.aCity.css("border-color", "red");
                return;
            }
            if (!districtCd) {
                m.aDistrict.css("border-color", "red");
                return;
            }

            // 只选择到 district 这一级, 添加下面所有的店铺
            if (!storeCd) {
                _common.myConfirm("Are you sure you want to add all stores under the district?", function (result) {/*要添加该区域下的所有商店吗?*/
                    if (result == "true") {
                        if (operateFlg == "1") {
                            appendTrList();
                        }
                        _common.prompt("Data save successfully", 2, "success");/*数据保存成功*/
                        $('#priceChange_dialog').modal("hide");
                    }
                });
                return;
            }

            let flg = false;
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                let _storeCd = $(this).find('td[tag=storeCd]').text();
                if (storeCd == _storeCd) {
                    flg = true;
                }
            });
            // 存在相同的店铺
            if (flg) {
                _common.prompt("The selected store already exists!",3,"info");
                return;
            }
            _common.myConfirm("Are you sure to save the data?", function (result) {/*请确认是否要保存*/
                if (result == "true") {
                    if (operateFlg == "1") {
                        appendTr();
                    }
                    _common.prompt("Data save successfully", 2, "success");/*数据保存成功*/
                    $('#priceChange_dialog').modal("hide");
                }
            });
        });
    }

    //表格新增
    var appendTrList = function () {
        let regionCd = m.aRegion.attr("k").trim();
        let regionName = m.aRegion.attr("v").replace(regionCd,'').trim();
        let cityCd = m.aCity.attr("k").trim();
        let cityName = m.aCity.attr("v").replace(cityCd,'').trim();
        let districtCd = m.aDistrict.attr("k").trim();
        let districtName = m.aDistrict.attr("v").replace(districtCd,'').trim();
        // 等待框开启
        _common.loading();
        $.myAjaxs({
            url: url_left + "/getStoreListByDistrictCd",
            async: true,
            cache: false,
            type: "get",
            data: {
                'cityCd':cityCd,
                'districtCd': districtCd,
                'articleId': m.articleId.attr('k'),
                'accDate': subfmtDate(m.acc_date.val())
            },
            dataType: "json",
            success: function (result) {
                let trs = '';
                for (let i = 0; i < result.length; i++) {
                    var No = Number(serialNoAuto())+i;
                    No = PrefixZero(No,4);
                    var rowindex = $("#zgGridTtable>.zgGrid-tbody tr").length+i;
                    let item = result[i];
                    let flg = false;
                    $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                        let _storeCd = $(this).find('td[tag=storeCd]').text();
                        if (item.k == _storeCd) {
                            flg = true;
                        }
                    });
                    // 存在相同的店铺
                    if (flg) {
                        continue;
                    }
                    trs += '<tr id="zgGridTtable_' + rowindex + '_tr" class="">' +
                    '<td tag="serialNo" width="100" title="' + No + '" align="center" id="zgGridTtable_' + rowindex + '_tr_serialNo" tdindex="zgGridTtable_serialNo">' + No + '</td>' +
                    '<td tag="regionCd" width="130" title="' + regionCd + '" align="center" id="zgGridTtable_' + rowindex + '_tr_regionCd" tdindex="zgGridTtable_regionCd">' + regionCd + '</td>' +
                    '<td tag="regionName" width="130" title="' + regionName + '" align="center" id="zgGridTtable_' + rowindex + '_tr_regionName" tdindex="zgGridTtable_regionName">' + regionName + '</td>' +
                    '<td tag="cityCd" width="130" title="' + cityCd + '" align="center" id="zgGridTtable_' + rowindex + '_tr_cityCd" tdindex="zgGridTtable_cityCd">' + cityCd + '</td>' +
                    '<td tag="cityName" width="130" title="' + cityName + '" align="center" id="zgGridTtable_' + rowindex + '_tr_cityName" tdindex="zgGridTtable_cityName">' + cityName + '</td>' +
                    '<td tag="districtCd" width="130" title="' + districtCd + '" align="center" id="zgGridTtable_' + rowindex + '_tr_districtCd" tdindex="zgGridTtable_districtCd">' + districtCd + '</td>' +
                    '<td tag="districtName" width="130" title="' + districtName + '" align="center" id="zgGridTtable_' + rowindex + '_tr_districtName" tdindex="zgGridTtable_districtName">' + districtName + '</td>' +
                    '<td tag="storeCd" width="130" title="' + item.k + '" align="center" id="zgGridTtable_' + rowindex + '_tr_storeCd" tdindex="zgGridTtable_storeCd">' + item.k + '</td>' +
                    '<td tag="storeName" width="130" title="' + item.v + '" align="center" id="zgGridTtable_' + rowindex + '_tr_storeName" tdindex="zgGridTtable_storeName">' + item.v + '</td>' +
                    '<td tag="barcode" width="115" title="' + item.hidek + '" align="center" id="zgGridTtable_' + rowindex + '_tr_barcode" tdindex="zgGridTtable_barcode">' + item.hidek + '</td>' +
                    '</tr>';
                }
                // 等待框关闭
                _common.loading_close();
                tableGrid.append(trs);
            }
        });
    }

    //表格新增
    var appendTr = function () {
        var No = serialNoAuto();
        var rowindex = $("#zgGridTtable>.zgGrid-tbody tr").length;
        let regionCd = m.aRegion.attr("k").trim();
        let regionName = m.aRegion.attr("v").replace(regionCd,'').trim();
        let cityCd = m.aCity.attr("k").trim();
        let cityName = m.aCity.attr("v").replace(cityCd,'').trim();
        let districtCd = m.aDistrict.attr("k").trim();
        let districtName = m.aDistrict.attr("v").replace(districtCd,'').trim();
        let storeCd = m.aStore.attr("k").trim();
        let storeName = m.aStore.attr("v").replace(storeCd,'').trim();
        let barcode = m.barcode.val();

        var tr = '<tr id="zgGridTtable_' + rowindex + '_tr" class="">' +
            '<td tag="serialNo" width="100" title="' + No + '" align="center" id="zgGridTtable_' + rowindex + '_tr_serialNo" tdindex="zgGridTtable_serialNo">' + No + '</td>' +
            '<td tag="regionCd" width="130" title="' + regionCd + '" align="center" id="zgGridTtable_' + rowindex + '_tr_regionCd" tdindex="zgGridTtable_regionCd">' + regionCd + '</td>' +
            '<td tag="regionName" width="130" title="' + regionName + '" align="center" id="zgGridTtable_' + rowindex + '_tr_regionName" tdindex="zgGridTtable_regionName">' + regionName + '</td>' +
            '<td tag="cityCd" width="130" title="' + cityCd + '" align="center" id="zgGridTtable_' + rowindex + '_tr_cityCd" tdindex="zgGridTtable_cityCd">' + cityCd + '</td>' +
            '<td tag="cityName" width="130" title="' + cityName + '" align="center" id="zgGridTtable_' + rowindex + '_tr_cityName" tdindex="zgGridTtable_cityName">' + cityName + '</td>' +
            '<td tag="districtCd" width="130" title="' + districtCd + '" align="center" id="zgGridTtable_' + rowindex + '_tr_districtCd" tdindex="zgGridTtable_districtCd">' + districtCd + '</td>' +
            '<td tag="districtName" width="130" title="' + districtName + '" align="center" id="zgGridTtable_' + rowindex + '_tr_districtName" tdindex="zgGridTtable_districtName">' + districtName + '</td>' +
            '<td tag="storeCd" width="130" title="' + storeCd + '" align="center" id="zgGridTtable_' + rowindex + '_tr_storeCd" tdindex="zgGridTtable_storeCd">' + storeCd + '</td>' +
            '<td tag="storeName" width="130" title="' + storeName + '" align="center" id="zgGridTtable_' + rowindex + '_tr_storeName" tdindex="zgGridTtable_storeName">' + storeName + '</td>' +
            '<td tag="barcode" width="115" title="' + barcode + '" align="center" id="zgGridTtable_' + rowindex + '_tr_barcode" tdindex="zgGridTtable_barcode">' + barcode + '</td>' +
            '</tr>';
        tableGrid.append(tr);
    }
    //序号排序
    var serialNoAuto = function () {
        var No = 1;
        $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
            $(this).find('td[tag=serialNo]').text(PrefixZero(No, 4));
            No++;
        })
        return PrefixZero(No, 4);
    }

    //画面按钮点击事件
    var but_event = function () {
        //订货日
        m.acc_date.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });

        //提交变价信息
        m.submitBtn.on("click", function () {
            let articleId = m.articleId.attr("k");
            let accDate = subfmtDate(m.acc_date.val());
            let changeId = m.changeId.val();
            let oldPrice = m.oldPrice.val();
            if (articleId == null || articleId === "") {
                _common.prompt("Please select a item first!", 5, "error");/*请选择店铺*/
                $("#articleId").css("border-color", "red");
                $("#articleId").focus();
                return false;
            }else {
                $("#articleId").css("border-color", "#CCC");
            }
            if (accDate == null || accDate === "") {
                _common.prompt("Please select business date！", 5, "error");/*请选择营业日期*/
                m.acc_date.focus();
                return false;
            }
            if ($("#zgGridTtable>.zgGrid-tbody tr").length < 1) {
                _common.prompt("Please select a store first!", 5, "error");/*请先选择店铺!*/
                $("#add").click();
                return false;
            }
            var storeDetail = [];
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                var obj = {
                    articleId: articleId,
                    accDate: accDate,
                    changeId: changeId,
                    serialNo: $(this).find('td[tag=serialNo]').text(),//序号
                    storeCd: $(this).find('td[tag=storeCd]').text(),//商品码
                    barcode: $(this).find('td[tag=barcode]').text(),//条码
                    oldPrice: reThousands(oldPrice),//当前价格
                    newPrice: "-1",// 暂停销售, 价格 -1
                }
                storeDetail.push(obj);
            });
            var itemDetailJson = "";
            if (storeDetail.length > 0) {
                itemDetailJson = JSON.stringify(storeDetail)
            }

            _common.myConfirm("Are you sure to save the data?", function (result) {/*请确认是否要保存*/
                if (result == "true") {
                    $.myAjaxs({
                        url: url_left + "/save",
                        async: true,
                        cache: false,
                        type: "post",
                        data: {
                            articleId: articleId,
                            accDate: accDate,
                            changeId: changeId,
                            itemDetailJson: itemDetailJson
                        },
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                _common.prompt("Data saved successfully！", 5, "info");
                                let record = result.data;
                                let changeId = record.changeId;
                                m.changeId.val(changeId);
                                //禁用页面按钮
                                disableAll();
                            } else {
                                _common.prompt(result.message, 5, "error");
                            }
                        },
                        error: function (e) {
                            _common.prompt("Data saved failed！", 5, "error");
                        },
                        complete: _common.myAjaxComplete
                    });
                }
            });

        });

        //下一张变价单
        m.nextBtn.on("click", function () {
            var subStatus = m.submitBtn.prop("disabled");
            if (!subStatus && $("#zgGridTtable>.zgGrid-tbody tr").length > 0) {
                _common.myConfirm("Data has not been saved. Do you want to save it?", function (result) {/*数据尚未保存，是否保存？*/
                    if (result == "true") {
                        m.submitBtn.click();
                    } else {
                        //启用页面按钮
                        enableAll();
                        //清空页面信息
                        $("#a_item_clear").click();
                        m.changeId.val("");
                        $("#a_store").css("border-color", "#CCC");
                        $("#acc_date").css("border-color", "#CCC");
                    }
                })
            } else {
                //启用页面按钮
                enableAll();
                //清空页面信息
                $("#a_item_clear").click();
                m.changeId.val("");
                $("#a_store").css("border-color", "#CCC");
                $("#acc_date").css("border-color", "#CCC");
            }
        })

        m.acc_date.on("change", function () {
            $(".zgGrid-tbody").empty();
        })
    }

    //禁用页面按钮
    var disableAll = function () {
        $("#auto_store").addClass("disable");
        $("#a_store").prop("disabled", true);
        m.articleId.prop("disabled",true);
        $("#a_item_refresh").hide();
        $("#a_item_clear").hide();
        m.changeId.prop("disabled", true);
        $("#add").prop("disabled", true);
        $("#update").prop("disabled", true);
        $("#delete").prop("disabled", true);
        m.submitBtn.prop("disabled", true);
    }

    //启用页面按钮
    var enableAll = function () {
        $("#auto_store").removeClass("disable");
        $("#a_store").prop("disabled", false);
        m.articleId.prop("disabled",false);
        $("#a_item_refresh").show();
        $("#a_item_clear").show();
        m.changeId.prop("disabled", false);
        $("#add").prop("disabled", false);
        $("#update").prop("disabled", false);
        $("#delete").prop("disabled", false);
        m.submitBtn.prop("disabled", false);
    }


    //初始化自动下拉
    var initAutoMatic = function () {
        a_articleId = $("#articleId").myAutomatic({
            url: url_left + "/getAllArticleIdAndName",
            ePageSize: 5,
            startCount: 3,
            param: [
                {
                    'k': 'effectiveDate',
                    'v': 'acc_date'
                }
            ],
            cleanInput: function (thisObj) {
                clearItemInfo();
                $("#zgGridTtable  tr:not(:first)").remove();
            },
            selectEleClick: function (thisObj) {
                getItemInfo(thisObj.attr('k'), subfmtDate(m.acc_date.val()));
            }
        });
    }

    //表格初始化-紧急变价商品明细列表样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Details",
            param: paramGrid,
            height: "300",
            colNames: ["Seq No","Region No.","Region Name", "City No.","City Name", "District No.","District Name", "Store No.","Store Name","Barcode"],
            colModel: [
                {name:"serialNo",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name: "regionCd", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "regionName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "cityCd", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "cityName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "districtCd", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "districtName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "storeCd", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "storeName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "barcode", type: "text", text: "right", width: "115", ishide: false, css: ""},
            ],//列内容
            width: "max",//宽度自动
            isPage: false,//是否需要分页
            isCheckbox: false,
            freezeHeader:true,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                {
                    butType: "add",
                    butId: "add",
                    butText: "Add",
                    butSize: ""//
                },// 新增
                {
                    butType: "delete",
                    butId: "delete",
                    butText: "Delete",
                    butSize: ""//,
                },//删除
            ],
        });
    }

    /**
     * 自定义函数名：PrefixZero
     * @param num： 被操作数
     * @param n： 固定的总位数
     */
    function PrefixZero(num, n) {
        return (Array(n).join(0) + num).slice(-n);
    }

    function subfmtDate(date) {
        var res = "";
        if (date != null && date != "") {
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        }
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('suspendSaleEntry');
_start.load(function (_common) {
    _index.init(_common);
});
