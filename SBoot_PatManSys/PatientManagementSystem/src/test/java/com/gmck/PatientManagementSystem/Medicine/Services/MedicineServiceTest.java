package com.gmck.PatientManagementSystem.Medicine.Services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.gmck.PatientManagementSystem.Medicine.Medicine;
import com.gmck.PatientManagementSystem.Medicine.Repositories.MedicineRepo;
import com.gmck.PatientManagementSystem.UserModel.Services.PatientService;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {

	@Mock
	private MedicineRepo repo;
	@Mock
	private PatientService patService;
	
	@InjectMocks
	@Autowired
	private MedicineService instance;
	
	@Test
	void testGetAllMedicines() {
		List<Medicine> expResult = getMedicineData();
		
		Mockito.doReturn(expResult)
		.when(repo)
		.findAll();
						
		List<Medicine> result = instance.getAllMedicines();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testPrescribeMedicine_quantityReductionCheck() {
		Medicine medicine = getMedicineData().get(0);
		String patId = "P1003";
		String docId = "D1003";
		int prescribedAmount = 3;
		
		Mockito.doReturn(medicine)
		.when(repo)
		.getById(medicine.getMedName());
		
		instance.prescribeMedicine(patId, docId, medicine.getMedName(), prescribedAmount);
		
		ArgumentCaptor<Medicine> argCaptor = ArgumentCaptor.forClass(Medicine.class);
		verify(repo).save(argCaptor.capture());
		
		Medicine result = argCaptor.getValue();
		medicine.setQuantity(medicine.getQuantity() - prescribedAmount);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(medicine);
	}

	@Test
	void testPrescribeMedicine_prescribeToPatient() {
		Medicine medicine = getMedicineData().get(0);
		String patId = "P1003";
		String docId = "D1003";
		int prescribedAmount = 3;
		String prescription = medicine.getMedName() + ". " + medicine.getMedDosage() + ". " + medicine.getCommonUses() 
		+ ". " + Integer.toString(prescribedAmount) +".";
		
		Mockito.doReturn(medicine)
		.when(repo)
		.getById(medicine.getMedName());
		
		instance.prescribeMedicine(patId, docId, medicine.getMedName(), prescribedAmount);
		
		verify(patService).addPatientPrescription(patId, prescription, docId, LocalDate.now());
	}
	
	@Test
	void testAddMedicine() {
		Medicine medicine = getMedicineData().get(0);
		
		instance.addMedicine(medicine.getMedName(), medicine.getMedDosage(), medicine.getCommonUses());
		
		ArgumentCaptor<Medicine> argCaptor = ArgumentCaptor.forClass(Medicine.class);
		verify(repo).save(argCaptor.capture());
		
		Medicine capturedMedicine = argCaptor.getValue();
		medicine.setQuantity(0);
		
		assertThat(capturedMedicine).usingRecursiveComparison().isEqualTo(medicine);
	}
	
	@Test
	void testRemoveMedicine() {
		Medicine medicine = getMedicineData().get(0);
				
		instance.removeMedicine(medicine.getMedName());
		
		verify(repo, times(1)).deleteById(medicine.getMedName());
	}

	@Test
	void testOrderMedicine() {
		Medicine medicine = getMedicineData().get(0);
		int orderAmount = 4;
		
		Mockito.doReturn(medicine)
		.when(repo)
		.getById(medicine.getMedName());
		
		instance.orderMedicine(medicine.getMedName(), orderAmount);
		
		ArgumentCaptor<Medicine> argCaptor = ArgumentCaptor.forClass(Medicine.class);
		verify(repo).save(argCaptor.capture());
		
		Medicine result = argCaptor.getValue();
		medicine.setQuantity(medicine.getQuantity() + orderAmount);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(medicine);
	}

	@Test
	void testSaveMedicine() {
		Medicine medicine = getMedicineData().get(0);
		
		instance.saveMedicine(medicine);
		
		verify(repo).save(medicine);
	}

	private List<Medicine> getMedicineData(){
		List<Medicine> medList = new ArrayList<>();
		
		Medicine med1 = new Medicine("Med Name 1", "Take 5-6", "Commonly used for many", 10);
		Medicine med2 = new Medicine("Med Name 1", "Take 5-6", "Commonly used for many", 10);
		
		medList.add(med1);
		medList.add(med2);
		
		return medList;
	}
}
