/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.webControllers;

import beans.clientQueue.SequentialController;
import entity.ResultsQueue;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import sequential.ServiceRateWS;

/**
 *
 * @author migueldiaz
 */
@ManagedBean
@SessionScoped
public class SequentialWebController {

    private int numberClients;
    private double arrivingRate;
    private int numberWebServices;
    private List<ServiceRateWS> queue = new ArrayList<ServiceRateWS>();
    private Boolean realTime;
    private SequentialController sequentialController = new SequentialController();
    private CartesianChartModel graphModel = new CartesianChartModel();
    private CartesianChartModel totalTimeGraph =new CartesianChartModel();
    private LineChartSeries prediction = new LineChartSeries();
    private LineChartSeries simulation = new LineChartSeries();
    private LineChartSeries totalPrediction = new LineChartSeries();
    private LineChartSeries totalSimulation = new LineChartSeries();
    private Integer selectedClient = 1;
    private Boolean visibleComponents = false;
    private ArrayList<ResultsQueue> resultsQueue;

    public SequentialWebController() {
        prediction.setLabel("Predicción");
        simulation.setLabel("Simulación");
        simulation.setMarkerStyle("diamond");
        graphModel.addSeries(prediction);
        graphModel.addSeries(simulation);
        
        totalPrediction.setLabel("Predicción");
        totalSimulation.setLabel("Simulación");
        totalSimulation.setMarkerStyle("diamond");
        totalTimeGraph.addSeries(totalPrediction);
        totalTimeGraph.addSeries(totalSimulation);
    }

    public CartesianChartModel getGraphModel() {
        return graphModel;
    }

    public void setGraphModel(CartesianChartModel graphModel) {
        this.graphModel = graphModel;
    }

    public void numberWebServicesChange() {
        ArrayList<ServiceRateWS> aux = new ArrayList<ServiceRateWS>();
        for (int i = 0; i < numberWebServices; i++) {
            ServiceRateWS row = new ServiceRateWS();
            row.setNumber(i + 1);
            row.setServiceRate(0);
            aux.add(row);
        }
        this.queue = aux;
    }

    public void onSlideEnd(SlideEndEvent event) {
        List<Integer> aux = resultsQueue.get(event.getValue()-1).getResults();
        for (int i = 0; i < numberWebServices; i++) {
            simulation.set(i + 1, aux.get(i));
        }
    }
    
     public void onSlideChange() {
        List<Integer> aux = resultsQueue.get(getSelectedClient()-1).getResults();
        for (int i = 0; i < numberWebServices; i++) {
            simulation.set(i + 1, aux.get(i));
        }
    }
    
    
    

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            if (event.getColumn().getHeaderText().equals("Clientes procesados por segundo")) {
                ServiceRateWS r = queue.get(event.getRowIndex());
                r.setServiceRate((Double) newValue);
                queue.set(event.getRowIndex(), r);
            } else {
                ServiceRateWS r = queue.get(event.getRowIndex());
                r.setStandarDeviation((Double) newValue);
                queue.set(event.getRowIndex(), r);
            }
        }
    }

    public void executeSimulation() {

    

            //lista de los resultados de la simulacion
            resultsQueue = sequentialController.noRealTimeSimulation(queue, arrivingRate, numberClients);
            List<Integer> simulatedResults = resultsQueue.get(selectedClient-1).getResults();
            //asignacion de los resultados simulados a las graficas web
            for (int i = 0; i < numberWebServices; i++) {
                simulation.set(i + 1, simulatedResults.get(i));
            }
            
            //lista de los resultados predichos por la formula
            List<ResultsQueue> predictionResults = sequentialController.mathPrediction(queue, arrivingRate,numberClients);
            //asignacion de los resultados predichos a las graficas web
            List<Integer> predictionRes=predictionResults.get(selectedClient-1).getResults();
            for (int i = 0; i < numberWebServices; i++) {
                prediction.set(i + 1, predictionRes.get(i));
            }
            
            //lista con acumulacion de los resultados simulados
           List<Long> totalSimulatedResults =new ArrayList<Long>();
           totalSimulatedResults.add(resultsQueue.get(0).getFinalResult());
           for(int i=1;i<resultsQueue.size();i++){
               totalSimulatedResults.add(resultsQueue.get(i).getFinalResult()+totalSimulatedResults.get(i-1));
           }
           
           //asignacion de los resultados simulados acumulados a las graficas web
           for(int i=0;i<totalSimulatedResults.size();i++){
               totalSimulation.set(i+1, totalSimulatedResults.get(i));
           }
           
           //lista con acumulacion de los resultados predichos 
           List<Long> totalPredictedResults =new ArrayList<Long>();
           totalPredictedResults.add(predictionResults.get(0).getFinalResult());
           for(int i=1;i<predictionResults.size();i++){
               totalPredictedResults.add(predictionResults.get(i).getFinalResult()+totalPredictedResults.get(i-1));
           }
           
           //asignacion de los resultados predichos acumulados a las graficas web
           for(int i=0;i<totalPredictedResults.size();i++){
               totalPrediction.set(i+1, totalPredictedResults.get(i));
           }
        visibleComponents = true;
    }

    public int getNumberClients() {
        return numberClients;
    }

    public void setNumberClients(int numberClients) {
        this.numberClients = numberClients;
        setSelectedClient(numberClients);
    }

    public double getArrivingRate() {
        return arrivingRate;
    }

    public void setArrivingRate(double arrivingRate) {
        this.arrivingRate = arrivingRate;
    }

    public int getNumberWebServices() {
        return numberWebServices;
    }

    public void setNumberWebServices(int numberWebServices) {
        this.numberWebServices = numberWebServices;
    }

    public List<ServiceRateWS> getServiceRateList() {
        return queue;
    }

    public void setServiceRateList(ArrayList<ServiceRateWS> serviceRateList) {
        this.queue = serviceRateList;
    }

    public Boolean getRealTime() {
        return realTime;
    }

    public void setRealTime(Boolean realTime) {
        this.realTime = realTime;
    }

    public Integer getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Integer selectedClient) {
        this.selectedClient = selectedClient;
    }

    public Boolean getVisibleComponents() {
        return visibleComponents;
    }

    public void setVisibleComponents(Boolean visibleComponents) {
        this.visibleComponents = visibleComponents;
    }

    public CartesianChartModel getTotalTimeGraph() {
        return totalTimeGraph;
    }

    public void setTotalTimeGraph(CartesianChartModel totalTimeGraph) {
        this.totalTimeGraph = totalTimeGraph;
    }
    
}
