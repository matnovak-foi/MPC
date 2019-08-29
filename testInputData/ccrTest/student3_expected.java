

/**
 *
 *  Sname1
 */

public class CitiesFacade extends AbstractFacade<Cities> {

    /**
     * Metoda koja korištenje Criteria API upita pretražuje gradove na temelju država. Također
     * sortira gradove prema njegovom nazivu, abecedno.
     *  drzava
     *  
     */
    public List<Cities> filtrirajGradove(Set<String> drzava){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Cities> gradovi = cq.from(Cities.class);
        cq.select(gradovi);
        cq.where(gradovi.<String>get("citiesPK").<String>get("state").in(drzava));
        cq.orderBy(cb.asc(gradovi.<String>get("name")));
        return em.createQuery(cq).getResultList();
    }
    /**
     * Metoda koja služi za filtriranje gradova prema njegovom nazivu
     *  drzava
     *  naziv
     *  
     */
    
    
}
public class StatesFacade extends AbstractFacade<States> {

    public List<States> filtrirajDrzave(String naziv){
        return em.createQuery(cq).getResultList();
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  Sname1
 */

public class ZipCodesFacade extends AbstractFacade<ZipCodes> {

        /**
         * Metoda koja putem Criteria API vraća zip kodove na temelju država. 
         *  grad
         *  
         */
        public List<ZipCodes> filtrirajZipKodove(Set<String> grad){
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<ZipCodes> zipovi = cq.from(ZipCodes.class);
        
        cq.select(zipovi);
        cq.where(zipovi.<String>get("cities").<String>get("citiesPK").<String>get("state").in(grad));
        return em.createQuery(cq).getResultList();

    }

}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Klasa koja se povezuje na WeatherBug servis i dohvaca meteo podatke.
 *  Sname1
 */


public class WeatherBugKlijent {
//private String mjWeatherBugKod = "A3776107717";
public static void setWb_code(String wb_code) {
        WeatherBugKlijent.wb_code = wb_code;
    }
    
    /**
     * Metoda koja dohvaca meteo podatke. Prosljeđuje joj se statička varijabla wb_code koja sadrži
     * WeatherBug API Code.
     *  zip
     *  
     */

}

/**
 * Klasa koja služi za dohvacanje država, gradova i zip kodova.
 *
 *  Sname1
 */


public class OdabiZipKodovaZaGradove implements Serializable {

    LiveWeatherData live = new LiveWeatherData();
    private ArrayList<LiveWeatherData> podaci = new ArrayList<LiveWeatherData>();
/**
     * Metoda koja dohvaća zip kodove na temelju odabranih država. Ispis je u
     * obliku ZIP - DRŽAVA - OKRUG - GRAD. Lista zip kodova se puni jedino ako
     * se nalazi barem jedan zapis u listi odabranih gradova.
     *
     * 
     */
    public Map<String, Object> getPopisZipKodova() {
        popisZipKodova = new TreeMap<String, Object>();
        if (odabraniGradovi == null || odabraniGradovi.isEmpty()) {
            return popisZipKodova;
        }
        List<ZipCodes> zipovi;


        if (filterZipKodova == null || filterZipKodova.trim().isEmpty()) {
            zipovi = zipCodesFacade.filtrirajZipKodove(odabraneDrzave.keySet());
        } else {

            zipovi = zipCodesFacade.filtrirajZipKodove(odabraneDrzave.keySet());
        }
        for (ZipCodes zip : zipovi) {
            String z = zip.getZip() + " - " + zip.getCities().getCitiesPK().getState()
                    + " - " + zip.getCities().getCitiesPK().getCounty() + " - "
                    + zip.getCities().getCitiesPK().getCity();


            popisZipKodova.put(z, z);
        }
        return popisZipKodova;
    }

    /**
     * Metoda koja služi za dohvaćanje gradova na temelju odabranih država.
     * Ispis gradova je u obliku DRŽAVA - OKRUG - GRAD. Implementirano je
     * filtiranje gradova prema nazivu grada. Ukoliko je filter prazan prikazuju
     * se svi gradovi na temelju odabranih država, a ukoliko filter sadrži
     * ključnu riječ prikazuju se gradovi koji sadrže tu ključnu riječ u nazivu
     * grada.
     *
     * 
     */
    public Map<String, Object> getPopisGradova() {
        popisGradova = new LinkedHashMap<String, Object>();
        if (odabraneDrzave == null || odabraneDrzave.isEmpty()) {
            return popisGradova;
        }

        List<Cities> gradovi;
}

    /**
     * Metoda kojom se dohvaćaju države. Ispisuju se sve države iz baze, te je
     * implementiran filter na temelju kojeg se može pretraživati država prema
     * ključnoj riječi.
     *
     * 
     */
    public Map<String, Object> getPopisDrzava() {
        popisDrzava = new TreeMap<String, Object>();
}
    /**
     * Creates a new instance of OdabiZipKodovaZaGradove
     */

    /**
     * Metoda koja služi za dodavanje država u listu odabranih država. Ukoliko
     * nije odabrana ni jedna država, ništa se ne radi, odnosno vraća se prazan
     * string.
     *
     * 
     */
    public String dodajDrzavu() {
        if (popisDrzavaOdabrano == null || popisDrzavaOdabrano.isEmpty()) {
            return "";
        }
        if (odabraneDrzave == null) {
            odabraneDrzave = new TreeMap<String, Object>();
        }
        for (String d : popisDrzavaOdabrano) {
            odabraneDrzave.put(d, d);
        }
        return "";
    }

    /**
     * Metoda koja služi za brisanje odabranih država.
     *
     * 
     */
    public String obrisiDrzavu() {
        if (odabraneDrzave != null && !odabraneDrzave.isEmpty() && odabraneDrzaveOdabrano != null && !odabraneDrzaveOdabrano.isEmpty()) {
            for (String d : odabraneDrzaveOdabrano) {
                odabraneDrzave.remove(d);
            }
        }
        return "";
    }

    /**
     * Metoda koja služi za dodavanje gradova u listu odabranih gradova. Ukoliko
     * nije odabran ni jedan grad, vraća se prazan string.
     *
     * 
     */
    public String dodajGrad() {
        if (popisGradovaOdabrano == null || popisGradovaOdabrano.isEmpty()) {
            return "";
        }
        if (odabraniGradovi == null) {
            odabraniGradovi = new TreeMap<String, Object>();
        }
        for (String d : popisGradovaOdabrano) {
            odabraniGradovi.put(d, d);
        }
        return "";
    }

    /**
     * Metoda koja služi za brisanje odabranih gradova.
     *
     * 
     */
    public String obrisiGrad() {
        if (odabraniGradovi != null && !odabraniGradovi.isEmpty()
                && odabraniGradoviOdabrano != null && !odabraniGradoviOdabrano.isEmpty()) {
            for (String g : odabraniGradoviOdabrano) {
                odabraniGradovi.remove(g);
            }
        }
        return "";
    }

    /**
     * Metoda koja služi za dohvaćanje gradova, te punjenje liste popisGradova,
     * na temelju odabranih država.
     *
     * 
     */
    public String preuzmiGradove() {
        popisGradova = new TreeMap<String, Object>();
        if (odabraneDrzave == null || odabraneDrzave.isEmpty()) {
            return "";
        }

        List<Cities> gradovi = citiesFacade.filtrirajGradove(odabraneDrzave.keySet());
        for (Cities grad : gradovi) {
            popisGradova.put(grad.getName(), grad.getName());
        }
        return "";
    }

    /**
     * Metoda koja služi za preuzimanje zip kodova, te punjenje liste
     * popisZipKodova, na temelju odabranih država.
     *
     * 
     */
    public String preuzmiZipKodove() {

        popisZipKodova = new TreeMap<String, Object>();
        if (odabraniGradovi == null || odabraniGradovi.isEmpty()) {
            return "";
        }
        //List<ZipCodes> zipovi = zipCodesFacade.findAll();
        List<ZipCodes> zipovi = zipCodesFacade.filtrirajZipKodove(odabraneDrzave.keySet());

        for (ZipCodes zip : zipovi) {
            popisZipKodova.put(zip.getZip().toString(), zip.getZip().toString());
        }

        return "";


    }

    /**
     * Metoda koja služi za dodavanje zip kodova u listu odabranih zip kodova.
     *
     * 
     */
    public String dodajZipKod() {
        if (popisZipKodovaOdabrano == null || popisZipKodovaOdabrano.isEmpty()) {
            return "";
        }
        if (odabraniZipKodovi == null) {
            odabraniZipKodovi = new TreeMap<String, Object>();
        }
        for (String z : popisZipKodovaOdabrano) {
            odabraniZipKodovi.put(z, z);
        }
        return "";
    }

    /**
     * Metoda koja služi za brisanje odabranih zip kodova.
     *
     * 
     */
    public String obrisiZipKod() {
        if (odabraniZipKodovi != null && !odabraniZipKodovi.isEmpty()
                && odabraniZipKodoviOdabrano != null && !odabraniZipKodoviOdabrano.isEmpty()) {
            for (String z : odabraniZipKodoviOdabrano) {
                odabraniZipKodovi.remove(z);
            }
        }
        return "";
    }

    /**
     * Metoda koja dohvaća WeatherBug code iz web.xml-a i sprema ga u varijablu
     * wb_code. Na temelju odabranih zip kodova prikazuje meteo podatke.
     *
     * 
     */
    public String preuzmiMeteoPodatke() {

        if (odabraniZipKodovi == null || odabraniZipKodovi.isEmpty()) {
            return "";
        }
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            wb_code = (String) env.lookup("wb_code");
            System.out.println("kod: " + wb_code);
        } catch (NamingException ex) {
            Logger.getLogger(WeatherBugKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }


        podaci.clear();
        List<Object> list = new ArrayList<Object>(odabraniZipKodovi.values());
        for (int i = 0; i < list.size(); i++) {
            live = weatherBugKlijent.dajMeteoPodatke(list.get(i).toString());
            podaci.add(live);

}


return "";
    }

    
}