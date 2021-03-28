package cn.com.bbut.iy.itemmaster.controller.audit;

/**
 *  2021/03/21
 */
public class TypeIdTools {
    public static String getDetailType(Integer typeId,String _type){

        String detailType = "";
        switch (typeId){
            case 2 :
                detailType = "tmp_on_order";
                break;
            case 3 :
                detailType = "tmp_write_off";
                break;
            case 4 :
                detailType = "tmp_transfer_out";
                break;
            case 5 :
                detailType = "tmp_adjustment";
                break;
            case 6 :
                detailType = "tmp_stock_take";
                break;
            case 7 :
                detailType = "tmp_return";
                break;
            case 8 :
                detailType = "tmp_return";
                break;
            case 10 :
                detailType = "tmp_ci_adjustment";
                break;
            case 12 :
                detailType = "tmp_receive";
                break;
            case 13 :
                detailType = "tmp_receive";
                break;
            case 15 :
                detailType = "tmp_transfer_in";
                break;
            case 16 :
                if(_type.equals("07")){
                    detailType = "tmp_receive_corr";  // 07
                }else if(_type.equals("08")){
                    detailType = "tmp_return_corr";   // 08
                }
                break;
            case 17 :
                if(_type.equals("504")){
                    detailType = "tmp_transfer_in_corr";  // 504
                }else if(_type.equals("505")){
                    detailType = "tmp_transfer_out_corr";   // 505
                }
                break;
            case 23 :
                detailType = "tmp_transfer_out";
                break;
        }
       return detailType;
    }
}
