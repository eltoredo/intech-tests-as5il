package com.intech.comptabilite.service.entityservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.intech.comptabilite.model.CompteComptable;

@SpringBootTest
public class CompteComptableServiceTest {
	
	@Autowired
	private CompteComptableService compteComptableService;

    @Test
    public void getByNumero() {
    	List<CompteComptable> pList = new ArrayList<>();
    	CompteComptable compteComptableUn;
    	CompteComptable compteComptableDeux;
    	
    	compteComptableUn = new CompteComptable();
    	compteComptableUn.setNumero(1);
    	compteComptableDeux = new CompteComptable();
    	compteComptableDeux.setNumero(2);
    	
    	pList.add(compteComptableUn);
    	pList.add(compteComptableDeux);
    	
    	assertEquals(compteComptableUn, compteComptableService.getByNumero(pList, 1));
    	assertEquals(null, compteComptableService.getByNumero(pList, 3));
    }
}
