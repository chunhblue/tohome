设置初始日期，及日期函数

```js
var initDate = function(startDate,endDate) {
		if (startDate) {
				startDate.datetimepicker({
					language:'en',
					format: 'dd/mm/yyyy',
					maxView: 4,
					startView: 2,
					minView: 2,
					autoclose: true,
					todayHighlight: true,
					todayBtn: true,
				}).on('click', function (ev) {
					if(position===0){ // 根据条件来对开始日期做选择限制
						$("#bs_start_date").datetimepicker("setStartDate", new Date(new Date().setMonth(new Date().getMonth()-2)));
					}else {
						// 只显示一年的时间
						$("#bs_start_date").datetimepicker("setStartDate",new Date(new Date()-1000 * 60 * 60 * 24 * 365));
					}
				});
			}

			// 倒推两个月
			let _start = new Date(new Date().setMonth(new Date().getMonth()-2));
			if (!endDate) {
				_start = new Date();
			}
			startDate.val(_start.Format('dd/MM/yyyy'));

		if (endDate) {
			endDate.datetimepicker({
				language:'en',
				format: 'dd/mm/yyyy',
				maxView: 4,
				startView: 2,
				minView: 2,
				autoclose: true,
				todayHighlight: true,
				todayBtn: true,
			}).on('click', function (ev) {
				$("#bs_end_date").datetimepicker("setStartDate",new Date(fmtDate($("#bs_start_date").val())));
			});
			// 默认日期当天
			endDate.val(new Date().Format('dd/MM/yyyy'));
		}
	};
```

