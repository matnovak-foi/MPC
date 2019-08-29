package org.foi.nwtis.student7.bazaPodataka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BazaPodataka implements AutoCloseable
{
	private Connection conn = null;
	private Statement stmt = null;
	
	private boolean zatvoreno = false;
	
	
	public BazaPodataka(BazaPodatakaInfo info) throws ClassNotFoundException, SQLException
	{
		Class.forName(info.getDriver());
		conn = DriverManager.getConnection(info.getServer() + info.getBazaPodataka(), info.getKorisnik(), info.getLozinka());
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	
	public ResultSet select(String sql) throws SQLException
	{
		if (zatvoreno)
			throw new SQLException("Veza prema bazi podataka je zatvorena");
		
		return stmt.executeQuery(sql);
	}
	
	
	public int update(String sql) throws SQLException
	{
		if (zatvoreno)
			throw new SQLException("Veza prema bazi podataka je zatvorena");
		
		return stmt.executeUpdate(sql);
	}
	
	
	@Override
	public void close() throws SQLException
	{
		if (stmt != null) stmt.close();
		if (conn != null) conn.close();
		zatvoreno = true;
	}
	
}package org.foi.nwtis.student7.bazaPodataka;


public class BazaPodatakaInfo
{
	private String driver;
	private String server;
	private String bazaPodataka;
	private String korisnik;
	private String lozinka;
	
	
	public BazaPodatakaInfo() {}

	
	public BazaPodatakaInfo(String driver, String server, String bazaPodataka, String korisnik, String lozinka)
	{
		this.driver = driver;
		this.server = server;
		this.bazaPodataka = bazaPodataka;
		this.korisnik = korisnik;
		this.lozinka = lozinka;
	}

	
	public String getDriver()
	{
		return driver;
	}

	
	public String getServer()
	{
		return server;
	}
	
	
	public String getBazaPodataka()
	{
		return bazaPodataka;
	}

	
	public String getKorisnik()
	{
		return korisnik;
	}

	
	public String getLozinka()
	{
		return lozinka;
	}

	
	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	
	public void setServer(String server)
	{
		this.server = server;
	}
	
	
	public void setBazaPodataka(String bazaPodataka)
	{
		this.bazaPodataka = bazaPodataka;
	}

	
	public void setKorisnik(String korisnik)
	{
		this.korisnik = korisnik;
	}

	
	public void setLozinka(String lozinka)
	{
		this.lozinka = lozinka;
	}
}package org.foi.nwtis.student7.dretve;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student7.web.podaci.Adresa;
import org.foi.nwtis.student7.web.podaci.Lokacija;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;


public class PreuzmiMeteoPodatke implements Runnable
{
	private final ProsirenaKonfiguracija konf;
	private final BazaPodataka bazaPodataka;
	private final int interval;
	
	private boolean raditi = true;
	
	private final ArrayList<Adresa> adrese = new ArrayList<>();
	private final HashMap<Adresa, MeteoPodaci> meteoPodaciPoAdresi = new HashMap<>();
	
	
	public PreuzmiMeteoPodatke(ProsirenaKonfiguracija konf) throws ClassNotFoundException, SQLException
	{
		this.konf = konf;
		this.interval = Integer.parseInt(konf.getDownloadInterval()) * 1000;
		
		BazaPodatakaInfo info = new BazaPodatakaInfo();
		info.setDriver(konf.getDriverDatabase());
		info.setServer(konf.getServerDatabase());
		info.setBazaPodataka(konf.getUserDatabase());
		info.setKorisnik(konf.getUserUsername());
		info.setLozinka(konf.getUserPassword());
		
		bazaPodataka = new BazaPodataka(info);
	}
	
	
	@Override
	public void run()
	{
		while (raditi) {
			try {
				Thread.sleep(interval);
				
				meteoPodaciPoAdresi.clear();
				adrese.clear();
				
				dohvatiAdreseIzBazePodataka();
				dohvatiMeteoPodatkePoAdresama();
				pohraniMeteoPodatke();
				
				System.out.println("Unjeti su meteorološki podaci.");
			}
			catch (InterruptedException ex) {
				raditi = false;
				System.out.println("PreuzmiMeteoPodatke završava s radom.");
			}
			catch (SQLException ex) {
				System.out.println("Greška u dodavanju meteoroloških podataka u bazu podataka: " + ex.getMessage());
			}
		}
		
		try {
			bazaPodataka.close();
		}
		catch (SQLException ex) {
			System.out.println("Baza podataka se ne da zatvoriti: " + ex.getMessage());
		}
	}

	
	private void dohvatiAdreseIzBazePodataka()
	{
		try {
			String sql = "SELECT IDADRESA, ADRESA, LATITUDE, LONGITUDE FROM ADRESE";
			ResultSet rezultat = bazaPodataka.select(sql);
			while (rezultat.next()) {
				long id = rezultat.getLong(1);
				String adresa = rezultat.getString(2);
				String latitude = rezultat.getString(3);
				String longitude = rezultat.getString(4);
				
				Lokacija lokacija = new Lokacija(latitude, longitude);
				adrese.add(new Adresa(id, adresa, lokacija));
			}
		}
		catch (SQLException ex) {
			System.out.println("Greška u dohvaćanju adresa iz baze podataka: " + ex.getMessage());
		}
	}

	
	private void dohvatiMeteoPodatkePoAdresama()
	{
		OWMKlijent owm = new OWMKlijent(konf.getApiKey());
		for (Adresa adresa : adrese) {
			MeteoPodaci meteo = owm.getRealTimeWeather(adresa.getGeoloc().getLatitude(), adresa.getGeoloc().getLongitude());
			meteoPodaciPoAdresi.put(adresa, meteo);
		}
	}

	
	private void pohraniMeteoPodatke() throws SQLException
	{
		String sqlBase = "INSERT INTO METEO VALUES "
			+ "(DEFAULT, %d, '%s', '%s', '%s', '%s', '%s', %f, %f, %f, %f, %f, %f, %f, DEFAULT)";
		
		Set<Adresa> setAdresa = meteoPodaciPoAdresi.keySet();
		Iterator<Adresa> iterAdresa = setAdresa.iterator();
		while (iterAdresa.hasNext()) {
			Adresa adresa = iterAdresa.next();
			MeteoPodaci meteo = meteoPodaciPoAdresi.get(adresa);
			String sql = String.format(Locale.US, sqlBase, 
				adresa.getIdadresa(),
				adresa.getAdresa(),
				adresa.getGeoloc().getLatitude(), 
				adresa.getGeoloc().getLongitude(),
				meteo.getWeatherValue(), 
				meteo.getWeatherValue(),
				meteo.getTemperatureValue(),
				meteo.getTemperatureMin(), 
				meteo.getTemperatureMax(), 
				meteo.getHumidityValue(),
				meteo.getPressureValue(), 
				meteo.getWindSpeedValue(), 
				meteo.getWindDirectionValue()
			);
			System.out.println(sql);
			bazaPodataka.update(sql);
		}
	}
}package org.foi.nwtis.student7.konfiguracija;

import org.foi.nwtis.student7.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student7.konfiguracije.bp.BP_Konfiguracija;


public class ProsirenaKonfiguracija extends BP_Konfiguracija implements ProsirenaKonfiguracijaSucelje
{
	
	public ProsirenaKonfiguracija(String datoteka) throws NemaKonfiguracije
	{
		super(datoteka);
	}

	@Override
	public String getApiKey()
	{
		return konf.dajPostavku("api.key");
	}

	@Override
	public String getDownloadInterval()
	{
		return konf.dajPostavku("download.interval");
	}
}package org.foi.nwtis.student7.konfiguracija;


public interface ProsirenaKonfiguracijaSucelje
{
	
	public String getApiKey();
	
	
	public String getDownloadInterval();
}package org.foi.nwtis.student7.rest.klijenti;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student7.web.podaci.Lokacija;


public class GMKlijent {

    GMRESTHelper helper;
    Client client;

	
    public GMKlijent() {
        client = ClientBuilder.newClient();
    }

	
    public Lokacija getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                    .path("maps/api/geocode/json");
            webResource = webResource.queryParam("address",
                    URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();
                     
            JsonObject obj = jo.getJsonArray("results")
                    .getJsonObject(0)
                    .getJsonObject("geometry")
                    .getJsonObject("location");

            Lokacija loc = new Lokacija(obj.getJsonNumber("lat").toString(), obj.getJsonNumber("lng").toString());

            return loc;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}package org.foi.nwtis.student7.rest.klijenti;


public class GMRESTHelper {
    private static final String GM_BASE_URI = "http://maps.google.com/";    

	
    public GMRESTHelper() {
    }

	
    public static String getGM_BASE_URI() {
        return GM_BASE_URI;
    }
        
}package org.foi.nwtis.student7.rest.klijenti;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;


public class OWMKlijent 
{
    String apiKey;
    OWMRESTHelper helper;
    Client client;

	
    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

	
    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);
        
        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();

            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue()*1000));
            mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue()*1000));
            
            mp.setTemperatureValue(new Double(jo.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
            mp.setTemperatureMin(new Double(jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
            mp.setTemperatureMax(new Double(jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
            mp.setTemperatureUnit("celsius");
            
            mp.setHumidityValue(new Double(jo.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
            mp.setHumidityUnit("%");
            
            mp.setPressureValue(new Double(jo.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
            mp.setPressureUnit("hPa");
            
            mp.setWindSpeedValue(new Double(jo.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
            mp.setWindSpeedName("");
            
            mp.setWindDirectionValue(new Double(jo.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");
            
            mp.setCloudsValue(jo.getJsonObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setPrecipitationMode("");
            
            mp.setWeatherNumber(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));
            
            mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue()*1000));
            return mp;
            
        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
	
	
	public List<MeteoPodaci> getWeatherForecast(String latitude, String longitude)
	{
		WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);
		
		String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
		try {
			List<MeteoPodaci> prognoza = new ArrayList<>();
			JsonObject jo = Json.createReader(new StringReader(odgovor)).readObject();
			
			JsonArray ja = jo.getJsonArray("list");
			for (int i = 0; i < ja.size(); i++) {
				MeteoPodaci mp = new MeteoPodaci();
				
				mp.setTemperatureValue(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
				mp.setTemperatureMin(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
				mp.setTemperatureMax(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
				mp.setPressureValue(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
				mp.setHumidityValue(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
				mp.setWeatherValue(ja.getJsonObject(i).getJsonArray("weather").getJsonObject(0).getString("description"));
				mp.setWindSpeedValue(new Double(ja.getJsonObject(i).getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
				mp.setWindDirectionValue(new Double(ja.getJsonObject(i).getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
				mp.setLastUpdate(new Date(ja.getJsonObject(i).getJsonNumber("dt").longValue() * 1000L));
				
				prognoza.add(mp);
			}
			
			return prognoza;
		} catch (Exception ex) {
			Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}package org.foi.nwtis.student7.rest.klijenti;


public class OWMRESTHelper {
    private static final String OWM_BASE_URI = "http://api.openweathermap.org/data/2.5/";    
    private String apiKey;

	
    public OWMRESTHelper(String apiKey) {
        this.apiKey = apiKey;
    }

	
    public static String getOWM_BASE_URI() {
        return OWM_BASE_URI;
    }

	
    public static String getOWM_Current_Path() {
        return "weather";
    }
        
	
    public static String getOWM_Forecast_Path() {
        return "forecast";
    }
        
	
    public static String getOWM_ForecastDaily_Path() {
        return "forecast/daily";
    }
        
	
    public static String getOWM_StationsNear_Path() {
        return "station/find";
    }
        
	
    public static String getOWM_Station_Path() {
        return "station";
    }
}package org.foi.nwtis.student7.rest.serveri;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.web.slusaci.SlusacAplikacije;


@Path("adreseREST")
public class AdreseRESTResource
{
	private final ProsirenaKonfiguracija konf;

	
	public AdreseRESTResource()
	{
		konf = (ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String dajSveAdrese()
	{
		JsonArrayBuilder adreseJAB = Json.createArrayBuilder();
		boolean greska = false;

		try (BazaPodataka bp = new BazaPodataka(getInfo())) {
			ResultSet rezultat = bp.select("SELECT * FROM ADRESE");
			while (rezultat.next()) {
				long id = rezultat.getLong(1);
				String adresa = rezultat.getString(2);
				String latitude = rezultat.getString(3);
				String longitude = rezultat.getString(4);

				JsonObjectBuilder adresaJOB = Json.createObjectBuilder();
				adresaJOB.add("id", id);
				adresaJOB.add("adresa", adresa);
				adresaJOB.add("latitude", latitude);
				adresaJOB.add("longitude", longitude);

				adreseJAB.add(adresaJOB);
			}
			rezultat.close();
		}
		catch (SQLException ex) {
			System.out.println("Greška s bazom podataka u REST web servisu dajSveAdrese: " + ex.getMessage());
			greska = true;
		}
		catch (ClassNotFoundException ex) {
			System.out.println("Greška s JDBC driver-om u REST web servisu dajSveAdrese: " + ex.getMessage());
			greska = true;
		}

		JsonObjectBuilder glavniJOB = Json.createObjectBuilder();
		if (!greska) {
			glavniJOB.add("adrese", adreseJAB);
			glavniJOB.add("status", 0);
		}
		else {
			glavniJOB.add("status", 1);
		}

		return glavniJOB.build().toString();
	}
	
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String dajVazeceMeteoPodatke(@PathParam("id") long id)
	{
		JsonObjectBuilder meteoJOB = Json.createObjectBuilder();

		try (BazaPodataka bp = new BazaPodataka(getInfo())) {
			ResultSet rezultatAdresa = bp.select("SELECT * FROM ADRESE WHERE IDADRESA=" + id);
			if (rezultatAdresa.next()) {
				String latitude = rezultatAdresa.getString(3);
				String longitude = rezultatAdresa.getString(4);
				
				String sql = String.format("SELECT * FROM METEO WHERE LATITUDE='%s' AND LONGITUDE='%s'", latitude, longitude);				
				ResultSet rezultatMeteo = bp.select(sql);
				if (rezultatMeteo.last()) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
					Date preuzeto = new Date(rezultatMeteo.getTimestamp("PREUZETO").getTime());
					
					meteoJOB.add("vrijeme", rezultatMeteo.getString("VRIJEME"));
					meteoJOB.add("temp", rezultatMeteo.getString("TEMP"));
					meteoJOB.add("tempMin", rezultatMeteo.getString("TEMPMIN"));
					meteoJOB.add("tempMax", rezultatMeteo.getString("TEMPMAX"));
					meteoJOB.add("vlaga", rezultatMeteo.getString("VLAGA"));
					meteoJOB.add("tlak", rezultatMeteo.getString("TLAK"));
					meteoJOB.add("vjetar", rezultatMeteo.getString("VJETAR"));
					meteoJOB.add("vjetarSmjer", rezultatMeteo.getString("VJETARSMJER"));
					meteoJOB.add("preuzeto", sdf.format(preuzeto));
					meteoJOB.add("status", 0);
				}
				else {
					meteoJOB.add("status", 1);
				}
				rezultatMeteo.close();
			}
			else {
				meteoJOB.add("status", 1);
			}
			rezultatAdresa.close();
		}
		catch (SQLException ex) {
			System.out.println("Greška s bazom podataka u REST web servisu dajAdresu: " + ex.getMessage());
			meteoJOB.add("status", 1);
		}
		catch (ClassNotFoundException ex) {
			System.out.println("Greška s JDBC driver-om u REST web servisu dajAdresu: " + ex.getMessage());
			meteoJOB.add("status", 1);
		}
		
		return meteoJOB.build().toString();
	}

	
	private BazaPodatakaInfo getInfo()
	{
		BazaPodatakaInfo info = new BazaPodatakaInfo();
		info.setDriver(konf.getDriverDatabase());
		info.setServer(konf.getServerDatabase());
		info.setBazaPodataka(konf.getUserDatabase());
		info.setKorisnik(konf.getUserUsername());
		info.setLozinka(konf.getUserPassword());

		return info;
	}
}package org.foi.nwtis.student7.web;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.rest.klijenti.GMKlijent;
import org.foi.nwtis.student7.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student7.web.podaci.Adresa;
import org.foi.nwtis.student7.web.podaci.Lokacija;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;
import org.foi.nwtis.student7.web.slusaci.SlusacAplikacije;


public class DodajAdresu extends HttpServlet
{
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		
		Enumeration<String> enumParametri = request.getParameterNames();
		if (!enumParametri.hasMoreElements())
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		String[] imenaParametara = new String[2];
		for (int i = 0; i < imenaParametara.length && enumParametri.hasMoreElements(); i++) {
			imenaParametara[i] = enumParametri.nextElement();
		}
		
		switch (imenaParametara[1]) {
			case "dohvatiGeo": {
				String adresaString = request.getParameter(imenaParametara[0]);
				HttpSession sesija = request.getSession();

				try {
					GMKlijent gmk = new GMKlijent();
					Lokacija lokacija = gmk.getGeoLocation(adresaString);
					Adresa adresa = new Adresa(1, adresaString, lokacija);

					sesija.setAttribute("dohvacenaAdresa", adresa);
					request.setAttribute("akcija", "dohvatiGeo");
					System.out.println("Dohvaćaju se geo podaci za: " + adresaString);
				}
				catch (Exception ex) {
					System.out.println("Ne mogu se naći podaci za adresu: " + adresaString);
				}
			} break;
			
			case "vidiGeo": {
				HttpSession sesija = request.getSession();
				Adresa dohvacenaAdresa = (Adresa) sesija.getAttribute("dohvacenaAdresa");
				
				if (dohvacenaAdresa != null)
					request.setAttribute("akcija", "vidiGeo");
			} break;
			
			case "spremiAdresu": {
				ProsirenaKonfiguracija konf = 
					(ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");
				
				BazaPodatakaInfo info = new BazaPodatakaInfo();
				info.setDriver(konf.getDriverDatabase());
				info.setServer(konf.getServerDatabase());
				info.setBazaPodataka(konf.getUserDatabase());
				info.setKorisnik(konf.getUserUsername());
				info.setLozinka(konf.getUserPassword());
				
				HttpSession sesija = request.getSession();
				Adresa dohvacenaAdresa = (Adresa) sesija.getAttribute("dohvacenaAdresa");
				
				if (dohvacenaAdresa != null) {
					try (BazaPodataka bp = new BazaPodataka(info)) {
						if (!adresaPostoji(bp, dohvacenaAdresa)) {
							String sqlInsert = String.format(
								"INSERT INTO ADRESE(ADRESA, LATITUDE, LONGITUDE) VALUES ('%s', '%s', '%s')",
								dohvacenaAdresa.getAdresa(),
								dohvacenaAdresa.getGeoloc().getLatitude(),
								dohvacenaAdresa.getGeoloc().getLongitude()
							);

							bp.update(sqlInsert);

							request.setAttribute("akcija", "spremiAdresuNEW");
							System.out.println("Preuzete adrese su spremljene u bazu podataka.");
						}
						else {
							request.setAttribute("akcija", "spremiAdresuEXISTS");
							System.out.println("Pokušala se dodati već postojeća adresa.");
						}
					}
					catch (SQLException | ClassNotFoundException ex) {
						System.out.println("Dogodila se s bazom podataka: " + ex.getMessage());
					}
				}
			} break;
			
			case "dohvatiMeteo": {
				HttpSession sesija = request.getSession();
				Adresa dohvacenaAdresa = (Adresa) sesija.getAttribute("dohvacenaAdresa");
				
				if (dohvacenaAdresa != null) {
					ProsirenaKonfiguracija konf
						= (ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");

					OWMKlijent owm = new OWMKlijent(konf.getApiKey());
					MeteoPodaci meteoPodaci 
						= owm.getRealTimeWeather(dohvacenaAdresa.getGeoloc().getLatitude(), dohvacenaAdresa.getGeoloc().getLongitude());
					
					request.setAttribute("akcija", "dohvatiMeteo");
					sesija.setAttribute("meteo", meteoPodaci);
					System.out.println("Dohvaćeni su meta podaci.");
				}
			} break;
		}
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	
	private boolean adresaPostoji(BazaPodataka bp, Adresa adresa) throws SQLException
	{
		boolean postoji = false;
		String sqlSelect = String.format(
			"SELECT * FROM ADRESE WHERE LATITUDE='%s' AND LONGITUDE='%s'",
			adresa.getGeoloc().getLatitude(),
			adresa.getGeoloc().getLongitude()
		);

		ResultSet rezultat = bp.select(sqlSelect);
		while (rezultat.next() && !postoji) {
			String latitude = rezultat.getString("LATITUDE");
			String longitude = rezultat.getString("LONGITUDE");
			boolean uvjet1 = latitude.equals(adresa.getGeoloc().getLatitude());
			boolean uvjet2 = longitude.equals(adresa.getGeoloc().getLongitude());
			if (uvjet1 && uvjet2)
				postoji = true;
		}		
		
		return postoji;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	@Override
	public String getServletInfo()
	{
		return "Short description";
	}

}package org.foi.nwtis.student7.web.podaci;


public class Adresa {
    private long idadresa;
    private String adresa;
    private Lokacija geoloc;

	
    public Adresa() {
    }

	
    public Adresa(long idadresa, String adresa, Lokacija geoloc) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

	
    public Lokacija getGeoloc() {
        return geoloc;
    }

	
    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

	
    public long getIdadresa() {
        return idadresa;
    }

	
    public void setIdadresa(long idadresa) {
        this.idadresa = idadresa;
    }

	
    public String getAdresa() {
        return adresa;
    }

	
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
}package org.foi.nwtis.student7.web.podaci;


public class Lokacija {

    private String latitude;
    private String longitude;

	
    public Lokacija() {
    }

	
    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
	
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

	
    public String getLatitude() {
        return latitude;
    }

	
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

	
    public String getLongitude() {
        return longitude;
    }

	
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
}package org.foi.nwtis.student7.web.podaci;

import java.util.Date;


public class MeteoPodaci {

    private Date sunRise;
    private Date sunSet;

    private Float temperatureValue;
    private Float temperatureMin;
    private Float temperatureMax;
    private String temperatureUnit;

    private Float humidityValue;
    private String humidityUnit;

    private Float pressureValue;
    private String pressureUnit;

    private Float windSpeedValue;
    private String windSpeedName;
    private Float windDirectionValue;
    private String windDirectionCode;
    private String windDirectionName;

    private int cloudsValue;
    private String cloudsName;

    private String visibility;

    private Float precipitationValue;
    private String precipitationMode;
    private String precipitationUnit;

    private int weatherNumber;
    private String weatherValue;
    private String weatherIcon;
    private Date lastUpdate;

	
    public MeteoPodaci() {
    }

	
    public MeteoPodaci(Date sunRise, Date sunSet, Float temperatureValue, Float temperatureMin, Float temperatureMax, String temperatureUnit, Float humidityValue, String humidityUnit, Float pressureValue, String pressureUnit, Float windSpeedValue, String windSpeedName, Float windDirectionValue, String windDirectionCode, String windDirectionName, int cloudsValue, String cloudsName, String visibility, Float precipitationValue, String precipitationMode, String precipitationUnit, int weatherNumber, String weatherValue, String weatherIcon, Date lastUpdate) {
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.temperatureValue = temperatureValue;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.temperatureUnit = temperatureUnit;
        this.humidityValue = humidityValue;
        this.humidityUnit = humidityUnit;
        this.pressureValue = pressureValue;
        this.pressureUnit = pressureUnit;
        this.windSpeedValue = windSpeedValue;
        this.windSpeedName = windSpeedName;
        this.windDirectionValue = windDirectionValue;
        this.windDirectionCode = windDirectionCode;
        this.windDirectionName = windDirectionName;
        this.cloudsValue = cloudsValue;
        this.cloudsName = cloudsName;
        this.visibility = visibility;
        this.precipitationValue = precipitationValue;
        this.precipitationMode = precipitationMode;
        this.precipitationUnit = precipitationUnit;
        this.weatherNumber = weatherNumber;
        this.weatherValue = weatherValue;
        this.weatherIcon = weatherIcon;
        this.lastUpdate = lastUpdate;
    }

	
    public Date getSunRise() {
        return sunRise;
    }

	
    public void setSunRise(Date sunRise) {
        this.sunRise = sunRise;
    }

	
    public Date getSunSet() {
        return sunSet;
    }

	
    public void setSunSet(Date sunSet) {
        this.sunSet = sunSet;
    }

	
    public Float getTemperatureValue() {
        return temperatureValue;
    }

	
    public void setTemperatureValue(Float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

	
    public Float getTemperatureMin() {
        return temperatureMin;
    }

	
    public void setTemperatureMin(Float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

	
    public Float getTemperatureMax() {
        return temperatureMax;
    }

	
    public void setTemperatureMax(Float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

	
    public String getTemperatureUnit() {
        return temperatureUnit;
    }

	
    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

	
    public Float getHumidityValue() {
        return humidityValue;
    }

	
    public void setHumidityValue(Float humidityValue) {
        this.humidityValue = humidityValue;
    }

	
    public String getHumidityUnit() {
        return humidityUnit;
    }

	
    public void setHumidityUnit(String humidityUnit) {
        this.humidityUnit = humidityUnit;
    }

	
    public Float getPressureValue() {
        return pressureValue;
    }

	
    public void setPressureValue(Float pressureValue) {
        this.pressureValue = pressureValue;
    }

	
    public String getPressureUnit() {
        return pressureUnit;
    }

	
    public void setPressureUnit(String pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

	
    public Float getWindSpeedValue() {
        return windSpeedValue;
    }

	
    public void setWindSpeedValue(Float windSpeedValue) {
        this.windSpeedValue = windSpeedValue;
    }

	
    public String getWindSpeedName() {
        return windSpeedName;
    }

	
    public void setWindSpeedName(String windSpeedName) {
        this.windSpeedName = windSpeedName;
    }

	
    public Float getWindDirectionValue() {
        return windDirectionValue;
    }

	
    public void setWindDirectionValue(Float windDirectionValue) {
        this.windDirectionValue = windDirectionValue;
    }

	
    public String getWindDirectionCode() {
        return windDirectionCode;
    }

	
    public void setWindDirectionCode(String windDirectionCode) {
        this.windDirectionCode = windDirectionCode;
    }

	
    public String getWindDirectionName() {
        return windDirectionName;
    }

	
    public void setWindDirectionName(String windDirectionName) {
        this.windDirectionName = windDirectionName;
    }

	
    public int getCloudsValue() {
        return cloudsValue;
    }

	
    public void setCloudsValue(int cloudsValue) {
        this.cloudsValue = cloudsValue;
    }

	
    public String getCloudsName() {
        return cloudsName;
    }

	
    public void setCloudsName(String cloudsName) {
        this.cloudsName = cloudsName;
    }

	
    public String getVisibility() {
        return visibility;
    }

	
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

	
    public Float getPrecipitationValue() {
        return precipitationValue;
    }

	
    public void setPrecipitationValue(Float precipitationValue) {
        this.precipitationValue = precipitationValue;
    }

	
    public String getPrecipitationMode() {
        return precipitationMode;
    }

	
    public void setPrecipitationMode(String precipitationMode) {
        this.precipitationMode = precipitationMode;
    }

	
    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

	
    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

	
    public int getWeatherNumber() {
        return weatherNumber;
    }

	
    public void setWeatherNumber(int weatherNumber) {
        this.weatherNumber = weatherNumber;
    }

	
    public String getWeatherValue() {
        return weatherValue;
    }

	
    public void setWeatherValue(String weatherValue) {
        this.weatherValue = weatherValue;
    }

	
    public String getWeatherIcon() {
        return weatherIcon;
    }

	
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

	
    public Date getLastUpdate() {
        return lastUpdate;
    }

	
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}package org.foi.nwtis.student7.web.slusaci;

import java.io.File;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.student7.dretve.PreuzmiMeteoPodatke;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.konfiguracije.NemaKonfiguracije;


@WebListener
public class SlusacAplikacije implements ServletContextListener
{
	private static ServletContext servletContext; 
	private Thread dretvaPreuzimanja;
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		servletContext = sce.getServletContext();
		
		try {
			postaviAtributKonfiguracije();
			postaviDretvuZaPreuzimanjeMeteoPodataka();
		}
		catch (NemaKonfiguracije ex) {
			System.out.println("Greška prilikom postavljanje atributa konfiguracije: " + ex.getMessage());
			System.out.println("Dretva je prekinuta. Ispravite grešku i ponovno isporučite aplikaciju");
		}
		catch (ClassNotFoundException | SQLException ex) {
			System.out.println("Greška kod povezivanja s bazom podataka: " + ex.getMessage());
		}
	}

	
	@Override
	public void contextDestroyed(ServletContextEvent sce) 
	{
		if (dretvaPreuzimanja != null && dretvaPreuzimanja.isAlive())
			dretvaPreuzimanja.interrupt();
	}

	
	public static ServletContext getServletContext()
	{
		return servletContext;
	}
	
	
	private void postaviAtributKonfiguracije() throws NemaKonfiguracije
	{
		String datoteka = servletContext.getInitParameter("konfiguracija");
		String putanja = servletContext.getRealPath("/WEB-INF") + File.separator;
		
		ProsirenaKonfiguracija konf = new ProsirenaKonfiguracija(putanja + datoteka);
		servletContext.setAttribute("konfiguracija", konf);
	}

	
	private void postaviDretvuZaPreuzimanjeMeteoPodataka() throws ClassNotFoundException, SQLException
	{
		ProsirenaKonfiguracija konf = (ProsirenaKonfiguracija) servletContext.getAttribute("konfiguracija");
		PreuzmiMeteoPodatke pmp = new PreuzmiMeteoPodatke(konf);
		dretvaPreuzimanja = new Thread(pmp, "PreuzmiMeteoPodatke");
		dretvaPreuzimanja.start();
	}
}package org.foi.nwtis.student7.ws.serveri;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;
import org.foi.nwtis.student7.web.slusaci.SlusacAplikacije;


@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS
{
	private ProsirenaKonfiguracija konf;
	
	
	@WebMethod(operationName = "dajSveMeteoPodatke")
	public List<MeteoPodaci> dajSveMeteoPodatke(@WebParam(name = "address") String address)
	{
		List<MeteoPodaci> preuzetiMeteoPodaci = new ArrayList<>();
		
		if (address != null && address.length() > 0) {
			try (BazaPodataka bp = new BazaPodataka(getInfo())) {
				String sql1 = String.format("SELECT * FROM ADRESE WHERE ADRESA='%s'", address);
				
				ResultSet rezultatAdresa = bp.select(sql1);
				if (rezultatAdresa.next()) {
					String latitude = rezultatAdresa.getString(3);
					String longitude = rezultatAdresa.getString(4);
					
					String sql2 = String.format(
						"SELECT * FROM METEO WHERE LATITUDE='%s' AND LONGITUDE='%s'",
						latitude,
						longitude
					);
					
					ResultSet rezultatMeteo = bp.select(sql2);
					while (rezultatMeteo.next()) {
						MeteoPodaci meteo = new MeteoPodaci();
						meteo.setWeatherValue(rezultatMeteo.getString("VRIJEME"));
						meteo.setTemperatureValue(rezultatMeteo.getFloat("TEMP"));
						meteo.setTemperatureMin(rezultatMeteo.getFloat("TEMPMIN"));
						meteo.setTemperatureMax(rezultatMeteo.getFloat("TEMPMAX"));
						meteo.setHumidityValue(rezultatMeteo.getFloat("VLAGA"));
						meteo.setPressureValue(rezultatMeteo.getFloat("TLAK"));
						meteo.setWindSpeedValue(rezultatMeteo.getFloat("VJETAR"));
						meteo.setWindDirectionValue(rezultatMeteo.getFloat("VJETARSMJER"));
						meteo.setLastUpdate(new Date(rezultatMeteo.getTimestamp("PREUZETO").getTime()));
						
						preuzetiMeteoPodaci.add(meteo);
					}
				}
			}
			catch (SQLException ex) {
				System.out.println("Greška s bazom podataka u SOAP web servisu dajMeteoPodatke: " + ex.getMessage());
			}
			catch (ClassNotFoundException ex) {
				System.out.println("Greška s JDBC driver-om u SOAP web servisu dajMeteoPodatke: " + ex.getMessage());
			}
		}		
		
		return preuzetiMeteoPodaci;
	}
	
	
	@WebMethod(operationName = "dajPrognozu")
	public List<MeteoPodaci> dajPrognozu(@WebParam(name = "address") String address)
	{
		List<MeteoPodaci> prognoza = new ArrayList<>();
		
		if (address != null && address.length() > 0) {
			try (BazaPodataka bp = new BazaPodataka(getInfo())) {
				OWMKlijent owm = new OWMKlijent(konf.getApiKey());
				String sql = String.format("SELECT * FROM ADRESE WHERE ADRESA='%s'", address);
				
				ResultSet rezultatAdresa = bp.select(sql);
				if (rezultatAdresa.next()) {
					String latitude = rezultatAdresa.getString(3);
					String longitude = rezultatAdresa.getString(4);
					prognoza = owm.getWeatherForecast(latitude, longitude);
				}
			}
			catch (SQLException ex) {
				System.out.println("Greška s bazom podataka u SOAP web servisu dajPrognozu: " + ex.getMessage());
			}
			catch (ClassNotFoundException ex) {
				System.out.println("Greška s JDBC driver-om u SOAP web servisu dajPrognozu: " + ex.getMessage());
			}
		}		
		
		return prognoza;
	}
		
	
	private BazaPodatakaInfo getInfo()
	{
		if (konf == null)
			konf = (ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");
		
		BazaPodatakaInfo info = new BazaPodatakaInfo();
		info.setDriver(konf.getDriverDatabase());
		info.setServer(konf.getServerDatabase());
		info.setBazaPodataka(konf.getUserDatabase());
		info.setKorisnik(konf.getUserUsername());
		info.setLozinka(konf.getUserPassword());

		return info;
	}
}package org.foi.nwtis.student7.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;


public class AdreseRESTKlijent
{
	private WebTarget webTarget;
	private Client client;
	private static final String BASE_URI = "http://localhost:8084/student7_zadaca_3_1/REST";

	
	public AdreseRESTKlijent()
	{
		client = javax.ws.rs.client.ClientBuilder.newClient();
		webTarget = client.target(BASE_URI).path("adreseREST");
	}

	
	public String dajVazeceMeteoPodatke(String id) throws ClientErrorException
	{
		WebTarget resource = webTarget;
		resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
		return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
	}

	
	public String dajSveAdrese() throws ClientErrorException
	{
		WebTarget resource = webTarget;
		return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
	}

	
	public void close()
	{
		client.close();
	}
}package org.foi.nwtis.student7.web.podaci;


public class Adresa {
    private long idadresa;
    private String adresa;
    private Lokacija geoloc;

	
    public Adresa() {
    }

	
    public Adresa(long idadresa, String adresa, Lokacija geoloc) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

	
    public Lokacija getGeoloc() {
        return geoloc;
    }

	
    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

	
    public long getIdadresa() {
        return idadresa;
    }

	
    public void setIdadresa(long idadresa) {
        this.idadresa = idadresa;
    }

	
    public String getAdresa() {
        return adresa;
    }

	
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
}package org.foi.nwtis.student7.web.podaci;


public class Lokacija {

    private String latitude;
    private String longitude;

	
    public Lokacija() {
    }

	
    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
	
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

	
    public String getLatitude() {
        return latitude;
    }

	
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

	
    public String getLongitude() {
        return longitude;
    }

	
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
}package org.foi.nwtis.student7.web.zrna;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.foi.nwtis.student7.rest.klijenti.AdreseRESTKlijent;
import org.foi.nwtis.student7.web.podaci.Adresa;
import org.foi.nwtis.student7.web.podaci.Lokacija;
import org.foi.nwtis.student7.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.student7.ws.serveri.MeteoPodaci;


@Named(value = "odabirAdresa")
@RequestScoped
public class OdabirAdresa
{
	
	private final List<Adresa> popisAdresa = new ArrayList<>();
	private long[] odabraniIDevi = {};
	
	private final List<MeteoPodaci> dohvaceniMeteoPodaci = new ArrayList<>();
	private final Map<String, MeteoPodaci> dohvaceniMultiMeteoPodaci = new LinkedHashMap<>();
	private String akcija = "NEMA";
	
	
	public OdabirAdresa() 
	{
		try {
			AdreseRESTKlijent adreseRESTKlijent = new AdreseRESTKlijent();
			String sveAdrese = adreseRESTKlijent.dajSveAdrese();
			JsonObject jsonTemelj = Json.createReader(new StringReader(sveAdrese)).readObject();

			if (jsonTemelj.getInt("status") == 0) {
				JsonArray jsonAdrese = jsonTemelj.getJsonArray("adrese");
				for (int i = 0; i < jsonAdrese.size(); i++) {
					JsonObject jsonAdresa = jsonAdrese.getJsonObject(i);

					long id = jsonAdresa.getInt("id");
					String adresa = jsonAdresa.getString("adresa");
					String latitude = jsonAdresa.getString("latitude");
					String longitude = jsonAdresa.getString("longitude");

					Lokacija lokacija = new Lokacija(latitude, longitude);
					popisAdresa.add(new Adresa(id, adresa, lokacija));
				}
			}

			adreseRESTKlijent.close();
		}
		catch (Exception ex) {
			System.out.println("Web servisi se ne mogu kontaktirati.");
		}
	}
	
	
	public List<Adresa> getPopisAdresa()
	{
		return popisAdresa;
	}

	
	public long[] getOdabraniIDevi()
	{
		return odabraniIDevi;
	}

	
	public void setOdabraniIDevi(long[] odabraniIDevi)
	{
		this.odabraniIDevi = odabraniIDevi;
	}
	
	
	public int getBrojOdabranihIDeva()
	{
		return odabraniIDevi.length;
	}
	
	
	public List<Adresa> getOdabraneAdrese()
	{
		List<Adresa> odabraneAdrese = new ArrayList<>();
		
		for (long id : odabraniIDevi) {
			boolean pronadjeno = false;
			for (int i = 0; i < popisAdresa.size() && !pronadjeno; i++) {
				Adresa adresa = popisAdresa.get(i);
				if (adresa.getIdadresa() == id) {
					odabraneAdrese.add(adresa);
					pronadjeno = true;
				}
			}
		}
		
		return odabraneAdrese;
	}

	
	public List<MeteoPodaci> getDohvaceniMeteoPodaci()
	{
		return dohvaceniMeteoPodaci;
	}

	
	public Map<String, MeteoPodaci> getDohvaceniMultiMeteoPodaci()
	{
		return dohvaceniMultiMeteoPodaci;
	}

	
	public String getAkcija()
	{
		return akcija;
	}
	
	
	public void dohvatiMeteoPodatke() 
	{
		long odabraniID = odabraniIDevi[0];
		Adresa odabranaAdresa = null;
		
		boolean pronadjeno = false;
		for (int i = 0; i < popisAdresa.size() && !pronadjeno; i++) {
			if (popisAdresa.get(i).getIdadresa() == odabraniID) {
				odabranaAdresa = popisAdresa.get(i);
				pronadjeno = true;
			}
		}
		
		if (odabranaAdresa != null) {
			dohvaceniMeteoPodaci.clear();
			dohvaceniMeteoPodaci.addAll(MeteoWSKlijent.dajSveMeteoPodatke(odabranaAdresa.getAdresa()));
			akcija = "PRIKAZI_METEO";
		}
	}
	
	
	public void dohvatiPrognozu()
	{
		long odabraniID = odabraniIDevi[0];
		Adresa odabranaAdresa = null;
		
		boolean pronadjeno = false;
		for (int i = 0; i < popisAdresa.size() && !pronadjeno; i++) {
			if (popisAdresa.get(i).getIdadresa() == odabraniID) {
				odabranaAdresa = popisAdresa.get(i);
				pronadjeno = true;
			}
		}
		
		if (odabranaAdresa != null) {
			dohvaceniMeteoPodaci.clear();
			dohvaceniMeteoPodaci.addAll(MeteoWSKlijent.dajPrognozu(odabranaAdresa.getAdresa()));
			akcija = "PRIKAZI_PROGNOZU";
		}
	}
	
	
	public void dohvatiVazeceMeteoPodatke()
	{
		
		List<Adresa> odabraneAdrese = new ArrayList<>();
		
		for (int i = 0; i < odabraniIDevi.length; i++) {
			boolean pronadjeno = false;
			for (int j = 0; j < popisAdresa.size() && !pronadjeno; j++) {
				if (popisAdresa.get(j).getIdadresa() == odabraniIDevi[i]) {
					odabraneAdrese.add(popisAdresa.get(j));
					pronadjeno = true;
				}
			}
		}
		
			if (!odabraneAdrese.isEmpty()) {
			AdreseRESTKlijent adreseRESTKlijent = new AdreseRESTKlijent();
			dohvaceniMultiMeteoPodaci.clear();
			for (Adresa a : odabraneAdrese) {
				String odgovor = adreseRESTKlijent.dajVazeceMeteoPodatke(Long.toString(a.getIdadresa()));
				JsonObject jo = Json.createReader(new StringReader(odgovor)).readObject();

				if (jo.getInt("status") == 0) {
					MeteoPodaci mp = new MeteoPodaci();

					mp.setWeatherValue(jo.getString("vrijeme"));
					mp.setTemperatureValue(new Double(jo.getString("temp")).floatValue());
					mp.setTemperatureMin(new Double(jo.getString("tempMin")).floatValue());
					mp.setTemperatureMax(new Double(jo.getString("tempMax")).floatValue());
					mp.setHumidityValue(new Double(jo.getString("vlaga")).floatValue());
					mp.setPressureValue(new Double(jo.getString("tlak")).floatValue());
					mp.setWindSpeedValue(new Double(jo.getString("vjetar")).floatValue());
					mp.setWindDirectionValue(new Double(jo.getString("vjetarSmjer")).floatValue());

					dohvaceniMultiMeteoPodaci.put(a.getAdresa(), mp);
				}
			}
			akcija = "PRIKAZI_VAZECE_METEO";
			adreseRESTKlijent.close();
		}
	}
	
	
	public String formatirajDatum(Date datum) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
		return sdf.format(datum);
	}
}package org.foi.nwtis.student7.ws.klijenti;


public class MeteoWSKlijent
{
	
	public static java.util.List<org.foi.nwtis.student7.ws.serveri.MeteoPodaci> dajSveMeteoPodatke(java.lang.String address)
	{
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service();
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
		return port.dajSveMeteoPodatke(address);
	}

	
	public static java.util.List<org.foi.nwtis.student7.ws.serveri.MeteoPodaci> dajPrognozu(java.lang.String address)
	{
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service();
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
		return port.dajPrognozu(address);
	}
}