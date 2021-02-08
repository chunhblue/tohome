[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title'/]</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0"
          user-scalable=no/>
    <meta name="renderer" content="webkit">

    <link href="${syspath}/static/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>
    <script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>
    <![endif]-->
    <STYLE type="text/css">
        a.downloadExpor:link, a.downloadExpor:visited, a.downloadExpor:active {
            color: #444444;
            font-size: 12px;
            text-decoration:underline;
        }
        a.downloadExpor:hover {
            color: #FF8008;
            cursor: pointer;
            text-decoration: underline ;
            font-weight:600;
        }
        .box{
            text-align:center;
            margin-left:auto;
            margin-right:auto;
            width:280px;
        }
    </STYLE>

<body>
<div>
    <div class="box">
        <div class="table-area" style="width:250px;margin:0 auto;">
            <div class="tt">上传</div>
            <div class="area">
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="table">
                    <tr>
                        <td>
                            <div id="msg" style="display:none;" >

                            </div>
                            <div id="loading" style="width:150px;margin:0 auto;overflow:hidden;">
                                <span style="float:left;">文件上传失败，${message}</span>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
