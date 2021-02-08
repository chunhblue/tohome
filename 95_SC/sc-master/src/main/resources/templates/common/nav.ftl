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
						<div class="nav-time">
							<span class="glyphicon glyphicon-time"></span> 
							<span>
									<script language=JavaScript>
										today=new Date();
										function initArray(){this.length=initArray.arguments.length;
										for(var i=0;i<this.length;i++) this[i+1]=initArray.arguments[i]};
										var d=new initArray("Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday","Saturday");
										// document.write("<font class=date>",today.getFullYear(),"年",today.getMonth()+1,"月",today.getDate(),"日 ", d[today.getDay()+1],"</font>");
										document.write("<font class=date>",today.getDate(),"/",today.getMonth()+1,"/",today.getFullYear()," ", d[today.getDay()+1],"</font>");
									</script>
								 </span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
</div>	