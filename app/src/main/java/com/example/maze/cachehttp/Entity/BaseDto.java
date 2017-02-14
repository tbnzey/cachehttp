package com.example.maze.cachehttp.Entity;/**
 * Created by maze on 2016/8/11.
 */

import com.example.maze.cachehttp.Utils.Tools;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>类描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法描述
 */
public class BaseDto implements Serializable{

    private Map<String, Object> map;
    private int code = 0;
    private String result;
    private String msg;
    private String JsonStr;

    private BaseDto(){}

    public BaseDto(String JsonStr){
        this.JsonStr = JsonStr;
        try {
            map = parserToMap(JsonStr);
            setCode(map.get("code") == null ? 0 : Integer.valueOf(map.get("code").toString()));
            setMsg(map.get("msg") == null ? "" : map.get("msg").toString());
            setResult(map.get("result") == null ? "" : map.get("result").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map parserToMap(String s) throws Exception {
        Map map = new LinkedHashMap();
        if (s != null && s.trim().length() > 0) {
            JSONObject json = new JSONObject(s);
            Iterator keys = json.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String valueStr = json.get(key).toString();
                if (valueStr.startsWith("{") && valueStr.endsWith("}")) {
                    map.put(key, parserToMap(valueStr));
                } else {
                    map.put(key, json.get(key));
                }
            }
        }
        return map;
    }

    public boolean isCode(){
        if(code == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCode(int code){
        if(code == code) return true;
        else return false;
    }

    public <T>T getResultElement(T t, String params){
        Map<String, Object> resultMap = getMapResult("result");
        return getMapResult(resultMap, t, params);
    }

    public <T>T getResultElement(String params){
        Map<String, Object> resultMap = getMapResult("result");
        return (T) getMapResult(resultMap, "", params);
    }

    public <T>T getMapResult(String pars){
        return (T) getMapResult(null, pars);
    }

    public <T>T getMapResult(T t, String pars){return getMapResult(map, t, pars);}

    /**
     *
     * @param map 搜索map
     * @param t 搜索结果默认值
     * @param pars 搜索内容
     * @param <T> 返回类型
     * @return
     */
    public <T>T getMapResult(Map map, T t, String pars){
        T res;
        if(t == null)
            res = map.get(pars) == null ? t : (T) map.get(pars);
        else
            res = map.get(pars) == null ? t : (T) map.get(pars).toString();
        return res;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return Tools.notBlank(msg) ? msg : "网络不良请刷新";
    }

    public String getMsgs(String defaultMsg){
        if(Tools.notBlank(msg)){
            return msg;
        }else{
            return defaultMsg;
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJsonStr() {
        return JsonStr;
    }

    public void setJsonStr(String jsonStr) {
        JsonStr = jsonStr;
    }
}
