package test;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import model.GestionXML;
import model.Node;
import model.TypeShape;
import model.Way;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Traitement {
    private final double _minX = 0.09;
    private final double _minY = 49.448;
    private final double _limiteX = 0.1900875;
    private final double _limiteY = 49.488;
    
    private final String _separateur = java.nio.file.FileSystems.getDefault().getSeparator();
    private final String _mapXML = "src" + _separateur + "data" + _separateur + "map.osm.xml";
    private final String _output = "src" + _separateur + "data" + _separateur + "map_filtree_2.json";
    
    private GestionXML _handler;
    public void lireXML() {
        _handler = new GestionXML();
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(_handler);
            try {
                parser.parse(new InputSource(new FileInputStream(_mapXML)));
                
            } catch (IOException ex) {
                Logger.getLogger(Traitement.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            FileWriter fstream;
            try {
                fstream = new FileWriter(_output);
                try (BufferedWriter out = new BufferedWriter(fstream)) {
//                    JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
//                    JsonGenerator generator = factory.createGenerator(out);
                    JsonBuilderFactory factory = Json.createBuilderFactory(null);
                    
                    JsonArrayBuilder array = factory.createArrayBuilder();
                    for(Way w:_handler.getWays()) {
                        //System.out.println(w.getNodes().size());
                        boolean fill = false;
                        boolean tracer = true;
                        boolean contientPointDansZone = false;
                        boolean tousLesPointsDansZone = true;
                        JsonObjectBuilder job = factory.createObjectBuilder();
                        
                        job.add("_id", w.getId());
                        job.add("_nom", w.getNom());
                        job.add("_type", w.getType().toString());
                        
                        if(w.getType().toString().equals(TypeShape.NATURAL.toString())) {
                            fill = true;
                        } else if(w.getType().toString().equals(TypeShape.TERMINAL.toString())) {
                            fill = true;
                        } else if(w.getType().toString().equals(TypeShape.QUAI.toString())) {
                            fill = true;
                        }
                        
                        //JsonObjectBuilder nodes = factory.createObjectBuilder();
                        JsonArrayBuilder listeCoordonnees = factory.createArrayBuilder();
                        for(Node n:w.getNodes()) {
                            boolean xOk = false;
                            boolean yOk = false;
                            JsonObjectBuilder coordonnees = factory.createObjectBuilder();;
                            coordonnees.add("x", n.getLongitude());
                            coordonnees.add("y", n.getLatitude());
                            
                            if(n.getLongitude()>_minX && n.getLongitude()<_limiteX) {
                                xOk = true;
                            }
                            
                            if(n.getLatitude()>_minY && n.getLatitude()<_limiteY) {
                                yOk = true;
                            }
                            if(xOk && yOk) {
                                contientPointDansZone = true;
                                
                            } else {
                                tousLesPointsDansZone = false;
                            }
                            listeCoordonnees.add(coordonnees);
                        }
                        if(contientPointDansZone) {
                            if(!fill) {
                                if(!tousLesPointsDansZone) {
                                    tracer = false;
                                }
                            }
                        } else {
                            tracer = false;
                        }
                        
                        job.add("_nodes", listeCoordonnees);
                        if(tracer) {
                            array.add(job);
                        }
                    }
                    JsonArray json = array.build();
                    out.write(json.toString().replace(",", ",\n"));
                }
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(Traitement.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } catch (SAXException ex) {
            Logger.getLogger(Traitement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Traitement t=new Traitement();
        t.lireXML();
    }
}
