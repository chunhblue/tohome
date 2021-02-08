[#ftl]
<STYLE>
    /*小屏幕*/
    @media (max-width: 767px) {
        .navItem {
            width: 100%;
        }

        .navItem > a {
            white-space: normal;
            overflow: visible;
            text-overflow: ellipsis;
        }

        .menuAll {
            display: inline;
        }

        .menu5, .menu4, .menu10, .menu7, .menu9, .menu8,.menu3  {
            display: none;
        }
    }

    /*小屏幕*/
    @media only screen and (min-width: 768px) and (max-width: 817px) {
        .navItem {
            /*width: 58px;*/
        }

        .navItem > a {
            /*width: 58px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu10,.menu4, .menuAll, .menu7, .menu9, .menu8 {
            display: none;
        }

        .menu3 {
            display: inline;
        }
    }

    /*中等屏幕*/
    @media only screen and (min-width: 818px) and (max-width: 889px) {
        .navItem {
            /*width: 65px;*/
        }

        .navItem > a {
            /*width: 65px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu10, .menuAll, .menu7, .menu9, .menu8,.menu3  {
            display: none;
        }

        .menu4 {
            display: inline;
        }
    }

    @media only screen and (min-width: 890px) and (max-width: 991px) {
        .navItem {
            /*width: 69px;*/
        }

        .navItem > a {
            /*width: 69px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu10, .menuAll, .menu7, .menu9, .menu8,.menu3  {
            display: none;
        }

        .menu4 {
            display: inline;
        }
    }


    /*中等屏幕*/
    /*@media only screen and (min-width: 992px) and (max-width: 1190px) {*/
    @media only screen and (min-width: 992px) and (max-width: 1089px) {
        .navItem {
            /*width: 95px;*/
        }

        .navItem > a {
            /*width: 95px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu7,.menu10, .menu4, .menuAll, .menu9, .menu8,.menu3  {
            display: none;
        }

        .menu5 {
            display: inline;
        }
    }

    /*@media only screen and (min-width: 992px) and (max-width: 1505px) {*/
    @media only screen and (min-width: 1090px) and (max-width: 1190px) {
        .navItem {
            /*width: 95px;*/
        }

        .navItem > a {
            /*width: 95px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu10, .menuAll, .menu7, .menu9, .menu8,.menu3  {
            display: none;
        }

        .menu4 {
            display: inline;
        }
    }

    /*1380*/
    @media only screen and (min-width: 1191px) and (max-width: 1302px) {
        .navItem {
            /*width: 95px;*/
        }

        .navItem > a {
            /*width: 95px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu10, .menuAll, .menu4, .menu9, .menu8,.menu3  {
            display: none;
        }

        .menu7 {
            display: inline;
        }
    }

    /*1380*/
    @media only screen and (min-width: 1303px) and (max-width: 1345px) {
        .navItem {
            /*width: 95px;*/
        }

        .navItem > a {
            /*width: 95px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu7, .menuAll, .menu4, .menu9, .menu10,.menu3  {
            display: none;
        }

        .menu8 {
            display: inline;
        }
    }

    /*1380*/
    @media only screen and (min-width: 1346px) and (max-width: 1450px) {
        .navItem {
            /*width: 95px;*/
        }

        .navItem > a {
            /*width: 95px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu10, .menuAll, .menu4, .menu7, .menu8,.menu3  {
            display: none;
        }

        .menu9 {
            display: inline;
        }
    }


    /*1500*/
    @media only screen and (min-width: 1451px) and (max-width: 1503px) {
        .navItem {
            /*width: 95px;*/
        }

        .navItem > a {
            /*width: 95px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu5, .menu10, .menuAll, .menu4, .menu7, .menu8,.menu3  {
            display: none;
        }

        .menu9 {
            display: inline;
        }
    }

    /*大屏幕*/
    /*@media only screen and (min-width: 1506) {*/
    @media only screen and (min-width: 1504px) and (max-width: 1570px) {
        .navItem {
            /*width: 120px;*/
        }

        .navItem > a {
            /*width: 120px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menu10 {
            display: inline;
        }

        .menu5, .menu4, .menuAll, .menu7,.menu9, .menu8,.menu3  {
            display: none;
        }
    }

    @media only screen and (min-width: 1571px) {
        .navItem {
            /*width: 120px;*/
        }

        .navItem > a {
            /*width: 120px;*/
            white-space: nowrap;
            /*overflow: hidden;*/
            text-overflow: ellipsis;
        }

        .menuAll {
            display: inline;
        }

        .menu10,.menu5, .menu4, .menu7, .menu9, .menu8,.menu3  {
            display: none;
        }
    }


    .dropdown-submenu {
        position: relative;
    }

    .dropdown-submenu > .dropdown-menu {
        top: 0;
        left: -305px;
        margin-top: -6px;
        margin-left: -1px;
        border-radius: 6px;
        /*-webkit-border-radius: 0 6px 6px 6px;*/
        /*-moz-border-radius: 0 6px 6px 6px;*/
        /*border-radius: 0 6px 6px 6px;*/
    }

    .dropdown-submenu:hover > .dropdown-menu {
        display: block;
    }

    .dropdown-submenu > a:after {
        display: block;
        content: " ";
        float: left;
        width: 0;
        height: 0;
        border-color: transparent;
        border-style: solid;
        border-width: 5px 5px 5px 0px;
        border-right-color: #cccccc;
        margin-top: 5px;
        margin-left: -10px;
    }

    .dropdown-submenu:hover > a:after {
        border-right-color: #ffffff;
    }

    .dropdown-submenu.pull-left {
        float: none;
    }

    .dropdown-submenu.pull-left > .dropdown-menu {
        left: -100%;
        margin-left: 10px;
        -webkit-border-radius: 6px 0 6px 6px;
        -moz-border-radius: 6px 0 6px 6px;
        border-radius: 6px 0 6px 6px;
    }

    .childLi {
    }

    .childText {
        width: 305px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
</STYLE>
[#if Session.IY_MASTER_MENUS??]

    <div class="menu10">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
            [#--页面显示10条, 其余的显示在More 按钮内--]
                [#if item_index lt 10]
                    <li class="navItem" title="${item.name!}">
                        <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                            ${item.name!}
                            [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                        [#if item.menuItems?exists]
                            <ul class="dropdown-menu" role="menu">
                                [#list item.menuItems as child ]
                                    [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                        <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </li>
                [/#if]
            [/#list]

            [#--遍历数据到 'More' 里面去--]
            [#if Session.IY_MASTER_MENUS?size gt 10]
                <li class="presentation">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                       aria-expanded="false" title="More">More<span class="glyphicon glyphicon-option-vertical"
                                                                    aria-hidden="true"></span></a>
                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                        [#list Session.IY_MASTER_MENUS as item ]
                            [#if item_index gte 10]
                                <li class="dropdown-submenu" title="${item.name!}">
                                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                                        ${item.name!}
                                        [#if !(item.url?exists)][/#if]</a>
                                    [#if item.menuItems?exists]
                                        <ul class="dropdown-menu">
                                            [#list item.menuItems as child ]
                                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                                    <li class="childLi" title="${child.name}"><a class="childText" href="${syspath}/${child.url!}">${child.name}</a></li>
                                                [/#if]
                                            [/#list]
                                        </ul>
                                    [/#if]
                                </li>
                            [/#if]
                        [/#list]
                    </ul>
                </li>
            [/#if]
        </ul>
    </div>


    <div class="menu4">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
            [#--页面显示4条, 其余的显示在More 按钮内--]
                [#if item_index lt 4]
                    <li class="navItem" title="${item.name!}">
                        <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                            ${item.name!}
                            [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                        [#if item.menuItems?exists]
                            <ul class="dropdown-menu" role="menu">
                                [#list item.menuItems as child ]
                                    [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                        <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </li>
                [/#if]
            [/#list]

            [#--遍历数据到 'More' 里面去--]
            [#if Session.IY_MASTER_MENUS?size gt 4]
                <li class="presentation">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                       aria-expanded="false" title="More">More<span class="glyphicon glyphicon-option-vertical"
                                                                    aria-hidden="true"></span></a>
                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                        [#list Session.IY_MASTER_MENUS as item ]
                            [#if item_index gte 4]
                                <li class="dropdown-submenu" title="${item.name!}">
                                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                                        ${item.name!}
                                        [#if !(item.url?exists)][/#if]</a>
                                    [#if item.menuItems?exists]
                                        <ul class="dropdown-menu">
                                            [#list item.menuItems as child ]
                                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                                    <li class="childLi" title="${child.name}"><a class="childText" href="${syspath}/${child.url!}">${child.name}</a></li>
                                                [/#if]
                                            [/#list]
                                        </ul>
                                    [/#if]
                                </li>
                            [/#if]
                        [/#list]
                    </ul>
                </li>
            [/#if]
        </ul>
    </div>

    <div class="menu8">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
            [#--页面显示8条, 其余的显示在More 按钮内--]
                [#if item_index lt 8]
                    <li class="navItem" title="${item.name!}">
                        <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                            ${item.name!}
                            [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                        [#if item.menuItems?exists]
                            <ul class="dropdown-menu" role="menu">
                                [#list item.menuItems as child ]
                                    [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                        <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </li>
                [/#if]
            [/#list]

            [#--遍历数据到 'More' 里面去--]
            [#if Session.IY_MASTER_MENUS?size gt 8]
                <li class="presentation">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                       aria-expanded="false" title="More">More<span class="glyphicon glyphicon-option-vertical"
                                                                    aria-hidden="true"></span></a>
                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                        [#list Session.IY_MASTER_MENUS as item ]
                            [#if item_index gte 8]
                                <li class="dropdown-submenu" title="${item.name!}">
                                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                                        ${item.name!}
                                        [#if !(item.url?exists)][/#if]</a>
                                    [#if item.menuItems?exists]
                                        <ul class="dropdown-menu">
                                            [#list item.menuItems as child ]
                                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                                    <li class="childLi" title="${child.name}"><a class="childText" href="${syspath}/${child.url!}">${child.name}</a></li>
                                                [/#if]
                                            [/#list]
                                        </ul>
                                    [/#if]
                                </li>
                            [/#if]
                        [/#list]
                    </ul>
                </li>
            [/#if]
        </ul>
    </div>

    <div class="menu5">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
            [#--页面显示5条, 其余的显示在More 按钮内--]
                [#if item_index lt 5]
                    <li class="navItem" title="${item.name!}">
                        <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                            ${item.name!}
                            [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                        [#if item.menuItems?exists]
                            <ul class="dropdown-menu" role="menu">
                                [#list item.menuItems as child ]
                                    [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                        <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </li>
                [/#if]
            [/#list]

            [#--遍历数据到 'More' 里面去--]
            [#if Session.IY_MASTER_MENUS?size gt 5]
                <li class="presentation">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                       aria-expanded="false" title="More">More<span class="glyphicon glyphicon-option-vertical"
                                                                    aria-hidden="true"></span></a>
                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                        [#list Session.IY_MASTER_MENUS as item ]
                            [#if item_index gte 5]
                                <li class="dropdown-submenu" title="${item.name!}">
                                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                                        ${item.name!}
                                        [#if !(item.url?exists)][/#if]</a>
                                    [#if item.menuItems?exists]
                                        <ul class="dropdown-menu">
                                            [#list item.menuItems as child ]
                                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                                    <li class="childLi" title="${child.name}"><a class="childText" href="${syspath}/${child.url!}">${child.name}</a></li>
                                                [/#if]
                                            [/#list]
                                        </ul>
                                    [/#if]
                                </li>
                            [/#if]
                        [/#list]
                    </ul>
                </li>
            [/#if]
        </ul>
    </div>

    <div class="menu7">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
            [#--页面显示7条, 其余的显示在More 按钮内--]
                [#if item_index lt 7]
                    <li class="navItem" title="${item.name!}">
                        <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                            ${item.name!}
                            [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                        [#if item.menuItems?exists]
                            <ul class="dropdown-menu" role="menu">
                                [#list item.menuItems as child ]
                                    [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                        <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </li>
                [/#if]
            [/#list]

            [#--遍历数据到 'More' 里面去--]
            [#if Session.IY_MASTER_MENUS?size gt 7]
                <li class="presentation">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                       aria-expanded="false" title="More">More<span class="glyphicon glyphicon-option-vertical"
                                                                    aria-hidden="true"></span></a>
                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                        [#list Session.IY_MASTER_MENUS as item ]
                            [#if item_index gte 7]
                                <li class="dropdown-submenu" title="${item.name!}">
                                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                                        ${item.name!}
                                        [#if !(item.url?exists)][/#if]</a>
                                    [#if item.menuItems?exists]
                                        <ul class="dropdown-menu">
                                            [#list item.menuItems as child ]
                                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                                    <li class="childLi" title="${child.name}"><a class="childText" href="${syspath}/${child.url!}">${child.name}</a></li>
                                                [/#if]
                                            [/#list]
                                        </ul>
                                    [/#if]
                                </li>
                            [/#if]
                        [/#list]
                    </ul>
                </li>
            [/#if]
        </ul>
    </div>

    <div class="menu9">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
            [#--页面显示9条, 其余的显示在More 按钮内--]
                [#if item_index lt 9]
                    <li class="navItem" title="${item.name!}">
                        <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                            ${item.name!}
                            [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                        [#if item.menuItems?exists]
                            <ul class="dropdown-menu" role="menu">
                                [#list item.menuItems as child ]
                                    [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                        <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </li>
                [/#if]
            [/#list]

            [#--遍历数据到 'More' 里面去--]
            [#if Session.IY_MASTER_MENUS?size gt 9]
                <li class="presentation">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                       aria-expanded="false" title="More">More<span class="glyphicon glyphicon-option-vertical"
                                                                    aria-hidden="true"></span></a>
                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                        [#list Session.IY_MASTER_MENUS as item ]
                            [#if item_index gte 9]
                                <li class="dropdown-submenu" title="${item.name!}">
                                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                                        ${item.name!}
                                        [#if !(item.url?exists)][/#if]</a>
                                    [#if item.menuItems?exists]
                                        <ul class="dropdown-menu">
                                            [#list item.menuItems as child ]
                                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                                    <li class="childLi" title="${child.name}"><a class="childText" href="${syspath}/${child.url!}">${child.name}</a></li>
                                                [/#if]
                                            [/#list]
                                        </ul>
                                    [/#if]
                                </li>
                            [/#if]
                        [/#list]
                    </ul>
                </li>
            [/#if]
        </ul>
    </div>

    <div class="menu3">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
            [#--页面显示3条, 其余的显示在More 按钮内--]
                [#if item_index lt 3]
                    <li class="navItem" title="${item.name!}">
                        <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                            ${item.name!}
                            [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                        [#if item.menuItems?exists]
                            <ul class="dropdown-menu" role="menu">
                                [#list item.menuItems as child ]
                                    [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                        <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </li>
                [/#if]
            [/#list]

            [#--遍历数据到 'More' 里面去--]
            [#if Session.IY_MASTER_MENUS?size gt 3]
                <li class="presentation">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                       aria-expanded="false" title="More">More<span class="glyphicon glyphicon-option-vertical"
                                                                    aria-hidden="true"></span></a>
                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                        [#list Session.IY_MASTER_MENUS as item ]
                            [#if item_index gte 3]
                                <li class="dropdown-submenu" title="${item.name!}">
                                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                                        ${item.name!}
                                        [#if !(item.url?exists)][/#if]</a>
                                    [#if item.menuItems?exists]
                                        <ul class="dropdown-menu">
                                            [#list item.menuItems as child ]
                                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                                    <li class="childLi" title="${child.name}"><a class="childText" href="${syspath}/${child.url!}">${child.name}</a></li>
                                                [/#if]
                                            [/#list]
                                        </ul>
                                    [/#if]
                                </li>
                            [/#if]
                        [/#list]
                    </ul>
                </li>
            [/#if]
        </ul>
    </div>

[#--小屏幕 全部--]
    <div class="menuAll">
        <ul class="nav navbar-nav menu-ul">
            [#list Session.IY_MASTER_MENUS as item ]
                <li class="navItem" title="${item.name!}">
                    <a class="dropdown-toggle" [#if item.url?exists] href="${syspath}/${item.url!}" [#else]data-hover="dropdown" data-toggle="dropdown" href="javascript:void(0);" [/#if] >
                        ${item.name!}
                        [#if !(item.url?exists)]<span class="caret"></span>[/#if]</a>
                    [#if item.menuItems?exists]
                        <ul class="dropdown-menu" role="menu">
                            [#list item.menuItems as child ]
                                [#if child.url?default("") != "index" && child.url?default("") != "informReply"]
                                    <li title="${child.name}"><a href="${syspath}/${child.url!}">${child.name}</a></li>
                                [/#if]
                            [/#list]
                        </ul>
                    [/#if]
                </li>
            [/#list]
        </ul>
    </div>

[/#if]