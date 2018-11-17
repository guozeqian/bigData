package com.ame.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class MyUDF extends UDF {
    /**
     * 继承UDF这个类 然后重写evalute这个方法，方法的参数就是
     * 我们传入进来的数据
     *
     * @param line
     * @return
     */
    public Text evaluate(final Text line) {
        if (null != line && !line.toString().equals("")) {
            //将我们的一行数据转换成大写
            String str = line.toString().toUpperCase();
            line.set(str);
            return line;
        }
        line.set("");
        return line;
    }


}
