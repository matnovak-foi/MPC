/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Klasa koja predstavlja entitet Dnevnik
 *  student13
 */

public class Dnevnik implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Osnovni konstruktor
     */


    /**
     * Konstruktor koji prima argument id tipa integer
     *  id 
     */


    /**
     * Konstruktor koji prima argumente id, korisnik, url, ipadresa, vrijeme, trajanje i status
     *  id
     *  korisnik
     *  url
     *  ipadresa
     *  vrijeme
     *  trajanje
     *  status 
     */

    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dnevnik)) {
            return false;
        }
        Dnevnik other = (Dnevnik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    public String toString() {
        return "org.foi.nwtis.student13.ejb.eb.Dnevnik[ id=" + id + " ]";
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Klasa koja predstavlja entitet Meteo
 *  student13
 */

public class Meteo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Osnovni konstruktor
     */


    /**
     * Konstruktor koji prima argument idmeteo tipa integer
     *  idmeteo 
     */


    /**
     * Konstruktor koji prima argumente idmeteo, adresastanice, latitude, longitude, 
     * vrijeme, vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, preuzeto
     *  idmeteo
     *  adresastanice
     *  latitude
     *  longitude
     *  vrijeme
     *  vrijemeopis
     *  temp
     *  tempmin
     *  tempmax
     *  vlaga
     *  tlak
     *  vjetar
     *  vjetarsmjer
     *  preuzeto 
     */

    public int hashCode() {
        int hash = 0;
        hash += (idmeteo != null ? idmeteo.hashCode() : 0);
        return hash;
    }

    
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meteo)) {
            return false;
        }
        Meteo other = (Meteo) object;
        if ((this.idmeteo == null && other.idmeteo != null) || (this.idmeteo != null && !this.idmeteo.equals(other.idmeteo))) {
            return false;
        }
        return true;
    }

    
    public String toString() {
        return "org.foi.nwtis.student13.ejb.eb.Meteo[ idmeteo=" + idmeteo + " ]";
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  student13
 */

public class Parkiralista implements Serializable {

    private static final long serialVersionUID = 1L;
/**
     * Osnovni konstruktor
     */


    /**
     * Konstruktor koji prima argument id
     *  id 
     */


    /**
     * Konstruktor koji prima argumente id, naziv, adresa, latitude, longitude
     *  id
     *  naziv
     *  adresa
     *  latitude
     *  longitude 
     */

    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parkiralista)) {
            return false;
        }
        Parkiralista other = (Parkiralista) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    public String toString() {
        return "org.foi.nwtis.student13.ejb.eb.Parkiralista[ id=" + id + " ]";
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Abstraktna klasa fasade
 *  student13
 */
public abstract class AbstractFacade<T> {
/**
     * Konstruktor koji prima entitetnu klasu
     *  entityClass 
     */


    /**
     * Vraca objekt EntitiyManager-a
     *  
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Kreira prosljedeni entitet
     *  entity 
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Ažurira prosljedeni entitet
     *  entity 
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Briše prosljedeni entitet
     *  entity 
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Pronalazi entitet na temelju id-a
     *  id
     *  
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Vraca sve zapise entiteta u obliku liste
     *  
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Pronalazi i vraca listu entiteta na temelju prosljedenog raspona
     *  range
     *  
     */
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Vraca broj entiteta
     *  
     */
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Klasa fasade dnevnika
 *
 *  student13
 */

public class DnevnikFacade extends AbstractFacade<Dnevnik> {
/**
     * Osnovni konstruktor
     */


    /**
     * Dohvaca podatke entiteta dnevnika na temelju prosljedenih argumenata.
     * Koristi se Criteria API.
     *
     *  ipAdresa
     *  datumOd
     *  datumDo
     *  adresaZahtjeva
     *  trajanje
     * 
     */
    public List<Dnevnik> dohvatiPodatkeDnevnika(String ipAdresa, Date datumOd, Date datumDo, String adresaZahtjeva, String trajanje) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Dnevnik> dnevnik = cq.from(Dnevnik.class);
        List<Predicate> conditionsList = new ArrayList<>();        
        if(adresaZahtjeva != null && !adresaZahtjeva.isEmpty()){
            conditionsList.add(cb.equal(dnevnik.get("url"), adresaZahtjeva));
        }
        if(ipAdresa != null && !ipAdresa.isEmpty()){
            conditionsList.add(cb.equal(dnevnik.get("ipadresa"), ipAdresa));
        }
        if(datumOd != null && datumDo != null){
            conditionsList.add(cb.greaterThanOrEqualTo(dnevnik.<Date>get("vrijeme"), datumOd));
            conditionsList.add(cb.lessThanOrEqualTo(dnevnik.<Date>get("vrijeme"), datumDo));
        }
        if(trajanje != null && !trajanje.isEmpty()){
            conditionsList.add(cb.equal(dnevnik.get("trajanje"), Integer.parseInt(trajanje)));
        }
        cq.select(dnevnik).where(conditionsList.toArray(new Predicate[]{}));
        List<Dnevnik> d = new ArrayList<>();
        d = getEntityManager().createQuery(cq).getResultList();
        return getEntityManager().createQuery(cq).getResultList();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Klasa fasade Meteo
 *  student13
 */

public class MeteoFacade extends AbstractFacade<Meteo> {
/**
     * Osnovni konstruktor
     */

    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Klasa fasade Parkiralista
 *  student13
 */

public class ParkiralistaFacade extends AbstractFacade<Parkiralista> {
/**
     * Osnovni konstruktor
     */

    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Klasa MeteoKlijentZrno služi za dohvacanje lokacije i meteorološke prognoze
 *  student13
 */


public class MeteoKlijentZrno {

    /**
     * Postavlja korisničke podatke (apiKey - openWeatherMap i gmApiKey - googleMaps)
     *  apiKey
     *  gmApiKey 
     */


    /**
     * Dohvaca i vraca lokaciju na temelju prosljedene adrese
     *  adresa
     *  Lokacija
     */
    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent(gmApiKey);
        Lokacija lokacija = gmk.getGeoLocation(adresa);
        return lokacija;
    }
   
    /**
     * Dohvaca i vraca prognozu vremena na temelju prosljedene adrese.
     *  id
     *  adresa
     *  MeteoPrognoza[] 
     */
    public MeteoPrognoza[] dajMeteoPrognoze(int id, String adresa) {
        OWMKlijentPrognoza klijentPrognoza = new OWMKlijentPrognoza(apiKey);
        Lokacija l = dajLokaciju(adresa);
        MeteoPrognoza[] meteoPrognoza = klijentPrognoza.getWeatherForecast(id, l.getLatitude(), l.getLongitude());
        return meteoPrognoza;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  nwtis_1
 */
public class GMKlijent {

    GMRESTHelper helper;
    Client client;
    String apikey;

    public GMKlijent(String apikey) {
        client = ClientBuilder.newClient();
        this.apikey = apikey;
    }

    public Lokacija getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                    .path("maps/api/geocode/json");
            webResource = webResource.queryParam("address",
                    URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");
            webResource = webResource.queryParam("key", this.apikey);

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

            StringReader sr = new StringReader(odgovor);
            JsonReader reader = Json.createReader(sr);

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
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *  teacher2
 */
public class GMRESTHelper {
    private static final String GM_BASE_URI = "https://maps.google.com/";    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  nwtis_1
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;

    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

     /**
     * Metoda za dohvacanje prognoze vremena za odredenu geografsku širinu i dužinu. Koristi api
     * za dohvacanje prognoze za trenutni dan tj. vrijeme
     *  latitude
     *  longitude
     *  List<MeteoPodaci>
     */
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
            mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue() * 1000));
            mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue() * 1000));

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

            mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
            return mp;

        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda za dohvacanje prognoze vremena za odredenu geografsku širinu i dužinu. Koristi api
     * za dohvacanje prognoze za 5 dana svaka 3 sata
     *  latitude
     *  longitude
     *  List<MeteoPodaci>
     */
    public List<MeteoPodaci> getRealTimeForecast(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);
        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JsonReader reader = Json.createReader(new StringReader(odgovor));
            JsonObject jo = reader.readObject();             
            List<MeteoPodaci> meteoPodaciList = new ArrayList<>();        
            JsonArray jsonArray = jo.getJsonArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                MeteoPodaci mp = new MeteoPodaci();
                JsonObject joInner = jsonArray.getJsonObject(i);
                mp.setTemperatureValue(new Double(joInner.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
                mp.setTemperatureMin(new Double(joInner.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
                mp.setTemperatureMax(new Double(joInner.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
                mp.setTemperatureUnit("celsius");
                mp.setHumidityValue(new Double(joInner.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
                mp.setHumidityUnit("%");
                mp.setPressureValue(new Double(joInner.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
                mp.setPressureUnit("hPa");
                mp.setWindSpeedValue(new Double(joInner.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
                mp.setWindSpeedName("");
                mp.setWindDirectionValue(new Double(joInner.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
                mp.setWindDirectionCode("");
                mp.setWindDirectionName("");
                mp.setCloudsValue(joInner.getJsonObject("clouds").getInt("all"));
                mp.setCloudsName(joInner.getJsonArray("weather").getJsonObject(0).getString("description"));
                mp.setPrecipitationMode("");
                mp.setWeatherNumber(joInner.getJsonArray("weather").getJsonObject(0).getInt("id"));
                mp.setWeatherValue(joInner.getJsonArray("weather").getJsonObject(0).getString("description"));
                mp.setWeatherIcon(joInner.getJsonArray("weather").getJsonObject(0).getString("icon"));
                mp.setLastUpdate(new Date(joInner.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));               
                meteoPodaciList.add(mp);
            }
            return meteoPodaciList;
        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
/**
 * Klasa za poziv api-a za dohvacanje prognoze vremena
 *  student13
 */
public class OWMKlijentPrognoza extends OWMKlijent{

    /**
     * Osnovni konstruktor, prima argument api ključa za openweathermap
     *  apiKey 
     */

    
    /**
     * Poziva metodu za korištenje api-a za dovacanje prognoze. 
     * Vraca polje dohvacenih meteo prognoza za odredeno parkiralište.
     *  id
     *  latitude
     *  longitude
     *  MeteoPrognoza[]
     */
    public MeteoPrognoza[] getWeatherForecast(int id, String latitude, String longitude){
         List<MeteoPodaci> mpList = getRealTimeForecast(latitude, longitude);
         List<MeteoPrognoza> meteoPrognozaList = new ArrayList<>();
         
         for(MeteoPodaci mp: mpList){
             Date date = mp.getLastUpdate();
             Calendar calendar = Calendar.getInstance();
             calendar.setTime(date);         
             MeteoPrognoza mPrognoza = new MeteoPrognoza(id, calendar.get(Calendar.HOUR_OF_DAY), mp);
             meteoPrognozaList.add(mPrognoza);
         }
         MeteoPrognoza[] meteoPrognozaArray = new MeteoPrognoza[meteoPrognozaList.size()];
         meteoPrognozaList.toArray(meteoPrognozaArray);
         return meteoPrognozaArray;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *  teacher2
 */
public class OWMRESTHelper {
    private static final String OWM_BASE_URI = "http://api.openweathermap.org/data/2.5/";    
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
        
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 *  teacher2
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  teacher2
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 *  teacher2
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  teacher2
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Klasa dnevnika
 *  student13
 */
public class Dnevnik {
/**
     * Konstruktor koji prima argument id, korisnik, url, ipadresa, vrijemezapisa, trajanje i status
     *  id
     *  korisnik
     *  url
     *  ipAdresa
     *  vrijemeZapisa
     *  trajanje
     *  status 
     */

}


/**
 * Klasa izbornika
 *  student13
 */
public class Izbornik {
/**
     * Konstruktor koji prima argumente labela i vrijednost
     *  labela
     *  vrijednost 
     */
public boolean equals(Object obj) {
        if(obj instanceof Izbornik) {
            Izbornik obj2 = (Izbornik) obj;
            if(this.vrijednost.equals(obj2.getVrijednost())
                    && 
                    this.labela.equals(obj2.getLabela())){
                return true;
            }
        }
        
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    
    public String toString() {
        return "Izbornik{" + "labela=" + labela + ", vrijednost=" + vrijednost + '}';
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 *  teacher2
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  teacher2
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * POJO klasa koja sadrži podatke parkirališta i njegovih meteoroloških podataka.
 * Služi za jednostavnije korištenje i prikaz korisniku
 *  student13
 */
public class MeteoPodaciParkiraliste extends Parkiraliste{   
    

    /**
     * Konstruktor koji prima argumente id, naziv, adresa, geolokacija i meteopodaci
     * Poziva konstruktor nasljedene klase Parkiraliste
     *  id
     *  naziv
     *  adresa
     *  geoloc
     *  meteoPodaci 
     */
    public MeteoPodaciParkiraliste(int id, String naziv, String adresa, Lokacija geoloc, MeteoPodaci meteoPodaci) {
        super(id, naziv, adresa, geoloc);
        this.meteoPodaci = meteoPodaci;
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 *  teacher2
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  teacher2
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Slušač aplikacije
 *
 *  student13
 */
public class SlusacAplikacije implements ServletContextListener {
/**
     * Metoda za inicijaliziranje konteksta aplikacije
     *
     *  sce ServletContextEvent
     */
    
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        String datoteka = servletContext.getInitParameter("konfiguracija");
        String putanja = servletContext.getRealPath("/WEB-INF") + java.io.File.separator;
        try {
            Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja + datoteka);
            servletContext.setAttribute("Konfiguracija", konfig);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prekini rad drete obrade podataka i makni atribute konfiguracije iz
     * servlet contexta
     *
     *  sce ServletContextEvent
     */
    
    public void contextDestroyed(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.removeAttribute("Konfiguracija");
        servletContext = null;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Klasa koja implementira sučelje Filter.
 * Služi za presretanje Servlet zahtjeva
 *  student13
 */

public class MyFilter implements Filter {

    
    DnevnikFacade dnevnikFacade;

    /**
     * Nadjačava metodu doFilter, mjeri koliko vremena prode od trenutka zahtjeva do trenutka odgovora.
     * Izvlači iz zahtjeva url tj. putanju, IP korisnika i status odgovora.
     * Izvučene podatke sprema u entite Dnevnika
     *  request
     *  response
     *  chain
     *  IOException
     *  ServletException 
     */
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long pocetak = System.currentTimeMillis();
        chain.doFilter(request, response);
        long kraj = System.currentTimeMillis();
        long trajanje = (kraj - pocetak);
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        int status = ((HttpServletResponse) response).getStatus();
        String ip = request.getRemoteAddr();
        if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            ip = ipAddress;
        }
        Dnevnik dnevnik = new Dnevnik(0, "Gost", url, ip, new Date(), (int) trajanje, status);
        dnevnikFacade.edit(dnevnik);
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Klasa Pregleda, služi za prikazivanje i rad s podacima parkirališta i
 * meteoroloških podataka
 *
 *  student13
 */


public class Pregled implements Serializable {

    private List<Izbornik> popisParking = new ArrayList<>();
    private List<String> popisParkingOdabrano = new ArrayList<>();
    private List<Izbornik> popisParkingMeteo = new ArrayList<>();
    private List<String> popisParkingMeteoOdabrana = new ArrayList<>();
    private List<MeteoPodaciParkiraliste> popisMeteoPodaci = new ArrayList<>();

    private boolean dodajAzuriraj = false;      //Zastavica koja oznacava da li treba napraviti azuriranje liste parkiralista
    private boolean gumbUpisiPrikazi = false;          //Zastavica koja definira da li treba prikazati ili sakriti gumb upisi
    private boolean tablicaPrognozaPrikazi = false;    //Zastavica za prikazivanje/skrivanje tablice prognoze
    private String pogreska = "";
    
    private String gumbPrognoza = "Prognoza";

    /**
     * Osnovni konstruktor
     */


    /**
     * Metoda koja se poziva prije poziva poslonih metoda Postavlja potrebne api
     * ključeve te kreira potrebne objekte za dohvat prognoze
     */
    
    public void init() {
        ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Konfiguracija konfig = (Konfiguracija) sc.getAttribute("Konfiguracija");
        if (konfig != null) {
            String apiKey = konfig.dajPostavku("apikey");
            String gmapiKey = konfig.dajPostavku("gmapikey");
            meteoKlijentZrno.postaviKoorisnickePodatke(apiKey, gmapiKey);
            owmKlijentPrognoza = new OWMKlijentPrognoza(apiKey);
        }
    }

    /**
     * Poziva metodu za dodavanje parkirališta u bazu. Ako je dodavanje uspješno
     * poziva se čišcenje forme te se podiže zastavica koja označava da je
     * dodano/ažurirano parkiralište
     *
     * 
     */
    public String dodajParkiraliste() {
        pogreska = "";
        upisiParkiraliste();
        dodajAzuriraj = true;
        if (pogreska.isEmpty()) {
            postaviPocetnoStanjeForme();
        }
        return "";
    }

    /**
     * Poziva metodu za ažuriranje parkirališta u bazi. Ako je dodavanje
     * uspješno poziva se čišcenje forme te se podiže zastavica koja označava da
     * je dodano/ažurirano parkiralište
     *
     * 
     */
    public String azurirajParkiraliste() {
        pogreska = "";
        upisiParkiraliste();
        if (pogreska.isEmpty()) {
            postaviPocetnoStanjeForme();
        }
        return "";
    }

    /**
     * Provjerava da li su sva polja u formu za dodavanje/ažuriranje
     * parkirališta popunjena. Ako nema pogrešaka ažurira/dodaje parkiralište u
     * bazu. Inače kreira poruku pogreške.
     */
    private void upisiParkiraliste() {
        pogreska = "";
        if (id == null) {
            pogreska += "ID nedostaje, ";
        }
        if (naziv.isEmpty()) {
            pogreska += "Naziv nedostaje, ";
        }
        if (adresa.isEmpty()) {
            pogreska += "Adresa nedostaje, ";
        }
        if (pogreska.isEmpty()) {
            Parkiralista parkiraliste = new Parkiralista();
            try {
                parkiraliste.setId(id);
                parkiraliste.setNaziv(naziv);
                parkiraliste.setAdresa(adresa);
                Lokacija lok = meteoKlijentZrno.dajLokaciju(adresa);
                parkiraliste.setLatitude(Float.parseFloat(lok.getLatitude()));
                parkiraliste.setLongitude(Float.parseFloat(lok.getLongitude()));
                parkiralistaFacade.edit(parkiraliste);
                dodajAzuriraj = true;
                pogreska = "";
            } catch (Exception ex) {
                pogreska += "Ne postoje geolokacijski podaci za navedenu adresu, ";
            }
        }
        if (!pogreska.isEmpty()) {
            pogreska = pogreska.substring(0, pogreska.length() - 2);
        }
    }

    /**
     * Vraca formu u početno stranje (čisti inpute na formi i skriva gumbe)
     */
    private void postaviPocetnoStanjeForme() {
        id = null;
        naziv = "";
        adresa = "";
        gumbUpisiPrikazi = false;      //Sakrij gumb
        pogreska = "";
    }

    /**
     * Dohvaca podatke odabranog parkirališta. Ako je odabrano više parkirališta
     * dohvaca podatke prvog u popisu. Postavlja zastavicu za prikazivanje gumba
     * na true Ako nije odabrano ni jedno ništa ne radi.
     *
     * 
     */
    public String dohvatiPodatkeParkiralista() {
        if (!popisParkingOdabrano.isEmpty()) {
            pogreska = "";
            String odabranoParkiralisteId = popisParkingOdabrano.get(0);
            Parkiralista odabraParkiraliste = parkiralistaFacade.find(Integer.parseInt(odabranoParkiralisteId));
            id = odabraParkiraliste.getId();
            naziv = odabraParkiraliste.getNaziv();
            adresa = odabraParkiraliste.getAdresa();
            gumbUpisiPrikazi = true;   //Prikazi gumb
        }
        return "";
    }

    /**
     * Obavlja prebacivanje odabranih parkirališta iz popisa parkirališta u
     * popis (listu) parkirališta za koje se mogu dohvatit meteorološki podaci.
     * Sukladno tomu odabrana parkirališta se brišu iz liste popisa
     * parkirališta.
     *
     * 
     */
    public String preuzmiParkiralista() {
        for (Izbornik i : popisParking) {
            if (popisParkingOdabrano.contains(i.getVrijednost()) && !popisParkingMeteo.contains(i)) {
                popisParkingMeteo.add(i);
            }
        }

        for (Izbornik i : popisParkingMeteo) {
            List<Izbornik> noviPopisParkiing = new ArrayList<>();
            if (popisParking.contains(i)) {
                popisParking.remove(i);
            }
        }

        return "";
    }

    /**
     * Prebacuje odabrana parkirališta iz popisa parkirališta za dohvat
     * meteoroloških podataka u opceniti popis svih parkirališta. Ažurira liste.
     *
     * 
     */
    public String vratiParkiralista() {
        Iterator iterator = popisParkingMeteo.iterator();
        while (iterator.hasNext()) {
            Izbornik i = (Izbornik) iterator.next();
            if (popisParkingMeteoOdabrana.contains(i.getVrijednost()) && !popisParking.contains(i)) {
                iterator.remove();
                popisParking.add(i);
            }
        }
        return "";
    }

    /**
     * Preuzima meteorološke podatke za odabrana parkirališta iz popisa
     * parkirališta za dohvacanje meteoroloških podataka. Dohvacene meteorološke
     * podatke prikazuje u tablici.
     *
     * 
     */
    public String preuzmiMeteoPodatke() {
        if (gumbPrognoza.equals("Zatvori prognoze")) {
            gumbPrognoza = "Prognoza";
            tablicaPrognozaPrikazi = false;
            popisMeteoPodaci = null;
        } else {
            if (!popisParkingMeteoOdabrana.isEmpty()) {
                popisMeteoPodaci = new ArrayList<>();
                for (String idParkiraliste : popisParkingMeteoOdabrana) {
                    Parkiralista pMeteo = parkiralistaFacade.find(Integer.parseInt(idParkiraliste));
                    String latitute = Float.toString(pMeteo.getLatitude());
                    String longitude = Float.toString(pMeteo.getLongitude());
                    MeteoPrognoza[] mpArr = owmKlijentPrognoza.getWeatherForecast(pMeteo.getId(), latitute, longitude);
                    for (int i = 0; i < mpArr.length; i++) {
                        MeteoPodaci mp = mpArr[i].getPrognoza();
                        MeteoPodaciParkiraliste mpParkiraliste = new MeteoPodaciParkiraliste(Integer.parseInt(idParkiraliste),
                                pMeteo.getNaziv(), pMeteo.getAdresa(), new Lokacija(latitute, longitude), mp);
                        popisMeteoPodaci.add(mpParkiraliste);
                    }
                }
                tablicaPrognozaPrikazi = true;
                gumbPrognoza = "Zatvori prognoze";
            }
        }
        return "";
    }
/**
     * Dohaca popis parkirališta iz baze podataka. Ako je popis prazan, dohvaca
     * izravno iz baze podataka. Ako popis nije prazan provjerava da li je
     * podignuta zastavica za dodavanje/ažuriranje, ako je podignuta ažurira
     * listu parkirališta sukladno dodanim/ažuriranim parkiralištima. Obavlja
     * abecedno sortiranje parkirališta. Parkirališta sprema i listu Izbornika
     * koji se prikazuje korisniku
     *
     *  List<Izbornik>
     */
    public List<Izbornik> getPopisParking() {
        if (popisParking.isEmpty() && popisParkingMeteo.isEmpty()) {
            for (Parkiralista parkiraliste : parkiralistaFacade.findAll()) {
                Izbornik i = new Izbornik(parkiraliste.getNaziv(), Integer.toString(parkiraliste.getId()));
                popisParking.add(i);
            }
        } else if (dodajAzuriraj) {
            dodajAzuriraj = false;
            popisParking = new ArrayList<Izbornik>();
            for (Parkiralista parkiraliste : parkiralistaFacade.findAll()) {
                Izbornik i = new Izbornik(parkiraliste.getNaziv(), Integer.toString(parkiraliste.getId()));
                popisParking.add(i);
            }
        }
        Collections.sort(popisParking, new Comparator<Izbornik>() {
            
            public int compare(Izbornik i1, Izbornik i2) {
                return i1.getLabela().compareTo(i2.getLabela());
            }
        });
        return popisParking;
    }
/**
     * Vraca popis parkirališta za dohvacanje meteoroloških podataka abecedno
     * sortiranih
     *
     * 
     */
    public List<Izbornik> getPopisParkingMeteo() {
        Collections.sort(popisParkingMeteo, new Comparator<Izbornik>() {
            
            public int compare(Izbornik i1, Izbornik i2) {
                return i1.getLabela().compareTo(i2.getLabela());
            }
        });
        return popisParkingMeteo;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Klasa za prikaz i rad s podacima dnevnika
 *  student13
 */


public class PregledDnevnika implements Serializable {
private boolean prikaziTablicu = false;

    /**
     * Osnovni konstruktor
     */


    /**
     * Dohvaca podatke iz baze za dnevnik na temelju prosljedenih podataka putem forme
     *  
     */
    public String preuzmiPodatke() {
        prikaziTablicu = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
        Date datumOdPretvoreni = null;
        Date datumDoPretvoreni = null;
        String datumOdDodano = datumOd + ":000";    //Potrebno dodati milisekunde jer je tako spremljeno u bazi podataka
        String datumDoDodano = datumDo + ":999";
        try {
            datumOdPretvoreni = sdf.parse(datumOdDodano.trim());
            datumDoPretvoreni = sdf.parse(datumDoDodano.trim());
        } catch (ParseException ex) {            
            Logger.getLogger(PregledDnevnika.class.getName()).log(Level.SEVERE, null, ex);
        }
        zapisiDnevnika = dnevnikFacade.dohvatiPodatkeDnevnika(ipAdresa.trim(), datumOdPretvoreni, datumDoPretvoreni, adresaZahtjeva.trim(), trajanje.trim());
        return "";
    }
}