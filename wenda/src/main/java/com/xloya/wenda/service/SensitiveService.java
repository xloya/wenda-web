package com.xloya.wenda.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveService.class);
    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineText;
            while((lineText = bufferedReader.readLine())!=null){
                addWord(lineText.trim());
            }

            reader.close();
        }catch (Exception e){
            LOGGER.error("读取敏感词文件失败"+e.getMessage());
        }
    }

    // 增加关键词abc
    private void addWord(String lineText){
        TrieNode tempNode = rootNode;
        for(int i=0;i<lineText.length();i++){

            Character character = lineText.charAt(i);

            if(isSymbol(character)){
                continue;
            }

            TrieNode node = tempNode.getSubNode(character);

            // 如果没有查找到当前字符，则添加到当前结点下面
            if(node==null){
                node = new TrieNode();
                tempNode.addSubNode(character,node);
            }

            tempNode = node;

            // 如果为最后一个字符，则标记
            if(i==lineText.length()-1){
                tempNode.setKeyWord(true);
            }
        }

    }

    private class TrieNode{

        // 是不是关键词的结尾
        private boolean end = false;

        // 当前节点下所有的子节点
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node){
            subNodes.put(key,node);
        }

        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeyWordEnd(){
            return end;
        }

        void setKeyWord(boolean end){
            this.end = end;
        }
    }


    private TrieNode rootNode = new TrieNode();


    // 判断是否不是东亚文字和ascii常用字符
    private boolean isSymbol(char c){
        int ic = (int) c;
        // 东亚文字0x2E80-0x9FFF
        return !((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    // 过滤
    public String filter(String text){
        if(StringUtils.isEmpty(text)){
            return text;
        }

        StringBuilder result = new StringBuilder();

        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while(position < text.length()){
            char c = text.charAt(position);

            // 判断是否是特殊字符，如空格
            if(isSymbol(c)){
                if(tempNode == rootNode){
                    result.append(c);
                    begin++;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);


            if(tempNode==null){
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            }else if(tempNode.isKeyWordEnd()){
                // 发现敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else{
                ++position;
            }
        }

        result.append(text.substring(begin));

        return result.toString();
    }
}
