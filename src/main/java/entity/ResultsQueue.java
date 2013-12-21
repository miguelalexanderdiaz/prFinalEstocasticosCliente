/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;

/**
 *
 * @author migueldiaz
 */
public class ResultsQueue {
    

    private int clientNumber;
    private List<Integer> results;
    private Long finalResult;
    
    
    public ResultsQueue(List<Integer> results,int clientNumber,double arrivingRate){
        this.clientNumber=clientNumber;
        this.results=results;
        Long sum=0L;
        for(int i=0;i<results.size();i++){
            sum+=results.get(i);
        }
//        Double arrivInMilis = (1 / arrivingRate)*1000;
//        Double aux=results.size()-1;
//        sum-=aux.longValue();
        this.finalResult=sum;
    }
    


    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public List<Integer> getResults() {
        return results;
    }

    public void setResults(List<Integer> results) {
        this.results = results;
    }

    public Long getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(Long finalResult) {
        this.finalResult = finalResult;
    }
    
}
