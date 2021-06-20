package Utils;

import com.jayway.jsonpath.JsonPath;


public class JSONPathFinder {
    public static Object getValueByJPath(String responseString, String jpath) {
        return (JsonPath.read(responseString, jpath));
    }
}
