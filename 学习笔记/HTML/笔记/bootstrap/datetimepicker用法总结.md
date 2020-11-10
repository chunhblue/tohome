[datetimepicker用法总结](https://blog.csdn.net/hizzyzzh/article/details/51212867#datetimepicker用法总结)



[选项属性](https://blog.csdn.net/hizzyzzh/article/details/51212867#3-选项属性)

- [1 format 格式](https://blog.csdn.net/hizzyzzh/article/details/51212867#31-format-格式)
- [2 weekStart 一周从哪一天开始](https://blog.csdn.net/hizzyzzh/article/details/51212867#32-weekstart-一周从哪一天开始)
- [3 startDate 开始时间](https://blog.csdn.net/hizzyzzh/article/details/51212867#33-startdate-开始时间)
- [4 endDate 结束时间](https://blog.csdn.net/hizzyzzh/article/details/51212867#34-enddate-结束时间)
- [5 daysOfWeekDisabled 一周的周几不能选](https://blog.csdn.net/hizzyzzh/article/details/51212867#35-daysofweekdisabled-一周的周几不能选)
- [6 autoclose 选完时间后是否自动关闭](https://blog.csdn.net/hizzyzzh/article/details/51212867#36-autoclose-选完时间后是否自动关闭)
- [7 startView 选完时间首先显示的视图](https://blog.csdn.net/hizzyzzh/article/details/51212867#37-startview-选完时间首先显示的视图)
- [8 minView 最精确的时间](https://blog.csdn.net/hizzyzzh/article/details/51212867#38-minview-最精确的时间)
- [9 maxView 最高能展示的时间](https://blog.csdn.net/hizzyzzh/article/details/51212867#39-maxview-最高能展示的时间)
- [10 todayBtn 当天日期按钮](https://blog.csdn.net/hizzyzzh/article/details/51212867#310-todaybtn-当天日期按钮)
- [11 todayHighlight 当天日期高亮](https://blog.csdn.net/hizzyzzh/article/details/51212867#311-todayhighlight-当天日期高亮)
- [12 keyboardNavigation 方向键改变日期](https://blog.csdn.net/hizzyzzh/article/details/51212867#312-keyboardnavigation-方向键改变日期)
- [13 language 语言](https://blog.csdn.net/hizzyzzh/article/details/51212867#313-language-语言)
- [14 forceParse 强制解析](https://blog.csdn.net/hizzyzzh/article/details/51212867#314-forceparse-强制解析)
- [15 minuteStep 步进值](https://blog.csdn.net/hizzyzzh/article/details/51212867#315-minutestep-步进值)
- [16 pickerPosition 选择框位置](https://blog.csdn.net/hizzyzzh/article/details/51212867#316-pickerposition-选择框位置)
- [17 showMeridian 是否显示上下午](https://blog.csdn.net/hizzyzzh/article/details/51212867#317-showmeridian-是否显示上下午)
- [18 initialDate 初始化日期时间](https://blog.csdn.net/hizzyzzh/article/details/51212867#318-initialdate-初始化日期时间)



4、结束时间

```javascript
// 开始日
        m.ud_start_date.datetimepicker({
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
                m.ud_end_date.datetimepicker('setStartDate', new Date(ev.date.valueOf()))
            } else {
                m.ud_end_date.datetimepicker('setStartDate', null);
            }

        });
        // 结束日
        m.ud_end_date.datetimepicker({
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
                m.ud_start_date.datetimepicker('setEndDate', new Date(ev.date.valueOf()))
            } else {
                m.ud_start_date.datetimepicker('setEndDate', new Date());
            }
        });
```

