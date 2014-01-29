package model;

import java.util.ArrayList;

public class Way {
    private final long _id;
    private final ArrayList<Node> _nodes;
    private String _nom;
    private TypeShape _type;
    public Way(long id) {
        _id = id;
        _nodes = new ArrayList<>();
        _nom = "";
        _type = TypeShape.NULL;
    }
    
    public void ajoutNode(Node n) {
        _nodes.add(n);
    }
    
    public void setType(TypeShape shape) {
        _type = shape;
    }
    
    public void setNom(String nom) {
        _nom = nom;
    }

    public long getId() {
        return _id;
    }

    public ArrayList<Node> getNodes() {
        return _nodes;
    }

    public String getNom() {
        return _nom;
    }

    public TypeShape getType() {
        return _type;
    }
}
