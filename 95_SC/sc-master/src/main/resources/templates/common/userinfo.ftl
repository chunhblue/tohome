[#ftl]
<STYLE>
	/*小屏幕*/
	@media (max-width: 767px) {
		.tool {
			display: none;
			/*visibility: hidden;*/
		}
		.username {
			width: auto;
		}
		.username > a {
			width: auto;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}
	}
	/*大屏幕*/
	@media (min-width: 768px) {
		.tool {
			display: inline;
			/*visibility: initial;*/
		}
		.username {
			width: auto;
		}
		.username > a {
			width: auto;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}
	}
</STYLE>

[#if Session.IY_MASTER_USER??]
	<script src="${m.staticpath}/[@spring.message 'js.userinfo'/]"></script>
	<ul class="nav navbar-nav navbar-right div-user">
		<li class="username">
			<a href="#" class="dropdown-toggle user-a" data-toggle="dropdown" title="${Session.IY_MASTER_USER.user.userName!}">
				<span>${Session.IY_MASTER_USER.user.dptName!}</span>
				<span>${Session.IY_MASTER_USER.user.userName!}</span>
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu" role="menu">
				<li><a href="#" data-toggle="modal" data-target="#myOut"><span class="glyphicon glyphicon-log-out"></span> Log Out</a></li>
			</ul>
		</li>
	</ul>
	<ul class="nav navbar-nav navbar-right div-user tool">
		<a href="#" class="navbar-brand "></a>
			[#--遍历 aTODO Task 和 通报--]
			[#if Session.IY_MASTER_MENUS?size != 0]
				[#list Session.IY_MASTER_MENUS as item ]
					[#if item.name?default("") == "Message"]
						[#if item.menuItems?exists]
							[#list item.menuItems as child ]
								[#if child.url?default("") == "index"]
									<li>
										<a class="dropdown-toggle" href="${syspath}/${child.url!}" title="${child.name!}"><span class="glyphicon glyphicon-list-alt" aria-hidden="true" style="font-size:20px;position:relative;"></span></a>
									</li>
								[/#if]
								[#if child.url?default("") == "informReply"]
									<li>
[#--										<a class="dropdown-toggle" href="${syspath}/${child.url!}" title="${child.name!}">--]
[#--											<span class="glyphicon glyphicon-bell" aria-hidden="true" style="font-size:20px;position:relative;"></span>--]
[#--											<span id="notifications" style="font-size:11px;position:absolute;left:28px;top:10px;border-radius: 50%;height: 20px;width: 20px;display: inline-block;background: #f20c55;vertical-align: top;">--]
[#--												<span id="count" style="display: block;color: #FFFFFF;height: 20px;line-height: 20px;text-align: center">--]
[#--												</span>--]
[#--											</span>--]
[#--										</a>--]
										<a href="#" class="dropdown-toggle user-a" data-toggle="dropdown" title="">
											<span class="glyphicon glyphicon-bell" aria-hidden="true" style="font-size:20px;position:relative;"></span>
											<span id="notifications" style="display: none;font-size:11px;position:absolute;left:28px;top:10px;border-radius: 50%;height: 20px;width: 20px;background: #f20c55;vertical-align: top;">
												<span id="count" style="display: block;color: #FFFFFF;height: 20px;line-height: 20px;text-align: center">
												</span>
											</span>
										</a>
										<ul class="dropdown-menu" >
											<li>
                                                <a href="${syspath}/${child.url!}" data-toggle="modal">
													<span class="glyphicon glyphicon-info-sign"></span>
                                                    Notification
													<span class="badge" id="notification" style="background-color:red;margin-top:-5px;"></span>
												</a>
											</li>

											<li>
                                                <a href="${syspath}/informNewProduct?inform=true" data-toggle="modal">
													<span class="glyphicon glyphicon-info-sign"></span>
                                                    New Item
													<span class="badge" id="newItem" style="background-color:red;margin-top:-5px;"></span>
												</a>
											</li>

											<li>
                                                <a href="${syspath}/informPromotion?inform=true" data-toggle="modal">
													<span class="glyphicon glyphicon-info-sign"></span>
                                                    MM Promotion
													<span class="badge" id="mmPromotion" style="background-color:red;margin-top:-5px;"></span>
												</a>
											</li>
										</ul>
									</li>
								[/#if]
							[/#list]
						[/#if]
					[/#if]
				[/#list]
			[/#if]
		</ul>
[/#if]

