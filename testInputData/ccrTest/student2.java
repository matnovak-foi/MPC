/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Klasa koja predstavlja entitet Dnevnik
 * @author student13
 */
@Entity
@Table(name = "DNEVNIK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dnevnik.findAll", query = "SELECT d FROM Dnevnik d")
    , @NamedQuery(name = "Dnevnik.findById", query = "SELECT d FROM Dnevnik d WHERE d.id = :id")
    , @NamedQuery(name = "Dnevnik.findByKorisnik", query = "SELECT d FROM Dnevnik d WHERE d.korisnik = :korisnik")
    , @NamedQuery(name = "Dnevnik.findByUrl", query = "SELECT d FROM Dnevnik d WHERE d.url = :url")
    , @NamedQuery(name = "Dnevnik.findByIpadresa", query = "SELECT d FROM Dnevnik d WHERE d.ipadresa = :ipadresa")
    , @NamedQuery(name = "Dnevnik.findByVrijeme", query = "SELECT d FROM Dnevnik d WHERE d.vrijeme = :vrijeme")
    , @NamedQuery(name = "Dnevnik.findByTrajanje", query = "SELECT d FROM Dnevnik d WHERE d.trajanje = :trajanje")
    , @NamedQuery(name = "Dnevnik.findByStatus", query = "SELECT d FROM Dnevnik d WHERE d.status = :status")})
public class Dnevnik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "KORISNIK")
    private String korisnik;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "URL")
    private String url;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "IPADRESA")
    private String ipadresa;
    @Column(name = "VRIJEME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRAJANJE")
    private int trajanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;

    /**
     * Osnovni konstruktor
     */
    public Dnevnik() {
    }

    /**
     * Konstruktor koji prima argument id tipa integer
     * @param id 
     */
    public Dnevnik(Integer id) {
        this.id = id;
    }

    /**
     * Konstruktor koji prima argumente id, korisnik, url, ipadresa, vrijeme, trajanje i status
     * @param id
     * @param korisnik
     * @param url
     * @param ipadresa
     * @param vrijeme
     * @param trajanje
     * @param status 
     */
    public Dnevnik(Integer id, String korisnik, String url, String ipadresa, Date vrijeme, int trajanje, int status) {
        this.id = id;
        this.korisnik = korisnik;
        this.url = url;
        this.ipadresa = ipadresa;
        this.trajanje = trajanje;
        this.status = status;
        this.vrijeme = vrijeme;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpadresa() {
        return ipadresa;
    }

    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
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

    @Override
    public String toString() {
        return "org.foi.nwtis.student13.ejb.eb.Dnevnik[ id=" + id + " ]";
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Klasa koja predstavlja entitet Meteo
 * @author student13
 */
@Entity
@Table(name = "METEO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meteo.findAll", query = "SELECT m FROM Meteo m")
    , @NamedQuery(name = "Meteo.findByIdmeteo", query = "SELECT m FROM Meteo m WHERE m.idmeteo = :idmeteo")
    , @NamedQuery(name = "Meteo.findByAdresastanice", query = "SELECT m FROM Meteo m WHERE m.adresastanice = :adresastanice")
    , @NamedQuery(name = "Meteo.findByLatitude", query = "SELECT m FROM Meteo m WHERE m.latitude = :latitude")
    , @NamedQuery(name = "Meteo.findByLongitude", query = "SELECT m FROM Meteo m WHERE m.longitude = :longitude")
    , @NamedQuery(name = "Meteo.findByVrijeme", query = "SELECT m FROM Meteo m WHERE m.vrijeme = :vrijeme")
    , @NamedQuery(name = "Meteo.findByVrijemeopis", query = "SELECT m FROM Meteo m WHERE m.vrijemeopis = :vrijemeopis")
    , @NamedQuery(name = "Meteo.findByTemp", query = "SELECT m FROM Meteo m WHERE m.temp = :temp")
    , @NamedQuery(name = "Meteo.findByTempmin", query = "SELECT m FROM Meteo m WHERE m.tempmin = :tempmin")
    , @NamedQuery(name = "Meteo.findByTempmax", query = "SELECT m FROM Meteo m WHERE m.tempmax = :tempmax")
    , @NamedQuery(name = "Meteo.findByVlaga", query = "SELECT m FROM Meteo m WHERE m.vlaga = :vlaga")
    , @NamedQuery(name = "Meteo.findByTlak", query = "SELECT m FROM Meteo m WHERE m.tlak = :tlak")
    , @NamedQuery(name = "Meteo.findByVjetar", query = "SELECT m FROM Meteo m WHERE m.vjetar = :vjetar")
    , @NamedQuery(name = "Meteo.findByVjetarsmjer", query = "SELECT m FROM Meteo m WHERE m.vjetarsmjer = :vjetarsmjer")
    , @NamedQuery(name = "Meteo.findByPreuzeto", query = "SELECT m FROM Meteo m WHERE m.preuzeto = :preuzeto")})
public class Meteo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDMETEO")
    private Integer idmeteo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ADRESASTANICE")
    private String adresastanice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private float longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "VRIJEME")
    private String vrijeme;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "VRIJEMEOPIS")
    private String vrijemeopis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TEMP")
    private double temp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TEMPMIN")
    private double tempmin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TEMPMAX")
    private double tempmax;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VLAGA")
    private double vlaga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TLAK")
    private double tlak;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VJETAR")
    private double vjetar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VJETARSMJER")
    private double vjetarsmjer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PREUZETO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date preuzeto;
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Parkiralista id;

    /**
     * Osnovni konstruktor
     */
    public Meteo() {
    }

    /**
     * Konstruktor koji prima argument idmeteo tipa integer
     * @param idmeteo 
     */
    public Meteo(Integer idmeteo) {
        this.idmeteo = idmeteo;
    }

    /**
     * Konstruktor koji prima argumente idmeteo, adresastanice, latitude, longitude, 
     * vrijeme, vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, preuzeto
     * @param idmeteo
     * @param adresastanice
     * @param latitude
     * @param longitude
     * @param vrijeme
     * @param vrijemeopis
     * @param temp
     * @param tempmin
     * @param tempmax
     * @param vlaga
     * @param tlak
     * @param vjetar
     * @param vjetarsmjer
     * @param preuzeto 
     */
    public Meteo(Integer idmeteo, String adresastanice, float latitude, float longitude, String vrijeme, String vrijemeopis, double temp, double tempmin, double tempmax, double vlaga, double tlak, double vjetar, double vjetarsmjer, Date preuzeto) {
        this.idmeteo = idmeteo;
        this.adresastanice = adresastanice;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vrijeme = vrijeme;
        this.vrijemeopis = vrijemeopis;
        this.temp = temp;
        this.tempmin = tempmin;
        this.tempmax = tempmax;
        this.vlaga = vlaga;
        this.tlak = tlak;
        this.vjetar = vjetar;
        this.vjetarsmjer = vjetarsmjer;
        this.preuzeto = preuzeto;
    }

    public Integer getIdmeteo() {
        return idmeteo;
    }

    public void setIdmeteo(Integer idmeteo) {
        this.idmeteo = idmeteo;
    }

    public String getAdresastanice() {
        return adresastanice;
    }

    public void setAdresastanice(String adresastanice) {
        this.adresastanice = adresastanice;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getVrijemeopis() {
        return vrijemeopis;
    }

    public void setVrijemeopis(String vrijemeopis) {
        this.vrijemeopis = vrijemeopis;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTempmin() {
        return tempmin;
    }

    public void setTempmin(double tempmin) {
        this.tempmin = tempmin;
    }

    public double getTempmax() {
        return tempmax;
    }

    public void setTempmax(double tempmax) {
        this.tempmax = tempmax;
    }

    public double getVlaga() {
        return vlaga;
    }

    public void setVlaga(double vlaga) {
        this.vlaga = vlaga;
    }

    public double getTlak() {
        return tlak;
    }

    public void setTlak(double tlak) {
        this.tlak = tlak;
    }

    public double getVjetar() {
        return vjetar;
    }

    public void setVjetar(double vjetar) {
        this.vjetar = vjetar;
    }

    public double getVjetarsmjer() {
        return vjetarsmjer;
    }

    public void setVjetarsmjer(double vjetarsmjer) {
        this.vjetarsmjer = vjetarsmjer;
    }

    public Date getPreuzeto() {
        return preuzeto;
    }

    public void setPreuzeto(Date preuzeto) {
        this.preuzeto = preuzeto;
    }

    public Parkiralista getId() {
        return id;
    }

    public void setId(Parkiralista id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmeteo != null ? idmeteo.hashCode() : 0);
        return hash;
    }

    @Override
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

    @Override
    public String toString() {
        return "org.foi.nwtis.student13.ejb.eb.Meteo[ idmeteo=" + idmeteo + " ]";
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.ejb.eb;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author student13
 */
@Entity
@Table(name = "PARKIRALISTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parkiralista.findAll", query = "SELECT p FROM Parkiralista p")
    , @NamedQuery(name = "Parkiralista.findById", query = "SELECT p FROM Parkiralista p WHERE p.id = :id")
    , @NamedQuery(name = "Parkiralista.findByNaziv", query = "SELECT p FROM Parkiralista p WHERE p.naziv = :naziv")
    , @NamedQuery(name = "Parkiralista.findByAdresa", query = "SELECT p FROM Parkiralista p WHERE p.adresa = :adresa")
    , @NamedQuery(name = "Parkiralista.findByLatitude", query = "SELECT p FROM Parkiralista p WHERE p.latitude = :latitude")
    , @NamedQuery(name = "Parkiralista.findByLongitude", query = "SELECT p FROM Parkiralista p WHERE p.longitude = :longitude")})
public class Parkiralista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 99)
    @Column(name = "NAZIV")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ADRESA")
    private String adresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private float longitude;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private List<Meteo> meteoList;

    /**
     * Osnovni konstruktor
     */
    public Parkiralista() {
    }

    /**
     * Konstruktor koji prima argument id
     * @param id 
     */
    public Parkiralista(Integer id) {
        this.id = id;
    }

    /**
     * Konstruktor koji prima argumente id, naziv, adresa, latitude, longitude
     * @param id
     * @param naziv
     * @param adresa
     * @param latitude
     * @param longitude 
     */
    public Parkiralista(Integer id, String naziv, String adresa, float latitude, float longitude) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @XmlTransient
    public List<Meteo> getMeteoList() {
        return meteoList;
    }

    public void setMeteoList(List<Meteo> meteoList) {
        this.meteoList = meteoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
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

    @Override
    public String toString() {
        return "org.foi.nwtis.student13.ejb.eb.Parkiralista[ id=" + id + " ]";
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.ejb.sb;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Abstraktna klasa fasade
 * @author student13
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    /**
     * Konstruktor koji prima entitetnu klasu
     * @param entityClass 
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Vraca objekt EntitiyManager-a
     * @return 
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Kreira prosljedeni entitet
     * @param entity 
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Ažurira prosljedeni entitet
     * @param entity 
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Briše prosljedeni entitet
     * @param entity 
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Pronalazi entitet na temelju id-a
     * @param id
     * @return 
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Vraca sve zapise entiteta u obliku liste
     * @return 
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Pronalazi i vraca listu entiteta na temelju prosljedenog raspona
     * @param range
     * @return 
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
     * @return 
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
package org.foi.nwtis.student13.ejb.sb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.student13.ejb.eb.Dnevnik;

/**
 * Klasa fasade dnevnika
 *
 * @author student13
 */
@Stateless
public class DnevnikFacade extends AbstractFacade<Dnevnik> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Osnovni konstruktor
     */
    public DnevnikFacade() {
        super(Dnevnik.class);
    }

    /**
     * Dohvaca podatke entiteta dnevnika na temelju prosljedenih argumenata.
     * Koristi se Criteria API.
     *
     * @param ipAdresa
     * @param datumOd
     * @param datumDo
     * @param adresaZahtjeva
     * @param trajanje
     * @return
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
package org.foi.nwtis.student13.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student13.ejb.eb.Meteo;

/**
 * Klasa fasade Meteo
 * @author student13
 */
@Stateless
public class MeteoFacade extends AbstractFacade<Meteo> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Osnovni konstruktor
     */
    public MeteoFacade() {
        super(Meteo.class);
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student13.ejb.eb.Parkiralista;

/**
 * Klasa fasade Parkiralista
 * @author student13
 */
@Stateless
public class ParkiralistaFacade extends AbstractFacade<Parkiralista> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Osnovni konstruktor
     */
    public ParkiralistaFacade() {
        super(Parkiralista.class);
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.student13.rest.klijenti.GMKlijent;
import org.foi.nwtis.student13.rest.klijenti.OWMKlijentPrognoza;
import org.foi.nwtis.student13.web.podaci.Lokacija;
import org.foi.nwtis.student13.web.podaci.MeteoPrognoza;

/**
 * Klasa MeteoKlijentZrno služi za dohvacanje lokacije i meteorološke prognoze
 * @author student13
 */
@Stateless
@LocalBean
public class MeteoKlijentZrno {

    private String apiKey;
    private String gmApiKey;

    /**
     * Postavlja korisničke podatke (apiKey - openWeatherMap i gmApiKey - googleMaps)
     * @param apiKey
     * @param gmApiKey 
     */
    public void postaviKoorisnickePodatke(String apiKey, String gmApiKey) {
        this.apiKey = apiKey;
        this.gmApiKey = gmApiKey;
    }

    /**
     * Dohvaca i vraca lokaciju na temelju prosljedene adrese
     * @param adresa
     * @return Lokacija
     */
    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent(gmApiKey);
        Lokacija lokacija = gmk.getGeoLocation(adresa);
        return lokacija;
    }
   
    /**
     * Dohvaca i vraca prognozu vremena na temelju prosljedene adrese.
     * @param id
     * @param adresa
     * @return MeteoPrognoza[] 
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
package org.foi.nwtis.student13.rest.klijenti;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student13.web.podaci.Lokacija;

/**
 *
 * @author nwtis_1
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

package org.foi.nwtis.student13.rest.klijenti;

/**
 *
 * @author teacher2
 */
public class GMRESTHelper {
    private static final String GM_BASE_URI = "https://maps.google.com/";    

    public GMRESTHelper() {
    }

    public static String getGM_BASE_URI() {
        return GM_BASE_URI;
    }
        
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.rest.klijenti;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
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
import org.foi.nwtis.student13.web.podaci.MeteoPodaci;

/**
 *
 * @author nwtis_1
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
     * @param latitude
     * @param longitude
     * @return List<MeteoPodaci>
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
     * @param latitude
     * @param longitude
     * @return List<MeteoPodaci>
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
package org.foi.nwtis.student13.rest.klijenti;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foi.nwtis.student13.web.podaci.MeteoPodaci;
import org.foi.nwtis.student13.web.podaci.MeteoPrognoza;

/**
 * Klasa za poziv api-a za dohvacanje prognoze vremena
 * @author student13
 */
public class OWMKlijentPrognoza extends OWMKlijent{

    /**
     * Osnovni konstruktor, prima argument api ključa za openweathermap
     * @param apiKey 
     */
    public OWMKlijentPrognoza(String apiKey) {
        super(apiKey);
    }
    
    /**
     * Poziva metodu za korištenje api-a za dovacanje prognoze. 
     * Vraca polje dohvacenih meteo prognoza za odredeno parkiralište.
     * @param id
     * @param latitude
     * @param longitude
     * @return MeteoPrognoza[]
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

package org.foi.nwtis.student13.rest.klijenti;

/**
 *
 * @author teacher2
 */
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
        
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.podaci;

/**
 *
 * @author teacher2
 */
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
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.podaci;

import java.util.Date;

/**
 *
 * @author teacher2
 */
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

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.podaci;

/**
 *
 * @author teacher2
 */
public class MeteoPrognoza {

    private int id;
    private int sat;
    private MeteoPodaci prognoza;

    public MeteoPrognoza() {
    }

    public MeteoPrognoza(int id, int dan, MeteoPodaci prognoza) {
        this.id = id;
        this.sat = dan;
        this.prognoza = prognoza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public MeteoPodaci getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(MeteoPodaci prognoza) {
        this.prognoza = prognoza;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student13.web.podaci;


/**
 *
 * @author teacher2
 */
public class Parkiraliste {
    private int id;
    private String naziv;
    private String adresa;
    private Lokacija geoloc;

    public Parkiraliste() {
    }

    public Parkiraliste(int id, String naziv, String adresa, Lokacija geoloc) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

    public Lokacija getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }      
	
    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }        
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.kontrole;

import java.util.Date;

/**
 * Klasa dnevnika
 * @author student13
 */
public class Dnevnik {

    private int id;
    private String korisnik;
    private String url;
    private String ipAdresa;
    private Date vrijemeZapisa;
    private long trajanje;
    private String status;

    /**
     * Konstruktor koji prima argument id, korisnik, url, ipadresa, vrijemezapisa, trajanje i status
     * @param id
     * @param korisnik
     * @param url
     * @param ipAdresa
     * @param vrijemeZapisa
     * @param trajanje
     * @param status 
     */
    public Dnevnik(int id, String korisnik, String url, String ipAdresa, Date vrijemeZapisa, long trajanje, String status) {
        this.id = id;
        this.korisnik = korisnik;
        this.url = url;
        this.ipAdresa = ipAdresa;
        this.vrijemeZapisa = vrijemeZapisa;
        this.trajanje = trajanje;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public Date getVrijemeZapisa() {
        return vrijemeZapisa;
    }

    public void setVrijemeZapisa(Date vrijemeZapisa) {
        this.vrijemeZapisa = vrijemeZapisa;
    }

    public long getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(long trajanje) {
        this.trajanje = trajanje;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }       
}
package org.foi.nwtis.student13.web.kontrole;

/**
 * Klasa izbornika
 * @author student13
 */
public class Izbornik {
    private String labela;
    private String vrijednost;

    /**
     * Konstruktor koji prima argumente labela i vrijednost
     * @param labela
     * @param vrijednost 
     */
    public Izbornik(String labela, String vrijednost) {
        this.labela = labela;
        this.vrijednost = vrijednost;
    }

    public String getLabela() {
        return labela;
    }

    public void setLabela(String labela) {
        this.labela = labela;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }   

    @Override
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

    @Override
    public String toString() {
        return "Izbornik{" + "labela=" + labela + ", vrijednost=" + vrijednost + '}';
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.podaci;

/**
 *
 * @author teacher2
 */
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
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.podaci;

import java.util.Date;

/**
 *
 * @author teacher2
 */
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

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.podaci;

/**
 * POJO klasa koja sadrži podatke parkirališta i njegovih meteoroloških podataka.
 * Služi za jednostavnije korištenje i prikaz korisniku
 * @author student13
 */
public class MeteoPodaciParkiraliste extends Parkiraliste{   
    private MeteoPodaci meteoPodaci;

    /**
     * Konstruktor koji prima argumente id, naziv, adresa, geolokacija i meteopodaci
     * Poziva konstruktor nasljedene klase Parkiraliste
     * @param id
     * @param naziv
     * @param adresa
     * @param geoloc
     * @param meteoPodaci 
     */
    public MeteoPodaciParkiraliste(int id, String naziv, String adresa, Lokacija geoloc, MeteoPodaci meteoPodaci) {
        super(id, naziv, adresa, geoloc);
        this.meteoPodaci = meteoPodaci;
    }

    public MeteoPodaci getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(MeteoPodaci meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }
    
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.podaci;

/**
 *
 * @author teacher2
 */
public class MeteoPrognoza {

    private int id;
    private int sat;
    private MeteoPodaci prognoza;

    public MeteoPrognoza() {
    }

    public MeteoPrognoza(int id, int dan, MeteoPodaci prognoza) {
        this.id = id;
        this.sat = dan;
        this.prognoza = prognoza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public MeteoPodaci getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(MeteoPodaci prognoza) {
        this.prognoza = prognoza;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student13.web.podaci;


/**
 *
 * @author teacher2
 */
public class Parkiraliste {
    private int id;
    private String naziv;
    private String adresa;
    private Lokacija geoloc;

    public Parkiraliste() {
    }

    public Parkiraliste(int id, String naziv, String adresa, Lokacija geoloc) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

    public Lokacija getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }      
	
    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }        
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.student13.konfiguracije.Konfiguracija;
import org.foi.nwtis.student13.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student13.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.student13.konfiguracije.NemaKonfiguracije;


/**
 * Slušač aplikacije
 *
 * @author student13
 */
public class SlusacAplikacije implements ServletContextListener {

    public static ServletContext servletContext;

    /**
     * Metoda za inicijaliziranje konteksta aplikacije
     *
     * @param sce ServletContextEvent
     */
    @Override
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
     * @param sce ServletContextEvent
     */
    @Override
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
package org.foi.nwtis.student13.web.zrna;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.student13.ejb.eb.Dnevnik;
import org.foi.nwtis.student13.ejb.sb.DnevnikFacade;

/**
 * Klasa koja implementira sučelje Filter.
 * Služi za presretanje Servlet zahtjeva
 * @author student13
 */
@WebFilter(urlPatterns = {"*.xhtml"})
public class MyFilter implements Filter {

    @EJB
    DnevnikFacade dnevnikFacade;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Nadjačava metodu doFilter, mjeri koliko vremena prode od trenutka zahtjeva do trenutka odgovora.
     * Izvlači iz zahtjeva url tj. putanju, IP korisnika i status odgovora.
     * Izvučene podatke sprema u entite Dnevnika
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException 
     */
    @Override
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

    @Override
    public void destroy() {
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.student13.ejb.eb.Parkiralista;
import org.foi.nwtis.student13.ejb.sb.MeteoKlijentZrno;
import org.foi.nwtis.student13.ejb.sb.ParkiralistaFacade;
import org.foi.nwtis.student13.konfiguracije.Konfiguracija;
import org.foi.nwtis.student13.rest.klijenti.OWMKlijentPrognoza;
import org.foi.nwtis.student13.web.kontrole.Izbornik;
import org.foi.nwtis.student13.web.podaci.Lokacija;
import org.foi.nwtis.student13.web.podaci.MeteoPodaci;
import org.foi.nwtis.student13.web.podaci.MeteoPodaciParkiraliste;
import org.foi.nwtis.student13.web.podaci.MeteoPrognoza;

/**
 * Klasa Pregleda, služi za prikazivanje i rad s podacima parkirališta i
 * meteoroloških podataka
 *
 * @author student13
 */
@Named(value = "pregled")
@SessionScoped
public class Pregled implements Serializable {

    @EJB
    private ParkiralistaFacade parkiralistaFacade;

    @EJB
    private MeteoKlijentZrno meteoKlijentZrno;

    private Integer id;
    private String adresa;
    private String naziv;
    private List<Izbornik> popisParking = new ArrayList<>();
    private List<String> popisParkingOdabrano = new ArrayList<>();
    private List<Izbornik> popisParkingMeteo = new ArrayList<>();
    private List<String> popisParkingMeteoOdabrana = new ArrayList<>();
    private List<MeteoPodaciParkiraliste> popisMeteoPodaci = new ArrayList<>();

    private boolean dodajAzuriraj = false;      //Zastavica koja oznacava da li treba napraviti azuriranje liste parkiralista
    private boolean gumbUpisiPrikazi = false;          //Zastavica koja definira da li treba prikazati ili sakriti gumb upisi
    private boolean tablicaPrognozaPrikazi = false;    //Zastavica za prikazivanje/skrivanje tablice prognoze
    private String pogreska = "";
    private OWMKlijentPrognoza owmKlijentPrognoza;
    private String gumbPrognoza = "Prognoza";

    /**
     * Osnovni konstruktor
     */
    public Pregled() {
    }

    /**
     * Metoda koja se poziva prije poziva poslonih metoda Postavlja potrebne api
     * ključeve te kreira potrebne objekte za dohvat prognoze
     */
    @PostConstruct
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**
     * Dohaca popis parkirališta iz baze podataka. Ako je popis prazan, dohvaca
     * izravno iz baze podataka. Ako popis nije prazan provjerava da li je
     * podignuta zastavica za dodavanje/ažuriranje, ako je podignuta ažurira
     * listu parkirališta sukladno dodanim/ažuriranim parkiralištima. Obavlja
     * abecedno sortiranje parkirališta. Parkirališta sprema i listu Izbornika
     * koji se prikazuje korisniku
     *
     * @return List<Izbornik>
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
            @Override
            public int compare(Izbornik i1, Izbornik i2) {
                return i1.getLabela().compareTo(i2.getLabela());
            }
        });
        return popisParking;
    }

    public void setPopisParking(List<Izbornik> popisParking) {
        this.popisParking = popisParking;
    }

    public List<String> getPopisParkingOdabrano() {
        return popisParkingOdabrano;
    }

    public void setPopisParkingOdabrano(List<String> popisParkingOdabrano) {
        this.popisParkingOdabrano = popisParkingOdabrano;
    }

    /**
     * Vraca popis parkirališta za dohvacanje meteoroloških podataka abecedno
     * sortiranih
     *
     * @return
     */
    public List<Izbornik> getPopisParkingMeteo() {
        Collections.sort(popisParkingMeteo, new Comparator<Izbornik>() {
            @Override
            public int compare(Izbornik i1, Izbornik i2) {
                return i1.getLabela().compareTo(i2.getLabela());
            }
        });
        return popisParkingMeteo;
    }

    public void setPopisParkingMeteo(List<Izbornik> popisParkingMeteo) {
        this.popisParkingMeteo = popisParkingMeteo;
    }

    public List<String> getPopisParkingMeteoOdabrana() {
        return popisParkingMeteoOdabrana;
    }

    public void setPopisParkingMeteoOdabrana(List<String> popisParkingMeteoOdabrana) {
        this.popisParkingMeteoOdabrana = popisParkingMeteoOdabrana;
    }

    public List<MeteoPodaciParkiraliste> getPopisMeteoPodaci() {
        return popisMeteoPodaci;
    }

    public void setPopisMeteoPodaci(List<MeteoPodaciParkiraliste> popisMeteoPodaci) {
        this.popisMeteoPodaci = popisMeteoPodaci;
    }

    public boolean isGumbUpisiPrikazi() {
        return gumbUpisiPrikazi;
    }

    public void setGumbUpisiPrikazi(boolean gumbUpisiPrikazi) {
        this.gumbUpisiPrikazi = gumbUpisiPrikazi;
    }

    public String getPogreska() {
        return pogreska;
    }

    public void setPogreska(String pogreska) {
        this.pogreska = pogreska;
    }

    public boolean isTablicaPrognozaPrikazi() {
        return tablicaPrognozaPrikazi;
    }

    public void setTablicaPrognozaPrikazi(boolean tablicaPrognozaPrikazi) {
        this.tablicaPrognozaPrikazi = tablicaPrognozaPrikazi;
    }

    public String getGumbPrognoza() {
        return gumbPrognoza;
    }

    public void setGumbPrognoza(String gumbPrognoza) {
        this.gumbPrognoza = gumbPrognoza;
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student13.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.foi.nwtis.student13.ejb.eb.Dnevnik;
import org.foi.nwtis.student13.ejb.sb.DnevnikFacade;

/**
 * Klasa za prikaz i rad s podacima dnevnika
 * @author student13
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;
    private String ipAdresa;
    private String datumOd;
    private String datumDo;
    private String adresaZahtjeva;
    private String trajanje;
    private List<Dnevnik> zapisiDnevnika;
    private boolean prikaziTablicu = false;

    /**
     * Osnovni konstruktor
     */
    public PregledDnevnika() {
    }

    /**
     * Dohvaca podatke iz baze za dnevnik na temelju prosljedenih podataka putem forme
     * @return 
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

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public String getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(String datumOd) {
        this.datumOd = datumOd;
    }

    public String getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(String datumDo) {
        this.datumDo = datumDo;
    }

    public String getAdresaZahtjeva() {
        return adresaZahtjeva;
    }

    public void setAdresaZahtjeva(String adresaZahtjeva) {
        this.adresaZahtjeva = adresaZahtjeva;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public List<Dnevnik> getZapisiDnevnika() {
        return zapisiDnevnika;
    }

    public void setZapisiDnevnika(List<Dnevnik> zapisiDnevnika) {
        this.zapisiDnevnika = zapisiDnevnika;
    }

    public boolean isPrikaziTablicu() {
        return prikaziTablicu;
    }

    public void setPrikaziTablicu(boolean prikaziTablicu) {
        this.prikaziTablicu = prikaziTablicu;
    }
}
