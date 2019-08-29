
package org.foi.nwtis.student12.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.student12.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student12.web.slusaci.SlusacAplikacije;


public class Autentikacija {    

    
    public static boolean autentificiraj(String username, String password) {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        BP_Konfiguracija konfig = (BP_Konfiguracija) sc.getAttribute("BP_Konfiguracija");

        String pomEmail = null;
        String pomLozinka = null;
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnik = konfig.getUser_username();
        String lozinka = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        System.out.printf("URL:%s Korisnik:%s Lozinka:%s Driver:%s \n", url, korisnik, lozinka, driver);
        String upit = "SELECT EMAIL_ADRESA, LOZINKA FROM polaznici "
                + "WHERE EMAIL_ADRESA='" + username + "' AND LOZINKA='" + password + "'";

        try (
                Connection veza = DriverManager.getConnection(url, korisnik, lozinka);
                Statement instrukcija = veza.createStatement();
                ResultSet rs = instrukcija.executeQuery(upit);) {
            while (rs.next()) {
                pomEmail = rs.getString("EMAIL_ADRESA");
                pomLozinka = rs.getString("LOZINKA");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (pomEmail != null && pomLozinka != null) {
            return true;
        }
        return false;
    }

    
    public static boolean autentificirajMail(String username, String password) {        
        
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();

        String pomIme = null;
        String pomLozinka = null;
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnik = konfig.getUser_username();
        String lozinka = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        System.out.printf("URL:%s Korisnik:%s Lozinka:%s Driver:%s \n", url, korisnik, lozinka, driver);
        String upit = "SELECT KOR_IME, LOZINKA FROM polaznici "
                + "WHERE KOR_IME='" + username + "' AND LOZINKA='" + password + "'";

        try (
                Connection veza = DriverManager.getConnection(url, korisnik, lozinka);
                Statement instrukcija = veza.createStatement();
                ResultSet rs = instrukcija.executeQuery(upit);) {
            while (rs.next()) {
                pomIme = rs.getString("KOR_IME");
                pomLozinka = rs.getString("LOZINKA");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (pomIme != null && pomLozinka != null) {
            return true;
        }
        return false;        
    }
}

package org.foi.nwtis.student12.web.kontrole;

import java.util.Date;
import java.util.List;
import javax.mail.Flags;


public class Poruka {
    private String id;
    private Date vrijeme;
    private String salje;
    private String predmet;
    private String vrsta;
    private int velicina;    
    private int brojPrivitaka;
    private Flags zastavice;
    private List<PrivitakPoruke> privitciPoruke;
    private boolean brisati;
    private boolean procitano;
    private String sadrzaj;
    private String privitciPodaci = "";

    
    public Poruka(String id, Date poslano, String salje, String predmet, String vrsta, int velicina, int brojPrivitaka, Flags zastavice, List<PrivitakPoruke> privitciPoruke, boolean brisati, boolean procitano, String sadrzajPoruke) {
        this.id = id;
        this.vrijeme = poslano;
        this.salje = salje;
        this.predmet = predmet;
        this.vrsta = vrsta;
        this.velicina = velicina;
        this.brojPrivitaka = brojPrivitaka;
        this.zastavice = zastavice;
        this.privitciPoruke = privitciPoruke;
        this.brisati = brisati;
        this.procitano = procitano;        
        this.sadrzaj = sadrzajPoruke;
    }

    public String getId() {
        return id;
    }

    public boolean isBrisati() {
        return brisati;
    }

    public void setBrisati(boolean brisati) {
        this.brisati = brisati;
    }

    public int getBrojPrivitaka() {
        return brojPrivitaka;
    }

    public Flags getZastavice() {
        return zastavice;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public String getPredmet() {
        return predmet;
    }

    public List<PrivitakPoruke> getPrivitciPoruke() {
        return privitciPoruke;
    }

    public boolean isProcitano() {
        return procitano;
    }

    public void setProcitano(boolean procitano) {
        this.procitano = procitano;
    }
    
    public String getSalje() {
        return salje;
    }

    public String getVrsta() {
        return vrsta;
    }

    public int getVelicina() {
        return velicina;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public String getPrivitciPodaci() {
        for (PrivitakPoruke pp : privitciPoruke) {
            if (pp.getDatoteka() == null) {
                privitciPodaci += "";
            } else {
                privitciPodaci += pp.getDatoteka() + " (" + pp.getVrsta() + ", " + pp.getVelicina() + "kb)<br/> ";                
            }            
        } 
        if (privitciPodaci.equals("")) privitciPodaci = "-/-";
        return privitciPodaci;
    }

    
}

package org.foi.nwtis.student12.web.kontrole;


public class PrivitakPoruke {

    private int broj;
    private String vrsta;
    private int velicina;
    private String datoteka;

    
    public PrivitakPoruke(int broj, String vrsta, int velicina, String datoteka) {
        this.broj = broj;
        this.vrsta = vrsta;
        this.velicina = velicina;
        this.datoteka = datoteka;
    }

    

    public int getBroj() {
        return broj;
    }

    public String getDatoteka() {
        return datoteka;
    }

    public int getVelicina() {
        return velicina;
    }

    public String getVrsta() {
        return vrsta;
    }
    
    
}

package org.foi.nwtis.student12.web.kontrole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProvjeraPoruke {
    private static String korisnickaGalerija;

    
    public static boolean provjeri(String ulaz) {
        String sintaksa = "USER\\s*([^\\s]+)\\s*PASSWORD\\s*([^\\s]+)\\s*GALERY\\s*([^\\s]+)";
        String p = ulaz.trim();

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();

        if (status) {
            System.out.println("Regex prosao");
            if (Autentikacija.autentificirajMail(m.group(1), m.group(2))) {
                korisnickaGalerija = m.group(3);
                System.out.println("Prosla autentikacija korisnika maila");
                return true;                
            } else {
                System.out.println("Autentikacija korisnika iz maila nije prosla");
                return false;
            }
        } else {
            System.out.println("Regex nije prosao");
            return false;
        }

    }

    public static String getKorisnickaGalerija() {
        return korisnickaGalerija;
    }    
    
}

package org.foi.nwtis.student12.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.student12.konfiguracije.Konfiguracija;
import org.foi.nwtis.student12.web.kontrole.ProvjeraPoruke;
import org.foi.nwtis.student12.web.zrna.SlanjePoruke;


public class ObradaPoruka implements Runnable {

    private int interval;
    private Konfiguracija konfig;
    private String emailPosluzitelj;
    private String emailPort;
    private String emailKorisnik;
    private String emailLozinka;
    private String trazeniPredmet;    
    private String mapaNeispravne;
    private String mapaOstale;
    private String mapaIspravne;
    private String adresaDretve;
    private String lozinkaDretve;
    private String adresaPrimateljaDretve;
    private String predmetDretve;
    private String izlaznaMapa;
    private String sadrzajPorukeDretve;
    private volatile Thread trenutnaDretva;
    private boolean textIsHtml = false;
    private List<Part> prilozi = new ArrayList<>();
    private String galleryPath;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");
    private String vrijemePocetka;
    private String vrijemeZavrsetka;
    private String vrijemeObrade;
    private int sveukupanBrojPoruka = 0;
    private int ukupanBrojPoruka;
    private int brojIspravnihPoruka;
    private int brojNeispravnihPoruka;
    private int brojOstalihPoruka;
    private int brojPreuzetihDatoteka;    
    
    
    public ObradaPoruka() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        konfig = (Konfiguracija) sc.getAttribute("Konfiguracija");
        
        interval = Integer.parseInt(konfig.dajPostavku("interval"));
        emailPosluzitelj = konfig.dajPostavku("adresaPosluzitelja");
        emailPort = konfig.dajPostavku("emailPort");
        emailKorisnik = konfig.dajPostavku("korisnickoIme");
        emailLozinka = konfig.dajPostavku("emailLozinka");
        trazeniPredmet = konfig.dajPostavku("trazeniPredmet");        
        mapaNeispravne = konfig.dajPostavku("mapaNeispravne");
        mapaIspravne = konfig.dajPostavku("mapaIspravne");
        mapaOstale = konfig.dajPostavku("mapaOstale");
        galleryPath = sc.getRealPath("WEB-INF") + "\\" + konfig.dajPostavku("dirGalerija");
        adresaDretve = konfig.dajPostavku("adresaDretve");
        adresaPrimateljaDretve = konfig.dajPostavku("adresaPrimateljaDretve");
        predmetDretve = konfig.dajPostavku("predmetDretve");
        lozinkaDretve = konfig.dajPostavku("lozinkaDretve");
        izlaznaMapa = konfig.dajPostavku("mapaIzlazne");
    }

    
    @Override
    public synchronized void run() {
        trenutnaDretva = Thread.currentThread();
        while (!trenutnaDretva.isInterrupted()) {
            processMail();
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                trenutnaDretva.interrupt();
            }
        }
    }

    
    public void zaustaviDretvu() {
        trenutnaDretva.interrupt();
    }

    
    private void printData(String data) {
        System.out.println(data);
    }

    
    public void processMail() {
        Date poc = new Date();
        ukupanBrojPoruka = 0;
        brojIspravnihPoruka = 0;
        brojNeispravnihPoruka = 0;
        brojOstalihPoruka = 0;
        brojPreuzetihDatoteka = 0;
        
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;
        Object messagecontentObject = null;
        String sender = null;
        String subject = null;
        Multipart multipart = null;
        Part part = null;
        String contentType = null;

        try {
            printData("--------------processing mails started-----------------");
            session = Session.getDefaultInstance(System.getProperties(), null);

            printData("getting the session for accessing email.");
            store = session.getStore("imap");

            store.connect(emailPosluzitelj, emailKorisnik, emailLozinka);
            printData("Connection established with IMAP server.");

            
            folder = store.getDefaultFolder();

            Folder folderIspravne = folder.getFolder(mapaIspravne);
            folderIspravne.create(Folder.HOLDS_MESSAGES);

            Folder folderNeispravne = folder.getFolder(mapaNeispravne);
            folderNeispravne.create(Folder.HOLDS_MESSAGES);

            Folder folderOstale = folder.getFolder(mapaOstale);
            folderOstale.create(Folder.HOLDS_MESSAGES);

            printData("Getting the Inbox folder.");

            
            folder = folder.getFolder("inbox");


            
            folder.open(Folder.READ_WRITE);

            
            messages = folder.getMessages();
            
            sveukupanBrojPoruka += messages.length;
            ukupanBrojPoruka += messages.length;

            
            for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                
                message = messages[messageNumber];

                
                messagecontentObject = message.getContent();

                
                if (messagecontentObject instanceof Multipart) {
                    printData("Found Email with Attachment");
                    sender = ((InternetAddress) message.getFrom()[0]).getPersonal();

                    
                    printData("If the personal information has no entry, check the address for the sender information.");

                    if (sender == null) {
                        sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                        printData("sender in NULL. Printing Address:" + sender);
                    }
                    printData("Sender -." + sender);

                    
                    subject = message.getSubject();

                    printData("subject=" + subject);

                    
                    multipart = (Multipart) message.getContent();

                    printData("Retrieve the Multipart object from the message");

                    
                    for (int i = 0; i < multipart.getCount(); i++) {
                        
                        part = multipart.getBodyPart(i);

                        
                        contentType = part.getContentType();

                        
                        printData("Content: " + contentType);

                        if (contentType.startsWith("text/plain") || contentType.startsWith("TEXT/PLAIN")) {
                            printData("---------reading content type text/plain  mail -------------");
                        } else {
                            
                            brojPreuzetihDatoteka++;
                            String fileName = part.getFileName();
                            prilozi.add(part);
                            printData("retrive the fileName=" + fileName);                            
                        }
                    }
                } else {
                    printData("Found Mail Without Attachment");
                    sender = ((InternetAddress) message.getFrom()[0]).getPersonal();

                    
                    printData("If the personal information has no entry, check the address for the sender information.");

                    if (sender == null) {
                        sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                        printData("sender in NULL. Printing Address:" + sender);
                    }

                    
                    subject = message.getSubject();
                    printData("subject=" + subject);

                }
                if (subject.startsWith(trazeniPredmet)) {
                    System.out.println("Trazeni predmet pronadjen");
                    if (ProvjeraPoruke.provjeri(getText(message).replaceAll("[\r\n]+", " "))) {
                        brojIspravnihPoruka++;
                        System.out.println("Prošlo testiranje sadržaja i autentifikaciju!");
                        Message[] pomMessArray = {message};
                        folderIspravne.open(Folder.READ_WRITE);
                        folder.copyMessages(pomMessArray, folderIspravne);
                        message.setFlag(Flags.Flag.DELETED, true);
                        System.out.println("Poruka prebacena u mapu Ispravne, prilog ide u: " + galleryPath + "\\" + ProvjeraPoruke.getKorisnickaGalerija());

                        for (Part p : prilozi) {
                            String fn = p.getFileName();
                            System.out.println("Ime fajla: " + fn);
                            
                            File f = new File(galleryPath + "\\" + ProvjeraPoruke.getKorisnickaGalerija() + "\\" + p.getFileName());
                            
                            if (f.exists()) {
                                f.delete();
                            }
                            f.getParentFile().mkdirs();
                            f.setWritable(true);
                            f.setReadable(true);
                            
                            FileOutputStream output = new FileOutputStream(f);
                            InputStream input = p.getInputStream();
                            byte[] buffer = new byte[4096];
                            int byteRead;
                            while ((byteRead = input.read(buffer)) != -1) {
                                output.write(buffer, 0, byteRead);
                            }
                            output.close();
                        }

                        prilozi.clear();
                        folderIspravne.close(true);
                    } else {
                        brojNeispravnihPoruka++;
                        System.out.println("Poruka nije prosla testiranje sadrzaja i autentifikaciju");
                        prilozi.clear();
                        Message[] pomMessArray = {message};
                        folderNeispravne.open(Folder.READ_WRITE);
                        folder.copyMessages(pomMessArray, folderNeispravne);
                        message.setFlag(Flags.Flag.DELETED, true);
                        System.out.println("Poruka prebacena u mapu Neispravne!");
                        folderNeispravne.close(true);
                    }
                } else {
                    brojOstalihPoruka++;
                    System.out.println("Nije pronadjen trazeni predmet");
                    Message[] pomMessArray = {message};
                    folderOstale.open(Folder.READ_WRITE);
                    folder.copyMessages(pomMessArray, folderOstale);
                    message.setFlag(Flags.Flag.DELETED, true);
                    System.out.println("Poruka prebacena u mapu Ostale!");
                    folderOstale.close(true);
                }
            }

            
            folder.close(true);
            
            


            
            store.close();
        } catch (AuthenticationFailedException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (FolderClosedException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (FolderNotFoundException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (ReadOnlyFolderException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (StoreClosedException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (Exception e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        }
        
        Date kraj = new Date();        
        vrijemeObrade = String.valueOf(kraj.getTime() - poc.getTime());
        vrijemePocetka = sdf.format(poc);        
        vrijemeZavrsetka = sdf.format(kraj);
        StringBuilder sb = new StringBuilder();
        sb.append("Obrada započela u: ");
        sb.append(vrijemePocetka);
        sb.append(System.lineSeparator());
        sb.append("Obrada završila u: ");
        sb.append(vrijemeZavrsetka);
        sb.append(System.lineSeparator());
        sb.append("Trajanje obrade u ms: ");
        sb.append(vrijemeObrade);
        sb.append(System.lineSeparator());
        sb.append("Sveukupan broj poruka: ");
        sb.append(sveukupanBrojPoruka);
        sb.append(System.lineSeparator());
        sb.append("Ukupan broj poruka: ");
        sb.append(ukupanBrojPoruka);
        sb.append(System.lineSeparator());
        sb.append("Broj ispravnih poruka: ");
        sb.append(brojIspravnihPoruka);
        sb.append(System.lineSeparator());
        sb.append("Broj neispravnih poruka: ");
        sb.append(brojNeispravnihPoruka);
        sb.append(System.lineSeparator());
        sb.append("Broj ostalih poruka: ");
        sb.append(brojOstalihPoruka);
        sb.append(System.lineSeparator());
        sb.append("Broj preuzetih datoteka: ");
        sb.append(brojPreuzetihDatoteka);        
        
        sadrzajPorukeDretve = sb.toString();
        System.out.println("Dretvin mail poslan: " + posaljiMail());
    }

    
    private String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }
    
    public String posaljiMail() {

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", emailPosluzitelj);
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(adresaDretve));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(adresaPrimateljaDretve));
            message.setSubject(predmetDretve);
            message.setText(sadrzajPorukeDretve);
            message.setHeader("Content-Type", "text/html");
            Transport.send(message);
            System.out.println("Message Sent SuccessFully...");
            
            Store store = session.getStore("imap");
            
            store.connect(emailPosluzitelj, adresaDretve, lozinkaDretve);
            
            Folder f = store.getFolder(izlaznaMapa);
            
            if (!f.exists()) {
                f.create(Folder.HOLDS_MESSAGES);
            }
            
            f.appendMessages(new Message[]{message});            
            System.out.println("Poruka premjestena u mapu " + izlaznaMapa);
        } catch (MessagingException ex) {
            Logger.getLogger(SlanjePoruke.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR";
        }
        return "OK";
    }
}

package org.foi.nwtis.student12.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.student12.konfiguracije.Konfiguracija;
import org.foi.nwtis.student12.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student12.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student12.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student12.web.ObradaPoruka;


@WebListener()
public class SlusacAplikacije implements ServletContextListener {
    ObradaPoruka op;
    private static BP_Konfiguracija bpkonfig;

    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String path = sce.getServletContext().getRealPath("WEB-INF");
        Konfiguracija konfig = null;

        String dbDatoteka = sce.getServletContext().getInitParameter("db_konfiguracija");
        String konfigDatoteka = sce.getServletContext().getInitParameter("konfiguracija");

        BP_Konfiguracija dbKonfig = new BP_Konfiguracija(path + File.separator + dbDatoteka);
        
        bpkonfig = dbKonfig;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + File.separator + konfigDatoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        sce.getServletContext().setAttribute("BP_Konfiguracija", dbKonfig);
        sce.getServletContext().setAttribute("Konfiguracija", konfig);

        System.out.println("Aplikacija pokrenuta");        
        op = new ObradaPoruka();        
        new Thread(op).start();        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (op != null) {
            op.zaustaviDretvu();
            System.out.println("Dretva ObradaPoruka zaustavljena!");
        }
    }

    public static BP_Konfiguracija getBpkonfig() {
        return bpkonfig;
    }    
    
}

package org.foi.nwtis.student12.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.student12.konfiguracije.Konfiguracija;
import org.foi.nwtis.student12.web.kontrole.Autentikacija;


@ManagedBean (name = "emailPovezivanje")
@SessionScoped
public class EmailPovezivanje {    
    
    private static Konfiguracija konfig;
    private String adresaPosluzitelja;
    private String korisnickoIme;
    private String lozinka = "";
   
    
    public EmailPovezivanje() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        konfig = (Konfiguracija) sc.getAttribute("Konfiguracija");
        
        adresaPosluzitelja = konfig.dajPostavku("adresaPosluzitelja");
        korisnickoIme = konfig.dajPostavku("korisnickoIme");
    }

    
    public String saljiPoruku() {
        if (!Autentikacija.autentificiraj(korisnickoIme, lozinka)) {
            return "NOT_OK";
        }
        return "OK";
    }

    
    public String citajPoruke() {
        if (!Autentikacija.autentificiraj(korisnickoIme, lozinka)) {
            return "NOT_OK";
        }
        return "OK";
    }

    public String getAdresaPosluzitelja() {
        return adresaPosluzitelja;
    }

    public void setAdresaPosluzitelja(String adresaPosluzitelja) {
        this.adresaPosluzitelja = adresaPosluzitelja;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    } 
        
}

package org.foi.nwtis.student12.web.zrna;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class Lokalizacija {

    private Map<String, Object> jezici;
    private String odabraniJezik;
    private Locale odabraniLocale;

    
    public Lokalizacija() {
        jezici = new HashMap<String, Object>();
        jezici.put("Engleski", Locale.ENGLISH);
        jezici.put("Deutsch", Locale.GERMAN);
        jezici.put("Hrvatski", new Locale("hr"));

        odabraniLocale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
    }

    
    public String odaberiJezik() {
        switch (odabraniJezik) {
            case "en":
                odabraniLocale = (Locale.ENGLISH);
                break;
            case "de":
                odabraniLocale = (Locale.GERMAN);
                break;
            case "hr":
                odabraniLocale = (new Locale("hr"));
                break;
        }
        return "OK";
    }

    public Map<String, Object> getJezici() {
        return jezici;
    }

    public void setJezici(Map<String, Object> jezici) {
        this.jezici = jezici;
    }

    public String getOdabraniJezik() {
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;        
    }

    public Locale getOdabraniLocale() {
        return odabraniLocale;
    }

    public void setOdabraniLocale(Locale odabraniLocale) {
        this.odabraniLocale = odabraniLocale;
    }
}

package org.foi.nwtis.student12.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.student12.web.kontrole.Poruka;


@ManagedBean
@RequestScoped
public class PregledPoruke {
    private Poruka poruka;

    public Poruka getPoruka() {
        PregledSvihPoruka psp = (PregledSvihPoruka)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pregledSvihPoruka");
        poruka = psp.getOdabranaPoruka();
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }
    

    
    public PregledPoruke() {
    }
}

package org.foi.nwtis.student12.web.zrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import org.foi.nwtis.student12.konfiguracije.Konfiguracija;
import org.foi.nwtis.student12.web.kontrole.Poruka;
import org.foi.nwtis.student12.web.kontrole.PrivitakPoruke;


@ManagedBean
@SessionScoped
public class PregledSvihPoruka {

    @ManagedProperty(value = "#{emailPovezivanje.adresaPosluzitelja}")
    private String adresaPosluzitelja;
    @ManagedProperty(value = "#{emailPovezivanje.korisnickoIme}")
    private String korisnickoIme;
    @ManagedProperty(value = "#{emailPovezivanje.lozinka}")
    private String lozinka;
    private List<Poruka> poruke = new ArrayList<Poruka>();
    private Poruka odabranaPoruka;
    private String porukaID;
    private int brojMailova;
    private Konfiguracija konfig;
    private int start;
    private int end;
    private boolean textIsHtml = false;
    private String mailFolder = "inbox";
    private String mapaNeispravne;
    private String mapaOstale;
    private String mapaIspravne;
    private String mapaIzlazne;
    private final String mapaInbox = "inbox";
    private List<String> mape = new ArrayList<>();

    public List<String> getMape() {
        return mape;
    }

    public String getMailFolder() {
        return mailFolder;
    }

    public void setMailFolder(String mailFolder) {
        this.mailFolder = mailFolder;
    }

    public void setAdresaPosluzitelja(String adresaPosluzitelja) {
        this.adresaPosluzitelja = adresaPosluzitelja;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPorukaID() {
        return porukaID;
    }

    public void setPorukaID(String porukaID) {        
        this.porukaID = porukaID;
    }

    public List<Poruka> getPoruke() {        
        return poruke;
    }

    public void setPoruke(List<Poruka> poruke) {
        this.poruke = poruke;
    }

    public Poruka getOdabranaPoruka() {
        return odabranaPoruka;
    }

    public void setOdabranaPoruka(Poruka odabranaPoruka) {
        this.odabranaPoruka = odabranaPoruka;
    }

    
    public PregledSvihPoruka() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        konfig = (Konfiguracija) sc.getAttribute("Konfiguracija");

        brojMailova = Integer.parseInt(konfig.dajPostavku("brojMailova"));
        mapaNeispravne = konfig.dajPostavku("mapaNeispravne");
        mapaIspravne = konfig.dajPostavku("mapaIspravne");
        mapaOstale = konfig.dajPostavku("mapaOstale");
        mapaIzlazne = konfig.dajPostavku("mapaIzlazne");

        mape.add(mapaInbox);
        mape.add(mapaIspravne);
        mape.add(mapaNeispravne);
        mape.add(mapaOstale);
        mape.add(mapaIzlazne);
        
       poruke.clear();
       processMail();
    }  

    
    public String pregledPoruke() {
        odabranaPoruka = null;
        for (Poruka p : poruke) {            
            if (p.getId().equals(porukaID)) {
                odabranaPoruka = p;
                break;
            }
        }        
        return "OK";
    }

    
    private void printData(String data) {
        System.out.println(data);
    }

    
    private void processMail() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;
        Object messagecontentObject = null;
        String sender = null;
        String subject = null;
        Multipart multipart = null;
        Part part = null;
        String contentType = null;

        try {
            printData("--------------processing mails started-----------------");
            session = Session.getDefaultInstance(System.getProperties(), null);

            printData("getting the session for accessing email.");
            store = session.getStore("imap");

            store.connect(adresaPosluzitelja, korisnickoIme, lozinka);
            printData("Connection established with IMAP server.");

            
            folder = store.getDefaultFolder();

            printData("Getting the Inbox folder.");

            
            folder = folder.getFolder(mailFolder);

            
            try {
                folder.open(Folder.READ_ONLY);
            } catch (FolderNotFoundException f) {
                System.out.println("Ne postoji mapa " + mailFolder);
                return;
            }
            
            end = folder.getMessageCount();
            start = end - brojMailova + 1;
            if (start < 1) {
                start = 1;
            }

            messages = folder.getMessages(start, end);
            
            messages = reverseMessageOrder(messages);

            
            for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                
                message = messages[messageNumber];

                
                messagecontentObject = message.getContent();

                List<PrivitakPoruke> pp = new ArrayList<>();

                
                if (messagecontentObject instanceof Multipart) {
                    printData("Found Email with Attachment");
                    sender = ((InternetAddress) message.getFrom()[0]).getPersonal();

                    
                    printData("If the personal information has no entry, check the address for the sender information.");

                    if (sender == null) {
                        sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                        printData("sender in NULL. Printing Address:" + sender);
                    }
                    printData("Sender -." + sender);

                    
                    subject = message.getSubject();

                    printData("subject=" + subject);

                    
                    multipart = (Multipart) message.getContent();

                    printData("Retrieve the Multipart object from the message");

                    
                    for (int i = 0; i < multipart.getCount(); i++) {
                        
                        part = multipart.getBodyPart(i);

                        
                        contentType = part.getContentType();

                        
                        printData("Content: " + contentType);

                        String fileName = "";
                        if (contentType.startsWith("text/plain")) {
                            printData("---------reading content type text/plain  mail -------------");
                        } else {
                            
                            fileName = part.getFileName();
                            printData("retrive the fileName=" + fileName);
                        }
                        PrivitakPoruke privitak = new PrivitakPoruke(i, contentType, part.getSize(), fileName);
                        pp.add(privitak);
                    }
                } else {
                    printData("Found Mail Without Attachment");
                    sender = ((InternetAddress) message.getFrom()[0]).getPersonal();

                    
                    printData("If the personal information has no entry, check the address for the sender information.");

                    if (sender == null) {
                        sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                        printData("sender in NULL. Printing Address:" + sender);
                    }

                    
                    subject = message.getSubject();
                    printData("subject=" + subject);
                }

                String[] zaglavlja = message.getHeader("Message-ID");
                String messID = "";
                if (zaglavlja != null && zaglavlja.length > 0) {
                    messID = zaglavlja[0];
                }
                Poruka poruka = new Poruka(messID, message.getSentDate(), sender, subject,
                        contentType, message.getSize(), pp.size(),
                        message.getFlags(), pp, true, true, getText(message));
                poruke.add(poruka);
            }

            
            folder.close(true);

            
            store.close();
        } catch (AuthenticationFailedException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (FolderClosedException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (FolderNotFoundException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (ReadOnlyFolderException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (StoreClosedException e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        } catch (Exception e) {
            printData("Not able to process the mail reading.");
            e.printStackTrace();
        }
    }

    
    private static Message[] reverseMessageOrder(Message[] messages) {
        Message revMessages[] = new Message[messages.length];
        int i = messages.length - 1;
        for (int j = 0; j < messages.length; j++, i--) {
            revMessages[j] = messages[i];
        }
        return revMessages;
    }

    
    private String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    
    public void promijeniMapu() {
        poruke.clear();
        processMail();
    }
}

package org.foi.nwtis.student12.web.zrna;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.student12.konfiguracije.Konfiguracija;


@ManagedBean
@RequestScoped
public class SlanjePoruke {

    @ManagedProperty(value = "#{emailPovezivanje.adresaPosluzitelja}")
    private String host;
    @ManagedProperty(value = "#{emailPovezivanje.korisnickoIme}")
    private String from;
    private String to;
    @ManagedProperty(value = "#{emailPovezivanje.korisnickoIme}")
    private String user;
    @ManagedProperty(value = "#{emailPovezivanje.lozinka}")
    private String password;
    private String content;
    private String contentType;
    private String subject;
    private String izlaznaMapa;
    private Konfiguracija konfig;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    
    public String posaljiMail() {

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            message.setHeader("Content-Type", contentType);
            Transport.send(message);
            System.out.println("Message Sent SuccessFully..." + content);
            
            Store store = session.getStore("imap");
            
            store.connect(host, user, password);
            
            Folder f = store.getFolder(izlaznaMapa);
            
            if (!f.exists()) {
                f.create(Folder.HOLDS_MESSAGES);
            }
            
            f.appendMessages(new Message[]{message});            
            System.out.println("Poruka premjestena u mapu " + izlaznaMapa);
        } catch (MessagingException ex) {
            Logger.getLogger(SlanjePoruke.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR";
        }
        return "OK";
    }

    
    public SlanjePoruke() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        konfig = (Konfiguracija) sc.getAttribute("Konfiguracija");

        izlaznaMapa = konfig.dajPostavku("mapaIzlazne");
    }
}