package com.gmck.PatientManagementSystem.Messaging.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.util.EnumLookup;

/**
 * Factory service class to select the message service to be used.
 * @author Glenn McKnight
 *
 */
@Service
public class MessageServiceFactory {
	
    private List<IMessageService> services;
    private static final Map<UserType, IMessageService> serviceCache = new HashMap<>();

    /**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables.
	 */
    @Autowired
    public MessageServiceFactory(List<IMessageService> services) {
    	this.services = services;
    }
    
    /**
     * PostContruct method to initialise serviceCache with UserType from
     * each service and the service itself. 
     */
    @PostConstruct
    private void initMyServiceCache() {
        for(IMessageService service : services) {
            serviceCache.put(service.getType(), service);
        }
    }

    /**
     * Get the required message service. 
     * Lookup required type passed as a parameter, get the appropriate service from
     * the cache and retun it. 
     * @param userId - the ID to get the UserType from. 
     * @return IMessageService - the service determined to be required by the method
     * 		based on the parameter.
     * @throws IllegalArgumentException - thrown if service can't be found.
     */
    public static IMessageService getMessageService(String userId) throws IllegalArgumentException {
    	try {
	    	UserType type;
	    	type = EnumLookup.lookup(UserType.class, userId.substring(0, 1));
	    	
	        IMessageService service = serviceCache.get(type);
	                
	        return service;
    	}catch(RuntimeException ex) {
    		ErrorUpdate.getInstance().updateObserver("Couldn't create message - error");
    		throw new RuntimeException("Couldn't create message - error");
    	}
    }
}
