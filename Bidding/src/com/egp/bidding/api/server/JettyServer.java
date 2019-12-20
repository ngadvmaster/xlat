package com.egp.bidding.api.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.egp.bidding.api.utils.Conts;
import com.egp.common.common.cp.BaseController;
import com.egp.common.common.cp.Param;
import com.egp.common.cp.NoCommandRegistered;

public class JettyServer {
	private static final Logger logger = Logger.getLogger(Conts.LOG_FILE_NAME);
	private static final Logger blackListIpLogger = Logger.getLogger("BlackListIpLogger");
	private static final String LOG_PROPERTIES_FILE = "config/log4j.properties";
	private static BaseController<HttpServletRequest, String> controller;
	private static String API_PORT = "8081";
	private static String SSL_PORT = "8443";

	private static void initializeLogger() {
		Properties logProperties = new Properties();
		try {
			File file = new File(LOG_PROPERTIES_FILE);
			logProperties.load(new FileInputStream(file));
			PropertyConfigurator.configure(logProperties);
			logger.info("Logging initialized.");
		} catch (IOException e) {
			throw new RuntimeException("Unable to load logging property config/log4j.properties");
		}
	}

	public static void main(String[] args) {
		try {
			initializeLogger();
			logger.debug("STARTING BIDDING API SERVER .... !!!!");
			loadCommands();
			// RMQApi.start("config/rmq.properties");
			// HazelcastLoader.start();
			// MongoDBConnectionFactory.init();
			int port = Integer.parseInt(API_PORT);
			int sslPort = Integer.parseInt(SSL_PORT);
			Server server = new Server();
			ServerConnector connector = new ServerConnector(server);
			connector.setPort(port);
			connector.setIdleTimeout(30000L);
			HttpConfiguration https = new HttpConfiguration();
			https.addCustomizer(new SecureRequestCustomizer());
			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath("config/apiegp.jks");
			sslContextFactory.setKeyStorePassword("123456aA@");
			sslContextFactory.setKeyManagerPassword("123456aA@");
			ServerConnector sslConnector = new ServerConnector(server, new ConnectionFactory[] { new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https) });
			sslConnector.setPort(sslPort);
			ServletHandler handler = new ServletHandler();
			handler.addFilterWithMapping(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
			handler.addServletWithMapping(JeetyServlet.class, "/api");
			HandlerCollection handlerCollection = new HandlerCollection();
			handlerCollection.setHandlers(new Handler[] { handler });
			server.setHandler(handlerCollection);
			server.setConnectors(new Connector[] { connector, sslConnector });
			server.start();
			logger.info("BIDDING API SERVER Started ...!!!");
			server.join();
		} catch (Exception e) {
			logger.info("BIDDING API SERVER Start error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void loadCommands() throws Exception {
		File file = new File("config/api_portal.xml");
		DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("portal");
		Element el = (Element) nodeList.item(0);
		API_PORT = el.getElementsByTagName("port").item(0).getTextContent();
		SSL_PORT = el.getElementsByTagName("ssl_port").item(0).getTextContent();
		Element cmds = (Element) el.getElementsByTagName("commands").item(0);
		NodeList cmdList = cmds.getElementsByTagName("command");
		Map<Integer, String> commandsMap = new HashMap<Integer, String>();
		for (int i = 0; i < cmdList.getLength(); i++) {
			Element eCmd = (Element) cmdList.item(i);
			Integer id = Integer.valueOf(Integer.parseInt(eCmd.getElementsByTagName("id").item(0).getTextContent()));
			String path = eCmd.getElementsByTagName("path").item(0).getTextContent();
			logger.debug(id + " <-> " + path);
			System.out.println(id + " <-> " + path);
			commandsMap.put(id, path);
		}

		controller = new BaseController<HttpServletRequest, String>();
		controller.initCommands(commandsMap);
	}

	public static class JeetyServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			onExecute(request, response);
		}

		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			onExecute(request, response);
		}

		private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200);
			Map<String, String[]> requestMap = request.getParameterMap();
			String remoteAddr = request.getRemoteAddr();
			JettyServer.logger.info("IP:" + remoteAddr);
			if (requestMap.containsKey("c")) {
				String command = request.getParameter("c");
				if ((command == null) || (command.equalsIgnoreCase(""))) {
					JettyServer.blackListIpLogger.debug(remoteAddr);
					return;
				}
				Param<HttpServletRequest> param = new Param<HttpServletRequest>();
				param.set(request);
				JettyServer.logger.debug("command: " + command);
				try {
					response.getWriter().println((String) JettyServer.controller.processCommand(Integer.valueOf(Integer.parseInt(command)), param));
					JettyServer.logger.debug(command);
				} catch (NoCommandRegistered e2) {
					JettyServer.logger.debug("COMMAND NOT FOUND");
					response.getWriter().println("COMMAND NOT FOUND");
					JettyServer.logger.debug("CMD_404");
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println(e1);
					response.getWriter().println("EXCEPTION: " + e1.getMessage());
				}
			} else {
				JettyServer.blackListIpLogger.debug(remoteAddr);
				response.getWriter().println("NO COMMANDS PARAMETERS");
				JettyServer.logger.debug("NO_CMD");
			}
		}
	}
}