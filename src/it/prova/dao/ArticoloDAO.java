package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class ArticoloDAO {

	public List<Articolo> list() {

		List<Articolo> result = new ArrayList<Articolo>();

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				//STRATEGIA EAGER FETCHING
				ResultSet rs = s
						.executeQuery("select * from articolo a inner join negozio n on n.id=a.negozio_id")) {

			while (rs.next()) {
				Articolo articoloTemp = new Articolo();
				articoloTemp.setNome(rs.getString("NOME"));
				articoloTemp.setMatricola(rs.getString("matricola"));
				articoloTemp.setId(rs.getLong("a.id"));

				Negozio negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("n.id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				articoloTemp.setNegozio(negozioTemp);
				result.add(articoloTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public Articolo selectById(Long idArticoloInput) {

		if (idArticoloInput == null || idArticoloInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a where a.id=?")) {

			ps.setLong(1, idArticoloInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("NOME"));
					result.setMatricola(rs.getString("matricola"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Articolo articoloInput) {

		if (articoloInput.getNegozio() == null || articoloInput.getNegozio().getId() < 1)
			return -1;

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c
						.prepareStatement("INSERT INTO articolo (nome, matricola,negozio_id) VALUES (?, ?, ?)")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getNegozio().getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	// TODO

	public Articolo selectByIdWithJoin(Long idInput) {
		Articolo result = new Articolo();
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"SELECT * FROM articolo a INNER JOIN negozio n ON a.negozio_id = n.id WHERE a.id = ?");) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {

					result.setId(rs.getLong("id"));
					result.setMatricola(rs.getString("matricola"));
					result.setNome(rs.getString("nome"));
				}
			}

		} catch (Exception e) {

			System.out.println("Errore nell'esecuzione della query");
			throw new RuntimeException(e);

		}
		return result;
	}

	public int update(Articolo articoloInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c
						.prepareStatement("UPDATE articolo SET id=?, nome=?, matricola=?, negozio_id=? WHERE id = ?;");) {

			ps.setLong(1, articoloInput.getId());
			ps.setString(2, articoloInput.getNome());
			ps.setString(3, articoloInput.getMatricola());
			ps.setLong(4, articoloInput.getNegozio().getId());
			ps.setLong(5, articoloInput.getId());

			result = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println("Errore nell'esecuzione della query");
			throw new RuntimeException(e);
		}
		return result;
	}

	public int delete(Articolo articoloInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM articolo WHERE id = ?;")) {
			
			ps.setLong(1, articoloInput.getId());
			result = ps.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException("Errore di esecuzione della query");
		}
		return result;
	}

	// implementare inoltre
	public List<Articolo> findAllByNegozio(Negozio negozioInput) {
		if(negozioInput == null || negozioInput.getId() == null)
			throw new RuntimeException("Input non valido");
		
		List<Articolo> resultList = new ArrayList<>();
		Articolo temp;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM articolo a INNER JOIN negozio n ON a.negozio_id = n.id WHERE n.id = ?;");) {
			
			ps.setLong(1, negozioInput.getId());
			
			try (ResultSet rs = ps.executeQuery();) {
				
				while (rs.next()) {
					temp = new Articolo();
					Negozio tempNegozio = new Negozio();
					
					temp.setId(rs.getLong("a.id"));
					temp.setMatricola(rs.getString("a.matricola"));
					temp.setNome(rs.getString("a.nome"));
					
					tempNegozio.setId(rs.getLong("a.negozio_id"));
					tempNegozio.setIndirizzo(rs.getString("n.indirizzo"));
					tempNegozio.setNome(rs.getString("n.nome"));
					
					temp.setNegozio(tempNegozio);
					
					resultList.add(temp);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Errore nell'esecuzione della query");
			throw new RuntimeException(e);
		}
		return resultList;
	}

	public List<Articolo> findAllByMatricola(String matricolaInput) {
		List<Articolo> resultList = new ArrayList<>();
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM articolo a INNER JOIN negozio n ON a.negozio_id = n.id WHERE matricola = ?;");) {
			
			ps.setString(1, matricolaInput);
			
			try (ResultSet rs = ps.executeQuery();) {
				
				
				Articolo tempArticolo = new Articolo();
				Negozio tempNegozio = new Negozio();
				
				while (rs.next()) {
					tempArticolo = new Articolo();
					tempNegozio = new Negozio();
					
					tempArticolo.setId(rs.getLong("a.id"));
					tempArticolo.setMatricola(rs.getString("a.matricola"));
					tempArticolo.setNome(rs.getString("a.nome"));
					
					tempNegozio.setId(rs.getLong("a.negozio_id"));
					tempNegozio.setIndirizzo(rs.getString("n.indirizzo"));
					tempNegozio.setNome(rs.getString("n.nome"));
					
					tempArticolo.setNegozio(tempNegozio);
					
					resultList.add(tempArticolo);
				}
				
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Errore nell'esecuzione della query");
		}
		
		return resultList;
	}

	public List<Articolo> findAllByIndirizzoNegozio(String indirizzoNegozioInput) {
		Articolo tempArticolo;
		Negozio tempNegozio;
		List<Articolo> resultList = new ArrayList<>();
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"SELECT * FROM articolo a INNER JOIN negozio n ON a.negozio_id = n.id WHERE n.indirizzo = ?;");) {

			ps.setString(1, indirizzoNegozioInput);
			try (ResultSet rs = ps.executeQuery();) {

				while (rs.next()) {
					tempArticolo = new Articolo();
					tempNegozio = new Negozio();

					tempArticolo.setId(rs.getLong("a.id"));
					tempArticolo.setMatricola(rs.getString("a.matricola"));
					tempArticolo.setNome(rs.getString("a.nome"));

					tempNegozio.setId(rs.getLong("a.negozio_id"));
					tempNegozio.setIndirizzo(rs.getString("n.indirizzo"));
					tempNegozio.setNome(rs.getString("n.nome"));

					tempArticolo.setNegozio(tempNegozio);

					resultList.add(tempArticolo);
				}

			}
		} catch (Exception e) {
			System.out.println("Errore di esecuzione della query");
			throw new RuntimeException(e);
		}
		return resultList;
	}

}
