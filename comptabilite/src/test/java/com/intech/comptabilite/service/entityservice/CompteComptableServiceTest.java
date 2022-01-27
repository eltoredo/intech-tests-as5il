package com.intech.comptabilite.service.entityservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.intech.comptabilite.model.CompteComptable;
import com.intech.comptabilite.model.JournalComptable;
import com.intech.comptabilite.repositories.CompteComptableRepository;

@SpringBootTest
public class CompteComptableServiceTest {
	
	@Autowired
	private CompteComptableService compteComptableService;
	
	@MockBean
	CompteComptableRepository c;

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
    
    @Test
    public void getListCompteComptable() {
    	CompteComptable compteComptable = new CompteComptable();
    	List<CompteComptable> cList = compteComptableService.getListCompteComptable();
    	cList.add(compteComptable);
	    
	    assertTrue(cList.size() > 0);
    }
}
