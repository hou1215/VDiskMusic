package com.hyuan.diy.utils;

import java.lang.reflect.Field;
import java.util.List;

import com.hyuan.diy.entity.Musics;

/**
 * @author  ��listת����json��ʽ
 */

public class ListToJson
{


        public static String listToJson(List<Musics> list)
        {

        StringBuffer jsonStr = new StringBuffer();

        if (null == list || 0 == list.size())
        {
                return null;
        }

        Class<?> classType = list.get(0).getClass();

        String javaBeanClassName = classType.getSimpleName();

        jsonStr.append("{\"").append(javaBeanClassName.toLowerCase()).append("s").append("\":[");

        Field[] fields = classType.getDeclaredFields();
        for (int i = 0; i < list.size(); i++)
        {
            jsonStr.append("{");
            for (Field field : fields)
            {

                String fieldName = field.getName();
                field.setAccessible(true);
                 // �õ�ָ�������ϴ� Field ��ʾ���ֶε�ֵ
                Object fieldValue;
                try
                {
                    fieldValue = field.get(list.get(i));
                    jsonStr.append("\"").append(fieldName.toLowerCase()).append("\":").append("\"").append(fieldValue).append("\"").append(",");
                } catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                }
                jsonStr.deleteCharAt(jsonStr.length() - 1);
                jsonStr.append("},");
            }
            jsonStr.deleteCharAt(jsonStr.length() - 1);
            jsonStr.append("]}");
            
            return jsonStr.toString();
        }

}
