[#ftl]
<div class="container-fluid">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
    		<div class="my-nav">
    			<div class="row">
					<div class="col-xs-12 col-sm-9 col-md-9 col-lg-10 ">
						<div class="nav-box">
							<ol class="breadcrumb">
							  [#list text?split("&") as item ]
							  	[#if !item_has_next]
								  <li class="active">${item}</li>
							  	[/#if] 
							  	[#if item_has_next]
								  <li>${item}</li>
							  	[/#if]
							  [/#list]
							</ol>
						</div>
					</div>
					<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2 ">
						<div style="white-space: nowrap" class="nav-time">
							<span class="glyphicon glyphicon-time"></span>
							<span id="timeNow" >


								 </span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
</div>
<script language=JavaScript>
	window.onload=function(){
		//每1秒刷新时间
		setInterval("getNowTime()",1000);
	}
	function getNowTime(){
		today=new Date();
		HH = today.getHours();
		Minutes = today.getMinutes();
		seconds = today.getSeconds();
		Month=today.getMonth()+1;
		function initArray(){this.length=initArray.arguments.length;
			for(var i=0;i<this.length;i++) this[i+1]=initArray.arguments[i]};
		var d=new initArray("Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday","Saturday");
		document.getElementById("timeNow").innerHTML=today.getDate()+"/"+Month+"/"+today.getFullYear()+" "+
		checkZero(HH)+":"+checkZero(Minutes)+":"+checkZero(seconds)+" "+
				d[today.getDay()+1]
	}
	function checkZero(i){
		var num = (i<10)?("0"+i) : i;
		return num;
	}
</script>