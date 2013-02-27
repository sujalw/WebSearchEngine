package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import edu.nyu.cs.cs2580.Output.action;

class QueryHandler implements HttpHandler {
	private static String plainResponse = "Request received, but I am not smart enough to echo yet!\n";

	private Ranker _ranker;

	public QueryHandler(Ranker ranker) {
		_ranker = ranker;
	}

	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}

	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (!requestMethod.equalsIgnoreCase("GET")) { // GET requests only.
			return;
		}

		// Print the user request header.
		Headers requestHeaders = exchange.getRequestHeaders();
		System.out.print("Incoming request: ");
		for (String key : requestHeaders.keySet()) {
			System.out.println(key + ":" + requestHeaders.get(key) + "; ");
		}
		System.out.println();
		String queryResponse = "";
		String uriQuery = exchange.getRequestURI().getQuery();
		String uriPath = exchange.getRequestURI().getPath();

		String sessionId = String.valueOf(System.currentTimeMillis());
		String outputFormat = "text";
		String host = requestHeaders.get("Host").get(0);

		if ((uriPath != null) && (uriQuery != null)) {
			if (uriPath.equals("/search")) {
				Map<String, String> query_map = getQueryMap(uriQuery);
				Set<String> keys = query_map.keySet();
				if (keys.contains("query")) {
					if (keys.contains("ranker")) {
						String ranker_type = query_map.get("ranker");
						outputFormat = query_map.get("format");

						Properties prop = new Properties();
						// load a properties file
						prop.load(this.getClass().getResourceAsStream(
								"config.properties"));
						String resultsDir = prop.getProperty("results_dir");

						// @CS2580: Invoke different ranking functions inside
						// your
						// implementation of the Ranker class.
						if (ranker_type.equals("cosine")) {
							CosineRanker cosineRanker = new CosineRanker(
									_ranker);
							Vector<ScoredDocument> sds = cosineRanker
									.runquery(query_map.get("query"));
							sds = Utilities.sortScoredDocumentAsPer(sds);

							Output output = new Output(sds, query_map);

							if (outputFormat.equals("text")) {
								// queryResponse = Utilities.generateOutput(sds,
								// query_map, sessionId);
								queryResponse = output
										.generateTextOutput(sessionId);

								String vsm_ranking_results = prop
										.getProperty("vsm_ranking_results");
								Utilities.writeToFile(resultsDir
										+ vsm_ranking_results, queryResponse,
										true);

							} else if (outputFormat.equals("html")) {
								queryResponse = output.generateHtmlOutput(
										sessionId, host);
							}

							// String vsm_ranking_results = prop
							// .getProperty("vsm_ranking_results");
							// Utilities.writeToFile(resultsDir
							// + vsm_ranking_results, queryResponse, true);

						} else if (ranker_type.equals("QL")) {
							// queryResponse = (ranker_type +
							// " not implemented.");
							QueryLikelihoodRankerwithJMSmoothing queryLikelihoodRankerwithJMSmoothing = new QueryLikelihoodRankerwithJMSmoothing(
									_ranker);
							QueryLikelihoodRankerwithJMSmoothing.setLambda(0.5);
							Vector<ScoredDocument> qlsd = queryLikelihoodRankerwithJMSmoothing
									.runquery(query_map.get("query"));

							Output output = new Output(qlsd, query_map);

							if (outputFormat.equals("text")) {
								// queryResponse = Utilities.generateOutput(sds,
								// query_map, sessionId);
								queryResponse = output
										.generateTextOutput(sessionId);

								String ql_ranking_results = prop
										.getProperty("ql_ranking_results");
								Utilities.writeToFile(resultsDir
										+ ql_ranking_results, queryResponse,
										true);

							} else if (outputFormat.equals("html")) {
								queryResponse = output.generateHtmlOutput(
										sessionId, host);
							}

							/*
							 * Iterator<ScoredDocument> iTer = qlsd.iterator();
							 * while (iTer.hasNext()) { ScoredDocument sd =
							 * iTer.next(); if (queryResponse.length() > 0) {
							 * queryResponse = queryResponse + "\n"; }
							 * queryResponse = queryResponse +
							 * query_map.get("query") + "\t" + sd.asString(); }
							 * if (queryResponse.length() > 0) { queryResponse =
							 * queryResponse + "\n"; }
							 * 
							 * // write the output to the desired file String
							 * ql_ranking_results = prop
							 * .getProperty("ql_ranking_results");
							 * Utilities.writeToFile(resultsDir +
							 * ql_ranking_results, queryResponse, true);
							 */

						} else if (ranker_type.equals("phrase")) {
							PhraseRanker phraseRanker = new PhraseRanker(
									_ranker);
							Vector<ScoredDocument> sds = phraseRanker.runquery(
									query_map.get("query"), 2); // bigram terms
							sds = Utilities.sortScoredDocumentAsPer(sds);

							Output output = new Output(sds, query_map);

							if (outputFormat.equals("text")) {
								queryResponse = output
										.generateTextOutput(sessionId);

								String phrase_ranking_results = prop
										.getProperty("phrase_ranking_results");
								Utilities.writeToFile(resultsDir
										+ phrase_ranking_results,
										queryResponse, true);

							} else if (outputFormat.equals("html")) {
								queryResponse = output.generateHtmlOutput(
										sessionId, host);
							}

						} else if (ranker_type.equals("linear")) {
							LinearRanker linRanker = new LinearRanker(_ranker);
							Vector<ScoredDocument> sds = linRanker.runquery(
									query_map.get("query"), 0.3f, 0.3f, 0.3f,
									0.1f);
							sds = Utilities.sortScoredDocumentAsPer(sds);

							Output output = new Output(sds, query_map);

							if (outputFormat.equals("text")) {
								queryResponse = output
										.generateTextOutput(sessionId);

								String linear_ranking_results = prop
										.getProperty("linear_ranking_results");
								Utilities.writeToFile(resultsDir
										+ linear_ranking_results,
										queryResponse, true);

							} else if (outputFormat.equals("html")) {
								queryResponse = output.generateHtmlOutput(
										sessionId, host);
							}

						} else if (ranker_type.equals("numviews")) {
							NumViewsRanker nvr = new NumViewsRanker(_ranker);
							String resPath = resultsDir
									+ prop.getProperty("numviews_ranking_results");
							Vector<ScoredDocument> sds = nvr
									.createNewViewsReverseSorted(resPath, true);
							Output output = new Output(sds, query_map);

							if (outputFormat.equals("text")) {
								queryResponse = output
										.generateTextOutput(sessionId);

								String numviews_ranking_results = prop
										.getProperty("numviews_ranking_results");
								Utilities.writeToFile(resultsDir
										+ numviews_ranking_results,
										queryResponse, true);

							} else if (outputFormat.equals("html")) {
								queryResponse = output.generateHtmlOutput(
										sessionId, host);
							}

							// prepend query to each line
							/*
							 * String query = query_map.get("query");
							 * queryResponse = query + "\t" + queryResponse;
							 * queryResponse = queryResponse.substring(0,
							 * queryResponse.lastIndexOf("\n")); queryResponse =
							 * queryResponse.replaceAll("\n", "\n"+query+"\t");
							 * 
							 * Output output = new Output();
							 * 
							 * if(outputFormat.equals("text")) {
							 * 
							 * } else if(outputFormat.equals("html")) {
							 * 
							 * }
							 * 
							 * Utilities.writeToFile(resPath, queryResponse,
							 * true);
							 */
						} else {
							queryResponse = (ranker_type + " not implemented.");
						}
					} else {
						// @CS2580: The following is instructor's simple ranker
						// that does not
						// use the Ranker class.
						Vector<ScoredDocument> sds = _ranker.runquery(query_map
								.get("query"));
						Iterator<ScoredDocument> itr = sds.iterator();
						while (itr.hasNext()) {
							ScoredDocument sd = itr.next();
							if (queryResponse.length() > 0) {
								queryResponse = queryResponse + "\n";
							}
							queryResponse = queryResponse
									+ query_map.get("query") + "\t"
									+ sd.asString();
						}
						if (queryResponse.length() > 0) {
							queryResponse = queryResponse + "\n";
						}
					}
				}
			} else if (uriPath.equals("/logging")) {
				Map<String, String> query_map = getQueryMap(uriQuery);
				Set<String> keys = query_map.keySet();

				if (keys.contains("did")) {
					int did = Integer.parseInt(query_map.get("did"));
					Output.logAction(sessionId, query_map.get("query"), did,
							action.CLICK, new Date());

					queryResponse = "Document title = "
							+ _ranker.getDoc(did).get_title_string()
							+ "<br><br>";
					queryResponse += "Document Body = <br>"
							+ _ranker.getDoc(did).get_body_string();

					outputFormat = "html";
				}
			}
		}

		// Construct a simple response.
		Headers responseHeaders = exchange.getResponseHeaders();
		if (outputFormat.equals("text")) {
			responseHeaders.set("Content-Type", "text/plain");
		} else if (outputFormat.equals("html")) {
			responseHeaders.set("Content-Type", "text/html");
		}

		exchange.sendResponseHeaders(200, 0); // arbitrary number of bytes
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(queryResponse.getBytes());
		responseBody.close();
	}
}