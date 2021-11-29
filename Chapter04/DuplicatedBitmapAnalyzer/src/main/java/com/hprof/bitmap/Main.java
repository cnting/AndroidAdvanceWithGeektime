package com.hprof.bitmap;

import com.android.tools.perflib.captures.DataBuffer;
import com.android.tools.perflib.captures.MemoryMappedFileBuffer;
import com.google.gson.Gson;
import com.squareup.haha.perflib.ArrayInstance;
import com.squareup.haha.perflib.ClassInstance;
import com.squareup.haha.perflib.ClassObj;
import com.squareup.haha.perflib.Heap;
import com.squareup.haha.perflib.Instance;
import com.squareup.haha.perflib.Snapshot;
import com.sun.prism.shader.DrawEllipse_LinearGradient_REFLECT_AlphaTest_Loader;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("doc/test.hprof");
        DataBuffer dataBuffer = new MemoryMappedFileBuffer(file);
        // 获得snapshot
        Snapshot snapshot = Snapshot.createSnapshot(dataBuffer);
        //执行这个方法才能获得栈信息
        snapshot.computeDominators();
        // 获得Bitmap Class
        ClassObj bitmapClass = snapshot.findClass("android.graphics.Bitmap");
        // 获得heap, 只需要分析app和default heap即可
        Collection<Heap> heaps = snapshot.getHeaps();
        //解析的结果
        Map<String, Result> resultMap = new HashMap<>();
        for (Heap heap : heaps) {
            if (heap.getName().equals("app") || heap.getName().equals("default")) {
                // 从heap中获得所有的Bitmap实例
                final List<Instance> bitmapInstances = bitmapClass.getHeapInstances(heap.getId());

                for (Instance instance : bitmapInstances) {
                    List<ClassInstance.FieldValue> fieldList = ((ClassInstance) instance).getValues();
                    // 从Bitmap实例中获得buffer数组，可以看7.0以下的源码：http://androidxref.com/7.1.2_r36/xref/frameworks/base/graphics/java/android/graphics/Bitmap.java
                    ArrayInstance buffer = HahaHelper.fieldValue(fieldList, "mBuffer");
                    if (buffer != null) {
                        byte[] bytes = buffer.asRawByteArray(0, buffer.getLength());
                        String md5 = getMD5(bytes);

                        Result result = resultMap.get(md5);
                        if (result == null) {
                            result = new Result();
                            result.width = HahaHelper.fieldValue(fieldList, "mWidth");
                            result.height = HahaHelper.fieldValue(fieldList, "mHeight");
                            result.bufferSize = bytes.length;
                            result.bufferHash = md5;
                            result.stacks = new ArrayList<>();
                        }
                        result.stacks.add(getStacks(instance));
                        result.duplicateCount = result.stacks.size();
                        resultMap.put(md5, result);
                    }
                }
            }
        }

        System.out.println(new Gson().toJson(resultMap.values()));

    }

    private static List<String> getStacks(Instance instance) {
        List<String> stacks = new ArrayList<>();
        while (instance != null) {
            stacks.add(instance.toString());
            instance = instance.getNextInstanceToGcRoot();
        }
        Collections.reverse(stacks);
        return stacks;
    }

    private static String getMD5(byte[] source) {
        MessageDigest md5;
        byte[] result = new byte[source.length];
        try {
            md5 = MessageDigest.getInstance("MD5");
            result = md5.digest(source);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            //以16进制输出，2位指定输出字段的宽度
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    static class Result {
        int width;
        int height;
        int bufferSize;
        String bufferHash;
        int duplicateCount;
        List<List<String>> stacks;
    }
}
