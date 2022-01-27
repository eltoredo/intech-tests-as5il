package com.intech.comptabilite.service.entityservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.intech.comptabilite.model.EcritureComptable;
import com.intech.comptabilite.model.SequenceEcritureComptable;
import com.intech.comptabilite.service.exceptions.NotFoundException;

@SpringBootTest
public class SequenceEcritureComptableServiceTest {
	
	@Autowired
	private SequenceEcritureComptableService sequenceEcritureComptableService;
	
	@Test
	public void getLastValueByCodeAndYear() {
		int value = 0;

		try {
			value = sequenceEcritureComptableService.getDernierValeurByCodeAndAnnee("BQ", 2016);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		assertTrue(value > 0);
	}
}
