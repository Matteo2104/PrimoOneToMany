package it.prova.test;

import java.util.List;

import javax.management.RuntimeErrorException;

import it.prova.dao.ArticoloDAO;
import it.prova.dao.NegozioDAO;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

@SuppressWarnings("unused")
public class NegozioTest {

	public static void main(String[] args) {
		NegozioDAO negozioDAOInstance = new NegozioDAO();
		ArticoloDAO articoloDAOInstance = new ArticoloDAO();

		// ora con i dao posso fare tutte le invocazioni che mi servono
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testInserimentoNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testFindByIdNegozio(negozioDAOInstance);

		testInsertArticolo(negozioDAOInstance, articoloDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testFindByIdArticolo(articoloDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		// ESERCIZIO: COMPLETARE DAO E TEST RELATIVI
		
		// funziona (ma rivedere il testing)
		//testSelectByIdWithJoin(articoloDAOInstance);
		//System.out.println("L'articolo trovato è: ");
		//System.out.println(articoloDAOInstance.selectByIdWithJoin((long)2));
		
		// funziona
		//testUpdate(articoloDAOInstance, negozioDAOInstance); 
		
		// funziona
		//testDelete(articoloDAOInstance, negozioDAOInstance);
		
		// funziona
		//testFindAllByNegozio(articoloDAOInstance, negozioDAOInstance);
		
		// funziona
		//testFindAllByIndirizzoNegozio(articoloDAOInstance, negozioDAOInstance);
		
		// funziona
		//testFindAllByMatricola(articoloDAOInstance, negozioDAOInstance);
		

		// funziona
		//testFindAllByIniziali(negozioDAOInstance);
		
		// 
		testPopulateArticoli(negozioDAOInstance, articoloDAOInstance);
		
		
		// ESERCIZIO SUCCESSIVO
		/*
		 * se io voglio caricare un negozio e contestualmente anche i suoi articoli
		 * dovrò sfruttare il populateArticoli presente dentro negoziodao. Per esempio
		 * Negozio negozioCaricatoDalDb = negozioDAOInstance.selectById...
		 * 
		 * negozioDAOInstance.populateArticoli(negozioCaricatoDalDb);
		 * 
		 * e da qui in poi il negozioCaricatoDalDb.getArticoli() non deve essere più a
		 * size=0 (se ha articoli ovviamente) LAZY FETCHING (poi ve lo spiego)
		 */

	}
	
	
	

	private static void testInserimentoNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testInserimentoNegozio inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testFindByIdNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindByIdNegozio inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		Negozio negozioCheRicercoColDAO = negozioDAOInstance.selectById(primoNegozioDellaLista.getId());
		if (negozioCheRicercoColDAO == null
				|| !negozioCheRicercoColDAO.getNome().equals(primoNegozioDellaLista.getNome()))
			throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdNegozio fine: PASSED.............");
	}

	private static void testInsertArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testInsertArticolo inizio.............");
		// mi serve un negozio esistente
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", primoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		System.out.println(".......testInsertArticolo fine: PASSED.............");
	}

	private static void testFindByIdArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindByIdArticolo inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticolo : FAILED, non ci sono articoli sul DB");

		Articolo primoArticoloDellaLista = elencoArticoliPresenti.get(0);

		Articolo articoloCheRicercoColDAO = articoloDAOInstance.selectById(primoArticoloDellaLista.getId());
		if (articoloCheRicercoColDAO == null
				|| !articoloCheRicercoColDAO.getNome().equals(primoArticoloDellaLista.getNome()))
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdArticolo fine: PASSED.............");
	}
	
	@SuppressWarnings("unused")
	private static void testSelectByIdWithJoin(ArticoloDAO articoloDAOInstance) {
		Articolo articoloCheStoCercando = articoloDAOInstance.selectByIdWithJoin((long)2);
		
		if (articoloCheStoCercando == null) {
			throw new RuntimeException("testSelectByIdWithJoin : FAILED");
		}
		
		System.out.println("......testSelectByIdWithJoin fine: PASSED.............");
	}
	
	@SuppressWarnings("unused")
	private static void testUpdate(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDAOInstance) {
		System.out.println("........... inizio test update .............");
		
		// creo un articolo relativo ad un negozio preso a caso
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("negozio00", "via dell'acqua 47"));
		if (quantiNegoziInseriti < 1) 
			throw new RuntimeException("non sono stati inseriti i negozi");
		
		List<Negozio> listaTemp = negozioDAOInstance.list();
		Negozio negozioRandom = listaTemp.get(listaTemp.size()-1);
		
		Articolo articoloDaModificare = new Articolo("maglietta000", "matricola000", negozioRandom);
		int quantiArticoliInseriti = articoloDAOInstance.insert(articoloDaModificare);
		if (quantiArticoliInseriti < 1) 
			throw new RuntimeException("non sono stati inseriti gli articoli");
		
		// a questo punto desidero modificare l'articolo appena inserito
		 List<Articolo> listArticoloTemp = articoloDAOInstance.list();
		 Articolo articoloAppenaInserito = listArticoloTemp.get(listArticoloTemp.size()-1);
		 
		 articoloAppenaInserito.setMatricola("MATRICOLA_MODIFICATA");
		 articoloAppenaInserito.setNome("NOME_MODIFICATO");
		 
		 articoloDAOInstance.update(articoloAppenaInserito);
		 
		 // e stampo per vedere se le modifiche sono avvenute
		List<Articolo> lista = articoloDAOInstance.list();
		if (!lista.get(lista.size()-1).getMatricola().equals("MATRICOLA_MODIFICATA")) 
			throw new RuntimeException("Modifica non avvenuta");
		
		System.out.println(".......... fine test update ...............");
	}
	
	@SuppressWarnings("unused")
	private static void testDelete(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDAOInstance) {
		System.out.println("............. inizio test delete ................");
		
		// preparo un record da eliminare successivamente
		Negozio negozioTemp = negozioDAOInstance.list().get(negozioDAOInstance.list().size()-1);
		int quantiArticoliInseriti = articoloDAOInstance.insert(new Articolo("maglietta500", "matricola500", negozioTemp));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("Non sono stati inseriti articoli");
		
		// a questo punto eseguo la delete
		Articolo articoloDaEliminare = articoloDAOInstance.list().get(articoloDAOInstance.list().size()-1);
		int quantiArticoliEliminati = articoloDAOInstance.delete(articoloDaEliminare);
		if (quantiArticoliEliminati < 1)
			throw new RuntimeException("Non sono stati eliminati articoli");
		
		System.out.println("............ test delete terminato ................");
	}
	
	@SuppressWarnings("unused")
	private static void testFindAllByNegozio(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindAllByNegozio inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio5", "via dei fori 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non ci sono negozi");
		
		List<Negozio> negoziPresenti = negozioDAOInstance.list();
		Negozio negozioAppenaInserito = negoziPresenti.get(negoziPresenti.size()-1);
		
		//mi inserisco un paio di articoli
		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", negozioAppenaInserito));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		int quantiArticoliInseriti2 = articoloDAOInstance
				.insert(new Articolo("articolo2", "matricola2", negozioAppenaInserito));
		if (quantiArticoliInseriti2 < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		
		
		List<Articolo> listaDiArticoliPerDeteriminatoNegozio = articoloDAOInstance.findAllByNegozio(negozioAppenaInserito);
		if (listaDiArticoliPerDeteriminatoNegozio.size() != 2)
			throw new RuntimeException("testFindAllByNegozio : FAILED, attesi due articoli ed invece reperiti "+listaDiArticoliPerDeteriminatoNegozio.size());
		
		System.out.println("......testFindAllByNegozio fine: PASSED.............");
	}
	
	@SuppressWarnings("unused")
	private static void testFindAllByIndirizzoNegozio(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDAOInstance) {
		System.out.println("......test listaDiArticoliInBaseAIndirizzo INIZIO.............");
		
		// inserisco 2 negozi
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("NEGOZIO100", "via dei meli 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non ci sono negozi");
		
		int quantiNegoziInseriti2 = negozioDAOInstance.insert(new Negozio("NEGOZIO200", "via roma 14"));
		if (quantiNegoziInseriti2 < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non ci sono negozi");
		
		List<Negozio> negoziAppenaInseriti = negozioDAOInstance.list();
		Negozio negozio100 = negoziAppenaInseriti.get(negoziAppenaInseriti.size()-2);
		Negozio negozio200 = negoziAppenaInseriti.get(negoziAppenaInseriti.size()-1);
		
		// inserisco 3 articoli per ciascun negozio - negozio100
		int quantiArticoliInseriti = articoloDAOInstance.insert(new Articolo("articolo101", "matricola101", negozio100));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		
		int quantiArticoliInseriti2 = articoloDAOInstance.insert(new Articolo("articolo102", "matricola102", negozio100));
		if (quantiArticoliInseriti2 < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		
		int quantiArticoliInseriti3 = articoloDAOInstance.insert(new Articolo("articolo103", "matricola103", negozio100));
		if (quantiArticoliInseriti3 < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		
		
		// inserisco 3 articoli per ciascun negozio - negozio200
		int quantiArticoliInseriti4 = articoloDAOInstance.insert(new Articolo("articolo201", "matricola201", negozio200));
		if (quantiArticoliInseriti4 < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		
		int quantiArticoliInseriti5 = articoloDAOInstance.insert(new Articolo("articolo202", "matricola202", negozio200));
		if (quantiArticoliInseriti5 < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		
		int quantiArticoliInseriti6 = articoloDAOInstance.insert(new Articolo("articolo203", "matricola203", negozio200));
		if (quantiArticoliInseriti6 < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non riesco ad inserire articolo");
		
		
		// voglio trovare gli articoli in base all'indirizzo del negozio
		List<Articolo> listaDiArticoliInBaseAIndirizzo = articoloDAOInstance.findAllByIndirizzoNegozio("via roma 14");
		
		if (listaDiArticoliInBaseAIndirizzo.size() != 15) 
			throw new RuntimeException("testFindAllByNegozio : FAILED, dimensione lista attesi 3, ricevuti " + listaDiArticoliInBaseAIndirizzo.size());
		
		for (Articolo articolo : listaDiArticoliInBaseAIndirizzo) {
			System.out.println(articolo);
		}
		
		System.out.println("......listaDiArticoliInBaseAIndirizzo fine: PASSED.............");
		
	}
	
	public static void testFindAllByMatricola(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDAOInstance) {
		System.out.println("................. inizio del test findAllByMatricola ...........");
		
		// istanzio alcuni dati per il testing
		int quantiNegoziInseriti1 = negozioDAOInstance.insert(new Negozio("negozio300", "via dei monti 2"));
		if (quantiNegoziInseriti1 < 1) 
			throw new RuntimeException("testFindAllByMatricola : FAILED, inserimento negozio non riuscito");
		
		int quantiNegoziInseriti2 = negozioDAOInstance.insert(new Negozio("negozio400", "via dei mari 987"));
		if (quantiNegoziInseriti2 < 1) 
			throw new RuntimeException("testFindAllByMatricola : FAILED, inserimento negozio non riuscito");
		
		
		List<Negozio> listaTemp = negozioDAOInstance.list();
		Negozio negozioAppenaInserito1 = listaTemp.get(listaTemp.size()-2);
		Negozio negozioAppenaInserito2 = listaTemp.get(listaTemp.size()-1);
	
		
		int quantiArticoliInseriti1 = articoloDAOInstance.insert(new Articolo("articolo301", "matricola301", negozioAppenaInserito1));
		if (quantiArticoliInseriti1 < 1) 
			throw new RuntimeException("testFindAllByMatricola : FAILED, inserimento articolo non riuscito");
		
		int quantiArticoliInseriti2 = articoloDAOInstance.insert(new Articolo("articolo302", "matricola301", negozioAppenaInserito2));
		if (quantiArticoliInseriti2 < 1) 
			throw new RuntimeException("testFindAllByMatricola : FAILED, inserimento articolo non riuscito");
		
		// eseguo la procedura findAllByMatricola
		List<Articolo> listaPerMatricola = articoloDAOInstance.findAllByMatricola("matricola301");
		if (listaPerMatricola.size() != 10) 
			throw new RuntimeException("testFindAllByMatricola : FAILED, dimensione lista errata");
		
		for (Articolo articolo : listaPerMatricola) {
			System.out.println(articolo);
		}
		
		System.out.println(".............. testFindAllByMatricola terminato .................");
	}
	
	public static void testFindAllByIniziali(NegozioDAO negozioDAOInstance) {
		System.out.println("........... inizio test findAllByIniziali ...............");
		
		// inserisco nel DB alcuni negozi
		int quantiNegoziInseriti1 = negozioDAOInstance.insert(new Negozio("Hnegozio900", "via dell'aquila reale 1"));
		if (quantiNegoziInseriti1 < 1)
			throw new RuntimeException("non sono stati inseriti negozi");
		
		int quantiNegoziInseriti2 = negozioDAOInstance.insert(new Negozio("Hnegozio901", "via dell'aquila reale 3"));
		if (quantiNegoziInseriti2 < 1)
			throw new RuntimeException("non sono stati inseriti negozi");
		
		int quantiNegoziInseriti3 = negozioDAOInstance.insert(new Negozio("Hnegozio902", "via dell'aquila reale 5"));
		if (quantiNegoziInseriti3 < 1)
			throw new RuntimeException("non sono stati inseriti negozi");
		
		// adesso inserisco in una lista tutti i negozi che hanno iniziale H (devono essere 3)
		List<Negozio> listaNegoziH = negozioDAOInstance.findAllByIniziali("H");
		if (listaNegoziH.size() != 9)
			throw new RuntimeException("elementi lista attesi 3, reali " + listaNegoziH.size());
		
		for (Negozio negozio : listaNegoziH) {
			System.out.println(negozio);
		}

		System.out.println("........... fine test findAllByIniziali ................");
		
	}
	
	public static void testPopulateArticoli(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println("........... test populate iniziato ...............");
		
		// creo un negozio
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("negozio999", "via di torre maura 0"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("non è stato inserito nessun negozio");
			
		Negozio negozioAppenaInserito = negozioDAOInstance.list().get(negozioDAOInstance.list().size() - 1);
		
		// creo alcuni articoli
		int quantiArticoliInseriti1 = articoloDAOInstance.insert(new Articolo("maglietta9990", "matricola9990", negozioAppenaInserito));
		if (quantiArticoliInseriti1 < 1)
			throw new RuntimeException("non è stato inserito nessun articolo");
		
		int quantiArticoliInseriti2 = articoloDAOInstance.insert(new Articolo("maglietta9991", "matricola9991", negozioAppenaInserito));
		if (quantiArticoliInseriti2 < 1)
			throw new RuntimeException("non è stato inserito nessun articolo");
		
		int quantiArticoliInseriti3 = articoloDAOInstance.insert(new Articolo("maglietta9992", "matricola9992", negozioAppenaInserito));
		if (quantiArticoliInseriti3 < 1)
			throw new RuntimeException("non è stato inserito nessun articolo");
		
		// faccio la populate sul negozio appena inserito
		Negozio n = negozioDAOInstance.selectById(negozioAppenaInserito.getId());
		
		negozioDAOInstance.populateArticoli(n);
		
		for (Articolo articolo : n.getArticoli()) {
			System.out.println(articolo);
		}
		
		System.out.println("........... test populate terminato ...............");
			
	}

}
