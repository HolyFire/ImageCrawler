package com.icecloud;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by icecloud on 17/1/16.
 */
public class Crawler {
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }

    public static String  getUrlContent(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        return new String(getData);

    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        String website = "https://www.pexels.com/search";
        String website2 = "https://www.pexels.com/search/cat.js?page=1";
        String img1 = "https://images.pexels.com/photos/104827/cat-pet-animal-domestic-104827.jpeg?w=940&h=650&auto=compress&cs=tinysrgb";
        try {
            String content = getUrlContent(website2);
            Pattern pattern = Pattern.compile("[\\s\\S]+<a href=\\\\\"/search/\\w+\\.js\\?page=(\\d+)\\\\\">(\\d+)<\\\\/a>");
            Matcher matcher = pattern.matcher(content);
            matcher.find();
            System.out.println(matcher.group(1));

            Pattern img_pattern = Pattern.compile("src=\\\\\"(https://[^<>]+/([^<>]+\\.\\w+))\\?");
            Matcher img_matcher = img_pattern.matcher(content);

            System.out.println(new File("./").list()[1]);
            while (img_matcher.find()){
                String url = img_matcher.group(1);
                String file_name = img_matcher.group(2);
                System.out.println(url);
                System.out.println(file_name);
                downLoadFromUrl(url,file_name,"./Downloads/");

            }

            
//            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
