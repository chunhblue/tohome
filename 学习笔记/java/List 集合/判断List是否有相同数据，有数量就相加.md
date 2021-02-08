判断List中是否有相同的数据，有的话数量就相加

```java
 public String saveBIrtQty(List<SaveInventoryQty> oldList){
 	List<SaveInventoryQty> newList = new ArrayList<>();
    if(oldList.size()>0){
        for(SaveInventoryQty saveOld:oldList){
                boolean checkFlg = true;
                for(SaveInventoryQty saveQtyNew : newList){
                    // newList与oldList 的商品id相同，数量相加
                    if(saveOld.getArticleId().equals(saveQtyNew.getArticleId())){               			      		   saveQtyNew.setInventoryQty(
                              saveQtyNew.getInventoryQty()+saveOld.getInventoryQty());
                        checkFlg = false;
                    }
                }
                if(checkFlg){
                    newList.add(saveOld);
                }  
        }
    }
 }

```

