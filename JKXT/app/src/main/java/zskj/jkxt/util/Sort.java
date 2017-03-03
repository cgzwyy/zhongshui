package zskj.jkxt.util;

import java.util.HashMap;
import java.util.Map;

import zskj.jkxt.domain.Fan;

/**
 * Created by WYY on 2017/3/2.
 */

public class Sort {
    public static void main(String[] args)
    {
        String[] a = {"8a","77d","2a","66a","99b","2f","7a","4m","6b","100d","34w"};
        String[] b ;
        b = selectsort(a);
        for(int i  = 0; i < b.length; i++)
        {
            System.out.println(b[i]);
        }
    }

    public static String[] selectsort(String[] p)
    {
        String temp = null;
        int minindex = 0;
        String[] array = p.clone();
        for(int i = 0; i < array.length; i++)
        {
            minindex = i;
            for(int j = i+1; j < array.length; j++)
            {
                if(array[minindex].compareTo(array[j]) > 0 )
                    minindex = j;
            }
            temp = array[i];
            array[i] = array[minindex];
            array[minindex] = temp;
        }
        return array;
    }
    public static Map<Integer,Fan> fanSort(Fan[] p)
    {
        Fan temp = null;
        int minindex = 0;
        Fan[] array = p.clone();

        for(int i = 0; i < array.length; i++)
        {
            minindex = i;
            for(int j = i+1; j < array.length; j++)
            {
                if(array[minindex].fan_number.compareTo(array[j].fan_number) > 0 )
                    minindex = j;
            }
            temp = array[i];
            array[i] = array[minindex];
            array[minindex] = temp;
        }
        Map<Integer,Fan> map = new HashMap<Integer, Fan>();

        for(int i=0;i<array.length;i++){
            map.put(i,array[i]);
        }
        return map;
    }
}
