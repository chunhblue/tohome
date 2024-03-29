// webpack.config.js
var path = require('path');
var webpack = require('webpack');
var JsonToPropPlugin = require('json-to-prop-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const TerserPlugin = require('terser-webpack-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
var outputPath = path.join(__dirname, '../../../target/classes/static');
var srcPath = path.join(__dirname, './src');
//console.log(process.env.arg);
const devMode = process.env.NODE_ENV !== 'pro';

module.exports = {
    context: srcPath,
    entry: {
        respond: '../node_modules/respond.js/dest/respond.min.js',
        // es5sham: '../node_modules/es5-shim/es5-sham.min.js',
        html5shiv: '../node_modules/html5shiv/dist/html5shiv.min.js',
        userinfo: './js/userinfo.js',
        index: './js/index.js',
        msg: './js/msg-1.0.js',
        defaultRole: './js/defaultRole-1.0.js',
        login: './js/login-1.0.js',
        rolelist: './js/base/role/list.js',
        roleedit: './js/base/role/edit.js',
        roleview: './js/base/role/view.js',
        bm: './js/bm/bm-1.0.js',
        bmEdit: './js/bm/bmEdit-1.0.js',
        bmView: './js/bm/bmView-1.0.js',
        bmUpdate: './js/bm/bmUpdate-1.0.js',
        bmhis: './js/bm_his/bmHis-1.0.js',
        bmHisView: './js/bm_his/bmHisView-1.0.js',
        actorassignment: './js/base/actorassignment-1.0.js',
        onlineExport: './js/onlineExport.js',
        order: './js/order/order-1.3.js',
        orderEdit: './js/order/orderEdit-1.0.js',
        receipt: './js/receipt/receipt-1.0.js',
        receiptEdit: './js/receipt/receiptEdit-1.0.js',
        receiptPrint: './js/receipt/receiptPrint-1.0.js',
        vendorReceipt: './js/vendor_receipt/receipt-1.0.js',
        vendorReceiptEdit: './js/vendor_receipt/receiptEdit-1.0.js',
        vendorReceiptPrint: './js/vendor_receipt/receiptPrint-1.0.js',
        difference: './js/difference/difference-1.0.js',
        differenceEdit: './js/difference/differenceEdit-1.0.js',
        defferencePrint: './js/difference/differencePrint-1.0.js',
        returnWarehouse: './js/return_warehouse/return-1.0.js',
        returnWarehouseEdit: './js/return_warehouse/returnEdit-1.0.js',
        returnWarehousePrint: './js/return_warehouse/returnPrint-1.0.js',
        returnWarehouseOrgPrint: './js/return_warehouse/returnOrgPrint-1.0.js',
        returnVendor: './js/return_vendor/return-1.0.js',
        returnVendorEdit: './js/return_vendor/returnEdit-1.0.js',
        vendorReturnPrint: './js/return_vendor/returnPrint-1.0.js',
        vendorReturnOrgPrint: './js/return_vendor/returnOrgPrint-1.0.js',
        returnDocumentPrint:'./js/return_vendor/returnDocumentPrint-1.0.js',
        returnDcDocumentPrint:'./js/return_warehouse/returnDcDocumentPrint-1.0.js',
        orderNewStore: './js/order_newStore/orderNewStore-1.0.js',
        orderNewStoreEdit: './js/order_newStore/orderNewStoreEdit-1.0.js',
        stockAdjustment: './js/stock_adjustment/stockAdjustment-1.0.js',
        stockAdjustmentEdit: './js/stock_adjustment/stockAdjustmentEdit-1.0.js',
        orderDetails: './js/order_details/orderDetails-1.0.js',
        orderDetailsEdit: './js/order_details/orderDetailsEdit-1.0.js',
        orderDcEdit: './js/order_dc_urgent/orderDcEdit-1.0.js',
        storeTransfers:'./js/store_transfers_out/storeTransfers-1.0.js',
        storeTransfersEdit:'./js/store_transfers_out/storeTransfersEdit-1.0.js',
        storeTransfersIn:'./js/store_transfers_in/storeTransfersIn-1.0.js',
        storeTransfersInEdit:'./js/store_transfers_in/storeTransfersInEdit-1.0.js',
        transfersModEdit:'./js/transfers_modify/transfersModEdit-1.0.js',
        stocktakePlan:'./js/stocktake_plan/stocktakePlan-1.0.js',
        stocktakePlanEdit:'./js/stocktake_plan/stocktakePlanEdit-1.0.js',
        stocktakePlanPrint:'./js/stocktake_plan/stocktakePlanPrint-1.0.js',
        stocktakePlanEntry:'./js/stocktake_entry/stocktakePlanEntry-1.0.js',
        stocktakeDataEntry:'./js/stocktake_entry/stocktakeDataEntry-1.0.js',
        stocktakePlanEntryPrint:'./js/stocktake_entry/stocktakePlanEntryPrint-1.0.js',
        stocktakeProcess:'./js/stocktake_process/stocktakeProcess-1.0.js',
        stocktakeProcessEdit:'./js/stocktake_process/stocktakeProcessEdit-1.0.js',
        stocktakeProcessPrint:'./js/stocktake_process/stocktakeProcessPrint-1.0.js',
        maItemSale:'./js/material_item_sales/maItemSale-1.0.js',
        maItemSaleEdit:'./js/material_item_sales/maItemSaleEdit-1.0.js',
        unpackSale:'./js/unpack_sales/unpackSale-1.0.js',
        unpackSaleEdit:'./js/unpack_sales/unpackSaleEdit-1.0.js',
        fundEntry:'./js/fund_entry/fundEntry-1.0.js',
        fundEntryEdit:'./js/fund_entry/fundEntryEdit-1.0.js',
        bmPromotion:'./js/bm_promotion/bmPromotion-1.0.js',
        promotionDetails:'./js/bm_promotion/details-1.0.js',
        priceByDay: './js/master/price/price-1.0.js',
        vouchersList: './js/inventory_vouchers/vouchersList-1.0.js',
        storeList: './js/master/store/storeList-1.0.js',
        storeDetails: './js/master/store/storeDetails-1.0.js',
        itemList: './js/master/item/itemList-1.0.js',
        itemDetails: './js/master/item/itemDetails-1.0.js',
        vendorList: './js/master/vendor/vendorList-1.0.js',
        vendorDetails: './js/master/vendor/vendorDetails-1.0.js',
        orderFailed:'./js/order_failed/orderFailed-1.0.js',
        dayOrderFailure:'./js/day_order_failure/dayOrderFailure-1.0.js',
        dayOrderFailureView:'./js/day_order_failure/dayOrderFailureView-1.0.js',
        bomSale:'./js/bomSale/bomSale-1.0.js',
        bomSalePrint:'./js/bomSale/bomSalePrint-1.0.js',
        groupSale:'./js/bomSaleStat/bomSaleStat-1.0.js',
        groupSalePrint:'./js/bomSaleStat/bomSaleStatPrint-1.0.js',
        nonoperatingList:'./js/operation/nonoperating/nonoperatingList-1.0.js',
        nonoperatingEntry:'./js/operation/nonoperating/nonoperatingEntry-1.0.js',
        stockScrap:'./js/stock_scrap/stockScrap-1.0.js',
        stockScrapEdit:'./js/stock_scrap/stockScrapEdit-1.0.js',
        dcReceiptDaily:'./js/dc_receipt_daily/dcReceiptDaily-1.0.js',
        dcReceiptDailyPrint:'./js/dc_receipt_daily/dcReceiptDailyPrint-1.0.js',
        cashier:'./js/cashier/cashier-1.0.js',
        vendorReceiptDaily:'./js/vendor_receipt_daily/vendorReceiptDaily-1.0.js',
        vendorReceiptDailyPrint:'./js/vendor_receipt_daily/vendorReceiptDailyPrint-1.0.js',
        returnsDaily:'./js/returns_daily/returnsDaily-1.0.js',
        returnsDailyPrint:'./js/returns_daily/returnsDailyPrint-1.0.js',
        electronicReceipt:'./js/electronicReceipt/electronicReceipt-1.0.js',
        operationExpress:'./js/information/express/express-1.0.js',
        cashierDetail:'./js/cashierDetail/cashierDetail-1.0.js',
        saleReconciliation:'./js/saleReconciliation/saleReconciliation-1.0.js',
        rrModifyQuery:'./js/rr_modify_query/rrModifyQuery-1.0.js',
        rrModifyEdit:'./js/rr_modify_query/rrModifyEdit-1.0.js',
        rrVoucherQuery:'./js/rr_voucher_query/rrVoucherQuery-1.0.js',
        rrVoucherQueryPrint:'./js/rr_voucher_query/rrVoucherQueryPrint-1.0.js',
        stocktakeQuery:'./js/stocktake_query/stocktakeQuery-1.0.js',
        stocktakeQueryEdit:'./js/stocktake_query/stocktakeQueryEdit-1.0.js',
        rtInventoryQuery:'./js/rtInventory_query/rtInventoryQuery-1.0.js',
        auditPending:'./js/auditMessage/auditPending-1.0.js',
        auditRejected:'./js/auditMessage/auditRejected-1.0.js',
        mySubmissions:'./js/auditMessage/mySubmissions-1.0.js',
        subbmitterQuery:'./js/auditMessage/subbmitterQuery-1.0.js',
        myMessage:'./js/auditMessage/myMessage-1.0.js',
        auth:'./js/base/auth/auth-1.0.js',
        authEdit:'./js/base/auth/authedit-1.0.js',
        passSet:'./js/base/auth/passSet-1.0.js',
        fourWeekOperation:'./js/information/4Week/4Week-1.0.js',
        businessDaily:'./js/businessDaily/businessDaily-1.0.js',
        menu:'./js/base/menu-1.1.js',
        storeItemRelationship:'./js/storeItemRelationship/storeItemRelationship-1.0.js',
        stocktakePLQ:'./js/stockprofitloss_query/stockPLQuery-1.0.js',
        cashierAmount:'./js/cashierAmount/cashierAmount-1.0.js',
        bankPayIn:'./js/bankPayIn/bankPayIn-1.0.js',
        receiptReturnWarehouse: './js/receipt_return_warehouse/return-1.0.js',
        receiptReturnWarehouseEdit: './js/receipt_return_warehouse/returnEdit-1.0.js',
        receiptReturnWarehousePrint: './js/receipt_return_warehouse/returnPrint-1.0.js',
        receiptReturnVendor: './js/receipt_return_vendor/return-1.0.js',
        receiptReturnVendorEdit: './js/receipt_return_vendor/returnEdit-1.0.js',
        receiptReturnVendorPrint: './js/receipt_return_vendor/returnPrint-1.0.js',
        priceChange: './js/priceChange/priceChange-1.0.js',
        priceChangeDetail: './js/priceChange/priceChangeDetail-1.0.js',
        storeReason: './js/storeReason/storeReason-1.0.js',
        paymentMode: './js/paymentMode/paymentMode-1.0.js',
        paymentModeEdit: './js/paymentMode/paymentModeEdit-1.0.js',
        informList: './js/inform/informList-1.0.js',
        informEdit: './js/inform/informEdit-1.0.js',
        informReplyList: './js/inform_reply/informReplyList-1.0.js',
        informReplyEdit: './js/inform_reply/informReplyEdit-1.0.js',
        informLogList: './js/inform_log/informLogList-1.0.js',
        newProductList: './js/inform_newProduct/newProductList-1.0.js',
        promotionList: './js/inform_promotion/promotionList-1.0.js',
        storePosition:'./js/store_positionLd/storeposition-1.0.js',
        organizationalStructure:'./js/store_positionLd/organizationalStructure-1.0.js',
        storePositionMaintain:'./js/store_positionLd/storePositionMaintain-1.0.js',
        storeTransferDaily:'./js/store_transfer_daily/storeTransferDaily-1.0.js',
        storeTransferDailyPrint:'./js/store_transfer_daily/storeTransferDailyPrint-1.0.js',
        orderDirectSupplier:'./js/ordersupplierLd/ordersupplier-1.0.js',
        orderDirectSupplierPrint:'./js/ordersupplierLd/ordersupplierprint-1.0.js',
        ordersupperview:'./js/ordersupplierLd/ordersupperview-1.0.js',
        orderCd:'./js/ordercdLd/ordercd-1.0.js',
        orderview:'./js/ordercdLd/orderview-1.0.js',
        orderCdPrint:'./js/ordercdLd/ordercdprint-1.0.js',
        custDataEnty:'./js/opreationmanaentyLd/custDataEntry-1.0.js',
        custPlanEntry:'./js/opreationmanaentyLd/custPlanEntry-1.0.js',
        custPlanPrint:'./js/opreationmanaentyLd/custPlanPrint-1.0.js',
        custPlan:'./js/opreationmanplanLd/custPlan-1.0.js',
        custPlanEdit:'./js/opreationmanplanLd/custPlanEdit-1.0.js',
        fsInventoryDataEntry:'./js/fsInventoryentryLd/fsInventoryDataEntry-1.0.js',
        fsInventoryPlanEntry:'./js/fsInventoryentryLd/fsInventoryPlanEntry-1.0.js',
        fsInventoryPlanEdit:'./js/fsinventoryplanLd/fsInventoryPlanEdit-1.0.js',
        fsInventoryPlan:'./js/fsinventoryplanLd/fsInventoryPlan-1.0.js',
        fsInventoryplanPrint:'./js/fsinventoryplanLd/fsInventoryplanPrint-1.0.js',
        materialDataEntry:'./js/materialentryLd/materialDataEntry-1.0.js',
        materialPlanEntry:'./js/materialentryLd/materialPlanEntry-1.0.js',
        materialPlan:'./js/materialplanLd/materialPlan-1.0.js',
        materialPlanEdit:'./js/materialplanLd/materialPlanEdit-1.0.js',
        writeOff:'./js/information/writeOff/writeOff-1.0.js',
        writeOffPrint:'./js/information/writeOff/writeOffPrint-1.0.js',
        adjustmentDaily:'./js/information/stockAdjustment/stockAdjustment-1.0.js',
        adjustmentDailyPrint:'./js/information/stockAdjustment/stockAdjustmentPrint-1.0.js',
        storeInventoryDaily:'./js/store_inventory_daily/storeInventoryDaily-1.0.js',
        storeInventoryDailyPrint:'./js/store_inventory_daily/storeInventoryDailyPrint-1.0.js',
        dailySaleReport:'./js/dailysalereportLd/dailySaleReport-1.0.js',
        classifiedSalerePort:'./js/classifiedsalereport/classifiedsalereport-1.0.js',
        goodsSaleReport:'./js/goodSaleReport/goodSaleDayReport-1.0.js',
        importantgoodsSaleReport:'./js/importantgoodsalereport/importantGoodSaleReport-1.0.js',
        suspendSaleEntry: './js/suspendSale/suspendSaleEntry-1.0.js',
        suspendSaleDetail: './js/suspendSale/suspendSaleDetail-1.0.js',
        invoiceEntryList: './js/invoiceEntry/invoiceEntryList-1.0.js',
        invoiceEntryEdit: './js/invoiceEntry/invoiceEntryEdit-1.0.js',
        mmPromotionSaleDaily: './js/mmPromotionSaleDaily/mmPromotionSaleDaily-1.0.js',
        operatorLog: './js/operatorLog/operatorLog-1.0.js',
        priceLabel: './js/priceLabel/priceLabel-1.0.js',
        priceLabelPrint: './js/priceLabel/priceLabelPrint-1.0.js',
        orderReport:'./js/orderReport/orderReport-1.0.js',
        coreItemByhi:'./js/importantgoodsalereport/coreItemByhi-1.0.js',
        serviceTypeDaily:'./js/serviceTypeDailyReport/serviceTypeDaily-1.0.js',
        reconciliationMng:'./js/reconciliationMng/reconciliationMng-1.0.js',
        itemTransfer:'./js/itemTransfer/itemTransfer-1.0.js',
        itemTransfersEdit:'./js/itemTransfer/itemTransferEdit-1.0.js',
        itemTransferDaily:'./js/itemTransferDaily/itemTransferDaily-1.0.js',
        itemTransferDailyPrint:'./js/itemTransferDaily/itemTransferDailyPrint-1.0.js',
        pogShelfMaintenance:'./js/pog_shelf_maintenance_management/shelfMaintenance-1.0.js',
        pogShelfListView:'./js/pog_shelf_maintenance_management/pogShelfListView-1.0.js',
        vendorReturnedDaily:'./js/vendor_returned_daily/vendorReturnedDaily-1.0.js',
        vendorReturnedDailyPrint:'./js/vendor_returned_daily/vendorReturnedDailyPrint-1.0.js',
        ReturnedDailyWarehouse:'./js/dc_returned_daily/dcReturnedDaily-1.0.js',
        ReturnedDailyWarehousePrint:'./js/dc_returned_daily/dcReturnedDailyPrint-1.0.js',
        viettelPhoneReport:'./js/viettel_phone_report/viettelPhoneReport-1.0.js',
        cardPaymentReport:'./js/card_payment_report/cardPaymentReport-1.0.js',
        customerCountReport:'./js/customer_count_report/customerCountReport-1.0.js',
        stampSummaryReport:'./js/stampSummaryReport/stampSummaryReport-1.0.js',
        stampDetailReport:'./js/stampSummaryReport/stampDetailReport-1.0.js',
        staffAttendanceReport:'./js/storeStaffAttendanceDaily/staffAttendanceDaily-1.0.js',
        shoppingOrderDifference:'./js/difference_shopping/shippingOrderDiscrepancy-1.0.js',
        shoppingOrderDifferEdit:'./js/difference_shopping/shippingOrderDiscrepancyEdit-1.0.js',
        stockTakeNonList:'./js/stockTake_non_list/stockTakeNonList-1.0.js',
        // NoServiceReport:'./js/customer_noservice/NoServiceReport-1.0.js',
        yearPromotionReport:'./js/yearPromotion/yearPromotionReport-1.0.js',
        hhreportSummy:'./js/hhreportSummy/hhreportSummy-1.0.js',
        orderHHt:'./js/hhtorder/hhtorder-1.0.js',
        remoteUpdate:'./js/remoteUpdate/remoteUpdate-1.0.js',
        remoteUpdateEdit:'./js/remoteUpdate/remoteUpdateEdit-1.0.js',
        clearInventory:'./js/clear_stock/clearInventory-1.0.js',

    },
    output: {
        filename: devMode ? "[name].js" : "[name].[hash:5].js",
        path: outputPath
    },
    resolveLoader: {modules: ["node_modules"]},
    module: {
        rules: [
            {
                test: /\.css$/,
                use: [MiniCssExtractPlugin.loader, 'css-loader'],
                include: [
                    path.resolve(srcPath, "css"),
                    path.resolve(srcPath, "../node_modules/bootstrap/dist/css"),
                    path.resolve(srcPath, "../node_modules/zggrid/css"),
                    path.resolve(srcPath, "../node_modules/myvalidator/css"),
                    path.resolve(srcPath, "../node_modules/select2/dist/css")
                ]
            },
            {
                // 这样在小于8K的图片将直接以base64的形式内联在代码中，可以减少一次http请求
                test: /\.(png|jpg|gif|cur)$/,
                use: 'url-loader?limit=8192'
            },
            {test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, use: "file-loader"},
            {test: /\.(woff|woff2)$/, use: "url-loader?prefix=font/&limit=5000"},
            {
                test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
                use: "url-loader?limit=10000&mimetype=application/octet-stream"
            },
            {test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, use: "file-loader"}
        ]
    },
    resolve: {
        modules: [
            "node_modules",
            path.resolve(srcPath, "js"),
            path.resolve(srcPath, "lib"),
            path.resolve(srcPath, "images"),
            path.resolve(srcPath, "css")
        ],
        extensions: [".js", ".jsx", ".json", ".css", ".svg"],
        alias: {
            'jquery$': path.join(__dirname, "node_modules/jquery/dist/jquery.min.js"),
            'common': path.join(__dirname, '../../../target/node/commons-1.0.js'),
            'jquery-ui': path.join(srcPath, './lib/jquery-ui.min.js'),
            'jquery-migrate': path.join(__dirname, "node_modules/jquery-migrate/dist/jquery-migrate.min.js"),
            'bootstrap': path.join(__dirname, 'node_modules/bootstrap/dist/js/bootstrap.min.js'),
            'datetimepicker': path.join(srcPath, '/lib/bootstrap/bootstrap-datetimepicker-3.3.0.js'),
            "bootstrapValidator": path.join(srcPath, '/lib/bootstrap/bootstrapValidator.min.js'),
            'zgGrid': path.join(__dirname, 'node_modules/zggrid/js/zgGrid.js'),
            'messenger': path.join(srcPath, '/lib/messenger-1.5.0.js'),
            'ajaxIntercept': path.join(srcPath, './lib/ajaxIntercept-1.0.js'),
            'dynamic': path.join(srcPath, '/lib/dynamic-model-2.0'),
            'myAutomatic': path.join(srcPath, '/lib/myAutomatic-1.0.js'),
            'myAjax': path.join(srcPath, '/lib/myAjax-1.0.js'),
            'start': path.join(srcPath, './lib/start-1.0.js'),
            'roleBasis': path.join(srcPath, './lib/role-basis.js'),
            // 'selectOrganization': path.join(srcPath, './js/base/selectOrganization.js'),
            'review': path.join(srcPath, './lib/review.js'),
            'myValidator': path.join(__dirname, 'node_modules/myvalidator/js/myValidator.js'),
            // 'bootstrap-table': path.join(srcPath, './lib/bootstrap/bootstrap-table.min.js'),
            // 'bootstrap-table-zh-CN': path.join(srcPath, './lib/bootstrap/bootstrap-table-zh-CN.js'),
            'jqprint': path.join(srcPath, './lib/jquery.jqprint-1.6.js'),
            'checkboxGrid': path.join(srcPath, './js/base/zgGrid.js'),
            'checkboxInPage': path.join(srcPath, './lib/checkboxInPage.js'),
            //图表
            'echarts': path.join(srcPath, './lib/echarts.min.js'),
            'validator': path.join(__dirname, "node_modules/bootstrap-validator/dist/validator.min.js"),
            'treeview': path.join(srcPath, '/lib/bootstrap/bootstrap-treeview.min.js'),
            'math': path.join(srcPath, './lib/math.min.js'),
            'select2': path.join(srcPath, './lib/select2.js'),
            'JsBarcode': path.join(srcPath, './lib/JsBarcode.all.js'),
            'qrcode': path.join(srcPath, './lib/jquery.qrcode.min.js'),
            //css
            "bootstrap.css": path.join(srcPath, '../node_modules/bootstrap/dist/css/bootstrap.css'),
            "bootstrapValidator.css": path.join(srcPath, '/css/bootstrapValidator.min.css'),
            // 'bootstrap-table.css': path.join(srcPath, '/css/bootstrap-table.min.css'),
            "messenger.css": path.join(srcPath, '/css/messenger.css'),
            "messenger-theme-future.css": path.join(srcPath, '/css/messenger-theme-future.css'),
            "basis.css": path.join(srcPath, '/css/basis-1.0.css'),
            "zgGrid.css": path.join(srcPath, '../node_modules/zggrid/css/zgGrid.css'),
            "bootstrap-datetimepicker.css": path.join(srcPath, '/css/bootstrap-datetimepicker.min.css'),
            "bootstrap-ie8.css": path.join(srcPath, '/css/bootstrap-ie8.css'),
            "myAutoComplete.css": path.join(srcPath, '/css/myAutoComplete.css'),
            "normalize.css": path.join(srcPath, '/css/normalize.css'),
            "scojs.css": path.join(srcPath, '/css/scojs.css'),
            "roleBasis.css": path.join(srcPath, '/css/role-basis.css'),
            "myValidator.css": path.join(srcPath, '../node_modules/myvalidator/css/myValidator.css'),
            "index.css": path.join(srcPath, '/css/index.css'),
            "select2.css": path.join(srcPath, '/css/select2.css'),
            "roleBasis.css": path.join(srcPath, '/css/role-basis.css')
        }
    },
    performance: {
        hints: devMode ? "warning" : false,
        maxEntrypointSize: 600000,
        maxAssetSize: 600000
    },
    optimization: {
        // minimize: true,
        minimizer: [
            new TerserPlugin({
                test: /\.js(\?.*)?$/i,
                sourceMap: true
            }),
            new OptimizeCSSAssetsPlugin({
                test: /\.css(\?.*)?$/i
            })
        ],
        splitChunks: {
            chunks: 'async',
            // name:true,
            // minSize: 30000,
            // maxAsyncRequests: 5,
            // maxInitialRequests: 3,
            cacheGroups: {
                common: {
                    name: 'common-all',
                    minChunks: 2,
                    chunks: 'all',
                    reuseExistingChunk: true,
                    //enforce: true, //忽略minSize，minChunks，maxAsyncRequests，maxInitialRequests
                    test: /\.(js|css)$/
                }
            }
        }
    },
    plugins: [
        new webpack.ProvidePlugin({	//加载jq
            "jquery-migrate": "jquery-migrate",
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery",
            "_common": "common",
            "_start": "start"
        }),
        new MiniCssExtractPlugin({
            filename: devMode ? "[name].css" : "[name].[hash:5].css",
        }),
        new JsonToPropPlugin({paths: [path.join(__dirname, '../../../target/classes/stats.properties')]}),
        new CopyWebpackPlugin([
            {
                from: __dirname + '/src/externals/',
                to: __dirname + '/../../../target/classes/static/',
                flatten: true
            }
        ])
    ]
};