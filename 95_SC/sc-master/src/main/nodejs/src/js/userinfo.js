
var _common = require('common');
var _myAjax=require("myAjax");
define('userinfo', function () {
    var systemPath = _common.config.surl;
    $("#notifications").hide();
    $("#count").text();
    //轮询时间 *秒
    var time = 30;
    setTimer();
    //轮询获取通知数量
    function setTimer(){
        let timer ;
        $.myAjaxs({
            url:systemPath+"/getNotificationsCount",
            async:true,
            cache:false,
            type :"get",
            dataType:"json",
            success:function(result){
                if(result){
                    if(result.success){
                        let count = result.data.count;
                        let notificationCount = result.data.notificationCount;
                        let mmPromotionCount = result.data.mmPromotionCount;
                        let newItemCount = result.data.newItemCount;
                        if(count==0){
                              $("#notifications").hide();
                        }else if(count>99){
                            $("#count").text("99+");
                            $("#notifications").show('slow');
                        }else{
                            $("#count").text(count);
                            $("#notifications").show('slow');
                        }
                        if(notificationCount==0){
                            $("#notification").hide();
                        }else if(notificationCount>99){
                            $("#notification").text("99+");
                            $("#notification").show();
                        }else{
                            $("#notification").text(notificationCount);
                            $("#notification").show();
                        }
                        if(newItemCount==0){
                            $("#newItem").hide();
                        }else if(newItemCount>99){
                            $("#newItem").text("99+");
                            $("#newItem").show();
                        }else{
                            $("#newItem").text(newItemCount);
                            $("#newItem").show();
                        }
                        if(mmPromotionCount==0){
                            $("#mmPromotion").hide();
                        }else if(mmPromotionCount>99){
                            $("#mmPromotion").text("99+");
                            $("#mmPromotion").show();
                        }else{
                            $("#mmPromotion").text(mmPromotionCount);
                            $("#mmPromotion").show();
                        }
                    }else{
                        $("#notifications").hide();
                        $("#notification").hide();
                        $("#newItem").hide();
                        $("#mmPromotion").hide();
                    }
                    // timer = setTimeout(() => {
                    //     setTimer();
                    // }, time*1000)
                    timer = setTimeout(function () {
                        setTimer();
                    }, time*1000)
                }else {
                    clearTimeout(timer); //清理定时任务
                }
            },
            error : function(e){
                _common.prompt("Notifications failed to load data!",5,"error");
                clearTimeout(timer); //清理定时任务
            },
            complete:_common.myAjaxComplete
        });
    }
});
var _index = require('userinfo');
