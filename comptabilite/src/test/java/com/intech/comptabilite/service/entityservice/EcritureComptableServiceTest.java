package com.intech.comptabilite.service.entityservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.intech.comptabilite.model.CompteComptable;
import com.intech.comptabilite.model.EcritureComptable;
import com.intech.comptabilite.model.JournalComptable;
import com.intech.comptabilite.model.LigneEcritureComptable;
import com.intech.comptabilite.repositories.EcritureComptableRepository;
import com.intech.comptabilite.service.exceptions.NotFoundException;

@SpringBootTest
public class EcritureComptableServiceTest {
	
	@Autowired
	private EcritureComptableService ecritureComptableService;
	
	@MockBean
	EcritureComptableRepository e;

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }
    
    @Test
    public void getTotalDebit() {
    	EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Débit total > 0");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assertions.assertEquals(341, ecritureComptableService.getTotalDebit(vEcriture).intValue());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Débit total = 0");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "0", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "0", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "0", "2"));
        Assertions.assertEquals(BigDecimal.valueOf(0), ecritureComptableService.getTotalDebit(vEcriture));
    }
    
    @Test
    public void getTotalCredit() {
    	EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Crédit total > 0");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assertions.assertEquals(341, ecritureComptableService.getTotalCredit(vEcriture).intValue());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Crédit total = 0");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "0"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "0"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "0"));
        Assertions.assertEquals(BigDecimal.valueOf(0), ecritureComptableService.getTotalCredit(vEcriture));
    }
    
    @Test
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assertions.assertTrue(ecritureComptableService.isEquilibree(vEcriture));

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assertions.assertFalse(ecritureComptableService.isEquilibree(vEcriture));
    }
    
    @Test
    public void insertEcritureComptable() {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setReference("BQ-2016/00001");
        EcritureComptable ecritureSaved;
        
        ecritureSaved = ecritureComptableService.insertEcritureComptable(ecritureComptable);
        
        try {
			assertEquals(ecritureSaved, ecritureComptableService.getEcritureComptableByRef("BQ-2016/00001"));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void updateEcritureComptable() {
    	EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setReference("BQ-2016/00001");
        
        ecritureComptableService.insertEcritureComptable(ecritureComptable);
        ecritureComptable.setReference("AC-2016/00001");
        ecritureComptable = ecritureComptableService.updateEcritureComptable(ecritureComptable);
        
        try {
			assertNull(ecritureComptableService.getEcritureComptableByRef("BQ-2016/00001"));
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			assertEquals(ecritureComptable, ecritureComptableService.getEcritureComptableByRef("AC-2016/00001"));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void deleteEcritureComptable() {
    	EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(10);
        ecritureComptable.setReference("BQ-2016/00001");
        
        try {
			assertNotNull(ecritureComptableService.getEcritureComptableByRef("BQ-2016/00001"));
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        ecritureComptableService.insertEcritureComptable(ecritureComptable);
        ecritureComptableService.deleteEcritureComptable(10);
        
        try {
			assertNull(ecritureComptableService.getEcritureComptableByRef("BQ-2016/00001"));
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    @Test
    public void getListEcritureComptable() {
    	EcritureComptable ecritureComptable = new EcritureComptable();
    	List<EcritureComptable> eList = ecritureComptableService.getListEcritureComptable();
    	eList.add(ecritureComptable);
	    
	    assertTrue(eList.size() > 0);
    }
    
    @Test
    public void getEcritureComptableByRef() {
        EcritureComptable ecritureComptable = new EcritureComptable();
        String ref = "BQ-2016/00001";
        ecritureComptable.setReference(ref);

        Mockito.when(e.getByReference(ecritureComptable.getReference()))
                 .thenReturn(java.util.Optional.of(ecritureComptable));

       Assertions.assertDoesNotThrow(() -> {
    	   ecritureComptableService.getEcritureComptableByRef(ecritureComptable.getReference());
       });
       
       ref = "blablabla";
       ecritureComptable.setReference(ref);
       
       Assertions.assertThrows(NotFoundException.class, () -> ecritureComptableService.getEcritureComptableByRef(ecritureComptable.getReference()));
    }
}
