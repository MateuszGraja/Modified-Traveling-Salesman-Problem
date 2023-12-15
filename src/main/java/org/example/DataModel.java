package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataModel {
    private int numberOfVertices;
    private List<Vertex> vertices = new ArrayList<>();
    private Set<IndexInfo> indexInfos = new HashSet<>();

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Set<IndexInfo> getIndexInfos() {
        return indexInfos;
    }
}
