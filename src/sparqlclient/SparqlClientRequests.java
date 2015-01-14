package sparqlclient;

import java.util.ArrayList;
import java.util.Map;

public class SparqlClientRequests {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");

		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);
		if (serverIsUp) {
			System.out.println("server is UP");

			getSynonymes(sparqlClient, "a été récompensé pour");

			/*
			 * nbPersonnesParPiece(sparqlClient);
			 * 
			 * 
			 * System.out.println("ajout d'une personne dans le bureau:"); query
			 * = "PREFIX : <http://www.lamaisondumeurtre.fr#>\n" +
			 * "PREFIX instances: <http://www.lamaisondumeurtre.fr/instances#>\n"
			 * + "INSERT DATA\n" + "{\n" +
			 * "  instances:Bob :personneDansPiece instances:Bureau.\n" + "}\n";
			 * sparqlClient.update(query);
			 * 
			 * 
			 * nbPersonnesParPiece(sparqlClient);
			 * 
			 * System.out.println("suppression d'une personne du bureau:");
			 * query = "PREFIX : <http://www.lamaisondumeurtre.fr#>\n" +
			 * "PREFIX instances: <http://www.lamaisondumeurtre.fr/instances#>\n"
			 * + "DELETE DATA\n" + "{\n" +
			 * "  instances:Bob :personneDansPiece instances:Bureau.\n" + "}\n";
			 * sparqlClient.update(query);
			 * 
			 * nbPersonnesParPiece(sparqlClient);
			 */
		} else {
			System.out.println("service is DOWN");
		}
	}

	private static void nbPersonnesParPiece(SparqlClient sparqlClient) {
		String query = "PREFIX : <http://www.lamaisondumeurtre.fr#>\n"
				+ "SELECT ?piece (COUNT(?personne) AS ?nbPers) WHERE\n" + "{\n"
				+ "    ?personne :personneDansPiece ?piece.\n" + "}\n"
				+ "GROUP BY ?piece\n";
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		System.out.println("nombre de personnes par pièce:");
		for (Map<String, String> result : results) {
			System.out.println(result.get("piece") + " : "
					+ result.get("nbPers"));
		}
	}

	public static ArrayList<String> getSynonymes(SparqlClient sc, String mot) {
		ArrayList<String> synonymes = new ArrayList<String>();
		String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
				+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>"
				+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?lab1 ?lab2 "
				+ "WHERE { "
				+ "?subject rdfs:label ?lab1."
				+ "?subject rdfs:label ?lab2."
				+ "FILTER (?lab1 != ?lab2 && lang(?lab2) = \"fr\" && str(?lab1) = \""
				+ mot + "\")." + "}";
		// System.out.println(query);

		Iterable<Map<String, String>> results = sc.select(query);
		//System.out.println("synonymes:");
		for (Map<String, String> result : results) {
			synonymes.add(result.get("lab2"));
			//System.out.print(result.get("lab2") + " ");
			//System.out.println(result);
		}
		return synonymes;
	}
	
	public static ArrayList<String> getSubClasses(SparqlClient sc, String mot) {
		ArrayList<String> synonymes = new ArrayList<String>();
		String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
				+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>"
				+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?lab2 "
				+ "WHERE { "
				+ "?subject rdfs:subClassOf ?object. "
				+ "?object rdfs:label ?lab1. "
				+ "?subject rdfs:label ?lab2. "
				+ "FILTER (lang(?lab2) = \"fr\"  && str(?lab1) = \"" + mot + "\")."
				+ "}";

		// System.out.println(query);

		Iterable<Map<String, String>> results = sc.select(query);
		//System.out.println("sparql results :");
		for (Map<String, String> result : results) {
			synonymes.add(result.get("lab2"));
			//System.out.print(result.get("lab2") + " ");
			//System.out.println(result);
		}
		return synonymes;
		
	}
}
