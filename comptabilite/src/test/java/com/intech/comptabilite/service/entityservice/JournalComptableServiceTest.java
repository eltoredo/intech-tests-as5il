package com.intech.comptabilite.service.entityservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.intech.comptabilite.model.JournalComptable;

@SpringBootTest
public class JournalComptableServiceTest {
	
	@Autowired
	private JournalComptableService journalComptableService;

    @Test
    public void getByCode() {
    	List<JournalComptable> pList = new ArrayList<>();
    	JournalComptable journalComptableUn;
    	JournalComptable journalComptableDeux;
    	
    	journalComptableUn = new JournalComptable();
    	journalComptableUn.setCode("1");
    	journalComptableDeux = new JournalComptable();
    	journalComptableDeux.setCode("2");
    	
    	pList.add(journalComptableUn);
    	pList.add(journalComptableDeux);
    	
    	assertEquals(journalComptableUn, journalComptableService.getByCode(pList, "1"));
    	assertEquals(null, journalComptableService.getByCode(pList, "3"));
    }
}
