package com.example.AudioStream.ModeliTunesApi;

import java.util.List;

public class Result {
    private int resultCount;
    private List<RootObject> results;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<RootObject> getResults() {
        return results;
    }

    public void setResults(List<RootObject> results) {
        this.results = results;
    }
}
