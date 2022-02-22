package com.gmck.PatientManagementSystem.util;

/**
 * Utility class to return an Enum of specified class type from val parameter. 
 * @author Glenn McKnight
 *
 */
public class EnumLookup {
	
	/**
	 * Get the enum of a value passed to method. 
	 * @param <E> - class extended Enum. 
	 * @param e - the enum class to get value from.
	 * @param val - value to get enum for. 
	 * @return Enum - appropriate enum for specified class and value. 
	 */
    public static <E extends Enum<E>> E lookup(Class<E> e, String val) {   
    	
	    E result = null;
        try {         
            result = Enum.valueOf(e, val);
            
        } catch (IllegalArgumentException ex) {
            System.out.println("Enum not found");
        } 
        return result;
    }		
}
