package plus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * Created by Administrator on 2019/8/10.
 */
public class CustomerSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList=super.getMethodList();
        methodList.add(new BatchInsertByList());
        return methodList;
    }
}
