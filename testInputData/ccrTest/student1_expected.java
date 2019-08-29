/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  NWTiS_1
 */

public class Adrese implements Serializable {
    private static final long serialVersionUID = 1L;

    public int hashCode() {
        int hash = 0;
        hash += (idadresa != null ? idadresa.hashCode() : 0);
        return hash;
    }

    
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adrese)) {
            return false;
        }
        Adrese other = (Adrese) object;
        if ((this.idadresa == null && other.idadresa != null) || (this.idadresa != null && !this.idadresa.equals(other.idadresa))) {
            return false;
        }
        return true;
    }

    
    public String toString() {
        return "org.foi.nwtis.student2.ejb.eb.Adrese[ idadresa=" + idadresa + " ]";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *  NWTiS_1
 */

public class Dnevnik implements Serializable {
    private static final long serialVersionUID = 1L;
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
        return "org.foi.nwtis.student2.ejb.eb.Dnevnik[ id=" + id + " ]";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *  NWTiS_1
 */
public abstract class AbstractFacade<T> {

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  NWTiS_1
 */

public class AdreseFacade extends AbstractFacade<Adrese> {

    public List<Adrese> findByAdresa(String zaAdresu) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Adrese> adresa = cq.from(Adrese.class);
        Expression<String> premaAdresi = adresa.get("adresa");
        cq.where(cb.and(cb.equal(premaAdresi, zaAdresu)));
        return getEntityManager().createQuery(cq).getResultList();    
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *  NWTiS_1
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *  NWTiS_1
 */


public class MeteoAdresniKlijent {
// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public MeteoPodaci dajVazeceMeteoPodatke(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);

        OWMKlijent owmk = new OWMKlijent(this.apiKey);

        MeteoPodaci mp = owmk.getRealTimeWeather(l.getLatitude(),
                l.getLongitude());     
        
        return mp;
    }

    public ArrayList<MeteoPrognoza> dajMeteoPrognoze(String adresa, int brojDana) {
        Lokacija l = dajLokaciju(adresa);
        if (l == null) {
            return null;
        }
        OWMKlijent owmk = new OWMKlijent("3e42147a3c12ac686ecafc5c42c98ebb");
        ArrayList<MeteoPrognoza> mp = owmk.getWeatherForecast(l.getLatitude(), l.getLongitude(), brojDana);
        
        return mp;
    }

    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);
        return l;
    }
}/*
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

            JSONObject jo = new JSONObject(odgovor);
            JSONObject obj = jo.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");

            Lokacija loc = new Lokacija(obj.getString("lat"), obj.getString("lng"));

            return loc;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 *  teacher2
 */
public class GMRESTHelper {
    private static final String GM_BASE_URI = "http://maps.google.com/";    
}/*
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
            JSONObject jo = new JSONObject(odgovor);
            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJSONObject("sys").getLong("sunrise")));
            mp.setSunSet(new Date(jo.getJSONObject("sys").getLong("sunset")));

            mp.setTemperatureValue(Float.parseFloat(jo.getJSONObject("main").getString("temp")));
            mp.setTemperatureMin(Float.parseFloat(jo.getJSONObject("main").getString("temp_min")));
            mp.setTemperatureMax(Float.parseFloat(jo.getJSONObject("main").getString("temp_max")));
            mp.setTemperatureUnit("celsius");

            mp.setHumidityValue(Float.parseFloat(jo.getJSONObject("main").getString("pressure")));
            mp.setHumidityUnit("%");

            mp.setPressureValue(Float.parseFloat(jo.getJSONObject("main").getString("humidity")));
            mp.setHumidityUnit("hPa");

            mp.setWindSpeedValue(Float.parseFloat(jo.getJSONObject("wind").getString("speed")));
            mp.setWindSpeedName("");

            mp.setWindDirectionValue(Float.parseFloat(jo.getJSONObject("wind").getString("deg")));
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJSONObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJSONArray("weather").getJSONObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJSONArray("weather").getJSONObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getLong("dt")));

            return mp;

        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<MeteoPrognoza> getWeatherForecast(String latitude, String longitude, int noDays) {
        MeteoPrognoza[] mPrognoza = new MeteoPrognoza[noDays + 1];

        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_ForecastDaily_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("cnt", noDays);
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

        try {
            JSONObject jo = new JSONObject(odgovor);
            JSONArray ja = jo.getJSONArray("list");
            int limit = jo.getInt("cnt");

            for (int i = 0; i < limit; i++) {
                mPrognoza[i] = new MeteoPrognoza();
                
                mPrognoza[i].setDan(i + 1);
                mPrognoza[i].setAdresa(jo.getJSONObject("city").getString("name"));
                mPrognoza[i].setDatum(new Date(ja.getJSONObject(i).getLong("dt") * 1000));
                
                MeteoPodaci mp = new MeteoPodaci();

                mp.setTemperatureMax(Float.parseFloat(ja.getJSONObject(i).getJSONObject("temp").getString("max")));
                mp.setTemperatureMin(Float.parseFloat(ja.getJSONObject(i).getJSONObject("temp").getString("min")));
                mp.setWindDirectionValue(Float.parseFloat(ja.getJSONObject(i).getString("deg")));
                
                mPrognoza[i].setPrognoza(mp);
           }
            
            return new ArrayList<>(Arrays.asList(mPrognoza));
        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}/*
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
        
}/*
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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Abstraktna klasa koju implemetiraju drugi beanovi, sadrži metodu za 
 * zapisivanje u dnevnik kako se nebi duplao kod
 *  Sname5
 */
public abstract class BaseBean {

    /**
     * Zapis akcije na stranici u dnevnik unutar baze podataka
     *
     *  pocetak vrijeme početka izvršavanja akcije
     *  status 200 - OK / 400 - ERROR
     */
    protected void zapisiUDnevnik(long pocetak, int status) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setKorisnik("student2");
        String IP = null;
        String url = null;

        // dohvati IP
        try {
            IP = InetAddress.getLocalHost().toString();
        } catch (Exception ex) {
        }
        if (IP == null || IP.length() <= 0) {
            dnevnik.setIpadresa("n/a");
        } else {
            dnevnik.setIpadresa(IP);
        }

        // dohvati trenutni URL
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = origRequest.getRequestURI();
        if (url != null) {
            dnevnik.setUrl(url);
        } else {
            dnevnik.setUrl("n/a");
        }

        dnevnik.setTrajanje((int) (System.currentTimeMillis() - pocetak));
        dnevnik.setVrijeme(new Date());
        dnevnik.setStatus(status);

        dnevnikFacade.create(dnevnik);
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * ManagedBean klasa koja se koristi za pregledAdresa.xhtml
 */


public class OdabirAdresaPrognoza extends BaseBean implements Serializable {

    private static final int STATUS_OK = 200;
    private static final int STATUS_ERROR = 400;
private boolean prikaziAzuriranje = false;
    private boolean prikaziPrognoze = false;

    /**
     * Creates a new instance of OdabirAdresaPrognoza
     */


    /**
     * Dodavanje odabrane adrese iz liste svih adresa u listu za dohvaćanje
     * meteo podataka
     */
    public void dodajAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        prognozaAdrese.add(odabranaAdresaZaDodati);
        aktivneAdrese.removeIf(s -> s.equals(odabranaAdresaZaDodati));

        // sortiraj abecedno
        Collections.sort(prognozaAdrese, Collator.getInstance(new Locale("hr", "HR")));

        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
    }

    /**
     * Uklanjanje odabrane adrese iz liste adresa za dohvaćanje meteo podataka i
     * prebacivanje u listu sa svim adresama
     */
    public void ukloniAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        aktivneAdrese.add(odabranaAdresaZaMaknuti);
        prognozaAdrese.removeIf(s -> s.equals(odabranaAdresaZaMaknuti));

        // sortiraj abecedno
        Collections.sort(prognozaAdrese, Collator.getInstance(new Locale("hr", "HR")));

        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
    }

    /**
     * Upisivanje nove adrese zajedno sa njenim geolokacijskim podacima u bazu
     * podataka
     *
     *  null
     */
    public Object upisiAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        // ako je prazno zapiši pogrešku u dnevnik
        if (novaAdresa == null || novaAdresa.isEmpty()) {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_ERROR);
            // ako nije prazno spremi novu adresu
        } else {
            Adrese adresa = new Adrese();
            adresa.setAdresa(novaAdresa);
            Lokacija l = meteoAdresniKlijent.dajLokaciju(novaAdresa);
            adresa.setLatitude(l.getLatitude());
            adresa.setLongitude(l.getLongitude());
            adreseFacade.create(adresa);
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
        }
        return null;
    }

    /**
     * Ažuriranje odabrane adrese u bazi podataka
     *
     *  null
     */
    public Object azurirajAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        List<Adrese> adrese = adreseFacade.findAll();
        Adrese adresaZaAzuriranje = new Adrese();

        // pronađi traženu adresu u listi svih adresa
        for (Adrese a : adrese) {
            if (a.getAdresa().equals(originalnaAdresa)) {
                adresaZaAzuriranje = a;
            }
        }

        // ako adresa ne postoji, izađi iz metode
        if (adresaZaAzuriranje.getAdresa() == null || adresaZaAzuriranje.getAdresa().isEmpty()) {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_ERROR);
            return null;
        }

        // dohvati nove geolokacijske podatke za adresu
        Lokacija l = meteoAdresniKlijent.dajLokaciju(novaAdresa);
        adresaZaAzuriranje.setLatitude(l.getLatitude());
        adresaZaAzuriranje.setLongitude(l.getLongitude());
        adresaZaAzuriranje.setAdresa(azuriranaAdresa);
        adreseFacade.edit(adresaZaAzuriranje);

        prikaziAzuriranje = false;
        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
        return null;
    }

    /**
     * Otvaranje forme za ažuriranje adrese
     *
     * 
     */
    public Object otvoriAzuriranjeAdrese() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        prikaziAzuriranje = true;
        originalnaAdresa = odabranaAdresaZaDodati;
        azuriranaAdresa = odabranaAdresaZaDodati;
        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
        return null;
    }

    /**
     * Dohvati meteo podatke za sve adrese dodane u listu za dohvaćanje meteo
     * podataka, dohvaćaju se podaci za broj dana koji je upisan u polje. Metoda
     * također otvara prikaz tablice ispod forme.
     */
    public void dohvatiMeteoPodatke() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        meteoPrognoza = new ArrayList<>();
        List<MeteoPrognoza> mp;

        for (String s : prognozaAdrese) {
            mp = meteoAdresniKlijent.dajMeteoPrognoze(s, brojDana);
            if (mp != null) {
                meteoPrognoza.addAll(mp);
            }
        }

        prikaziPrognoze = true;

        // zapisi u dnevnik
        if (!meteoPrognoza.isEmpty()) {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
        } else {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_ERROR);
        }
    }

    /**
     * Zatvaranje prikaza tablice ispod forme
     */
    public void zatvoriTablicu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        prikaziPrognoze = false;
        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
    }
public List<String> getAktivneAdrese() {
        aktivneAdrese = new ArrayList<>();
        if (prognozaAdrese == null) {
            prognozaAdrese = new ArrayList<>();
        }
        List<Adrese> adrese = adreseFacade.findAll();
        for (Adrese a : adrese) {
            if (!prognozaAdrese.contains(a.getAdresa())) {
                aktivneAdrese.add(a.getAdresa());
            }
        }

        // sortiraj abecedno
        Collections.sort(aktivneAdrese, Collator.getInstance(new Locale("hr", "HR")));

        return aktivneAdrese;
    }

    /**
     * Metoda zabranjuje vrijednosti veće od 16 i manje od 1 da budu upisane kao
     * vrijednost dana za dohvaćanje meteo podataka
     *
     *  brojDana broj dana za dohvaćanje meteo podataka za adresu
     */
    public void setBrojDana(int brojDana) {
        if (brojDana > 16) {
            this.brojDana = 16;
        } else if (brojDana < 1) {
            this.brojDana = 1;
        } else {
            this.brojDana = brojDana;
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * ManagedBean klasa koja se koristi za pregledAdresa.xhtml
 */


public class PregledDnevnika extends BaseBean implements Serializable {

    private boolean zapisiPretrazivanje = false;
    private boolean zapisiFiltriranje = false;

    /**
     * Creates a new instance of PregledDnevnika
     */


    /**
     * Pretraživanje tablice sa dnevnikom na temelju upisane vrijednosti
     */
    private void pretraziDnevnik() {
        if (zapisiPretrazivanje) {
            vrijemePozivanjaMetode = System.currentTimeMillis();
        }
        if (rijecZaPretrazivanje == null || rijecZaPretrazivanje.isEmpty()) {
            return;
        }
        dnevnik.removeIf(s -> !s.getKorisnik().contains(rijecZaPretrazivanje));

        if (zapisiPretrazivanje) {
            zapisiUDnevnik(vrijemePozivanjaMetode, 200);
            zapisiPretrazivanje = false;
        }
    }

    /**
     * Filtriranje tablice sa dnevnikom na temelju upisanih parametara
     */
    private void filtrirajDnevnik() {
        if (zapisiFiltriranje) {
            vrijemePozivanjaMetode = System.currentTimeMillis();
        }

        // provjeri jeli upisana IP adresa
        if (!(ipAdresa == null || ipAdresa.isEmpty())) {
            dnevnik.removeIf(s -> !s.getIpadresa().contains(ipAdresa));
        }

        // provjeri jeli upisano trajanje
        if (trajanje != 0) {
            dnevnik.removeIf(i -> i.getTrajanje() != trajanje);
        }

        // provjeri jeli upisan status
        if (status != 0) {
            dnevnik.removeIf(i -> i.getStatus() != status);
        }

        // provjeri jeli upisan datum od
        if (!(odDatuma == null || odDatuma.isEmpty())) {
            Date od = new Date(Long.parseLong(odDatuma) * 1000);
            dnevnik.removeIf(d -> d.getVrijeme().before(od));
        }

        // provjeri jeli upisan datum do
        if (!(doDatuma == null || doDatuma.isEmpty())) {
            Date dod = new Date(Long.parseLong(doDatuma) * 1000);
            dnevnik.removeIf(d -> d.getVrijeme().after(dod));
        }

        if (zapisiFiltriranje) {
            zapisiUDnevnik(vrijemePozivanjaMetode, 200);
            zapisiFiltriranje = false;
        }
    }

    /**
     * Uključivanje zapisivanja pretrazivanja u dnevnik, služi kako se nebi svaki put pisalo
     * prilikom osvježavanja stranice
     */
    public void ukljuciZapisivanjePretrazivanja() {
        zapisiPretrazivanje = true;
    }

    /**
     * Uključivanje zapisivanja filtriranja u dnevnik, služi kako se nebi svaki put pisalo
     * prilikom osvježavanja stranice
     */
    public void ukljuciZapisivanjeFiltriranja() {
        zapisiFiltriranje = true;
    }

    public List<Dnevnik> getDnevnik() {
        if (dnevnik == null) {
            dnevnik = new ArrayList<>();
        }

        dnevnik = dnevnikFacade.findAll();
        // provjeri dali treba pretraziti ili filtrirati
        pretraziDnevnik();
        filtrirajDnevnik();
        return dnevnik;
    }
}