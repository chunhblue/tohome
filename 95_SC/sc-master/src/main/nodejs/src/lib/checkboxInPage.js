/**
 * 每一个复选框的value值都保存一个唯一id（也可以自定义一个属性保存唯一id），监听checkbox onchange事件，选中时sessionStorage.setItem(id, ‘1’)，取消选中时sessionStorage.setItem(id, ‘0’)，保存选择信息。
 *
 * 刷新时通过sessionStorage 保存的选择信息恢复checkbox 选择状态。
 * @type {{getAllCheckedIdList: (function(): []), clear: clear, initChecked: initChecked}}
 */
var ckbIP = (function () {
    // sessionStorage key前缀
    var lsKeyPrefix = "checkBoxInPageByCW:";

    // 当前页面复选框列表选择器字符串
    var checkBoxSelectorStr;

    // 复选框列表name
    var checkBoxName = "checkBoxInPage";

    // 全选框id 选择器
    var checkAllBoxId = "#checkAll";

    return {
        initChecked: initChecked,
        getAllCheckedIdList: getAllCheckedIdList,
        clear: clear
    };

    // 获取所有选中的行id 的集合
    function getAllCheckedIdList() {
        var idArr = [];
        var sessionStorageLength = sessionStorage.length;
        for (var i = 0; i < sessionStorageLength; i++) {
            var key = sessionStorage.key(i);
            var index = key.indexOf(lsKeyPrefix);
            if (index !== -1 && sessionStorage.getItem(key) === '1') {
                idArr.push(key.substring(index + lsKeyPrefix.length));
            }
        }

        return idArr;

    }
    
    function initChecked(param) {
        initParam(param);

        var listDom = $(checkBoxSelectorStr);
        addEvent(listDom); // 添加监听事件

        var allCheckFlag = true; // 若页面上行已全部选中，则标题置为选中状态

        for (var i = 0; i < listDom.length; i++) {
            var tmpDom = listDom[i];
            var id = $(tmpDom).val();
            if (sessionStorage.getItem(lsKeyPrefix + id) === '1') {
                $(tmpDom).attr('checked', 'checked');
            }else {
                $(tmpDom).attr('checked', false);
                allCheckFlag = false;
            }

        }

        if (allCheckFlag) {
            $(checkAllBoxId).attr('checked', 'checked');
        }

    }

    function initParam(param) {
        if (param !== undefined && param !== null) {
            if (param.checkAllId !== undefined) {
                checkAllBoxId = "#" + param.checkAllId;
            }
            if (param.checkBoxName !== undefined) {
                checkBoxName = param.checkBoxName;
            }
        }
        checkBoxSelectorStr = "input[name='" + checkBoxName + "']";

    }

    // 添加监听事件
    function addEvent(curListDom) {
        curListDom.on('change', onCheckChange);
        $(checkAllBoxId).on('click', onCheckAllClick);

    }

    // 当页面关闭清空sessionStorage
    function clear() {
        var delKeyArr = []; // 要删除的key集合，删除会导致sessionStorage 长度动态变化，要先记录
        var length = sessionStorage.length;
        for (var i = 0; i < length; i++) {
            var key = sessionStorage.key(i);
            var index = key.indexOf(lsKeyPrefix);
            if (index !== -1) {
                delKeyArr.push(key);
            }
        }

        var delKeyLength = delKeyArr.length;
        for (var j = 0; j < delKeyLength; j++) {
            sessionStorage.removeItem(delKeyArr[j]);
        }

    }

    // 全选复选框
    function onCheckAllClick() {
        var checkAllStatus = $(checkAllBoxId).attr('checked');
        var curPageCheckListDom = $(checkBoxSelectorStr); // 当前页面复选框
        if (checkAllStatus === 'checked') {
            curPageCheckListDom.attr('checked', 'checked');
            for (var i = 0; i < curPageCheckListDom.length; i++) {
                var curTmpDom = curPageCheckListDom[i];
                var id = $(curTmpDom).val();
                sessionStorage.setItem(lsKeyPrefix + id, '1'); // 选中
            }

        }else {
            curPageCheckListDom.attr('checked', false);
            for (var i = 0; i < curPageCheckListDom.length; i++) {
                var curTmpDom = curPageCheckListDom[i];
                var id = $(curTmpDom).val();
                sessionStorage.setItem(lsKeyPrefix + id, '0'); // 未选中
            }

        }

    }

    function onCheckChange(event) {
        var id = $(this).val();
        var checkStatus = $(this).attr('checked');

        if (checkStatus === 'checked') {
            sessionStorage.setItem(lsKeyPrefix + id, '1'); // 选中
            var curlistDom = $(checkBoxSelectorStr + ":not(:checked)"); // 当前页面未被选中的复选框
            if (curlistDom.length === 0) {
                $(checkAllBoxId).attr('checked', 'checked');
            }

        }else {
            sessionStorage.setItem(lsKeyPrefix + id, '0'); // 未选中
            $(checkAllBoxId).attr('checked', false); // 标题复选框置为未选中
        }

    }


})();

























