package com.ame.mr2.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PairSort implements WritableComparable<PairSort> {

    private  String first;
    private  Integer second;

    //  a   9
    //  a   9
    @Override
    public String toString() {
        return first+"\t"+second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    /**
     * 这个方法就是实现我们的比较器
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(PairSort o) {
        //比较我们第一列的数据
        int i = this.first.compareTo(o.first);
        //如果判断不等于0，那么就说明第一列不相等  a   b
        if(i != 0){
            //如果第一列不相等，那么优先按照第一列来做排序
            //直接返回比较的结果，就可以把我们的数据进行排序
            return  i ;
        }else{
            //如果第一列相等了   a   a
            //如果第一列相等，那么就要比较第二列了
            int i1 = this.second.compareTo(o.second);
            //如果第一列相等了，那么就比较第二列，直接将第二列的值返回回去，就可以做排序了
            //默认比较是按照升序排序，如果需要降序排序，那么就直接取反即可
            return -i1;
        }
    }

    /**
     * 序列化的方法
     * @param out
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(first);
        out.writeInt(second);
    }

    /**
     * 反序列化的方法
     * @param in
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.first = in.readUTF();
        this.second = in.readInt();
    }
}
