## datetimepicker设置结束时间>开始时间

```javascript
$("#startDate").datetimepicker({
    language:'en',    
    format: 'dd/mm/yyyy',   
    maxView:4,  
    startView:2,   
    minView:2,   
    autoclose:true,   
    todayHighlight:true,   
    todayBtn:true
}).on('changeDate', function (ev) { 
    if (ev.date) {     
        $("#endDate").datetimepicker('setStartDate', new Date(ev.date.valueOf()))  
    } else {     
        $("#endDate").datetimepicker('setStartDate', null);  
    }
});
$("#endDate").datetimepicker({ 
    language:'en',  
    format: 'dd/mm/yyyy',   
    maxView:4,   
    startView:2,   
    minView:2,  
    autoclose:true,  
    todayHighlight:true,   
    todayBtn:true}).on('changeDate', function (ev) { 
    if (ev.date) {    
        $("#startDate").datetimepicker('setEndDate', new Date(ev.date.valueOf()))   
    } else {    
        $("#startDate").datetimepicker('setEndDate', new Date());  
    }
});
```

**效果：选择endDate的时候，不可以选择startDate之前的日期**