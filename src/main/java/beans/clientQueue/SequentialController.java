/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.clientQueue;

import entity.ResultsQueue;
import java.util.ArrayList;
import java.util.List;
import sequential.ServiceRateWS;

/**
 *
 * @author migueldiaz
 */
public class SequentialController {

    public ArrayList<ResultsQueue> mathPrediction(List<ServiceRateWS> queue, Double arrivingRate, Integer numberClients) {
        ArrayList<ResultsQueue> resultsQueue = new ArrayList<ResultsQueue>();
        for (int i = 0; i < numberClients; i++) {
            ArrayList<Integer> results = new ArrayList<Integer>();
            for (int j = 0; j < queue.size(); j++) {
                Double aux = (1 / (queue.get(j).getServiceRate() - arrivingRate)) * 1000;
                results.add(aux.intValue());
            }
            ResultsQueue rs = new ResultsQueue(results, i + 1, arrivingRate);
            resultsQueue.add(rs);
        }
        //System.out.println("Resultados prediccion" + results);
        return resultsQueue;
    }

    public ArrayList<ResultsQueue> noRealTimeSimulation(List<ServiceRateWS> queue, Double arrivingRate, Integer numberClients) {
        try { // Call Web Service Operation
            ArrayList<ResultsQueue> resultsQueue = new ArrayList<ResultsQueue>();
            for (int i = 0; i < numberClients; i++) {
                sequential.SequentialService_Service service = new sequential.SequentialService_Service();
                sequential.SequentialService port = service.getSequentialServicePort();
                ResultsQueue rs = new ResultsQueue(port.fullSimulation(queue, arrivingRate), i + 1, arrivingRate);
                resultsQueue.add(rs);
            }


            return resultsQueue;
        } catch (Exception ex) {

            return null;
        }
    }

    public void realTimeSimulation(ArrayList<Integer> queue) {
//        try { // Call Web Service Operation
//            sequential.SequentialService_Service service = new sequential.SequentialService_Service();
//            sequential.SequentialService port = service.getSequentialServicePort();
//            // TODO initialize WS operation arguments here
//            java.lang.Integer request = Integer.valueOf(0);
//            // TODO process result here
//            java.lang.Integer result = port.byIndexSimulation(request);
//            System.out.println("Result = " + result);
//        } catch (Exception ex) {
//            // TODO handle custom exceptions here
//        }
    }
}
