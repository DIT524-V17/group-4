package sem.simpleftp.utils;



public class StringUtils {

    public static boolean isEmpty(CharSequence str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
    
    public static int length(CharSequence str){
        if(str == null){
            return 0;
        } else {
            return str.length();
        }
    }
    
    public static boolean equals(String s, String s1){
        if(s != null && s1 != null){
            return s.equals(s1);
        }
        return false;
    }
    
    public static boolean equalsIgnoreNull(String s, String s1){
    	if(s == null){
    		s = "";
    	}
    	
    	if(s1 == null) {
    		s1 = "";
    	}

        return s.equals(s1);
    }
    
    public static boolean hasLowerChar(CharSequence str){
        if(isEmpty(str)){
            return false;
        }
        for(char c : str.toString().toCharArray()){
            if(c >= 'a' && c <= 'z'){
                return true;
            }
        }
        return false;
    }
    
    public static boolean strEquals(String str1, String str2) {
		if(isEmpty(str1) && isEmpty(str2)){
			return true;
		} else if(str1 != null && str1.equals(str2)){
			return true;
		} 
		return false;
	}
}
