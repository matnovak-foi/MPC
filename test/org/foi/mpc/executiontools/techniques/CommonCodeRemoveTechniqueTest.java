package org.foi.mpc.executiontools.techniques;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.common.filesystem.file.InMemoryFile;
import org.foi.common.filesystem.file.InMemoryTextFileUtility;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class CommonCodeRemoveTechniqueTest {
    private CommonCodeRemoveTechnique ccrTechnique;
    private File testDir;

    @Before
    public void setUp() {
        ccrTechnique = new CommonCodeRemoveTechnique();
        testDir = new File("testDir");
        testDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(testDir);
    }

    @Test
    public void isExecutionPhaseAndHasCorrectGetName() throws IOException {
        assertTrue(ccrTechnique instanceof ExecutionTool);
        assertTrue(ccrTechnique instanceof PreprocessingTechnique);
        assertNotNull(ccrTechnique.getName());
        assertEquals("RemoveCommonCode", ccrTechnique.getName());
    }

    public class withInMemoryFiles {
        InMemoryDir testDir;
        InMemoryFile file;
        InMemoryTextFileUtility tfu = new InMemoryTextFileUtility(StandardCharsets.UTF_8);
        File resultFile;
        private boolean cleanIsRun;

        @Before
        public void setUp() {
            testDir = new InMemoryDir("testDir");
            file = new InMemoryFile("file1.java");
            testDir.addFile(file);

            ccrTechnique.setDc(new InMemoryDirectoryFileUtility());
            ccrTechnique.setTfu(tfu);

            resultFile = new File(testDir.getName() + File.separator + ccrTechnique.getName() + File.separator + file.getName());
        }

        @Test
        public void createsCommonCodeRemoveDir() {
            ccrTechnique.runPreporcess(testDir);
            assertEquals(2, testDir.listFiles().length);
            List<String> dirContent = Arrays.asList(testDir.list());
            assertThat(dirContent, hasItem(ccrTechnique.getName()));
        }

        @Test
        public void fileIsOkLeaveItAsIs() throws IOException {
            file.setContent("test");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("test", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesSetMethodOnOneFile() throws IOException {
            file.setContent("public void setName(int name){this.name=name}\n..." +
                            "private static void setName(float name){this.name=name}" +
                            "static protected void setName(double name){this.name=name}" +
                            " public  void   setName(String name){this.name=name}" +
                            "void setName(Object name){this.name=name}" +
                            "void  setName(Something name){ name =  name   }" +
                            "void  setName  (  Bla   name  )  {  this.name  =  name  }" +
                            "void  setName  (  Bla   name  ) \n {  this.name  =  name  }" +
                            "void  setName  (  Bla   name  ) \r\n {  this.namež  =  namež  }" +
                            "void  setNamež  (  Bla   name  ) \r\n   \r\n {  this.name  =  name  } " +
                            "...\n...void \t\n   setName123 \t\n   ( \t\n   Bla \t\n    name \t\n   ) \r\n  \t  \r\n { \n \t  this \n \t  . \n\t   name \t\n   = \n \t  name \n \t  }\n...\n..."+
                            "\n...\n...private void setName(float name){this.name=name}\n ...");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n...  ...\n...\n...\n...\n...\n...\n ...", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesSetMethod() throws IOException {
            file.setContent("public void setVar(int var){\n" +
                    " this.var=var ; \n" +
                    "}");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("", tfu.readFileContentToString(resultFile));
        }


        @Test
        public void leavesModifiedSetMethod() throws IOException {
            file.setContent("public void setVar(int var){\n" +
                    " if(var==null) this.var=var; \n" +
                    "}");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("public void setVar(int var){\n" +
                    " if(var==null) this.var=var; \n" +
                    "}", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesGetMethod() throws IOException {
            file.setContent("public TextFileUtility getTfu() {\n" +
                    "        return tfu;\n" +
                    "    }\n..." +
                    "public static String getOWM_BASE_URI() {\n" +
                    "        return OWM_BASE_URI;\n" +
                    "    }\n..."+
                    "static public String getOWM_BASE_URI() {\n" +
                            "  \t      return OWM_BASE_URI;\n" +
                            " \t   } " +
                    "public void main(){ bla }\n" +
                    "public static synchronized ServletContext getSingletonObject() {\n" +
                    "        return sc;\n" +
                    "    }" +
                    "public String getSadržaj() {\n" +
                    "        return sadržaj;\n" +
                    "    }");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n...\n... public void main(){ bla }\n", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesIsMethod() throws IOException {
            file.setContent("public boolean isTest() {\n" +
                    "        \t\t return test;\n" +
                    "   }");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesCollectionSetOrGetMethods() throws IOException {
            file.setContent("" +
                    "public void setEvidencijaRada(HashMap<String, EvidencijaModel> evidencijaRada) {\n" +
                    "        this.evidencijaRada = evidencijaRada;\n" +
                    "    }" +
                    "\n..."  +
                    "public HashMap<String, EvidencijaModel> getEvidencijaRada() {\n" +
                    "        return evidencijaRada;\n" +
                    "    }" +
                    "\n..." +
                    "public static ArrayList<ZahtjeviKorisnika> getZahtjevi() {\n" +
                    "   \t     return zahtjevi;\n" +
                    "    }\n..." +
                    "\n..." +
                    "static public void setZahtjevi(ArrayList<ZahtjeviKorisnika> zahtjevi) {\n" +
                    "   \t     this.zahtjevi = zahtjevi;\n" +
                    "    }" +
                    "");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n...\n...\n...\n...", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesSetterAndGetterWithCollectionOfCollections() throws IOException {
            file.setContent("" +
                    "public void setEvidencijaRada(HashMap<String, HashMap<String, EvidencijaModel>> evidencijaRada) {\n" +
                    "   \t     this.evidencijaRada = evidencijaRada;\n" +
                    "    }" +
                    "\n..."  +
                    "public HashMap<String, HashMap<String, EvidencijaModel>> getEvidencijaRada() {\n" +
                    "   \t     return evidencijaRada;\n" +
                    "    }" +
                    "\n..." +
                    "public ArrayList<ArrayList<ZahtjeviKorisnika>> getZahtjevi() {\n" +
                    "        return zahtjevi;\n" +
                    "    }\n..." +
                    "\n..." +
                    "public void setZahtjevi(ArrayList<ArrayList<ZahtjeviKorisnika>> zahtjevi) {\n" +
                    "        this.zahtjevi = zahtjevi;\n" +
                    "    }" +
                    "public void setZahtjevi(\tArrayList\t<\tArrayList<\tZahtjeviKorisnika\t>\t>\t zahtjevi) {\n" +
                    "        this.zahtjevi = zahtjevi;\n" +
                    "    }" +
                    "public ArrayList<Map.Entry<Integer, Integer>> getKoordinate() {\n" +
                    "        return koordinate;\n" +
                    "    }");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n...\n...\n...\n...", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesImportStatements() throws IOException {
            file.setContent("import test;\n" +
                    "import org.foi.common.filesystem.DirectoryUtility;\n" +
                    "import org.foi.mpc.phases.ExecutionTool;\n" +
                    "import org.foi.mpc.phases.phases.PreprocessingTechnique;\n" +
                    "import org.foi.mpc.utility.InMemoryDir;\n" +
                    "import org.foi.mpc.utility.*;\n" +
                    " \t import \t org.junit.After;\n" +
                    "import org.junit.runner.RunWith;\n" +
                    "\n" +
                    "import java.io.File;\n" +
                    "   import   java  .  nio  .  charset  .  StandardCharsets  ;\n" +
                    "   import   java.nio.charset.StandardCharsets   ;   \n" +
                    "import java.nio.charset.StandardCharsets;\n" +
                    "\n" +
                    "import static org.junit.Assert.*;\n" +
                    "import static java.lang.Thread.sleep;\n");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesPackageStatements() throws IOException {
            file.setContent(""+
                    "package org.foi.nwtis.student6.zadaca_1;\n" +
                    "package org.foi.nwtis.student6.zadaca_1;\n" +
                    "\t package \t  org . foi .   mpc  .  executiontools .  techniques  ;  \n\n" );
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesEmptyConstructors() throws IOException {
            file.setContent(""+
                    "\n public ObradaZahtjeva( ) {\n" +
                    "        \n" +
                    "    }\n..." +
                    "\n ObradaZahtjeva(  ) {\n" +
                    "        \tsuper();\n" +
                    "    }\n..." +
                    "\n ObradaZahtjeva  ( int a  , int b  ) {\n" +
                    "        super (  a  ,  b   ) ;\n" +
                    "    }\n..." +
                    "\n public AdreseFacade() {\n" +
                    "  \t      super(Adrese.class);\n" +
                    "   \t }"+
                    "" );
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n\n...\n\n...\n\n...\n", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removesAnotations() throws IOException {
            file.setContent(""+
                    "@Overide\n" +
                    "@author student6\n" +
                    "@SuppressWarnings(\"SleepWhileInLoop\")\n" +
                    "@Entity\n" +
                    "@Table(name = \"ADRESE\")\n" +
                    "@XmlRootElement\n" +
                    "\t@Id\n" +
                    "@GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                    "@Basic(optional = false)\n" +
                    "@Column(name = \"IDADRESA\")\n"+
                    "@Size(min = 1, max = 255)\n"+
                    "@NamedQuery(name = \"Adrese.findByLongitude\", query = \"SELECT a FROM Adrese a WHERE a.longitude = :longitude\")\n"+
                    "@NamedQueries({\n" +
                    " \t   @NamedQuery(name = \"Adrese.findAll\", query = \"SELECT a FROM Adrese a\"),\n" +
                    "\t    @NamedQuery(name = \"Adrese.findByIdadresa\", query = \"SELECT a FROM Adrese a WHERE a.idadresa = :idadresa\"),\n" +
                    "    @NamedQuery(name = \"Adrese.findByAdresa\", query = \"SELECT a FROM Adrese a WHERE a.adresa = :adresa\"),\n" +
                    "    @NamedQuery(name = \"Adrese.findByLatitude\", query = \"SELECT a FROM Adrese a WHERE a.latitude = :latitude\"),\n" +
                    "    @NamedQuery(name = \"Adrese.findByLongitude\", query = \"SELECT a FROM Adrese a WHERE a.longitude = :longitude\")})\n"+
                    "public void function(){ bla }\n" +
                    "@Some\t(name=\"ADRESE\")public void function(){ bla }\n"+
                    "@Some public void function(){ bla }\n"+
                    "@Path(\"{id}\")\n"+
                    "@WebListener()\n"+
                    "@Something(value = \"#{emailPovezivanje.adresaPosluzitelja}\")\n"+
                    "@WebServiceRef(wsdlLocation = \"META-INF/wsdl/api.wxbug.net/weatherservice.asmx.wsdl\")\n" +
                    "@XmlType(name = \"LiveWeatherData#*\", propOrder = {\n" +
                    "    \"auxTemperature\",\n    \"auxTemperatureRate\",\n" +
                    "    \"city\",\n    \"cityCode\",\n    \"country\",\n    \"currIcon\",\n" +
                    "    \"currDesc\",\n    \"dewPoint\",\n    \"elevation\",\n    \"elevationUnit\",\n" +
                    "    \"feelsLike\",\n    \"gustTime\",\n    \"gustWindSpeed\",\n    \"gustWindSpeedUnit\",\n    \"gustWindDirectionString\",\n" +
                    "    \"gustWindDirectionDegrees\",\n    \"humidity\",\n    \"humidityUnit\",\n    \"humidityHigh\",\n" +
                    "    \"humidityLow\",\n    \"humidityRate\",\n    \"inputLocationUrl\",\n    \"moonPhase\",\n    \"moonPhaseImage\",\n    \"pressure\",\n    \"pressureUnit\",\n    \"pressureHigh\",\n" +
                    "    \"pressureLow\",\n    \"pressureRate\",\n    \"pressureRateUnit\",\n    \"light\",\n" +
                    "    \"lightRate\",\n    \"indoorTemperature\",\n    \"indoorTemperatureRate\",\n    \"latitude\",\n" +
                    "    \"longitude\",\n    \"obDate\",\n    \"obDateTime\",\n    \"rainMonth\",\n" +
                    "    \"rainRate\",\n    \"rainRateMax\",\n    \"rainRateUnit\",\n    \"rainToday\",\n" +
                    "    \"rainUnit\",\n    \"rainYear\",\n    \"state\",\n    \"stationIDRequested\",\n" +
                    "    \"stationIDReturned\",\n    \"stationName\",\n    \"stationURL\",\n    \"sunrise\",\n    \"sunset\",\n" +
                    "    \"temperature\",\n    \"temperatureHigh\",\n    \"temperatureLow\",\n    \"temperatureRate\",\n    \"temperatureRateUnit\",\n" +
                    "    \"temperatureUnit\",\n    \"timeZone\",\n    \"timeZoneOffset\",\n    \"webUrl\",\n    \"wetBulb\",\n" +
                    "    \"windDirection\",\n    \"windDirectionAvg\",\n    \"windDirectionDegrees\",\n    \"windSpeed\",\n" +
                    "    \"windSpeedAvg\",\n    \"windSpeedUnit\",\n    \"zipCode\"\n}, propOrder = {\n    \"auxTemperature\",\n    \"auxTemperatureRate\",\n})\n"+
                    "@Something(\"application/json;charset=utf-8?\")\n" +
                    "@ManagedProperty(\"#(param.putanja)\")");
            ccrTechnique.runPreporcess(testDir);
            String expected = "\n student6\n" +
                    "public void function(){ bla }\n" +
                    "public void function(){ bla }\n" +
                    " public void function(){ bla }\n\n";
            assertEquals(expected, tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeEmptyVoidFunctions() throws IOException {
            file.setContent(""+
                    "public void ObradaZahtjeva( ) {\n" +
                    "     \t   \n" +
                    "    }\n..." +
                    "void ObradaZahtjeva( \t ) {\n" +
                    "        \n" +
                    "    }\n..." +
                    "void ObradaZahtjeva(int a, int b, Type < \t Type > c) {\n" +
                    "    }\n..." +
                    "void ObradaZahtjeva(int a, int b, \t Type \t < Type \t > c) {\n" +
                    "    return; \n" +
                    "    }\n..." +
                    "" );
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n...\n...\n...\n...", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeEmptyStaticVoidFunctions() throws IOException {
            file.setContent(""+
                    "static public void ObradaZahtjeva( ) {\n" +
                    "   \t     \n" +
                    "    }\n..." +
                    "static \t void \t ObradaZahtjeva( ) {\n" +
                    "        \n" +
                    "    }\n..." +
                    "static void ObradaZahtjeva(int a, int b, Type < \t Type > c) {\n" +
                    "    }\n..." +
                    "public static void ObradaZahtjeva(int a, int b, Type < Type > c) {\n" +
                    "    return; \n" +
                    "    }\n..." +
                    "" );
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n...\n...\n...\n...", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void doNotRemoveEmptyCatchStatements() throws IOException {
            file.setContent("" +
                    "try {\n" +
                    "            IP = InetAddress.getLocalHost().toString();\n" +
                    "        } catch (Exception ex) {\n" +
                    "        \t}" +
                    "try {\n" +
                    "            IP = InetAddress.getLocalHost().toString();\n" +
                    "        } catch (Exception ex) {\n" +
                    " System.out.println(\"\");\n" +
                    "        }" +
                    "");
            ccrTechnique.runPreporcess(testDir);
            String expected = "try {\n" +
                    "            IP = InetAddress.getLocalHost().toString();\n" +
                    "        } catch (Exception ex) {\n" +
                    "        \t}try {\n" +
                    "            IP = InetAddress.getLocalHost().toString();\n" +
                    "        } catch (Exception ex) {\n" +
                    " System.out.println(\"\");\n" +
                    "        }";
            assertEquals(expected, tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeNonInitializedFields() throws IOException {
            file.setContent("" +
                    "private static final long serialVersionUID = 1L;\n" +
                    "    private \t Integer id;\n" +
                    "    private String korisnik;\n" +
                    "    private String url;\n" +
                    "    private String ipadresa;\n" +
                    "    private Date vrijeme;\n" +
                    "    private int trajanje;\n" +
                    "    private List<int> status;\n" +
                    "    private java.util.Properties properties;"+
                    "");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("private static final long serialVersionUID = 1L;\n", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeFieldSetCopyConstructor() throws IOException {
            file.setContent("" +
                    "Adresa(long idadresa, String adresa, Lokacija geoloc) {\n" +
                    "        this.idadresa = idadresa;\n" +
                    "        this.adresa = adresa;\n" +
                    "        this.geoloc = geoloc;\n" +
                    "        this.geoloc2 = geoloc3;\n" +
                    "    }\n...\n" +
                    "public Adresa(long idadresa, String adresa, Lokacija geoloc) {\n" +
                    "    \t    this.idadresa = idadresa;\n" +
                    "        this.adresa = adresa;\n" +
                    "        this.geoloc = geoloc;\n" +
                    "        this.geoloc2 = geoloc3;\n" +
                    "    }\n..." +
                    "");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n\n...\n\n...", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void ifThisKeywordIsNotUsedLeaveConstructor() throws IOException {
            String inputContent = "" +
                    "Adresa(long idadresa, String adresa, Lokacija geoloc) {\n" +
                    "   \t     this.idadresa = idadresa;\n" +
                    "        this.adresa = adresa;\n" +
                    "        this.geoloc = geoloc;\n" +
                    "        geoloc2 = geoloc3;\n" +
                    "    }\n..." +
                    "public Adresa(long idadresa, String adresa, Lokacija geoloc) {\n" +
                    "        idadresa = idadresa;\n" +
                    "        this.adresa = adresa;\n" +
                    "        this.geoloc = geoloc;\n" +
                    "        geoloc2 = geoloc3;\n" +
                    "    }\n..." +
                    "";
            file.setContent(inputContent);
            ccrTechnique.runPreporcess(testDir);
            assertEquals(inputContent, tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeSimpleFieldSetFunction() throws IOException {
            file.setContent("" +
                    "void postavi(String latitude, String longitude) {\n" +
                    "     \t   this.latitude = latitude;\n" +
                    "        this.longitude = longitude;\n" +
                    "    }\n..." +
                    "\npublic void postaviKorisnickePodatke(String apiKey) {\n" +
                    "        this.apiKey = apiKey;\n" +
                    "    }" +
                    "\npublic static void postaviKorisnickePodatke(String apiKey) {\n" +
                    "        this.apiKey = apiKey;\n" +
                    "    }" +
                    "");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n" +
                    "\n" +
                    "...\n" +
                    "\n", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void ifThisKeywordIsNotUsedLeaveFieldSetCopyFunction() throws IOException {
            String inputContent = "" +
                    "void postavi(String latitude, String longitude) {\n" +
                    "   \t     latitude = latitude;\n" +
                    "        this.longitude = longitude;\n" +
                    "    }\n..." +
                    "public void postaviKorisnickePodatke(String apiKey) {\n" +
                    "        apiKey = apiKey;\n" +
                    "    }" +
                    "";
            file.setContent(inputContent);
            ccrTechnique.runPreporcess(testDir);
            assertEquals(inputContent, tfu.readFileContentToString(resultFile));
        }

        @Test
        public void doesNotRemoveToMuchOfAComment() throws IOException {
            String inputContent = "" +
                    "\n//public String datoteka;\n" +
                    "    \n" +
                    "\n" +
                    "    /**\n" +
                    "     * Creates a new instance of PregledDatoteke\n" +
                    "     */"+
                    "";
            file.setContent(inputContent);
            ccrTechnique.runPreporcess(testDir);
            String expecedContent = "" +
                    "\n" +
                    "//\n" +
                    "    \n" +
                    "\n" +
                    "    /**\n" +
                    "     * Creates a new instance of PregledDatoteke\n" +
                    "     */"+
                    "";
            assertEquals(expecedContent, tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeEmptyClass() throws IOException {
            file.setContent("public class Lokacija {\n" +
                    "  \t  \n" +
                    "  \t  \n" +
                    "\n" +
                    "    \n" +
                    "}\n" +
                    "static class DnevnikFacade extends AbstractFacade<Dnevnik> implements Bla, Bla2 {\n" +
                    "    \n" +
                    "}");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeClassThatWillBecomeEmptyDuringCCR() throws IOException {
            file.setContent("public class Adresa {\n" +
                    " \t   private long idadresa;\n" +
                    "    private String adresa;\n" +
                    "    private Lokacija geoloc;\n" +
                    "\n" +
                    "    public Adresa(long idadresa, String adresa, Lokacija geoloc) {\n" +
                    "   \t     this.idadresa = idadresa;\n" +
                    "        this.adresa = adresa;\n" +
                    "        this.geoloc = geoloc;\n" +
                    "    }\n" +
                    "\n" +
                    "    }");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeEmptyFunctionsAndClass() throws IOException {

            file.setContent("public class LiveWeatherData {\n"+
                "public String getCountry() {}\n"+
                "\n"+
                "    \n"+
                "    public void setCountry() {}\n"+
                "}");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void doNotDeleteCatchStatementsOnItsOwn() throws IOException {

            file.setContent("} \n catch (AuthenticationFailedException e) {\n}" +
                    "} catch (AddressException e) {\n" +
                    "            this.neuspjesno = true;\n" +
                    "            this.uspjesno = false;\n" +
                    "        } catch (SendFailedException e) {\n" +
                    "            this.neuspjesno = true;\n" +
                    "            this.uspjesno = false;\n" +
                    "        } catch (MessagingException e) {\n" +
                    "            this.neuspjesno = true;\n" +
                    "            this.uspjesno = false;\n" +
                    "        }");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("} catch (AuthenticationFailedException e) {\n" +
                    "}} catch (AddressException e) {\n" +
                    "            this.neuspjesno = true;\n" +
                    "            this.uspjesno = false;\n" +
                    "        } catch (SendFailedException e) {\n" +
                    "            this.neuspjesno = true;\n" +
                    "            this.uspjesno = false;\n" +
                    "        } catch (MessagingException e) {\n" +
                    "            this.neuspjesno = true;\n" +
                    "            this.uspjesno = false;\n" +
                    "        }", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void replaceEmptyCatchesWithOneCatch() throws IOException {

            file.setContent("catch (AuthenticationFailedException e) {\n"+
                "            } catch (FolderClosedException e) {\n"+
                "            } catch (FolderNotFoundException e) {\n"+
                "            } catch (NoSuchProviderException e) {\n"+
                "            } catch (ReadOnlyFolderException e) {\n"+
                "            } catch (StoreClosedException e) {\n"+
                "            } catch (MessagingException | InterruptedException e) {\n"+
                "            }");
            ccrTechnique.runPreporcess(testDir);
            assertEquals("\n catch (Exception e) {}\n", tfu.readFileContentToString(resultFile));
        }
    }

    public class withRealFiles {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        DirectoryFileUtility dfu = new DirectoryFileUtility();

        @Before
        public void setUp() {
            ccrTechnique.setDc(dfu);
            ccrTechnique.setTfu(tfu);
        }

        @Test
        public void runsCleaner() {
            final boolean[] cleanIsRun = {false};
            ccrTechnique.setCleaner(new PreprocessDirectoryCleaner(ccrTechnique.getName()){
                @Override
                public void clean(File preprocessedDir) {
                    cleanIsRun[0] = true;
                }
            });

            ccrTechnique.runTool(testDir);

            assertTrue(cleanIsRun[0]);
        }

        @Test
        public void canWorkWithFilesInRootDir() throws IOException {
            File file = new File(testDir + File.separator + "file1.java");
            tfu.createFileWithText(file, "import java.nio.charset.StandardCharsets;\n" +
                    "something ...");

            ccrTechnique.runTool(testDir);

            assertEquals(1, testDir.listFiles().length);
            assertEquals("\nsomething ...", tfu.readFileContentToString(testDir.listFiles()[0]));
        }

        @Test
        public void canWorkWithFilesInSubDir() throws IOException {
            File subDir = new File(testDir + File.separator + "subdir");
            subDir.mkdir();
            File file = new File(subDir + File.separator + "file1.java");
            tfu.createFileWithText(file, "import java.nio.charset.StandardCharsets;\n" +
                    "something ...");

            ccrTechnique.runTool(testDir);

            assertEquals(1, testDir.listFiles().length);
            assertEquals("\nsomething ...", tfu.readFileContentToString(testDir.listFiles()[0]));
        }

        @Test
        public void realCaseExample() throws IOException {
            File file = new File("testInputData" +File.separator+"ccrTest"+File.separator+"student1.java");
            File testFile = dfu.copyFile(file,testDir);

            File expectedFile = new File("testInputData" +File.separator+"ccrTest"+File.separator+"student1_expected.java");
            ccrTechnique.runTool(testDir);
            assertEquals(tfu.readFileContentToString(expectedFile), tfu.readFileContentToString(testFile));
        }

        @Test
        public void realCaseExample2StackOverflowProblem() throws IOException {
            File file = new File("testInputData" +File.separator+"ccrTest"+File.separator+"student2.java");
            File testFile = dfu.copyFile(file,testDir);

            File expectedFile = new File("testInputData" +File.separator+"ccrTest"+File.separator+"student2_expected.java");
            ccrTechnique.runTool(testDir);
            assertEquals(tfu.readFileContentToString(expectedFile), tfu.readFileContentToString(testFile));
        }

        @Test
        public void templateExclusionRunedNowGoesRemoveCommonCode() throws IOException {
            File file = new File("testInputData" +File.separator+"ccrTest"+File.separator+"student3.java");
            File testFile = dfu.copyFile(file,testDir);

            File expectedFile = new File("testInputData" +File.separator+"ccrTest"+File.separator+"student3_expected.java");
            ccrTechnique.runTool(testDir);
            assertEquals(tfu.readFileContentToString(expectedFile), tfu.readFileContentToString(testFile));
        }
    }
}
