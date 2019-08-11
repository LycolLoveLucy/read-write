package plus.pagination;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import plus.Page;

/**
 * Created by Administrator on 2019/8/11.
 */
public class DialectBuilder {



    public static    String  pageAbleForH2Select(String originalSql,Page page){
        Assert.notNull(page,"the page Object must not be null");
        if(page.getCurrentPage()>1){
            return  String.format("select * from ( %s) temp limit %d offset %d",originalSql,page.getPageSize()*page.getPageSize(),(page.getCurrentPage()-1)*page.getPageSize());
        }
        return  String.format("select * from %s limit %d",page.getPageSize());
    }
}
