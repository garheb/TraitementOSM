/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author brokep
 */
public class GestionXML extends DefaultHandler{
    
    private Way _way;
    
    private HashMap<Long, Node> _nodes;
    private ArrayList<Way> _ways;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("node")) {
            long id = Long.parseLong(attributes.getValue("id"));
            double lon = Double.parseDouble(attributes.getValue("lon"));
            double lat = Double.parseDouble(attributes.getValue("lat"));
            _nodes.put(id, new Node(id, lon, lat));
        } else if(qName.equals("way")) {
            long id = Long.parseLong(attributes.getValue("id"));
            _way = new Way(id);
            _ways.add(_way);
        } else if(_way!=null && qName.equals("tag")) {
            String k = attributes.getValue("k");
            switch(k) {
                case "building": 
                    boolean building = Boolean.parseBoolean(attributes.getValue("v"));
                    if(building) {
                        _way.setType(TypeShape.BUILDING);
                    }
                    break;
                case "amenity": 
                    _way.setType(TypeShape.AMENITY);
                    break;
                case "name":
                    String nom = attributes.getValue("v");
                    if(nom.contains("Terminal")) {
                        _way.setType(TypeShape.TERMINAL);
                    } else if(nom.contains("Quai")) {
                        _way.setType(TypeShape.QUAI);
                    }
                    _way.setNom(nom);
                    break;
                case "highway": 
                    _way.setType(TypeShape.HIGHWAY);
                    break;
                case "natural": 
                    _way.setType(TypeShape.NATURAL);
                    break;
            }
        } else if(qName.equals("nd")) {
            long ref = Long.parseLong(attributes.getValue("ref"));
            Node n = _nodes.get(ref);
            _way.ajoutNode(n);
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            _way = null;
        }
    }
    
    public GestionXML() {
        _ways = new ArrayList<>();
        _nodes = new HashMap<>();
    }
    
    public ArrayList<Way> getWays() {
        return _ways;
    }
}
