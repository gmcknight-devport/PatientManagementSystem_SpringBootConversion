package com.gmck.PatientManagementSystem.LoginServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Factory service class to select the login strategy to be executed.
 * @author Glenn McKnight
 *
 */
@Component
public class StrategySelectFactory {
		
	@Autowired
    private List<ILoginStrategy> strategies;
    
	private static final Map<UserType, ILoginStrategy> strategyCache = new HashMap<>();

    /**
     * PostContruct method to initialise strategyCache with UserType from
     * each strategy and the service. 
     */
    @PostConstruct
    private void initMyServiceCache() {
        for(ILoginStrategy service : strategies) {
            strategyCache.put(service.getType(), service);
        }
    }
	
    /**
     * Execute the required login strategy.
     * Get the strategy from the cache using the UserType parameter, and
     * execute the strategy. 
     * @param type - The UserType for the strategy to be run.
     * @param userId - The ID of the user to be logged in.
     */
	public void execute(UserType type, String userId) {	
		try {
			ILoginStrategy strategy = strategyCache.get(type);        
			strategy.execute(userId);    
			
		}catch(RuntimeException e) {
			ErrorUpdate.getInstance().updateObserver("Couldn't login - error");
		}
	}
}
