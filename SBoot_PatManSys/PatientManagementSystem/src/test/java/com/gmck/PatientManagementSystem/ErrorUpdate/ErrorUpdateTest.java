package com.gmck.PatientManagementSystem.ErrorUpdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ErrorUpdateTest {

	private ErrorUpdate instance;
	
	@Mock
	private IErrorUpdateObserver testObserverImpl;
	
	@Mock
	private IErrorUpdateObserver testObserverImpl2;
	
	@BeforeEach
	void setUp() throws Exception {
		this.instance = ErrorUpdate.getInstance();
	}

	@AfterEach
	void tearDown() throws Exception {
		instance.getObservers().clear();
		assertThat(instance.getObservers().size()).isEqualTo(0);
		
		instance = null;
		assertThat(instance).isNull();
	}

	@Test
	void testAddObserver() {
		instance.addObserver(testObserverImpl);
		
		List<IErrorUpdateObserver> result = instance.getObservers();
		List<IErrorUpdateObserver> expResult = new ArrayList<>();
		expResult.add(testObserverImpl);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testRemoveObserver() {
		instance.addObserver(testObserverImpl);
		
		if(instance.getObservers().size() <= 0) {
			fail("Observer wasn't added to be removed");
		}
		
		instance.removeObserver(testObserverImpl);
		
		assertThat(instance.getObservers()).isEmpty();
	}
	
	@Test
	void testRemoveAllObservers() {
		instance.addObserver(testObserverImpl);
		instance.addObserver(testObserverImpl2);
		
		if(instance.getObservers().size() <= 0) {
			fail("Observer wasn't added to be removed");
		}
		
		instance.removeAllObservers();
		
		assertThat(instance.getObservers()).isEmpty();
	}

	@Test
	void testUpdateObserver() {
		String errorMessage = "Error has occurred";
		
		instance.addObserver(testObserverImpl);
		
		instance.updateObserver(errorMessage);
		
		ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
		verify(testObserverImpl).update(argCaptor.capture());		
		String result = argCaptor.getValue();
		
		assertThat(result).isEqualTo(errorMessage);
	}
}
