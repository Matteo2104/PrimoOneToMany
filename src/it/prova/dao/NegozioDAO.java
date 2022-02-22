package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

@SuppressWarnings("unused")
public class NegozioDAO {

	public List<Negozio> list() {

		List<Negozio> result = new ArrayList<Negozio>();
		Negozio negozioTemp = null;

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("select * from negozio a ")) {

			while (rs.next()) {
				negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				result.add(negozioTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public Negozio selectById(Long idNegozioInput) {

		if (idNegozioInput == null || idNegozioInput < 1)
			return null;

		Negozio result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio i where i.id=?")) {

			ps.setLong(1, idNegozioInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Negozio();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setIndirizzo(rs.getString("indirizzo"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Negozio negozioInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("INSERT INTO negozio (nome, indirizzo) VALUES (?, ?)")) {

			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// TODO
	public int update(Negozio negozioInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c
						.prepareStatement("UPDATE negozio SET id=?, nome=?, indirizzo=? WHERE id = ?;");) {

			ps.setLong(1, negozioInput.getId());
			ps.setString(2, negozioInput.getNome());
			ps.setString(3, negozioInput.getIndirizzo());
			ps.setLong(4, negozioInput.getId());

			result = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println("Errore nell'esecuzione della query");
			throw new RuntimeException(e);
		}
		return result;
	}

	public int delete(Negozio negozioInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM negozio WHERE id = ?;")) {
			
			ps.setLong(1, negozioInput.getId());
			result = ps.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException("Errore di esecuzione della query");
		}
		return result;
	}

	// prende negozioInput e grazie al suo id va sulla tabella articoli e poi
	// ad ogni iterazione sul resultset aggiunge agli articoli di negozioInput
	public void populateArticoli(Negozio negozioInput) {
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"SELECT * FROM negozio n INNER JOIN articolo a ON n.id = a.negozio_id WHERE n.id = ?;");) {

			ps.setLong(1, negozioInput.getId());

			try (ResultSet rs = ps.executeQuery();) {
				List<Articolo> listaArticoliTemp = new ArrayList<>();
				Articolo articoloTemp;
				Negozio negozioTemp;
				while (rs.next()) {
					articoloTemp = new Articolo();
					negozioTemp = new Negozio();

					articoloTemp.setId(rs.getLong("a.id"));
					articoloTemp.setMatricola(rs.getString("a.matricola"));
					articoloTemp.setNome(rs.getString("a.nome"));

					
					//negozioTemp.setId();
					//negozioTemp.setIndirizzo();
					//negozioTemp.setNome();
					
					//articoloTemp.setNegozio(negozioTemp);
					
					listaArticoliTemp.add(articoloTemp);

				}

				negozioInput.setArticoli(listaArticoliTemp);
			}
		} catch (Exception e) {
			throw new RuntimeException("Errore nell'esecuzione della query");
		}
	}

	// implementare inoltre
	public List<Negozio> findAllByIniziali(String inizialeInput) {
		List<Negozio> resultList = new ArrayList<>();
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"SELECT * FROM negozio WHERE nome LIKE ?;");) {

			String temp = "";
			temp += inizialeInput;
			temp += "%";
			ps.setString(1, temp);

			try (ResultSet rs = ps.executeQuery()) {
				// Articolo articolo;
				Negozio negozio;

				while (rs.next()) {
					// articolo = new Articolo();
					negozio = new Negozio();

					negozio.setId(rs.getLong("id"));
					negozio.setIndirizzo(rs.getString("indirizzo"));
					negozio.setNome(rs.getString("nome"));

					resultList.add(negozio);
				}

			}

		} catch (Exception e) {
			throw new RuntimeException("Errore nell'esecuzione della query");
		}
		return resultList;
	}

}
